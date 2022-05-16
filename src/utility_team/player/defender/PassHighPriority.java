package utility_team.player.defender;

import easy_soccer_lib.perception.PlayerPerception;
import easy_soccer_lib.utils.EFieldSide;
import easy_soccer_lib.utils.Vector2D;
import utility_team.player.AbstractBehavior;
import utility_team.player.Player;

public class PassHighPriority extends AbstractBehavior{

	@Override
	public void perform(Player player) {
Vector2D ballPos = player.ballPosition;
		
		if (player.isAlignedTo(ballPos)) {
			// Retorna percepção do jogador que receberá o passe, o jogador mais proximo dele
			PlayerPerception p = player.fieldPerc.getTeamPlayer(player.selfPerc.getFieldSide(), 
					player.closestTeamPlayer(player.selfPerc.getUniformNumber()));
			// Chuta para posição do jogador que receberá o passe
			player.commander.doKickToPoint(70.0d, p.getPosition());

		}
		else {
			player.commander.doTurnToPoint(ballPos);
		}
	}

	@Override
	public double utility(Player player) {
		Vector2D ballPos = player.ballPosition;
		if (!player.isCloseTo(ballPos, 3.0) || !player.isPlayerClosestToBallInTeam()) {
			return -1.0f;
		}
		
		int adversariesAhead = 0;
		
		double x = player.myPosition.getX();
		double y = player.myPosition.getY();
		
		for (PlayerPerception adv : player.playerOtherTeam) {
			
			if (player.mySide == EFieldSide.LEFT) {
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
		
		return (float) (0.4 + (adversariesAhead * 1.0));
	}

}

