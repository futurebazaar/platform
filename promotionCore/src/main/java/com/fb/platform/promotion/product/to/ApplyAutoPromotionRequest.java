/**
 * 
 */
package com.fb.platform.promotion.product.to;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
	//this comes into play for modification of order
	private List<Integer> appliedPromotions = new ArrayList<Integer>();

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
	public List<Integer> getAppliedPromotions() {
		return appliedPromotions;
	}
	public void setAppliedPromotions(List<Integer> appliedPromotions) {
		this.appliedPromotions = appliedPromotions;
	}
}
