/**
 * 
 */
package com.fb.platform.egv.to;

/**
 * @author keith
 * 
 */

public class GetPinResponse implements GiftVoucherResponse {

	private String sessionToken;
	private GetPinResponseStatusEnum responseStatus;
	private long number;
	private String pin;

	public String getSessionToken() {
		return sessionToken;
	}

	public void setSessionToken(String sessionToken) {
		this.sessionToken = sessionToken;
	}

	public GetPinResponseStatusEnum getResponseStatus() {
		return responseStatus;
	}

	public void setResponseStatus(GetPinResponseStatusEnum responseStatus) {
		this.responseStatus = responseStatus;
	}

	public long getNumber() {
		return number;
	}

	public void setNumber(long number) {
		this.number = number;
	}

	public String getPin() {
		return pin;
	}

	public void setPin(String pin) {
		this.pin = pin;
	}

}
