package utility_team.player.goalkepper;

import easy_soccer_lib.PlayerCommander;
import easy_soccer_lib.utils.Vector2D;
import utility_team.player.Player;
import utility_team.player.regular_player.ReturnHome;

public class GoalkeeperPlayer extends Player{

	public GoalkeeperPlayer(PlayerCommander player, Vector2D home) {
		super(player, home);

		//behaviors.add(new ReturnHome(home));
		behaviors.add(new GoalkeeperReturnHome(home));
		behaviors.add(new GoalkeeperAdvance());	
	}

}
