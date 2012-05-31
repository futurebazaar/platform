/**
 * 
 */
package com.fb.platform.egv.to;

/**
 * @author keith
 *
 */
public class CreateGiftVoucherResponse {

	private String sessionToken;
	
	private CreateGiftVoucherResponseStatusEnum status;

	public String getSessionToken() {
		return sessionToken;
	}

	public void setSessionToken(String sessionToken) {
		this.sessionToken = sessionToken;
	}

	public CreateGiftVoucherResponseStatusEnum getStatus() {
		return status;
	}

	public void setStatus(CreateGiftVoucherResponseStatusEnum status) {
		this.status = status;
	}
	
}
