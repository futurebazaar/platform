package com.fb.platform.payback.model;

import java.io.Serializable;

public class PointsItems implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String orderId;
	private String referenceId;
	private String loyaltyCard;
	private String PartnerMerchantId;
	private String PartnerTerminalId;
	private String txnActionCode;
	private String txnClassificationCode;
	private String txnPaymentType;
	private String txnDate; // No Need to set type as java Date as it is passed as it is
	private String settlementDate;
	private int txnValue;
	private String marketingCode;
	private String branchId;
	private int txnPoints;
	private String txnTimestamp;
	private String reason;
	private String departmentName;
	private String articleId;
	private int quantity;
	private String departmentCode;
	private int itemAmount;
	private String itemId;
	private String pointsHeaderId;
	
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
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
	public String getTxnTimestamp() {
		return txnTimestamp;
	}
	public void setTxnTimestamp(String txnTimestamp) {
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
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	
	public String getPointsHeaderId() {
		return pointsHeaderId;
	}
	public void setPointsHeaderId(String pointsHeaderId) {
		this.pointsHeaderId = pointsHeaderId;
	}
}
