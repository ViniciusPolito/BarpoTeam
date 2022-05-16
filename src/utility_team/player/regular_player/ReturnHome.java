package utility_team.player.regular_player;

import java.util.Random;

import easy_soccer_lib.utils.EFieldSide;
import easy_soccer_lib.utils.EMatchState;
import easy_soccer_lib.utils.Vector2D;
import utility_team.formations.Formations;
import utility_team.player.AbstractBehavior;
import utility_team.player.Player;

public class ReturnHome extends AbstractBehavior {
	private Vector2D homebase;
	public Formations formation = new Formations();

	public ReturnHome(Vector2D homePos) {
		this.homebase = homePos;
	}

	@Override
	public void perform(Player agent) {

		if (agent.matchInfo.getState() == EMatchState.PLAY_ON) {
			if (agent.matchInfo.getTeamScore(EFieldSide.LEFT) > agent.matchInfo.getTeamScore(EFieldSide.RIGHT)) {
				if (agent.selfPerc.getFieldSide() == EFieldSide.LEFT) {
					homebase = formation.defendingFormation(agent.commander);
				} else {
					homebase = formation.attackingFormation(agent.commander);
				}

			} else if (agent.matchInfo.getTeamScore(EFieldSide.LEFT) < agent.matchInfo.getTeamScore(EFieldSide.RIGHT)) {
				if (agent.selfPerc.getFieldSide() == EFieldSide.LEFT) {
					homebase = formation.attackingFormation(agent.commander);
				} else {
					homebase = formation.defendingFormation(agent.commander);
				}

			} else {
				homebase = formation.neutralFormation(agent.commander);
			}
		} else if (agent.matchInfo.getState() == EMatchState.AFTER_GOAL_LEFT) {
			homebase = formation.initalHexaFormation(agent.commander);
		} else if (agent.matchInfo.getState() == EMatchState.AFTER_GOAL_RIGHT) {
			homebase = formation.initalHexaFormation(agent.commander);
		}
		
		
		//Random random = new Random();
		//int rand = random.nextInt(5 - 0 + 1) + 0;
		//homebase = new Vector2D(homebase.getX() + rand,homebase.getY() + rand);
		
		
		if (!agent.arrivedAt(homebase)) {

			if (agent.isAlignedTo(homebase)) {
				agent._printf_once("RTHB: Running to the base...");
				agent.commander.doDashBlocking(60.0d);
			} else {
				agent._printf("RTHB: Turning...");
				agent.commander.doTurnToPointBlocking(homebase);
			}
		}
	}

	@Override
	public double utility(Player agent) {

			if (!agent.isPlayerClosestToBallInTeam()) {
				return 0.5;
			} else {
				return 0.0;
			}
		

	}

}
