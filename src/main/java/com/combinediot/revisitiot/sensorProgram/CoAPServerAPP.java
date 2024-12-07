package com.combinediot.revisitiot.sensorProgram;

//import org.eclipse.californium.core.CoAPServer;



/*
Below are some tips on what you can extend the basic skeleton project with:
	-Run on real IoT devices, like Raspberry Pi
	-Making it actually correspond to a real scenario (smart home, smart healthcare, industrial control system, etc.)
	-Some form of actuator (alarm, controller, of just a screen)
	-Adding more sensors and actuators, both CoAP and MQTT connected.
	-Real-world sensing and actuating using real sensors/actuators
	-Arduino CoAP sensor server (ex. ESP32, 8266)
	-REST interface
	-More advanced measurements and evaluation.
	-Security aspects (using SSL/TLS, attacking the system, Denial of Service etc.)
	-Making your system compatible with other IoT platforms such as NodeRed, TheThings.io, etc. 
 */

public class CoAPServerAPP {
    public static void main(String[] args) {
        try {
            CoAPServer server = new CoAPServer();
            server.start(); // Start the Californium CoAP server
            System.out.println("CoAP Server is up and running...");
        } 
        catch (Exception ex) 
        {
            System.err.println("Error starting the CoAP Server: " + ex.getMessage());
        }
    }
}

