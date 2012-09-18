package com.fb.platform.ifs.to.order;

import java.util.ArrayList;
import java.util.List;

public class Order {
	
	private int id;
	private String referenceOrderId;
	private List<OrderItem> orderItems = new ArrayList<OrderItem>();
	private String pincode;
	private boolean isCod;
	
	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setReferenceOrderId(String referenceOrderId) {
		this.referenceOrderId = referenceOrderId;
	}

	public String getReferenceOrderId() {
		return referenceOrderId;
	}

	public void setOrderItems(List<OrderItem> orderItems) {
		this.orderItems = orderItems;
	}
	
	public List<OrderItem> getOrderItems() {
		return orderItems;
	}

	public void setPincode(String pincode) {
		this.pincode = pincode;
	}

	public String getPincode() {
		return pincode;
	}

	public void setCod(boolean isCod) {
		this.isCod = isCod;
	}

	public boolean isCod() {
		return isCod;
	}
	
}
