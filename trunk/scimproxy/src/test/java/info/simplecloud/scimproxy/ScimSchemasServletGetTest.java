package info.simplecloud.scimproxy;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mortbay.jetty.servlet.DefaultServlet;
import org.mortbay.jetty.testing.HttpTester;
import org.mortbay.jetty.testing.ServletTester;

public class ScimSchemasServletGetTest {

    private static HttpTester     request  = new HttpTester();
    private static HttpTester     response = new HttpTester();
    private static ServletTester  tester   = null;

    @BeforeClass
    public static void setUp() throws Exception {
        tester = new ServletTester();
        tester.addServlet(ScimSchemasServlet.class, "/v1/Schemas");
        tester.addServlet(DefaultServlet.class, "/");
        tester.start();
    }

    @Test
    public void getSchemas() throws Exception {
        request.setMethod("GET");
        request.setVersion("HTTP/1.0");
        request.setHeader("Authorization", "Basic dXNyOnB3");

        request.setURI("/v1/Schemas");
        response.parse(tester.getResponses(request.generate()));
        String r = response.getContent();

        Assert.assertEquals(200, response.getStatus());
    }

    @Test
    public void getSchemasJson() throws Exception {
        request.setMethod("GET");
        request.setVersion("HTTP/1.0");
        request.setHeader("Authorization", "Basic dXNyOnB3");
        request.setHeader("Accept", "application/json");

        request.setURI("/v1/Schemas");
        response.parse(tester.getResponses(request.generate()));
        String r = response.getContent();

        Assert.assertEquals(200, response.getStatus());
    }

   
    @Test
    public void getSchemasXml() throws Exception {
        request.setMethod("GET");
        request.setVersion("HTTP/1.0");
        request.setHeader("Authorization", "Basic dXNyOnB3");
        request.setHeader("Accept", "application/xml");

        request.setURI("/v1/Schemas");
        response.parse(tester.getResponses(request.generate()));
        String r = response.getContent();

        Assert.assertEquals(501, response.getStatus());
    }

}
