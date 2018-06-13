package model;

import java.util.ArrayList;

import Agents.ColoringAgent;

// La classe qui permet aux agents eclaireurs de communiquer avec les agents colorieurs
// Les agents eclaireurs ajoutent des ordres et les agents colorieurs les recupèrent depuis cette classe
public class CommunicationSystem {
	
	private static ArrayList<Order> Orders = new ArrayList<Order>();
	
	public static ArrayList<Order> consultOrders(ColoringAgent receivingAgent){
		
		ArrayList<Order> allOrders = new ArrayList<Order>();
		
		for (Order order : Orders){
			if (order.getIntendedAgent() == receivingAgent){
				allOrders.add(order);
			}
		}
		// On supprime les ordres après les avoir lu
		clearOrdersForAgent(receivingAgent);
		return allOrders;
	}
	
	public static void addOrder(Order order){
		Orders.add(order);
	}
	
public static void clearOrdersForAgent(ColoringAgent Agent){
		
		ArrayList<Order> ordersToDelete = new ArrayList<Order>();
		
		for (Order order : Orders){
			if (order.getIntendedAgent() == Agent){
				ordersToDelete.add(order);
			}
		}
		
		for (Order order : ordersToDelete){
			Orders.remove(order);
		}
		
	}

}
