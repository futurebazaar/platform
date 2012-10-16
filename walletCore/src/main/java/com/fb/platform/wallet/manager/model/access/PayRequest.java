package com.fb.platform.wallet.manager.model.access;

import java.math.BigDecimal;

public class PayRequest {

	private long userId;
	private long clientId;
	private String sessionToken;
	private String walletPassord;
	private long orderId;
	private BigDecimal amount;

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

	public String getSessionToken() {
		return sessionToken;
	}

	public void setSessionToken(String sessionToken) {
		this.sessionToken = sessionToken;
	}
	
	public long getOrderId() {
		return orderId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	/**
	 * @return the walletPassord
	 */
	public String getWalletPassord() {
		return walletPassord;
	}

	/**
	 * @param walletPassord the walletPassord to set
	 */
	public void setWalletPassord(String walletPassord) {
		this.walletPassord = walletPassord;
	}
	

}
