/**
 * 
 */
package com.fb.platform.egv.to;

import java.io.Serializable;

/**
 * @author keith
 *
 */
public class UseResponse implements Serializable{

	private String sessionToken;
	private long gvNumber;
	
	private UseResponseStatusEnum status;

	public String getSessionToken() {
		return sessionToken;
	}

	public void setSessionToken(String sessionToken) {
		this.sessionToken = sessionToken;
	}

	public UseResponseStatusEnum getStatus() {
		return status;
	}

	public void setStatus(UseResponseStatusEnum status) {
		this.status = status;
	}

	public long getGvNumber() {
		return gvNumber;
	}

	public void setGvNumber(long gvNumber) {
		this.gvNumber = gvNumber;
	}
	
}
