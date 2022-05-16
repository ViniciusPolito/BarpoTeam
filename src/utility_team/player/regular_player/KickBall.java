package utility_team.player.regular_player;

import easy_soccer_lib.utils.Vector2D;
import utility_team.player.AbstractBehavior;
import utility_team.player.Player;


public class KickBall extends AbstractBehavior {

	/**
	 * C�digo adaptado (simplificado) do c�digo de ActionKickToGoal.java (do agente com behavior tree). 
	 */
	@Override
	public void perform(Player agent) {
		Vector2D ballPos = agent.fieldPerc.getBall().getPosition();
		
		if (agent.isAlignedTo(ballPos)) {
			if (agent.isCloseTo(ballPos, 1.0)) {
				//da um chute com forca maxima (100)
				//para o centro do gol -- pode ser melhorado!
				agent.commander.doKickToPoint(100.0d, agent.offensiveGoalPos);
			} else {
				//corre com forca intermediaria (porque esta perto da bola)
				agent.commander.doDashBlocking(60.0d);
			}
		} else {
			agent.commander.doTurnToPoint(ballPos);
		}

	}

	/**
	 * Esta m�trica � formada pela combina��o de v�rias medidas.
	 */
	@Override
	public double utility(Player agent) {
		double distanceAgentBall = agent.myPosition.distanceTo(agent.ballPosition);

		// MEDIDA 1 -- proximidade da bola
		// - dist�ncia <  1.5 	--> medida tem valor m�ximo (1,0)
		// - dist�ncia >= 1.5 	--> medida tem valor que decai linearmente de 1.0 a 0.0, na medida em que a dist�ncia aumenta de 1.5 a 9.5 
		double metric1; 
		if (distanceAgentBall < 1.5) {
			metric1 = 1.0;
		} else {
			metric1 = 1.0 - (distanceAgentBall - 1.5) / 8.0;  //express�o pode ser simplificada, mas pode deixar para o compilador... 
		}
		
		double distanceBallGoal = agent.ballPosition.distanceTo(agent.offensiveGoalPos);
		
		// MEDIDA 2 -- dist�ncia adequada da bola para o gol
		// - medida tem valor que decai linearmente de 1.0 a 0.0, na medida em que a dist�ncia aumenta de 10 a 30.0
		// - medida pode ter valor maior que 1.0 quando a bola est� muito pr�xima (dist�ncia < 10)
		double metric2; 
		metric2 = 1.0 - (distanceBallGoal - 10.0) / 20.0;  //express�o pode ser simplificada... 
		
		// MEDIDA 3 (sugest�o) -- qu�o longe o goleiro advers�rio est� do centro do gol (pois o agente chuta para o centro)
		// FALTA IMPLEMENTAR ! 
		double metric3 = 1.0;
		
		return 0.2*metric1 + 0.7*metric2 + 0.1*metric3;
	}


}
