/**
 * 
 */
package com.fb.platform.promotion.product.to;

import java.io.Serializable;

import org.joda.time.DateTime;

import com.fb.platform.promotion.to.OrderRequest;

/**
 * @author vinayak
 *
 */
public class ApplyAutoPromotionRequest implements Serializable {

	private OrderRequest orderReq;
	private String sessionToken = null;
	private DateTime orderBookingDate;

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
	public DateTime getOrderBookingDate() {
		return orderBookingDate;
	}
	public void setOrderBookingDate(DateTime orderBookingDate) {
		this.orderBookingDate = orderBookingDate;
	}
}
