/**
 * 
 */
package com.fb.platform.promotion.admin.to;

/**
 * @author SalimM
 *
 */
public enum SearchScratchCardStatusEnum {

	SUCCESS("SUCCESS"),
	INTERNAL_ERROR("INTERNAL_ERROR"),
	NO_SESSION("NO_SESSION"),
	INSUFFICIENT_DATA("INSUFFICIENT_DATA"),
	INVALID_USER("INVALID_USER"),
	NO_DATA_FOUND("NO_DATA_FOUND"),
	NO_SCRATCH_CARD_FOUND("NO_DATA_FOUND");
	
	private String status = null;

	private SearchScratchCardStatusEnum(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return this.status;
	}
}
