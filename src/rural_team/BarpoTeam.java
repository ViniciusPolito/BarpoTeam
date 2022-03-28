package rural_team;

import easy_soccer_lib.AbstractTeam;
import easy_soccer_lib.PlayerCommander;
import easy_soccer_lib.utils.EFieldSide;
import easy_soccer_lib.utils.Vector2D;
import rural_team.formations.Formations;
import rural_team.players.DefenserPlayer;
import rural_team.players.Goalkeeper;
import rural_team.players.RegularPlayer;
import rural_team.players.StrikerPlayer;

public class BarpoTeam extends AbstractTeam {

	public BarpoTeam(String suffix, int numberOfPlayers, boolean goalkeeper ) {
		super(suffix, numberOfPlayers, goalkeeper);
	}

	@Override
	protected void launchPlayer(int uniform, PlayerCommander commander) {

		RegularPlayer pl;		
		Formations formation = new Formations();
		Vector2D form;
		form = formation.initalHexaFormation(commander);
		
		// se o time estiver no lado direito, faz o ajuste
		if (commander.getMyFieldSide() == EFieldSide.RIGHT) {
			form.setX(-form.getX());
			form.setY(-form.getY());
		}
		
		System.out.printf("Jogador %d, lado %s -- x %f, y %f  	\n", uniform, commander.getMyFieldSide(), form.getX(), form.getY());

		if (uniform >= 2 && uniform <= 4) {
			pl = new DefenserPlayer(commander, form.getX(), form.getY());
		} else if(uniform >= 5) {
			pl = new StrikerPlayer(commander, form.getX(), form.getY());
		} 
		else {
			pl = new Goalkeeper(commander, form.getX(), form.getY());
		}
		pl.start();
	}
}
