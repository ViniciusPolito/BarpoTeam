package utility_team;

import java.net.UnknownHostException;


public class MainUT {

	public static void main(String[] args) throws UnknownHostException {
		UtilityTeam team1 = new UtilityTeam("Demons", 6, true);
		UtilityTeam team2 = new UtilityTeam("Gods", 6, true);
		
		team1.launchTeamAndServer();
		team2.launchTeam();
	}
	
}

