package behavior_tree_lib;

import java.util.LinkedList;

/**
 * Representa um nó "sequence". Tenta realizar todas as ações, na ordem indicada.
 * 
 * @author Pablo Sampaio
 *
 * @param <T>
 */
public class Sequence<T> extends BTNode<T> {
	private LinkedList<BTNode<T>> list;
	
	public Sequence(String name) {
		super("SEQ-" + name);
		this.list = new LinkedList<BTNode<T>>();
	}
	
	public void add(BTNode<T> node) {
		this.list.add(node);
	}

	@Override
	public BTStatus tick(T agentInterface) {
		BTNode<T> node;
		BTStatus status;
		
		if (this.list.isEmpty()) {
			print("ATENCAO: ", super.name, " vazio!");
		}
		
	
		print(super.name, "...");
		for (int i = 0; i < list.size(); i++) {
			node = this.list.get(i);

			BTNode.LEVEL ++;
			status = node.tick(agentInterface);
			print(node, " -- ", status);
			BTNode.LEVEL --;
			
			switch (status) {
			case SUCCESS:
				break;
			case FAILURE:
				//print(super.name, " -- FAILURE");
				return BTStatus.FAILURE;
			case RUNNING:
				//print(super.name, " -- RUNNING ", node);
				return BTStatus.RUNNING;
			default:
				BTNode.LEVEL = 0;
				throw new IllegalArgumentException("Unexpected value: " + status);
			}
		}
		
		//print(super.name, " -- SUCCESS (all)");
		return BTStatus.SUCCESS;
	}

}
