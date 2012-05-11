package com.fb.platform.promotion.admin.to;

/**
 * @author nehaga
 *
 */
public class UpdatePromotionResponse {
	
	private String sessionToken = null;
	private UpdatePromotionEnum updatePromotionEnum;
	private String errorCause;
	
	public String getSessionToken() {
		return sessionToken;
	}
	public void setSessionToken(String sessionToken) {
		this.sessionToken = sessionToken;
	}
	public String getErrorCause() {
		return errorCause;
	}
	public void setErrorCause(String errorCause) {
		this.errorCause = errorCause;
	}
	public UpdatePromotionEnum getUpdatePromotionEnum() {
		return updatePromotionEnum;
	}
	public void setUpdatePromotionEnum(UpdatePromotionEnum updatePromotionEnum) {
		this.updatePromotionEnum = updatePromotionEnum;
	}
}
