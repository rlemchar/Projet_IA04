package Agents;

import model.GridModel;
import model.CaseColour;
import sim.util.Int2D;


public abstract class AgentOnField {
	
	/* Grid */
	protected GridModel grid;
	
	/* Location */
	protected Int2D location;
	
	/* Perception */
	protected int powerOfPerception;
	
	public AgentOnField(){
		this.grid = null;
	}
	
	
	public void move(){
		if(this.grid != null){
			
		}
	}
}
