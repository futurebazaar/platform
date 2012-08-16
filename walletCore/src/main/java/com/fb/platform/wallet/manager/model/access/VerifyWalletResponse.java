package com.fb.platform.wallet.manager.model.access;

import java.math.BigDecimal;

public class VerifyWalletResponse {
	
	String sessionToken;
	VerifyWalletStatusEnum status;
	private BigDecimal amount;
	/**
	 * @return the sessionToken
	 */
	public String getSessionToken() {
		return sessionToken;
	}
	/**
	 * @param sessionToken the sessionToken to set
	 */
	public void setSessionToken(String sessionToken) {
		this.sessionToken = sessionToken;
	}
	/**
	 * @return the status
	 */
	public VerifyWalletStatusEnum getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(VerifyWalletStatusEnum status) {
		this.status = status;
	}
	/**
	 * @return the amount
	 */
	public BigDecimal getAmount() {
		return amount;
	}
	/**
	 * @param amount the amount to set
	 */
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	
	
	
}
