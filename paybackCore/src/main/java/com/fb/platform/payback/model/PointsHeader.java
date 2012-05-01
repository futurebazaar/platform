package com.fb.platform.payback.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class PointsHeader implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private long orderId;
	private String transactionId;
	private int partnerMerchantId;
	private int partnerTerminalId;
	private String txnActionCode;
	private String txnClassificationCode;
	private String txnPaymentType;
	private String txnDate; // No Need to set type as java Date as it is passed as it is
	private String settlementDate;
	private String txnTimestamp;
	private int txnValue;
	private String marketingCode;
	private String branchId;
	private int txnPoints;
	private String reason;
	private BigDecimal burnRatio;
	private BigDecimal earnRatio;
	
	
	public String getTxnActionCode() {
		return txnActionCode;
	}
	public void setTxnActionCode(String txnActionCode) {
		this.txnActionCode = txnActionCode;
	}
	public String getTxnClassificationCode() {
		return txnClassificationCode;
	}
	public void setTxnClassificationCode(String txnClassificationCode) {
		this.txnClassificationCode = txnClassificationCode;
	}
	public String getTxnPaymentType() {
		return txnPaymentType;
	}
	public void setTxnPaymentType(String txnPaymentType) {
		this.txnPaymentType = txnPaymentType;
	}
	public String getTxnTimestamp() {
		return txnTimestamp;
	}
	public void setTxnTimestamp(String txnTimestamp) {
		this.txnTimestamp = txnTimestamp;
	}
	public String getMarketingCode() {
		return marketingCode;
	}
	public void setMarketingCode(String marketingCode) {
		this.marketingCode = marketingCode;
	}
	public String getBranchId() {
		return branchId;
	}
	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}
	public BigDecimal getBurnRatio() {
		return burnRatio;
	}
	public void setBurnRatio(BigDecimal burnRatio) {
		this.burnRatio = burnRatio;
	}
	public BigDecimal getEarnRatio() {
		return earnRatio;
	}
	public void setEarnRatio(BigDecimal earnRatio) {
		this.earnRatio = earnRatio;
	}
	public long getOrderId() {
		return orderId;
	}
	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}
	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	public int getPartnerMerchantId() {
		return partnerMerchantId;
	}
	public void setPartnerMerchantId(int partnerMerchantId) {
		this.partnerMerchantId = partnerMerchantId;
	}
	public int getPartnerTerminalId() {
		return partnerTerminalId;
	}
	public void setPartnerTerminalId(int partnerTerminalId) {
		this.partnerTerminalId = partnerTerminalId;
	}
	public String getTxnDate() {
		return txnDate;
	}
	public void setTxnDate(String txnDate) {
		this.txnDate = txnDate;
	}
	public String getSettlementDate() {
		return settlementDate;
	}
	public void setSettlementDate(String settlementDate) {
		this.settlementDate = settlementDate;
	}
	public int getTxnValue() {
		return txnValue;
	}
	public void setTxnValue(int txnValue) {
		this.txnValue = txnValue;
	}
	public int getTxnPoints() {
		return txnPoints;
	}
	public void setTxnPoints(int txnPoints) {
		this.txnPoints = txnPoints;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	

}
