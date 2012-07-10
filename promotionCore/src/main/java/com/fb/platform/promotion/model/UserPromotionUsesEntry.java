package com.fb.platform.promotion.model;

import java.io.Serializable;

import com.fb.commons.to.Money;

public class UserPromotionUsesEntry implements Serializable {

	private int id;
	private int userId;
	private int promotionId;
	private int orderId;
	private Money discountAmount;
	private int noOfUseInMonth = 0;
	
	
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getPromotionId() {
		return promotionId;
	}
	public void setPromotionId(int promotionId) {
		this.promotionId = promotionId;
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
	public int getNoOfUseInMonth() {
		return noOfUseInMonth;
	}
	public void setNoOfUseInMonth(int noOfUseInMonth) {
		this.noOfUseInMonth = noOfUseInMonth;
	}
}
