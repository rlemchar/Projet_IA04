package Agents;

import model.GridModel;

import java.util.ArrayList;
import java.util.Random;

import model.MyColor;
import model.CaseColor;
import model.CaseColorListWrap;

import sim.engine.SimState;
import sim.engine.Steppable;
import sim.engine.Stoppable;
import sim.util.Int2D;
import util.Constants;
import util.Statics;

import strategies.IStrategyMove;


/**
 * 
 * @author wakidou
 * ----------------------------------------------------------------------------------
 * 		Classe de base pour les agents sur la grille 
 * ----------------------------------------------------------------------------------
 */
public abstract class AgentOnField implements Steppable,IStrategyMove{
	
	protected Stoppable stop;
	
	/**
	 * Serial version
	 */
	private static final long serialVersionUID = 7976565556245180759L;

	/* Grid */
	protected GridModel grid;
	
	/* Location of this */
	protected Int2D location;
	
	/* Perception */
	protected int powerOfPerception;
	
	/* steps */
	protected int steps;
	
	/* Agent Color */
	protected MyColor colorAgent;
	
	/* Opposite color */
	protected MyColor oppositeColor;
	
	/* Constructeur par defaut */
	public AgentOnField() {
		this.grid = null;
		this.steps = Constants.MAX_STEPS;
	}
	
	/* Constructeur avec couleur agent */
	public AgentOnField(MyColor colorAgent){
		this();
		this.colorAgent = colorAgent;
		this.oppositeColor = (this.colorAgent == MyColor.Blue) ? MyColor.Red : MyColor.Blue;
	}
	
	/* Step */
	@Override
	public void step(SimState state) {
		this.grid = (GridModel) state;
		this.location = this.grid.getGrid().getObjectLocation(this);
		this.steps = Constants.MAX_STEPS;
	}
	
	public void TotalMove() {       //cette fonction permet de g�rer les points d'�nergie/ le co�t des d�placements
		CaseColor currentColor = (CaseColor)Statics.Get(this.grid.getGrid().getObjectsAtLocation(this.location.x, this.location.y),CaseColor.class);
		int costOfMove = Statics.computeCost(currentColor.getColor(), this.getColorAgent());
		while (this.steps >= costOfMove) {
			moveWithoutObjective();
			this.steps = this.steps - costOfMove;
			currentColor = (CaseColor)Statics.Get(this.grid.getGrid().getObjectsAtLocation(this.location.x, this.location.y),CaseColor.class);
			costOfMove = Statics.computeCost(currentColor.getColor(), this.getColorAgent());
		}
		this.grid.getGrid().setObjectLocation(this, this.location);
	}
	
	
	/**
	 * Bouger sans objectif
	 */
	public void moveWithoutObjective(){		
		// Mouvement al�atoire
		this.location = this.moveRandom();
	}
	
	public Int2D moveRandom() {
		/* Variables locales */
		int rand = new Random().nextInt(4);
		int offset = 0;
		
		/* Mis à jour offset */
		switch(rand)
		{
			case 0:
				offset = (this.location.x < this.grid.getGrid().getHeight()) ? 1 : -1;
				break;
			case 1:
				offset = (this.location.x > 0) ? -1 : 1;
				break;
			case 2:
				offset = (this.location.y < this.grid.getGrid().getWidth()) ? 1 : -1;
				break;
			case 3:
				offset = (this.location.y > 0) ? -1 : 1;
				break;
		}
		
		return (rand < 2) ? 
				new Int2D(this.location.x + offset,this.location.y) 
			  : new Int2D(this.location.x,this.location.y + offset);
	}
	
	/* Permet à un agent de percevoir */
	public int[][] determineLimits(){      // determine les limites de la perception en prenant en compte la taille de la grille
		/* Variable locale */
		int[][] limits = new int[2][2];
		
		/* Mis à jour des limites */
		limits[0][0] = (this.location.x >= this.powerOfPerception) ? 
				this.location.x - this.powerOfPerception : 0;
		limits[0][1] = (this.location.x > Constants.GRID_SIZE - this.powerOfPerception) ? 
				Constants.GRID_SIZE : this.location.x + this.powerOfPerception;
		limits[1][0] = (this.location.y >= this.powerOfPerception) ? 
				this.location.y - this.powerOfPerception : 0;
		limits[1][1] = this.location.y > Constants.GRID_SIZE - this.powerOfPerception ?
				Constants.GRID_SIZE : this.location.y + this.powerOfPerception;
		
		return limits;
	}
	
