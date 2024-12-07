package com.combinediot.revisitiot.GatewayProgram.Gateway;

/*
 * Starting order
 * 1.CoAPServerAPP
 * 2. MQTTbroker
 * 3. GateWay
 * */


public class GatewayProg{
		 
	public static void main(String[] args) {
	
		new Gateway("temperature");
		new Gateway("time");
	}
}
