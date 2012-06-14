/**
 * 
 */
package com.fb.platform.egv.to;

import java.io.Serializable;

/**
 * @author keith
 *
 */
public class CancelResponse implements Serializable{

	private String sessionToken;
	private long gvNumber;
	
	private CancelResponseStatusEnum status;

	public String getSessionToken() {
		return sessionToken;
	}

	public void setSessionToken(String sessionToken) {
		this.sessionToken = sessionToken;
	}

	public CancelResponseStatusEnum getStatus() {
		return status;
	}

	public void setStatus(CancelResponseStatusEnum status) {
		this.status = status;
	}

	public long getGvNumber() {
		return gvNumber;
	}

	public void setGvNumber(long gvNumber) {
		this.gvNumber = gvNumber;
	}
	
}
