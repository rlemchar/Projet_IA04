package model;

import java.util.Random;

import model.ModeForGetFreeLocation;
import model.Color;
import sim.engine.SimState;
import sim.field.grid.SparseGrid2D;
import util.Constants;
import util.Statics;
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
			grid.setObjectLocation(new PaintPot(), getFreeLocation());
	}
	
	/**
	 * Permet de retourner une position d'une case libre n'importe où sur la grille
	 * @return position libre
	 */
	private Int2D getFreeLocation() {
		return getFreeLocation(0);
	}
	
	/**
	 * Permet de retourner une position d'une case libre dans une zone définie en mode normal
	 * @return position libre
	 */
	private Int2D getFreeLocation(int yLim) {
		return getFreeLocation(yLim,ModeForGetFreeLocation.Regular);
	}
	
	/**
	 * Permet de retourner une position d'une case libre dans une zone définie selon le mode choisi
	 * @param zoneBegin -> point représentant le début (haut gauche) de la zone de limitation
	 * @return position libre
	 */
	private Int2D getFreeLocation(int yLim,ModeForGetFreeLocation mode) {
		Int2D location = null,temp = null;
		switch(mode) {
			case Inverse:
				do {
					temp = new Int2D(
							random.nextInt(grid.getWidth()),
							yLim - random.nextInt(yLim)
					);
					if(Statics.isCaseFree(this, temp));
						location = temp;
				} while (location != null);
			default:
				do {
					temp = new Int2D(
							random.nextInt(grid.getWidth()),
							yLim + random.nextInt(grid.getHeight() - yLim)
					);
					if(Statics.isCaseFree(this, temp));
						location = temp;
				} while (location != null);
				break;
		}
		return location;
	}
	
	/**
	 * Positionne les agents sur la grille selon leur couleur
	 */
	public void addAgents(){
		
	}




}