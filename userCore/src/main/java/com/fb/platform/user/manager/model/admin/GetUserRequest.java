package com.fb.platform.user.manager.model.admin;

import java.io.Serializable;

public class GetUserRequest implements Serializable {

	private String sessionToken = null;
	private String key = null; // can be userid,phoneno,emailid of the user
	/**
	 * @return the sessionToken
	 */
	public String getSessionToken() {
		return sessionToken;
	}
	/**
	 * @param sessionToken the sessionToken to set
	 */
	public void setSessionToken(String sessionToken) {
		this.sessionToken = sessionToken;
	}
	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}
	/**
	 * @param key the key to set
	 */
	public void setKey(String key) {
		this.key = key;
	}
}
