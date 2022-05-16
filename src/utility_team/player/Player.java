package utility_team.player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import easy_soccer_lib.PlayerCommander;
import easy_soccer_lib.perception.FieldPerception;
import easy_soccer_lib.perception.MatchPerception;
import easy_soccer_lib.perception.PlayerPerception;
import easy_soccer_lib.utils.EFieldSide;
import easy_soccer_lib.utils.EMatchState;
import easy_soccer_lib.utils.Vector2D;

public class Player extends Thread {
	// atributos com acesso de pacote: para serem acessados pelos behaviors
	public final PlayerCommander commander;

	// percepções vindas do commander
	public PlayerPerception selfPerc;
	public FieldPerception fieldPerc;

	// percepções mais detalhadas (extraídas das duas acima)
	Vector2D myDirection;
	public Vector2D myPosition;
	public EFieldSide mySide;
	public Vector2D ballPosition;
	ArrayList<PlayerPerception> playersMyTeam;
	public ArrayList<PlayerPerception> playerOtherTeam;

	Vector2D homePosition;
	public Vector2D offensiveGoalPos;
	public Vector2D defensiveGoalPos;

	protected List<AbstractBehavior> behaviors = new ArrayList<AbstractBehavior>();

	// Atributos incluidos pelo aluno
	public MatchPerception matchInfo; // Percepção da partida
	EMatchState state; // Estado da partida
	private static final double ERROR_RADIUS = 1.2d;// Alcance do jogador

	//

	public Player(PlayerCommander player, Vector2D home) {
		commander = player;
		homePosition = home;

		// adiciona behaviors -- ordem não importa
		behaviors.add(new MatchStates());
	}

