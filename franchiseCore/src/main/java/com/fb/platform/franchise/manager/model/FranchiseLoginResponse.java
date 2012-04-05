package com.fb.platform.franchise.manager.model;

import java.io.Serializable;

import com.fb.platform.user.manager.model.auth.LoginResponse;

@SuppressWarnings("serial")
public class FranchiseLoginResponse implements Serializable {

	private Integer userId;
	private FranchiseLoginStatusEnum loginStatus;
	private String sessionToken;

	public FranchiseLoginResponse() {
	}
	
	public FranchiseLoginResponse(LoginResponse loginResponse){
		this.userId = loginResponse.getUserId();
		this.sessionToken = loginResponse.getSessionToken();
	}
	
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public FranchiseLoginStatusEnum getLoginStatus() {
		return loginStatus;
	}
	public void setLoginStatus(FranchiseLoginStatusEnum loginStatus) {
		this.loginStatus = loginStatus;
	}
	public String getSessionToken() {
		return sessionToken;
	}
	public void setSessionToken(String sessionToken) {
		this.sessionToken = sessionToken;
	}
}
