/**
 * 
 */
package com.fb.platform.promotion.product.to;

import java.io.Serializable;

/**
 * @author vinayak
 *
 */
public class GetApplicablePromotionsRequest implements Serializable {

	private String sessionToken;
	private int productId;

	public String getSessionToken() {
		return sessionToken;
	}
	public void setSessionToken(String sessionToken) {
		this.sessionToken = sessionToken;
	}
	public int getProductId() {
		return productId;
	}
	public void setProductId(int productId) {
		this.productId = productId;
	}

}
