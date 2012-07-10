package com.fb.platform.payback.to;

import java.io.Serializable;

public class RollbackRequest implements Serializable {
	
	private String sessionToken;
	private long headerId;
	
	public String getSessionToken() {
		return sessionToken;
	}
	public void setSessionToken(String sessionToken) {
		this.sessionToken = sessionToken;
	}
	public long getHeaderId() {
		return headerId;
	}
	public void setHeaderId(long headerId) {
		this.headerId = headerId;
	}

}
