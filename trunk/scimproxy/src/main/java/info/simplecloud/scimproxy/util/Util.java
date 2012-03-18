package info.simplecloud.scimproxy.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URLDecoder;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

/**
 * Util methods that's used by ScimProxy.
 */
public class Util {

	private static SecureRandom random = new SecureRandom();

	/**
	 * Generates a pseudo random string that can be used as a version string in
	 * a SCIM user.
	 * 
	 * @return A pseudo random string.
	 */
	public static String generateVersionString() {
		return new BigInteger(130, random).toString(32) + Long.toString(System.currentTimeMillis());
	}


    /**
     * Gets the content from a request by looping though all lines.
     * 
     * @param req
     *            The request to parse.
     * @return The content of the request or null if an error occurred while
     *         parsing request.
     */
    public static String getContent(HttpServletRequest req) {
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
     * Gets an user id from a request. /Users/myuserid will return myuserid.
     * 
     * @param query
     *            A URI, for example /Users/myuserid.
     * @return A scim user id.
     */
    public static String getUserIdFromUri(String query) {
        String id = "";
        // TODO: add more validation of input
        String s = "/v1/Users/";
        String pw = "/password";
        String jsonExt = ".json";
        String xmlExt = ".xml";

        if (query != null && query.length() > 0) {

            // remove any extension if any
            int indexOfJsonExt = query.indexOf(jsonExt);
            int indexOfXmlExt = query.indexOf(xmlExt);
            
            if(indexOfJsonExt > -1) {
            	query = query.substring(0, indexOfJsonExt) + query.substring(indexOfJsonExt+jsonExt.length());
            }
            if(indexOfXmlExt > -1) {
            	query = query.substring(0, indexOfXmlExt) + query.substring(indexOfXmlExt+xmlExt.length());
            }

            int indexOfUserId = query.indexOf(s) + s.length();
            int indexOfPw = query.indexOf(pw);
            try {
	            if(indexOfPw > -1) {
	            	id = query.substring(indexOfUserId, indexOfPw);
	            }
	            else {
	            	id = query.substring(indexOfUserId);
	            }
                id = URLDecoder.decode(id, "UTF-8");
            } catch (Exception e) {
                // just return empty id
            }
        }
        return id;
    }

    public static boolean isChangePassword(String query) {
        String pw = "/password";
        boolean isPw = false;
        if (query != null && query.length() > 0) {
        	if(query.endsWith(pw)) {
        		isPw = true;
        	}
        }
        return isPw;
    }

    /**
     * Gets an group id from a request. /Groups/myGroupId will return myGroupId.
     * 
     * @param query
     *            A URI, for example /Groups/myuserid.
     * @return A scim group id.
     */
    public static String getGroupIdFromUri(String query) {
        String id = "";
        // TODO: add more validation of input
        String s = "/v1/Groups/";
        String jsonExt = ".json";
        String xmlExt = ".xml";

        if (query != null && query.length() > 0) {
            // remove any extension if any
            int indexOfJsonExt = query.indexOf(jsonExt);
            int indexOfXmlExt = query.indexOf(xmlExt);
            
            if(indexOfJsonExt > -1) {
            	query = query.substring(0, indexOfJsonExt) + query.substring(indexOfJsonExt+jsonExt.length());
            }
            if(indexOfXmlExt > -1) {
            	query = query.substring(0, indexOfXmlExt) + query.substring(indexOfXmlExt+xmlExt.length());
            }


            int indexOfUserId = query.indexOf(s) + s.length();

            try {
            	id = query.substring(indexOfUserId);
                id = URLDecoder.decode(id, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                // just return empty id
            }
        }
        return id;
    }
    
    public static List<String> getAttributeStringFromRequest(HttpServletRequest req) {
        String attributesString = req.getParameter("attributes") == null ? "" : req.getParameter("attributes");
        List<String> attributesList = new ArrayList<String>();
        if (attributesString != null && !"".equals(attributesString)) {
            for (String attribute : attributesString.split(",")) {
                attributesList.add(attribute.trim());
            }
        }
        return attributesList;
    }
    
    

    private static String repeat(String s, int count) {
    	String str = "";
    	for(int i=0; i<count; i++) {
    		str += s;
    	}
        return str;
    }
    
    // Transformed into java from https://github.com/umbrae/jsonlintdotcom/blob/master/c/js/jsl.format.js
    public static String formatJsonPretty(String json) {

        int i           = 0;
        int il          = 0;
        String tab         = "    ";
        String newJson     = "";
        int indentLevel = 0;
        boolean inString    = false;
        char currentChar;

        for (i = 0, il = json.length(); i < il; i += 1) { 
            currentChar = json.charAt(i);

            switch (currentChar) {
            case '{': 
            case '[': 
                if (!inString) { 
                    newJson += currentChar + "\n" + repeat(tab, indentLevel + 1);
                    indentLevel += 1; 
                } else { 
                    newJson += currentChar; 
                }
                break; 
            case '}': 
            case ']': 
                if (!inString) { 
                    indentLevel -= 1; 
                    newJson += "\n" + repeat(tab, indentLevel) + currentChar; 
                } else { 
                    newJson += currentChar; 
                } 
                break; 
            case ',': 
                if (!inString) { 
                    newJson += ",\n" + repeat(tab, indentLevel); 
                } else { 
                    newJson += currentChar; 
                } 
                break; 
            case ':': 
                if (!inString) { 
                    newJson += ": "; 
                } else { 
                    newJson += currentChar; 
                } 
                break; 
            case ' ':
            case '\n':
            case '\t':
                if (inString) {
                    newJson += currentChar;
                }
                break;
            case '"': 
                if (i > 0 && json.charAt(i - 1) != '\\') {
                    inString = !inString; 
                }
                newJson += currentChar; 
                break;
            default: 
                newJson += currentChar; 
                break;                    
            } 
        } 

        return newJson; 
    }
	
	
}
