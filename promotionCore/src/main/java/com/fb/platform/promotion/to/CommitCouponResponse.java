/**
 * 
 */
package com.fb.platform.promotion.to;

import java.io.Serializable;

/**
 * @author vinayak
 *
 */
public class CommitCouponResponse implements Serializable {

	private CommitCouponStatusEnum commitCouponStatus = null;
	private String sessionToken = null;

	public CommitCouponStatusEnum getCommitCouponStatus() {
		return commitCouponStatus;
	}
	public void setCommitCouponStatus(CommitCouponStatusEnum commitCouponStatus) {
		this.commitCouponStatus = commitCouponStatus;
	}
	public String getSessionToken() {
		return sessionToken;
	}
	public void setSessionToken(String sessionToken) {
		this.sessionToken = sessionToken;
	}
}
