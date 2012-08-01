/**
 * 
 */
package com.fb.platform.egv.to;


/**
 * @author keith
 * 
 */

public class SendPinResponse implements GiftVoucherResponse {

	private String sessionToken;
	private SendPinResponseStatusEnum responseStatus;
	private long number;

	public String getSessionToken() {
		return sessionToken;
	}

	public void setSessionToken(String sessionToken) {
		this.sessionToken = sessionToken;
	}

	public SendPinResponseStatusEnum getResponseStatus() {
		return responseStatus;
	}

	public void setResponseStatus(SendPinResponseStatusEnum responseStatus) {
		this.responseStatus = responseStatus;
	}

	public long getNumber() {
		return number;
	}

	public void setNumber(long number) {
		this.number = number;
	}

}
