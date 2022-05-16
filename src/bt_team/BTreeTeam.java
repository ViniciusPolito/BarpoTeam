package bt_team;

import bt_team.player.BTreePlayer;
import easy_soccer_lib.AbstractTeam;
import easy_soccer_lib.PlayerCommander;
import easy_soccer_lib.utils.Vector2D;


/**
 * Time simples, demonstrado em sala.
 */
public class BTreeTeam extends AbstractTeam {

	public BTreeTeam(String suffix) {
		super("BT-Team-" + suffix, 2, false);
	}

	@Override
	protected void launchPlayer(int uniformNumber, PlayerCommander commander) {
		double x, y;

		switch (uniformNumber) {
		case 1:
			x = -37.0d;
			y = -20.0d;
			break;
		case 2:
			x = -37.0d;
			y = 20.0d;
			break;
		default:
			x = -37.0d;
			y = 0;
		}
		
		BTreePlayer pl = new BTreePlayer(commander, new Vector2D(x, y));
		pl.start();
	}

}
