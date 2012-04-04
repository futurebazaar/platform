/**
 * 
 */
package com.fb.platform.promotion.to;

import java.io.Serializable;

/**
 * @author keith
 *
 */
public class ReleaseCouponResponse implements Serializable{

	private ReleaseCouponStatusEnum releaseCouponStatus = null;
	private String sessionToken = null;
	public ReleaseCouponStatusEnum getReleaseCouponStatus() {
		return releaseCouponStatus;
	}
	public void setReleaseCouponStatus(ReleaseCouponStatusEnum commitCouponStatus) {
		this.releaseCouponStatus = commitCouponStatus;
	}
	public String getSessionToken() {
		return sessionToken;
	}
	public void setSessionToken(String sessionToken) {
		this.sessionToken = sessionToken;
	}

	
}
