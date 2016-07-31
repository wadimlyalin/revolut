package ru.vadimlyalin;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.glassfish.grizzly.http.server.HttpServer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class TransferControllerTest {

    private HttpServer server;
    private WebTarget target;

    @Before
    public void setUp() throws Exception {
        // start the server
        server = Main.startServer();
        // create the client
        Client c = ClientBuilder.newClient();

        target = c.target(Main.BASE_URI);
    }

    @After
    public void tearDown() throws Exception {
        server.stop();
    }

    @Test
    public void testTransfer() {
        int status = target.path("/transfers/from/1/to/2/sum/1").request().method("POST").getStatus();
        assertEquals(status, Response.Status.CREATED.getStatusCode());
    }

	@Test
	public void testTransfer_Error() {
		int status = target.path("/transfers/from/1/to/2/sum/100").request().method("POST").getStatus();
		assertEquals(status, Response.Status.NOT_FOUND.getStatusCode());
	}
}
