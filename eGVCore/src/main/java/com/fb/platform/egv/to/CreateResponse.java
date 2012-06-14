/**
 * 
 */
package com.fb.platform.egv.to;

import java.io.Serializable;

/**
 * @author keith
 *
 */
public class CreateResponse implements Serializable{

	private String sessionToken;
	private long gvNumber;
	
	private CreateResponseStatusEnum status;

	public String getSessionToken() {
		return sessionToken;
	}

	public void setSessionToken(String sessionToken) {
		this.sessionToken = sessionToken;
	}

	public CreateResponseStatusEnum getStatus() {
		return status;
	}

	public void setStatus(CreateResponseStatusEnum status) {
		this.status = status;
	}

	public long getGvNumber() {
		return gvNumber;
	}

	public void setGvNumber(long gvNumber) {
		this.gvNumber = gvNumber;
	}
	
}