package utility_team.player.regular_player;

import easy_soccer_lib.perception.PlayerPerception;
import easy_soccer_lib.utils.EFieldSide;
import easy_soccer_lib.utils.Vector2D;
import utility_team.player.AbstractBehavior;
import utility_team.player.Player;


public class AdvanceWithBall extends AbstractBehavior {

	@Override
	public void perform(Player agent) {
		// Código adaptado (simplificado) do código de ActionAdvanceWithBall.java (do agente com behavior tree)
		Vector2D ballPos = agent.ballPosition;
		
		if (agent.isCloseTo(ballPos, 1.0)) {
			agent.commander.doKickToPoint(30.0d, agent.offensiveGoalPos); //da um toque adiante (forca baixa)
		
		} else if (agent.isAlignedTo(ballPos)) {
			agent.commander.doDashBlocking(100.0d); //chega mais perto da bola
		
		} else {
			agent.commander.doTurnToPoint(ballPos); //esta longe e desalinhado da bola, então alinha
		}

	}

	@Override
	public double utility(Player agent) {
		Vector2D ballPos = agent.ballPosition;
		if (!agent.isCloseTo(ballPos, 10.0) || !agent.isPlayerClosestToBallInTeam()) {
			return -1.0f;
		}
		
		int adversariesAhead = 0;
		
		double x = agent.myPosition.getX();
		double y = agent.myPosition.getY();
		
		for (PlayerPerception adv : agent.playerOtherTeam) {
			
			if (agent.mySide == EFieldSide.LEFT) {
				if (adv.getPosition().getX() > x 
						&& Math.abs( adv.getPosition().getY() - y ) <= 15.0) {
					adversariesAhead ++;
				}
			
			} else {
				if (adv.getPosition().getX() < x 
						&& Math.abs( adv.getPosition().getY() - y ) <= 15.0) {
					adversariesAhead ++;
				}
			}
		}
		
		return (float) (1.0 / (adversariesAhead + 1.0));
	}


}
