package gui;

import java.awt.Color;

import javax.swing.JFrame;

import Agents.AgentOnField;
import model.GridModel;
import model.PaintPot;
import sim.display.Controller;
import sim.display.Display2D;
import sim.display.GUIState;
import sim.engine.SimState;
import sim.portrayal.Inspector;
import sim.portrayal.grid.SparseGridPortrayal2D;
import sim.portrayal.simple.OvalPortrayal2D;
import sim.portrayal.simple.RectanglePortrayal2D;
import util.Constants;

public class GridGUI extends GUIState {
	public Display2D display;
	public JFrame displayFrame;
	SparseGridPortrayal2D yardPortrayal = new SparseGridPortrayal2D();

	public GridGUI(SimState state) {
		super(state);
	}

	public static String getName() {
		return "Splash"; 
	}

	public void start() {
		super.start();
		setupPortrayals();
	}

	public void load(SimState state) {
		super.load(state);
		setupPortrayals();
	}

	public void setupPortrayals() {
		GridModel grid = (GridModel) state;	
		yardPortrayal.setField(grid.getGrid());
		/* Couleur pour les pots de peintures */
		yardPortrayal.setPortrayalForClass(PaintPot.class, getIntLabelForObject(Color.PINK));
		/* Couleurs pour les agents */
		grid.getListAgents().forEach(agent -> {
			yardPortrayal.setPortrayalForObject(agent, getIntLabelForObject(agent));
		});
		
		/* Affichage de la grille */
		display.reset();
		display.setBackdrop(Color.GREEN);
		display.repaint();
	}
	/**
	 * @param color -> couleur de l'objet
	 * @return Un IntLabel pour l'objet à afficher
	 */
	private IntLabel getIntLabelForObject(Color color) {
		return getIntLabelForObject(color,null);
	}
	
	/**
	 * @param color -> couleur de l'objet
	 * @return Un IntLabel pour l'objet à afficher
	 */
	private IntLabel getIntLabelForObject(Color color,String labelName) {
		RectanglePortrayal2D r = new RectanglePortrayal2D();
		r.paint = color;
		r.filled = true;
		return new IntLabel(r, labelName);
	}
	
	/**
	 * @param color -> Couleur des agents
	 * @return un IntLabel pour l'agent à afficher
	 */
	private IntLabel getIntLabelForObject(AgentOnField agent) throws IllegalArgumentException{ 
		switch(agent.getColorAgent()) {
			case Blue:
				return getIntLabelForObject(Color.BLUE,agent.toString());
			case Red:
				return getIntLabelForObject(Color.RED,agent.toString());
			default:
				throw new IllegalArgumentException("L'agent doit avoir une couleur !");
		
		}
	}

	@Override
	public void init(Controller c) {
		super.init(c);
		display = new Display2D(Constants.FRAME_SIZE, Constants.FRAME_SIZE, this);
		display.setClipping(false);
		displayFrame = display.createFrame();
		displayFrame.setTitle("Beings");
		c.registerFrame(displayFrame);
		displayFrame.setVisible(true);
		display.attach(yardPortrayal, "Yard");
	}
	
	public  Object  getSimulationInspectedObject()  {  return  state;  }
	public  Inspector  getInspector() {
		Inspector  i  =  super.getInspector();
		i.setVolatile(true);
		return  i;
	}
}
