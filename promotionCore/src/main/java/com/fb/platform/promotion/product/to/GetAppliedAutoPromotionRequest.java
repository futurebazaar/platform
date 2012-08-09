/**
 * 
 */
package com.fb.platform.promotion.product.to;

import java.io.Serializable;

/**
 * @author nehaga
 *
 */
public class GetAppliedAutoPromotionRequest implements Serializable {
	private int orderId;
	private String sessionToken;
	
	public int getOrderId() {
		return orderId;
	}
	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}
	public String getSessionToken() {
		return sessionToken;
	}
	public void setSessionToken(String sessionToken) {
		this.sessionToken = sessionToken;
	}
}
