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
	private String statusMessage;

	public String getStatusMessage() {
		return statusMessage;
	}
	
	public CommitCouponStatusEnum getCommitCouponStatus() {
		return commitCouponStatus;
	}
	public void setCommitCouponStatus(CommitCouponStatusEnum commitCouponStatus) {
		this.commitCouponStatus = commitCouponStatus;
		this.statusMessage = commitCouponStatus.getMesage();
	}
	public String getSessionToken() {
		return sessionToken;
	}
	public void setSessionToken(String sessionToken) {
		this.sessionToken = sessionToken;
	}
	
	public void setCommitCouponStatus(PromotionStatusEnum status) {
		this.statusMessage = status.getMesage();
		switch (status) {
		case ALREADY_APPLIED_COUPON_ON_ORDER:
			commitCouponStatus = CommitCouponStatusEnum.ALREADY_APPLIED_COUPON_ON_ORDER;
			break;
		case BRAND_MISMATCH:
			commitCouponStatus = CommitCouponStatusEnum.BRAND_MISMATCH;
			break;
		case ALREADY_APPLIED_PROMOTION_ON_ORDER:
			commitCouponStatus = CommitCouponStatusEnum.ALREADY_APPLIED_PROMOTION_ON_ORDER;
			break;
		case NO_APPLIED_COUPON_ON_ORDER:
			commitCouponStatus = CommitCouponStatusEnum.NO_APPLIED_COUPON_ON_ORDER;
			break;
		case NO_APPLIED_PROMOTION_ON_ORDER:
			commitCouponStatus = CommitCouponStatusEnum.NO_APPLIED_PROMOTION_ON_ORDER;
			break;
		case CATEGORY_MISMATCH:
			commitCouponStatus = CommitCouponStatusEnum.CATEGORY_MISMATCH;
			break;
		case COUPON_CODE_EXPIRED:
			commitCouponStatus = CommitCouponStatusEnum.COUPON_CODE_EXPIRED;
			break;
		case INACTIVE_COUPON:
			commitCouponStatus = CommitCouponStatusEnum.INACTIVE_COUPON;
			break;
		case INVALID_CLIENT:
			commitCouponStatus = CommitCouponStatusEnum.INVALID_CLIENT;
			break;
		case INVALID_COUPON_CODE:
			commitCouponStatus = CommitCouponStatusEnum.INVALID_COUPON_CODE;
			break;
		case LESS_ORDER_AMOUNT:
			commitCouponStatus = CommitCouponStatusEnum.LESS_ORDER_AMOUNT;
			break;
		case LIMIT_SUCCESS:
			commitCouponStatus = CommitCouponStatusEnum.LIMIT_SUCCESS;
			break;
		case NOT_APPLICABLE:
			commitCouponStatus = CommitCouponStatusEnum.NOT_APPLICABLE;
			break;
		case NUMBER_OF_USES_EXCEEDED:
			commitCouponStatus = CommitCouponStatusEnum.NUMBER_OF_USES_EXCEEDED;
			break;
		case TOTAL_MAX_AMOUNT_EXCEEDED:
			commitCouponStatus = CommitCouponStatusEnum.TOTAL_MAX_AMOUNT_EXCEEDED;
			break;
		case TOTAL_MAX_AMOUNT_PER_USER_EXCEEDED:
			commitCouponStatus = CommitCouponStatusEnum.TOTAL_MAX_AMOUNT_PER_USER_EXCEEDED;
			break;
		case TOTAL_MAX_USES_EXCEEDED:
			commitCouponStatus = CommitCouponStatusEnum.TOTAL_MAX_USES_EXCEEDED;
			break;
		case TOTAL_MAX_USES_PER_USER_EXCEEDED:
			commitCouponStatus = CommitCouponStatusEnum.TOTAL_MAX_USES_PER_USER_EXCEEDED;
			break;
		case USER_NOT_AUTHORIZED:
			commitCouponStatus = CommitCouponStatusEnum.USER_NOT_AUTHORIZED;
			break;
		case SUCCESS:
			commitCouponStatus = CommitCouponStatusEnum.SUCCESS;
			break;
		default:
			commitCouponStatus = CommitCouponStatusEnum.INTERNAL_ERROR;
		}
	}
}
