package behavior_tree_lib;

import java.util.LinkedList;

/**
 * Representa um nó "selector". Realiza a primeira ação possível, tentando na ordem indicada.
 * 
 * @author Pablo Sampaio
 *
 * @param <T>
 */
public class Selector<T> extends BTNode<T> {
	private LinkedList<BTNode<T>> list;
	
	public Selector(String name) {
		super("SEL-" + name);
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
				//print(super.name, " -- SUCCESS");
				return BTStatus.SUCCESS;
			case FAILURE:
				break;
			case RUNNING:
				//print(super.name, " -- RUNNING");
				return BTStatus.RUNNING;
			default:
				BTNode.LEVEL = 0;
				throw new IllegalArgumentException("Unexpected value: " + status);
			}
		}
		
		//print(super.name, " -- FAILURE (all)");
		return BTStatus.FAILURE;
	}

}
