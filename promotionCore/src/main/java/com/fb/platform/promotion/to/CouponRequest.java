package com.fb.platform.promotion.to;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 
 * @author vinayak
 *
 */
public class CouponRequest implements Serializable {

	private String couponCode;
	private OrderRequest orderReq;
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
	public String getSessionToken() {
		return sessionToken;
	}
	public void setSessionToken(String sessionToken) {
		this.sessionToken = sessionToken;
	}
	public BigDecimal getOrderValue()
	{
		return orderReq.getOrderValue();
	}
}
