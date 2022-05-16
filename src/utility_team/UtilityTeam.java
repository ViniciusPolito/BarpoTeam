package utility_team;

import easy_soccer_lib.AbstractTeam;
import easy_soccer_lib.PlayerCommander;
import easy_soccer_lib.utils.Vector2D;
import utility_team.formations.Formations;
import utility_team.player.Player;
import utility_team.player.defender.Defender;
import utility_team.player.goalkepper.GoalkeeperPlayer;
import utility_team.player.regular_player.RegularPlayer;


/**
 * Time simples, demonstrado em sala.
 */
public class UtilityTeam extends AbstractTeam {
	
	Formations formation = new Formations();

	public UtilityTeam(String name, int players, boolean withGoalie) {
		super("Barpo-" + name, players, withGoalie);
	}

	@Override
	protected void launchPlayer(int uniformNumber, PlayerCommander commander) {

		Vector2D vector = formation.initalHexaFormation(commander);
		
		if(uniformNumber == 1) {
			GoalkeeperPlayer pl = new GoalkeeperPlayer(commander, vector);
			pl.start();
		} else if(uniformNumber == 2 || uniformNumber == 3) {
			Defender pl = new Defender(commander, vector);
			pl.start();
		}
		else {
			RegularPlayer pl = new RegularPlayer(commander, vector);
			pl.start();
		}

	}

}
