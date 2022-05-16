package utility_team.player;

/**
 * Esta classe representa um comportamento do agente {@link Player} com dois métodos.
 * O método {@link #perform(Player)} permite executar o comportamento propriamente dito.
 * E o método {@link #utility(Player)} retorna um valor de utilidade, que quão 
 * útil/adequado é o comportamento no momento atual.
 *  
 * @author Pablo Sampaio
 */
public abstract class AbstractBehavior {

	/**
	 * Aplica a as ações deste comportamento no player. 
	 */
	public abstract void perform(Player player);
	
	/**
	 * Recomendação: gerar valores aproximadamente de 0.0 a 1.0.
	 * Usar valor negativo (ex.: -1.0) quando a ação não for uma boa escolha. 
	 */
	public abstract double utility(Player player);
	
}
