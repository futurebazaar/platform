/**
 * 
 */
package com.fb.platform.egv.to;


/**
 * @author keith
 *
 */
public class CancelRequest implements GiftVoucherRequest{

	private String sessionToken;
	private long giftVoucherNumber;
	private int orderItemId;
	
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
	public int getOrderItemId() {
		return orderItemId;
	}
	public void setOrderItemId(int orderItemId) {
		this.orderItemId = orderItemId;
	}
	
	
}
