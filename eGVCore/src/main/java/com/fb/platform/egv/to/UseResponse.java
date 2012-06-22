/**
 * 
 */
package com.fb.platform.egv.to;

import java.math.BigDecimal;


/**
 * @author keith
 *
 */
public class UseResponse implements GiftVoucherResponse {

	private String sessionToken;
	private UseResponseStatusEnum responseStatus;
	private long number;
	private BigDecimal amount;

	public String getSessionToken() {
		return sessionToken;
	}

	public void setSessionToken(String sessionToken) {
		this.sessionToken = sessionToken;
	}

	public UseResponseStatusEnum getResponseStatus() {
		return responseStatus;
	}

	public void setResponseStatus(UseResponseStatusEnum status) {
		this.responseStatus = status;
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
