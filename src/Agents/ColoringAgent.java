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
	
	/**
	 * Permet de colorier une zone 
	 * -> La zone est égale à la zone de perception de l'agent
	 */
	public void Color(){
		/* Variables locales*/
		int x,y;
		
		/* Coloriage de la zone */
		for(x = this.location.x - this.powerOfPerception;x < this.location.x + this.powerOfPerception;x++){
			for(y = this.location.y - this.powerOfPerception;y < this.location.y + this.powerOfPerception;y++){
				
			}
		}
	}
	
	public void rechargePaint(){
		
	}
	
	public void moveTowardsDestination(){
	
	}
		
	public void compareDestinations(){
	
	}
	
	/** 
	 * Pour Roxanne 
	 */
	public void answerToScout(){
	
	}
}
