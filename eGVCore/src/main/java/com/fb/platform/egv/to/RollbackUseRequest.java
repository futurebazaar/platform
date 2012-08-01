/**
 * 
 */
package com.fb.platform.egv.to;

import java.math.BigDecimal;

/**
 * @author keith
 *
 */
public class RollbackUseRequest implements GiftVoucherRequest{

	private String sessionToken;
	private long giftVoucherNumber;
	private int orderId;
	
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
	public int getOrderId() {
		return orderId;
	}
	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}
	
	
}
