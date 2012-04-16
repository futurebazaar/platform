package com.fb.platform.promotion.to;

import java.io.Serializable;

public class ClearPromotionCacheRequest  implements Serializable {
	private int promotionId;
	private String sessionToken;
	
	public int getPromotionId() {
		return promotionId;
	}
	public void setPromotionId(int promotionId) {
		this.promotionId = promotionId;
	}
	public String getSessionToken() {
		return sessionToken;
	}
	public void setSessionToken(String sessionToken) {
		this.sessionToken = sessionToken;
	}
}
