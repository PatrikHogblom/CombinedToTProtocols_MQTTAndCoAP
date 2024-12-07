package com.combinediot.revisitiot.GatewayProgram.MQTTbroker;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;




public class MQTTBroker{

	//Map<String, SubscriptionHandler> topics  = new HashMap<>();
	
	public static void main(String[] args) {
		
		ServerSocket broker = null;
		ArrayList<Socket> connectedClients = new ArrayList<>(); //Holds all clients sockets that are connected to this broker
		
		while(true)
		{
			try {
				broker = new ServerSocket(1883);
				broker.setReuseAddress(true);
				
				//Run a infinite loop for getting client requests
				Socket client = broker.accept();
				
				//Display that a new client is connected to broker 
				System.out.println("New client connected" + client.getInetAddress().getHostAddress());
				
				//Create a new thread object 
				ClientHandler clientSocket = new ClientHandler(client);
				
				//This thread will handle the client separately
				new Thread(clientSocket).start();
				
				//Add the client to array connectedClients
				connectedClients.add(client);
				//System.out.println("Connected clients in the broker are: " + connectedClients);
				
				
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			finally {
				if(broker != null) {
					try {
						broker.close();
					} catch (Exception e2) {
						e2.printStackTrace();
					}
				}
			}
		}

	}
}
