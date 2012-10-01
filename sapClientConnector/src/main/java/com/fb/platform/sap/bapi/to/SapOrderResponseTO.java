package com.fb.platform.sap.bapi.to;

import com.fb.platform.sap.client.commons.SapResponseStatus;

public class SapOrderResponseTO {
	
	private String sapMessage;
	private String orderId;
	private String returnOrderId;
	private String type;
	private SapResponseStatus status;
	
	public String getSapMessage() {
		return sapMessage;
	}
	public void setSapMessage(String sapMessage) {
		this.sapMessage = sapMessage;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
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
	public String getReturnOrderId() {
		return returnOrderId;
	}
	public void setReturnOrderId(String returnOrderId) {
		this.returnOrderId = returnOrderId;
	}
	
	@Override
	public String toString() {
		return "SapOrderResponseTO [sapMessage=" + sapMessage + ", orderId="
				+ orderId + ", returnOrderId=" + returnOrderId + ", type="
				+ type + ", status=" + status + "]";
	}
	
}
