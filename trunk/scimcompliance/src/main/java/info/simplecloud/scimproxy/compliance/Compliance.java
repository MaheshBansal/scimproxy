package info.simplecloud.scimproxy.compliance;

import info.simplecloud.scimproxy.compliance.enteties.Result;
import info.simplecloud.scimproxy.compliance.enteties.Statistics;
import info.simplecloud.scimproxy.compliance.enteties.TestResult;
import info.simplecloud.scimproxy.compliance.exception.CritialComplienceException;
import info.simplecloud.scimproxy.compliance.test.ConfigTest;
import info.simplecloud.scimproxy.compliance.test.PostTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.commons.validator.routines.UrlValidator;

@Path("/test")
public class Compliance extends HttpServlet {

    private static final long serialVersionUID = 1L;

    // TODO: l18n and remove hardcoded text strings in code

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Result runTests(@FormParam("url") String url, @FormParam("username") String username,
            @FormParam("password") String password, @FormParam("clientId") String clientId, @FormParam("clientSecret") String clientSecret,
            @FormParam("authorizationServer") String authorizationServer, @FormParam("authMethod") String authMethod) throws InterruptedException {
        Thread.sleep(2000);
        List<TestResult> testResults = new ArrayList<TestResult>();
        testResults.add(new TestResult(1, "Test Number One", "bla bla bla", "Wire stuff sadfaslädk..."));
        testResults.add(new TestResult(2, "Test Number Two", "bla bla bla", "Wire stuff sadfaslädk..."));
        testResults.add(new TestResult(3, "Test Number Three", "bla bla bla", "Wire stuff sadfaslädk..."));
        testResults.add(new TestResult(4, "Test Number four", "bla bla bla", "Wire stuff sadfaslädk..."));
        return new Result(new Statistics(34, 3), testResults, "basicAuth".equalsIgnoreCase(authMethod), "Basic");
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        String host = req.getParameter("url");

        // TODO: remove when done coding!
        if (host == null || "".equals(host)) {
            host = "http://127.0.0.1:8080";
        }

        String[] schemes = { "http", "https" };
        UrlValidator urlValidator = new UrlValidator(schemes, UrlValidator.ALLOW_LOCAL_URLS);
        if (!urlValidator.isValid(host)) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().println("Bad URL!");
            return;
        }

        // create a CSP to use to connect to the server
        CSP csp = new CSP();
        csp.setUrl(host);
        csp.setVersion("/v1");

        // if basic
        csp.setAuthentication("basic");
        csp.setUsername("usr");
        csp.setPassword("pw");

        /*
         * // if oauth2, grant_type client_credentials
         * csp.setAuthentication("oauth2"); csp.setUsername("usr");
         * csp.setPassword("pw");
         * csp.setOAuth2AuthorizationServer("oAuthAuthorizationServer");
         * 
         * // if oauth2, other grant_type csp.setAuthentication("oauth2-v10");
         * csp.setUsername("usr"); csp.setPassword("pw");
         * csp.setOAuth2AuthorizationServer("oAuthAuthorizationServer");
         * csp.setoAuth2GrantType("grantType");
         * csp.setoAuth2ClientId("clientId");
         * csp.setoAuth2ClientSecret("clientSecret");
         */

        ArrayList<TestResult> results = new ArrayList<TestResult>();
        String json = "{\"results\":[\n";

        // get the configuration
        try {

            // start with the critical tests (will throw exception and test will
            // stop if fails)
            ConfigTest configTest = new ConfigTest();
            results.add(configTest.getConfiguration(csp));
            results.add(configTest.getSchema("Users", csp));
            results.add(configTest.getSchema("Groups", csp));

            // TODO: add the required attributes in userSchema and groupSchema
            // that server wanted

            // start mandatory test suite (JSON)
            results.addAll(new PostTest(csp).run());

            int nbrSuccess = 0;
            int nbrFailed = 0;

            for (TestResult result : results) {
                if (result.getStatus() == TestResult.SUCCESS) {
                    nbrSuccess++;
                } else {
                    nbrFailed++;
                }
                json += result.toJson() + ",";
            }

            // remove the last , sign
            if (json.charAt(json.length() - 1) == ',') {
                json = json.substring(0, json.length() - 1);
            }

            json += "], \"stats\":{\"failed\":" + nbrFailed + ",\"success\":" + nbrSuccess + "}}";
        } catch (Exception e) {
            if (e instanceof CritialComplienceException) {
                json += ((CritialComplienceException) e).getResult().toJson();
                // TODO: if config succeeds and schema fails we will have more
                // then 1 test!
                json += "], \"stats\":{\"failed\":1,\"success\":0}}";
            } else {
                resp.sendError(500, "Internal server error.");
            }
        }

        resp.setContentType("application/json;charset=UTF-8");

        // System.out.println(json);
        resp.getWriter().print(json);

        // required features in JSON
        // define test suite

        // create group

        // create simple user
        // create full user

        // update user
        // replace group
        // replace simple user
        // replace full user

        // delete users
        // delete group

        // json (both accept and .json)
        // xml (both accept and .json)

        // bulk 10 users

        // patch simple attribute (both using PATCH and POST with x-headers)
        // patch complex attribute
        // patch multiValued simple
        // patch multiValued complex
        // add user to group

        // get user
        // get partial user
        // partial complex
        // partial multiValued

        // list users
        // sort
        // ascending
        // descending
        // filter
        // all different filter types
        // nested filters
        // multiValued
        // pagination
        // combine (sort, filter attributes)

    }

}
