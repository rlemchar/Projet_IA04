package util;

import java.util.ArrayList;

import model.CaseColor;
import model.Color;
import model.CaseColorListWrap;
import sim.util.Bag;
import sim.util.Int2D;
import model.GridModel;
import model.PaintPot;

import Agents.ScoutAgent;

/**
--------------------------------------------------------------------
	Cette classe regroupe des mÃ©thodes statiques utiles pour le projet
--------------------------------------------------------------------
**/


public final class Statics {
	/**
	 * @param objects -> Liste spÃ©ciale retournÃ© par la mÃ©thode getObjectsAtLocation
	 * @return L'objet reprÃ©sentant la couleur de la case sinon null
	 */
	public static CaseColor GetCaseColor(Bag objects){
		for(Object obj : objects) {
			if(obj instanceof CaseColor)
				return (CaseColor)obj;
		}
		return null;
	}
	
	
	public static ScoutAgent GetScoutAgent(Bag objects){
		for(Object obj : objects) {
			if(obj instanceof ScoutAgent)
				return (ScoutAgent)obj;
		}
		return null;
	}
	
	
	
	/**
	 * Permet de renvoyer tous les couleurs des cases pour une zone donnÃ©e en paramï¿½tre
	 * @param grid -> Grille de simulation
	 * @param pointTopLeft -> Point haut gauche de la zone
	 * @param sizeX -> Longueur
	 * @param sizeY -> Largeur
	 * @return Liste des couleurs de chaque case
	 */
	public static ArrayList<CaseColor> GetZoneColor(GridModel grid,Int2D pointTopLeft,int sizeX,int sizeY){
		ArrayList<CaseColor> result = new ArrayList<CaseColor>();
		for(int x = pointTopLeft.x; x < sizeX && x < grid.getGrid().getHeight();x++){
			for(int y = pointTopLeft.y; y < sizeY && x < grid.getGrid().getWidth() ;y++){
				// On suppose que la case couleur est bien trouvï¿½ -> retour de GetCaseColor non null
				result.add(Statics.GetCaseColor(grid.getGrid().getObjectsAtLocation(x, y)));
			}
		}
		return result;
	}
	
	/**
	 * @param location -> Position de la case
	 * @param grid -> Grille de simulation
	 * @return -> La couleur de la case
	 */
	public static Color GetColorOfCase(GridModel grid,Int2D location) {
		return Statics.GetColorOfCase(grid,location.x,location.y);
	}
	
	/**
	 * @param grid -> Grille de simulation
	 * @param x -> ordonnÃ©e
	 * @param y -> abscisse
	 * @return
	 */
	public static Color GetColorOfCase(GridModel grid,int x,int y) {
		return Statics.GetCaseColor(grid.getGrid().getObjectsAtLocation(x,y)).getColor();
	}
	
	/**
	 * @param grid
	 * @param x
	 * @param y
	 * @param offset_x
	 * @param offset_y
	 * @return
	 */
	public static Color GetColorOfCase(GridModel grid,int x,int y,int offset_x,int offset_y) {
		return Statics.GetColorOfCase(grid, x + offset_x,y + offset_y);
	}
	
	/**
	 * Cette mÃ©thode permet de retourner trois listes contenant les cases classÃ©s par couleur
	 * @param grid -> Grille de simulation
	 * @param cellsToAnalyse -> Liste de positions de cases Ã  analyser
	 * @return une liste de position de case des couleurs recherchÃ©s
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
	 * @return -> boolÃ©en indiquant si la case est libre ou pas
	 */
	public static boolean isCaseFree(GridModel grid,Int2D location) {
		Bag objects = grid.getGrid().getObjectsAtLocation(location.x, location.y);
		if(objects == null)
			return true;
		return objects.size() == 0 || objects.numObjs == 1 && objects.get(0) instanceof CaseColor;
	}
	
	/**
	 * Permet de trouver une case avec une couleur prÃ©cise dans un tableau de cases 
	 * @param cells -> Le tableau de cases
	 * @param toFind -> La couleur Ã  trouver
	 * @return On renvoit la liste des cases 
	 */
	public static ArrayList<CaseColor> hasCellsWithColor(CaseColor[] cells,Color toFind) {
		/* Variable locale */
		ArrayList<CaseColor> allFound = new ArrayList<CaseColor>();
		
		/* Parcours du tableau */
		for(CaseColor cell : cells) {
			if(cell.getColor() == toFind)
				allFound.add(cell);
		}
		
		return allFound;
	}
	
	
	/**
	 * Permet de récupérer le pot de peinture depuis un bag
	 * @param objects -> Liste / Bag d'objets où chercher le pot de peinture
	 * @return le pot de peinture sinon null
	 */
	public static PaintPot getPaintPot(Bag objects){
		for(Object obj : objects) {
			if(obj instanceof PaintPot)
				return (PaintPot)obj;
		}
		return null;
	}
	
	/**
	 * Permet de récupérer le pot de peinture à une position x,y
	 * @param grid -> Grille de simulation
	 * @param x -> abscisse
	 * @param y -> ordonnée
	 * @return le pot de peinture sinon null
	 */
	public static PaintPot getPaintPot(GridModel grid,int x, int y){
		return Statics.getPaintPot(grid.getGrid().getObjectsAtLocation(x, y));
	}
	
	/**
	 * Permet de récupérer le pot de peinture à une position x,y
	 * @param grid -> Grille de simulation
	 * @param pos -> Int2D représentant la position de recherche
	 * @return le pot de peinture sinon null
	 */
	public static PaintPot getPaintPot(GridModel grid,Int2D pos){
		return Statics.getPaintPot(grid.getGrid().getObjectsAtLocation(pos.x, pos.y));
	}
}
