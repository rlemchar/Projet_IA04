package model;

import Agents.ColoringAgent;
import Agents.ScoutAgent;
import sim.util.Int2D;
import util.TargetType;

// Classe qui symbolise un ordre donnÃ© par un agent Ã©claireur Ã  un agent colorieur
public class Order {
	
	/* Emetteur */
	ScoutAgent orderingAgent;
	
	/* Destinataire */
	ColoringAgent intendedAgent;
	
	/* Destination */
	Int2D position;
	
	/* Type de l'ordre */
	private TargetType targetType;
	
	/* Score permettant de donner un niveau à la zone à colorier */
	private int scoreForLandType;
	
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
	 * Retourne la position souhaité pour l'ordre
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
	
	/**
	 * Retourne le score indiquant le niveau de la zone à colorier
	 * @return
	 */
	public int getScoreForLandType(){
		return this.scoreForLandType;
	}
	
	@Override
	public String toString(){
		String result = "";
		result += String.format("Type de l'ordre : {0} \n",this.targetType.toString());
		result += String.format("Destinataire : {0} \n , Emetteur : {1} \n", this.intendedAgent.toString(),this.orderingAgent.toString());
		result += String.format("Destination : {0},{1}",this.position.x,this.position.y);
		return result;
	}
	

}
