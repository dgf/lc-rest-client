package de.htw.lc.test;

import static org.junit.Assert.*;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.junit.Test;

import de.htw.lc.RestClient;

public class RestClientTest {

    @Test
    public void testGET() {
        final String content = "REST GET";
        int port = 12345;
        Server server = new Server(port);
        server.setHandler(new AbstractHandler() {
            public void handle(String target, Request baseRequest,
                    HttpServletRequest request, HttpServletResponse response)
                    throws IOException, ServletException {
                response.setContentType("text/plain;charset=utf-8");
                response.setStatus(HttpServletResponse.SC_OK);
                baseRequest.setHandled(true);
                response.getWriter().print(content);
            }
        });
        server.setStopAtShutdown(true);
        try {
            server.start();
        } catch (Exception e) {
            fail(e.getMessage());
        }

        RestClient client = new RestClient();
        String response = client.get("http://localhost:" + port);
        assertEquals(content, response);
    }
}
