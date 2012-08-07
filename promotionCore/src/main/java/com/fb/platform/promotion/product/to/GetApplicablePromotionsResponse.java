/**
 * 
 */
package com.fb.platform.promotion.product.to;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fb.platform.promotion.product.model.PromotionConfig;

/**
 * @author vinayak
 *
 */
public class GetApplicablePromotionsResponse implements Serializable {

	private String sessionToken;
	private List<PromotionConfig> applicableConfigs = new ArrayList<PromotionConfig>();

	public String getSessionToken() {
		return sessionToken;
	}
	public void setSessionToken(String sessionToken) {
		this.sessionToken = sessionToken;
	}
	public List<PromotionConfig> getApplicableConfigs() {
		return applicableConfigs;
	}
	public void setApplicableConfigs(List<PromotionConfig> applicableConfigs) {
		this.applicableConfigs = applicableConfigs;
	}
}
