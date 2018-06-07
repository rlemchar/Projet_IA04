package model;

import java.util.ArrayList;

import Agents.AgentOnField;
import Agents.ColoringAgent;
import Agents.ScoutAgent;
import model.PaintPot;
import model.Color;
import sim.engine.SimState;
import sim.field.grid.SparseGrid2D;
import util.Constants;
import util.FactoryAgent;
import util.Statics;
import sim.util.Int2D;

public class GridModel extends SimState {
	/**
	 * Serial Version
	 */
	private static final long serialVersionUID = -3869529856795364016L;
	
	/* Grille de simulation */
	public SparseGrid2D grid = new SparseGrid2D(Constants.GRID_SIZE, Constants.GRID_SIZE);
	
	/* Liste des agents */
	private ArrayList<AgentOnField> listAgents;
	
	
	public GridModel(long seed) {
		super(seed);
		this.listAgents = new ArrayList<AgentOnField>();
	}

	@Override
	public void start() {
		super.start();
		/* On nettoie la grille */
		grid.clear();
		/* On initialise la grille avec la couleur neutre */
		InitGridColor();
		/* On ajoute la peinture et les agents*/
		initPaint();
		addAgents();
	}
	
	/**
	 * Permet d'ajouter la couleur de départ pour toutes les cases 
	 * -> Au départ, aucune couleur présente => Color = Color.None
	 */
	private void InitGridColor(){
		for(int x = 0; x < Constants.GRID_SIZE;x++){
			for(int y = 0;y < Constants.GRID_SIZE;y++){
				grid.setObjectLocation(new CaseColor(Color.None,x,y), x, y);
			}
		}
	}
		
	/**
	 * Permet d'ajouter les pots de peintures sur la carte à l'initialisation 
	 */
	public void initPaint(){
		for(int i = 0; i < Constants.MAX_TIN_OF_PAINT; ++i)
		{
			PaintPot temp = new PaintPot();
			grid.setObjectLocation(temp, getFreeLocation());
			schedule.scheduleRepeating(temp);
		}
	}
	
	/**
	 * Permet de retourner une position d'une case libre n'importe où sur la grille
	 * @return position libre
	 */
	public Int2D getFreeLocation() {
		return getFreeLocation(0);
	}
	
	/**
	 * Permet de retourner une position d'une case libre dans une zone définie en mode normal
	 * @return position libre
	 */
	public Int2D getFreeLocation(int yLim) {
		return getFreeLocation(yLim,0);
	}
	
	/**
	 * Permet de retourner une position d'une case libre dans une zone définie selon le mode choisi
	 * @param zoneBegin -> point représentant le début (haut gauche) de la zone de limitation
	 * @return position libre
	 */
	public Int2D getFreeLocation(int yLim,int mode) {
		Int2D location = null,temp = null;
		switch(mode){
			case -1:
				do {
					temp = new Int2D(random.nextInt(grid.getWidth()),random.nextInt(yLim));
					if(Statics.isCaseFree(this, temp));
						location = temp;
					
				} while (location == null);
				break;
			default:
				do {
					temp = new Int2D(random.nextInt(grid.getWidth()), yLim + random.nextInt(grid.getHeight() - yLim));
					if(Statics.isCaseFree(this, temp));
						location = temp;
				} while (location == null);
				break;
		}
		return location;
	}
	
	/**
	 * Positionne les agents sur la grille selon leur couleur
	 */
	public void addAgents(){
		/* Les variables */
		int i;
		AgentOnField temp = null;
		
		/* Les rouges -> En haut de la map*/
		for(i = 0; i < Constants.NUM_AGENTS; i++) {
			temp = FactoryAgent.make((i < Constants.MAX_SCOUT_AGENTS) ? ScoutAgent.class : ColoringAgent.class,Color.Red);
			grid.setObjectLocation(temp, getFreeLocation(Constants.SPAWN_ZONE_INIT, -1));
			this.listAgents.add(temp);
			schedule.scheduleRepeating(temp);
		}
		
		/* Les bleus -> En bas de la map */
		for(i = 0; i < Constants.NUM_AGENTS; i++) {
			temp = FactoryAgent.make((i < Constants.MAX_SCOUT_AGENTS) ? ScoutAgent.class : ColoringAgent.class,Color.Blue);
			grid.setObjectLocation(temp, getFreeLocation(grid.getHeight() - Constants.SPAWN_ZONE_INIT));
			this.listAgents.add(temp);
			schedule.scheduleRepeating(temp);
		}		
	}

	/* Getteur numAgents*/
	public ArrayList<AgentOnField> getListAgents() {
		return this.listAgents;
	}
	
	/* Supprimer un agent */
	public void removeAgent(AgentOnField agent) {
		this.listAgents.remove(agent);
	}
	
	/* Getteur grille */
	public SparseGrid2D getGrid() {
		return grid;
	}
	
	/* Setteur grille */
	public void setGrid(SparseGrid2D grid) {
		this.grid = grid;
	}

}