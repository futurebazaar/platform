package com.fb.platform.wallet.manager.model.access;

import java.math.BigDecimal;

public class RevertRequest {

	private long userId;
	private long clientId;
	private BigDecimal amount;
	private String sessionToken;
	private String transactionIdToRevert;
	private long newRefundId;

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public long getClientId() {
		return clientId;
	}

	public void setClientId(long clientId) {
		this.clientId = clientId;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getSessionToken() {
		return sessionToken;
	}

	public void setSessionToken(String sessionToken) {
		this.sessionToken = sessionToken;
	}
	
	public String getTransactionIdToRevert() {
		return transactionIdToRevert;
	}

	public void setTransactionIdToRevert(String transactionIdToRevert) {
		this.transactionIdToRevert = transactionIdToRevert;
	}

	/**
	 * @return the newRefundId
	 */
	public long getNewRefundId() {
		return newRefundId;
	}

	/**
	 * @param newRefundId the newRefundId to set
	 */
	public void setNewRefundId(long newRefundId) {
		this.newRefundId = newRefundId;
	}
	

}
