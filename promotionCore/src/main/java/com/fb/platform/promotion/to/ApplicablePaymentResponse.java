package com.fb.platform.promotion.to;

import java.io.Serializable;
import java.util.List;

import com.fb.platform.promotion.model.PaymentOption;

/**
 * 
 * @author keith
 * 
 */
public class ApplicablePaymentResponse implements Serializable {

	private String couponCode;
	private ApplicablePaymentResponseStatusEnum status;
	private List<PaymentOption> includePaymentList;
	private List<PaymentOption> excludePaymentList;
	private String sessionToken;

	public String getCouponCode() {
		return couponCode;
	}

	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}

	public ApplicablePaymentResponseStatusEnum getStatus() {
		return status;
	}

	public void setStatus(ApplicablePaymentResponseStatusEnum status) {
		this.status = status;
	}

	public List<PaymentOption> getIncludePaymentList() {
		return includePaymentList;
	}

	public void setIncludePaymentList(List<PaymentOption> includePaymentList) {
		this.includePaymentList = includePaymentList;
	}

	public List<PaymentOption> getExcludePaymentList() {
		return excludePaymentList;
	}

	public void setExcludePaymentList(List<PaymentOption> excludePaymentList) {
		this.excludePaymentList = excludePaymentList;
	}

	public String getSessionToken() {
		return sessionToken;
	}

	public void setSessionToken(String sessionToken) {
		this.sessionToken = sessionToken;
	}

}
