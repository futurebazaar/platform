/**
 * 
 */
package com.fb.platform.promotion.admin.to;

import java.io.Serializable;

/**
 * @author vinayak
 *
 */
public class AssignCouponToUserResponse implements Serializable {

	private String sessionToken = null;

	private AssignCouponToUserStatusEnum status = null;

	public String getSessionToken() {
		return sessionToken;
	}

	public void setSessionToken(String sessionToken) {
		this.sessionToken = sessionToken;
	}

	public AssignCouponToUserStatusEnum getStatus() {
		return status;
	}

	public void setStatus(AssignCouponToUserStatusEnum status) {
		this.status = status;
	}
}
