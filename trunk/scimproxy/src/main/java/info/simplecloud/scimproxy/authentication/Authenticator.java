package info.simplecloud.scimproxy.authentication;

import info.simplecloud.scimproxy.config.Config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Authenticator {

	private Config config = null;
	private AuthenticateUser authUser = null;
	
	public Authenticator(Config config) {
		this.config = config;
	}

	public boolean authenticate(HttpServletRequest req, HttpServletResponse resp) {

		boolean authStatus = false;
		// check if BASIC authentication is configured
		if(config.isBasicAuth()) {
			String basicAuth = req.getHeader("Authorization");
			if(basicAuth != null && basicAuth.indexOf("Basic ") == 0) {
				Basic basic = new Basic();
				authStatus = basic.authenticate(basicAuth);
				this.authUser = basic.getAuthUser();
			}
		}
		if(config.isNoneAuth()) {
			authStatus = true; // this means public to all and everything!
			
			//Dummy user
			authUser = new AuthenticateUser("usr","pw");
		}

		return authStatus;
	}

	public void setAuthUser(AuthenticateUser cred) {
		this.authUser = cred;
	}

	public AuthenticateUser getAuthUser() {
		return authUser;
	}
}
