package com.fb.platform.promotion.model;

public class CouponUses {
	
	private String couponCode;
	private int promoUserId;
	private int timesUsed;
	public String getCouponCode() {
		return couponCode;
	}
	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}
	public int getPromoUserId() {
		return promoUserId;
	}
	public void setPromoUserId(int promoUserId) {
		this.promoUserId = promoUserId;
	}
	public int getTimesUsed() {
		return timesUsed;
	}
	public void setTimesUsed(int timesUsed) {
		this.timesUsed = timesUsed;
	}
	
	
	
}
