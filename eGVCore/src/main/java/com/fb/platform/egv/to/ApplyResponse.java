/**
 * 
 */
package com.fb.platform.egv.to;

import java.math.BigDecimal;

/**
 * @author keith
 *
 */

public class ApplyResponse implements GiftVoucherResponse{

	private String sessionToken;
	private ApplyResponseStatusEnum responseStatus;
	private long number;
	private BigDecimal amount;


	public String getSessionToken() {
		return sessionToken;
	}

	public void setSessionToken(String sessionToken) {
		this.sessionToken = sessionToken;
	}

	public ApplyResponseStatusEnum getResponseStatus() {
		return responseStatus;
	}

	public void setResponseStatus(ApplyResponseStatusEnum responseStatus) {
		this.responseStatus = responseStatus;
	}

	public long getNumber() {
		return number;
	}

	public void setNumber(long number) {
		this.number = number;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	
}
