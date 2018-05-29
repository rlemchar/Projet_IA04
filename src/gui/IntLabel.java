package gui;

import model.PaintPot;
import sim.portrayal.DrawInfo2D;
import sim.portrayal.SimplePortrayal2D;
import sim.portrayal.simple.LabelledPortrayal2D;
import Agents.ColoringAgent;
import Agents.ScoutAgent;

public class IntLabel extends LabelledPortrayal2D {
	private static final long serialVersionUID = 1L;
	
	public IntLabel(SimplePortrayal2D child, String label) {
		super(child, label);
	}

	@Override
	public String getLabel(Object object, DrawInfo2D info) {
		if(object instanceof PaintPot) {
			return ((PaintPot)object).toString();
		}
		if(object instanceof ColoringAgent) {
			return ((ColoringAgent)object).toString();
		}
		if(object instanceof ScoutAgent) {
			return ((ScoutAgent)object).toString();
		}
		return "Unknown Object";
	}
}
