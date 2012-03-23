package com.fb.platform.user.manager.model.admin;

import java.io.Serializable;

public class AddUserRequest implements Serializable {
	
	//private String sessionToken = null;
	private String userName = null;
	private String password = null;
	/**
	 * @return the sessionToken
	 */
	/**public String getSessionToken() {
		return sessionToken;
	}**/
	
	/**
	 * @param sessionToken the sessionToken to set
	 */
	
	/**public void setSessionToken(String sessionToken) {
		this.sessionToken = sessionToken;
	}**/
	/**
	 * @return the username
	 */
	public String getUserName() {
		return userName;
	}
	/**
	 * @param username the username to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	
	

}
