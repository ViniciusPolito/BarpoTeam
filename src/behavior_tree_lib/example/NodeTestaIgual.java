package behavior_tree_lib.example;

import behavior_tree_lib.BTNode;
import behavior_tree_lib.BTStatus;


class NodeTestaIgual extends BTNode<MyAgent> {
	private int numberToCompare;
	
	NodeTestaIgual(int x) {
		super("TestaEq" + x);
		this.numberToCompare = x;
	}
	
	@Override
	public BTStatus tick(MyAgent agent) {
		if (agent.status == this.numberToCompare) {
			return BTStatus.SUCCESS;
		}
		return BTStatus.FAILURE;
	}
	
}

