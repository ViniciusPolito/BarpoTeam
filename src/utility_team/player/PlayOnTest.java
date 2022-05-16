package utility_team.player;

import easy_soccer_lib.utils.EMatchState;

public class PlayOnTest extends AbstractBehavior{

	@Override
	public void perform(Player player) {
		player.kickOffLeft(6, 5);
	}

	@Override
	public double utility(Player player) {
		if(player.matchInfo.getState() == EMatchState.PLAY_ON) {
			return 2.0f; 
		}
		else {
			return 0f;
		}
	}

}
