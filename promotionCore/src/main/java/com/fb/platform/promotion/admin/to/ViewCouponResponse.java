/**
 * 
 */
package com.fb.platform.promotion.admin.to;

import java.io.Serializable;

/**
 * @author ashish
 *
 */
public class ViewCouponResponse implements Serializable {

	private String sessionToken;
	private CouponTO couponTO;
	private ViewCouponStatusEnum status;
	private String errorCause;
	
	public String getSessionToken() {
		return sessionToken;
	}
	public void setSessionToken(String sessionToken) {
		this.sessionToken = sessionToken;
	}
	public CouponTO getCouponTO() {
		return couponTO;
	}
	public void setCouponTO(CouponTO couponTO) {
		this.couponTO = couponTO;
	}
	public ViewCouponStatusEnum getStatus() {
		return status;
	}
	public void setStatus(ViewCouponStatusEnum status) {
		this.status = status;
	}
	public String getErrorCause() {
		return errorCause;
	}
	public void setErrorCause(String errorCause) {
		this.errorCause = errorCause;
	}
}
