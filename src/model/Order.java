package model;

import Agents.ColoringAgent;
import Agents.ScoutAgent;
import sim.util.Int2D;
import util.TargetType;

// Classe qui symbolise un ordre donné par un agent éclaireur à un agent colorieur
public class Order {
	
	/* Emetteur */
	private ScoutAgent orderingAgent;
	
	/* Destinataire */
	private ColoringAgent intendedAgent;
	
	/* Destination */
	private Int2D position;
	
	/* Type de l'ordre */
	private TargetType targetType;
	
	/* Score permettant de donner un niveau � la zone � colorier */
	private int scoreForLandType;
	
	public Order(Int2D position) {
		this.position = position;
	}
	
	public Order(ScoutAgent orderingAgent,ColoringAgent intendedAgent,Int2D position){
		// constructeur pour les ordres concernant les tubes de peintures ( 3 arguments)
		this.orderingAgent = orderingAgent;
		this.intendedAgent = intendedAgent;
		this.position = position;
		this.targetType = TargetType.paintPot;
	}
	
	public Order(ScoutAgent orderingAgent,ColoringAgent intendedAgent,Int2D position, int scoreOfLand){
		// constructeur pour les ordres concernant les cases à colorier ( 4 arguments)
		this.orderingAgent = orderingAgent;
		this.intendedAgent = intendedAgent;
		this.position = position;
		this.targetType = TargetType.land;
		this.scoreForLandType = scoreOfLand;
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
	
	/**
	 * Retourne le score indiquant le niveau de la zone � colorier
	 * @return
	 */
	public int getScoreForLandType(){
		return this.scoreForLandType;
	}
	
	@Override
	public String toString(){
		String result = "";
		result += String.format("Type de l'ordre : %s \n",this.targetType.toString());
		result += String.format("Destinataire : %s \n , Emetteur : %s \n", this.intendedAgent.toString(),this.orderingAgent.toString());
		result += String.format("Destination : %d,%d",this.position.x,this.position.y);
		return result;
	}
	
	
	

}
