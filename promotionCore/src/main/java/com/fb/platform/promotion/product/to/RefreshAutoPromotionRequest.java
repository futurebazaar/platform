/**
 * 
 */
package com.fb.platform.promotion.product.to;

import java.io.Serializable;

/**
 * @author nehaga
 *
 */
public class RefreshAutoPromotionRequest implements Serializable {

	private String sessionToken = null;

	public String getSessionToken() {
		return sessionToken;
	}
	public void setSessionToken(String sessionToken) {
		this.sessionToken = sessionToken;
	}
}
