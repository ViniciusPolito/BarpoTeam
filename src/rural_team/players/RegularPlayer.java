package rural_team.players;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import easy_soccer_lib.PlayerCommander;
import easy_soccer_lib.perception.FieldPerception;
import easy_soccer_lib.perception.MatchPerception;
import easy_soccer_lib.perception.PlayerPerception;
import easy_soccer_lib.utils.EFieldSide;
import easy_soccer_lib.utils.EMatchState;
import easy_soccer_lib.utils.Vector2D;
import rural_team.formations.Formations;

enum PState {
	RETURN_TO_HOME, ATTACKING, ATTACKING_RUN, DEFENDING, DEFENDING_GOAL // APENAS PARA O GOLEIRO
};

public class RegularPlayer extends Thread {
	private static final double ERROR_RADIUS = 1.2d;// Alcance do jogador

	protected PlayerCommander commander;
	protected PState state;

	protected PlayerPerception selfInfo; // Percepção do jogador
	protected FieldPerception fieldInfo; // Percepção do campo
	protected MatchPerception matchInfo; // Percepção da partida

	public Formations formation = new Formations();
	protected Vector2D homebase; // posicao base do jogador
	
	public RegularPlayer(PlayerCommander player, double x, double y) {
		commander = player;
		homebase = new Vector2D(x, y);
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
		_printf("Starting RegularPlayer");
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

	protected void updatePerceptions() {
		PlayerPerception newSelf = commander.perceiveSelf();
		FieldPerception newField = commander.perceiveField();
		MatchPerception newMatch = commander.perceiveMatch();

		// so atualiza os atributos se tiver nova percepcao (senao, mantem as percepcoes
		// antigas)
		if (newSelf != null) {
			this.selfInfo = newSelf;
		}
		if (newField != null) {
			this.fieldInfo = newField;
		}
		if (newMatch != null) {
			this.matchInfo = newMatch;
		}
	}

	protected void matchStates(MatchPerception matchInfo) {

		switch (matchInfo.getState()) {
		case BEFORE_KICK_OFF:
			_printf_once("BEFORE_KICK_OFF");
			// Não faz nada
			break;
		case TIME_OVER:
			_printf_once("TIME_OVER");
			this.stateReturnToHomeBase();
			break;
		case KICK_OFF_LEFT:
			_printf_once("KICK_OFF_LEFT");
			kickOffLeft(7, 6);
			break;
		case KICK_OFF_RIGHT:
			_printf_once("KICK_OFF_RIGHT");
			kickOffRight(6, 7);
			break;
		case KICK_IN_LEFT:
			_printf_once("KICK_IN_LEFT");
			kickInLeft();
			break;
		case KICK_IN_RIGHT:
			_printf_once("KICK_IN_RIGHT");
			kickInRight();
			break;
		case FREE_KICK_LEFT:
			_printf_once("FREE_KICK_LEFT");
			freeKickLeft();
			break;
		case FREE_KICK_RIGHT:
			_printf_once("FREE_KICK_RIGHT");
			freeKickRight();
			break;
		case CORNER_KICK_LEFT:
			_printf_once("CORNER_KICK_LEFT");
			cornerKickLeft();
			break;
		case CORNER_KICK_RIGHT:
			_printf_once("CORNER_KICK_RIGHT");
			cornerKickRight();
			break;
		case GOAL_KICK_LEFT:
			_printf_once("GOAL_KICK_LEFT");
			goalKickLeft();
			break;
		case GOAL_KICK_RIGHT:
			_printf_once("GOAL_KICK_RIGHT");
			goalKickRight();
			break;
		case AFTER_GOAL_LEFT:
			_printf_once("AFTER_GOAL_LEFT");
			homebase = formation.initalHexaFormation(commander);
			this.stateReturnToHomeBase();// RETURN TO HOME
			break;
		case AFTER_GOAL_RIGHT:
			_printf_once("AFTER_GOAL_RIGHT");
			homebase = formation.initalHexaFormation(commander);
			this.stateReturnToHomeBase();// RETURN TO HOME
			break;
		case FREE_KICK_FAULT_LEFT:
			_printf_once("FREE_KICK_FAULT_LEFT");
			freeKickLeft();
			break;
		case FREE_KICK_FAULT_RIGHT:
			_printf_once("FREE_KICK_FAULT_RIGHT");
			freeKickRight();
			break;
		default:
			_printf_once("SWITCH OTHERS CONDITIONS");
			break;
		}
	}
	
	////// RETURN_TO_HOME_BASE ///////
	public void stateReturnToHomeBase() {
		if (closestToTheBall()) {
			state = PState.ATTACKING;
			return;
		}
		// se não chegou na home base...
		if (!arrivedAt(homebase)) {

			if (isAlignedTo(homebase)) {
				_printf_once("RTHB: Running to the base...");
				commander.doDashBlocking(60.0d);
			} else {
				_printf("RTHB: Turning...");
				commander.doTurnToPointBlocking(homebase);
			}
		}
		// se chegou na home base: fica parado
	}

	/////// ATTACKING ///////
	protected void stateAttacking() {
		if (!closestToTheBall()) {
			state = PState.RETURN_TO_HOME;
			return;
		}
		Vector2D ballPosition = fieldInfo.getBall().getPosition();
		Random rand = new Random();
		int n = rand.nextInt(12);
		if (arrivedAt(ballPosition)) {
			if (selfInfo.getFieldSide() == EFieldSide.LEFT) {
				_printf_once("ATK: Kicking the ball...");
				commander.doKickToPoint(100.0d, new Vector2D(52.0d, (n - 6)));
			} else {
				_printf_once("ATK: Kicking the ball (right side)...");
				commander.doKickToPoint(100.0d, new Vector2D(-52.0d, (n - 6)));
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

	/////// DEFENDING ///////
	protected void stateDefending() {
		if (!closestToTheBall()) {
			state = PState.RETURN_TO_HOME;
			return;
		}
		Vector2D ballPosition = fieldInfo.getBall().getPosition();
		if (arrivedAt(ballPosition)) {
			if (selfInfo.getFieldSide() == EFieldSide.LEFT) {
				_printf_once("DEF: Kicking the ball...");
				autoPassing(); // PASSAR BOLA PARA JOGADOR MAIS PERTO
			} else {
				_printf_once("DEF: Kicking the ball (right side)...");
				autoPassing(); // PASSAR BOLA PARA JOGADOR MAIS PERTO
			}
		} else {
			if (isAlignedTo(ballPosition)) {
				_printf_once("DEF: Running to the ball...");
				commander.doDashBlocking(100.0d);
			} else {
				_printf("DEF: Turning...");
				commander.doTurnToPointBlocking(ballPosition);
			}
		}
	}

	/////// AUXILIAR FUNCTIONS (PlayerCommander) ///////

	// KICK OFF
	public void kickOffLeft(int jogador1, int jogador2) {
		if (selfInfo.getFieldSide() == EFieldSide.LEFT) {
			if (selfInfo.getUniformNumber() == jogador1) {
				homebase = new Vector2D(0, 0);
			} else if (selfInfo.getUniformNumber() == jogador2) {
				homebase = new Vector2D(-1, 8);
			} else {
				// Jogador fica parado no kickoff
			}
			walkTo(homebase, 70);
			if (selfInfo.getUniformNumber() == jogador1) {
				// Retorna percepção do jogador que receberá o passe
				PlayerPerception p = fieldInfo.getTeamPlayer(EFieldSide.LEFT, jogador2);
				// Chuta para posição do jogador que receberá o passe
				commander.doKickToPoint(50.0d, p.getPosition());
			}
		}
	}

	public void kickOffRight(int jogador1, int jogador2) {
		if (selfInfo.getFieldSide() == EFieldSide.RIGHT) {
			if (selfInfo.getUniformNumber() == jogador1) {
				homebase = new Vector2D(0, 0);
			} else if (selfInfo.getUniformNumber() == jogador2) {
				homebase = new Vector2D(1, 8);
			} else {
				// Jogador fica parado no kickoff
			}
			walkTo(homebase, 70);
			if (selfInfo.getUniformNumber() == jogador1) {
				// Retorna percepção do jogador que receberá o passe
				PlayerPerception p = fieldInfo.getTeamPlayer(EFieldSide.RIGHT, jogador2);
				// Chuta para posição do jogador que receberá o passe
				commander.doKickToPoint(50.0d, p.getPosition());
			}
		}
	}
	// FALTA
	protected void freeKickLeft() {
		if (selfInfo.getFieldSide() == EFieldSide.LEFT) {
			if (theClosestToTheBall(1)) {
				Vector2D ballPos = fieldInfo.getBall().getPosition();
				walkTo(ballPos, 70);
				autoPassing();
			}
		}
	}
	protected void freeKickRight() {
		if (selfInfo.getFieldSide() == EFieldSide.RIGHT) {
			if (theClosestToTheBall(1)) {
				Vector2D ballPos = fieldInfo.getBall().getPosition();
				walkTo(ballPos, 70);
				autoPassing();
			}
		}
	}
	// LATERAL, jogador adversario chuta bola para fora
	protected void kickInLeft() {
		Vector2D ballPos = fieldInfo.getBall().getPosition();
		if (selfInfo.getFieldSide() == EFieldSide.LEFT) {
			if (theClosestToTheBall(1)) {
				homebase = ballPos;
				walkTo(homebase, 70);
				PlayerPerception p = fieldInfo.getTeamPlayer(selfInfo.getFieldSide(),
						closestTeamPlayer(selfInfo.getUniformNumber()));
				commander.doKickToPoint(70.0d, p.getPosition());
			} else if (theClosestToTheBall(2)) {
				homebase = ballPos;
				walkTo(homebase, 50);
			}
		}
	}
	protected void kickInRight() {
		Vector2D ballPos = fieldInfo.getBall().getPosition();
		if (selfInfo.getFieldSide() == EFieldSide.RIGHT) {
			if (theClosestToTheBall(1)) {
				homebase = ballPos;
				walkTo(homebase, 70);
				PlayerPerception p = fieldInfo.getTeamPlayer(selfInfo.getFieldSide(),
						closestTeamPlayer(selfInfo.getUniformNumber()));
				commander.doKickToPoint(70.0d, p.getPosition());
			} else if (theClosestToTheBall(2)) {
				homebase = ballPos;
				walkTo(homebase, 50);
			}
		}
	}
	// ESCANTEIO, meu jogador chuta bola para trás do meu gol
	protected void cornerKickLeft() {
		Vector2D ballPos = fieldInfo.getBall().getPosition();
		if (selfInfo.getFieldSide() == EFieldSide.LEFT) {
			if (theClosestToTheBall(1)) {
				homebase = ballPos;
				walkTo(homebase, 50);
				commander.doKickToPoint(100.0d, new Vector2D(41, 0));
			} else if (theClosestToTheBall(2)) {
				homebase = new Vector2D(41, 0);
				walkTo(homebase, 100);
			}
		}
	}
	protected void cornerKickRight() {
		Vector2D ballPos = fieldInfo.getBall().getPosition();
		if (selfInfo.getFieldSide() == EFieldSide.RIGHT) {
			if (theClosestToTheBall(1)) {
				homebase = ballPos;
				walkTo(homebase, 50);
				commander.doKickToPoint(70.0d, new Vector2D(-41, 0));
			} else if (theClosestToTheBall(2)) {
				homebase = new Vector2D(-41, 0);
				walkTo(homebase, 100);
			}
		}
	}
	// TIRO DE META, jogador adversario chuta bola para atrás do meu gol
	protected void goalKickLeft() {
		Vector2D ballPos = fieldInfo.getBall().getPosition();
		if (selfInfo.getFieldSide() == EFieldSide.LEFT) {
			if (selfInfo.isGoalie()) {
				homebase = ballPos;
				walkTo(homebase, 50);
				PlayerPerception p = fieldInfo.getTeamPlayer(selfInfo.getFieldSide(),
						closestTeamPlayer(selfInfo.getUniformNumber()));
				commander.doKickToPoint(70.0d, p.getPosition());
			}
		}
	}
	protected void goalKickRight() {
		Vector2D ballPos = fieldInfo.getBall().getPosition();
		if (selfInfo.getFieldSide() == EFieldSide.RIGHT) {
			if (selfInfo.isGoalie()) {
				homebase = ballPos;
				walkTo(homebase, 50);
				PlayerPerception p = fieldInfo.getTeamPlayer(selfInfo.getFieldSide(),
						closestTeamPlayer(selfInfo.getUniformNumber()));
				commander.doKickToPoint(70.0d, p.getPosition());
			}
		}
	}
	protected void walkTo(Vector2D place, double speed) {
		// se não chegou na home base...
		if (!arrivedAt(place)) {

			if (isAlignedTo(place)) {
				_printf_once("RTHB: Running to the base...");
				commander.doDashBlocking(speed);
			} else {
				_printf("RTHB: Turning...");
				commander.doTurnToPointBlocking(homebase);
			}
		}
		// se chegou na home base: fica parado
	}
	public void autoPassing() {
		if (closestToTheBall()) {
			// Retorna percepção do jogador que receberá o passe, o jogador mais proximo
			// dele
			PlayerPerception p = fieldInfo.getTeamPlayer(selfInfo.getFieldSide(),
					closestTeamPlayer(selfInfo.getUniformNumber()));
			// Chuta para posição do jogador que receberá o passe
			commander.doKickToPoint(70.0d, p.getPosition());
		}
	}
	/////// AUXILIAR FUNCTIONS (NO PlayerCommander) ///////
	private int closestTeamPlayer(int numberPlayer) {
		// Jogadores do meu time
		List<PlayerPerception> myTeam = fieldInfo.getTeamPlayers(selfInfo.getFieldSide());
		double pDist;
		double minDistance = 2000d;
		PlayerPerception playerMinDistance = null;
		for (PlayerPerception p : myTeam) {
			pDist = p.getPosition().distanceTo(selfInfo.getPosition()); // DISTANCIA DO JOGADOR
			if (p.getUniformNumber() == selfInfo.getUniformNumber()) {
			} else {
				if (pDist < minDistance) {
					minDistance = pDist;
					playerMinDistance = p;
				}
			}
		}
		return playerMinDistance.getUniformNumber();
	}
	protected boolean closestToTheBall() {
		Vector2D ballPos = fieldInfo.getBall().getPosition();
		List<PlayerPerception> myTeam = fieldInfo.getTeamPlayers(this.commander.getMyFieldSide());
		// ou: fieldInfo.getTeamPlayers( selfInfo.getFieldSide() );

		double pDist;
		double minDistance = 2000d;
		PlayerPerception playerMinDistance = null;

		for (PlayerPerception p : myTeam) {
			pDist = p.getPosition().distanceTo(ballPos);
			if (pDist < minDistance) {
				minDistance = pDist;
				playerMinDistance = p;
			}
		}
		return selfInfo.getUniformNumber() == playerMinDistance.getUniformNumber();
	}
	protected boolean theClosestToTheBall(int player) {
		Vector2D ballPos = fieldInfo.getBall().getPosition();
		List<PlayerPerception> myTeam = fieldInfo.getTeamPlayers(this.commander.getMyFieldSide());
		List<Double> list = new ArrayList<Double>();
		double pDist;
		for (PlayerPerception p : myTeam) {
			pDist = p.getPosition().distanceTo(ballPos);
			list.add(pDist);
		}
		Collections.sort(list);
		return list.get(player - 1) == selfInfo.getPosition().distanceTo(ballPos);
	}
	protected int numbClosestToTheBall() {
		Vector2D ballPos = fieldInfo.getBall().getPosition();
		List<PlayerPerception> myTeam = fieldInfo.getTeamPlayers(this.commander.getMyFieldSide());
		double pDist;
		double minDistance = 2000d;
		PlayerPerception playerMinDistance = null;
		for (PlayerPerception p : myTeam) {
			pDist = p.getPosition().distanceTo(ballPos);
			if (pDist < minDistance) {
				minDistance = pDist;
				playerMinDistance = p;
			}
		}
		return playerMinDistance.getUniformNumber();
	}
	protected boolean arrivedAt(Vector2D targetPosition) {
		Vector2D myPos = selfInfo.getPosition();
		return Vector2D.distance(myPos, targetPosition) <= ERROR_RADIUS;
	}
	protected boolean isAlignedTo(Vector2D targetPosition) {
		Vector2D myPos = selfInfo.getPosition();
		double angle = selfInfo.getDirection().angleFrom(targetPosition.sub(myPos));
		return angle > -15.0d && angle < 15.0d;
	}
	// FOR DEBUGGING
	// Printa uma vez no loop
	public void _printf_once(String format, Object... objects) {
		if (!format.equals(lastformat)) { // dependendo, pode usar ==
			_printf(format, objects);
		}
	}
	private String lastformat = "";
	// Printa adicionando o time e o jogador
	public void _printf(String format, Object... objects) {
		String playerInfo = "";
		if (selfInfo != null) {
			playerInfo += "[" + selfInfo.getTeam() + "/" + selfInfo.getUniformNumber() + "] ";
		}
		System.out.printf(playerInfo + format + "%n", objects);
		lastformat = format;
	}
}
