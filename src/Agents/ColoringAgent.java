package Agents;

import sim.engine.SimState;
import sim.engine.Steppable;
import sim.util.Int2D;
import util.Constants;
import util.Statics;

import java.util.ArrayList;
import java.util.stream.Stream;

import model.CaseColor;
import model.Color;
import model.CommunicationSystem;
import model.GridModel;
import model.Order;
import model.PaintPot;

public class ColoringAgent extends AgentOnField implements Steppable{
	/**
	 * Serial version
	 */
	private static final long serialVersionUID = 4967689413678754350L;
	
	public enum Direction {
		Nord,Sud,Est,Ouest
	}
	
	/* Nombre de tubes de peinture */
	private int numberOfTubeOfPaint;
	
	/* Booléen indiquant si l'agent a actuellement une destination précise où aller */
	private boolean hasAdestination;
	
	/* Destination objectif de l'agent */
	private Int2D destination;
	
	/** Constructeur par d�faut **/
	public ColoringAgent() {
		super();
		this.numberOfTubeOfPaint = 0;
		this.hasAdestination = false;
	}
	
	/**
	 * Constructeur en initialisant la couleur de l'agent
	 * @param colorAgent Couleur de l'agent
	 */
	public ColoringAgent(Color colorAgent) {
		super(colorAgent);
		this.numberOfTubeOfPaint = 0;
		this.hasAdestination = false;
	}
	
	@Override
	public void step(SimState state) {
		this.grid = (GridModel) state;
		/*
		if (!hasAdestination){
			if (isThereNewOrders){
				moveTowardsDestination();
			}else{
				move();
			}
		}else{
			moveTowardsDestination();
		}
		*/
	}
	
	public boolean receiveInfoFromScout(){
		return false;
	}
	
	/**
	 * Permet de colorier une zone 
	 * -> La zone est �gale � la zone de perception de l'agent
	 */
	public void Color(){
		/* Variables locales*/
		Stream<CaseColor> colorZoneFiltered;
		CaseColor[] cellWithOppositeColor;
		
		/* R�cup�ration de la zone de coloriage -> Uniquement les cases qui ne sont pas de la couleur de l'agent*/
		colorZoneFiltered = Statics.GetZoneColor(grid, new Int2D(
									this.location.x - this.powerOfPerception,
									this.location.y - this.powerOfPerception
								), this.location.x + this.powerOfPerception, this.location.y + this.powerOfPerception
							).stream().filter(cell -> cell.getColor() != this.colorAgent);
		
		/* R�cup�ration des cases avec la couleur de l'�quipe adverse */
		cellWithOppositeColor = colorZoneFiltered.filter(cell -> cell.getColor() == this.oppositeColor).toArray(CaseColor[]::new);
		
		/* On ne colorie pas si le nombre de cases � colorier est inf�rieur au seuil */
		if(colorZoneFiltered.count() >= Constants.THRESHOLD_FOR_PAINTING_A_ZONE){
			/* On colorie et/ou on rend les cases neutre d'abord */
			for(CaseColor cell : colorZoneFiltered.toArray(CaseColor[]::new)){
				if(this.HasPaint()){
					cell.setColor(cell.getColor() == Color.None ? this.colorAgent : Color.None);
					this.numberOfTubeOfPaint--;
				}		
				else
					break;	
			}
			
			/* On colorie les cases neutres restantes */
			for(CaseColor cell : cellWithOppositeColor){
				if(this.HasPaint()){
					cell.setColor(this.colorAgent);
					this.numberOfTubeOfPaint--;
				}
				else
					break;
			}
		}
	}
	
	/**
	 * Permet de recharger le nombre de tubes de peinture
	 * @param pot -> Pot de peinture � la position de l'agent
	 */
	public void rechargePaint(PaintPot pot){
		if(!this.isFullyLoadedOfPaint()){
			while(this.isFullyLoadedOfPaint() && pot.getQuantity() != 0){
				this.numberOfTubeOfPaint++;
				pot.decQuantity();
			}
		}
	}
	
