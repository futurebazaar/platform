/**
 * 
 */
package com.fb.platform.user.manager.model.auth;

import java.io.Serializable;

/**
 * @author vinayak
 *
 */
public class ChangePasswordResponse implements Serializable {

	private ChangePasswordStatusEnum status = null;
	private String sessionToken = null;

	public ChangePasswordStatusEnum getStatus() {
		return status;
	}
	public void setStatus(ChangePasswordStatusEnum status) {
		this.status = status;
	}
	public String getSessionToken() {
		return sessionToken;
	}
	public void setSessionToken(String sessionToken) {
		this.sessionToken = sessionToken;
	}

}
