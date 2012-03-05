/**
 * 
 */
package com.fb.platform.user.manager.model.auth;

import java.io.Serializable;


/**
 * @author vinayak
 *
 */
public class LoginResponse implements Serializable {

	private Integer userId;
	private LoginStatusEnum loginStatus;
	private String sessionToken;

	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public LoginStatusEnum getLoginStatus() {
		return loginStatus;
	}
	public void setLoginStatus(LoginStatusEnum loginStatus) {
		this.loginStatus = loginStatus;
	}
	public String getSessionToken() {
		return sessionToken;
	}
	public void setSessionToken(String sessionToken) {
		this.sessionToken = sessionToken;
	}
}
