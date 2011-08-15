package info.simplecloud.scimproxy;

import info.simplecloud.core.User;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mortbay.jetty.servlet.DefaultServlet;
import org.mortbay.jetty.testing.HttpTester;
import org.mortbay.jetty.testing.ServletTester;

public class ScimUserServletAuthTest {

    static HttpTester     request  = new HttpTester();
    static HttpTester     response = new HttpTester();
    static ServletTester  tester   = null;

    private static String id       = "";

    @BeforeClass
    public static void setUp() throws Exception {
        tester = new ServletTester();
        tester.addServlet(ScimUserServlet.class, "/User/*");
        tester.addServlet(DefaultServlet.class, "/");
        tester.start();

        User scimUser = new User("ABC123-auth");
        scimUser.setUserName("Alice");
        scimUser.setNickName("A");

        request.setMethod("POST");
        request.setVersion("HTTP/1.0");
        request.setURI("/User");
        request.setHeader("Content-Length", Integer.toString(scimUser.getUser("JSON").length()));
        request.setHeader("Content-Type", "application/x-www-form-urlencoded");
        request.setHeader("Authorization", "Basic dXNyOnB3");
        request.setContent(scimUser.getUser("JSON"));
        response.parse(tester.getResponses(request.generate()));

        User tmp = new User(response.getContent(), "JSON");
        id = tmp.getId();
    }

    @Test
    public void notAuthenticated() throws Exception {
        request.setMethod("GET");
        request.setVersion("HTTP/1.0");
        request.removeHeader("Authorization");

        request.setURI("/User/" + id);

        response.parse(tester.getResponses(request.generate()));

        Assert.assertEquals(401, response.getStatus());
    }

    @Test
    public void malformedAuth1() throws Exception {
        request.setMethod("GET");
        request.setVersion("HTTP/1.0");
        request.removeHeader("Authorization");
        request.setHeader("Authorizationsasdasd", "Basic dXNyOnB3");

        request.setURI("/User/" + id);

        response.parse(tester.getResponses(request.generate()));

        Assert.assertEquals(401, response.getStatus());
    }

    @Test
    public void malformedAuth2() throws Exception {
        request.setMethod("GET");
        request.setVersion("HTTP/1.0");
        request.setHeader("Authorization", "dXNyOnB3");

        request.setURI("/User/" + id);

        response.parse(tester.getResponses(request.generate()));

        Assert.assertEquals(401, response.getStatus());
    }

    @Test
    public void malformedAuth3() throws Exception {
        request.setMethod("GET");
        request.setVersion("HTTP/1.0");
        request.setHeader("Authorization", "Basic asdXNyOnB3");

        request.setURI("/User/" + id);

        response.parse(tester.getResponses(request.generate()));

        Assert.assertEquals(401, response.getStatus());
    }

}
