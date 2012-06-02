package com.fb.platform.promotion.model;

import java.io.Serializable;
import java.math.BigDecimal;

import com.fb.platform.promotion.to.OrderRequest;

public class OrderDiscount implements Serializable {

	private OrderRequest orderRequest = null;
	private BigDecimal totalOrderDiscount = null;
	
	public OrderRequest getOrderRequest() {
		return orderRequest;
	}
	public void setOrderRequest(OrderRequest orderRequest) {
		this.orderRequest = orderRequest;
	}
	public BigDecimal getTotalOrderDiscount() {
		return totalOrderDiscount;
	}
	public void setTotalOrderDiscount(BigDecimal totalOrderDiscount) {
		this.totalOrderDiscount = totalOrderDiscount;
	}
}
