package com.combinediot.revisitiot.sensorProgram;

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.CoapServer;
import org.eclipse.californium.core.server.resources.CoapExchange;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



public class CoAPServer extends CoapServer {

    private final List<Long> sensorValuesMeasurement = new ArrayList<>();
    public static List<String> resourceList = new ArrayList<>(Arrays.asList("/time", "/temperature"));

    public CoAPServer() {
        System.out.println("Starting CoAP Server...");
        add(new TimeResource());
        add(new WeatherResource()); // Adding CPU Temperature Resource
    }

    private class TimeResource extends CoapResource {
        public TimeResource() {
            super("time");
            getAttributes().setTitle("Time Resource");
        }

        @Override
        public void handleGET(CoapExchange exchange) {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            exchange.respond("Current date and time: " + dtf.format(now));
        }
    }

    private class WeatherResource extends CoapResource {
        public WeatherResource() {
            super("temperature");
            getAttributes().setTitle("Mock Temperature Resource");
        }

        @Override
        public void handleGET(CoapExchange exchange) {
            // Generate mock temperature data
            double mockTemperature = 5.0 + Math.random() * 10;

            // Format the temperature to only two decimal points
            String formattedTemperature = String.format("%.2f", mockTemperature);

            // Send the properly formatted response
            exchange.respond("Current Mock Temperature: " + formattedTemperature + " Celcius");
        }
    }


}
