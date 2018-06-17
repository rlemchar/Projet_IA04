package Agents;

import java.util.ArrayList;

import util.Constants;
import util.Statics;
import model.MyColor;
import model.CommunicationSystem;
import model.Order;
import model.PaintPot;
import sim.engine.SimState;
import sim.engine.Steppable;
import sim.util.Bag;
import sim.util.Int2D;


public class ScoutAgent extends AgentOnField implements Steppable {
	
	private ArrayList<Int2D> lastPerception;
	private ArrayList<Int2D> lastPaintPotDetected;
	private ArrayList<Int2D> lastOwnLandDetected;
	private ArrayList<ColoringAgent> lastAgentsDetected;
	
	public ScoutAgent() {
		super();
		this.powerOfPerception = Constants.PERCEPTION_FOR_SCOUT_AGENT;
	}
	
	public ScoutAgent(MyColor colorAgent) {
		super(colorAgent);
	}

	/**
	 * Serial Version
	 */
	private static final long serialVersionUID = 7682473999209704532L;

	@Override
	public void step(SimState state) {
		super.step(state);
		this.lastPerception = perceive();
		this.move();
		this.detectRelevantInformation();
		this.informOthers();
		this.resetDetections();
	}

	@Override
	public void moveWithoutObjective(){	
		// Mouvement aleatoire
		this.location = this.moveRandom();
		//	S'�carte des autres Scouts Agents de la meme couleur
		if(this.avoidScoutAgent() != null) this.location = this.avoidScoutAgent();
		//	S'ecarte des bords pour couvrir le maximum de surface
		if(this.avoidEdge() != null) this.location = this.avoidEdge();
	}
	
	@Override
	public void move() {
		this.TotalMove();
	}
	
	private Int2D avoidEdge() {
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
	

	private Int2D avoidScoutAgent() {
		Int2D newLocation = null;
		for(Int2D cell : this.lastPerception) {
			ScoutAgent secondScout = (ScoutAgent)Statics.Get(this.grid.getGrid().getObjectsAtLocation(cell.x, cell.y),ScoutAgent.class);
			if (secondScout != null && secondScout != this && secondScout.getColorAgent() == this.colorAgent){
				// Il y'a un agent scout dans la case, on s'en eloigner
				int distanceX = cell.x  - this.location.x; // Si > 0 aller � gauche
				int distanceY = cell.y - this.location.y;  // Si > 0 aller en bas
				
				if(Math.abs(distanceX) > Math.abs(distanceY)){
					if(distanceY > 0)
						newLocation =  new Int2D(this.location.x, this.location.y - 1);
					else
						newLocation =  new Int2D(this.location.x, this.location.y + 1);
				}
				else{
					if(distanceX > 0)
						newLocation =  new Int2D(this.location.x - 1, this.location.y);
					else
						newLocation =  new Int2D(this.location.x + 1, this.location.y);
				}
			}
			
		}
		
		return newLocation;
	}
	
	public void informOthers(){
		
		if (!lastAgentsDetected.isEmpty()){
			
			Int2D mostPertinentPaintPot;
			Int2D mostPertinentLand;
						
			for (ColoringAgent coloringAgent : this.lastAgentsDetected){
				mostPertinentPaintPot = getMostPertinentPaintPotLocation(coloringAgent);
				mostPertinentLand = getMostPertinentLandLocation(coloringAgent);

				if (mostPertinentPaintPot != null){
					Order order = new Order(this,coloringAgent, mostPertinentPaintPot);
					CommunicationSystem.addOrder(order);
				}
				if (mostPertinentLand != null){
					Order order = new Order(this,coloringAgent, mostPertinentLand,Statics.computeScoreCell(this.grid,mostPertinentLand,this.colorAgent));
					CommunicationSystem.addOrder(order);
				}
			}
			
		}
	}
			
	private ArrayList<Int2D> filterPaintPots(){
		// Filtering info to target paint pots
		
		ArrayList<Int2D> paintPotsLocations = new ArrayList<Int2D>();
		
		for (Int2D perceivedCell : this.lastPerception){    
			Bag objects = this.grid.getGrid().getObjectsAtLocation(perceivedCell.x, perceivedCell.y);
			if (objects != null){
				for (Object obj : objects){
					if (obj.getClass() == PaintPot.class){
						paintPotsLocations.add(perceivedCell);
					}
				}
			}
		}
		
		return paintPotsLocations;
	}
	
	private ArrayList<ColoringAgent> filterAgents(){
		ArrayList<ColoringAgent> agentsToInform = new ArrayList<ColoringAgent>();
		
		// On regarde si des agents colorieur sont présents dans le champ de perception
		for (Int2D perceivedCell : this.lastPerception){  
			Bag objects = this.grid.getGrid().getObjectsAtLocation(perceivedCell.x, perceivedCell.y);
			if (objects != null){
				for (Object obj : objects){
					if (obj instanceof ColoringAgent){
						if (((ColoringAgent)obj).colorAgent == this.colorAgent){
							// Il y'a un agent colorieur du même camp dans la case, on doit l'ajouter à la liste des destinataires
							agentsToInform.add((ColoringAgent)obj);
						}
					}
				}
			}
		}
			
		return agentsToInform;
		
	}
	
	ArrayList<Int2D> useFilterStrategy2(ArrayList<Int2D> toFilter){
		// Filtering information to target own color cells
		return null;
	}
	
	/**
	 * Efface les précédents données 
	 */
	public void resetDetections(){
		this.lastPerception.clear();
		this.lastOwnLandDetected.clear();
		this.lastPaintPotDetected.clear();
		this.lastAgentsDetected.clear();
	}
	
	void detectRelevantInformation(){
		this.lastPerception = perceive();
		this.lastPaintPotDetected = filterPaintPots();
		this.lastOwnLandDetected = Statics.GetColorOfCases(this.grid, this.lastPerception).getOneList(this.colorAgent);	
		this.lastAgentsDetected = filterAgents();
	}
	
	/**
	 * Retourne la position du pot de peinture le plus proche de l'agent passé en paramètre
	 * @param coloringAgent
	 * @return le pot de peinture le plus proche
	 */
	private Int2D getMostPertinentPaintPotLocation(ColoringAgent coloringAgent){
		
		if (this.lastPaintPotDetected.isEmpty()){
			return null;
		}
		else{
			return this.lastPaintPotDetected
						.stream()
						.min((p1,p2) -> Integer.compare(
								Statics.absoluteDistance(coloringAgent.location, p1)
							   ,Statics.absoluteDistance(coloringAgent.location, p2)))
						.get();
		}
	}
	
	private Int2D getMostPertinentLandLocation(ColoringAgent coloringAgent){
		
		if (this.lastOwnLandDetected.isEmpty()){
			return null;
		}else{
			return this.lastOwnLandDetected
					.stream()
					.min((p1,p2) -> Integer.compare(
							Statics.absoluteDistance(coloringAgent.location, p1)
						   ,Statics.absoluteDistance(coloringAgent.location, p2)))
					.get();
		}
	}
	
}