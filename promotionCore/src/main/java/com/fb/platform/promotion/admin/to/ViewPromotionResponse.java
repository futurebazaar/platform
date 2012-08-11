package com.fb.platform.promotion.admin.to;

/**
 * @author nehaga
 *
 */
public class ViewPromotionResponse {
	
	private String sessionToken = null;
	private PromotionTO promotionCompleteView = null;
	private ViewPromotionEnum viewPromotionEnum = null;
	private String errorCause;
	
	public String getSessionToken() {
		return sessionToken;
	}
	public void setSessionToken(String sessionToken) {
		this.sessionToken = sessionToken;
	}
	public PromotionTO getPromotionCompleteView() {
		return promotionCompleteView;
	}
	public void setPromotionCompleteView(PromotionTO promotionCompleteView) {
		this.promotionCompleteView = promotionCompleteView;
	}
	public ViewPromotionEnum getViewPromotionEnum() {
		return viewPromotionEnum;
	}
	public void setViewPromotionEnum(ViewPromotionEnum viewPromotionEnum) {
		this.viewPromotionEnum = viewPromotionEnum;
	}
	public String getErrorCause() {
		return errorCause;
	}
	public void setErrorCause(String errorCause) {
		this.errorCause = errorCause;
	}
}
