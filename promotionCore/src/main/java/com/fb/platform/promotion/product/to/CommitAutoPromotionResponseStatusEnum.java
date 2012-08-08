/**
 * 
 */
package com.fb.platform.promotion.product.to;

/**
 * @author nehaga
 *
 */
public enum CommitAutoPromotionResponseStatusEnum {

    SUCCESS("SUCCESS"),
    NO_SESSION("NO_SESSION"),
    INTERNAL_ERROR("INTERNAL_ERROR");

	private String status = null;

	private CommitAutoPromotionResponseStatusEnum(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return this.status;
	}

}
