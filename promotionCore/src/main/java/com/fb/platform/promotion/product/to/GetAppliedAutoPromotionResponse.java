/**
 * 
 */
package com.fb.platform.promotion.product.to;

import java.io.Serializable;
import java.util.List;

import com.fb.platform.promotion.model.Promotion;


/**
 * @author nehaga
 *
 */
public class GetAppliedAutoPromotionResponse implements Serializable {
	private GetAppliedAutoPromotionResponseStatusEnum getAppliedAutoPromotionStatus;
	private String sessionToken;
	private List<Promotion> promotionList;
	
	public GetAppliedAutoPromotionResponseStatusEnum getGetAppliedAutoPromotionStatus() {
		return getAppliedAutoPromotionStatus;
	}
	public void setGetAppliedAutoPromotionStatus(
			GetAppliedAutoPromotionResponseStatusEnum getAppliedAutoPromotionStatus) {
		this.getAppliedAutoPromotionStatus = getAppliedAutoPromotionStatus;
	}
	public String getSessionToken() {
		return sessionToken;
	}
	public void setSessionToken(String sessionToken) {
		this.sessionToken = sessionToken;
	}
	public List<Promotion> getPromotionList() {
		return promotionList;
	}
	public void setPromotionList(List<Promotion> promotionList) {
		this.promotionList = promotionList;
	}
}
