package com.fb.platform.promotion.admin.to;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.fb.commons.to.PlatformMessage;

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
	
	public List<PlatformMessage> isValid() {
		List<PlatformMessage> requestInvalidationList = new ArrayList<PlatformMessage>();
		requestInvalidationList.addAll(isSessionTokenValid());
		return requestInvalidationList;
	}
	
	private List<PlatformMessage> isSessionTokenValid() {
		List<PlatformMessage> sessionInvalidationList = new ArrayList<PlatformMessage>();
		if(StringUtils.isBlank(sessionToken)) {
			sessionInvalidationList.add(new PlatformMessage("EPA1", null));
		}
		return sessionInvalidationList;
	}
	
}
