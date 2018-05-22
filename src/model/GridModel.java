package model;

import java.util.Random;

import sim.engine.SimState;
import sim.field.grid.SparseGrid2D;
import util.Constants;
import sim.util.Int2D;

public class GridModel extends SimState {
	/**
	 * Serial Version
	 */
	private static final long serialVersionUID = -3869529856795364016L;
	
	
	public SparseGrid2D grid = new SparseGrid2D(Constants.GRID_SIZE, Constants.GRID_SIZE);
	private static final Random random = new Random();
	private int numAgents;
	
	public GridModel(long seed) {
		super(seed);
	}

	@Override
	public void start() {
		super.start();
		grid.clear();
		addPaint();
		InitGridColor();
		//addAgents();
		numAgents = Constants.NUM_AGENTS;
	}
	
	/* Méthode pour colorier la grille à l'initialisation --> Neutre à l'initialisation*/
	private void InitGridColor(){
		for(int x = 0; x < Constants.GRID_SIZE;x++){
			for(int y = 0;y < Constants.GRID_SIZE;y++){
				grid.setObjectLocation(new CaseColor(Color.None), x, y);
			}
		}
	}
	
	public SparseGrid2D getGrid() {
		return grid;
	}

	public void setGrid(SparseGrid2D grid) {
		this.grid = grid;
	}
	
	public void addPaint(){
		for(int i = 0; i < 15; ++i)
			grid.setObjectLocation(new FoodGroup(), getFreeLocation());
	}
	
	private Int2D getFreeLocation() {
		Int2D location = null;
		do {
			location = new Int2D(random.nextInt(grid.getWidth()), random.nextInt(grid.getHeight()) );
		} while (grid.getObjectsAtLocation(location.x,location.y) != null);
		return location;
	}
	
	public void addAgents(){
		
	}




}