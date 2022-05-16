package utility_team.player;

/**
 * Esta classe representa um comportamento do agente {@link Player} com dois m�todos.
 * O m�todo {@link #perform(Player)} permite executar o comportamento propriamente dito.
 * E o m�todo {@link #utility(Player)} retorna um valor de utilidade, que qu�o 
 * �til/adequado � o comportamento no momento atual.
 *  
 * @author Pablo Sampaio
 */
public abstract class AbstractBehavior {

	/**
	 * Aplica a as a��es deste comportamento no player. 
	 */
	public abstract void perform(Player player);
	
	/**
	 * Recomenda��o: gerar valores aproximadamente de 0.0 a 1.0.
	 * Usar valor negativo (ex.: -1.0) quando a a��o n�o for uma boa escolha. 
	 */
	public abstract double utility(Player player);
	
}
