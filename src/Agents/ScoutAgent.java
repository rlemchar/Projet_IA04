package Agents;

import java.util.ArrayList;

import model.Color;
import model.CommunicationSystem;
import model.GridModel;
import model.Order;
import model.PaintPot;
import sim.engine.SimState;
import sim.engine.Steppable;
import sim.util.Bag;
import sim.util.Int2D;

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
		// move();
		// this.lastPerception = perceive();
		// informOthers(filterInformation(this.lastPerception));
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
							// Il y'a un agent colorieur dans la case, on doit l'ajouter à la liste des destinataires
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