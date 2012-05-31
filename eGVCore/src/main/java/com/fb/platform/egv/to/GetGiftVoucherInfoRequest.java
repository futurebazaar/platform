/**
 * 
 */
package com.fb.platform.egv.to;

import java.math.BigDecimal;

/**
 * @author keith
 *
 */
public class GetGiftVoucherInfoRequest {

	private String sessionToken;
	private long giftVoucherNumber;
	private int giftVoucherPin;
	private int userId;
	
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
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getGiftVoucherPin() {
		return giftVoucherPin;
	}
	public void setGiftVoucherPin(int giftVoucherPin) {
		this.giftVoucherPin = giftVoucherPin;
	}
	
	
}
