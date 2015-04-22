package de.htw.lc.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.security.ConstraintMapping;
import org.eclipse.jetty.security.ConstraintSecurityHandler;
import org.eclipse.jetty.security.HashLoginService;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.security.Constraint;
import org.eclipse.jetty.util.security.Password;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.htw.lc.Response;
import de.htw.lc.RestClient;

@SuppressWarnings("serial")
public class RestClientTest {

    private Server server;
    private RestClient client;

    private static int PORT = 12345;
    private static String HOST = "localhost";

    @Before
    public void setup() {
        server = new Server(PORT);
        client = new RestClient();
    }

    private void startJetty() {
        try {
            server.start();
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @After
    public void stopJetty() throws Exception {
        server.stop();
    }

    @Test
    public void testGETplain() throws IOException {
        final String content = "REST GET TEST";
        server.setHandler(new AbstractHandler() {
            public void handle(String target, Request baseRequest, HttpServletRequest request,
                    HttpServletResponse response) throws IOException, ServletException {
                response.setContentType("text/plain;charset=utf-8");
                response.setStatus(HttpServletResponse.SC_OK);
                baseRequest.setHandled(true);
                response.getWriter().print(content);
            }
        });
        startJetty();
        Response response = client.get("http://" + HOST + ":" + PORT + "/");
        assertEquals(200, response.getCode());
        assertEquals(content, response.getBody());
    }

    /**
     * Creates a security handler that only accepts request of admin users.
     * 
     * @return handler
     */
    private ConstraintSecurityHandler createAdminSecurityHandler() {
        Constraint constraint = new Constraint();
        constraint.setName(Constraint.__FORM_AUTH);
        constraint.setRoles(new String[] { "admin" });
        constraint.setAuthenticate(true);

        ConstraintMapping constraintMapping = new ConstraintMapping();
        constraintMapping.setConstraint(constraint);
        constraintMapping.setPathSpec("/*");

        ConstraintSecurityHandler securityHandler = new ConstraintSecurityHandler();
        securityHandler.addConstraintMapping(constraintMapping);
        HashLoginService loginService = new HashLoginService();
        loginService.putUser("admin1", new Password("password"), new String[] { "admin" });
        loginService.putUser("user1", new Password("password"), new String[] { "user" });
        securityHandler.setLoginService(loginService);
        return securityHandler;
    }

    class HelloServlet extends DefaultServlet {
        @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException,
                IOException {
            response.getWriter().append("hello " + request.getUserPrincipal().getName());
        }
    }
    
    @Test
    public void testGETauth() throws IOException {
        ServletContextHandler context = new ServletContextHandler(server, "/", //
                ServletContextHandler.SESSIONS | ServletContextHandler.SECURITY);
        ServletHolder servletHolder = new ServletHolder(new HelloServlet());
        context.addServlet(servletHolder, "/*");
        context.setSecurityHandler(createAdminSecurityHandler());

        startJetty();
        String server = HOST + ":" + PORT + "/";
        Response response = client.get("http://admin1:password@" + server);
        assertEquals(200, response.getCode());
        assertEquals("hello admin1", response.getBody());

        response = client.get("http://" + server);
        assertEquals(401, response.getCode());

        response = client.get("http://unknown:secret@" + server);
        assertEquals(401, response.getCode());

        response = client.get("http://user1:password@" + server);
        assertEquals(403, response.getCode());
    }

}
