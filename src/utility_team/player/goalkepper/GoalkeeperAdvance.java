package utility_team.player.goalkepper;

import easy_soccer_lib.utils.EFieldSide;
import easy_soccer_lib.utils.Vector2D;
import utility_team.player.AbstractBehavior;
import utility_team.player.Player;

public class GoalkeeperAdvance extends AbstractBehavior {

	@Override
	public void perform(Player player) {
		
		Vector2D ballPos = player.fieldPerc.getBall().getPosition();
		
		if (player.isAlignedTo(ballPos)) {
			if (player.isCloseTo(ballPos, 1.0)) {
				player.commander.doKickToPoint(100.0d, player.offensiveGoalPos);
			} else {
				player.commander.doDashBlocking(100.0d);
			}
		} else {
			player.commander.doTurnToPoint(ballPos);
		}
				
	}

	@Override
	public double utility(Player player) {		
			
		Vector2D ballPos = player.fieldPerc.getBall().getPosition();
			if (player.mySide == EFieldSide.LEFT) {
				if ((ballPos.getX() < -36.0) && (((ballPos.getY()) > -20.0)  || ((ballPos.getY()) < 20.0)  )) {
					return 1.0f;
				}
			
			} else {
				if ((ballPos.getX() > 36.0) && (((ballPos.getY()) > -20.0)  || ((ballPos.getY()) < 20.0)  )) {
					return 1.0f;
				}
			}

		return 0.0f;
	}
	

}
