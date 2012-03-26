/**
 * 
 */
package com.fb.platform.promotion.to;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author vinayak
 *
 */
public class OrderRequest implements Serializable {
 
	private int orderId = 0;
	private BigDecimal orderValue = null;
	private List<OrderItem> orderItems = null;

	public int getOrderId() {
		return orderId;
	}
	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}
	public BigDecimal getOrderValue() {
		return orderValue;
	}
	public void setOrderValue(BigDecimal orderValue) {
		this.orderValue = orderValue;
	}
	public List<OrderItem> getOrderItems() {
		return orderItems;
	}
	public void setOrderItems(List<OrderItem> orderItems) {
		this.orderItems = orderItems;
	}

}
