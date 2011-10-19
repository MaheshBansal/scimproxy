package info.simplecloud.scimproxy;

import info.simplecloud.core.Group;
import info.simplecloud.core.coding.encode.JsonEncoder;
import info.simplecloud.core.coding.encode.XmlEncoder;
import info.simplecloud.scimproxy.authentication.AuthenticateUser;
import info.simplecloud.scimproxy.storage.dummy.DummyStorage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ScimGroupsServlet extends RestServlet {


	private static final long serialVersionUID = 5979024190676564472L;

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        String attributesString = req.getParameter("attributes") == null ? "" : req.getParameter("attributes");
        AuthenticateUser authUser = (AuthenticateUser) req.getAttribute("AuthUser");
        List<String> attributesList = new ArrayList<String>();
        if (attributesString != null && !"".equals(attributesString)) {
            for (String attribute : attributesString.split(",")) {
                attributesList.add(attribute.trim());
            }
        }

        // TODO: SPEC: REST: what is major
        String sortBy = req.getParameter("sortBy") == null ? "displayName" : req.getParameter("sortBy");
        String sortOrder = req.getParameter("sortOrder") == null ? "ascending" : req.getParameter("sortOrder");
        if (!sortOrder.equalsIgnoreCase("ascending") && !sortOrder.equalsIgnoreCase("descending")) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().print("Sort order must be 'ascending' or 'descending'");
            return;
        }

        DummyStorage storage = DummyStorage.getInstance(authUser.getSessionId());
        @SuppressWarnings("rawtypes")
		List groups = null;

        String filter = req.getParameter("filter");
        
        if (filter != null && !"".equals(filter)) {
        	groups = storage.getList(sortBy, sortOrder, filter);
        } else {
        	groups = storage.getGroupList(sortBy, sortOrder);
        }

        int index = 0;
        int count = 0;

        String startIndexStr = req.getParameter("startIndex"); // must be
                                                               // absolut and
                                                               // defaults to 0
        String countStr = req.getParameter("count"); // must be absolut and
                                                     // defaults to 0
        if (startIndexStr != null && !"".equals(startIndexStr)) {
            index = Integer.parseInt(startIndexStr);
        }
        if (countStr != null && !"".equals(countStr)) {
            count = Integer.parseInt(countStr);
        }

        int max = index + count;
        if (max > groups.size() || max == 0) {
            max = groups.size();
        }

        if (index > groups.size()) {
            index = groups.size();
        }

        try {
        	groups = groups.subList(index, max);
        } catch (IndexOutOfBoundsException e) {
        	groups = new ArrayList<Group>();
        }

        resp.setStatus(HttpServletResponse.SC_OK);
        resp.setContentType(HttpGenerator.getContentType(req));

        String response = "";
        if (Group.ENCODING_JSON.equalsIgnoreCase(HttpGenerator.getEncoding(req))) {
            response = new JsonEncoder().encode(groups, attributesList);
        }
        if (Group.ENCODING_XML.equalsIgnoreCase(HttpGenerator.getEncoding(req))) {
            response = new XmlEncoder().encode(groups, attributesList);
        }

        resp.getWriter().print(response);
	}

}
