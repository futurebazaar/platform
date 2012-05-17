/**
 * 
 */
package com.fb.platform.promotion.admin.to;

/**
 * @author vinayak
 *
 */
public enum CreateCouponStatusEnum {

	SUCCESS("SUCCESS"),
	INTERNAL_ERROR("INTERNAL_ERROR"),
	NO_SESSION("NO_SESSION"),
	INVALID_COUNT("INVALID_COUNT"),
	MAX_COUNT_EXCEEDED("MAX_COUNT_EXCEEDED"),
	INVALID_LENGTH("INVALID_LENGTH"),
	INVALID_PROMOTION("INVALID_PROMOTION"),
	CODE_GENERATION_FAILED("CODE_GENERATION_FAILED"),
	INVALID_LIMITS_CONFIG("INVALID_LIMITS_CONFIG");

	private String status = null;

	private CreateCouponStatusEnum(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return this.status;
	}
}
