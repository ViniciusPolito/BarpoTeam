package bt_team.player;

import behavior_tree_lib.BTNode;
import behavior_tree_lib.BTStatus;
import easy_soccer_lib.utils.Vector2D;

public class ActionKickToGoal extends BTNode<BTreePlayer> {

	@Override
	public BTStatus tick(BTreePlayer agent) {
		Vector2D ballPos = agent.fieldPerc.getBall().getPosition();
		
		//condicao ruim extrema: longe demais da bola
		if (!agent.isCloseTo(ballPos, 3.0)) {
			return BTStatus.FAILURE;
		}
		
		if (agent.isAlignedTo(ballPos)) {
			if (agent.isCloseTo(ballPos, 1.0)) {
				//da um chute com forca maxima (100)
				agent.commander.doKickToPoint(100.0d, agent.goalPosition);
				return BTStatus.SUCCESS;
			} else {
				//corre com forca intermediaria (porque esta perto da bola)
				agent.commander.doDashBlocking(60.0d);
			}
		} else {
			agent.commander.doTurnToPoint(ballPos);
		}
		
		return BTStatus.RUNNING;
	}

}
