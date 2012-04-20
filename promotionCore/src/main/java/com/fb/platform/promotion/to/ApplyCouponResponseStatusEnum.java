/**
 * 
 */
package com.fb.platform.promotion.to;

import java.io.Serializable;

/**
 * @author vinayak
 *
 */
public enum ApplyCouponResponseStatusEnum implements Serializable {

	SUCCESS("SUCCESS", "Nice, your coupon is applied. Happy savings!"),
	NO_SESSION("NO_SESSION", "INVALID SESSION, PLEASE APPLY AGAIN OR LOGIN AGAIN AND TRY"),
	INVALID_COUPON_CODE("INVALID_COUPON_CODE", "This is not a valid coupon. Try another one."),
	COUPON_CODE_EXPIRED("COUPON_CODE_EXPIRED", "You are too late in using this coupon. It has already expired."),
	USER_NOT_AUTHORIZED("USER_NOT_AUTHORIZED", "Are you sure this coupon belongs to you? Our system says you cannot use this coupon. "),
	NUMBER_OF_USES_EXCEEDED("NUMBER_OF_USES_EXCEEDED", "NUMBER_OF_USES_EXCEEDED"),
	NOT_APPLICABLE("NOT_APPLICABLE", "Oops, there seems to be a problem with our systems. Can you please try again?"),
	LESS_ORDER_AMOUNT("LESS_ORDER_AMOUNT", "This coupon is not applicable on this Order Amount."),
	ALREADY_APPLIED_COUPON_ON_ORDER("ALREADY_APPLIED_COUPON_ON_ORDER", "COUPON HAS ALREADY BEEN APPLIED ON THE ORDER"),
	ALREADY_APPLIED_PROMOTION_ON_ORDER("ALREADY_APPLIED_PROMOTION_ON_ORDER", "PROMOTION HAS ALREADY BEEN APPLIED ON THE ORDER"),
	NO_APPLIED_COUPON_ON_ORDER("NO_APPLIED_COUPON_ON_ORDER", "NO APPLIED COUPON FOUND ON THE ORDER"),
	NO_APPLIED_PROMOTION_ON_ORDER("NO_APPLIED_PROMOTION_ON_ORDER", "NO APPLIED PROMOTION FOUND ON THE ORDER"),
	INTERNAL_ERROR("INTERNAL_ERROR", "Oops, there seems to be a problem with our systems. Can you please try again?"),
	INVALID_CLIENT("INVALID_CLIENT", "This is not a valid coupon. Try another one."),
	CATEGORY_MISMATCH("CATEGORY_MISMATCH", "This coupon is not valid on purchase of the products you selected."),
	BRAND_MISMATCH("BRAND_MISMATCH", "This coupon is not valid on purchase of the products you selected."),
	INACTIVE_COUPON("INACTIVE_COUPON", "This is not a valid coupon. Try another one."),
	TOTAL_MAX_USES_EXCEEDED("TOTAL_MAX_USES_EXCEEDED", "You are too late in using this coupon. It has already expired."),
	TOTAL_MAX_USES_PER_USER_EXCEEDED("TOTAL_MAX_USES_PER_USER_EXCEEDED", "This coupon has already been used and cannot be applied on this order."),
	TOTAL_MAX_AMOUNT_EXCEEDED("TOTAL_MAX_AMOUNT_EXCEEDED","You are too late in using this coupon. It has already expired."),
	TOTAL_MAX_AMOUNT_PER_USER_EXCEEDED("TOTAL_MAX_AMOUNT_PER_USER_EXCEEDED",  "This coupon has already been used and cannot be applied on this order."),
	LIMIT_SUCCESS("LIMIT_SUCCESS", "LIMIT_SUCCESS"),
	LESS_ORDER_AMOUNT_OF_BRAND_PRODUCTS("LESS_ORDER_AMOUNT_OF_BRAND_PRODUCTS", "This coupon is not applicable on this Order Amount."),
	LESS_ORDER_AMOUNT_OF_CATEGORY_PRODUCTS("LESS_ORDER_AMOUNT_OF_CATEGORY_PRODUCTS", "This coupon is not applicable on this Order Amount.");


	private String status = null;
	private String message = null;
	
	private ApplyCouponResponseStatusEnum(String status, String message) {
		this.status = status;
		this.message = message;
	}

	@Override
	public String toString() {
		return this.status;
	}
	
	public String getMesage() {
		return message;
	}
}
