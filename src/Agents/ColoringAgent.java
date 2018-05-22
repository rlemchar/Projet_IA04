package Agents;

import sim.engine.SimState;
import sim.engine.Steppable;
import sim.util.Double2D;

public class ColoringAgent implements Steppable {

	/**
	 * Serial version
	 */
	private static final long serialVersionUID = 4967689413678754350L;
	
	boolean hasAdestination = false;
	Double2D destination;
	
	boolean hasPaint;
	
	@Override
	public void step(SimState arg0) {
		// TODO Auto-generated method stub
	
	
	}
	
	public boolean receiveInfoFromScout(){
		return hasAdestination;
	
	}
	
	public void perceiveAround(){
	
	}
	
	public void color(){
	
	}
	
	public void rechargePaint(){
	
	}
	
	public void moveTowardsDestination(){
	
	}
		
	public void moveRandom(){
	
	}
	
	public void compareDestinations(){
	
	}
	
	public void answerToScout(){
	
	}

}
