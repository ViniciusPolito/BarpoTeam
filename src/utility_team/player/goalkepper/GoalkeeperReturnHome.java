package utility_team.player.goalkepper;

import easy_soccer_lib.utils.EFieldSide;
import easy_soccer_lib.utils.Vector2D;
import utility_team.player.AbstractBehavior;
import utility_team.player.Player;

public class GoalkeeperReturnHome extends AbstractBehavior{
	
	private Vector2D homebase;
	public GoalkeeperReturnHome(Vector2D homePos) {
		this.homebase = homePos;
	}
	
	@Override
	public void perform(Player player) {
		if (!player.arrivedAt(homebase)) {

			if (player.isAlignedTo(homebase)) {
				player._printf_once("RTHB: Running to the base...");
				player.commander.doDashBlocking(60.0d);
			} else {
				player._printf("RTHB: Turning...");
				player.commander.doTurnToPointBlocking(homebase);
			}
		}
		
	}

	@Override
	public double utility(Player player) {
		Vector2D ballPos = player.fieldPerc.getBall().getPosition();
		if (player.mySide == EFieldSide.LEFT) {
			if (!((ballPos.getX() < -36.0) && (((ballPos.getY()) > -20.0) || ((ballPos.getY()) < 20.0)))) {
				return 1.0f;
			}

		} else {
			if (!((ballPos.getX() > 36.0) && (((ballPos.getY()) > -20.0) || ((ballPos.getY()) < 20.0)))) {
				return 1.0f;
			}
		}

		return 0.0f;
	}

}
