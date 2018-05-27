package util;

import Agents.AgentOnField;
import model.Color;

/**
 * 
 * @author wakidou
 * -------------------------------------------------------------------------
 * 		Permet d'instancier des agents selon leur couleur et leur type
 * -------------------------------------------------------------------------
 */
public final class FactoryAgent {
	public static AgentOnField make(Class<?> c,Color colorAgent) {
		try {
			if(!(c == AgentOnField.class))
				throw new IllegalArgumentException("La classe en paramètre n'est pas un AgentOnField");
			
			if(colorAgent == Color.None)
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
