package com.fb.platform.promotion.admin.to;

import java.util.ArrayList;
import java.util.List;

/**
 * @author nehaga
 *
 */
public class SearchPromotionResponse {
	
	private String sessionToken = null;
	private SearchPromotionEnum searchPromotionEnum = null;
	private List<PromotionTO> promotionsList = new ArrayList<PromotionTO>();
	private String errorCause;
	
	public String getSessionToken() {
		return sessionToken;
	}
	public void setSessionToken(String sessionToken) {
		this.sessionToken = sessionToken;
	}
	public List<PromotionTO> getPromotionsList() {
		return promotionsList;
	}
	public void setPromotionsList(List<PromotionTO> promotionsList) {
		this.promotionsList = promotionsList;
	}
	public String getErrorCause() {
		return errorCause;
	}
	public void setErrorCause(String errorCause) {
		this.errorCause = errorCause;
	}
	public SearchPromotionEnum getSearchPromotionEnum() {
		return searchPromotionEnum;
	}
	public void setSearchPromotionEnum(SearchPromotionEnum searchPromotionEnum) {
		this.searchPromotionEnum = searchPromotionEnum;
	}
	
}
