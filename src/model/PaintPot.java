package model;

import util.Constants;
import java.util.Random;
import model.GridModel;
import sim.engine.Steppable;
import sim.engine.SimState;
import sim.util.Int2D;


public class PaintPot implements Steppable {
	
	/**
	 * Serial Version
	 */
	private static final long serialVersionUID = -8147643876316158641L;
	
	
	private int quantity;
	private GridModel model;
	
	@Override
	public void step(SimState state) {
		model = (GridModel) state;
		Int2D pos = model.getGrid().getObjectLocation(this);
		if(empty()) replace();
		decQuantity();
		
	}
	//Construction d'un nouveau pot de peinture
	public PaintPot() {
		quantity = Constants.MAX_PAINTING;
		Random randomGenerator = new Random();
		quantity += randomGenerator.nextInt(5);
		
	}
	
	//Retourne le nombre d'unité de peinture restant dans le pot
	public int getQuantity() {
		return quantity;
	}

	//Lorsqu'un agent prend une unité de peinture
	public void decQuantity() {
		--quantity;
	}
	
	//Retourne TRUE s'il n'y a plus de peinture dans le pot
	public boolean empty() {
		return quantity == 0;
	}
	
	private void replace() {
		model.getGrid().setObjectLocation(this, model.getFreeLocation());
		quantity = Constants.MAX_PAINTING;
		Random randomGenerator = new Random();
		quantity += randomGenerator.nextInt(5);
	}
	
	

}
