package com.fb.platform.promotion.to;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ClearPromotionCacheResponse  implements Serializable {
	private int promotionId;
	private List<ClearCouponCacheResponse> clearCouponCacheResponse = new ArrayList<ClearCouponCacheResponse>();
	private String sessionToken;
	private ClearCacheEnum clearCacheEnum = null;
	
	public int getPromotionId() {
		return promotionId;
	}
	public void setPromotionId(int promotionId) {
		this.promotionId = promotionId;
	}
	public List<ClearCouponCacheResponse> getClearCouponCacheResponse() {
		return clearCouponCacheResponse;
	}
	public void setClearCouponCacheResponse(List<ClearCouponCacheResponse> clearCouponCacheResponse) {
		this.clearCouponCacheResponse = clearCouponCacheResponse;
	}
	public String getSessionToken() {
		return sessionToken;
	}
	public void setSessionToken(String sessionToken) {
		this.sessionToken = sessionToken;
	}
	public ClearCacheEnum getClearCacheEnum() {
		return clearCacheEnum;
	}
	public void setClearCacheEnum(ClearCacheEnum clearCacheEnum) {
		this.clearCacheEnum = clearCacheEnum;
	}
	
	
}