	/**
	 * Méthode pour percevoir les alentours
	 * @return Les coordonnées sous forme de liste
	 */
	public ArrayList<Int2D> perceive(){
		/* Variables locales */
		int[][] limits = new int[2][2];
		ArrayList<Int2D> allCoordsFromPerception = new ArrayList<Int2D>();
		
		/* Récupération des limites de la perception */
		limits = determineLimits();		
		int lower_bound_x = limits[0][0];
		int upper_bound_x = limits[0][1];
		int lower_bound_y = limits[1][0];
		int upper_bound_y = limits[1][1];

		/* Perception */
		for(int x = lower_bound_x; x < upper_bound_x;x++){
			for(int y =  lower_bound_y; y < upper_bound_y;y++){
				allCoordsFromPerception.add(new Int2D(x,y));
			}
		}
		
		return allCoordsFromPerception;
	}


	/**
	 * Recherche une case où un agent peut aller 
	 * @return -> Position idéale pour bouger
	 */
	public Int2D getNewLocation(){
		/* Les variables */
		ArrayList<Int2D> cells;
		CaseColorListWrap lists = null;
		Random r = new Random();
		
		/* Récupération des infos couleurs des cases perçues */
		lists = Statics.GetColorOfCases(this.grid, perceive());
		
		/* Stratégie */
		/* On regarde si il y a des cases de même couleur que l'agent */
		cells = lists.getOneList(this.colorAgent);
		if(!cells.isEmpty()) {
			this.steps--;
			return cells.get(r.nextInt(cells.size())); 
		}
		
		/* On regarde les cases non colorés */
		cells = lists.getNone();
		if(!cells.isEmpty() && this.steps >= 2) {
			this.steps -= 2;
			return cells.get(r.nextInt(cells.size())); 
		}
		
		/* On va en territoire ennemi */
		cells = lists.getOneList(this.oppositeColor);
		if(!cells.isEmpty() && this.steps == Constants.MAX_STEPS) {
			this.steps = 0;
			return cells.get(r.nextInt(cells.size())); 
		}
		
		/* Sinon on ne bouge pas */
		return null;
	}
	
	
	
	/**
	 * Permet de se déplacer selon la stratégie ou mission courante 
	 */
	public abstract void move();
	
	/** Strategie de base pour tout les agents lors des déplacements **/
	@Override
	public MyColor StrategyMove() {
		return getColorAgent();
	}
	
	@Override 
	public MyColor OtherWiseMove() {
		return MyColor.None;
	}
	
	@Override 
	public MyColor WorstCaseMove() {
		return this.oppositeColor;
	}
	
	/**
	 * Déplace l'agent sur une nouvelle case
	 * @param location -> Int2D
	 */
	public void setNewPosition(Int2D location) {
		this.grid.getGrid().setObjectLocation(this, location);
		this.location = location;
	}
	
	/**
	 * Déplace l'agent sur une nouvelle case
	 * @param x
	 * @param y
	 */
	public void setNewPosition(int x,int y) {
		this.grid.getGrid().setObjectLocation(this, x, y);
		this.location = new Int2D(x,y);
	}
	
	/* Getteurs */
	public MyColor getColorAgent() { return this.colorAgent; }
	
	/* Setteurs */
	public void setColorAgent(MyColor colorAgent) { 
		this.colorAgent = colorAgent; 
		this.oppositeColor = (this.colorAgent == MyColor.Blue) ? MyColor.Red : MyColor.Blue;
	}
	public void setGrid(GridModel grid) { this.grid = grid; }
	public void setLocation(Int2D location) { this.location = location; }
	
	public Stoppable getStop() {
		return stop;
	}

	public void setStop(Stoppable stop) {
		this.stop = stop;
	}
	
	/* Getteur pour location */
	public Int2D getLocation() {
		return this.location;
	}
}
