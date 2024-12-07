package com.combinediot.revisitiot.GatewayProgram.MQTTclient;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Arrays;


//https://www.eclipse.org/paho/index.php?page=clients/java/index.php
public class MQTTClient implements MqttCallback{
	
	//Variables 
	 private boolean runProgram = false;
	 private BufferedReader reader;
	 
	 //Important MQTT variables
	 private String topic ;//       = "MQTT Examples";
	 private String payload; //      = "Message from MqttPublishSample";
	 private int qos;//             = 2;
	 private String brokerURL;//       = "tcp://localhost:1883";//"tcp://broker.mqttdashboard.com:1883";
	 private String clientId ;//    = "MQTTclientID";
	 private MemoryPersistence persistence;
	 protected boolean retained;
	 
	 private MqttClient client;
	 
	 Socket server;
	 
	public static void main(String[] args) {
	
		new MQTTClient();

		
	}
	
	
	public MQTTClient() {
		
		System.out.println("MQTT client starts!");
		this.runProgram = true;
		this.topic = "a/b";
		this.payload = "Payload example";
		this.qos = 1;
		this.brokerURL ="tcp://localhost:1883";
		this.clientId = "MQTT";
		this.reader = new BufferedReader(new InputStreamReader(System.in));
		this.persistence = new MemoryPersistence();
		this.retained = false;
		
		
		
		
		while(runProgram == true)
		{
			System.out.println("----------------------------------");
	    	System.out.println("1. Create and connect to broker");
	    	System.out.println("2. Subscribe to the broker");
	    	System.out.println("3. Unsubscribe to the broker");
	    	System.out.println("4. Publish to the broker");
	    	System.out.println("5. MQTT ping");
	    	System.out.println("6. Disconnect  from broker");
	    	System.out.println("----------------------------------");
	    	
	    	try {
				Integer input = Integer.parseInt(reader.readLine());
				switch (input) {
				
				//Create and connect to the broker
				case 1:
					client = new MqttClient(this.brokerURL, this.clientId, this.persistence);
					client.setCallback(this);
					
					MqttConnectOptions connOpts = new MqttConnectOptions();
					connOpts.setCleanSession(true);
					connOpts.setKeepAliveInterval(60);
					
			        System.out.println("Connecting to broker: "+brokerURL);
			        client.connect();
			        System.out.println("Connected to broker: " + brokerURL + " with clientID: "+ clientId);
					break;
					
				case 2: //Subscribe to the Broker 
					System.out.println("Subscribing to topic \"" + topic +"\" qos " + qos);
					client.subscribe(topic, qos);
					break;
				
				case 3: //Unsubscribe to the broker
					System.out.println("Unsubscribe the topic from the MQTT broker");
					String testUnsubscribeTopic = "a/b";  
					client.unsubscribe(topic);
					break;
					
				case 4: //Publish to the broker 
					System.out.println("Publish to the broker");
					System.out.println("Publishing message: "+ payload);
			        MqttMessage message = new MqttMessage();
			        message.setQos(qos);
			        message.setPayload(payload.getBytes());
					message.setRetained(retained);
			        client.publish(topic, message);
			        break;
			        
				case 6: //Diconnect from broker 
					
					if(client.isConnected())
					{
						client.disconnect();
						//client.close();
						//System.out.println("Closing and Disconnecting from the MQTT Broker!");
					}
					else
					{
						System.out.println("Cannot Diconnect!");
					}
					break;

				default:
					break;
				}
				
				//server = new Socket("localhost", 1883);
				//readMessagesFromBroker(server);
				//System.err.println(client);
				
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
	}


	@Override
	public void connectionLost(Throwable cause) {
		// TODO Auto-generated method stub
		System.out.println("Connection to " + brokerURL + " is lost " + cause);
		
	}


	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		//is called when message arrives from broker
		//System.err.println("Message from broker received");
		System.err.println("Topic: "+ topic + "Message: " + message);
	}


	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {
		// TODO Auto-generated method stub
		System.out.println("Delivery complete callback: Publish Completed "+ Arrays.toString(token.getTopics()));
		
	}
}
