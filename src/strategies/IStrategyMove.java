package strategies;

import model.Color;

/**
 * Interface permettant de définir la stratégie de déplacement selon la couleur de la case
 * 
 * @author wakidou
 *
 */
public interface IStrategyMove {
	
	/**
	 * Retourne la couleur sur laquelle l'agent va privilégier lors de ses déplacements
	 * @return Color
	 */
	Color StrategyMove();
	
	/**
	 * Retourne la couleur sur laquelle l'agent se déplace si celle de la stratégie n'est pas possible
	 * @return Color
	 */
	Color OtherWiseMove();
	
	/**
	 * Retourne la couleur sur laquelle l'agent ira en dernier recours
	 * @return Color;
	 */
	Color WorstCaseMove();
	
}
