package com.fb.tinla.models;

import java.util.List;

public class Shipment {

	private String orderId;
	private String deliveryNumber;
	private String deliveryCreatedDate;
	private String invoiceNumber;
	private String invoiceCreatedDate;
	private String pgiCreatedDate;
	private String deletedDate;
	private String deletedTime;
	private String invoiceNetValue;
	private String dcCode;
	private String lspCode;
	private String lspName;
	private String trackingNumber;
	private String status;
	private String quantity;
	private String deletedBy;
	private List<ShipmentItem> shipmentItems;

	public String getDeliveryNumber() {
		return deliveryNumber;
	}

	public void setDeliveryNumber(String deliveryNumber) {
		this.deliveryNumber = deliveryNumber;
	}

	public String getDeliveryCreatedDate() {
		return deliveryCreatedDate;
	}

	public void setDeliveryCreatedDate(String deliveryCreatedDate) {
		this.deliveryCreatedDate = deliveryCreatedDate;
	}

	public String getInvoiceNumber() {
		return invoiceNumber;
	}

	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	public String getInvoiceCreatedDate() {
		return invoiceCreatedDate;
	}

	public void setInvoiceCreatedDate(String invoiceCreatedDate) {
		this.invoiceCreatedDate = invoiceCreatedDate;
	}

	public String getDcCode() {
		return dcCode;
	}

	public void setDcCode(String dcCode) {
		this.dcCode = dcCode;
	}

	public String getLspCode() {
		return lspCode;
	}

	public void setLspCode(String lspCode) {
		this.lspCode = lspCode;
	}

	public String getLspName() {
		return lspName;
	}

	public void setLspName(String lspName) {
		this.lspName = lspName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public String getPgiCreatedDate() {
		return pgiCreatedDate;
	}

	public void setPgiCreatedDate(String pgiCreatedDate) {
		this.pgiCreatedDate = pgiCreatedDate;
	}

	public String getInvoiceNetValue() {
		return invoiceNetValue;
	}

	public void setInvoiceNetValue(String invoiceNetValue) {
		this.invoiceNetValue = invoiceNetValue;
	}

	public String getTrackingNumber() {
		return trackingNumber;
	}

	public void setTrackingNumber(String trackingNumber) {
		this.trackingNumber = trackingNumber;
	}

	public String getDeletedDate() {
		return deletedDate;
	}

	public void setDeletedDate(String deletedDate) {
		this.deletedDate = deletedDate;
	}

	public List<ShipmentItem> getShipmentItems() {
		return shipmentItems;
	}

	public void setShipmentItems(List<ShipmentItem> shipmentItems) {
		this.shipmentItems = shipmentItems;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getDeletedBy() {
		return deletedBy;
	}

	public void setDeletedBy(String deletedBy) {
		this.deletedBy = deletedBy;
	}

	public String getDeletedTime() {
		return deletedTime;
	}

	public void setDeletedTime(String deletedTime) {
		this.deletedTime = deletedTime;
	}

}
