/**
 * 
 */
package com.fb.platform.egv.to;

import java.math.BigDecimal;

/**
 * @author keith
 *
 */
public class UseRequest implements GiftVoucherRequest{

	private String sessionToken;
	private long giftVoucherNumber;
	private String giftVoucherPin;
	private BigDecimal amount;
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
	public String getGiftVoucherPin() {
		return giftVoucherPin;
	}
	public void setGiftVoucherPin(String giftVoucherPin) {
		this.giftVoucherPin = giftVoucherPin;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public int getOrderId() {
		return orderId;
	}
	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}
	
	
}
