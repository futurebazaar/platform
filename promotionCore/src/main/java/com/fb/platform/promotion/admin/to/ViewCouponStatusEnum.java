/**
 * 
 */
package com.fb.platform.promotion.admin.to;

/**
 * @author ashish
 *
 */
public enum ViewCouponStatusEnum {

	SUCCESS("SUCCESS"),
	NO_SESSION("NO_SESSION"),
	INTERNAL_ERROR("INTERNAL_ERROR"),
	IMPROPER_REQUEST("IMPROPER_REQUEST"),
	INVALID_COUPON("INVALID_COUPON");
	
	private String status = null;

	private ViewCouponStatusEnum(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return this.status;
	}
}
