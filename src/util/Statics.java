package util;

import java.util.ArrayList;

import model.CaseColor;
import model.Color;
import model.CaseColorListWrap;
import sim.util.Bag;
import sim.util.Int2D;
import model.GridModel;

/**
--------------------------------------------------------------------
	Cette classe regroupe des méthodes statiques utiles pour le projet
--------------------------------------------------------------------
**/


public final class Statics {
	/**
	 * @param objects -> Liste spéciale retourné par la méthode getObjectsAtLocation
	 * @return L'objet représentant la couleur de la case sinon null
	 */
	public static CaseColor GetCaseColor(Bag objects){
		for(Object obj : objects) {
			if(obj instanceof CaseColor)
				return (CaseColor)obj;
		}
		return null;
	}
	
	/**
	 * @param location -> Position de la case
	 * @param grid -> Grille de simulation
	 * @return -> La couleur de la case
	 */
	public static Color GetColorOfCase(GridModel grid,Int2D location) {
		return Statics.GetCaseColor(grid.getGrid().getObjectsAtLocation(location.x, location.y)).getColor();
	}
	
	/**
	 * Cette méthode permet de retourner trois listes contenant les cases classés par couleur
	 * @param grid -> Grille de simulation
	 * @param cellsToAnalyse -> Liste de positions de cases à analyser
	 * @return une liste de position de case des couleurs recherchés
	 */
	public static CaseColorListWrap GetColorOfCases(GridModel grid,ArrayList<Int2D> cellsToAnalyse){
		CaseColorListWrap result = new CaseColorListWrap();
		cellsToAnalyse.stream()
			.forEach(cell -> {
				CaseColor colorOfCase = Statics.GetCaseColor(grid.getGrid().getObjectsAtLocation(cell.x, cell.y));
				switch(colorOfCase.getColor()) {
					case Blue:
						result.AddBlue(cell);
						break;
					case None:
						result.AddNone(cell);
						break;
					case Red:
						result.AddRed(cell);
						break;
				}
			});
		return result;		
	}
	
	/**
	 * Permet de savoir si une case est libre ou non
	 * @param grid
	 * @param location
	 * @return -> booléen indiquant si la case est libre ou pas
	 */
	public static boolean isCaseFree(GridModel grid,Int2D location) {
		Bag objects = grid.getGrid().getObjectsAtLocation(location.x, location.y);
		return objects.size() == 0 || (objects.size() == 1 && objects.get(0) instanceof CaseColor);
	}
}
