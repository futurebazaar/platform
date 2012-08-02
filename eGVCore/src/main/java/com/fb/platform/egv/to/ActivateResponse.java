/**
 * 
 */
package com.fb.platform.egv.to;

import java.math.BigDecimal;

import org.joda.time.DateTime;

/**
 * @author keith
 * 
 */

public class ActivateResponse implements GiftVoucherResponse {

	private String sessionToken;
	private ActivateResponseStatusEnum responseStatus;
	private long number;
	private BigDecimal amount;
	private DateTime validFrom;
	private DateTime validTill;

	public String getSessionToken() {
		return sessionToken;
	}

	public void setSessionToken(String sessionToken) {
		this.sessionToken = sessionToken;
	}

	public ActivateResponseStatusEnum getResponseStatus() {
		return responseStatus;
	}

	public void setResponseStatus(ActivateResponseStatusEnum responseStatus) {
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

	public DateTime getValidFrom() {
		return validFrom;
	}

	public void setValidFrom(DateTime validFrom) {
		this.validFrom = validFrom;
	}

	public DateTime getValidTill() {
		return validTill;
	}

	public void setValidTill(DateTime validTill) {
		this.validTill = validTill;
	}

}
