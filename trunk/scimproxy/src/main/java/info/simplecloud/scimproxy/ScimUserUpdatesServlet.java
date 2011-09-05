package info.simplecloud.scimproxy;

import info.simplecloud.core.exceptions.InvalidUser;
import info.simplecloud.core.exceptions.UnknownAttribute;
import info.simplecloud.core.exceptions.UnknownEncoding;
import info.simplecloud.core.types.Meta;
import info.simplecloud.scimproxy.authentication.AuthenticateUser;
import info.simplecloud.scimproxy.exception.PreconditionException;
import info.simplecloud.scimproxy.storage.dummy.UserNotFoundException;
import info.simplecloud.scimproxy.trigger.Trigger;
import info.simplecloud.scimproxy.user.User;
import info.simplecloud.scimproxy.util.Util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * To retrieve a known Resource, clients send GET requests to the Resource end
 * point; e.g., /User/{id}. This servlet is the /User end point.
 */

public class ScimUserUpdatesServlet extends RestServlet {

    private static final long serialVersionUID = -5875059636322733570L;

    private Log               log              = LogFactory.getLog(ScimUserServlet.class);

    protected Trigger trigger = new Trigger();
    
    protected info.simplecloud.core.User internalPost(String query, HttpServletRequest req) throws UnknownEncoding, InvalidUser {
        info.simplecloud.core.User scimUser = new info.simplecloud.core.User(query, HttpGenerator.getEncoding(req));
        AuthenticateUser authUser = (AuthenticateUser)req.getAttribute("AuthUser");
        
        if (scimUser.getMeta() == null) {
        	scimUser.setMeta(new Meta());
        }
        if(scimUser.getMeta().getVersion() == null || "".equals(scimUser.getMeta().getVersion())) {
        	scimUser.getMeta().setVersion(Util.generateVersionString());
        }
        scimUser.getMeta().setLocation(HttpGenerator.getLocation(scimUser, req));

        // add user to set ID
        User.getInstance(authUser.getSessionId()).addUser(scimUser);

        // set location to object
        scimUser.getMeta().setLocation(HttpGenerator.getLocation(scimUser, req));

        // TODO: this is really not a nice way to get the Location into the meta data
        try {
			User.getInstance(authUser.getSessionId()).deletetUser(scimUser.getId());
		} catch (UserNotFoundException e) {
			// do nothing
		}
        User.getInstance(authUser.getSessionId()).addUser(scimUser);

        
        // TODO:   trigger.post(...);				

        return scimUser;
    }

    protected info.simplecloud.core.User internalPut(String userId, String etag, String query, HttpServletRequest req) throws UnknownEncoding, InvalidUser, UserNotFoundException, PreconditionException {

        info.simplecloud.core.User scimUser = new info.simplecloud.core.User(query, HttpGenerator.getEncoding(req));
        AuthenticateUser authUser = (AuthenticateUser)req.getAttribute("AuthUser");
        
        // verify that user have not been changed since latest get and this operation
        info.simplecloud.core.User oldUser = User.getInstance(authUser.getSessionId()).getUser(userId);
        
        // TODO: should return precondition exception if oldUser is not found or don't have a version.
        if(oldUser != null) {
        	if(oldUser.getMeta() != null) {
        		if(!etag.equals(oldUser.getMeta().getVersion())) {
        			throw new PreconditionException();
        		}
        	}
        }

        // set a new version number on the user that we are about to change
        Meta meta = scimUser.getMeta();
        if (meta == null) {
            meta = new Meta();
        }
        meta.setVersion(Util.generateVersionString());
    	meta.setLocation(HttpGenerator.getLocation(scimUser, req));

        scimUser.setMeta(meta);
        
        // delete old user
        User.getInstance(authUser.getSessionId()).deletetUser(userId);

        // add new user
        User.getInstance(authUser.getSessionId()).addUser(scimUser);
        
        // creating user in downstream CSP, any communication errors is handled in triggered and ignored here
        // TODO:   trigger.put(query, userId, etag);				

        return scimUser;
    }

    
    protected info.simplecloud.core.User internalPatch(String userId, String etag, String query, HttpServletRequest req) throws UnknownEncoding, InvalidUser, UserNotFoundException, PreconditionException, UnknownAttribute {

        info.simplecloud.core.User scimUser = new info.simplecloud.core.User(query, HttpGenerator.getEncoding(req));
        AuthenticateUser authUser = (AuthenticateUser)req.getAttribute("AuthUser");
        
        // verify that user have not been changed since latest get and this operation
        info.simplecloud.core.User oldUser = User.getInstance(authUser.getSessionId()).getUser(userId);
        
        // TODO: should return precondition exception if oldUser is not found or don't have a version.
        if(oldUser != null) {
        	if(oldUser.getMeta() != null) {
        		if(!etag.equals(oldUser.getMeta().getVersion())) {
        			throw new PreconditionException();
        		}
        	}
        }

        // patch user
        scimUser.patch(query, HttpGenerator.getEncoding(req));
        // generate new version number
        User.getInstance(authUser.getSessionId()).updateVersionNumber(scimUser);

        
        // set a new version number on the user that we are about to change
        Meta meta = scimUser.getMeta();
        if (meta == null) {
            meta = new Meta();
        }
        meta.setVersion(Util.generateVersionString());
    	meta.setLocation(HttpGenerator.getLocation(scimUser, req));

        scimUser.setMeta(meta);
        
        // delete old user
        User.getInstance(authUser.getSessionId()).deletetUser(userId);

        // add new user
        User.getInstance(authUser.getSessionId()).addUser(scimUser);
        
        // creating user in downstream CSP, any communication errors is handled in triggered and ignored here
        // TODO:   trigger.put(query, userId, etag);				

        return scimUser;
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
     * @throws UserNotFoundException 
     * @throws PreconditionException 
     */
    public void internalDelete(String userId, String etag, HttpServletRequest req) throws UserNotFoundException, PreconditionException {

    	AuthenticateUser authUser = (AuthenticateUser)req.getAttribute("AuthUser");
    	info.simplecloud.core.User scimUser = User.getInstance(authUser.getSessionId()).getUser(userId);
        String version = scimUser.getMeta().getVersion();
        if (etag != null && !"".equals(etag) && etag.equals(version)) {
        	User.getInstance(authUser.getSessionId()).deletetUser(userId);
            // creating user in downstream CSP, any communication errors is handled in triggered and ignored here
        	// TODO: trigger
        	//trigger.delete(scimUser);				
        }
        else {
        	throw new PreconditionException();
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
    protected String getContent(HttpServletRequest req) {
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


}
