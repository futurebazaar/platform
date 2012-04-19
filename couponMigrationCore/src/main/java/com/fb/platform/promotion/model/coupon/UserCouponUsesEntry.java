package com.fb.platform.promotion.model.coupon;

import java.io.Serializable;

import org.joda.time.DateTime;

import com.fb.commons.to.Money;

public class UserCouponUsesEntry implements Serializable {

	private int id;
	private int userId;
	private int couponId;
	private int orderId;
	private Money discountAmount;
	private DateTime createdDate;
	
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getCouponId() {
		return couponId;
	}
	public void setCouponId(int couponId) {
		this.couponId = couponId;
	}
	public int getOrderId() {
		return orderId;
	}
	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}
	public Money getDiscountAmount() {
		return discountAmount;
	}
	public void setDiscountAmount(Money discountAmount) {
		this.discountAmount = discountAmount;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public DateTime getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(DateTime createdDate) {
		this.createdDate = createdDate;
	}
}
