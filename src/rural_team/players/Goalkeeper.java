package rural_team.players;

import easy_soccer_lib.PlayerCommander;
import easy_soccer_lib.utils.EFieldSide;
import easy_soccer_lib.utils.EMatchState;
import easy_soccer_lib.utils.Vector2D;

public class Goalkeeper extends RegularPlayer {

	public Goalkeeper(PlayerCommander player, double x, double y) {
		super(player, x, y);
	}
	
	@Override
	public void run() {

		// ETAPA 1
		_printf("Waiting initial perceptions...");
		selfInfo = commander.perceiveSelfBlocking();
		fieldInfo = commander.perceiveFieldBlocking();
		matchInfo = commander.perceiveMatchBlocking();

		// POSIÇÃO INICIAL
		state = PState.RETURN_TO_HOME;

		// Ajusta posição do jogador
		_printf("Starting GoalKeeperPlayer");
		_printf("X; %f    Y: f%", homebase.getX(), homebase.getY());
		// commander.doMoveBlocking(homebase);

		if (selfInfo.getFieldSide() == EFieldSide.RIGHT) { // ajusta a posicao base de acordo com o lado do jogador
															// (basta mudar os sinais)
			homebase.setX(-homebase.getX());
			homebase.setY(-homebase.getY());
		}

		commander.doMoveBlocking(homebase);

		try {
			Thread.sleep(3000); // espera, para dar tempo de ver as mensagens iniciais
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		while (commander.isActive()) {// Enquanto tiver conexão com o servidor, rodar o loop. AQUI VAI ESTAR O CODIGO
										// DA MAQUINA DE ESTADOS
			updatePerceptions(); // deixar aqui, no comeco do loop, para ler o resultado do 'doMove'

			if (matchInfo.getState() == EMatchState.PLAY_ON) {
				_printf_once("PLAY_ON");
				homebase = formation.neutralFormation(commander);
				switch (this.state) {
				case DEFENDING_GOAL:
					this.stateDefendingGoal();
					break;
				case RETURN_TO_HOME:
					this.stateReturnToHomeBase();
					break;
				default:
					break;
				}

			} else {
				matchStates(matchInfo);
			}
		}
	}
	
	@Override
	public void stateReturnToHomeBase() {
		if (closestToTheBall()) {
			state = PState.DEFENDING_GOAL;
			return;
		}

		// se não chegou na home base...
		if (!arrivedAt(homebase)) {

			if (isAlignedTo(homebase)) {
				_printf_once("RTHB: Running to the base...");
				commander.doDashBlocking(100.0d);
			} else {
				_printf("RTHB: Turning...");
				commander.doTurnToPointBlocking(homebase);
			}
		}
		// se chegou na home base: fica parado
	}
	
	protected void stateDefendingGoal() {
		if (!closestToTheBall()) {
			state = PState.RETURN_TO_HOME;
			return;
		}

		Vector2D ballPosition = fieldInfo.getBall().getPosition();

		if (arrivedAt(ballPosition)) {
			if (selfInfo.getFieldSide() == EFieldSide.LEFT) {
				_printf_once("GK: Catching the ball...");
				//commander.doCatch(MAX_PRIORITY);
				commander.doKickToPoint(100.0d, new Vector2D(52.0d, 0));//Rebater
			} else {
				_printf_once("GK: Catching the ball (right side)...");
				//commander.doCatch(MAX_PRIORITY);
				commander.doKickToPoint(100.0d, new Vector2D(52.0d, 0));//Rebater
			}

		} else {
			if (isAlignedTo(ballPosition)) {
				_printf_once("ATK: Running to the ball...");
				commander.doDashBlocking(100.0d);
			} else {
				_printf("ATK: Turning...");
				commander.doTurnToPointBlocking(ballPosition);
			}
		}
		
	}	
}
