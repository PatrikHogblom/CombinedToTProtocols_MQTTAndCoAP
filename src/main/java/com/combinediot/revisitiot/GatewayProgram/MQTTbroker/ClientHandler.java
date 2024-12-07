package com.combinediot.revisitiot.GatewayProgram.MQTTbroker;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

public class ClientHandler extends Thread{
	
	private final Socket clientsocket;
	MQTTTopicSocketHandler topicsmap = new MQTTTopicSocketHandler();
	public ArrayList<Long> publishTimetoAllSocketsMeasurement = new ArrayList<>();
	int timeval_count = 0;
	//private Map<Socket, byte[]> topics = new HashMap<>();
	
	public ClientHandler(Socket socket) {
		// TODO Auto-generated constructor stub
		this.clientsocket = socket;
	}
	
	@Override
	public void run(){
		InputStream is;
        OutputStream os;
        byte[] buffer;
		
		try {
			
			
            is = clientsocket.getInputStream();
            os = clientsocket.getOutputStream();
            buffer = new byte[1024];
                             
			while(true)
			{
								
				is.read(buffer);//message sent from the client
				var byteArrayInputStream = new ByteArrayInputStream(buffer);
				var streamFirstByte = byteArrayInputStream.read();//first byte of MQTT fixed header
				Integer remainingLength = byteArrayInputStream.read();//second byte, i.e Remaining length(1-4 bytes)
				
				int MessageType = (streamFirstByte >> 4);
				
				//System.out.println("byteArrayInputStream: " + byteArrayInputStream);
				//System.out.println("byteArrayInputStream: " + streamFirstByte);
				//System.out.println("Typemessage: " + (streamFirstByte >> 4));
				//System.out.println("RemianingLength: " + remainingLength);
				
				//create a decoder for the buffer so that we can see what message were sent and if its correct
				switch (MessageType) {
				case 1://Connect Message
					System.out.println("CONNECT ACK");
					
					//i.e 16 -> 10000 ->correct if messageid is MQTT
					//decode the 
					
					//Protocol name 2 bytes = MSB and LSB, then a fixed amount of bytes for each letter in the id, and lastly a byte for level 
					var connMSB = byteArrayInputStream.read();
					var connLSB = byteArrayInputStream.read(); 
					
					
					char[] connMessageID = new char[connLSB];
					//read print the id name 
					for(int i = 0; i < connLSB; i++)
					{
						//var ProtocolLetter = byteArrayInputStream.read();
						connMessageID[i] = (char) ((byte) byteArrayInputStream.read() & 0xff);
						//System.out.println(MessageID[i]);
					}
					//System.out.println(connMessageID);
					var connLevel = byteArrayInputStream.read();
					
					//flags 1 byte 
					var connFlags =  byteArrayInputStream.read();
					
					//Keep Alive 2 bytes
					var connKeepAliveMSB = byteArrayInputStream.read();
					var connKeepAliveLSB = byteArrayInputStream.read();
					
					System.out.println( "MSB: " + connMSB + "LSB: " +connLSB+ "MessageId: " + String.valueOf(connMessageID) + 
							" Level: " + connLevel + " flags: "+ connFlags + " keepMSB: "+ connKeepAliveMSB + 
							" keepsLSB: " + connKeepAliveLSB);
					
					//Payload -> can include clientID length and name 
					
					
					
					/*Connect Acknowlegdement Message*/
										
					//Header
					byte MsgHeader1stByte = 0b00100000; //MQTT control packet(2), reserved = 0 
					byte MsgHeader2ndByte = 0b00000010; //Remaining Length(2)
					
					//Variable Length Header
					byte ConnectAckFlags = 0b00000000;
					byte ConnectReturnCode = 0b00000000;//i.e. 0x00 connection accepted 
					
					byte[] ConnAckMsg = {(byte) MsgHeader1stByte, (byte)MsgHeader2ndByte, (byte)ConnectAckFlags, (byte) ConnectReturnCode};
					
					os.write(ConnAckMsg);
					os.flush();
					break;
			
				case 3: //Publish Message 
					//The program should be able to receive and 
					//handle clients that want to publish messages to certain topics.
					
					//2 bytes controlpacket and remaining length(7 + x(length of payload))
					
					//get the Qoslevel
					var MQTTcontrolPacketType = (streamFirstByte >> 4); 
					byte DUPFlag =  (byte) ((streamFirstByte &  0b00001000)>>3);
					byte QoSlevel =  (byte) ((streamFirstByte & 0b00000110)>>1);
					byte RETAIN = (byte)((streamFirstByte & 0b00000001));
					
					System.out.println("MQTTcontrolPacketType: "+ MQTTcontrolPacketType+ " DUPFlag: "+ DUPFlag +
							" QoSlevel: "+ QoSlevel + " RETAIN: " +RETAIN );
					
					//TopicLength
					var pubMSB = byteArrayInputStream.read();
					var pubLSB = byteArrayInputStream.read(); 
					
					//TopicChar
					byte[] topicChars = new byte[pubLSB];
					char[] topicResult = new char[pubLSB];
					//read print the id name 
					for(int i = 0; i < pubLSB; i++)
					{
						topicChars[i] = (byte) byteArrayInputStream.read();
						char topicLetter = (char) ((byte) topicChars[i] & 0xff);
						topicResult[i] = topicLetter; 
					}
					
					//Something is wrong when getting packet identifier? for example payload becomes 
					
					//System.err.println("Topic:" + String.valueOf(topicResult));
					
					//Packet Identifier 
					//var pubPacketIdentifierMSB = byteArrayInputStream.read();
					//var pubPacketIdentifierLSB = byteArrayInputStream.read();
					
					//System.out.println( "MSB: " + pubMSB + "LSB: " +pubLSB+ "MessageId: " + String.valueOf(topicResult) + 
					//		" pubPacketIdentifierMSB: " + pubPacketIdentifierMSB + " pubPacketIdentifierLSB: "+ pubPacketIdentifierLSB);
					
					
					//Payload
					/*int payloadlettersamount = remainingLength - pubLSB; //remaininglength -(length MSB/LSB(bytes) - topics letters(bytes) - packet id(bytes))
					byte[] payloadChars = new byte[payloadlettersamount];
					char[] payloadResult = new char[payloadlettersamount];
					for(int i = 0; i < payloadlettersamount; i++)
					{
						//var ProtocolLetter = byteArrayInputStream.read();
						payloadChars[i] = (byte) byteArrayInputStream.read();
						//System.out.println(payloadChars[i]);
						char payloadLetter = (char) ((byte) payloadChars[i] & 0xff);
						payloadResult[i] = payloadLetter; 
					}
					System.err.println("Paylaod:" + String.valueOf(payloadResult));*/
					
					/*Publish acknowlegdement*/
					byte FirstbyteHeader = 0b01000000;
					byte SendondbyteHeader = 0b00000010; //Remaining length(2)
					
					if(QoSlevel == 0) {
						//None-response
						
						//Payload
						int payloadlettersamountQos1 = remainingLength - pubLSB; //remaininglength -(length MSB/LSB(bytes) - topics letters(bytes) - packet id(bytes))
						byte[] payloadChars1 = new byte[payloadlettersamountQos1];
						char[] payloadResult1 = new char[payloadlettersamountQos1];
						for(int i = 0; i < payloadlettersamountQos1; i++)
						{
							//var ProtocolLetter = byteArrayInputStream.read();
							payloadChars1[i] = (byte) byteArrayInputStream.read();
							//System.out.println(payloadChars[i]);
							char payloadLetter = (char) ((byte) payloadChars1[i] & 0xff);
							payloadResult1[i] = payloadLetter; 
						}
						System.err.println("Paylaod:" + String.valueOf(payloadResult1));
						
						System.out.println("PUBLISH - QoS level 0");
					}
					else if(QoSlevel == 1) {
						//PUBACK Packet - response
						
						//Something is wrong when getting packet identifier? for example payload becomes 
						
						//System.err.println("Topic:" + String.valueOf(topicResult));
						
						//Packet Identifier 
						var pubPacketIdentifierMSB = byteArrayInputStream.read();
						var pubPacketIdentifierLSB = byteArrayInputStream.read();
						
						System.out.println( "MSB: " + pubMSB + "LSB: " +pubLSB+ "MessageId: " + String.valueOf(topicResult) + 
								" pubPacketIdentifierMSB: " + pubPacketIdentifierMSB + " pubPacketIdentifierLSB: "+ pubPacketIdentifierLSB);
						
						
						//Payload
						int payloadlettersamount = remainingLength - pubLSB; //remaininglength -(length MSB/LSB(bytes) - topics letters(bytes) - packet id(bytes))
						byte[] payloadChars = new byte[payloadlettersamount];
						char[] payloadResult = new char[payloadlettersamount];
						for(int i = 0; i < payloadlettersamount; i++)
						{
							//var ProtocolLetter = byteArrayInputStream.read();
							payloadChars[i] = (byte) byteArrayInputStream.read();
							//System.out.println(payloadChars[i]);
							char payloadLetter = (char) ((byte) payloadChars[i] & 0xff);
							payloadResult[i] = payloadLetter; 
						}
						System.err.println("Paylaod:" + String.valueOf(payloadResult));
						
						
						System.out.println("PUBLISH ACK - QoS level 1");
						byte [] pubAck = { (byte) FirstbyteHeader, (byte) SendondbyteHeader, (byte) pubPacketIdentifierMSB, (byte) pubPacketIdentifierLSB};
						os.write(pubAck);
					}
					else if(QoSlevel == 2){
						System.out.println("PUBLISH - QoS level 2");
						//PUBREC(publish received) packet, part 1 - response 
						//PUBREL(Publish Release), part 2
						//PUBCOMP(Publish complete), part 3
					}
					
					
					ArrayList<Socket> so = new ArrayList<Socket>();
					so = topicsmap.getSocketsOfSpecificTopic(String.valueOf(topicResult));
					
					System.err.println(so);
					
					//Measure the time we send the publish messages here?
					long startPublishAllSocketTime = 0;
					long endPublishAllSocketTime = 0;
					startPublishAllSocketTime = System.nanoTime();//Start timer
					for(int i = 0; i <= so.size()-1; i++) {
						System.out.println(so.get(i));
						//Send message to sockets existing in the list
						Socket newSocket = new Socket();
						newSocket = so.get(i);
						OutputStream sendPublishtoSocket = newSocket.getOutputStream();
						
						//Copy the publish message from buffer and send a new publish message to client sockets
						sendPublishtoSocket.write(Arrays.copyOfRange(buffer, 0, 2+remainingLength));
						sendPublishtoSocket.flush();
					}
					endPublishAllSocketTime = System.nanoTime();//end time
					long calcPublishTimeToAllSockets = (endPublishAllSocketTime-startPublishAllSocketTime);
					publishTimetoAllSocketsMeasurement.add(calcPublishTimeToAllSockets);
					
					if(publishTimetoAllSocketsMeasurement.size() == 100)
					{
						try {
							File outputFile = new File("C:\\Users\\patri\\OneDrive\\Dokument\\InternetOfThings\\project3-combinedIoT\\measuredvalues\\MQTTbrokerMeasuredTimesByPublishToAllSockets.txt");
							Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile), "UTF-8"));
							for (long id : publishTimetoAllSocketsMeasurement) {
							    writer.write(id + "\n");
							}
							writer.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					
					
					timeval_count++;
					
					
					//sendtoSocket = topicsmap.getSocket();
					os.flush();
					break;
					 
				case 8: //Subscribe
					//The program should be able to receive and 
					//handle client requests for subscribing to different topics.
					
					//Package ID
					var PacketIDMSB = byteArrayInputStream.read();
					var PacketIDLSB = byteArrayInputStream.read();
					System.out.println("PacketIDMSB: " + PacketIDMSB + "PacketIDLSB: " + PacketIDLSB );
					
					//Payload
					//--Topic Filter 
					var LengthMSB = byteArrayInputStream.read();
					var LengthLSB = byteArrayInputStream.read();
					char[] TopicFilter = new char[LengthLSB];
					
					for(int i = 0; i < LengthLSB; i++)
					{
						TopicFilter[i] = (char) ((byte) byteArrayInputStream.read() & 0xff);
					}
					
					String topic = String.valueOf(TopicFilter);
					
					//-- Requested QoS			
					var byteNplus1_reqQoS = byteArrayInputStream.read();
					
					System.out.println( "PacketIDMSB: " + PacketIDMSB + " PacketIDLSB: " +PacketIDLSB+ " LengthMSB: " + LengthMSB + 
							" LengthLSB: " + LengthLSB + " TopicFilter: "+ topic + " byteNplus1_reqQoS: "+ byteNplus1_reqQoS);
					
					//Test new topics handler
					topicsmap.addSocketAndToopic(clientsocket, topic);
					//System.err.println("Showing the list in subscibe: listtopic");
					//topicsmap.showMultimap();
					
					/*Save Socket and topic to a list map*/
					//topicsmap.addTopicAndSocket(clientsocket, topic);
					
					/*Create subscribe ACK*/
					System.out.println("SUBSCRIBE ACK");
					byte[] subAckMsg = {(byte) 0b10010000, (byte) 0b00000011, (byte) PacketIDMSB, (byte) PacketIDLSB, 0x01};
					os.write(subAckMsg);
					os.flush();
					break;
					
				case 10: //Unsubscribe
					//The program should be able to receive and handle client request 
					//for unsubscribing to previously subscribed topics.
					
					
					//PacketID
					var unsubPacketIdentifierMSB = byteArrayInputStream.read();
					var unsubPacketIdentifierLSB = byteArrayInputStream.read();
					
					//Payload		
					var unsubLengthMSB = byteArrayInputStream.read();
					var unsubLengthLSB = byteArrayInputStream.read();
					
					byte[] unsubTopicFilter = new byte[unsubLengthLSB];
					char[] unsubTopicFilterChar = new char[unsubLengthLSB];
					for(int i = 0; i < unsubLengthLSB; i++)
					{
						unsubTopicFilter[i] = (byte) byteArrayInputStream.read();
						char topicLetter = (char) ((byte) unsubTopicFilter[i] & 0xff);
						unsubTopicFilterChar[i] = topicLetter; 
					}
					//System.err.println("Topic:" + String.valueOf(unsubTopicFilterChar));
					
					System.out.println( "PacketIDMSB: " + unsubPacketIdentifierMSB + " PacketIDLSB: " +unsubPacketIdentifierLSB+ " LengthMSB: " + unsubLengthMSB + 
							" LengthLSB: " + unsubLengthLSB + " TopicFilter: "+ unsubTopicFilter);
					
					/*Unsubscribe Acknowlegdement*/
					System.out.println("UNSUBSCRIBE ACK");
					
					byte unsubFirstbyteHeader = (byte)(0b10110000);
					byte unsubSecondbyteHeader = 0b00000010; //Remaining length(2)
					byte unsubPacketIDtoAckMSB =  (byte)unsubPacketIdentifierMSB;
					byte unsubPacketIDtoAckLSB =  (byte)unsubPacketIdentifierLSB;
					byte[] unsubAckMsg = {(byte)unsubFirstbyteHeader, (byte)unsubSecondbyteHeader, (byte)unsubPacketIDtoAckMSB, (byte)unsubPacketIDtoAckLSB};
					os.write(unsubAckMsg);
					os.flush();
					
					/*Remove the topic and client from the socket/topic map*/
					topicsmap.removeSpecificTopicAndSocket(clientsocket,  String.valueOf(unsubTopicFilterChar));
					//topicsmap.removeSocketOfSpecificTopic(clientsocket, String.valueOf(unsubTopicFilterChar));
					break;
					
				case 12: //PingRequest
					//The program should be able to receive and handle clients that sends MQTT Ping requests
					System.out.println("Remaining Length:" + remainingLength);
					/*Ping ACK/response*/
					System.out.println("PING ACK");
					byte pingFirstbyteHeader = (byte)(0b11010000);
					byte pingSecondbyteHeader = 0b00000000; //Remaining length(2)
					byte[] pingResponse = {(byte)pingFirstbyteHeader, (byte)pingSecondbyteHeader};
					os.write(pingResponse);
					os.flush();
					break;
					
				case 14: //Disconnect
					 System.out.println("Broker Disconnection!");
					 //os.flush();
					 clientsocket.close(); 
					break;
					
				default:
					break;
				}
				//topicsmap.showTopicsAndSockets();
				//topicsmap.showTopicsSockets();
				System.err.println("Showing the list in subscibe: topics Multimap");
				topicsmap.showMultimap();
			}
    		
			
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
	
}
