/**
 * 
 */
package com.fb.platform.promotion.to;

import java.io.Serializable;

/**
 * @author keith
 *
 */
public enum ReleaseCouponStatusEnum  implements Serializable {

	SUCCESS("SUCCESS"),
	INVALID_COUPON_CODE("INVALID_COUPON_CODE"),
	INTERNAL_ERROR("INTERNAL_ERROR"),
	COUPON_NOT_COMMITTED("COUPON_NOT_COMMITTED"),
	NO_SESSION("NO_SESSION");

	private String status = null;

	private ReleaseCouponStatusEnum(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return this.status;
	}
}
