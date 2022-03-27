package rural_team.players;

import easy_soccer_lib.PlayerCommander;
import easy_soccer_lib.utils.EFieldSide;
import easy_soccer_lib.utils.EMatchState;

public class StrikerPlayer extends RegularPlayer {

	public StrikerPlayer(PlayerCommander player, double x, double y) {
		super(player, x, y);
		// TODO Auto-generated constructor stub
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
		_printf("Starting StrikerPlayer");
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

				if (matchInfo.getTeamScore(EFieldSide.LEFT) > matchInfo.getTeamScore(EFieldSide.RIGHT)) {
					if (selfInfo.getFieldSide() == EFieldSide.LEFT) {
						homebase = formation.defendingFormation(commander);
					} else {
						homebase = formation.attackingFormation(commander);
					}

				} else if (matchInfo.getTeamScore(EFieldSide.LEFT) < matchInfo.getTeamScore(EFieldSide.RIGHT)) {
					if (selfInfo.getFieldSide() == EFieldSide.LEFT) {
						homebase = formation.attackingFormation(commander);
					} else {
						homebase = formation.defendingFormation(commander);
					}

				} else {
					homebase = formation.neutralFormation(commander);
				}

				switch (this.state) {
				case ATTACKING:
					this.stateAttacking();
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
}
