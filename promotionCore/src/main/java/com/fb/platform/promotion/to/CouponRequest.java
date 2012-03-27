package com.fb.platform.promotion.to;

import java.io.Serializable;

@SuppressWarnings("serial")
public class CouponRequest implements Serializable,PromotionRequest {
	private String couponCode;
	private OrderRequest orderReq;
	private int userId;
	private int clientId;
	private String sessionToken = null;
	
	public String getCouponCode() {
		return couponCode;
	}
	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}
	public OrderRequest getOrderReq() {
		return orderReq;
	}
	public void setOrderReq(OrderRequest orderReq) {
		this.orderReq = orderReq;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getClientId() {
		return clientId;
	}
	public void setClientId(int clientId) {
		this.clientId = clientId;
	}
	/**
	 * @return the sessionToken
	 */
	public String getSessionToken() {
		return sessionToken;
	}
	/**
	 * @param sessionToken the sessionToken to set
	 */
	public void setSessionToken(String sessionToken) {
		this.sessionToken = sessionToken;
	}
	
}
