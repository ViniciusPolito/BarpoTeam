package utility_team.player;

import easy_soccer_lib.utils.EMatchState;

public class MatchStates extends AbstractBehavior {

	@Override
	public void perform(Player player) {
		
		switch (player.matchInfo.getState()) {
		case BEFORE_KICK_OFF:
			player._printf_once("BEFORE_KICK_OFF");
			// Não faz nada
			break;
		case TIME_OVER:
			player._printf_once("TIME_OVER");
			break;
		case KICK_OFF_LEFT:
			player._printf_once("KICK_OFF_LEFT");
			player.kickOffLeft(6, 5);
			break;
		case KICK_OFF_RIGHT:
			player._printf_once("KICK_OFF_RIGHT");
			player.kickOffRight(5, 6);
			break;
		case KICK_IN_LEFT:
			player._printf_once("KICK_IN_LEFT");
			player.kickInLeft();
			break;
		case KICK_IN_RIGHT:
			player._printf_once("KICK_IN_RIGHT");
			player.kickInRight();
			break;
		case FREE_KICK_LEFT:
			player._printf_once("FREE_KICK_LEFT");
			player.freeKickLeft();
			break;
		case FREE_KICK_RIGHT:
			player._printf_once("FREE_KICK_RIGHT");
			player.freeKickRight();
			break;
		case CORNER_KICK_LEFT:
			player._printf_once("CORNER_KICK_LEFT");
			player.cornerKickLeft();
			break;
		case CORNER_KICK_RIGHT:
			player._printf_once("CORNER_KICK_RIGHT");
			player.cornerKickRight();
			break;
		case GOAL_KICK_LEFT:
			player._printf_once("GOAL_KICK_LEFT");
			player.goalKickLeft();
			break;
		case GOAL_KICK_RIGHT:
			player._printf_once("GOAL_KICK_RIGHT");
			player.goalKickRight();
			break;
		case AFTER_GOAL_LEFT:
			player._printf_once("AFTER_GOAL_LEFT");
			//player.myPosition = formation.initalHexaFormation(player.commander);
			//this.stateReturnToHomeBase();// RETURN TO HOME
			break;
		case AFTER_GOAL_RIGHT:
			player._printf_once("AFTER_GOAL_RIGHT");
			//player.myPosition = formation.initalHexaFormation(player.commander);
			//this.stateReturnToHomeBase();// RETURN TO HOME
			break;
		case FREE_KICK_FAULT_LEFT:
			player._printf_once("FREE_KICK_FAULT_LEFT");
			player.freeKickLeft();
			break;
		case FREE_KICK_FAULT_RIGHT:
			player._printf_once("FREE_KICK_FAULT_RIGHT");
			player.freeKickRight();
			break;
		default:
			player._printf_once("SWITCH OTHERS CONDITIONS");
			break;
		}
		
	}

	@Override
	public double utility(Player player) {
		if(player.matchInfo.getState() == EMatchState.PLAY_ON) {
			return -1.0f; 
		}
		else {
			return 1.0f;
		}
	}

}
