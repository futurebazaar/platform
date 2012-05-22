package com.fb.platform.promotion.admin.to;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * @author nehaga
 *
 */
public class ViewPromotionRequest {
	
	private String sessionToken = null;
	private int promotionId;
	
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
	
	public String isValid() {
		List<String> requestInvalidationList = new ArrayList<String>();
		requestInvalidationList.addAll(isSessionTokenValid());
		return StringUtils.join(requestInvalidationList.toArray(), ",");
	}
	
	private List<String> isSessionTokenValid() {
		List<String> sessionInvalidationList = new ArrayList<String>();
		if(StringUtils.isBlank(sessionToken)) {
			sessionInvalidationList.add("Session token cannot be empty");
		}
		return sessionInvalidationList;
	}
	
}
