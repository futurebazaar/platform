package com.fb.platform.promotion.to;

import java.io.Serializable;

import com.fb.platform.promotion.model.PaymentOption;

/**
 * 
 * @author keith
 * 
 */
public class ApplicablePaymentResponse implements Serializable {

	private String couponCode;
	private ApplicablePaymentResponseStatusEnum status;
	private PaymentOption paymentOption;
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

	public String getSessionToken() {
		return sessionToken;
	}

	public void setSessionToken(String sessionToken) {
		this.sessionToken = sessionToken;
	}

	public PaymentOption getPaymentOption() {
		return paymentOption;
	}

	public void setPaymentOption(PaymentOption paymentOption) {
		this.paymentOption = paymentOption;
	}

}
