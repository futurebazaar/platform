/**
 * 
 */
package com.fb.platform.egv.to;


/**
 * @author keith
 *
 */
public class ApplyRequest implements GiftVoucherRequest{

	private String sessionToken;
	private long giftVoucherNumber;
	private String giftVoucherPin;
	
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
	public String getGiftVoucherPin() {
		return giftVoucherPin;
	}
	public void setGiftVoucherPin(String giftVoucherPin) {
		this.giftVoucherPin = giftVoucherPin;
	}	
	
}
