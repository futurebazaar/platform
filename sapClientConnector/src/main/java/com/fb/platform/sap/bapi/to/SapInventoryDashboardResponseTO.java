package com.fb.platform.sap.bapi.to;

import org.joda.time.DateTime;

public class SapInventoryDashboardResponseTO {
	
	private String idocNumber;
	private DateTime creationDateTime;
	private String transactionCode;
	private String article;
	private int actualQuantity;
	private String transferQuantity;
	private int segmentNumber;
	private String poNumber;
	private String unit;
	private int supplyingSite;
	private int receivingSite;
	private int supplyingLocation;
	private int receivingLocation;
	private String movementType;
	private String articleDocument;
	private String cancelGR;
	
	public String getIdocNumber() {
		return idocNumber;
	}
	public void setIdocNumber(String idocNumber) {
		this.idocNumber = idocNumber;
	}
	public String getTransactionCode() {
		return transactionCode;
	}
	public void setTransactionCode(String transactionCode) {
		this.transactionCode = transactionCode;
	}
	public String getArticle() {
		return article;
	}
	public void setArticle(String article) {
		this.article = article;
	}
	public int getActualQuantity() {
		return actualQuantity;
	}
	public void setActualQuantity(int actualQuantity) {
		this.actualQuantity = actualQuantity;
	}
	public String getTransferQuantity() {
		return transferQuantity;
	}
	public void setTransferQuantity(String transferQuantity) {
		this.transferQuantity = transferQuantity;
	}
	public int getSegmentNumber() {
		return segmentNumber;
	}
	public void setSegmentNumber(int segmentNumber) {
		this.segmentNumber = segmentNumber;
	}
	public String getPoNumber() {
		return poNumber;
	}
	public void setPoNumber(String poNumber) {
		this.poNumber = poNumber;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public int getSupplyingSite() {
		return supplyingSite;
	}
	public void setSupplyingSite(int supplyingSite) {
		this.supplyingSite = supplyingSite;
	}
	public int getReceivingSite() {
		return receivingSite;
	}
	public void setReceivingSite(int receivingSite) {
		this.receivingSite = receivingSite;
	}
	public int getSupplyingLocation() {
		return supplyingLocation;
	}
	public void setSupplyingLocation(int supplyingLocation) {
		this.supplyingLocation = supplyingLocation;
	}
	public int getReceivingLocation() {
		return receivingLocation;
	}
	public void setReceivingLocation(int receivingLocation) {
		this.receivingLocation = receivingLocation;
	}
	public String getMovementType() {
		return movementType;
	}
	public void setMovementType(String movementType) {
		this.movementType = movementType;
	}
	public String getArticleDocument() {
		return articleDocument;
	}
	public void setArticleDocument(String articleDocument) {
		this.articleDocument = articleDocument;
	}
	public String getCancelGR() {
		return cancelGR;
	}
	public void setCancelGR(String cancelGR) {
		this.cancelGR = cancelGR;
	}
	public DateTime getCreationDateTime() {
		return creationDateTime;
	}
	public void setCreationDateTime(DateTime creationDateTime) {
		this.creationDateTime = creationDateTime;
	}
	
	@Override
	public String toString() {
		return "SapInventoryDashboardResponseTO [idocNumber=" + idocNumber
				+ ", creationDateTime=" + creationDateTime
				+ ", transactionCode=" + transactionCode + ", article="
				+ article + ", actualQuantity=" + actualQuantity
				+ ", transferQuantity=" + transferQuantity + ", segmentNumber="
				+ segmentNumber + ", poNumber=" + poNumber + ", unit=" + unit
				+ ", supplyingSite=" + supplyingSite + ", receivingSite="
				+ receivingSite + ", supplyingLocation=" + supplyingLocation
				+ ", receivingLocation=" + receivingLocation
				+ ", movementType=" + movementType + ", articleDocument="
				+ articleDocument + ", cancelGR=" + cancelGR + "]";
	}
	
}
