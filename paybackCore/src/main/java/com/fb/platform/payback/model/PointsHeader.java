package com.fb.platform.payback.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Properties;
import org.joda.time.DateTime;

import com.fb.platform.payback.util.PointsTxnClassificationCodeEnum;

public class PointsHeader implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private long orderId;
	private String transactionId;
	private String partnerMerchantId;
	private String partnerTerminalId;
	private String txnActionCode;
	private String txnClassificationCode;
	private String txnPaymentType;
	private DateTime txnDate; // No Need to set type as java Date as it is passed as it is
	private DateTime settlementDate;
	private DateTime txnTimestamp;
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
	public DateTime getTxnTimestamp() {
		return txnTimestamp;
	}
	public void setTxnTimestamp(DateTime txnTimestamp) {
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
	public String getPartnerMerchantId() {
		return partnerMerchantId;
	}
	public void setPartnerMerchantId(String partnerMerchantId) {
		this.partnerMerchantId = partnerMerchantId;
	}
	public String getPartnerTerminalId() {
		return partnerTerminalId;
	}
	public void setPartnerTerminalId(String partnerTerminalId) {
		this.partnerTerminalId = partnerTerminalId;
	}
	public DateTime getTxnDate() {
		return txnDate;
	}
	public void setTxnDate(DateTime txnDate) {
		this.txnDate = txnDate;
	}
	public DateTime getSettlementDate() {
		return settlementDate;
	}
	public void setSettlementDate(DateTime settlementDate) {
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
	public void setOrderDetails(OrderDetail orderDetail, Properties props) {
		String clientName = orderDetail.getClientName().toUpperCase(); 
		String branchId = props.getProperty(clientName + "_BRANCH_ID");
		String marketingCode = props.getProperty(clientName + "_MARKETING_CODE");
		String [] partnerIds = props.getProperty(clientName + "_IDS").split(",");
		String merchantId = partnerIds[0];
		String terminalId = partnerIds[1];
		this.setPartnerMerchantId(merchantId);
		this.setPartnerTerminalId(terminalId);
		this.setBranchId(branchId);
		this.setMarketingCode(marketingCode);
		
	}
	
	public void setTxnDetails(PaymentDetail paymentDetail, Properties props, String txnActionCode){
		this.setTransactionId(paymentDetail.getTransactionId());
		this.setTxnDate(paymentDetail.getTxnDate());
		String [] txnCodes = PointsTxnClassificationCodeEnum.valueOf(txnActionCode).toString().split(",");
		this.setTxnClassificationCode(txnCodes[0]);
		this.setTxnPaymentType(txnCodes[1]);
		this.setTxnTimestamp(paymentDetail.getTxnDate());
		
	}
	

}
