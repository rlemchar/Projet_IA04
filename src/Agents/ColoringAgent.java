package Agents;

import sim.engine.SimState;
import sim.engine.Steppable;
import sim.util.Int2D;
import util.Constants;
import util.Statics;

import java.util.ArrayList;
import java.util.stream.Stream;

import model.CaseColor;
import model.Color;
import model.GridModel;


public class ColoringAgent extends AgentOnField implements Steppable {
	/**
	 * Serial version
	 */
	private static final long serialVersionUID = 4967689413678754350L;
	
	/* Nombre de tubes de peinture */
	private int numberOfTubeOfPaint;
	
	/* Peinture disponible */
	
	
	private boolean hasAdestination = false;
	Int2D destination;
	
	/** Constructeur par défaut **/
	public ColoringAgent() {
		super();
		this.numberOfTubeOfPaint = 0;
	}
	
	/**
	 * Constructeur en initialisant la couleur de l'agent
	 * @param colorAgent Couleur de l'agent
	 */
	public ColoringAgent(Color colorAgent) {
		super(colorAgent);
		this.numberOfTubeOfPaint = 0;
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
		Stream<CaseColor> colorZoneFiltered;
		int i;
		
		/* Récupération de la zone de coloriage -> Uniquement les cases qui ne sont pas de la couleur de l'agent*/
		colorZoneFiltered = Statics.GetZoneColor(grid, new Int2D(
									this.location.x - this.powerOfPerception,
									this.location.y - this.powerOfPerception
								), this.location.x + this.powerOfPerception, this.location.y + this.powerOfPerception
							).stream().filter(cell -> cell.getColor() != this.colorAgent);
		
		/* On ne colorie pas si le nombre de cases à colorier est inférieur au seuil */

		
	}
	
	public void rechargePaint(){
		
	}
	
	public void moveTowardsDestination(){
	
	}
		
	public void compareDestinations(){
	
	}
	
	public boolean HasPaint(){
		return this.numberOfTubeOfPaint == 0;
	}
	
	/** 
	 * Pour Roxanne 
	 */
	public void answerToScout(){
	
	}
}
