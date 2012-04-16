package com.fb.platform.promotion.to;

import java.io.Serializable;

public enum ClearCacheEnum  implements Serializable {
	
	SUCCESS("SUCCESS"),
	CACHE_NOT_FOUND("CACHE_NOT_FOUND"),
	INTERNAL_ERROR("INTERNAL_ERROR"),
	NO_SESSION("NO_SESSION");

	private String status = null;

	private ClearCacheEnum(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return this.status;
	}
}
