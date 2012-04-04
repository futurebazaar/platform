/**
 * 
 */
package com.fb.platform.promotion.to;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author keith
 *
 */
public class ReleaseCouponRequest implements Serializable {
	private String couponCode;
	private int orderId;
	private String sessionToken;
	public String getCouponCode() {
		return couponCode;
	}
	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}
	public int getOrderId() {
		return orderId;
	}
	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}
	public String getSessionToken() {
		return sessionToken;
	}
	public void setSessionToken(String sessionToken) {
		this.sessionToken = sessionToken;
	}
	
}
