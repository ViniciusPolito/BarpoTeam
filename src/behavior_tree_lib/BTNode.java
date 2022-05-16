package behavior_tree_lib;


public abstract class BTNode<T> {
	protected static int LEVEL = 0; // vari�vel para controlar o n�vel da �rvore
	
	protected final String name;

	public BTNode() {
		this.name = this.getClass().getName();
	}
	
	/**
	 * O nome serve para indicar a "macro-a��o" que este n� representa. 
	 */
	public BTNode(String name) {
		this.name = name;
	}

	
	/**
	 * Funcao principal. Pode implementar um teste de um condicao (retornando SUCCESS
	 * ou FAILURE) ou uma acao propriamente dita. 
	 * <br><br>
	 * Recebe uma classe do tipo T, que representa o agente. 
	 * <br><br>
	 * A clase T deve dar acesso ao estado do agente e �s percep��es do agente, que servem 
	 * como informa��es de entrada para a �rvore tomar uma decis�o.
	 * <br><br>
	 * A classe T tamb�m deve permitir indicar as a��es escolhidas pela �rvore. 
	 */
	public abstract BTStatus tick(T agent);
	
	@Override
	public String toString() {
		if (this.name != null) {
			return this.name;
		}
		return super.toString();
	}

	
	/**
	 * Uma funcao para facilitar debugging. Imprime 1 vez a cada T milissegundos.
	 */
	protected void printTimed(int period, Object ... args) {
		if (System.currentTimeMillis() > lastPrintTime + period) {
			for (Object object : args) {
				System.out.print(object);
			}
			System.out.println();
			this.lastPrintTime = System.currentTimeMillis();
		}
	}
	
	protected void print(Object ... args) {
		for (int i =0; i < BTNode.LEVEL; i++) {
			System.out.print("  ");
		}
		for (Object object : args) {
			System.out.print(object);
		}
		System.out.println();
	}

	private long lastPrintTime;

}
