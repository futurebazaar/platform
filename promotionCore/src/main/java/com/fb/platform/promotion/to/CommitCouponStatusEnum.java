/**
 * 
 */
package com.fb.platform.promotion.to;

import java.io.Serializable;

/**
 * @author vinayak
 *
 */
public enum CommitCouponStatusEnum implements Serializable {

	SUCCESS("SUCCESS"),
	INVALID_COUPON_CODE("INVALID_COUPON_CODE"),
	NUMBER_OF_USES_EXCEEDED("NUMBER_OF_USES_EXCEEDED"),
	INTERNAL_ERROR("INTERNAL_ERROR"),
	NO_SESSION("NO_SESSION");

	private String status = null;
	
	private CommitCouponStatusEnum(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return this.status;
	}
}
