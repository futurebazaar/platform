/**
 * 
 */
package com.fb.platform.promotion.admin.to;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

/**
 * @author vinayak
 *
 */
public class AssignCouponToUserRequest implements Serializable {

	private String sessionToken = null;

	private int userId = 0;
	private int overrideCouponUserLimit = 0;
	private String couponCode = null;

	public AssignCouponToUserResponse validate() {
		AssignCouponToUserResponse response = new AssignCouponToUserResponse();

		if (StringUtils.isBlank(sessionToken)) {
			response.setStatus(AssignCouponToUserStatusEnum.NO_SESSION);
			return response;
		}

		if (userId == 0) {
			response.setStatus(AssignCouponToUserStatusEnum.INVALID_USER_ID);
			return response;
		}

		if (StringUtils.isBlank(couponCode)) {
			response.setStatus(AssignCouponToUserStatusEnum.INVALID_COUPON_CODE);
			return response;
		}

		return null;
	}

	public String getSessionToken() {
		return sessionToken;
	}
	public void setSessionToken(String sessionToken) {
		this.sessionToken = sessionToken;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getCouponCode() {
		return couponCode;
	}
	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}
	public int getOverrideCouponUserLimit() {
		return overrideCouponUserLimit;
	}
	public void setOverrideCouponUserLimit(int overrideCouponUserLimit) {
		this.overrideCouponUserLimit = overrideCouponUserLimit;
	}
}
