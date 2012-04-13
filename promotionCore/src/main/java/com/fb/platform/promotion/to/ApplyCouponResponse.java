package com.fb.platform.promotion.to;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 
 * @author keith
 *
 */
public class ApplyCouponResponse implements Serializable{

	private String couponCode;
	private BigDecimal discountValue;
	private ApplyCouponResponseStatusEnum couponStatus;
	private String statusMessage;
	private String promoName;
	private String promoDescription;
	private String sessionToken;

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
	 * @return the statusMessage
	 */
	public String getStatusMessage() {
		return statusMessage;
	}
	public ApplyCouponResponseStatusEnum getCouponStatus() {
		return couponStatus;
	}
	public void setCouponStatus(ApplyCouponResponseStatusEnum couponStatus) {
		this.couponStatus = couponStatus;
		this.statusMessage = couponStatus.getMesage();
	}
	public String getSessionToken() {
		return sessionToken;
	}
	public void setSessionToken(String sessionToken) {
		this.sessionToken = sessionToken;
	}
	public String getPromoName() {
		return promoName;
	}
	public void setPromoName(String promoName) {
		this.promoName = promoName;
	}
	public String getPromoDescription() {
		return promoDescription;
	}
	public void setPromoDescription(String promoDescription) {
		this.promoDescription = promoDescription;
	}
}
