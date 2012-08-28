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
	
	@Override
	public String toString() {
		String details = "\nReference Order ID: " + referenceID
				+ "\nThird party order no.: " + thirdPartyOrder
				+ "\nCreated date: " + createdOn
				+ "\nOrder type: " + type
				+ "\nBP no.: " + accountNumber
				+ "\nSales document type: " + salesDocType
				+ "\nSales channel: " + salesChannel
				+ "\nSales Organization: " + salesOrganization
				+ "\nIsThirdparty: " + isThirdParty
				+ "\nReason Code: " + reasonCode
				+ "\nClient: " + client
				+ "\nChannel Type: " + channelType
				+ "\nSubmitted On: " + submittedOn;
		
		if (pricingTO != null) {
				details += pricingTO.toString();
		}
		
		return details;
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

}
