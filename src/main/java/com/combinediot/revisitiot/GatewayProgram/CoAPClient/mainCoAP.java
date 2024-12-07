package com.combinediot.revisitiot.GatewayProgram.CoAPClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Random;

public class mainCoAP {
	
	static CoAPMessageFormat coapMessage = new CoAPMessageFormat();
	
	public static String toBinary(int x, int len)
    {
        if (len > 0)
        {
            return String.format("%" + len + "s",Integer.toBinaryString(x)).replaceAll(" ", "0");
        }
 
        return null;
    }
	
	public static Integer binaryToInteger(String binary){
	    char[] numbers = binary.toCharArray();
	    Integer result = 0;
	    int count = 0;
	    for(int i=numbers.length-1;i>=0;i--){
	         if(numbers[i]=='1')result+=(int)Math.pow(2, count);
	         count++;
	    }
	    return result;
	}
	
	// Function to print the output
	public static void main(String[] args) throws Exception 
	{
		CoAPclient client = new CoAPclient();
		Boolean runStatus = true;
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		//+byte[] connectMessage = new byte[5];
		
		Random rand = new Random();
		int messageid = rand.nextInt((int)Math.pow(2, 8)); //randomly take a number up to 255
		
		while(runStatus) 
		{
			System.out.println("CoAP Client - Choose command");
	        System.out.println("----------------------" + "\n");
	        //System.out.println("0: simple test to send and receive according to notes");
	        System.out.println("1. POST");
	        System.out.println("2. PUT");
	        System.out.println("3. GET");
	        System.out.println("4. DELETE");
	        System.out.println("5. EXIT");
	        String input = br.readLine();
	        switch (input) 
	        {
				
	        	/*case "0":
	        		connectMessage[0] = (byte) 80; // //Byte 1: (0101 0000) -> Coap version 1(01), NON(XX01), no token(xxxx 0000)
					connectMessage[1] = (byte) 1; //Byte 2: (0000 0001) GET -> Code
					connectMessage[2] = (byte) 170; //Byte 3: (1010 1010) Random msg id number part 1
					connectMessage[3] = (byte) 85; //Byte 4: (0101 0101) Random msg id number part 2
					
					System.out.println(connectMessage[0]); //shoud be coap version (i.e. 80 if testing values is used)
	                System.out.println(connectMessage[1]);
	                System.out.println(connectMessage[2]);
	                System.out.println(connectMessage[3]);
					
					System.out.println(connectMessage);
					client.sendMessage(connectMessage); //should i use in get? -> seems you should only get received messages 
					byte[] test = client.receiveMessage();	
					System.out.println(test);
					break;*/
				
				//POST- should be able to send POST messages to send data to the sensor/acutator
	        	case "1": 
	        		System.out.println("Write the path you want POST to:");
                    String postpath = br.readLine();
                    
                    System.out.println(" Write the value/text you want to POST onto the path:");
                    String PostValue = br.readLine();
	        		//String path = "sink";
	        		CoAPContentFormats POSTcontentformat = CoAPContentFormats.TEXT_PLAIN; 
	        		//String PostValue = "hello world"; //payload
	        		
	        		byte[] postmessage = coapMessage.CoAPMessage(1, CoAPMessageType.CON, 0, CoAPMethodCodes.POST, messageid, CoAPOptionType.URI_PATH, POSTcontentformat , postpath, PostValue);
	        		messageid++;
	        		client.sendMessage(postmessage);
	        		break;
	        	
	        	//The program should be able to send PUT messages to send data to the sensor/actuator.
	        	case "2":
	        		System.out.println("Write the path you want PUT the value to:");
                    String Putpath = br.readLine();
                    
                    System.out.println("Write the value/text you want to PUT onto the path:");
                    String PUTValue = br.readLine();
	        		CoAPContentFormats PUTcontentformat = CoAPContentFormats.TEXT_PLAIN;
	        		
	        		byte[] putmessage = coapMessage.CoAPMessage(1, CoAPMessageType.CON, 0, CoAPMethodCodes.PUT, messageid, CoAPOptionType.URI_PATH, PUTcontentformat , Putpath, PUTValue);
	        		messageid++;
	        		client.sendMessage(putmessage);
	        		break;
	        	
	        	//The program should be able to send GET messages to retrieve data from the sensor/actuator.
	        	case "3":
	        		System.out.println("Write the path you want GET values from:");
                    String GETpath = br.readLine();
                    
                   // System.out.println("Write the value/text you want to PUT onto the path:");
                    String GETValue = null;
	        		CoAPContentFormats GETcontentformat = CoAPContentFormats.TEXT_PLAIN;
	        		
	        		byte[] GETmessage = coapMessage.CoAPMessage(1, CoAPMessageType.CON, 0, CoAPMethodCodes.GET, messageid, CoAPOptionType.URI_PATH, GETcontentformat , GETpath, GETValue);
	        		messageid++;
	        		client.sendMessage(GETmessage);
	        		byte[] GETreceived = client.receiveMessage();	
					System.out.println(GETreceived);
	        		break;
	        	
	        	//The program should be able to send DELETE messages to remove data from the sensor/actuator.
	        	case "4": 
	        		System.out.println("Write the path you want DELETE values from:");
                    String DELETEpath = br.readLine();
                    
                    //System.out.println("Write the value/text you want to DELETE onto the path:");
                    String DELETEValue =  null;
	        		CoAPContentFormats DELETEcontentformat = CoAPContentFormats.TEXT_PLAIN;
	        		
	        		byte[] DELETEmessage = coapMessage.CoAPMessage(1, CoAPMessageType.CON, 0, CoAPMethodCodes.DELETE, messageid, CoAPOptionType.URI_PATH, DELETEcontentformat , DELETEpath, DELETEValue);
	        		messageid++;
	        		client.sendMessage(DELETEmessage);
	        		byte[] DELETEreceived = client.receiveMessage();	
					System.out.println(DELETEreceived);
	        		break;
				
	        	case "5":
					runStatus = false;
					break;
				
	        	
				default:
					System.out.println("Enter valid option" + "\n");
			}
		}
		br.close();
	}
}
