package Agents;

import sim.engine.SimState;
import sim.engine.Steppable;
import sim.util.Int2D;

import model.Color;
import model.GridModel;


public class ColoringAgent extends AgentOnField implements Steppable {
	/**
	 * Serial version
	 */
	private static final long serialVersionUID = 4967689413678754350L;
	
	boolean hasAdestination = false;
	Int2D destination;
	
	boolean hasPaint;
	
	public ColoringAgent() {
		super();
	}
	
	public ColoringAgent(Color colorAgent) {
		super(colorAgent);
	}
	
	@Override
	public void step(SimState state) {
		this.grid = (GridModel) state;
	}
	
	public boolean receiveInfoFromScout(){
		return false;
	}
	
	public void color(){
	
	}
	
	public void rechargePaint(){
		
	}
	
	public void moveTowardsDestination(){
	
	}
		
	public void compareDestinations(){
	
	}
	
	public void answerToScout(){
	
	}
}
