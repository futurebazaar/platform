/**
 * 
 */
package com.fb.platform.promotion.product.to;

import java.io.Serializable;

/**
 * @author nehaga
 *
 */
public class RefreshAutoPromotionResponse implements Serializable {

	private RefreshAutoPromotionResponseStatusEnum refreshAutoPromotionStatus;
	private String sessionToken;
	
	public RefreshAutoPromotionResponseStatusEnum getRefreshAutoPromotionStatus() {
		return refreshAutoPromotionStatus;
	}
	public void setRefreshAutoPromotionStatus(
			RefreshAutoPromotionResponseStatusEnum refreshAutoPromotionStatus) {
		this.refreshAutoPromotionStatus = refreshAutoPromotionStatus;
	}
	public String getSessionToken() {
		return sessionToken;
	}
	public void setSessionToken(String sessionToken) {
		this.sessionToken = sessionToken;
	}
	
}
