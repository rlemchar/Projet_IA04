package Agents;

import java.util.ArrayList;
import java.util.Random;

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
	
	public ScoutAgent() {
		super();
	}
	
	public ScoutAgent(Color colorAgent) {
		super(colorAgent);
	}

	/**
	 * Serial Version
	 */
	private static final long serialVersionUID = 7682473999209704532L;

	@Override
	public void step(SimState state) {
		super.step(state);
		moveRandom();
	}
	
	@Override
	public void moveRandom(){		
		Int2D newLocation = this.location;

		
		//Stratégie 3 : mouvement aléatoire
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
		
		
		
		
		//Stratégie 2 : Si on a un autre Scout Agent dans son champs de vision
		this.lastPerception = this.perceive();
		System.out.print(lastPerception);
		for(Int2D cell : this.lastPerception) {
			System.out.print("Coucou");
			Bag objects = this.grid.getGrid().getObjectsAtLocation(cell.x, cell.y);
			if (objects != null){
				ScoutAgent secondScout = Statics.GetScoutAgent(objects);
				
				System.out.print(secondScout);
				if (secondScout != null && secondScout.getColorAgent() == this.colorAgent){
					// Il y'a un agent scout dans la case, on s'en éloigner
					int distanceX = cell.x  - this.location.x; // Si > 0 aller à gauche
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
							newLocation =  new Int2D(this.location.x + 1, this.location.y + 1);
					}
				}
			}
		}
		
		
		//Stratégie 3 : Si on est du bord
		if(this.location.x >= Constants.GRID_SIZE - Constants.PERCEPTION_FOR_SCOUT_AGENT)
			newLocation =  new Int2D(this.location.x - 1, this.location.y);
		if(this.location.x <= Constants.PERCEPTION_FOR_SCOUT_AGENT)
			newLocation =  new Int2D(this.location.x + 1, this.location.y);
		if(this.location.y >= Constants.GRID_SIZE - Constants.PERCEPTION_FOR_SCOUT_AGENT)
			newLocation =  new Int2D(this.location.x, this.location.y - 1);
		if(this.location.y <= Constants.PERCEPTION_FOR_SCOUT_AGENT)
			newLocation =  new Int2D(this.location.x, this.location.y + 1);
		
		
		
		
		this.grid.getGrid().setObjectLocation(this, newLocation);
		
	}
	
	ArrayList<Int2D> filterInformation(ArrayList<Int2D> toFilter){
		return useFilterStrategy1(toFilter);
	}

	public void informOthers(ArrayList<Int2D> perceivedTargets){
		
		if (!perceivedTargets.isEmpty()){
		
			ArrayList<ColoringAgent> agentsToInform = new ArrayList<ColoringAgent>();
			
			// On regarde si des agents colorieur sont présents dans le champ de perception
			for (Int2D perceivedCell : this.lastPerception){
				Bag objects = this.grid.getGrid().getObjectsAtLocation(perceivedCell.x, perceivedCell.y);
				if (objects != null){
					for (Object obj : objects){
						if (obj.getClass() == ColoringAgent.class){
							// Il y'a un agent colorieur dans la case, on doit l'ajouter Ã  la liste des destinataires
							agentsToInform.add((ColoringAgent)obj);
						}
					}
				}
			}
			
			if (!agentsToInform.isEmpty()){
				// On informe les agents colorieurs
				for (ColoringAgent coloringAgent : agentsToInform){
					for (Int2D target: perceivedTargets){
						// Pour chaque agent destinataire, on envoie des ordres pr chaque objectifs
						Order order = new Order(this,coloringAgent, target);
						CommunicationSystem.addOrder(order);
					}
				}
			}
		}
	}


	ArrayList<Int2D> useFilterStrategy1(ArrayList<Int2D> toFilter){
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
	
	ArrayList<Int2D> useFilterStrategy2(ArrayList<Int2D> toFilter){
		// Filtering information to target own color cells
		return null;
	}
	
}