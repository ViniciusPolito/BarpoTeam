package behavior_tree_lib;

public enum BTStatus {
	SUCCESS,  // o nó realizou (e terminou) uma ação
	FAILURE,  // o nó não pode realizar uma ação
	RUNNING   // o nós ainda está executando uma ação
}
