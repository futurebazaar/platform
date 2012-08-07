/**
 * 
 */
package com.fb.platform.promotion.product.to;

/**
 * @author vinayak
 *
 */
public enum ApplyAutoPromotionResponseStatusEnum {

    SUCCESS("SUCCESS"),
    NO_SESSION("NO_SESSION"),
    INTERNAL_ERROR("INTERNAL_ERROR");

	private String status = null;

	private ApplyAutoPromotionResponseStatusEnum(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return this.status;
	}

}
