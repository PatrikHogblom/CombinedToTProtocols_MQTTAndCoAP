package com.combinediot.revisitiot.GatewayProgram.Gateway;

import com.combinediot.revisitiot.GatewayProgram.CoAPClient.*;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Gateway extends Thread implements MqttCallback{
	
	
	private int qos = 1;
	private String brokerURL = "tcp://localhost:1883";//"tcp://broker.mqttdashboard.com:1883";
	private String clientId = "MQTTclientID";
	private MemoryPersistence persistence;
	protected boolean retained;
		 
	private MqttClient mqttclient;
		 
	Socket server;
	
	//MessageFormat 
	static CoAPMessageFormat coapMessage = new CoAPMessageFormat();
	DecoderCoAPMessage decodedByte = new DecoderCoAPMessage();
	Random rand = new Random();
	int messageid = rand.nextInt((int)Math.pow(2, 8)); //randomly take a number up to 255
	
	//CoAPMessageFormat coapMessage;
	
	//Time measured Arrays
	public ArrayList<Long> CoAPclient_TimeValuesMeasurement = new ArrayList<>();
	public ArrayList<Long> Mqttclient_TimeValuesMeasurement = new ArrayList<>();
	//int countTime = 0;
	
	
	public Gateway(String topic){
		
		try {
			//CoAP Client
			CoAPclient coapclient = new CoAPclient();
			
			//MQTT Client
			//Connect to the broker and subscribe the values
			
			this.clientId = Integer.toString(decodedByte.getCoAPdecodeMsgID());
			//this.payload = decodedByte.getCoAPdecodePayloadString();
			
			mqttclient = new MqttClient(this.brokerURL, this.clientId, this.persistence);
			mqttclient.setCallback(this);
			
			MqttConnectOptions connOpts = new MqttConnectOptions();
			connOpts.setCleanSession(true);
			connOpts.setKeepAliveInterval(60);
			
	        mqttclient.connect();
	      
	        //Subscribe to the client, (do this only once when the program starts?)
	        //mqttclient.subscribe(topic, qos);
	        mqttclient.subscribe(topic, qos);
			
			//Boolean runStatus = true;
			//BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			//+byte[] connectMessage = new byte[5];
			Runnable runRunnable =  new Runnable() {
				
				@Override
				public void run() {
					
					try {
						   String GETpath = topic;//this.topic; //br.readLine();
				        
					       // System.out.println("Write the value/text you want to PUT onto the path:");
					        String GETValue = "test";
							CoAPContentFormats GETcontentformat = CoAPContentFormats.TEXT_PLAIN;
							
							
							long startGETMessageTime_CoAPcli = 0;
							long endGETMessageTime_CoAPcli = 0;
							
							startGETMessageTime_CoAPcli = System.nanoTime();//start timer here?
							byte[] GETmessage = coapMessage.CoAPMessage(1, CoAPMessageType.CON, 0, CoAPMethodCodes.GET, messageid, CoAPOptionType.URI_PATH, GETcontentformat , GETpath, GETValue);
							//System.out.println(GETmessage);
							coapclient.sendMessage(GETmessage);
							byte[] GETreceived = coapclient.receiveMessage();	
							endGETMessageTime_CoAPcli = System.nanoTime();//end Timer here?
							long calctime_CoAPclient = endGETMessageTime_CoAPcli - startGETMessageTime_CoAPcli;
							CoAPclient_TimeValuesMeasurement.add(calctime_CoAPclient);
							messageid++;
							//System.out.println(GETreceived);
							
							//decode the received message, so that we can get the sensor value to be sent later to mqtt broker 
							decodedByte = new DecoderCoAPMessage();
							decodedByte.CoAPByteDecoder(GETreceived);
							
							//System.err.println( "decoded Payload in StrBuilder: " + decodedByte.getCoAPdecodePayloadString());
							
							//MQTTclient
							MqttMessage message = new MqttMessage();
							message.setQos(qos);
						    message.setPayload(decodedByte.getCoAPdecodePayloadString().getBytes());
						    message.setRetained(retained);
						    //start timer here?
						    
						    long startMqttclientPublishTime = 0;
							long endMqttclientPublishTime = 0;
							startMqttclientPublishTime = System.nanoTime();//start timer here?
						    mqttclient.publish(topic, message);
						    endMqttclientPublishTime = System.nanoTime();//start timer here?
						    long calctime_MQTTclient = endMqttclientPublishTime - startMqttclientPublishTime;
						    Mqttclient_TimeValuesMeasurement.add(calctime_MQTTclient);
							
							//countTime++;
						  //Add the mesaured times from arraylist to a txt.file
							if(Mqttclient_TimeValuesMeasurement.size() == 100)
							{
								try {
									File CoAPoutputFile = new File("C:\\Users\\patri\\OneDrive\\Dokument\\InternetOfThings\\project3-combinedIoT\\measuredvalues\\CoAPclient_TimeValuesMeasurement.txt");
									Writer CoAPwriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(CoAPoutputFile), "UTF-8"));
									for (long coapCliTime : CoAPclient_TimeValuesMeasurement) {
										CoAPwriter.write(coapCliTime + "\n");
									}
									CoAPwriter.close();
									
									File MQTToutputFile = new File("C:\\Users\\patri\\OneDrive\\Dokument\\InternetOfThings\\project3-combinedIoT\\measuredvalues\\Mqttclient_TimeValuesMeasurement.txt");
									Writer MQTTwriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(MQTToutputFile), "UTF-8"));
									for (long mqttCliTime : Mqttclient_TimeValuesMeasurement) {
										MQTTwriter.write(mqttCliTime + "\n");
									}
									MQTTwriter.close();
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						    
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
					
				}
			};
			
			ScheduledExecutorService ex = Executors.newScheduledThreadPool(1);
			ex.scheduleAtFixedRate(runRunnable, 0, 10, TimeUnit.SECONDS);
			//ex.scheduleAtFixedRate(runnableMQTT, 0, 5, TimeUnit.SECONDS);
			
			this.start();
	        
	        //publish to the broker 
	        
	        
	        //send a 
	        
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		

	}

	@Override
	public void connectionLost(Throwable cause) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {
		// TODO Auto-generated method stub
		
	}

}
