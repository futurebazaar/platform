/**
 * 
 */
package com.fb.platform.promotion.product.to;

import java.io.Serializable;
import java.util.List;

/**
 * @author nehaga
 *
 */
public class CommitAutoPromotionRequest implements Serializable {
	
	private int orderId;
	private List<Integer> appliedPromotionsList;
	protected String sessionToken;
	
	public int getOrderId() {
		return orderId;
	}
	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}
	public List<Integer> getAppliedPromotionsList() {
		return appliedPromotionsList;
	}
	public void setAppliedPromotionsList(List<Integer> appliedPromotionsList) {
		this.appliedPromotionsList = appliedPromotionsList;
	}
	public String getSessionToken() {
		return sessionToken;
	}
	public void setSessionToken(String sessionToken) {
		this.sessionToken = sessionToken;
	}
}
