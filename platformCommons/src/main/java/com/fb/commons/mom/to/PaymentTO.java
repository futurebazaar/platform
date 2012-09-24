package com.fb.commons.mom.to;

import java.io.Serializable;
import java.math.BigDecimal;

import org.joda.time.DateTime;

public class PaymentTO implements Serializable {
	private String paymentMode;
	private String paymentGateway;
	private String bank;
	private String merchantID;
	private String transactionID;
	private String pgTransactionID;
	private String RRN;
	private String instrumentNumber;
	private DateTime paymentTime;
	private String authCode;
	private DateTime validTill;
	private PricingTO pricingTO;
	
	public PricingTO getPricingTO() {
		return pricingTO;
	}
	public void setPricingTO(PricingTO pricingTO) {
		this.pricingTO = pricingTO;
	}
	public String getPaymentMode() {
		return paymentMode;
	}
	public void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode;
	}
	public String getPaymentGateway() {
		return paymentGateway;
	}
	public void setPaymentGateway(String paymentGateway) {
		this.paymentGateway = paymentGateway;
	}
	public String getBank() {
		return bank;
	}
	public void setBank(String bank) {
		this.bank = bank;
	}
	public String getMerchantID() {
		return merchantID;
	}
	public void setMerchantID(String merchantID) {
		this.merchantID = merchantID;
	}
	public String getTransactionID() {
		return transactionID;
	}
	public void setTransactionID(String transactionID) {
		this.transactionID = transactionID;
	}
	public String getPgTransactionID() {
		return pgTransactionID;
	}
	public void setPgTransactionID(String pgTransactionID) {
		this.pgTransactionID = pgTransactionID;
	}
	public String getRRN() {
		return RRN;
	}
	public void setRRN(String rRN) {
		RRN = rRN;
	}
	public String getInstrumentNumber() {
		return instrumentNumber;
	}
	public void setInstrumentNumber(String instrumentNumber) {
		this.instrumentNumber = instrumentNumber;
	}
	public DateTime getPaymentTime() {
		return paymentTime;
	}
	public void setPaymentTime(DateTime paymentTime) {
		this.paymentTime = paymentTime;
	}
	public String getAuthCode() {
		return authCode;
	}
	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}
	public DateTime getValidTill() {
		return validTill;
	}
	public void setValidTill(DateTime validTill) {
		this.validTill = validTill;
	}
	
	@Override
	public String toString() {
		return "PaymentTO [paymentMode=" + paymentMode + ", paymentGateway="
				+ paymentGateway + ", bank=" + bank + ", merchantID="
				+ merchantID + ", transactionID=" + transactionID
				+ ", pgTransactionID=" + pgTransactionID + ", RRN=" + RRN
				+ ", instrumentNumber=" + instrumentNumber + ", paymentTime="
				+ paymentTime + ", authCode=" + authCode + ", validTill="
				+ validTill + ", pricingTO=" + pricingTO + "]";
	}
	
}
