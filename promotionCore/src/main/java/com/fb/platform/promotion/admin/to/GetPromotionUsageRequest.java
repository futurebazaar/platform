package com.fb.platform.promotion.admin.to;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

public class GetPromotionUsageRequest implements Serializable {
	private String sessionToken = null;
	private int promotionId;
	
	public String getSessionToken() {
		return sessionToken;
	}
	
	public void setSessionToken(String sessionToken) {
		this.sessionToken = sessionToken;
	}

	public int getpromotionId() {
		return promotionId;
	}
	
	public void setpromotionId(int promotionId) {
		this.promotionId = promotionId;
	}
	
	public GetPromotionUsageResponse validate() {
		GetPromotionUsageResponse response = new GetPromotionUsageResponse();
		response.setSessionToken(getSessionToken());
		
		if (StringUtils.isBlank(sessionToken)) {
			response.setStatus(GetPromotionUsageEnum.NO_SESSION);
			return response;
		}
	
		if (promotionId == 0) {
			response.setStatus(GetPromotionUsageEnum.INVALID_PROMOTION);
			return response; 
		}
		
		return null;
	}
}