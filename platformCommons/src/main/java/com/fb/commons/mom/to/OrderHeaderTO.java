package com.fb.commons.mom.to;

import java.io.Serializable;

import org.joda.time.DateTime;

public class OrderHeaderTO implements Serializable {
	
	private String referenceID;
	private String thirdPartyOrder;
	private DateTime createdOn;
	private String type;
	private String accountNumber;
	private String salesDocType;
	private String salesChannel;
	private String salesOrganization;
	private boolean isThirdParty;
	private String reasonCode;
	private String client;
	private String channelType;
	private DateTime submittedOn;	
	private PricingTO pricingTO;
	private String loyaltyCardNumber;
	private String returnOrderID;	
	
	public PricingTO getPricingTO() {
		return pricingTO;
	}
	public void setPricingTO(PricingTO pricingTO) {
		this.pricingTO = pricingTO;
	}
	public String getReferenceID() {
		return referenceID;
	}
	public void setReferenceID(String referenceID) {
		this.referenceID = referenceID;
	}
	public String getThirdPartyOrder() {
		return thirdPartyOrder;
	}
	public void setThirdPartyOrder(String thirdPartyOrder) {
		this.thirdPartyOrder = thirdPartyOrder;
	}
	public DateTime getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(DateTime createdOn) {
		this.createdOn = createdOn;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	public String getSalesDocType() {
		return salesDocType;
	}
	public void setSalesDocType(String salesDocType) {
		this.salesDocType = salesDocType;
	}
	public String getSalesChannel() {
		return salesChannel;
	}
	public void setSalesChannel(String salesChannel) {
		this.salesChannel = salesChannel;
	}
	public String getSalesOrganization() {
		return salesOrganization;
	}
	public void setSalesOrganization(String salesOrganization) {
		this.salesOrganization = salesOrganization;
	}
	public boolean isThirdParty() {
		return isThirdParty;
	}
	public void setThirdParty(boolean isThirdParty) {
		this.isThirdParty = isThirdParty;
	}
	public String getReasonCode() {
		return reasonCode;
	}
	public void setReason(String reasonCode) {
		this.reasonCode = reasonCode;
	}
	public String getClient() {
		return client;
	}
	public void setClient(String client) {
		this.client = client;
	}
	public String getChannelType() {
		return channelType;
	}
	public void setChannelType(String channelType) {
		this.channelType = channelType;
	}
	public DateTime getSubmittedOn() {
		return submittedOn;
	}
	public void setSubmittedOn(DateTime submittedOn) {
		this.submittedOn = submittedOn;
	}
	public String getLoyaltyCardNumber() {
		return loyaltyCardNumber;
	}
	public void setLoyaltyCardNumber(String loyaltyCardNumber) {
		this.loyaltyCardNumber = loyaltyCardNumber;
	}
	public String getReturnOrderID() {
		return returnOrderID;
	}
	public void setReturnOrderID(String returnOrderID) {
		this.returnOrderID = returnOrderID;
	}
	
	@Override
	public String toString() {
		return "OrderHeaderTO [referenceID=" + referenceID
				+ ", thirdPartyOrder=" + thirdPartyOrder + ", createdOn="
				+ createdOn + ", type=" + type + ", accountNumber="
				+ accountNumber + ", salesDocType=" + salesDocType
				+ ", salesChannel=" + salesChannel + ", salesOrganization="
				+ salesOrganization + ", isThirdParty=" + isThirdParty
				+ ", reasonCode=" + reasonCode + ", client=" + client
				+ ", channelType=" + channelType + ", submittedOn="
				+ submittedOn + ", pricingTO=" + pricingTO
				+ ", loyaltyCardNumber=" + loyaltyCardNumber
				+ ", returnOrderID=" + returnOrderID + "]";
	}
	
}
