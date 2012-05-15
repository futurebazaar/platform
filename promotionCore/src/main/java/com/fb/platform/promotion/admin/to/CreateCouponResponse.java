/**
 * 
 */
package com.fb.platform.promotion.admin.to;

import java.io.Serializable;

/**
 * @author vinayak
 *
 */
public class CreateCouponResponse implements Serializable {

	private CreateCouponStatusEnum status = null;
	private int numberOfCouponsCreated = 0;
	private String coupons = null;
	private String sessionToken = null;
	private String commaSeparatedCouponCodes;

	public String getSessionToken() {
		return sessionToken;
	}
	public void setSessionToken(String sessionToken) {
		this.sessionToken = sessionToken;
	}
	public CreateCouponStatusEnum getStatus() {
		return status;
	}
	public void setStatus(CreateCouponStatusEnum status) {
		this.status = status;
	}
	public int getNumberOfCouponsCreated() {
		return numberOfCouponsCreated;
	}
	public void setNumberOfCouponsCreated(int numberOfCouponsCreated) {
		this.numberOfCouponsCreated = numberOfCouponsCreated;
	}
	public String getCoupons() {
		return coupons;
	}
	public void setCoupons(String coupons) {
		this.coupons = coupons;
	}
	public String getCommaSeparatedCouponCodes() {
		return commaSeparatedCouponCodes;
	}
	public void setCommaSeparatedCouponCodes(String commaSeparatedCouponCodes) {
		this.commaSeparatedCouponCodes = commaSeparatedCouponCodes;
	}

}
