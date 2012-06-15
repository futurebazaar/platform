/**
 * 
 */
package com.fb.platform.egv.to;


/**
 * @author keith
 *
 */
public class CancelResponse implements GiftVoucherResponse{

	private String sessionToken;
	private long gvNumber;
	
	private CancelResponseStatusEnum responseStatus;

	public String getSessionToken() {
		return sessionToken;
	}

	public void setSessionToken(String sessionToken) {
		this.sessionToken = sessionToken;
	}

	public CancelResponseStatusEnum getResponseStatus() {
		return responseStatus;
	}

	public void setResponseStatus(CancelResponseStatusEnum status) {
		this.responseStatus = status;
	}

	public long getGvNumber() {
		return gvNumber;
	}

	public void setGvNumber(long gvNumber) {
		this.gvNumber = gvNumber;
	}
	
}
