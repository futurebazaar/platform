package com.fb.platform.sap.bapi.to;

import com.fb.platform.sap.client.commons.SapResponseStatus;

public class SapOrderResponseTO {
	
	private String sapMessage;
	private String orderID;
	private String type;
	private SapResponseStatus status;
	
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
	public SapResponseStatus getStatus() {
		return status;
	}
	public void setStatus(SapResponseStatus status) {
		this.status = status;
	}
	
	@Override
	public String toString() {
		return "SapOrderResponseTO [sapMessage=" + sapMessage + ", orderID="
				+ orderID + ", type=" + type + ", status=" + status + "]";
	}
	
}
