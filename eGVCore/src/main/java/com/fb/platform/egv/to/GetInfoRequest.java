/**
 * 
 */
package com.fb.platform.egv.to;

import java.math.BigDecimal;

/**
 * @author keith
 *
 */
public class GetInfoRequest {

	private String sessionToken;
	private long giftVoucherNumber;
	private int giftVoucherPin;
	
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
	public int getGiftVoucherPin() {
		return giftVoucherPin;
	}
	public void setGiftVoucherPin(int giftVoucherPin) {
		this.giftVoucherPin = giftVoucherPin;
	}
	
	
}
