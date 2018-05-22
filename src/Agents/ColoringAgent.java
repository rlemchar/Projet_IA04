package Agents;

import sim.engine.SimState;
import sim.engine.Steppable;
import sim.util.Int2D;

import java.util.ArrayList;

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

	@Override
	protected ArrayList<Int2D> perceive() {
		ArrayList<Int2D> allCoordsFromPerception = new ArrayList<Int2D>();
		for(int x = this.location.x - this.powerOfPerception; x < this.location.x + this.powerOfPerception;x++){
			for(int y = this.location.y - this.powerOfPerception;y < this.location.y + this.powerOfPerception;y++){
				allCoordsFromPerception.add(new Int2D(x,y));
			}
		}
		return allCoordsFromPerception;
	}

}
