package utility_team.player.defender;

import easy_soccer_lib.PlayerCommander;
import easy_soccer_lib.utils.Vector2D;
import utility_team.player.Player;
import utility_team.player.regular_player.AdvanceWithBall;
import utility_team.player.regular_player.GetBall;
import utility_team.player.regular_player.KickBall;
import utility_team.player.regular_player.ReturnHome;

public class Defender extends Player{

	public Defender(PlayerCommander player, Vector2D home) {
		super(player, home);

		behaviors.add(new ReturnHome(home));
		
		behaviors.add(new AdvanceWithBall());
		behaviors.add(new GetBall());
		behaviors.add(new KickBall());	
		behaviors.add(new PassHighPriority());			
	}

}
