package Agents;

import model.GridModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import model.CaseColor;
import model.Color;
import sim.util.Bag;
import sim.util.Int2D;
import util.Constants;


public abstract class AgentOnField {
	
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
	
	public AgentOnField(Color colorAgent){
		this.grid = null;
		this.steps = Constants.MAX_STEPS;
		this.colorAgent = colorAgent;
	}
	
	/* Permet à un agent de bouger */
	public void move(){
		if(this.grid != null){
			if(steps != 0){
				
			}
		}
	}
	
	/* Permet à un agent de percevoir */
	protected abstract ArrayList<Int2D> perceive();
	
	/* Recherche une case où un agent peut aller */
	private Int2D getNewLocation(){
		/* Récupère toutes les cases adjacentes */
		ArrayList<Int2D> allCases = perceive();
		Int2D newLocation = null,i = null;
		Bag objects = null;
		
		/* Recherche d'une case où bouger */
		while(newLocation != null){
			i = allCases.get(new Random().nextInt(allCases.size()));
			objects = grid.getGrid().getObjectsAtLocation(i.x, i.y);
			List<Int2D> possibleCases = objects.stream()
				.filter(object -> object instanceof CaseColor)
				.filter(object -> {
					CaseColor caseC = (CaseColor)object;
					if(caseC.getColor() == this.colorAgent && steps >= 1){
						return true;
					}
					else {
						if(caseC.getColor() == Color.None && steps >= 2){
							return true;
						}
						else{
							return steps == 3;
						}
					}
				})
				.map(object -> grid.getGrid().getObjectLocation(object))
				.collect();
		}
		
	}
}
