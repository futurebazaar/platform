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

	private String userName;
	private int overrideCouponUserLimit = 0;
	private String couponCode = null;

	public AssignCouponToUserResponse validate() {
		AssignCouponToUserResponse response = new AssignCouponToUserResponse();
		response.setSessionToken(sessionToken);

		if (StringUtils.isBlank(sessionToken)) {
			response.setStatus(AssignCouponToUserStatusEnum.NO_SESSION);
			return response;
		}

		if (StringUtils.isBlank(userName)) {
			response.setStatus(AssignCouponToUserStatusEnum.INVALID_USER);
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
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
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
