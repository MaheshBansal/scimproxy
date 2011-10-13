package info.simplecloud.scimproxy.viewer.version2;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.lang.StringEscapeUtils;
import org.mortbay.util.UrlEncoded;

@SuppressWarnings("serial")
public class List extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String creds = (String) req.getSession().getAttribute("Creds");
        if (creds == null) {
            resp.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authenticate.");
            return;
        }

        String baseUrl = (String) req.getSession().getAttribute("BaseUrl");
        if (baseUrl == null) {
            System.out.println("Error, missing base url");
            return;
        }

        Map<String, String> indata = Helper.readJsonPostData(req);

        HttpClient client = new HttpClient();
        client.getParams().setAuthenticationPreemptive(false);

        String query = "?";
        if (indata.get("sort") != null) {
            query += "sortBy=" + indata.get("sort") + "&";
            query += "sortOrder=" + indata.get("sortOrder") + "&";
        }
        if (indata.get("filter") != null) {
            query += "filter=" + UrlEncoded.encodeString(indata.get("filter")) + "&";
        }
        if (indata.get("attributes") != null) {
            query += "attributes=" + UrlEncoded.encodeString(indata.get("attributes"));
        }
        if (query.endsWith("&")) {
            query = query.substring(0, query.length() - 1);
        }

        GetMethod method = new GetMethod(baseUrl + "v1/" + indata.get("type") + "s" + query);
        method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(3, false));
        method.setRequestHeader("Accept", indata.get("encoding"));
        method.setRequestHeader("Authorization", creds);

        client.executeMethod(method);
        
        if ("application/json".equals(indata.get("encoding"))) {
            resp.getWriter().print(method.getResponseBodyAsString());
        } else {
            resp.getWriter().print(StringEscapeUtils.escapeHtml(method.getResponseBodyAsString()));
        }
    }
}
