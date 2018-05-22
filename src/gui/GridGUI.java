package gui;

import java.awt.Color;

import javax.swing.JFrame;

import model.GridModel;
import sim.display.Controller;
import sim.display.Display2D;
import sim.display.GUIState;
import sim.engine.SimState;
import sim.portrayal.Inspector;
import sim.portrayal.grid.SparseGridPortrayal2D;
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
		display.reset();
		display.setBackdrop(Color.GREEN);
		display.repaint();
	}

	@Override
	public void init(Controller c) {
		super.init(c);
		display = new Display2D(Constants.FRAME_SIZE, Constants.FRAME_SIZE, this);
		display.setClipping(false);
		displayFrame = display.createFrame();
		displayFrame.setTitle("Frames");
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
