/**
 * 
 */
package com.fb.platform.user.manager.model.auth;

import java.io.Serializable;

/**
 * @author vinayak
 *
 */
public class ChangePasswordRequest implements Serializable {

	private String sessionToken = null;
	private String oldPassword = null;
	private String newPassword = null;

	public String getSessionToken() {
		return sessionToken;
	}
	public void setSessionToken(String sessionToken) {
		this.sessionToken = sessionToken;
	}
	public String getOldPassword() {
		return oldPassword;
	}
	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}
	public String getNewPassword() {
		return newPassword;
	}
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
}
