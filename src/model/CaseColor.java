package model;

import sim.util.Int2D;
import model.MyColor;

import java.awt.Paint;

import sim.portrayal.simple.RectanglePortrayal2D;

/**
 * 
 * Classe de base définissant la couleur pour chaque case de la grille 
 * 
 * @author wakidou
 *
 */
public class CaseColor extends RectanglePortrayal2D{
	/** La couleur **/
	private MyColor color;
	
	/** Coordonnées de la case **/
	protected Int2D location;
	
	/** Constructeur par défaut **/
	public CaseColor(){
		
	}
	
	/**
	 * Constructeur avec une couleur pour la case
	 * @param color
	 */
	public CaseColor(MyColor color){
		this.color = color;
	}
	
	/**
	 * Constructeur avec une couleur pour la case + les coordonnées de la case sur la grille 
	 * @param color 
	 * @param location
	 */
	public CaseColor(MyColor color,Int2D location) {
		this(color);
		this.location = location;
	}
	
	/**
	 * Constructeur avec une couleur pour la case + les coordonnées (séparés) de la case sur la grille 
	 * @param color
	 * @param x
	 * @param y
	 */
	public CaseColor(MyColor color,int x,int y) {
		this(color,new Int2D(x,y));
	}
	
	
	public void draw(Paint color) {
		this.paint = color;
	}

	/* Getteur et setteur pour la couleur */
	public MyColor getColor() { return color; }
	public void setColor(MyColor color) { this.color = color; }
	
	/* Getteur pour la location */
	public int getX() { return this.location.x; }
	public int getY() { return this.location.y; }
}
