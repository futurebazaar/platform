package com.fb.platform.promotion.to;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 
 * @author keith
 *
 */
public class ApplyCouponResponse implements Serializable{

	private String couponCode;
	private BigDecimal discountValue;
	private ApplyCouponResponseStatusEnum couponStatus;
	private String statusMessage;
	private String promoName;
	private String promoDescription;
	private String sessionToken;

	/**
	 * @return the couponCode
	 */
	public String getCouponCode() {
		return couponCode;
	}
	/**
	 * @param couponCode the couponCode to set
	 */
	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}
	/**
	 * @return the discountValue
	 */
	public BigDecimal getDiscountValue() {
		return discountValue;
	}
	/**
	 * @param discountValue the discountValue to set
	 */
	public void setDiscountValue(BigDecimal discountValue) {
		this.discountValue = discountValue;
	}
	/**
	 * @return the statusMessage
	 */
	public String getStatusMessage() {
		return statusMessage;
	}
	public ApplyCouponResponseStatusEnum getCouponStatus() {
		return couponStatus;
	}
	public void setCouponStatus(ApplyCouponResponseStatusEnum couponStatus) {
		this.couponStatus = couponStatus;
		this.statusMessage = couponStatus.getMesage();
	}
	public void setCouponStatus(PromotionStatusEnum status) {
		this.statusMessage = status.getMesage();
		switch (status) {
		case ALREADY_APPLIED_COUPON_ON_ORDER:
			couponStatus = ApplyCouponResponseStatusEnum.ALREADY_APPLIED_COUPON_ON_ORDER;
			break;
		case BRAND_MISMATCH:
			couponStatus = ApplyCouponResponseStatusEnum.BRAND_MISMATCH;
			break;
		case ALREADY_APPLIED_PROMOTION_ON_ORDER:
			couponStatus = ApplyCouponResponseStatusEnum.ALREADY_APPLIED_PROMOTION_ON_ORDER;
			break;
		case NO_APPLIED_COUPON_ON_ORDER:
			couponStatus = ApplyCouponResponseStatusEnum.NO_APPLIED_COUPON_ON_ORDER;
			break;
		case NO_APPLIED_PROMOTION_ON_ORDER:
			couponStatus = ApplyCouponResponseStatusEnum.NO_APPLIED_PROMOTION_ON_ORDER;
			break;
		case CATEGORY_MISMATCH:
			couponStatus = ApplyCouponResponseStatusEnum.CATEGORY_MISMATCH;
			break;
		case COUPON_CODE_EXPIRED:
			couponStatus = ApplyCouponResponseStatusEnum.COUPON_CODE_EXPIRED;
			break;
		case INACTIVE_COUPON:
			couponStatus = ApplyCouponResponseStatusEnum.INACTIVE_COUPON;
			break;
		case INVALID_CLIENT:
			couponStatus = ApplyCouponResponseStatusEnum.INVALID_CLIENT;
			break;
		case INVALID_COUPON_CODE:
			couponStatus = ApplyCouponResponseStatusEnum.INVALID_COUPON_CODE;
			break;
		case LESS_ORDER_AMOUNT:
			couponStatus = ApplyCouponResponseStatusEnum.LESS_ORDER_AMOUNT;
			break;
		case LIMIT_SUCCESS:
			couponStatus = ApplyCouponResponseStatusEnum.LIMIT_SUCCESS;
			break;
		case NOT_APPLICABLE:
			couponStatus = ApplyCouponResponseStatusEnum.NOT_APPLICABLE;
			break;
		case NUMBER_OF_USES_EXCEEDED:
			couponStatus = ApplyCouponResponseStatusEnum.NUMBER_OF_USES_EXCEEDED;
			break;
		case TOTAL_MAX_AMOUNT_EXCEEDED:
			couponStatus = ApplyCouponResponseStatusEnum.TOTAL_MAX_AMOUNT_EXCEEDED;
			break;
		case TOTAL_MAX_AMOUNT_PER_USER_EXCEEDED:
			couponStatus = ApplyCouponResponseStatusEnum.TOTAL_MAX_AMOUNT_PER_USER_EXCEEDED;
			break;
		case TOTAL_MAX_USES_EXCEEDED:
			couponStatus = ApplyCouponResponseStatusEnum.TOTAL_MAX_USES_EXCEEDED;
			break;
		case TOTAL_MAX_USES_PER_USER_EXCEEDED:
			couponStatus = ApplyCouponResponseStatusEnum.TOTAL_MAX_USES_PER_USER_EXCEEDED;
			break;
		case USER_NOT_AUTHORIZED:
			couponStatus = ApplyCouponResponseStatusEnum.USER_NOT_AUTHORIZED;
			break;
		case SUCCESS:
			couponStatus = ApplyCouponResponseStatusEnum.SUCCESS;
			break;
		default:
			couponStatus = ApplyCouponResponseStatusEnum.INTERNAL_ERROR;
		}
	}
	public String getSessionToken() {
		return sessionToken;
	}
	public void setSessionToken(String sessionToken) {
		this.sessionToken = sessionToken;
	}
	public String getPromoName() {
		return promoName;
	}
	public void setPromoName(String promoName) {
		this.promoName = promoName;
	}
	public String getPromoDescription() {
		return promoDescription;
	}
	public void setPromoDescription(String promoDescription) {
		this.promoDescription = promoDescription;
	}
}
