package com.fb.platform.sap.bapi.to;

import java.util.Date;

public class SapInventoryDashboardResponseTO {
	
	private int idocNumber;
	private Date creationDate;
	private Date creationTime;
	private String transactionCode;
	private String article;
	private int actualQuantity;
	private int transferQuantity;
	private int segmentNumber;
	private int poNumber;
	private int unit;
	private int supplyingSite;
	private int receivingSite;
	private int supplyingLocation;
	private int receivingLocation;
	private String materialType;
	private String articleDocument;
	private String cancelGR;
	
	public int getIdocNumber() {
		return idocNumber;
	}
	public void setIdocNumber(int idocNumber) {
		this.idocNumber = idocNumber;
	}
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	public Date getCreationTime() {
		return creationTime;
	}
	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
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
	public int getTransferQuantity() {
		return transferQuantity;
	}
	public void setTransferQuantity(int transferQuantity) {
		this.transferQuantity = transferQuantity;
	}
	public int getSegmentNumber() {
		return segmentNumber;
	}
	public void setSegmentNumber(int segmentNumber) {
		this.segmentNumber = segmentNumber;
	}
	public int getPoNumber() {
		return poNumber;
	}
	public void setPoNumber(int poNumber) {
		this.poNumber = poNumber;
	}
	public int getUnit() {
		return unit;
	}
	public void setUnit(int unit) {
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
	public String getMaterialType() {
		return materialType;
	}
	public void setMaterialType(String materialType) {
		this.materialType = materialType;
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
	
	@Override
	public String toString() {
		return "SapInventoryDashboardResponseTO [idocNumber=" + idocNumber
				+ ", creationDate=" + creationDate + ", creationTime="
				+ creationTime + ", transactionCode=" + transactionCode
				+ ", article=" + article + ", actualQuantity=" + actualQuantity
				+ ", transferQuantity=" + transferQuantity + ", segmentNumber="
				+ segmentNumber + ", poNumber=" + poNumber + ", unit=" + unit
				+ ", supplyingSite=" + supplyingSite + ", receivingSite="
				+ receivingSite + ", supplyingLocation=" + supplyingLocation
				+ ", receivingLocation=" + receivingLocation
				+ ", materialType=" + materialType + ", articleDocument="
				+ articleDocument + ", cancelGR=" + cancelGR + "]";
	}
	
}
