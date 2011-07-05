package info.simplecloud.scimproxy.authentication;

import info.simplecloud.scimproxy.config.Config;

import org.apache.xmlbeans.impl.util.Base64;

public class Basic implements IAuth {

	@Override
	public boolean authenticate(String token) {
		
		boolean authSuccess = false;
		try {
			String basicIdentifier = "Basic ";
			String encoded = token.substring(basicIdentifier.length());
	        String decoded = new String(Base64.decode(encoded.getBytes()));
	        
	        String correctPassword = Config.getInstance().getBasicAuthPassword();
	        String correctUser = Config.getInstance().getBasicAuthUsername();
	        
	        String user = decoded.substring(0, decoded.indexOf(":"));
	        String pass = decoded.substring(decoded.indexOf(":")+1);

	        if(correctPassword.equals(pass) && correctUser.equals(user)) {
	        	authSuccess = true;
	        }
		} catch (Exception e) {
			// do nothing
		}

		return authSuccess;
	}

}
