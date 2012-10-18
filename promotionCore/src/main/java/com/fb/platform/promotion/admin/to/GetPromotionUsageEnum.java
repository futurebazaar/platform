package com.fb.platform.promotion.admin.to;

public enum GetPromotionUsageEnum {
	SUCCESS("SUCCESS"),
	INTERNAL_ERROR("INTERNAL_ERROR"),
	NO_SESSION("NO_SESSION"),
	INVALID_PROMOTION("INVALID_PROMOTION");
	
	private String status = null;

	private GetPromotionUsageEnum(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return this.status;
	}
}
