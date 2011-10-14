package info.simplecloud.scimproxy.viewer.version2;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

@SuppressWarnings("serial")
public class Configuration extends HttpServlet {

    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        
        String baseUrl = (String) req.getSession().getAttribute("BaseUrl");
        if (baseUrl == null) {
            System.out.println("Error, missing base url");
            return;
        }

        HttpClient client = new HttpClient();
        client.getParams().setAuthenticationPreemptive(false);

        GetMethod method = new GetMethod(baseUrl + "v1/ServiceProviderConfig");
        method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(3, false));
        method.setRequestHeader("Accept", "application/json");
        
        
        int responseCode = client.executeMethod(method);
        if (responseCode == 200) {
            String body = method.getResponseBodyAsString();
            resp.getWriter().print(body);
        } else {
            resp.getWriter().print("Error, server returned " + responseCode);
        }
    }
}
