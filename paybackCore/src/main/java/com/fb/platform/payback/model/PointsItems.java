package com.fb.platform.payback.model;

import java.io.Serializable;

import org.joda.time.DateTime;

public class PointsItems implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private long orderId;
	private String referenceId;
	private String loyaltyCard;
	private String PartnerMerchantId;
	private String PartnerTerminalId;
	private String txnActionCode;
	private String txnClassificationCode;
	private String txnPaymentType;
	private DateTime txnDate; // No Need to set type as java Date as it is passed as it is
	private DateTime settlementDate;
	private int txnValue;
	private String marketingCode;
	private String branchId;
	private int txnPoints;
	private DateTime txnTimestamp;
	private String reason;
	private String departmentName;
	private String articleId;
	private int quantity;
	private String departmentCode;
	private int itemAmount;
	private long itemId;
	private long pointsHeaderId;
	
	public long getOrderId() {
		return orderId;
	}
	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}
	public String getReferenceId() {
		return referenceId;
	}
	public void setReferenceId(String referenceId) {
		this.referenceId = referenceId;
	}
	public String getLoyaltyCard() {
		return loyaltyCard;
	}
	public void setLoyaltyCard(String loyaltyCard) {
		this.loyaltyCard = loyaltyCard;
	}
	public String getPartnerMerchantId() {
		return PartnerMerchantId;
	}
	public void setPartnerMerchantId(String partnerMerchantId) {
		PartnerMerchantId = partnerMerchantId;
	}
	public String getPartnerTerminalId() {
		return PartnerTerminalId;
	}
	public void setPartnerTerminalId(String partnerTerminalId) {
		PartnerTerminalId = partnerTerminalId;
	}
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
	public int getTxnPoints() {
		return txnPoints;
	}
	public void setTxnPoints(int txnPoints) {
		this.txnPoints = txnPoints;
	}
	public DateTime getTxnTimestamp() {
		return txnTimestamp;
	}
	public void setTxnTimestamp(DateTime txnTimestamp) {
		this.txnTimestamp = txnTimestamp;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getDepartmentName() {
		return departmentName;
	}
	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}
	public String getArticleId() {
		return articleId;
	}
	public void setArticleId(String articleId) {
		this.articleId = articleId;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public String getDepartmentCode() {
		return departmentCode;
	}
	public void setDepartmentCode(String departmentCode) {
		this.departmentCode = departmentCode;
	}
	public int getItemAmount() {
		return itemAmount;
	}
	public void setItemAmount(int itemAmount) {
		this.itemAmount = itemAmount;
	}
	public long getItemId() {
		return itemId;
	}
	public void setItemId(long itemId) {
		this.itemId = itemId;
	}
	
	public long getPointsHeaderId() {
		return pointsHeaderId;
	}
	public void setPointsHeaderId(long pointsHeaderId) {
		this.pointsHeaderId = pointsHeaderId;
	}
}
