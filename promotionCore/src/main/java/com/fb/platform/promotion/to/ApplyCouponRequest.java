package com.fb.platform.promotion.to;

import java.io.Serializable;
import java.math.BigDecimal;

import org.joda.time.DateTime;

/**
 * 
 * @author vinayak
 *
 */
public class ApplyCouponRequest implements Serializable {

	private String couponCode;
	private OrderRequest orderReq;
	private String sessionToken = null;
	private Boolean isCouponCommitted;
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
	public OrderRequest getOrderReq() {
		return orderReq;
	}
	public void setOrderReq(OrderRequest orderReq) {
		this.orderReq = orderReq;
	}
	public String getSessionToken() {
		return sessionToken;
	}
	public void setSessionToken(String sessionToken) {
		this.sessionToken = sessionToken;
	}
	public BigDecimal getOrderValue()
	{
		return orderReq.getOrderValue();
	}
	public Boolean getIsOrderCommitted() {
		return isCouponCommitted;
	}
	public void setIsOrderCommitted(Boolean isCouponCommitted) {
		this.isCouponCommitted = isCouponCommitted;
	}
}
