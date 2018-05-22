package model;

import util.Constants;

public class FoodGroup {
	private int quantity;
	
	
	//Construction d'un nouveau pot de peinture
	public FoodGroup() {
		quantity = Constants.MAX_PAINTING;
	}
	
	//Retourne le nombre d'unité de peinture restant dans le pot
	public int getQuantity() {
		return quantity;
	}

	//Lorsqu'un agent prend une unité de peinture
	public void decQuantity() {
		--quantity;
	}
	
	//Retourne TRUE s'il n'y a plus de peinture dans le pot
	public boolean empty() {
		return quantity == 0;
	}

}
