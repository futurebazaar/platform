package com.fb.platform.promotion.admin.to;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.fb.commons.to.PlatformMessage;


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
	
	public List<PlatformMessage> isValid() {
		List<PlatformMessage> requestInvalidationList = new ArrayList<PlatformMessage>();
		requestInvalidationList.addAll(isSessionTokenValid());
		requestInvalidationList.addAll(isPromotionDetailsValid());
		requestInvalidationList.addAll(promotionTO.isValid());
		return requestInvalidationList;
	}
	
	private List<PlatformMessage> isSessionTokenValid() {
		List<PlatformMessage> sessionInvalidationList = new ArrayList<PlatformMessage>();
		if(StringUtils.isEmpty(sessionToken)) {
			sessionInvalidationList.add(new PlatformMessage("EPA1", null));
		}
		return sessionInvalidationList;
	}
	
	private List<PlatformMessage> isPromotionDetailsValid() {
		List<PlatformMessage> sessionInvalidationList = new ArrayList<PlatformMessage>();
		if(promotionTO.getId() == 0) {
			sessionInvalidationList.add(new PlatformMessage("EPA13", null));
		}
		return sessionInvalidationList;
	}
}
