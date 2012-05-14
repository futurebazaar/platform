package com.fb.platform.promotion.admin.to;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;


/**
 * @author nehaga
 *
 */
public class UpdatePromotionRequest {
	
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
	
	public String isValid() {
		List<String> requestInvalidationList = new ArrayList<String>();
		requestInvalidationList.addAll(isSessionTokenValid());
		requestInvalidationList.addAll(isPromotionDetailsValid());
		requestInvalidationList.add(promotionTO.isValid());
		return StringUtils.join(requestInvalidationList.toArray(), ",");
	}
	
	private List<String> isSessionTokenValid() {
		List<String> sessionInvalidationList = new ArrayList<String>();
		if(StringUtils.isEmpty(sessionToken)) {
			sessionInvalidationList.add("Session token cannot be empty");
		}
		return sessionInvalidationList;
	}
	
	private List<String> isPromotionDetailsValid() {
		List<String> sessionInvalidationList = new ArrayList<String>();
		if(promotionTO.getPromotionId() == 0) {
			sessionInvalidationList.add("Incorrect Promotion Id");
		}
		return sessionInvalidationList;
	}
}
