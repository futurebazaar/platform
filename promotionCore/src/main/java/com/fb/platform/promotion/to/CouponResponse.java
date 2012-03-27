package com.fb.platform.promotion.to;

import java.io.Serializable;
import java.math.BigDecimal;

@SuppressWarnings("serial")
public class CouponResponse implements Serializable {

	private String couponCode;
	private BigDecimal discountValue;
	private PromotionResponseStatusCode statusCode;
	private String statusMessage;
	private String statusDescription;
	
	/**
	 * @return the couponCode
	 */
	public String getCouponCode() {
		return couponCode;
	}
	/**
	 * @param couponCode the couponCode to set
	 */
	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}
	/**
	 * @return the discountValue
	 */
	public BigDecimal getDiscountValue() {
		return discountValue;
	}
	/**
	 * @param discountValue the discountValue to set
	 */
	public void setDiscountValue(BigDecimal discountValue) {
		this.discountValue = discountValue;
	}
	/**
	 * @return the statusCode
	 */
	public PromotionResponseStatusCode getStatusCode() {
		return statusCode;
	}
	/**
	 * @param statusCode the statusCode to set
	 */
	public void setStatusCode(PromotionResponseStatusCode statusCode) {
		this.statusCode = statusCode;
	}
	/**
	 * @return the statusMessage
	 */
	public String getStatusMessage() {
		return statusMessage;
	}
	/**
	 * @param statusMessage the statusMessage to set
	 */
	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}
	/**
	 * @return the statusDescription
	 */
	public String getStatusDescription() {
		return statusDescription;
	}
	/**
	 * @param statusDescription the statusDescription to set
	 */
	public void setStatusDescription(String statusDescription) {
		this.statusDescription = statusDescription;
	}
	
	
	
}