	/**
	 * Permet à un agent de se déplacer vers une destination précise
	 */
	public void moveTowardsDestination(){
		/* Variables locales */
		Int2D tempCoord,offsetCoord; // offsetCoord -> offset pour le point haut gauche de la zone de déplacement possible
		CaseColor[] adjacentCase;
		Direction direction; // Enuméré permettant d'indiquer la direction vers la destination
		ArrayList<CaseColor> tempForCase;
		
		/* On vérifie si l'agent a bien une destination précise 
		 * ou si l'agent n'est pas déja à la bonne destination */
		if(!this.hasAdestination || (this.destination.x == this.location.x && this.destination.y == this.location.y))
			return;
		
		/* Initialisation de la direction */
		direction = (this.destination.x < this.location.x) ? Direction.Nord : Direction.Sud;
		
		/* On se déplace sur nos cases si possible -> cases faisant partie d'un chemin possible */
		while(this.steps != 0) {
			/* Init pour le point haut gauche */
			offsetCoord = new Int2D((this.location.x == 0) ? 0 : 1,(this.location.y == 0) ? 0 : 1);
			tempCoord = new Int2D(this.location.x - offsetCoord.x,this.location.y - offsetCoord.y);
			
			/* On prend une zone de 1 centré sur la position actuelle 
			 * puis on récupère les cases à coté qui sont sur un chemin possible */
			adjacentCase  = Statics.GetZoneColor(this.grid,tempCoord, 3, 3).stream()
					.filter(caseColor -> (caseColor.getX() == this.location.x ^ caseColor.getY() == this.location.y) 
								&& (direction == Direction.Nord) ? caseColor.getX() <= this.location.x : caseColor.getX() >= this.location.x
					)
					.toArray(CaseColor[]::new);
			
			/* On se déplace selon la stratégie */
			
			
		}
		
	}
		
	public void compareDestinations(){
	
	}
	
	/**
	 * Permet de savoir si l'agent peut colorier une case adverse
	 * @return
	 */
	public boolean CanColorOppositeCase(){
		return this.numberOfTubeOfPaint > 2;
	}
	
	/**
	 * Renvoie un bool�en qui permet de v�rifier si la charge de peinture est � son max
	 * @return
	 */
	public boolean isFullyLoadedOfPaint(){
		return this.numberOfTubeOfPaint == Constants.MAX_TUBE_OF_PAINT;
	}
	
	
	/**
	 * Permet de savoir si l'agent a des tubes de peintures
	 * @return bool�en
	 */
	public boolean HasPaint(){
		return this.numberOfTubeOfPaint == 0;
	}
	
	// Partie communication / Execution ordre
	
	public Boolean isThereNewOrders(){
		
		ArrayList<Order> lastOrders = CommunicationSystem.consultOrders(this);
		if (lastOrders.isEmpty()){
			return false;
		}else{
			compareAndChooseOrder(lastOrders);
			return true;
		}
	}
	
	// Dans cette fonction on compare les ordres des agents scout et on choisit le 
	// plus proche en fesant somme de : valeur absolue de la difference des abscisses et
	// valeur absolue de la difference des ordonnées (fonction calculateDistanceScore)
	public void compareAndChooseOrder(ArrayList<Order> orders){
		
		int min = -1; // n'a pas encore de valeur mais en aura puisque minimum un ordre si 
		// fonction est appelée.
		
		for (Order order : orders){
			int distance = calculateDistanceScore(order.getPosition());
			if (min == -1 || (distance < min) ){
				min = calculateDistanceScore(order.getPosition());
			}
		}	
	}

	public void answerToScout(){
		// vrmt necessaire ?
		
	
	}
	
	int calculateDistanceScore(Int2D targetLocation){
		
		int x = Math.abs(this.location.x + targetLocation.x);
		int y = Math.abs(this.location.y + targetLocation.y);
		return x+y;
	}
}
