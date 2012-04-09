/**
 * 
 */
package com.fb.platform.promotion.to;

import java.io.Serializable;

/**
 * @author vinayak
 *
 */
public enum CouponResponseStatusEnum implements Serializable {

	SUCCESS("SUCCESS"),
	NO_SESSION("NO_SESSION"),
	INVALID_COUPON_CODE("INVALID_COUPON_CODE"),
	COUPON_CODE_EXPIRED("COUPON_CODE_EXPIRED"),
	USER_NOT_AUTHORIZED("NOT_AUTHORIZED_USER"),
	NUMBER_OF_USES_EXCEEDED("NUMBER_OF_USES_EXCEEDED"),
	NOT_APPLICABLE("NOT_APPLICABLE"),
	LESS_ORDER_AMOUNT("LESS_ORDER_AMOUNT"),
	ALREADY_APPLIED_COUPON_ON_ORDER("ALREADY_APPLIED_COUPON_ON_ORDER"),
	ALREADY_APPLIED_PROMOTION_ON_ORDER("ALREADY_APPLIED_PROMOTION_ON_ORDER"),
	INTERNAL_ERROR("INTERNAL_ERROR");

	private String status = null;
	
	private CouponResponseStatusEnum(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return this.status;
	}
}
