package com.fb.commons.mom.to;

import org.joda.time.DateTime;

public class LineItemTO extends ItemTO {
	private String description;
	private String articleID;
	private boolean isThirdParty;
	private String vendor;
	private String transportMode;
	private String giftVoucherDetails;
	private String ProductGroupID;
	private String catalog;
	private String salesUnit;
	private DateTime requiredDeliveryDate;
	private String reasonCode;
	private String notes;
	private String bundle;
	private String operationCode;
	private String relationshipArticleID;
	private String storageLocation;
	private String shippingMode;
	private PricingTO pricingTO;
	private String payToOthers;
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getArticleID() {
		return articleID;
	}
	public void setArticleID(String articleID) {
		this.articleID = articleID;
	}
	public boolean isThirdParty() {
		return isThirdParty;
	}
	public void setThirdParty(boolean isThirdParty) {
		this.isThirdParty = isThirdParty;
	}
	public String getVendor() {
		return vendor;
	}
	public void setVendor(String vendor) {
		this.vendor = vendor;
	}
	public String getTransportMode() {
		return transportMode;
	}
	public void setTransportMode(String transportMode) {
		this.transportMode = transportMode;
	}
	public String getGiftVoucherDetails() {
		return giftVoucherDetails;
	}
	public void setGiftVoucherDetails(String giftVoucherDetails) {
		this.giftVoucherDetails = giftVoucherDetails;
	}
	public String getProductGroupID() {
		return ProductGroupID;
	}
	public void setProductGroupID(String productGroupID) {
		ProductGroupID = productGroupID;
	}
	public String getCatalog() {
		return catalog;
	}
	public void setCatalog(String catalog) {
		this.catalog = catalog;
	}
	public String getSalesUnit() {
		return salesUnit;
	}
	public void setSalesUnit(String salesUnit) {
		this.salesUnit = salesUnit;
	}
	public DateTime getRequiredDeliveryDate() {
		return requiredDeliveryDate;
	}
	public void setRequiredDeliveryDate(DateTime requiredDeliveryDate) {
		this.requiredDeliveryDate = requiredDeliveryDate;
	}
	public String getReasonCode() {
		return reasonCode;
	}
	public void setReasonCode(String reasonCode) {
		this.reasonCode = reasonCode;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	public String getBundle() {
		return bundle;
	}
	public void setBundle(String bundle) {
		this.bundle = bundle;
	}
	public String getOperationCode() {
		return operationCode;
	}
	public void setOperationCode(String operationCode) {
		this.operationCode = operationCode;
	}
	public String getRelationshipArticleID() {
		return relationshipArticleID;
	}
	public void setRelationshipArticleID(String relationshipArticleID) {
		this.relationshipArticleID = relationshipArticleID;
	}
	public String getStorageLocation() {
		return storageLocation;
	}
	public void setStorageLocation(String storageLocation) {
		this.storageLocation = storageLocation;
	}
	public PricingTO getPricingTO() {
		return pricingTO;
	}
	public void setPricingTO(PricingTO pricingTO) {
		this.pricingTO = pricingTO;
	}
	public String getShippingMode() {
		return shippingMode;
	}
	public void setShippingMode(String shippingMode) {
		this.shippingMode = shippingMode;
	}
	public String getPayToOthers() {
		return payToOthers;
	}
	public void setPayToOthers(String payToOthers) {
		this.payToOthers = payToOthers;
	}

	@Override
	public String toString() {
		String order = super.toString() 
				+ "\nDescription: " + description
				+ "\nArticleId: " + articleID
				+ "\nIsThirdParty : " + isThirdParty
				+ "\nVendor : " + vendor 
				+ "\nTransport mode : " + transportMode
				+ "\nGV details : " + giftVoucherDetails
				+ "\nProduct group ID : " + ProductGroupID
				+ "\nCatalog : " + catalog
				+ "\nSales unit : " + salesUnit
				+ "\nRequired delivery date : " + requiredDeliveryDate
				+ "\nReason code : " + reasonCode
				+ "\nNotes : " + notes
				+ "\nBundle : " + bundle
				+ "\nOperation code : " + operationCode
				+ "\nParent Child Relationship ID : " + relationshipArticleID
				+ "\nStorage location : " + storageLocation
				+ "\nShipping mode : " + shippingMode
				+ "\npay to others : " + payToOthers;
		
		if (pricingTO != null) {
			order += "\n" + pricingTO.toString();
		}
		
		return order;
	}
}
