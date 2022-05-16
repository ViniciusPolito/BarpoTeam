package utility_team.player.regular_player;

import easy_soccer_lib.utils.EFieldSide;
import easy_soccer_lib.utils.Vector2D;
import utility_team.player.AbstractBehavior;
import utility_team.player.Player;


public class GetBall extends AbstractBehavior {

	/**
	 * Código adaptado (simplificado) do código de ActionGoGetBall.java (do agente com behavior tree). 
	 */
	@Override
	public void perform(Player agent) {
		Vector2D ballPos = agent.ballPosition;
		
		if (agent.isCloseTo(ballPos, 1.0)) {
			//faz nada (situação desejada)
			//a partir daqui, talvez ele seja disparado um AdvanceWithBall ou um KickBall
		} else if (agent.isAlignedTo(ballPos)) {
			agent.commander.doDashBlocking(100.0d);
		} else {
			agent.commander.doTurnToPoint(ballPos);
		}

	}

	/**
	 * Esta métrica é formada pela combinação de várias medidas.
	 */
	@Override
	public double utility(Player agent) {
		double distanceBallDefensiveGoal = agent.ballPosition.distanceTo(agent.defensiveGoalPos);
		
		if(agent.getPlayerClosestToBall(agent.mySide).getUniformNumber() == 1) {
			return 5.0;
		}
		
		if (!agent.isPlayerClosestToBallInTeam() || agent.isCloseTo(agent.ballPosition,1.0)) {
			return -1.0;
		}
		
		

		// MEDIDA 1 -- medida de perigo de sofrer gol (quão próxima a bola está do meu gol)
		// - distância <  10.0 	--> medida tem valor máximo (1,0)
		// - distância >= 10.0 	--> medida tem valor que desce linearmente de 1.0 a 0.0, na medida em que a distância aumenta de 10.0 para 50.0 (aprox. meio do campo) 
		double metric1; 
		if (distanceBallDefensiveGoal < 10.0) {
			metric1 = 1.0;
		} else {
			metric1 = 1.0 - (distanceBallDefensiveGoal - 10.0) / 40.0;  //expressão pode ser simplificada, mas pode deixar para o compilador... 
		}
		
		EFieldSide mySide = agent.mySide;
		EFieldSide advSide = agent.mySide.opposite();
		double distMyTeamToBall  = agent.getPlayerClosestToBall(mySide).distanceTo(agent.ballPosition);
		double distAdvTeamToBall = agent.getPlayerClosestToBall(advSide).distanceTo(agent.ballPosition);
	
		// MEDIDA 2 -- medida de perigo do adversário dominar a bola
		// - proporção = 1.0  --> adversário a uma mesma distância que meu time
		// - proporção > 1.0  --> adversário está mais perto da bola do que o meu time, a utilidade desta ação fica mais alta! 
		
		double metric2 = distMyTeamToBall / distAdvTeamToBall;  // FAZER: criar constante 0.0001 para evitar divisão por 0
		
		return 0.4*metric1 + 0.6*metric2;
	}


}
