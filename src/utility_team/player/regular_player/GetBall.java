package utility_team.player.regular_player;

import easy_soccer_lib.utils.EFieldSide;
import easy_soccer_lib.utils.Vector2D;
import utility_team.player.AbstractBehavior;
import utility_team.player.Player;


public class GetBall extends AbstractBehavior {

	/**
	 * C�digo adaptado (simplificado) do c�digo de ActionGoGetBall.java (do agente com behavior tree). 
	 */
	@Override
	public void perform(Player agent) {
		Vector2D ballPos = agent.ballPosition;
		
		if (agent.isCloseTo(ballPos, 1.0)) {
			//faz nada (situa��o desejada)
			//a partir daqui, talvez ele seja disparado um AdvanceWithBall ou um KickBall
		} else if (agent.isAlignedTo(ballPos)) {
			agent.commander.doDashBlocking(100.0d);
		} else {
			agent.commander.doTurnToPoint(ballPos);
		}

	}

	/**
	 * Esta m�trica � formada pela combina��o de v�rias medidas.
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
		
		

		// MEDIDA 1 -- medida de perigo de sofrer gol (qu�o pr�xima a bola est� do meu gol)
		// - dist�ncia <  10.0 	--> medida tem valor m�ximo (1,0)
		// - dist�ncia >= 10.0 	--> medida tem valor que desce linearmente de 1.0 a 0.0, na medida em que a dist�ncia aumenta de 10.0 para 50.0 (aprox. meio do campo) 
		double metric1; 
		if (distanceBallDefensiveGoal < 10.0) {
			metric1 = 1.0;
		} else {
			metric1 = 1.0 - (distanceBallDefensiveGoal - 10.0) / 40.0;  //express�o pode ser simplificada, mas pode deixar para o compilador... 
		}
		
		EFieldSide mySide = agent.mySide;
		EFieldSide advSide = agent.mySide.opposite();
		double distMyTeamToBall  = agent.getPlayerClosestToBall(mySide).distanceTo(agent.ballPosition);
		double distAdvTeamToBall = agent.getPlayerClosestToBall(advSide).distanceTo(agent.ballPosition);
	
		// MEDIDA 2 -- medida de perigo do advers�rio dominar a bola
		// - propor��o = 1.0  --> advers�rio a uma mesma dist�ncia que meu time
		// - propor��o > 1.0  --> advers�rio est� mais perto da bola do que o meu time, a utilidade desta a��o fica mais alta! 
		
		double metric2 = distMyTeamToBall / distAdvTeamToBall;  // FAZER: criar constante 0.0001 para evitar divis�o por 0
		
		return 0.4*metric1 + 0.6*metric2;
	}


}
