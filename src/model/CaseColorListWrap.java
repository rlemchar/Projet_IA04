package model;

import java.util.ArrayList;

import sim.util.Int2D;

/**
 * @author wakidou
 * ---------------------------------------------------------------------
 * 		Cette classe propose trois listes :
 * 			- une liste contenant des positions de cases de couleur bleu
 * 			- une liste contenant des positions de cases de couleur rouge
 * 			- une liste contenant des positions de cases qui n'ont aucune couleur
 * ---------------------------------------------------------------------
 */

public class CaseColorListWrap {
	/* Les attributs */
	protected ArrayList<Int2D> red;
	protected ArrayList<Int2D> blue;
	protected ArrayList<Int2D> none;
	
	/* Constructeurs */
	public CaseColorListWrap(){
		red = new ArrayList<Int2D>();
		blue = new ArrayList<Int2D>();
		none = new ArrayList<Int2D>();
	}
	
	public CaseColorListWrap(ArrayList<Int2D> red,ArrayList<Int2D> blue,ArrayList<Int2D> none){
		this.red = red;
		this.blue = blue;
		this.none = none;
	}
	
	/* Getteurs */
	public ArrayList<Int2D> getRed() { return red; }
	public ArrayList<Int2D> getBlue() { return blue; }
	public ArrayList<Int2D> getNone() { return none; }
	public ArrayList<Int2D> getOneList(Color color){
		switch(color) {
			case Blue:
				return getBlue();
			case Red:
				return getRed();
			default:
				return getNone();
		}
	}
	
	/* Clears */
	public void ClearRed() { red.clear(); }
	public void ClearBlue() { blue.clear(); }
	public void ClearNone() { none.clear(); }
	public void Clear() {
		ClearRed();
		ClearBlue();
		ClearNone();
	}
	
	/* Add */
	public void AddRed(Int2D red) { this.red.add(red); }
	public void AddBlue(Int2D blue) { this.blue.add(blue); }
	public void AddNone(Int2D none) { this.none.add(none); }
}
