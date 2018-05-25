package Agents;

import model.Color;

import sim.engine.SimState;
import sim.engine.Steppable;

public class ScoutAgent extends AgentOnField implements Steppable {
	public ScoutAgent(Color colorAgent) {
		super(colorAgent);
	}

	/**
	 * Serial Version
	 */
	private static final long serialVersionUID = 7682473999209704532L;

	@Override
	public void step(SimState state) {
		super.step(state);
	}

	public void informOthers(){

	}
}
