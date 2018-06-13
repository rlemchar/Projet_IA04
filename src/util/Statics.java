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
	
	
	public static ScoutAgent GetScoutAgent(Bag objects){
		for(Object obj : objects) {
			if(obj instanceof ScoutAgent)
				return (ScoutAgent)obj;
		}
		return null;
	}
	
	
	
	/**
	 * Permet de renvoyer tous les couleurs des cases pour une zone donnée en param�tre
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
				// On suppose que la case couleur est bien trouv� -> retour de GetCaseColor non null
				result.add(Statics.GetCaseColor(grid.getGrid().getObjectsAtLocation(x, y)));
			}
		}
		return result;
	}
	
	public static int computeCost(Color currentCaseColor, Color myColor) {
		int cost = 0;
		if(currentCaseColor == myColor) cost = 1;     //on est chez nous 
		if(currentCaseColor != myColor && currentCaseColor!= model.Color.None) cost = 3;  //on est chez les adversaires
		if(currentCaseColor != myColor) cost = 2;             // on est sur une case neutre
		return cost;
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
	 * @param x -> ordonnée
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
		if(objects == null)
			return true;
		return objects.size() == 0 || objects.numObjs == 1 && objects.get(0) instanceof CaseColor;
	}
	
	/**
	 * Permet de trouver une case avec une couleur précise dans un tableau de cases 
	 * @param cells -> Le tableau de cases
	 * @param toFind -> La couleur à trouver
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
	 * Permet de r�cup�rer le pot de peinture depuis un bag
	 * @param objects -> Liste / Bag d'objets o� chercher le pot de peinture
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
	 * Permet de r�cup�rer le pot de peinture � une position x,y
	 * @param grid -> Grille de simulation
	 * @param x -> abscisse
	 * @param y -> ordonn�e
	 * @return le pot de peinture sinon null
	 */
	public static PaintPot getPaintPot(GridModel grid,int x, int y){
		return Statics.getPaintPot(grid.getGrid().getObjectsAtLocation(x, y));
	}
	
	/**
	 * Permet de r�cup�rer le pot de peinture � une position x,y
	 * @param grid -> Grille de simulation
	 * @param pos -> Int2D repr�sentant la position de recherche
	 * @return le pot de peinture sinon null
	 */
	public static PaintPot getPaintPot(GridModel grid,Int2D pos){
		return Statics.getPaintPot(grid.getGrid().getObjectsAtLocation(pos.x, pos.y));
	}
	
	public static int computeScoreCell(GridModel grid, Int2D cell, Color colorAgent) {
		int score = 0;
		Bag BagToEvaluate = null;
		for(int i = -1; i != 1; i++) {
			for(int j = -1; j != 1; j++) {
				BagToEvaluate = grid.getGrid().getObjectsAtLocation(cell.x + i, cell.y + j);
				Color colorCase = GetCaseColor(BagToEvaluate).getColor();
				if(colorCase == colorAgent) score = score + 0;
				else score = score + 1;
			}			
		}
		return score;
	}
}
