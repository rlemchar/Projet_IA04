package model;

import Agents.ColoringAgent;
import Agents.ScoutAgent;
import sim.util.Int2D;

// Classe qui symbolise un ordre donné par un agent éclaireur à un agent colorieur
public class Order {
	
	ScoutAgent orderingAgent;
	ColoringAgent intendedAgent;
	Int2D position;
	
	public Order(ScoutAgent orderingAgent,ColoringAgent intendedAgent,Int2D position){
		this.orderingAgent = orderingAgent;
		this.intendedAgent = intendedAgent;
		this.position = position;
		
	}
	
	public ScoutAgent getOrderingAgent() {
		return orderingAgent;
	}

	public ColoringAgent getIntendedAgent() {
		return intendedAgent;
	}

	public Int2D getPosition() {
		return position;
	}
	
	

}
