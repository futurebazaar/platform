package com.fb.platform.promotion.to;

import java.io.Serializable;

/**
 * 
 * @author vinayak
 *
 */
public class CouponRequest implements Serializable, PromotionRequest {

	private String couponCode;
	private OrderRequest orderReq;
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
	public int getClientId() {
		return clientId;
	}
	public void setClientId(int clientId) {
		this.clientId = clientId;
	}
	public String getSessionToken() {
		return sessionToken;
	}
	public void setSessionToken(String sessionToken) {
		this.sessionToken = sessionToken;
	}
}
