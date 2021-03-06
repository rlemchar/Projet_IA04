package util;

import Agents.AgentOnField;
import model.MyColor;

/**
 * 
 * @author wakidou
 * -------------------------------------------------------------------------
 * 		Permet d'instancier des agents selon leur couleur et leur type
 * -------------------------------------------------------------------------
 */
public final class FactoryAgent {
	/**
	 * @param c -> Classe fille de la classe AgentOnField
	 * @param colorAgent -> Couleur de l'agent
	 * @return Une nouvelle instance d'un agent 
	 */
	public static AgentOnField make(Class<? extends AgentOnField> c,MyColor colorAgent) {
		try {
			if(colorAgent == MyColor.None)
				throw new IllegalArgumentException("L'agent doit posséder une couleur !");
				
			AgentOnField agent = (AgentOnField)c.newInstance();
			agent.setColorAgent(colorAgent);
			return agent;
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
