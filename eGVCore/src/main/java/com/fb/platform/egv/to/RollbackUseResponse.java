/**
 * 
 */
package com.fb.platform.egv.to;

import java.math.BigDecimal;


/**
 * @author keith
 *
 */
public class RollbackUseResponse implements GiftVoucherResponse {

	private String sessionToken;
	private RollbackUseResponseStatusEnum responseStatus;
	private long number;

	public String getSessionToken() {
		return sessionToken;
	}

	public void setSessionToken(String sessionToken) {
		this.sessionToken = sessionToken;
	}

	public RollbackUseResponseStatusEnum getResponseStatus() {
		return responseStatus;
	}

	public void setResponseStatus(RollbackUseResponseStatusEnum status) {
		this.responseStatus = status;
	}

	public long getNumber() {
		return number;
	}

	public void setNumber(long number) {
		this.number = number;
	}

}
