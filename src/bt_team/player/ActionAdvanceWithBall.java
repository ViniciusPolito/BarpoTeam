package bt_team.player;

import behavior_tree_lib.BTNode;
import behavior_tree_lib.BTStatus;
import easy_soccer_lib.utils.Vector2D;


public class ActionAdvanceWithBall extends BTNode<BTreePlayer> {

	@Override
	public BTStatus tick(BTreePlayer agent) {
		Vector2D ballPos = agent.fieldPerc.getBall().getPosition();
		
		//condicao ruim extrema: longe da bola
		if (!agent.isCloseTo(ballPos, 10.0)) {
			return BTStatus.FAILURE;
		}
		
		//resultado desejado atingido: perto da bola (dist < 3) e perto do goal (dist < 20)
		if (agent.isCloseTo(ballPos, 3.0) && agent.isCloseTo(agent.goalPosition, 20)) {
			return BTStatus.SUCCESS;
		}
		
		//caso intermediario: razoavelmente perto da bola 
		//                    corre atras da bola e da um pequeno toque
		if (agent.isAlignedTo(ballPos)) {
			if (agent.isCloseTo(ballPos, 1.0)) {
				agent.commander.doKickToPoint(40.0d, agent.goalPosition); //da um toque adiante (forca baixa)
			} else {
				agent.commander.doDashBlocking(100.0d); //chega mais perto da bola
			}
		} else {
			agent.commander.doTurnToPoint(ballPos);
		}
		
		return BTStatus.RUNNING;
	}
	
}
