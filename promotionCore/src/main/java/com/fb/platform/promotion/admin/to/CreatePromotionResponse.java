package com.fb.platform.promotion.admin.to;

/**
 * @author nehaga
 *
 */
public class CreatePromotionResponse {
	
	private String sessionToken = null;
	private int promotionId;
	private CreatePromotionEnum createPromotionEnum;
	private String errorCause;
	
	public String getSessionToken() {
		return sessionToken;
	}
	public void setSessionToken(String sessionToken) {
		this.sessionToken = sessionToken;
	}
	public int getPromotionId() {
		return promotionId;
	}
	public void setPromotionId(int promotionId) {
		this.promotionId = promotionId;
	}
	public CreatePromotionEnum getCreatePromotionEnum() {
		return createPromotionEnum;
	}
	public void setCreatePromotionEnum(CreatePromotionEnum createPromotionEnum) {
		this.createPromotionEnum = createPromotionEnum;
	}
	public String getErrorCause() {
		return errorCause;
	}
	public void setErrorCause(String errorCause) {
		this.errorCause = errorCause;
	}
	
}
