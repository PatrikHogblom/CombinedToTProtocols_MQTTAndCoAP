package com.combinediot.revisitiot.GatewayProgram.CoAPClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class CoAPclient{
		private DatagramSocket socket;
		private InetAddress adress;
		private int port = 5683;
		private DatagramPacket packetReceive;
		private BufferedReader bufferedReader;
		private Boolean runManageMessage = true; 
		//private Integer input;
		
		//MessageFormat 
		CoAPMessageFormat coapMessage;
		
		
		public CoAPclient() {
			
			try {
				socket = new DatagramSocket();
				adress = InetAddress.getByName("localhost");		
				socket.connect(adress, port);
				
				if(socket.isConnected())
				{
					System.out.println("The Socket is connected: "+socket.isConnected());
					//System.out.println("The socket is bounded: "+socket.isBound());
				}
				else
				{
					System.out.println("The Socket is disconnected: "+socket.isConnected());
					//System.out.println("The socket is isbounded: "+socket.isBound());
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			
		}
		
		
		public void sendMessage(byte[] connectMessage) {
			// TODO Auto-generated method stub
			//Send message
			DatagramPacket datagram = new DatagramPacket(connectMessage, connectMessage.length);
			try {
				socket.send(datagram);
				System.out.println("The packets are sent successfully");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		
		public byte[] receiveMessage() throws IOException {
				byte[] buffer = new byte[512]; 
				packetReceive = new DatagramPacket(buffer, buffer.length);
				socket.receive(packetReceive);
				byte[] receivedPacketByte = packetReceive.getData();
				
				int arr = 0;
				for(int i = 0; i < receivedPacketByte.length; i++) {
					arr = (int) (receivedPacketByte[i] & 0xff);//convert from signed byte to unsigned byte 
					//System.out.println("Header and payload: " + arr);
					if((int) (receivedPacketByte[i] & 0xff)/255 == 1)//payload exists 
					{
						System.out.println("payload exists");
					}
				}
				return receivedPacketByte;
		}
		
		
	}
