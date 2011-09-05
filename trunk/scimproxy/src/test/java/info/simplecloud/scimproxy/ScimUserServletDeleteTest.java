package info.simplecloud.scimproxy;

import info.simplecloud.core.User;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mortbay.jetty.servlet.DefaultServlet;
import org.mortbay.jetty.testing.HttpTester;
import org.mortbay.jetty.testing.ServletTester;

public class ScimUserServletDeleteTest {

    private static HttpTester    request  = new HttpTester();
    private static HttpTester    response = new HttpTester();
    private static ServletTester tester   = null;

    private static String        id       = "";

    @BeforeClass
    public static void setUp() throws Exception {
        tester = new ServletTester();
        tester.addServlet(ScimUserServlet.class, "/v1/User/*");
        tester.addServlet(DefaultServlet.class, "/");
        tester.start();

        User scimUser = new User("ABC123-deleteed");
        scimUser.setUserName("AliceDelete");

        request.setMethod("POST");
        request.setVersion("HTTP/1.0");
        request.setHeader("Authorization", "Basic dXNyOnB3");

        request.setURI("/v1/User");
        request.setHeader("Content-Length", Integer.toString(scimUser.getUser("JSON").length()));
        request.setHeader("Content-Type", "application/x-www-form-urlencoded");
        request.setContent(scimUser.getUser("JSON"));
        response.parse(tester.getResponses(request.generate()));

        User tmp = new User(response.getContent(), "JSON");
        id = tmp.getId();
    }

    @Test
    public void deleteUser() throws Exception {

        request.setMethod("GET");
        request.setVersion("HTTP/1.0");

        // get resource to see if it's there
        request.setURI("/v1/User/" + id);
        response.parse(tester.getResponses(request.generate()));

        Assert.assertEquals(200, response.getStatus());

        User scimUser = new User(response.getContent(), "JSON");
        Assert.assertEquals(id, scimUser.getId());

        // delete resource

        request.setMethod("DELETE");
        request.setVersion("HTTP/1.0");
        request.setHeader("ETag", scimUser.getMeta().getVersion());
        response.parse(tester.getResponses(request.generate()));

        Assert.assertTrue(response.getMethod() == null);
        Assert.assertEquals(200, response.getStatus());

        // next request should be 404

        request.setMethod("GET");
        request.setVersion("HTTP/1.0");
        request.removeHeader("ETag");
        response.parse(tester.getResponses(request.generate()));
        Assert.assertTrue(response.getMethod() == null);
        Assert.assertEquals(404, response.getStatus());
    }

    @Test
    public void deleteNonExistingUser() throws Exception {
        request.setMethod("DELETE");
        request.setVersion("HTTP/1.0");

        // get resource to see if it's there
        request.setURI("/v1/User/jsjsjsjsjsjs");
        response.parse(tester.getResponses(request.generate()));

        Assert.assertEquals(404, response.getStatus());
    }

}
