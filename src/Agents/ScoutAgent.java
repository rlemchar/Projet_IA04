package Agents;

import java.util.ArrayList;
import java.util.Random;

import util.Constants;
import util.Statics;
import util.TargetType;
import model.Color;
import model.CommunicationSystem;
import model.Order;
import model.PaintPot;
import model.CaseColor;
import sim.engine.SimState;
import sim.engine.Steppable;
import sim.util.Bag;
import sim.util.Int2D;
import util.Statics;
import util.Constants;

public class ScoutAgent extends AgentOnField implements Steppable {
	
	ArrayList<Int2D> lastPerception;
	ArrayList<Int2D> lastPaintPotDetected;
	ArrayList<Int2D> lastOwnLandDetected;
	ArrayList<ColoringAgent> lastAgentsDetected;
	
	public ScoutAgent() {
		super();
		this.powerOfPerception = Constants.PERCEPTION_FOR_SCOUT_AGENT;
	}
	
	public ScoutAgent(Color colorAgent) {
		super();
	}

	/**
	 * Serial Version
	 */
	private static final long serialVersionUID = 7682473999209704532L;

	@Override
	public void step(SimState state) {
		super.step(state);
		this.lastPerception = perceive();
		moveRandom();
		
	}
	
	private Int2D moveRandom3() {
		Int2D newLocation = null;

		//Strat�gie 3 : mouvement al�atoire
		Random generator = new Random();
		int rand = generator.nextInt(4);
		
		switch(rand)
		{
			case 0:
				newLocation =  new Int2D(this.location.x + 1, this.location.y);
				break;
			case 1:
				newLocation =  new Int2D(this.location.x - 1, this.location.y);
				break;
			case 2:
				newLocation =  new Int2D(this.location.x, this.location.y + 1);
				break;
			case 3:
				newLocation =  new Int2D(this.location.x, this.location.y - 1);
				break;
		}
		return newLocation;
	}
	
	private Int2D moveRandom2() {
		Int2D newLocation = null;

		//Strat�gie 2 : Si on a un autre Scout Agent dans son champs de vision
		for(Int2D cell : this.lastPerception) {
			Bag objects = this.grid.getGrid().getObjectsAtLocation(cell.x, cell.y);
			if (objects != null){
				ScoutAgent secondScout = Statics.GetScoutAgent(objects);
				if (secondScout != null && secondScout != this && secondScout.getColorAgent() == this.colorAgent){
					// Il y'a un agent scout dans la case, on s'en �loigner
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
		}
		
		return newLocation;
	}
	
	private Int2D moveRandom1() {
		Int2D newLocation = null;
		
		//Strat�gie 1 : Si on est du bord
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
	
	
	
	@Override
	public void moveRandom(){		
		
		Int2D newLocation = this.moveRandom3();
//		
		if(this.moveRandom2() != null) newLocation = this.moveRandom2();
//		
		if(this.moveRandom1() != null) newLocation = this.moveRandom1();
//		
		this.grid.getGrid().setObjectLocation(this, newLocation);
		
		detectRelevantInformation();
		informOthers();
		resetDetections();
		
	}
	
	public void informOthers(){
		
		if (!lastAgentsDetected.isEmpty()){
			
			Int2D mostPertinentPaintPot;
			Int2D mostPertinentLand;
			
			for (ColoringAgent coloringAgent : this.lastAgentsDetected){
				mostPertinentPaintPot = getMostPertinentLandLocation(coloringAgent);
				mostPertinentLand = getMostPertinentLandLocation(coloringAgent);
				
				System.out.println(mostPertinentPaintPot);
				System.out.println(mostPertinentLand);
				
				if (mostPertinentPaintPot != null){
					Order order = new Order(this,coloringAgent, mostPertinentPaintPot,TargetType.paintPot);
					CommunicationSystem.addOrder(order);
				}
				if (mostPertinentLand != null){
					Order order = new Order(this,coloringAgent, mostPertinentLand,TargetType.land);
					CommunicationSystem.addOrder(order);
				}
			}
			
		}
	}
			
	ArrayList<Int2D> filterPaintPots(){
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
	
	ArrayList<ColoringAgent> filterAgents(){
		ArrayList<ColoringAgent> agentsToInform = new ArrayList<ColoringAgent>();
		
		// On regarde si des agents colorieur sont présents dans le champ de perception
		for (Int2D perceivedCell : this.lastPerception){  
			Bag objects = this.grid.getGrid().getObjectsAtLocation(perceivedCell.x, perceivedCell.y);
			if (objects != null){
				for (Object obj : objects){
					if (obj.getClass() == ColoringAgent.class){
						ColoringAgent foundAgent = (ColoringAgent) obj;
						if (foundAgent.colorAgent == this.colorAgent){
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
	
	int calculateDistanceScore(Int2D agentLocalisation, Int2D targetLocalisation){
		
		int xdistance = Math.abs(agentLocalisation.x - targetLocalisation.x);
		int ydistance = Math.abs(agentLocalisation.y - targetLocalisation.y);
		
		return xdistance+ydistance;
	}
	
	void resetDetections(){
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
	
	Int2D getMostPertinentPaintPotLocation(ColoringAgent coloringAgent){
		
		if (this.lastPaintPotDetected.isEmpty()){
			return null;
		}else{
			Int2D closer = this.lastPaintPotDetected.get(0);
			for (int i =1; i<this.lastPaintPotDetected.size();i++){
				if (calculateDistanceScore(coloringAgent.getLocation(),closer) > 
				calculateDistanceScore(coloringAgent.getLocation(),this.lastPaintPotDetected.get(i))){
					closer = this.lastPaintPotDetected.get(i);
				}
			}
			return closer;
		}
	}
	
	Int2D getMostPertinentLandLocation(ColoringAgent coloringAgent){
		
		if (this.lastOwnLandDetected.isEmpty()){
			return null;
		}else{
			Int2D closer = this.lastOwnLandDetected.get(0);
			for (int i =1; i<this.lastOwnLandDetected.size();i++){
				if (calculateDistanceScore(coloringAgent.getLocation(),closer) > 
				calculateDistanceScore(coloringAgent.getLocation(),this.lastOwnLandDetected.get(i))){
					closer = this.lastOwnLandDetected.get(i);
				}
			}
			return closer;
		}
	}
	
}