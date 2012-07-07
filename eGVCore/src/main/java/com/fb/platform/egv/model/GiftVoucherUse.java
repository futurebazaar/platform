/**
 * 
 */
package com.fb.platform.egv.model;

import java.io.Serializable;

import org.joda.time.DateTime;

import com.fb.commons.to.Money;

/**
 * This class holds Gift Voucher Usage information
 *  
 * @author keith
 *
 */
public class GiftVoucherUse implements Serializable{

	private int id;
	private int usedBy;
	private Money amountRedeemed;
	private String giftVoucherNumber;
	private DateTime usedOn;
	private int orderId;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getUsedBy() {
		return usedBy;
	}
	public void setUsedBy(int usedBy) {
		this.usedBy = usedBy;
	}
	public Money getAmountRedeemed() {
		return amountRedeemed;
	}
	public void setAmountRedeemed(Money amountRedeemed) {
		this.amountRedeemed = amountRedeemed;
	}
	public DateTime getUsedOn() {
		return usedOn;
	}
	public void setUsedOn(DateTime usedOn) {
		this.usedOn = usedOn;
	}
	public int getOrderId() {
		return orderId;
	}
	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}
	public String getGiftVoucherNumber() {
		return giftVoucherNumber;
	}
	public void setGiftVoucherNumber(String giftVoucherNumber) {
		this.giftVoucherNumber = giftVoucherNumber;
	}
	
}
