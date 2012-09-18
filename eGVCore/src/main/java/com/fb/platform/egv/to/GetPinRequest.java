/**
 * 
 */
package com.fb.platform.egv.to;

/**
 * @author keith
 * 
 */
public class GetPinRequest implements GiftVoucherRequest {

	private String sessionToken;
	private long giftVoucherNumber;

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
}
