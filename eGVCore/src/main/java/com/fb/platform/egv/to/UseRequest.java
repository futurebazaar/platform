/**
 * 
 */
package com.fb.platform.egv.to;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author keith
 *
 */
public class UseRequest implements Serializable{

	private String sessionToken;
	private long giftVoucherNumber;
	private int giftVoucherPin;
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
	public int getGiftVoucherPin() {
		return giftVoucherPin;
	}
	public void setGiftVoucherPin(int giftVoucherPin) {
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
