package utility_team.player.regular_player;

import easy_soccer_lib.PlayerCommander;
import easy_soccer_lib.utils.Vector2D;
import utility_team.player.Player;

public class RegularPlayer extends Player {

	public RegularPlayer(PlayerCommander player, Vector2D home) {
		super(player, home);
		
		behaviors.add(new ReturnHome(home));
		
		behaviors.add(new AdvanceWithBall());
		behaviors.add(new GetBall());
		behaviors.add(new KickBall());	
		behaviors.add(new PassToPlayer());	
	}

}
