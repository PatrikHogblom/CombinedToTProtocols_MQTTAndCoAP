package com.combinediot.revisitiot.GatewayProgram.CoAPClient;

import java.util.ArrayList;

/*      0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 
	 * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
	 * |Ver| T |  TKL  |     Code      |         Message ID            | 
	 * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
	 * |  Token(if any, TKL bytes) ....                                |
	 * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
	 * | Options(if any) ...                                           |
	 * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
	 * |1 1 1 1 1 1 1 1 |           Payload(if Avaliable)              |
	 * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+ 
	 * 
*/

public class CoAPMessageFormat {
	
	private int CoAPVersion;
	private CoAPMessageType coapType;
	private int tokenLength;
	private CoAPMethodCodes coapMethodCodes;
	private CoAPResponseCodes coapResponseCodes;
	private int coapmessageID;
	private CoAPOptionType coapOptionCodes;
	private int coapOptionLengthValue; // ie. length = 4
	private String coapOptionStringValue; // i.e. "sink"
	private CoAPContentFormats coapContentFormat;
	//private int PayloadBoolean;
	private String payload;
	
	//Constructor, set default parameters
	public CoAPMessageFormat() {}
	public byte[] CoAPMessage(
			int CoAPVersion,
			CoAPMessageType coapType,
			int tokenLength,
			CoAPMethodCodes coapMethodCodes,
			//CoAPResponseCodes coapResponseCodes,
			int coapmessageID,
			CoAPOptionType coapOptionCodes,
			CoAPContentFormats coapContentFormat,
			String coapOptionStringValue, // i.e. "sink"
			String payload) 
	{
		
		ArrayList<Byte> MessageByte = new ArrayList<Byte>();
		
		try {
			setCoAPVersion(CoAPVersion);
			setCoAPMessageType(coapType);
			setCoAPTokenLength(tokenLength);   
			setCoAPMethodCode(coapMethodCodes);
			setCoAPMessageID(coapmessageID);
			String msgID_bin = mainCoAP.toBinary(getCoAPMessageID(),16);        		
	    	String msgID_firsthalf = msgID_bin.substring(0, msgID_bin.length()/2);
	        String msgID_secondhalf = msgID_bin.substring(msgID_bin.length()/2);
	            
	        MessageByte.add((byte)((int)(getCoAPVersion() << 6) | (getCoAPMessageType().getInteger() << 4)| (getCoAPTokenLength())));
	        MessageByte.add((byte)(int)(getCoAPMethodCode().getInteger())); //Byte 2: (0000 0001) GET -> Code                  		
	        MessageByte.add((byte)((int)mainCoAP.binaryToInteger(msgID_firsthalf))); //Byte 3: (1010 1010) Random msg id number part 1
	        MessageByte.add((byte)((int)mainCoAP.binaryToInteger(msgID_secondhalf))); //Byte 4: (0101 0101) Random msg id number part 2
			
	        setCoAPOptionType(coapOptionCodes);
			setCoAPOptionLengthValue(coapOptionStringValue.length());
			setCoAPOptionStringValue(coapOptionStringValue);
			
			int coapDelta = CoAPOptionExtended.getOptionExtendedCode(CoAPOptionExtended.getOptionExtended(getCoAPOptionType().getInteger()),getCoAPOptionType().getInteger());
			int coapLength = CoAPOptionExtended.getOptionExtendedCode(CoAPOptionExtended.getOptionExtended(getCoAPOptionLengthValue()),getCoAPOptionLengthValue());
			
			//System.out.println("Delta is: " + coapDelta +"Length is" + coapLength);
			
			MessageByte.add((byte)((coapDelta<< 4)|(coapLength)));
				
			char[] chars = getCoAPOptionStringValue().toCharArray();
				
			for (char c : chars) {
				String binary = Integer.toBinaryString(c);
				MessageByte.add((byte)((int) mainCoAP.binaryToInteger(binary)));
			}
			
			
				
			if(payload != null) {
				//Content format to be used in payload
				setCoAPContentFormat(coapContentFormat);
				MessageByte.add((byte) 17); //0001 0001 -> delta 1, length 1
				MessageByte.add((byte)(int)getCoAPContentFormat().getInteger());
				
				MessageByte.add((byte) 0xFF); //i.e. 1111 1111
				setCoAPPayload(payload);
					
				char[] paychars = getCoAPpayload().toCharArray();
					
				for (char c : paychars) {
					String binary = Integer.toBinaryString(c);
					MessageByte.add((byte)((int) mainCoAP.binaryToInteger(binary)));
				}
			}
			/*else
			{
				MessageByte.add((byte) 0x00);
				//setCoAPPayload(payload);
			}*/
				
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		byte[] sendMessageBytes = new byte[MessageByte.size()];
		for(int i = 0; i <= MessageByte.size()-1; i++) {				
			sendMessageBytes[i] = (byte) MessageByte.get(i);
			//System.out.println(MessageByte.get(i));
		}
		System.out.println(sendMessageBytes);
		return sendMessageBytes;
	}
	
	//Version
	public int setCoAPVersion(int CoAPVersion) throws Exception
	{
		if(CoAPVersion < 0x0 || CoAPVersion > 0x3)
            throw new Exception("CoAPVersion have a Max Size of 2-bits, version is currently: " + CoAPVersion);
		return this.CoAPVersion = CoAPVersion;
	}
	
	public int getCoAPVersion() {
		return this.CoAPVersion;
	}
	
	//T
	public void setCoAPMessageType(CoAPMessageType coapType) {
		this.coapType = coapType;
	}
	
	public CoAPMessageType getCoAPMessageType() {
		return coapType;
	}
	
	//TKL
	public void setCoAPTokenLength(int tokenLength) {
        this.tokenLength = tokenLength;
	}
	
	public int getCoAPTokenLength() {
	        return tokenLength;
	 }
	 
	
	//Codes: Method	
	public void setCoAPMethodCode(CoAPMethodCodes coapCodes) {
		// TODO Auto-generated method stub
		this.coapMethodCodes = coapCodes;

	}
	
	public CoAPMethodCodes getCoAPMethodCode() {
		return coapMethodCodes;
	}
	//Codes: Response
	public void setCoAPResponseCode(CoAPResponseCodes coapCodes) {
		// TODO Auto-generated method stub
		this.coapResponseCodes = coapCodes;

	}
	
	public CoAPResponseCodes getCoAPResponseCode() {
		return coapResponseCodes;
	}
	
	//MessageID
	public void setCoAPMessageID(int messageID) {
		this.coapmessageID = messageID;

	}
	public int getCoAPMessageID() {
		return coapmessageID;
	}
	
	//Options
	
	//Type -> delta 
	public void setCoAPOptionType(CoAPOptionType coapOptionCodes) {
		this.coapOptionCodes = coapOptionCodes;
	}
	
	public CoAPOptionType getCoAPOptionType() {
		return coapOptionCodes;
	}
	
	//Value -> length 
	//Using int values, such as specifying the length of options 
	public void setCoAPOptionLengthValue(int coapOptionLengthValue) {
		this.coapOptionLengthValue = coapOptionLengthValue;
	}
	
	public int getCoAPOptionLengthValue() {
		return coapOptionLengthValue;
	}
	
	public void setCoAPOptionStringValue(String coapOptionStringValue) {
		this.coapOptionStringValue = coapOptionStringValue;	
	}
	
	public String getCoAPOptionStringValue() {
		return this.coapOptionStringValue;
	}
	
	//ContentFormat
	public void setCoAPContentFormat(CoAPContentFormats coapContentFormat) {
		this.coapContentFormat = coapContentFormat;
	}
	
	public CoAPContentFormats getCoAPContentFormat() {
		return coapContentFormat;
	}
	
	//Payload
	public void setCoAPPayload(String payload) {
		this.payload = payload;
	}
	public String getCoAPpayload() {
		return payload;
	}
	
	
	
	
}
