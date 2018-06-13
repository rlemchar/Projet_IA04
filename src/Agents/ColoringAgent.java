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
import model.Order;
import model.PaintPot;

public class ColoringAgent extends AgentOnField implements Steppable{
	/**
	 * Serial version
	 */
	private static final long serialVersionUID = 4967689413678754350L;
	
	/* Nombre de tubes de peinture */
	private int numberOfTubeOfPaint;
	
	/* BoolÃ©en indiquant si l'agent a actuellement une destination prÃ©cise oÃ¹ aller */
	private boolean hasAdestination;
	
	/* Destination objectif de l'agent */
	private Order order;
	
	/* Pouvoir de coloration */
	private int powerOfColoration;
	
	/** Constructeur par dï¿½faut **/
	public ColoringAgent() {
		super();
		this.numberOfTubeOfPaint = 0;
		this.hasAdestination = false;
		this.order = null;
		this.powerOfPerception = Constants.PERCEPTION_FOR_COLORING_AGENT;
		this.powerOfColoration = Constants.COLORATION_POWER_FOR_COLORING_AGENT;
	}
	
	/**
	 * Constructeur en initialisant la couleur de l'agent
	 * @param colorAgent Couleur de l'agent
	 */
	public ColoringAgent(Color colorAgent) {
		super(colorAgent);
		this.numberOfTubeOfPaint = 0;

		this.hasAdestination = false;
		this.order = null;
	}
	
	@Override
	public void step(SimState state) {
		super.step(state);
		
		/* Bouger */
		if(this.isThereNewOrders()){
			if(this.hasAdestination){			
				/* Compare entre l'ordre actuel et les nouveaux */
				this.chooseBetweenDestinationAndNewOrders(CommunicationSystem.consultOrders(this));
			}
			else{
				/* On récupère le nouvel ordre */
				this.order = this.compareAndChooseOrder(CommunicationSystem.consultOrders(this));
				this.hasAdestination = true;
			}
			this.moveTowardsDestination();
		}
		else{
			if(this.hasAdestination)
				this.moveTowardsDestination();
			else
				this.TotalMove();
		}
		
		/* Cas où on est arrivé à destination */
		if(this.hasAdestination){
			if(this.location == this.order.getPosition()){
				switch(this.order.getTargetType()){
					case land:
						/* L'agent cherche une case à proximité à colorier */
						break;
					case paintPot:
						/* Il récupère un tube de peinture */
						this.rechargePaint(Statics.getPaintPot(this.grid,this.location));
						break;
					default:
						break;
				}
			}
		}
	}
	
