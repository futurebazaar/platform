/**
 * 
 */
package com.fb.commons.mom.bigBazaar.to;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.fb.commons.mom.to.AddressTO;

/**
 * @author nehaga
 *
 */
public class InvoicePartnerHeaderTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5247788569250638879L;
	private String partnerFunction;
	private String customerLocVendorNum;
	private AddressTO address;
	private String languageKey;
	private String region;
	private String idocUserName;
	private String idocOrgCode;
	private int segment;
	
	public String getPartnerFunction() {
		return partnerFunction;
	}
	public void setPartnerFunction(String partnerFunction) {
		this.partnerFunction = partnerFunction;
	}
	public String getCustomerLocVendorNum() {
		return customerLocVendorNum;
	}
	public void setCustomerLocVendorNum(String customerLocVendorNum) {
		this.customerLocVendorNum = customerLocVendorNum;
	}
	public AddressTO getAddress() {
		return address;
	}
	public void setAddress(AddressTO address) {
		this.address = address;
	}
	public String getLanguageKey() {
		return languageKey;
	}
	public void setLanguageKey(String languageKey) {
		this.languageKey = languageKey;
	}
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}
	public String getIdocUserName() {
		return idocUserName;
	}
	public void setIdocUserName(String idocUserName) {
		this.idocUserName = idocUserName;
	}
	public String getIdocOrgCode() {
		return idocOrgCode;
	}
	public void setIdocOrgCode(String idocOrgCode) {
		this.idocOrgCode = idocOrgCode;
	}
	public int getSegment() {
		return segment;
	}
	public void setSegment(int segment) {
		this.segment = segment;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
			.append("partnerFunction", this.partnerFunction)
			.append("customerLocVendorNum", this.customerLocVendorNum)
			.append("address", this.address)
			.append("languageKey", this.languageKey)
			.append("region", this.region)
			.append("idocUserName", this.idocUserName)
			.append("idocOrgCode", this.idocOrgCode)
			.append("segmentNum", this.segment)
			.toString();
	}
}
