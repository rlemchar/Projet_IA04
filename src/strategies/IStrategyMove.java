package strategies;

import model.MyColor;

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
	MyColor StrategyMove();
	
	/**
	 * Retourne la couleur sur laquelle l'agent se déplace si celle de la stratégie n'est pas possible
	 * @return Color
	 */
	MyColor OtherWiseMove();
	
	/**
	 * Retourne la couleur sur laquelle l'agent ira en dernier recours
	 * @return Color;
	 */
	MyColor WorstCaseMove();
	
}
