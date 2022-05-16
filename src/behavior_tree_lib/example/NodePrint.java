package behavior_tree_lib.example;

import behavior_tree_lib.BTNode;
import behavior_tree_lib.BTStatus;


class NodePrint extends BTNode<MyAgent> {
	private String message;
	
	NodePrint(String msg) {
		super("Print");
		this.message = msg;
	}
	
	@Override
	public BTStatus tick(MyAgent data) {
		print("ACAO REALIZADA - Print " + message);
		return BTStatus.SUCCESS;
	}
	
}