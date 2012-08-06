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
public class ActivateRequest implements GiftVoucherRequest {

	private String sessionToken;
	private long giftVoucherNumber;
	private DateTime validFrom;
	private DateTime validTill;
	private BigDecimal amount;

	public String getSessionToken() {
		return sessionToken;
	}
	public void setSessionToken(String sessionToken) {
		this.sessionToken = sessionToken;
	}
	public long getGiftVoucherNumber() {
		return giftVoucherNumber;
	}
	public void setGiftVoucherNumber(long giftVoucherNumber) {
		this.giftVoucherNumber = giftVoucherNumber;
	}
	public DateTime getValidTill() {
		return validTill;
	}
	public void setValidTill(DateTime validTill) {
		this.validTill = validTill;
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

}
