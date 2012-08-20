package com.fb.platform.promotion.to;

import java.io.Serializable;

/**
 * 
 * @author vinayak
 * 
 */
public class ApplicablePaymentRequest implements Serializable {

	private String couponCode;
	private String sessionToken = null;

	public String getCouponCode() {
		return couponCode;
	}

	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}

	public String getSessionToken() {
		return sessionToken;
	}

	public void setSessionToken(String sessionToken) {
		this.sessionToken = sessionToken;
	}
}
