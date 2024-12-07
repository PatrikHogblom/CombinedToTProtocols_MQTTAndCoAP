package com.combinediot.revisitiot.GatewayProgram.CoAPClient;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;

public class DecoderCoAPMessage {
	
	/*Varables drom CoAP decoding*/
	private int decodeVersion;
	private int decodeType;
	private int decodeTokenlen;
	private int decodeCodeType;
	private int decodeMsgID;
	private byte[] decodeToken;
	private int decodeOptDelta;
	private int decodeOptLength;
	private int decodeBytePayloadExists;
	private StringBuilder decodePayloadString;
	
	public void CoAPByteDecoder(byte[] CoAPServerResonse) {
		// TODO Auto-generated constructor stub
		
		try(var byteArray = new ByteArrayInputStream(CoAPServerResonse)){
					
					//First byte have VER, TYPE, TOKEN Length
					var firstByte = byteArray.read();
					var version = (firstByte & 0b11000000) >> 6;
					var type = (firstByte & 0b00110000) >> 4;
					var tokenlen = (firstByte & 0b00001111);
					
					System.err.println("Version: " + version + " Type: " + CoAPMessageType.TypeName(type) + " Token Length: " +  tokenlen);		
					
					//Second byte have Request/Response Code
					var SecondByte_code = byteArray.read();
					System.err.println("Response code is: " + SecondByte_code + " Ans thus is of type: " + CoAPResponseCodes.CodeType(SecondByte_code));				
					
					//Thrid and forth byte is the messageId 
					
					var msgID_firstbyte = byteArray.read();
					var msgID_secondbyte = byteArray.read();
					var msgID = (((msgID_firstbyte & 0xFF) << 8) | (msgID_secondbyte & 0xFF) << 0);
					
					System.err.println("MessageID is: " + msgID );				
					
					//if token exists, it could only exist 0-8 bytes
					byte[] token = new byte[tokenlen];
					if(tokenlen != 0)
					{
						for(int i = 0; i < tokenlen; i++ ) {
							token[i] = (byte) byteArray.read();
							System.err.println( (i+1) + " byte of token is: " + token[i] );
						}
					}
					else
					{
						System.err.println( " No token exists!" );
						token = null;
					}
					
					//Now decode the options from the byte array 
					
					//First see if the options exists, i.e if the byte is not zero
					var byteOptions = byteArray.read();
					int optDelta = 0;
					int optLength = 0;
					if(byteOptions != 0)
					{
						optDelta = ((byteOptions & 0b11110000)>>4);
						optLength = (byteOptions & 0b00001111);
						System.err.println("Options gives: " + byteOptions + " i.e. Delta is " + optDelta + " and Length is: " + optLength);
					}
					else
					{
						System.err.println("Options doesn't exist");
					}
					
					
					var bytePayloadExists = byteArray.read();
					StringBuilder payloadString = new StringBuilder();
					if(bytePayloadExists == 0xff)
					{
						System.err.println("Payload exist: " + bytePayloadExists);
						boolean payloadNull = false;
		
						ArrayList<Character> payloadchar = new ArrayList<Character>();
						int i = 0;
						while(payloadNull == false)
						{
							var payload = byteArray.read();
							if(payload != 0)
							{
								System.err.println(i + " pos have char: "  + (char)payload + " Which is the value " + payload);
								payloadchar.add((char)payload);
							}
							else if(payload == 0) 
							{
								payloadNull = true;
							}
							
							i++;
						}
						
						//var payload = byteArray.readAllBytes();
						for (Character s : payloadchar) {
							payloadString.append(s);
						}
						
						System.err.println("Payload String: " + payloadString.toString());
					}
					else
					{
						System.err.println("Payload doesn't exist: ");
					}
					
					//return all decoded values
					this.decodeVersion = version;
					this.decodeType = type;
					this.decodeTokenlen = tokenlen;
					this.decodeCodeType = SecondByte_code;
					this.decodeMsgID = msgID;
					this.decodeToken = token;
					this.decodeOptDelta = optDelta;
					this.decodeOptLength = optLength;
					this.decodeBytePayloadExists = bytePayloadExists;
					this.decodePayloadString = payloadString;
				}
				catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}	
	}
	
	public int getCoAPDecodedVer() {
		return this.decodeVersion;
	}
	public int getCoAPDecodedType() {
		return this.decodeType;
	}
	public int getCoAPDecodedTokenlen() {
		return this.decodeTokenlen;
	}
	public int getCoAPdecodeCodeType() {
		return this.decodeCodeType;
	}
	public int getCoAPdecodeMsgID() {
		return this.decodeMsgID;
	}
	public byte[] getCoAPdecodeToken() {
		return this.decodeToken;
	}
	public int getCoAPdecodeOptDelta() {
		return this.decodeOptDelta;
	}
	public int getCoAPdecodeOptLength() {
		return this.decodeOptLength;
	}
	public int getCoAPdecodeBytePayloadExists() {
		return this.decodeBytePayloadExists;
	}
	public String getCoAPdecodePayloadString() {
		
		String payload = this.decodePayloadString.toString();
		System.out.println("Payload: " + payload + "Strbuilder: " + this.decodePayloadString);
		return payload;
	}
	

}
