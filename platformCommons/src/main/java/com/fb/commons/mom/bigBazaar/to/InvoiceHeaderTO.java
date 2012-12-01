/**
 * 
 */
package com.fb.commons.mom.bigBazaar.to;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.fb.commons.to.Money;

/**
 * @author nehaga
 *
 */
public class InvoiceHeaderTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1441885525557949447L;
	
	private String currency;
	private String localCurrency;
	private Money exchangeRate;
	private String paymentKey;
	private String documentType;
	private String invoiceNumber;
	private String weightUnit;
	private String invoiceType;
	private int receiptNum;
	private String billingCategory;
	private int segment;
	
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getLocalCurrency() {
		return localCurrency;
	}
	public void setLocalCurrency(String localCurrency) {
		this.localCurrency = localCurrency;
	}
	public Money getExchangeRate() {
		return exchangeRate;
	}
	public void setExchangeRate(Money exchangeRate) {
		this.exchangeRate = exchangeRate;
	}
	public String getPaymentKey() {
		return paymentKey;
	}
	public void setPaymentKey(String paymentKey) {
		this.paymentKey = paymentKey;
	}
	public String getDocumentType() {
		return documentType;
	}
	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}
	public String getInvoiceNumber() {
		return invoiceNumber;
	}
	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}
	public String getWeightUnit() {
		return weightUnit;
	}
	public void setWeightUnit(String weightUnit) {
		this.weightUnit = weightUnit;
	}
	public String getInvoiceType() {
		return invoiceType;
	}
	public void setInvoiceType(String invoiceType) {
		this.invoiceType = invoiceType;
	}
	public int getReceiptNum() {
		return receiptNum;
	}
	public void setReceiptNum(int receiptNum) {
		this.receiptNum = receiptNum;
	}
	public String getBillingCategory() {
		return billingCategory;
	}
	public void setBillingCategory(String billingCategory) {
		this.billingCategory = billingCategory;
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
			.append("currency", this.currency)
			.append("localCurrency", this.localCurrency)
			.append("exchangeRate", this.exchangeRate)
			.append("paymentKey", this.paymentKey)
			.append("documentType", this.documentType)
			.append("invoiceNumber", this.invoiceNumber)
			.append("weightUnit", this.weightUnit)
			.append("invoiceType", this.invoiceType)
			.append("receiptNum", this.receiptNum)
			.append("billingCategory", this.billingCategory)
			.append("segment", this.segment)
			.toString();
	}
}
