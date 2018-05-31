package model;

import java.util.ArrayList;

import Agents.ColoringAgent;

// La classe qui permet aux agents eclaireurs de communiquer avec les agents colorieurs
// Les agents eclaireurs ajoutent des ordres et les agents colorieurs les recupèrent depuis cette classe
public class CommunicationSystem {
	
	static ArrayList<Order> Orders;
	
	public static ArrayList<Order> consultOrders(ColoringAgent receivingAgent){
		
		ArrayList<Order> allOrders = new ArrayList<Order>();
		
		for (Order order : Orders){
			if (order.getIntendedAgent() == receivingAgent){
				allOrders.add(order);
			}
		}
		return allOrders;
	}
	
	public static void addOrder(Order order){
		Orders.add(order);
	}

}
