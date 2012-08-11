/**
 * 
 */
package com.fb.platform.promotion.product.to;

/**
 * @author nehaga
 *
 */
public enum GetAppliedAutoPromotionResponseStatusEnum {

    SUCCESS("SUCCESS"),
    NO_SESSION("NO_SESSION"),
    INTERNAL_ERROR("INTERNAL_ERROR");

	private String status = null;

	private GetAppliedAutoPromotionResponseStatusEnum(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return this.status;
	}

}
