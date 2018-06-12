package model;

import sim.util.Int2D;

/**
 * 
 * Classe de base définissant la couleur pour chaque case de la grille 
 * 
 * @author wakidou
 *
 */
public class CaseColor {
	/** La couleur **/
	private Color color;
	
	/** Coordonnées de la case **/
	protected Int2D location;
	
	/** Constructeur par défaut **/
	public CaseColor(){
		
	}
	
	/**
	 * Constructeur avec une couleur pour la case
	 * @param color
	 */
	public CaseColor(Color color){
		this.color = color;
	}
	
	/**
	 * Constructeur avec une couleur pour la case + les coordonnées de la case sur la grille 
	 * @param color 
	 * @param location
	 */
	public CaseColor(Color color,Int2D location) {
		this(color);
		this.location = location;
	}
	
	/**
	 * Constructeur avec une couleur pour la case + les coordonnées (séparés) de la case sur la grille 
	 * @param color
	 * @param x
	 * @param y
	 */
	public CaseColor(Color color,int x,int y) {
		this(color,new Int2D(x,y));
	}

	/* Getteur et setteur pour la couleur */
	public Color getColor() { return color; }
	public void setColor(Color color) { this.color = color; }
	
	/* Getteur pour la location */
	public int getX() { return this.location.x; }
	public int getY() { return this.location.y; }
}
