/**
 * 
 */
package com.fb.platform.promotion.to;


/**
 * @author vinayak
 *
 */
public class ApplyScratchCardResponse {
    private ApplyScratchCardStatus applyScratchCardStatus;
    private String couponCode;
    private String sessionToken;

    public ApplyScratchCardStatus getApplyScratchCardStatus() {
		return applyScratchCardStatus;
	}
	public void setApplyScratchCardStatus(
			ApplyScratchCardStatus applyScratchCardStatus) {
		this.applyScratchCardStatus = applyScratchCardStatus;
	}
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