	/**
	 * Permet de colorier une zone 
	 * EDIT : La zone de coloration est differente de la zone de perception
	 */
	public void Color(){
		/* Variables locales*/
		Stream<CaseColor> colorZoneFiltered;
		CaseColor[] cellWithOppositeColor;
		
		/* Rï¿½cupï¿½ration de la zone de coloriage -> Uniquement les cases qui ne sont pas de la couleur de l'agent*/
		colorZoneFiltered = Statics.GetZoneColor(grid, new Int2D(
									this.location.x - this.powerOfColoration,
									this.location.y - this.powerOfColoration
								), this.location.x + this.powerOfColoration, this.location.y + this.powerOfColoration
							).stream().filter(cell -> cell.getColor() != this.colorAgent);
		
		/* Rï¿½cupï¿½ration des cases avec la couleur de l'ï¿½quipe adverse */
		cellWithOppositeColor = colorZoneFiltered.filter(cell -> cell.getColor() == this.oppositeColor).toArray(CaseColor[]::new);
		
		/* On ne colorie pas si le nombre de cases ï¿½ colorier est infï¿½rieur au seuil */
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
	 * @param pot -> Pot de peinture ï¿½ la position de l'agent
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
	 * Permet Ã  un agent de se dÃ©placer vers une destination prÃ©cise
	 */
	public void moveTowardsDestination(){
		/* Variables locales */
		Int2D[] offsets = new Int2D[2]; // offset pour les cases adjacentes Ã  rï¿½cupï¿½rer -> 0 pour l'axe x , 1 pour l'axe y
		Int2D distanceCoordTemp;
		
		/* On vÃ©rifie si l'agent a bien une destination prÃ©cise 
		 * ou si l'agent n'est pas dÃ©ja Ã  la bonne destination */
		if(!(this.hasAdestination) || this.order.getPosition().x == this.location.x && this.order.getPosition().y == this.location.y)
			return;
		
		/* On se dï¿½place sur nos cases si possible -> cases faisant partie d'un chemin possible */
		while(this.steps > 0) {
			/* Init des offset  */
			if(this.order.getPosition().x < this.location.x) {
				offsets[0] = new Int2D(-1,0);
			}
			else
				offsets[0] = new Int2D((this.order.getPosition().x == this.location.x) ? 0 : 1,0);
			
			if(this.order.getPosition().y < this.location.y) {
				offsets[1] = new Int2D(0,-1);
			}
			else
				offsets[1] = new Int2D(0,(this.order.getPosition().y == this.location.y) ? 0 : 1);
			
			
			/* Init des distances selon les coordonnï¿½es */
			distanceCoordTemp = new Int2D(Math.abs(this.order.getPosition().x - this.location.x)
										 ,Math.abs(this.order.getPosition().y - this.location.y)); 
			
			/* On se dÃ©place selon la stratÃ©gie */
			if(MoveOnNewCellAccordingToColor(StrategyMove(),offsets,distanceCoordTemp)) {
				continue;
			}
			/* On va sur une case retournÃ© par OtherWise */
			else {
				if(MoveOnNewCellAccordingToColor(OtherWiseMove(),offsets,distanceCoordTemp))
					continue;
				else if(MoveOnNewCellAccordingToColor(WorstCaseMove(),offsets,distanceCoordTemp))
					continue;
				else
					// On ne peut plus bouger 
					break;
			}
		}
	}
		
	public void compareDestinations(){
	
	}
	
	/**
	 * Se dÃ©place sur une nouvelle case selon la couleur dÃ©sirÃ©
	 * @param desiredColor -> Couleur sur laquelle on souhaite se dÃ©placer
	 * @param offset -> offset pour repÃ©rer les cases adjacentes
	 * @param dist -> distance selon chaque axe entre la destination et la position actuelle
	 */
	private boolean MoveOnNewCellAccordingToColor(Color desiredColor,Int2D[] offsets,Int2D dist) {
		/* Variable locale */
		boolean isSameColor;
		int numberOfSteps;
		
		/* SÃ©curitÃ© d'utilisation */
		if(offsets.length != 2) {
			return false;
		}
		
		/* Init number of steps */
		if(desiredColor == this.colorAgent)
			numberOfSteps = 1;
		else
			numberOfSteps = (desiredColor == Color.None) ? 2 : 3;
		
		/* On quitte si on ne peut pas se dÃ©placer */
		if(this.steps < numberOfSteps)
			return false;
		
		/* On regarde si les cases sont de mÃªme couleur */
		isSameColor = true;
		Color prec = Color.None;
		for(int i = 0; i < offsets.length; i++)
			if(i != 0) {
				isSameColor &= 
					Statics.GetColorOfCase(this.grid,this.location.x,this.location.y,offsets[i].x,offsets[i].y) == prec;
			}
			else
				prec = Statics.GetColorOfCase(this.grid,this.location.x,this.location.y,offsets[i].x,offsets[i].y);
		
		/* Si cases de la mÃªme couleur */
		if(isSameColor) {
			if(Statics.GetColorOfCase(this.grid,this.location) == desiredColor) {
				// On se dÃ©place le long de la coordonnÃ©e oÃ¹ il y a le plus de distance Ã  parcourir
				if(dist.x > dist.y)
					this.setNewPosition(this.location.x + offsets[0].x,this.location.y);
				else if(dist.x < dist.y)
					this.setNewPosition(this.location.x,this.location.y + offsets[1].y);
				else { // On se déplace aléatoirement
					if(this.grid.random.nextBoolean())
						this.setNewPosition(this.location.x + offsets[0].x,this.location.y);
					else
						this.setNewPosition(this.location.x,this.location.y + offsets[1].y);
				}
				this.steps -= numberOfSteps;
				return true;
			}
			/* On gÃ¨re ce cas Ã  un plus haut niveau que cette mÃ©thode Ã  cause des steps */
			else {
				return false;
			}
		}
		/* On cherche une case qui est de la mÃªme couleur que celle dÃ©sirÃ© */
		else {
			for(int i = 0;i < offsets.length;i++) {
				if(Statics.GetColorOfCase(this.grid, this.location.x + offsets[i].x,this.location.y + offsets[i].y) == desiredColor) {
					this.setNewPosition(this.location.x + offsets[i].x, this.location.x + offsets[i].y);
					this.steps -= numberOfSteps;
					return true;
				}
			}
			return false;
		}
	}
	
	/**
	 * Permet de savoir si l'agent peut colorier une case adverse
	 * @return
	 */
	public boolean CanColorOppositeCase(){
		return this.numberOfTubeOfPaint > 2;
	}
	
	/**
	 * Renvoie un boolï¿½en qui permet de vï¿½rifier si la charge de peinture est ï¿½ son max
	 * @return
	 */
	public boolean isFullyLoadedOfPaint(){
		return this.numberOfTubeOfPaint == Constants.MAX_TUBE_OF_PAINT;
	}
	
	
	/**
	 * Permet de savoir si l'agent a des tubes de peintures
	 * @return boolï¿½en
	 */
	public boolean HasPaint(){
		return this.numberOfTubeOfPaint == 0;
	}
	
	// Partie communication / Execution ordre
	
	public boolean isThereNewOrders(){
		
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
	// valeur absolue de la difference des ordonnÃ©es (fonction calculateDistanceScore)
	public Order compareAndChooseOrder(ArrayList<Order> orders){
		
		int min = -1; // n'a pas encore de valeur mais en aura puisque minimum un ordre si 
		// fonction est appelÃ©e.
		Order result = null;
		
		for (Order order : orders){
			int distance = calculateDistanceScore(order.getPosition());
			if (min == -1 || (distance < min) ){
				min = calculateDistanceScore(order.getPosition());
				result = order;
			}
		}
		
		return result;
		
	}
	
	/**
	 * Permet de choisir entre la destination/ objectif actuelle et une destination parmi les ordres
	 * @param orders -> Les nouveaux ordres
	 */
	private void chooseBetweenDestinationAndNewOrders(ArrayList<Order> orders){
		/* Si l'ordre est de type "pot de peinture" et que l'agent est déja chargé, on ignore l'ordre */
		
		
		
		/* Récupération de la destination la plus proche parmi les ordres */
		Order bestOrder = this.compareAndChooseOrder(orders);
		
		/* Récupération des distances par rapport à l'agent */
		int dist_order = this.calculateDistanceScore(bestOrder.getPosition());
		int dist_destination = this.calculateDistanceScore(this.order.getPosition());
		
		/* Mis à jour si le nouvel ordre est meilleur */
		if(dist_order < dist_destination)
			this.order.setPosition(bestOrder.getPosition());
	}

	public void answerToScout(){
		// vrmt necessaire ?
		
	
	}
	
	private int calculateDistanceScore(Int2D targetLocation){
		
		int x = Math.abs(this.location.x + targetLocation.x);
		int y = Math.abs(this.location.y + targetLocation.y);
		return x+y;
	}
}
