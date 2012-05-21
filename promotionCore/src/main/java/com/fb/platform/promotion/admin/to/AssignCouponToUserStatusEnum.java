/**
 * 
 */
package com.fb.platform.promotion.admin.to;

/**
 * @author vinayak
 *
 */
public enum AssignCouponToUserStatusEnum {

	SUCCESS("SUCCESS"),
	INTERNAL_ERROR("INTERNAL_ERROR"),
	NO_SESSION("NO_SESSION"),
	INVALID_USER("INVALID_USER"),
	COUPON_ALREADY_ASSIGNED_TO_USER("COUPON_ALREADY_ASSIGNED_TO_USER"),
	INVALID_COUPON_TYPE("INVALID_COUPON_TYPE"),
	INVALID_COUPON_CODE("INVALID_COUPON_CODE");

	private String status = null;

	private AssignCouponToUserStatusEnum(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return this.status;
	}

}
