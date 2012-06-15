/**
 * 
 */
package com.fb.platform.egv.to;


/**
 * @author keith
 *
 */
public class CreateResponse implements GiftVoucherResponse{

	private String sessionToken;
	private long gvNumber;
	
	private CreateResponseStatusEnum responseStatus;

	public String getSessionToken() {
		return sessionToken;
	}

	public void setSessionToken(String sessionToken) {
		this.sessionToken = sessionToken;
	}

	public CreateResponseStatusEnum getResponseStatus() {
		return responseStatus;
	}

	public void setResponseStatus(CreateResponseStatusEnum status) {
		this.responseStatus = status;
	}

	public long getGvNumber() {
		return gvNumber;
	}

	public void setGvNumber(long gvNumber) {
		this.gvNumber = gvNumber;
	}
	
}
