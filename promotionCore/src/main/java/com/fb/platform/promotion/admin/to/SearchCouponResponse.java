package com.fb.platform.promotion.admin.to;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ashish
 *
 */
public class SearchCouponResponse implements Serializable{
	
	private String sessionToken = null;
	private SearchCouponStatusEnum status = null;
	private List<CouponBasicDetails> couponBasicDetailsList = new ArrayList<CouponBasicDetails>();
	private String errorCause;
	
	public String getSessionToken() {
		return sessionToken;
	}
	public void setSessionToken(String sessionToken) {
		this.sessionToken = sessionToken;
	}
	public List<CouponBasicDetails> getCouponBasicDetailsList() {
		return couponBasicDetailsList;
	}
	public void setCouponBasicDetailsList(List<CouponBasicDetails> couponBasicDetailsList) {
		this.couponBasicDetailsList = couponBasicDetailsList;
	}
	public String getErrorCause() {
		return errorCause;
	}
	public void setErrorCause(String errorCause) {
		this.errorCause = errorCause;
	}
	public SearchCouponStatusEnum getStatus() {
		return status;
	}
	public void setStatus(SearchCouponStatusEnum status) {
		this.status = status;
	}
	
}
