package com.fb.tinla.models;

public class OrderItem {

	private String lineItemNumber;
	private Shipment shipment;
	private Return ret;
	private String quantity;
	private String status;

	public String getLineItemNumber() {
		return lineItemNumber;
	}

	public void setLineItemNumber(String lineItemNumber) {
		this.lineItemNumber = lineItemNumber;
	}

	public Shipment getShipment() {
		return shipment;
	}

	public void setShipment(Shipment shipment) {
		this.shipment = shipment;
	}

	public Return getReturn() {
		return ret;
	}

	public void setReturn(Return ret) {
		this.ret = ret;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
