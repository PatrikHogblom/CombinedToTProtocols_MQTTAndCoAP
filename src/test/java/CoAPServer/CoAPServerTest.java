package CoAPServer;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.elements.exception.ConnectorException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CoAPServerTest {

    private static CoapClient timeClient;
    private static CoapClient tempClient;

    @BeforeAll
    public static void setup() {
        // Initialize clients
        timeClient = new CoapClient("coap://localhost:5683/time");
        tempClient = new CoapClient("coap://localhost:5683/temperature");
    }

    @AfterAll
    public static void tearDown() {
        // Shutdown clients
        if (timeClient != null) {
            timeClient.shutdown();
        }
        if (tempClient != null) {
            tempClient.shutdown();
        }
    }

    @Test
    public void testTimeEndpoint() {
        try {
            CoapResponse response = timeClient.get();
            assertNotNull(response, "Response should not be null");
            assertTrue(response.isSuccess(), "Response should be successful");
            System.out.println("Response from /time: " + response.getResponseText());
        } catch (ConnectorException | IOException e) {
            System.err.println("Error during GET request for /time: " + e.getMessage());
            assertTrue(false, "Exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testTemperatureEndpoint() {
        try {
            CoapResponse response = tempClient.get();
            assertNotNull(response, "Response should not be null");
            assertTrue(response.isSuccess(), "Response should be successful");
            System.out.println("Response from /temperature: " + response.getResponseText());
        } catch (ConnectorException | IOException e) {
            System.err.println("Error during GET request for /temperature: " + e.getMessage());
            assertTrue(false, "Exception occurred: " + e.getMessage());
        }
    }
}
