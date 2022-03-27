package rural_team;

import java.net.UnknownHostException;

public class Main {
	public static void main(String[] args) throws UnknownHostException {
		BarpoTeam team1 = new BarpoTeam("Barpo_T1", 7, true);
		BarpoTeam team2 = new BarpoTeam("Barpo_T2", 7, true);
		
		team1.launchTeamAndServer();
		team2.launchTeam();
	}
}
