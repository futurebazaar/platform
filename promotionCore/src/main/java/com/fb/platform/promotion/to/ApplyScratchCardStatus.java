/**
 * 
 */
package com.fb.platform.promotion.to;

/**
 * @author vinayak
 *
 */
public enum ApplyScratchCardStatus {
    SUCCESS("SUCCESS"),
    INVALID_SCRATCH_CARD("INVALID_SCRATCH_CARD"),
    COUPON_ALREADY_ASSIGNED_TO_USER("COUPON_ALREADY_ASSIGNED_TO_USER"),
    NOT_FIRST_ORDER("NOT_FIRST_ORDER"),
    NO_SESSION("NO_SESSION"),
    INTERNAL_ERROR("INTERNAL_ERROR");

	private String status = null;

	private ApplyScratchCardStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return this.status;
	}
}
