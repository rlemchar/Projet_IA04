package Agents;

import java.util.ArrayList;

import model.Color;
import sim.engine.SimState;
import sim.engine.Steppable;
import sim.util.Int2D;

public class ScoutAgent extends AgentOnField implements Steppable {
	public ScoutAgent(Color colorAgent) {
		super(colorAgent);
	}

	/**
	 * Serial Version
	 */
	private static final long serialVersionUID = 7682473999209704532L;

	@Override
	public void step(SimState arg0) {
		// TODO Auto-generated method stub

	}

	public void informOthers(){

	}

	@Override
	protected ArrayList<Int2D> perceive() {
		return null;
	}
}
