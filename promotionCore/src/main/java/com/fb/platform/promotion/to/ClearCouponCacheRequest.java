package com.fb.platform.promotion.to;

import java.io.Serializable;


public class ClearCouponCacheRequest  implements Serializable {
	private String couponCode;
	private String sessionToken;
	
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
