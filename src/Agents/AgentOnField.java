package Agents;

import model.GridModel;

import java.util.ArrayList;
import java.util.Random;

import model.Color;
import model.CaseColorListWrap;

import sim.engine.SimState;
import sim.engine.Steppable;
import sim.engine.Stoppable;

import sim.util.Int2D;
import util.Constants;
import util.Statics;


/**
 * 
 * @author wakidou
 * ----------------------------------------------------------------------------------
 * 		Classe de base pour les agents sur la grille 
 * ----------------------------------------------------------------------------------
 */
public abstract class AgentOnField implements Steppable{
	
	protected Stoppable stop;
	
	/**
	 * Serial version
	 */
	private static final long serialVersionUID = 7976565556245180759L;

	/* Grid */
	protected GridModel grid;
	
	/* Location */
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
	}
	
	/* Permet à un agent de bouger */
	public void move(){
		/* Les variables */
		Int2D newLocation;
		
		/* On bouge seulement si la grille est disponible */
		if(this.grid != null){
			newLocation = getNewLocation();
			if(newLocation != null) {
				this.grid.getGrid().setObjectLocation(this, newLocation);
			}
		}
	}
	
	/* Permet à un agent de percevoir */
	public ArrayList<Int2D> perceive(){
		ArrayList<Int2D> allCoordsFromPerception = new ArrayList<Int2D>();
		for(int x = this.location.x - this.powerOfPerception; x < this.location.x + this.powerOfPerception;x++){
			for(int y = this.location.y - this.powerOfPerception;y < this.location.y + this.powerOfPerception;y++){
				allCoordsFromPerception.add(new Int2D(x,y));
			}
		}
		return allCoordsFromPerception;
	}

	/**
	 * Recherche une case o� un agent peut aller 
	 * @return -> Position idéale pour bouger
	 */
	private Int2D getNewLocation(){
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
	
	
	
}
