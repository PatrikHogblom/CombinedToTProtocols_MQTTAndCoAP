package CoAPServer;

import com.combinediot.revisitiot.sensorProgram.CoAPServer;
import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class CoAPServerTest {

    private static CoAPServer server;
    private static final String BASE_URI = "coap://localhost:5683";

    @BeforeAll
    static void startServer() {
        server = new CoAPServer();
        server.start();
        System.out.println("CoAPServer started.");
    }

    @AfterAll
    static void stopServer() {
        if (server != null) {
            server.stop();
            System.out.println("CoAPServer stopped.");
        }
    }

    @Test
    public void testTimeResource() {
        CoapClient client = new CoapClient(BASE_URI + "/time");

        CoapResponse response = client.get();
        assertNotNull(response, "Response should not be null.");
        assertEquals("2.05", response.getCode().toString(), "Expected response code 2.05 Content.");
        assertTrue(response.getResponseText().startsWith("Current date and time:"),
                "Response should contain current date and time.");

    }

    @Test
    public void testTemperatureResource() {
        CoapClient client = new CoapClient(BASE_URI + "/temperature");

        CoapResponse response = client.get();
        assertNotNull(response, "Response should not be null.");
        assertEquals("2.05", response.getCode().toString(), "Expected response code 2.05 Content.");
        assertTrue(response.getResponseText().contains("Mock Temperature"),
                "Response should contain mock temperature data.");
    }

    @Test
    public void testInvalidPath() {
        CoapClient client = new CoapClient(BASE_URI + "/invalid");

        CoapResponse response = client.get();
        assertNotNull(response, "Response should not be null.");
        assertEquals("4.04", response.getCode().toString(), "Expected 4.04 Not Found.");
    }

    @Test
    public void testUnsupportedMethod() {
        CoapClient client = new CoapClient(BASE_URI + "/time");

        CoapResponse response = client.post("data", 0);
        assertNotNull(response, "Response should not be null.");
        assertEquals("4.05", response.getCode().toString(), "Expected 4.05 Method Not Allowed.");
    }
}




