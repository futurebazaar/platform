package com.fb.platform.promotion.to;


/**
 * @author nehaga
 *
 */
public class CreatePromotionRequest {
	
	private String sessionToken = null;
	private PromotionTO promotionTO = null;
	
	public String getSessionToken() {
		return sessionToken;
	}
	public void setSessionToken(String sessionToken) {
		this.sessionToken = sessionToken;
	}
	public PromotionTO getPromotion() {
		return promotionTO;
	}
	public void setPromotion(PromotionTO promotionTO) {
		this.promotionTO = promotionTO;
	}
}
