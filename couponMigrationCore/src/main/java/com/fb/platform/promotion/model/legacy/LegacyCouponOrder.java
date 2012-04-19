package com.fb.platform.promotion.model.legacy;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class LegacyCouponOrder {
	private BigDecimal couponDiscount;
	private int userId;
	private Timestamp confirmingTimeStamp;
	private int orderId;
	
	public BigDecimal getCouponDiscount() {
		return couponDiscount;
	}
	public void setCouponDiscount(BigDecimal couponDiscount) {
		this.couponDiscount = couponDiscount;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public Timestamp getConfirmingTimeStamp() {
		return confirmingTimeStamp;
	}
	public void setConfirmingTimeStamp(Timestamp confirmingTimeStamp) {
		this.confirmingTimeStamp = confirmingTimeStamp;
	}
	public int getOrderId() {
		return orderId;
	}
	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}
	
	
}
