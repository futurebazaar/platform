package com.fb.platform.promotion.to;

import java.io.Serializable;

public class ClearCouponCacheResponse  implements Serializable {
	private String couponCode;
	private String sessionToken;
	private ClearCacheEnum clearCacheEnum = null;
	
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
	public ClearCacheEnum getClearCacheEnum() {
		return clearCacheEnum;
	}
	public void setClearCacheEnum(ClearCacheEnum clearCacheEnum) {
		this.clearCacheEnum = clearCacheEnum;
	}
	
	
}
