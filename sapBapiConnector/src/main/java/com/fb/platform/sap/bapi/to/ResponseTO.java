package com.fb.platform.sap.bapi.to;

public class ResponseTO {
	
	private String sapMessage;
	private String orderID;
	private String sapID;
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
	public String getSapID() {
		return sapID;
	}
	public void setSapID(String sapID) {
		this.sapID = sapID;
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
				+ "\nSap ID: " + sapID
				+ "\nType: " + type
				+ "\nMessage: : " + sapMessage;
		return response;
	}
	
}
