/**
 * 
 */

package com.fb.platform.egv.model;

import java.io.Serializable;

import com.fb.commons.to.Money;

/**
 * @author keith
 *
 */
public class GiftVoucher implements Serializable {

	private int id;
	private String number;
	private int pin;
//	private boolean isActive;
	private GiftVoucherDates dates;
	private int orderItemId;
	private String email;
	private int userId;
	private GiftVoucherStatusEnum status;
	private Money amount;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public int getPin() {
		return pin;
	}
	public void setPin(int pin) {
		this.pin = pin;
	}
//	public boolean isActive() {
//		return isActive;
//	}
//	public void setActive(boolean isActive) {
//		this.isActive = isActive;
//	}
	public GiftVoucherDates getDates() {
		return dates;
	}
	public void setDates(GiftVoucherDates dates) {
		this.dates = dates;
	}
	public int getOrderItemId() {
		return orderItemId;
	}
	public void setOrderItemId(int orderItemId) {
		this.orderItemId = orderItemId;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public GiftVoucherStatusEnum getStatus() {
		return status;
	}
	public Money getAmount() {
		return amount;
	}
	public void setAmount(Money amount) {
		this.amount = amount;
	}
	public void setStatus(GiftVoucherStatusEnum status) {
		this.status = status;
	}
	
	public boolean isUsable() {
		return (getStatus() == GiftVoucherStatusEnum.CONFIRMED);
	}

}
