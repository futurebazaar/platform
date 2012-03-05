/**
 * 
 */
package com.fb.platform.user.manager.model.auth;

import java.io.Serializable;

/**
 * @author vinayak
 *
 */
public class LogoutRequest implements Serializable {

	private String sessionToken = null;

	public String getSessionToken() {
		return sessionToken;
	}

	public void setSessionToken(String sessionToken) {
		this.sessionToken = sessionToken;
	}
}
