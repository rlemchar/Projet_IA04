package main;

import gui.GridGUI;
import model.GridModel;
import sim.display.Console;

public class SimulationMain {
	public static void main(String[] args) {
        runUI();
	}
	public static void runUI() {
		GridModel model = new GridModel(System.currentTimeMillis());
		GridGUI gui = new GridGUI(model);
		Console console = new Console(gui);
		console.setVisible(true);
	}
}
