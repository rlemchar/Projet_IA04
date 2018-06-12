package model;

import Agents.ColoringAgent;
import Agents.ScoutAgent;
import sim.util.Int2D;
import util.TargetType;

// Classe qui symbolise un ordre donné par un agent éclaireur à un agent colorieur
public class Order {
	
	ScoutAgent orderingAgent;
	ColoringAgent intendedAgent;
	Int2D position;
	TargetType targetType;
	
	public Order(ScoutAgent orderingAgent,ColoringAgent intendedAgent,Int2D position, TargetType targetType){
		this.orderingAgent = orderingAgent;
		this.intendedAgent = intendedAgent;
		this.position = position;
		this.targetType = targetType;
		
	}
	
	public ScoutAgent getOrderingAgent() {
		return orderingAgent;
	}

	public ColoringAgent getIntendedAgent() {
		return intendedAgent;
	}

	/**
	 * Retourne la position souhait� pour l'ordre
	 * @return
	 */
	public Int2D getPosition() {
		return position;
	}
	
	public void setPosition(Int2D position){
		this.position = position;
	}
	
	/**
	 * Retourne le type d'ordre
	 * @return
	 */
	public TargetType getTargetType(){
		return this.targetType;
	}
	
	
	

}
