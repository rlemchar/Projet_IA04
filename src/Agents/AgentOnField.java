package Agents;

import model.GridModel;

import java.util.ArrayList;
import java.util.Random;

import model.Color;
import model.CaseColorListWrap;

import sim.engine.SimState;
import sim.engine.Steppable;
import sim.engine.Stoppable;
import sim.util.Bag;
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
	protected Color colorAgent;
	
	/* Opposite color */
	protected Color oppositeColor;
	
	/* Constructeur par defaut */
	public AgentOnField() {
		this.grid = null;
		this.steps = Constants.MAX_STEPS;
	}
	
	/* Constructeur avec couleur agent */
	public AgentOnField(Color colorAgent){
		this();
		this.colorAgent = colorAgent;
		this.oppositeColor = (this.colorAgent == Color.Blue) ? Color.Red : Color.Blue;
	}
	
	/* Step */
	@Override
	public void step(SimState state) {
		
		this.grid = (GridModel) state;
		this.location = this.grid.getGrid().getObjectLocation(this);
		this.steps = Constants.MAX_STEPS;
	}
	
	protected Int2D moveRandom() {
		Int2D newLocation = null;

		Random generator = new Random();
		int rand = generator.nextInt(4);
		
		switch(rand)
		{
			case 0:
				if(this.location.x < 49) newLocation =  new Int2D(this.location.x + 1, this.location.y);
				break;
			case 1:
				if(this.location.x > 0) newLocation =  new Int2D(this.location.x - 1, this.location.y);
				break;
			case 2:
				if(this.location.y < 49) newLocation =  new Int2D(this.location.x, this.location.y + 1);
				break;
			case 3:
				if(this.location.y > 0) newLocation =  new Int2D(this.location.x, this.location.y - 1);
				break;
		}
		return newLocation;
	}
	
	
	protected Int2D avoidEdge() {
		Int2D newLocation = null;
		if(this.location.x >= Constants.GRID_SIZE - Constants.PERCEPTION_FOR_SCOUT_AGENT)
			newLocation =  new Int2D(this.location.x - 1, this.location.y);
		if(this.location.x <= Constants.PERCEPTION_FOR_SCOUT_AGENT)
			newLocation =  new Int2D(this.location.x + 1, this.location.y);
		if(this.location.y >= Constants.GRID_SIZE - Constants.PERCEPTION_FOR_SCOUT_AGENT)
			newLocation =  new Int2D(this.location.x, this.location.y - 1);
		if(this.location.y <= Constants.PERCEPTION_FOR_SCOUT_AGENT)
			newLocation =  new Int2D(this.location.x, this.location.y + 1);
		
		return newLocation;
	}
	
	

	
	
	
	
	/* Permet à un agent de percevoir */
	public ArrayList<Integer> determineLimits(){      // determine les limites de la perception en prenant en compte la taille de la grille
		
		ArrayList<Integer> limits = new ArrayList<Integer>();
		
		if (this.location.x < this.powerOfPerception) {
			limits.add(0);
		}
		else {
			limits.add(this.location.x - this.powerOfPerception);
		}
		if (this.location.x > Constants.GRID_SIZE - this.powerOfPerception) {
			limits.add(Constants.GRID_SIZE);
		}
		else {
			limits.add(this.location.x + this.powerOfPerception);
		}
		
		if (this.location.y < this.powerOfPerception) {
			limits.add(0);
		}
		else {
			limits.add(this.location.y - this.powerOfPerception);
		}
		if (this.location.y > Constants.GRID_SIZE - this.powerOfPerception) {
			limits.add(Constants.GRID_SIZE);
		}
		else {
			limits.add(this.location.y + this.powerOfPerception);
		}
		
		return limits;
	}
	
	
	public ArrayList<Int2D> perceive(){
		ArrayList<Integer> limits = new ArrayList<Integer>();
		limits = determineLimits();
		Integer lower_bound_x = limits.get(0);
		Integer upper_bound_x = limits.get(1);
		Integer lower_bound_y = limits.get(2);
		Integer upper_bound_y = limits.get(3);

		ArrayList<Int2D> allCoordsFromPerception = new ArrayList<Int2D>();
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
	
	/** Strategie de base pour tout les agents lors des déplacements **/
	@Override
	public Color StrategyMove() {
		return getColorAgent();
	}
	
	@Override 
	public Color OtherWiseMove() {
		return Color.None;
	}
	
	@Override 
	public Color WorstCaseMove() {
		return this.oppositeColor;
	}
	
	/**
	 * Déplace l'agent sur une nouvelle case
	 * @param location -> Int2D
	 */
	public void setNewPosition(Int2D location) {
		this.grid.getGrid().setObjectLocation(this, location);
	}
	
	/**
	 * Déplace l'agent sur une nouvelle case
	 * @param x
	 * @param y
	 */
	public void setNewPosition(int x,int y) {
		this.grid.getGrid().setObjectLocation(this, x, y);
	}
	
	/* Getteurs */
	public Color getColorAgent() { return this.colorAgent; }
	
	/* Setteurs */
	public void setColorAgent(Color colorAgent) { this.colorAgent = colorAgent; }
	public void setGrid(GridModel grid) { this.grid = grid; }
	
	
	public Stoppable getStop() {
		return stop;
	}

	public void setStop(Stoppable stop) {
		this.stop = stop;
	}
	
	public Int2D getLocation(){
		return this.location;
	}
}
