package Agents;

import sim.engine.SimState;
import sim.engine.Steppable;
import sim.util.Int2D;
import util.Constants;
import util.Statics;
import util.TargetType;

import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.Comparator;

import model.CaseColor;
import model.MyColor;
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
	
	/* Booléen indiquant si l'agent a actuellement une destination précise où aller */
	private boolean hasAdestination;
	
	/* Destination objectif de l'agent */
	private Order order;
	
	/* Pouvoir de coloration */
	private int powerOfColoration;
	
	/* Cible de l'agent*/
	private TargetType target;
	
	/** Constructeur par d�faut **/
	public ColoringAgent() {
		super();
		this.numberOfTubeOfPaint = 0;
		this.hasAdestination = false;
		this.order = null;
		this.powerOfPerception = Constants.PERCEPTION_FOR_COLORING_AGENT;
		this.powerOfColoration = Constants.COLORATION_POWER_FOR_COLORING_AGENT;
		this.target = TargetType.paintPot;
	}
	
	/**
	 * Constructeur en initialisant la couleur de l'agent
	 * @param colorAgent Couleur de l'agent
	 */
	public ColoringAgent(MyColor colorAgent) {
		super(colorAgent);
		this.numberOfTubeOfPaint = 0;
		this.hasAdestination = false;
		this.order = null;
		this.powerOfPerception = Constants.PERCEPTION_FOR_COLORING_AGENT;
		this.powerOfColoration = Constants.COLORATION_POWER_FOR_COLORING_AGENT;
		this.target = TargetType.paintPot;
	}
	
	@Override
	public void step(SimState state) {
		super.step(state);
		
		/* L'agent cherche des pots de peinture */
		if (this.target == TargetType.paintPot){
			/* Si l'agent a rechargé , on efface sa mission */
			if(this.rechargePaint(Statics.getPaintPot(this.grid, this.location.x,this.location.y))){
				this.resetTarget();
			}
		}
		// L'agent cherche à colorier un groupe de cases
		else{
			/* Si l'agent est arrivé à destination */
			if(this.location == this.getDestination()) { 
				this.Color();
			}
			
			/* Sinon on regarde si on colorie ou non la zone actuelle */
			else {
				if(this.order != null) {
					boolean compScore = Statics.computeScoreCell(this.grid, this.order.getPosition(),this.colorAgent) > Statics.computeScoreCell(this.grid,this.location,this.colorAgent);
					if(compScore)
						this.Color();
				}
			}
		}
		
		/* Sinon on se déplace */
		this.move();
	}
	
	/**
	 * Implémentation de move au niveau de l'agent Colorieur
	 */
	@Override
	public void move() {
		/* Variable locale */
		Order newOrder = this.lookForBestNewOrder();
		
		/* Mis à jour l'ordre 
		 * -> Si pas d'ordre , on prend le nouvel ordre 
		 * -> Sinon on prend soit le nouveau soit on garde l'actuel */
		if (newOrder != null){
			this.order = (this.order != null) ? this.compareCurrentOrderWithNewOne(newOrder) : newOrder;
		}
		
		/* Mis à jour hasADestination */
		this.hasAdestination = (this.order != null);
		
		/* Déplacement */
		if(this.getDestination() != null)
			this.moveTowardsDestination();
		else {
			//System.out.println(this.location);
			this.setNewPosition(this.moveRandom());
			//System.out.println(this.location);
		}
	}
	

	/**
	 * Permet de colorier une zone 
	 * EDIT : La zone de coloration est differente de la zone de perception
	 */
	public boolean Color(){
		/* Variables locales*/
		Stream<CaseColor> colorZoneFiltered;
		CaseColor[] cellWithOppositeColor;
		
		System.out.println("Je vais colorier !");
		
		/* R�cup�ration de la zone de coloriage -> Uniquement les cases qui ne sont pas de la couleur de l'agent*/
		colorZoneFiltered = Statics.GetZoneColor(grid, new Int2D(
									this.location.x - this.powerOfColoration,
									this.location.y - this.powerOfColoration
								), this.location.x + this.powerOfColoration, this.location.y + this.powerOfColoration
							).stream().filter(cell -> cell.getColor() != this.colorAgent);
		
		/* R�cup�ration des cases avec la couleur de l'�quipe adverse */
		cellWithOppositeColor = colorZoneFiltered.filter(cell -> cell.getColor() == this.oppositeColor).toArray(CaseColor[]::new);
		
		/* On ne colorie pas si le nombre de cases � colorier est inf�rieur au seuil */
		if(colorZoneFiltered.count() >= Constants.THRESHOLD_FOR_PAINTING_A_ZONE){
			/* On colorie et/ou on rend les cases neutre d'abord */
			for(CaseColor cell : colorZoneFiltered.toArray(CaseColor[]::new)){
				if(this.numberOfTubeOfPaint > 0){
					cell.setColor(cell.getColor() == this.oppositeColor ? MyColor.None : this.colorAgent);
					this.numberOfTubeOfPaint--;
				}		
				else
					break;	
			}
			
			/* On colorie les cases neutres restantes */
			for(CaseColor cell : cellWithOppositeColor){
				if(this.numberOfTubeOfPaint > 0){
					cell.setColor(this.colorAgent);
					this.numberOfTubeOfPaint--;
				}
				else
					break;
			}
			
			/* La mission est terminée */
			this.resetTarget();
			
			return true;
		}
		return false;
		
	}
	
	/**
	 * Permet de recharger le nombre de tubes de peinture
	 * @param pot -> Pot de peinture � la position de l'agent
	 */
	public boolean rechargePaint(PaintPot pot){
		/* On vérifie que le pot de peinture existe */
		if(pot == null)
			return false;
	
		/* On remplit les tubes de peintures */
		if(!this.isFullyLoadedOfPaint()){
			while(this.isFullyLoadedOfPaint() && pot.getQuantity() != 0){
				this.numberOfTubeOfPaint++;
				pot.decQuantity();
			}
		}
		
		/* Recharge effectué -> Si l'agent est remplit , on retourne true quand même */
		return true;
	}
	
	/**
	 * Permet à un agent de se déplacer vers une destination précise
	 */
	public void moveTowardsDestination(){
		/* Variables locales */
		Int2D[] offsets = new Int2D[2]; // offset pour les cases adjacentes à r�cup�rer -> 0 pour l'axe x , 1 pour l'axe y
		Int2D distanceCoordTemp;
		
		/* On vérifie si l'agent a bien une destination précise 
		 * ou si l'agent n'est pas déja à la bonne destination */
		if(!(this.hasAdestination) || this.order.getPosition().x == this.location.x && this.order.getPosition().y == this.location.y)
			return;
		
		/* On se d�place sur nos cases si possible -> cases faisant partie d'un chemin possible */
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
			
			
			/* Init des distances selon les coordonn�es */
			distanceCoordTemp = new Int2D(Math.abs(this.order.getPosition().x - this.location.x)
										 ,Math.abs(this.order.getPosition().y - this.location.y)); 
			
			/* On se déplace selon la stratégie */
			if(MoveOnNewCellAccordingToColor(StrategyMove(),offsets,distanceCoordTemp)) {
				continue;
			}
			/* On va sur une case retourné par OtherWise */
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
	
	/**
	 * Se déplace sur une nouvelle case selon la couleur désiré
	 * @param desiredColor -> Couleur sur laquelle on souhaite se déplacer
	 * @param offset -> offset pour repérer les cases adjacentes
	 * @param dist -> distance selon chaque axe entre la destination et la position actuelle
	 */
	private boolean MoveOnNewCellAccordingToColor(MyColor desiredColor,Int2D[] offsets,Int2D dist) {
		/* Variable locale */
		boolean isSameColor;
		int numberOfSteps;
		
		/* Sécurité d'utilisation */
		if(offsets.length != 2) {
			return false;
		}
		
		/* Init number of steps */
		if(desiredColor == this.colorAgent)
			numberOfSteps = 1;
		else
			numberOfSteps = (this.oppositeColor == desiredColor) ? 3 : 2;
		
		/* On quitte si on ne peut pas se déplacer */
		if(this.steps < numberOfSteps)
			return false;
		
		/* On regarde si les cases sont de même couleur */
		isSameColor = true;
		MyColor prec = MyColor.None;
		for(int i = 0; i < offsets.length; i++)
			if(i != 0) {
				isSameColor &= 
					Statics.GetColorOfCase(this.grid,this.location.x,this.location.y,offsets[i].x,offsets[i].y) == prec;
			}
			else
				prec = Statics.GetColorOfCase(this.grid,this.location.x,this.location.y,offsets[i].x,offsets[i].y);
		
		
		/* Si cases de la même couleur */
		if(isSameColor) {
			if(desiredColor == Statics.GetColorOfCase(this.grid, this.location.x, this.location.y, offsets[0].x, offsets[0].y)) {
				// On se déplace le long de la coordonnée où il y a le plus de distance à parcourir
				if(dist.x > dist.y)
					this.setNewPosition(this.location.x + offsets[0].x,this.location.y);
				else if(dist.x < dist.y)
					this.setNewPosition(this.location.x,this.location.y + offsets[1].y);
				else { // On se d�place al�atoirement
					if(this.grid.random.nextBoolean())
						this.setNewPosition(this.location.x + offsets[0].x,this.location.y);
					else
						this.setNewPosition(this.location.x,this.location.y + offsets[1].y);
				}

				this.steps -= numberOfSteps;
				return true;
			}
			/* On gère ce cas à un plus haut niveau que cette méthode à cause des steps */
			else {
				return false;
			}
		}
		/* On cherche une case qui est de la même couleur que celle désiré */
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
	 * Renvoie un bool�en qui permet de v�rifier si la charge de peinture est � son max
	 * @return
	 */
	public boolean isFullyLoadedOfPaint(){
		return this.numberOfTubeOfPaint == Constants.MAX_TUBE_OF_PAINT;
	}
	
	// Partie communication / Execution ordre
	
	/**
	 * Donne l'ordre le plus interessant parmis ceux reçus par l'agent compte tenu de l'objectif de l'agent
	 * La fonction attribut l'ordre à l'agent
	 * Si l'objectif est de trouver de la peinture, le plus intéressant est le plus proche
	 * Si l'objectif est de peindre, le plus intéressant est celui qui a le meilleur score
	 * @return
	 */
	public Order lookForBestNewOrder(){
		/* Variable locale */
		Comparator<Order> comp; //Comparateur pour les ordres
	
		/* Récupération des derniers ordres reçues */
		ArrayList<Order> lastOrders = CommunicationSystem.consultOrders(this);
		
		/* On ne fait rien si pas d'ordre */
		if (lastOrders.isEmpty())
			return null;
		
		/* Filtrage des ordres */
		ArrayList<Order> filteredOrders = lastOrders
									.stream()
									.filter(order -> order.getTargetType() == this.target)
									.collect(Collectors.toCollection(ArrayList::new));;	
		
		/* On ne fait rien si pas d'ordre */
		if(filteredOrders.isEmpty())
			return null;
		
		/* Récupération du meilleur ordre dans le contexte de l'agent */
		switch(this.target) {
			// L'agent cherche de la peinture : le meilleur ordre est le pot le plus proche
			case land:
				comp = (order1,order2) -> Integer.compare(
						Statics.absoluteDistance(this.location, order1.getPosition())
					   ,Statics.absoluteDistance(this.location, order2.getPosition())
					);
				break;
			// L'agent cherche à colorier : le meilleur ordre est le meilleur score
			case paintPot:
				comp = (order1,order2) -> Integer.compare(
						 order1.getScoreForLandType()
						,order2.getScoreForLandType()
					);
				break;
			default:
				return null;
			
		}
		return filteredOrders.stream().min(comp).get();
		
	}
	
	/**
	 * Cette fonction compare l'ordre actuel à un autre nouvellement reçu (choisi par la fonction
	 * lookForBestOrder())
	 * Elle modifie l'ordre objectif si le nouvel ordre est plus interessant
	 * @param newOrder
	 * @returns soit l'ordre en paramètre soit l'ordre courant
	 */
	public Order compareCurrentOrderWithNewOne(Order newOrder){
		/* Variables locales */
		boolean comp;
		Order result = this.order;
		
		// L'objectif de l'agent est de trouver de la peinture : Le plus proche est le mieux
		if (this.target == TargetType.paintPot){
			comp = Statics.absoluteDistance(this.location, newOrder.getPosition()) < Statics.absoluteDistance(this.location, this.order.getPosition());
			if (comp){
				result = newOrder;
			}			
		}
		// Sinon L'objectif de l'agent est de peindre : Il cherche la case au meilleur score 
		else{
			comp = Statics.computeScoreCell(this.grid, this.order.getPosition(), this.colorAgent) > Statics.computeScoreCell(this.grid, newOrder.getPosition(), this.colorAgent);
			if(comp){
				result = newOrder;
			}
		}
		
		return result;
	}
	
	/**
	 * Effacement de la "mission"
	 */
	private void resetTarget(){
		this.order = null;
		this.target = (this.target == TargetType.paintPot) ? TargetType.land : TargetType.paintPot;
	}
	
	private Int2D getDestination(){
		if (this.order == null){
			return null;
		}else{
			return this.order.getPosition();
		}
	}
}

