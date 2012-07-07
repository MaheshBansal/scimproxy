package info.simplecloud.scimproxy.compliance.test;

import info.simplecloud.core.Group;
import info.simplecloud.core.Resource;
import info.simplecloud.core.User;
import info.simplecloud.core.exceptions.InvalidUser;
import info.simplecloud.core.exceptions.UnknownEncoding;
import info.simplecloud.scimproxy.compliance.CSP;
import info.simplecloud.scimproxy.compliance.ComplienceUtils;
import info.simplecloud.scimproxy.compliance.enteties.TestResult;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.io.FileUtils;

public class PostTest extends Test {
	
	public PostTest(CSP csp) {
		super(csp);
	}

	public ArrayList<TestResult> run() {
		ArrayList<TestResult> results = new ArrayList<TestResult>();
		
		// simple user
        User scimUser = new User();
        Group scimGroup = new Group();
        
        // full user
        String fullUser = "";
        User scimUserFull = null;
		try {
			fullUser = FileUtils.readFileToString(new File("src/main/resources/user_full.json"));
	        scimUserFull = new User(fullUser, User.ENCODING_JSON);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// user
        scimUser.setUserName("AliceJson");
        results.add(create("json", scimUser, false));
        scimUser.setUserName("AliceUrlAcceptContentTypeJson");
        results.add(create("json", scimUser, true));
        scimUserFull.setUserName("AliceFullJson");
        results.add(create("json", scimUserFull, false));

		// group
        scimGroup.setDisplayName("ScimGroupJson");
        results.add(create("json", scimGroup, false));

        // run same tests but now with XML
		if(csp.getSpc().hasXmlDataFormat()) {
			// user
	        scimUser.setUserName("AliceXml");
			results.add(create("xml", scimUser, false));
	        scimUser.setUserName("AliceUrlAcceptContentTypeXml");
	        results.add(create("xml", scimUser, true));
	        scimUserFull.setUserName("AliceFullXml");
	        results.add(create("xml", scimUserFull, false));

			// group
	        scimGroup.setDisplayName("ScimGroupXml");
	        results.add(create("xml", scimGroup, false));
		}

		return results;
	}
		
	/**
	 * Creates Resources on the server.
	 * @param csp The server.
	 * @param enc The encoding to use when talking to the server (json or xml)
	 * @param resource The Resource to create.
	 * @return The test Result.
	 */
	@SuppressWarnings("deprecation")
	public TestResult create(String enc, Resource resource, boolean useUrlToSetAcceptContentType) {
        String resourceString = null;
        String endpoint = null;
        PostMethod method = null;
        HttpClient client = null;
        String resourceType = "";
        
        try {
        	if(resource instanceof User) {
        		User tmp = (User)resource;
				resourceString = tmp.getUser(enc);
                endpoint = csp.getUrl() + csp.getVersion() + "/Users";
                resourceType = "user";
        	}
        	else if(resource instanceof Group) {
        		Group tmp = (Group)resource;
				resourceString = tmp.getGroup(enc);
                endpoint = csp.getUrl() + csp.getVersion() + "/Groups";
                resourceType = "group";
        	}

        	// make sure to support the /Users.json and /Users.xml way to define accept content type
            if(useUrlToSetAcceptContentType) {
            	endpoint += "." + enc.toLowerCase();
            }

            method = new PostMethod(endpoint);
            // create client with the correct authn for server
            client = ComplienceUtils.getHttpClientWithAuth(csp, method);
            
            // Create a method instance.
            ComplienceUtils.configureMethod(method);

            if("xml".equalsIgnoreCase(enc)) {
            	if(!useUrlToSetAcceptContentType) {
                    method.setRequestHeader(new Header("Accept", "application/xml"));
            	}
                method.setRequestHeader(new Header("Content-Type", "application/xml"));
            }
            else {
            	if(!useUrlToSetAcceptContentType) {
                    method.setRequestHeader(new Header("Accept", "application/json"));
            	}
                method.setRequestHeader(new Header("Content-Type", "application/json"));
            }
            
            

            method.setRequestBody(resourceString);

        	// Execute the method.
            int statusCode = client.executeMethod(method);

            // Read the response body.
            byte[] responseBody = method.getResponseBody();

            String serverResp = new String(responseBody);

            if(statusCode == 201) {

                Resource cspResource = null;
            	if(resource instanceof User) {
            		cspResource = new User(serverResp, enc);
            	}
            	else if(resource instanceof Group) {
            		cspResource = new Group(new String(responseBody), enc);
            	}
            	
            	if(verify(cspResource)) {
               		return new TestResult(TestResult.SUCCESS, "Create " + resourceType + " in " + enc, "Success", ComplienceUtils.getWire(method, resourceString));
            	}
            	else {
               		return new TestResult(TestResult.ERROR, "Create " + resourceType + " in " + enc, "Missing id or meta.location.", ComplienceUtils.getWire(method, resourceString));
            	}
            }
            else {
           		return new TestResult(TestResult.ERROR, "Create " + resourceType + " in " + enc, "Failed. Server did not respond with 201.", ComplienceUtils.getWire(method, resourceString));
            }

        } catch (HttpException e) {
       		return new TestResult(TestResult.ERROR, "Create " + resourceType + " in " + enc, "Failed. Fatal http protocol violation.", ComplienceUtils.getWire(method, resourceString));
        } catch (IOException e) {
       		return new TestResult(TestResult.ERROR, "Create " + resourceType + " in " + enc, "Failed. Fatal http transport violation.", ComplienceUtils.getWire(method, resourceString));
        } catch (UnknownEncoding e) {
       		return new TestResult(TestResult.ERROR, "Create " + resourceType + " in " + enc, "Failed. Returned unknown encoding.", ComplienceUtils.getWire(method, resourceString));
        } catch (InvalidUser e) {
       		return new TestResult(TestResult.ERROR, "Create " + resourceType + " in " + enc, "Failed. Failed to parse resource.", ComplienceUtils.getWire(method, resourceString));
        } catch (Exception e) {
       		return new TestResult(TestResult.ERROR, "Create " + resourceType + " in " + enc, "Failed. Unknown error.", ComplienceUtils.getWire(method, resourceString));
		} finally {
            // Release the connection.
			if(method != null) {
                method.releaseConnection();
			}
        }
	}

	/**
	 * Verifies that a Resource is created successfully.
	 * @param r The Resource.
	 * @return true if id and location is present, otherwise false.
	 */
	private boolean verify(Resource r) {
		
		if(r.getId() == null || "".equals(r.getId().trim())) {
			return false;
		}
		
		if(r.getMeta().getLocation() == null || "".equals(r.getMeta().getLocation().trim())) {
			return false;
		}
		
		return true;
	}
	
}
