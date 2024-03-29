/**
 * 
 */
package com.fb.platform.promotion.to;

import java.io.Serializable;
import java.math.BigDecimal;

import org.joda.time.DateTime;

/**
 * @author vinayak
 *
 */
public class CommitCouponRequest implements Serializable {

	private String couponCode;
	private int orderId;
	private BigDecimal discountValue;
	private String sessionToken;
	private DateTime orderBookingDate;

	public DateTime getOrderBookingDate() {
		return orderBookingDate;
	}
	public void setOrderBookingDate(DateTime orderBookingDate) {
		this.orderBookingDate = orderBookingDate;
	}
	public String getCouponCode() {
		return couponCode;
	}
	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}
	public BigDecimal getDiscountValue() {
		return discountValue;
	}
	public void setDiscountValue(BigDecimal discountValue) {
		this.discountValue = discountValue;
	}
	public String getSessionToken() {
		return sessionToken;
	}
	public void setSessionToken(String sessionToken) {
		this.sessionToken = sessionToken;
	}
	public int getOrderId() {
		return orderId;
	}
	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}
}
