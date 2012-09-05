package com.fb.platform.sap.bapi.to;

public class ResponseTO {
	
	private String sapMessage;
	private String orderID;
	private String type;
	
	public String getSapMessage() {
		return sapMessage;
	}
	public void setSapMessage(String sapMessage) {
		this.sapMessage = sapMessage;
	}
	public String getOrderID() {
		return orderID;
	}
	public void setOrderID(String orderID) {
		this.orderID = orderID;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	@Override
	public String toString() {
		String response = "\nOrder ID: " + orderID
				+ "\nType: " + type
				+ "\nMessage:  " + sapMessage;
		return response;
	}
	
}