	@Override
	public void run() {
		System.out.println(">> 1. Waiting initial perceptions...");
		updatePerceptions(true);

		if (selfPerc.getFieldSide() == EFieldSide.LEFT) {
			offensiveGoalPos = new Vector2D(52.0d, 0);
			defensiveGoalPos = new Vector2D(-52.0d, 0);
		} else {
			offensiveGoalPos = new Vector2D(-52.0d, 0);
			defensiveGoalPos = new Vector2D(52.0d, 0);
			// homePosition.setX(- homePosition.getX());
			// homePosition.setY(- homePosition.getY());
		}

		System.out.println(">> 2. Moving to initial position...");
		commander.doMoveBlocking(this.homePosition);

		System.out.println(">> 3. Now starting...");

		int iterationsRemaining = 0;
		AbstractBehavior selectedBehavior = null;
		double bestUtility;

		while (commander.isActive()) {
			updatePerceptions(false); // non-blocking

			// selecionar o melhor comportamento, quando acabarem as iterações restantes
			if (iterationsRemaining == 0) {
				// percorre todos os comportamentos e seleciona o de maior utility
				bestUtility = -2000.0f;
				double utility;

				_printf("Selecting behavior...");
				for (AbstractBehavior b : this.behaviors) {
					utility = b.utility(this);
					_printf(" - behavior %s -- u=%.2f", b, utility);
					if (utility > bestUtility) {
						bestUtility = utility;
						selectedBehavior = b;
					}
				}
				_printf("Selected behavior: %s", selectedBehavior);

				iterationsRemaining = 20;
			}

			// executa o comportamento selecionado, em toda iteração
			selectedBehavior.perform(this);
			iterationsRemaining--;

			try {
				sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}

		System.out.println(">> 4. Terminated!");
	}

	private void updatePerceptions(boolean blocking) {
		PlayerPerception newSelf;
		FieldPerception newField;
		MatchPerception newMatch;

		if (blocking) {
			newSelf = commander.perceiveSelfBlocking();
			newField = commander.perceiveFieldBlocking();
			newMatch = commander.perceiveMatch();// Aluno Adicionou

		} else {
			newSelf = commander.perceiveSelf();
			newField = commander.perceiveField();
			newMatch = commander.perceiveMatch();// Aluno Adicionou
		}

		if (newSelf != null) {
			this.selfPerc = newSelf;
			// percepções mais detalhadas
			this.myPosition = newSelf.getPosition();
			this.myDirection = newSelf.getDirection();
			this.mySide = newSelf.getFieldSide();
		}
		if (newField != null) {
			this.fieldPerc = newField;
			// percepções mais detalhadas
			this.ballPosition = newField.getBall().getPosition();
			this.playersMyTeam = newField.getTeamPlayers(this.mySide);
			this.playerOtherTeam = newField.getTeamPlayers(this.mySide.opposite());
		}
		if (newMatch != null) {
			this.matchInfo = newMatch;
			// percepções mais detalhadas
		}
	}

	/** My Functions **/
	public int closestTeamPlayer(int numberPlayer) {
		// Jogadores do meu time
		List<PlayerPerception> myTeam = fieldPerc.getTeamPlayers(selfPerc.getFieldSide());
		double pDist;
		double minDistance = 2000d;
		PlayerPerception playerMinDistance = null;
		for (PlayerPerception p : myTeam) {
			pDist = p.getPosition().distanceTo(selfPerc.getPosition()); // DISTANCIA DO JOGADOR
			if (p.getUniformNumber() == selfPerc.getUniformNumber()) {
			} else {
				if (pDist < minDistance) {
					minDistance = pDist;
					playerMinDistance = p;
				}
			}
		}
		return playerMinDistance.getUniformNumber();
	}

	// KICK OFF
	void kickOffLeft(int jogador1, int jogador2) {
		
		if (selfPerc.getFieldSide() == EFieldSide.LEFT) {
			if (selfPerc.getUniformNumber() == jogador1) {
				homePosition = new Vector2D(0, 0);
			} else if (selfPerc.getUniformNumber() == jogador2) {
				homePosition = new Vector2D(-1, 16);
			} else {
				// Jogador fica parado no kickoff
			}
			walkTo(homePosition, 70);
			if (selfPerc.getUniformNumber() == jogador1) {
				// Retorna percepção do jogador que receberá o passe
				PlayerPerception p = fieldPerc.getTeamPlayer(EFieldSide.LEFT, jogador2);
				// Chuta para posição do jogador que receberá o passe
				commander.doKickToPoint(50.0d, p.getPosition());
			}
		}
	}

	void kickOffRight(int jogador1, int jogador2) {
		if (selfPerc.getFieldSide() == EFieldSide.RIGHT) {
			if (selfPerc.getUniformNumber() == jogador1) {
				homePosition = new Vector2D(0, 0);
			} else if (selfPerc.getUniformNumber() == jogador2) {
				homePosition = new Vector2D(1, 8);
			} else {
				// Jogador fica parado no kickoff
			}
			walkTo(homePosition, 70);
			if (selfPerc.getUniformNumber() == jogador1) {
				// Retorna percepção do jogador que receberá o passe
				PlayerPerception p = fieldPerc.getTeamPlayer(EFieldSide.RIGHT, jogador2);
				// Chuta para posição do jogador que receberá o passe
				commander.doKickToPoint(50.0d, p.getPosition());
			}
		}
	}

	// FALTA
	void freeKickLeft() {
		if (selfPerc.getFieldSide() == EFieldSide.LEFT) {
			if (theClosestToTheBall(1)) {
				Vector2D ballPos = fieldPerc.getBall().getPosition();
				walkTo(ballPos, 70);
				autoPassing();
			}
		}
	}

	void freeKickRight() {
		if (selfPerc.getFieldSide() == EFieldSide.RIGHT) {
			if (theClosestToTheBall(1)) {
				Vector2D ballPos = fieldPerc.getBall().getPosition();
				walkTo(ballPos, 70);
				autoPassing();
			}
		}
	}

	// LATERAL, jogador adversario chuta bola para fora
	void kickInLeft() {
		Vector2D ballPos = fieldPerc.getBall().getPosition();
		if (selfPerc.getFieldSide() == EFieldSide.LEFT) {
			if (theClosestToTheBall(1)) {
				homePosition = ballPos;
				walkTo(homePosition, 70);
				PlayerPerception p = fieldPerc.getTeamPlayer(selfPerc.getFieldSide(),
						closestTeamPlayer(selfPerc.getUniformNumber()));
				commander.doKickToPoint(70.0d, p.getPosition());
			} else if (theClosestToTheBall(2)) {
				homePosition = ballPos;
				walkTo(homePosition, 50);
			}
		}
	}

	void kickInRight() {
		Vector2D ballPos = fieldPerc.getBall().getPosition();
		if (selfPerc.getFieldSide() == EFieldSide.RIGHT) {
			if (theClosestToTheBall(1)) {
				homePosition = ballPos;
				walkTo(homePosition, 70);
				PlayerPerception p = fieldPerc.getTeamPlayer(selfPerc.getFieldSide(),
						closestTeamPlayer(selfPerc.getUniformNumber()));
				commander.doKickToPoint(70.0d, p.getPosition());
			} else if (theClosestToTheBall(2)) {
				homePosition = ballPos;
				walkTo(homePosition, 50);
			}
		}
	}

	// ESCANTEIO, meu jogador chuta bola para trás do meu gol
	void cornerKickLeft() {
		Vector2D ballPos = fieldPerc.getBall().getPosition();
		if (selfPerc.getFieldSide() == EFieldSide.LEFT) {
			if (theClosestToTheBall(1)) {
				homePosition = ballPos;
				walkTo(homePosition, 50);
				commander.doKickToPoint(100.0d, new Vector2D(41, 0));
			} else if (theClosestToTheBall(2)) {
				homePosition = new Vector2D(41, 0);
				walkTo(homePosition, 100);
			}
		}
	}

	void cornerKickRight() {
		Vector2D ballPos = fieldPerc.getBall().getPosition();
		if (selfPerc.getFieldSide() == EFieldSide.RIGHT) {
			if (theClosestToTheBall(1)) {
				homePosition = ballPos;
				walkTo(homePosition, 50);
				commander.doKickToPoint(70.0d, new Vector2D(-41, 0));
			} else if (theClosestToTheBall(2)) {
				homePosition = new Vector2D(-41, 0);
				walkTo(homePosition, 100);
			}
		}
	}

	// TIRO DE META, jogador adversario chuta bola para atrás do meu gol
	void goalKickLeft() {
		Vector2D ballPos = fieldPerc.getBall().getPosition();
		if (selfPerc.getFieldSide() == EFieldSide.LEFT) {
			if (selfPerc.isGoalie()) {
				homePosition = ballPos;
				walkTo(homePosition, 50);
				PlayerPerception p = fieldPerc.getTeamPlayer(selfPerc.getFieldSide(),
						closestTeamPlayer(selfPerc.getUniformNumber()));
				commander.doKickToPoint(70.0d, p.getPosition());
			}
		}
	}

	void goalKickRight() {
		Vector2D ballPos = fieldPerc.getBall().getPosition();
		if (selfPerc.getFieldSide() == EFieldSide.RIGHT) {
			if (selfPerc.isGoalie()) {
				homePosition = ballPos;
				walkTo(homePosition, 50);
				PlayerPerception p = fieldPerc.getTeamPlayer(selfPerc.getFieldSide(),
						closestTeamPlayer(selfPerc.getUniformNumber()));
				commander.doKickToPoint(70.0d, p.getPosition());
			}
		}
	}

	// OUTRAS
	void autoPassing() {
		if (closestToTheBall()) {
			// Retorna percepção do jogador que receberá o passe, o jogador mais proximo
			// dele
			PlayerPerception p = fieldPerc.getTeamPlayer(selfPerc.getFieldSide(),
					closestTeamPlayer(selfPerc.getUniformNumber()));
			// Chuta para posição do jogador que receberá o passe
			commander.doKickToPoint(70.0d, p.getPosition());
		}
	}

	void walkTo(Vector2D place, double speed) {
		// se não chegou na home base...
		if (!arrivedAt(place)) {

			if (isAlignedTo(place)) {
				_printf_once("RTHB: Running to the base...");
				commander.doDashBlocking(speed);
			} else {
				_printf("RTHB: Turning...");
				commander.doTurnToPointBlocking(place);
			}
		}
		// se chegou na home base: fica parado
	}

	boolean closestToTheBall() {
		Vector2D ballPos = fieldPerc.getBall().getPosition();
		List<PlayerPerception> myTeam = fieldPerc.getTeamPlayers(this.commander.getMyFieldSide());
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
		return selfPerc.getUniformNumber() == playerMinDistance.getUniformNumber();
	}

	boolean theClosestToTheBall(int player) {
		Vector2D ballPos = fieldPerc.getBall().getPosition();
		List<PlayerPerception> myTeam = fieldPerc.getTeamPlayers(this.commander.getMyFieldSide());
		List<Double> list = new ArrayList<Double>();
		double pDist;
		for (PlayerPerception p : myTeam) {
			pDist = p.getPosition().distanceTo(ballPos);
			list.add(pDist);
		}
		Collections.sort(list);
		return list.get(player - 1) == selfPerc.getPosition().distanceTo(ballPos);
	}

	/** Algumas funcoes auxiliares que mais de um tipo de behavior pode precisar **/
	boolean isCloseTo(Vector2D pos) {
		return isCloseTo(pos, 1.0);
	}

	public boolean isCloseTo(Vector2D pos, double minDistance) {
		// Vector2D myPos = selfPerc.getPosition();
		// return pos.distanceTo(myPos) < minDistance;
		return myPosition.distanceTo(pos) < minDistance;
	}

	public boolean arrivedAt(Vector2D targetPosition) {
		Vector2D myPos = selfPerc.getPosition();
		return Vector2D.distance(myPos, targetPosition) <= ERROR_RADIUS;
	}

	public boolean isAlignedTo(Vector2D position) {
		return isAlignedTo(position, 12.0);
	}

	boolean isAlignedTo(Vector2D position, double minAngle) {
		if (minAngle < 0)
			minAngle = -minAngle;

		Vector2D myPos = selfPerc.getPosition();

		if (position == null || myPos == null) {
			return false;
		}

		// double angle = selfPerc.getDirection().angleFrom(position.sub(myPos));
		double angle = myDirection.angleFrom(position.sub(myPosition));
		return angle < minAngle && angle > -minAngle;
	}

	// diz se é o jogador mais perto, dentre TODOS
	boolean isPlayerClosestToBall() {
		PlayerPerception closestPlayer = getPlayerClosestToBall();
		return closestPlayer.getUniformNumber() == selfPerc.getUniformNumber();
	}
	
	// diz se é o jogador mais perto,considerando apenas o time dele
	public boolean isPlayerClosestToBallInTeam() {
		PlayerPerception closestPlayer = getPlayerClosestToBall(mySide);
		return closestPlayer.getUniformNumber() == selfPerc.getUniformNumber();
	}

	public PlayerPerception getPlayerClosestToBall(EFieldSide side) {
		PlayerPerception closestPlayer = null;
		double minDistance = 2000.0d;
		double dist;
		for (PlayerPerception p : this.fieldPerc.getTeamPlayers(side)) {
			dist = p.getPosition().distanceTo(ballPosition);
			if (dist < minDistance) {
				minDistance = dist;
				closestPlayer = p;
			}
		}
		return closestPlayer;
	}

	public PlayerPerception getPlayerClosestToBall() {
		PlayerPerception closestPlayer = null;
		double minDistance = 2000.0d;
		double dist;
		for (PlayerPerception p : fieldPerc.getAllPlayers()) {
			dist = p.getPosition().distanceTo(ballPosition);
			if (dist < minDistance) {
				minDistance = dist;
				closestPlayer = p;
			}
		}
		return closestPlayer;
	}

	// for debugging
	public void _printf_once(String format, Object... objects) {
		if (!format.equals(lastformat)) {
			_printf(format, objects);
		}
	}

	private String lastformat = "";

	public void _printf(String format, Object... objects) {
		String playerInfo = "";
		if (selfPerc != null) {
			playerInfo += "[" + selfPerc.getTeam() + "/" + selfPerc.getUniformNumber() + "] ";
		}
		System.out.printf(playerInfo + format + "%n", objects);
		lastformat = format;
	}

}
