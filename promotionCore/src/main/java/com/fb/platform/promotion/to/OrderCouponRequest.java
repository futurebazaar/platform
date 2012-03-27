/**
 * 
 */
package com.fb.platform.promotion.to;

import java.io.Serializable;

/**
 * @author vinayak
 *
 */
public class OrderCouponRequest implements PromotionRequest, Serializable {

	private String couponCode = null;
	private OrderRequest orderRequest = null;
	private String sessionToken = null;

	public String getCouponCode() {
		return couponCode;
	}
	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}
	public OrderRequest getOrderRequest() {
		return orderRequest;
	}
	public void setOrderRequest(OrderRequest orderRequest) {
		this.orderRequest = orderRequest;
	}
	public String getSessionToken() {
		return sessionToken;
	}
	public void setSessionToken(String sessionToken) {
		this.sessionToken = sessionToken;
	}
}
