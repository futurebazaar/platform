/**
 * 
 */
package com.fb.platform.promotion.to;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author vinayak
 *
 */
public class OrderRequest implements Serializable {
 
	private int orderId = 0;
	private List<OrderItem> orderItems = new ArrayList<OrderItem>();
	private int clientId = 0;

	public int getOrderId() {
		return orderId;
	}
	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}
	public BigDecimal getOrderValue() {
		BigDecimal orderValue = new BigDecimal(0);
		for(OrderItem oItem : orderItems){
			orderValue = orderValue.add(oItem.getPrice());
		}
				return orderValue;
	}
	public List<OrderItem> getOrderItems() {
		return orderItems;
	}
	public void setOrderItems(List<OrderItem> orderItems) {
		this.orderItems = orderItems;
	}
	public int getClientId() {
		return clientId;
	}
	public void setClientId(int clientId) {
		this.clientId = clientId;
	}
	
	public boolean isAllProductsInCategory(List<Integer> categories){
		for(OrderItem o:orderItems){
			if(!o.isOrderItemInCategory(categories)){
				return false;
			}
		}
		return true;
	}
	
	public boolean isAnyProductInCategory(){
		return false;
	}
	
	public boolean isValidClient(List<Integer> client_list){
		return client_list.contains(Integer.valueOf(clientId));
	}
	
}
