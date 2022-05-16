package bt_team.player;

import behavior_tree_lib.BTNode;
import behavior_tree_lib.BTStatus;
import easy_soccer_lib.utils.Vector2D;


public class ActionGoGetBall extends BTNode<BTreePlayer> {

	@Override
	public BTStatus tick(BTreePlayer agent) {
		Vector2D ballPos = agent.fieldPerc.getBall().getPosition();
		
		//condicao desejada: perto da bola (dist < 2) 
		if (agent.isCloseTo(ballPos, 2.0)) {
			print("PERTO!");
			return BTStatus.SUCCESS;
		}

		if (agent.isAlignedTo(ballPos)) {
			agent.commander.doDashBlocking(100.0d);
		} else {
			agent.commander.doTurnToPoint(ballPos);
		}
		
		return BTStatus.RUNNING;
	}
	
}
