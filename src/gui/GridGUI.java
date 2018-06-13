package gui;
import java.awt.Color;

import javax.swing.JFrame;

import Agents.AgentOnField;
import Agents.ScoutAgent;
import model.GridModel;
import model.PaintPot;
import sim.display.Controller;
import sim.display.Display2D;
import sim.display.GUIState;
import sim.engine.SimState;
import sim.portrayal.Inspector;
import sim.portrayal.grid.SparseGridPortrayal2D;
import sim.portrayal.simple.RectanglePortrayal2D;
import util.Constants;
import util.Statics;

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

	/* (non-Javadoc)
	 * @see sim.display.GUIState#load(sim.engine.SimState)
	 */
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
				return getIntLabelForObject(agent instanceof ScoutAgent ? Color.CYAN : Color.BLUE);
			case Red:
				return getIntLabelForObject(agent instanceof ScoutAgent ? Color.ORANGE : Color.RED);
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
	
	@Override
	/**
	 * Cette fonction est appelé à la fin de la simulation
	 * -> On décompte le nombre de cases couleurs et on affiche le gagnant
	 */
	public void finish() {
		super.finish();
		
		/* Variables locales */
		int red,blue,x,y;
		GridModel grid;
		
		/* Init */
		red = 0;
		blue = 0;
		grid = (GridModel)state;
		
		/* Décompte */
		for(x = 0;x < grid.getGrid().getHeight();x++) {
			for(y = 0;y < grid.getGrid().getWidth();y++) {
				switch(Statics.GetColorOfCase(grid, x, y)) {
					case Blue:
						blue++;
						break;
					case Red:
						red++;
						break;
					default:
						break;
				}
			}
		}
		
		/* Affichage du gagnant -> pour le moment sur la console */
		if(blue == red) {
			System.out.println("Match nul. Nombre de cases coloriés de part et d'autre = "+red);
		}
		else {
			System.out.println(String.format(
					"L'équipe {0} a gagné avec {1} cases coloriés \n Les perdants ont réussi à colorier {2} cases"
					, (blue > red) ? "bleue" : "rouge",(blue > red) ? blue : red,(blue > red) ? red : blue));
		}
	}
	
	public  Object  getSimulationInspectedObject()  {  return  state;  }
	public  Inspector  getInspector() {
		Inspector  i  =  super.getInspector();
		i.setVolatile(true);
		return  i;
	}
}
