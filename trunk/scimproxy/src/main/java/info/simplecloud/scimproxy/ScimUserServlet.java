package info.simplecloud.scimproxy;

import info.simplecloud.core.ScimUser;
import info.simplecloud.core.execeptions.InvalidUser;
import info.simplecloud.core.execeptions.UnknownEncoding;
import info.simplecloud.core.types.Meta;
import info.simplecloud.scimproxy.storage.dummy.UserNotFoundException;
import info.simplecloud.scimproxy.trigger.Trigger;
import info.simplecloud.scimproxy.user.User;
import info.simplecloud.scimproxy.util.Util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * To retrieve a known Resource, clients send GET requests to the Resource end
 * point; e.g., /User/{id}. This servlet is the /User end point.
 */

public class ScimUserServlet extends RestServlet {

    /**
     * Serialize id.
     */
    private static final long serialVersionUID = -5875059636322733570L;

    private Log               log              = LogFactory.getLog(ScimUserServlet.class);

    private Trigger trigger = new Trigger();
    
    /**
     * Returns a scim user.
     * 
     * @param req
     *            Servlet request.
     * @param resp
     *            Servlet response.
     * @throws IOException
     *             Servlet I/O exception.
     */
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String userId = getIdFromUri(req.getRequestURI());

        try {
        	// TODO: should CSP trigger GET call be made before trying locally?

            ScimUser scimUser = User.getUser(userId);
            String userStr = null;

            if (req.getParameter("attributes") != null) {
                userStr = scimUser.getUser(HttpGenerator.getEncoding(req), getAttributeStringFromRequest(req));
            } else {
                userStr = scimUser.getUser(HttpGenerator.getEncoding(req));
            }

            if (userStr == null) {
                HttpGenerator.badRequest(resp);
            } else {
                resp.setContentType(HttpGenerator.getContentType(req));
                resp.setHeader("ETag", scimUser.getMeta().getVersion());

                HttpGenerator.ok(resp, userStr);
                log.info("Returning user " + scimUser.getId());
            }

        } catch (UnknownEncoding e) {
            HttpGenerator.serverError(resp);
        } catch (UserNotFoundException e) {
            HttpGenerator.notFound(resp);
        }

    }

    /**
     * Create a new scim user.
     * 
     * @param req
     *            Servlet request.
     * @param resp
     *            Servlet response.
     * @throws IOException
     *             Servlet I/O exception.
     */
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        String query = getContent(req);
        // TODO: SPEC: REST: Should the post message be base64 encoded in spec
        // or not?

        if (query != null && !"".equals(query)) {
            try {
                ScimUser scimUser = new ScimUser(query, HttpGenerator.getEncoding(req));
                Meta meta = scimUser.getMeta();
                if (meta == null) {
                    meta = new Meta();
                }
                if(meta.getVersion() == null || "".equals(meta.getVersion())) {
                    meta.setVersion(Util.generateVersionString());
                }
                scimUser.setMeta(meta);

                User.addUser(scimUser);

                resp.setContentType(HttpGenerator.getContentType(req));
                resp.setHeader("Location", HttpGenerator.getLocation(scimUser, req));
                resp.setHeader("ETag", scimUser.getMeta().getVersion());

                log.info("Creating user " + scimUser.getId());
                
                // creating user in downstream CSP, any communication errors is handled in triggered and ignored here
                trigger.create(scimUser);				

                HttpGenerator.created(resp, scimUser.getUser(HttpGenerator.getEncoding(req)));
            } catch (UnknownEncoding e) {
                HttpGenerator.serverError(resp, "Unknown encoding.");
            } catch (InvalidUser e) {
                HttpGenerator.badRequest(resp, "Malformed user.");
            }
        } else {
            HttpGenerator.badRequest(resp);
        }

    }

    /**
     * PUT performs a full update.
     * 
     * @param req
     *            Servlet request.
     * @param resp
     *            Servlet response.
     * @throws IOException
     *             Servlet I/O exception.
     */
    public void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        String userId = getIdFromUri(req.getRequestURI());
        String query = getContent(req);

        if (query != null && !"".equals(query) && userId != null) {

            try {

                // TODO: SPEC: REST: Should the post message be base64 encoded
                // in spec or not?
                // TODO: SPEC: REST: Should ETag be verified in PUT?
                // TODO: SPEC: REST: Should we keep created time?
                ScimUser scimUser = new ScimUser(query, HttpGenerator.getEncoding(req));
                Meta meta = scimUser.getMeta();
                if (meta == null) {
                    meta = new Meta();
                }
                meta.setVersion(Util.generateVersionString());
                scimUser.setMeta(meta);

                // delete old user
                User.deletetUser(userId);

                // add new user
                User.addUser(scimUser);

                resp.setHeader("Location", HttpGenerator.getLocation(scimUser, req));
                resp.setContentType(HttpGenerator.getContentType(req));
                resp.setHeader("ETag", scimUser.getMeta().getVersion());
                HttpGenerator.ok(resp, scimUser.getUser(HttpGenerator.getEncoding(req)));

                log.info("Replacing user " + scimUser.getId());
            } catch (UserNotFoundException e) {
                HttpGenerator.notFound(resp);
            } catch (UnknownEncoding e) {
                HttpGenerator.badRequest(resp, "Unknown encoding.");
            } catch (InvalidUser e) {
                HttpGenerator.badRequest(resp, "Invalid user.");
            }

        } else {
            HttpGenerator.badRequest(resp, "Invalid user or user id.");
        }

    }

    /**
     * Delete a scim user.
     * 
     * @param req
     *            Servlet request.
     * @param resp
     *            Servlet response.
     * @throws IOException
     *             Servlet I/O exception.
     */
    public void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String userId = getIdFromUri(req.getRequestURI());
        log.trace("Trying to deleting user " + userId + ".");

        if (userId != null) {
            try {
                ScimUser scimUser = User.getUser(userId);
                String etag = req.getHeader("ETag");
                String version = scimUser.getMeta().getVersion();
                if (etag != null && !"".equals(etag) && etag.equals(version)) {
                    User.deletetUser(userId);
                    
                    // creating user in downstream CSP, any communication errors is handled in triggered and ignored here
                    trigger.delete(scimUser);				
                    
                    HttpGenerator.ok(resp);
                    log.info("Deleating user " + userId + ".");
                } else {
                    HttpGenerator.preconditionFailed(resp, scimUser);
                }

            } catch (UserNotFoundException e) {
                HttpGenerator.notFound(resp);
                log.trace("User " + userId + " is not found.");
            }
        } else {
            log.trace("Trying to delete a user that can't be found in storage with user id " + userId + ".");
            HttpGenerator.badRequest(resp, "Missing or malformed user id.");
        }
    }

    /**
     * Change or remove attribute on a scim user.
     * 
     * @param req
     *            Servlet request.
     * @param resp
     *            Servlet response.
     * @throws IOException
     *             Servlet I/O exception.
     */
    public void doPatch(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        String query = getContent(req);
        String userId = getIdFromUri(req.getRequestURI());
        String etag = req.getHeader("ETag");

        if (!"".equals(query) && userId != null && etag != null && !"".equals(etag)) {

            // TODO: SPEC: REST: Should the post message be base64 encoded in
            // spec or not?

            // TODO: SPEC: Add support for the /User/{id}/password function.

            try {
                ScimUser scimUser = User.getUser(userId);
                // check that version haven't changed since loaded from server
                String version = scimUser.getMeta().getVersion();
                if (etag.equals(version)) {
                    // patch user
                    scimUser.patch(query, HttpGenerator.getEncoding(req));
                    // generate new version number
                    User.updateVersionNumber(scimUser);

                    // creating user in downstream CSP, any communication errors is handled in triggered and ignored here
                    trigger.patch(query, userId, etag);				

                    resp.getWriter().print(scimUser.getUser(HttpGenerator.getEncoding(req)));
                    resp.setStatus(HttpServletResponse.SC_OK); // 200
                    log.info("Patching user " + scimUser.getId());
                } else {
                    HttpGenerator.preconditionFailed(resp, scimUser);
                }

            } catch (UserNotFoundException e) {
                HttpGenerator.notFound(resp);
            } catch (UnknownEncoding e) {
                HttpGenerator.badRequest(resp, "Unknown encoding.");
            } catch (InvalidUser e) {
                HttpGenerator.badRequest(resp, "Malformed user.");
            }

        } else {
            HttpGenerator.badRequest(resp, "Missing user id or ETag");
        }

    }

    /**
     * Gets the content from a request by looping though all lines.
     * 
     * @param req
     *            The request to parse.
     * @return The content of the request or null if an error occurred while
     *         parsing request.
     */
    private String getContent(HttpServletRequest req) {
        String query = "";
        BufferedReader reader;
        try {
            reader = new BufferedReader(new InputStreamReader(req.getInputStream()));
            String message = null;
            while ((message = reader.readLine()) != null) {
                query += message;
            }
        } catch (IOException e) {
            query = null;
        }
        return query;
    }

    /**
     * Gets an user id from a request. /User/myuserid will return myuserid.
     * 
     * @param query
     *            A URI, for example /User/myuserid.
     * @return A scim user id.
     */
    public static String getIdFromUri(String query) {
        String id = "";
        // TODO: add more validation of input
        String s = "/User/";
        if (query != null && query.length() > 0) {
            int indexOfUserId = query.indexOf(s) + s.length();

            id = query.substring(indexOfUserId);
            try {
                id = URLDecoder.decode(id, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                // just return empty id
            }
        }
        return id;
    }

    private List<String> getAttributeStringFromRequest(HttpServletRequest req) {
        String attributesString = req.getParameter("attributes") == null ? "" : req.getParameter("attributes");
        List<String> attributesList = new ArrayList<String>();
        if (attributesString != null && !"".equals(attributesString)) {
            for (String attribute : attributesString.split(",")) {
                attributesList.add(attribute.trim());
            }
        }
        return attributesList;
    }

}
