/**
 * 
 */
package com.fb.platform.promotion.to;

import java.io.Serializable;

/**
 * @author keith
 *
 */
public enum ApplicablePaymentResponseStatusEnum  implements Serializable {

	SUCCESS("SUCCESS"),
	INVALID_COUPON_CODE("INVALID_COUPON_CODE"),
	INTERNAL_ERROR("INTERNAL_ERROR"),
	NO_PAYMENT_OPTION_FOUND("NO_PAYMENT_OPTION_FOUND"),
	NO_SESSION("NO_SESSION");

	private String status = null;

	private ApplicablePaymentResponseStatusEnum(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return this.status;
	}
}
