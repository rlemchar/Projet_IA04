package Agents;

import sim.engine.SimState;
import sim.engine.Steppable;
import sim.util.Int2D;
import util.Constants;
import util.Statics;

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
		CaseColor[] cellWithOppositeColor;
		
		/* Récupération de la zone de coloriage -> Uniquement les cases qui ne sont pas de la couleur de l'agent*/
		colorZoneFiltered = Statics.GetZoneColor(grid, new Int2D(
									this.location.x - this.powerOfPerception,
									this.location.y - this.powerOfPerception
								), this.location.x + this.powerOfPerception, this.location.y + this.powerOfPerception
							).stream().filter(cell -> cell.getColor() != this.colorAgent);
		
		/* Récupération des cases avec la couleur de l'équipe adverse */
		cellWithOppositeColor = colorZoneFiltered.filter(cell -> cell.getColor() == this.oppositeColor).toArray(CaseColor[]::new);
		
		/* On ne colorie pas si le nombre de cases à colorier est inférieur au seuil */
		if(colorZoneFiltered.count() >= Constants.THRESHOLD_FOR_PAINTING_A_ZONE){
			/* On colorie et/ou on rend les cases neutre d'abord */
			for(CaseColor cell : colorZoneFiltered.toArray(CaseColor[]::new)){
				if(this.HasPaint()){
					cell.setColor(cell.getColor() == Color.None ? this.colorAgent : Color.None);
					this.numberOfTubeOfPaint--;
				}		
				else
					break;	
			}
			
			/* On colorie les cases neutres restantes */
			for(CaseColor cell : cellWithOppositeColor){
				if(this.HasPaint()){
					cell.setColor(this.colorAgent);
					this.numberOfTubeOfPaint--;
				}
				else
					break;
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
	 * Permet de savoir si l'agent peut colorier une case adverse
	 * @return
	 */
	public boolean CanColorOppositeCase(){
		return this.numberOfTubeOfPaint > 2;
	}
	
	
	/**
	 * Permet de savoir si l'agent a des tubes de peintures
	 * @return booléen
	 */
	public boolean HasPaint(){
		return this.numberOfTubeOfPaint == 0;
	}
	
	/** 
	 * Pour Roxanne 
	 */
	public void answerToScout(){
	
	}
}
