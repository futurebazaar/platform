package com.fb.platform.promotion.admin.to;

import com.fb.commons.to.Money;

public class GetPromotionUsageResponse {
	private String sessionToken = null;
	private GetPromotionUsageEnum status = null;
	private int totalOrders = 0;
	private Money discount = null;
	
	public String getSessionToken() {
		return sessionToken;
	}
	
	public int gettotalOrders() {
		return totalOrders;
	}
	
	public void settotalOrders(int totalOrders) {
		this.totalOrders = totalOrders;
	}
	
	public Money getdiscount() {
		return discount;
	}
	
	public void setdiscount(Money discount) {
		this.discount = discount;
	}
	
	public void setSessionToken(String sessionToken) {
		this.sessionToken = sessionToken;
	}

	public GetPromotionUsageEnum getStatus() {
		return status;
	}
	
	public void setStatus(GetPromotionUsageEnum status) {
		this.status = status;
	}
	
}
