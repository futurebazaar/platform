package com.fb.platform.franchise.manager.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class GetDirectFranchiseRequest implements Serializable{

	private String sessionToken = null;
	
	public String getSessionToken() {
		return sessionToken;
	}

	public void setSessionToken(String sessionToken) {
		this.sessionToken = sessionToken;
	}
}
