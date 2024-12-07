package com.combinediot.revisitiot.sensorProgram;

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.CoapServer;
import org.eclipse.californium.core.server.resources.CoapExchange;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class CoAPServer extends CoapServer {

    private final List<Long> sensorValuesMeasurement = new ArrayList<>();
    public static List<String> resourceList = new ArrayList<>(Arrays.asList("/rand", "/time"));

    public CoAPServer() {
        System.out.println("Starting CoAP Server...");
        add(new RandomNumberResource());
        add(new TimeResource());
    }

    private class RandomNumberResource extends CoapResource {
        public RandomNumberResource() {
            super("rand");
            getAttributes().setTitle("Random Number Resource");
        }

        @Override
        public void handleGET(CoapExchange exchange) {
            long startTime = System.nanoTime();
            String payload = "Random Number: " + ThreadLocalRandom.current().nextInt(0, 100);
            exchange.respond(payload);
            long endTime = System.nanoTime();
            sensorValuesMeasurement.add(endTime - startTime);
        }
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

}
