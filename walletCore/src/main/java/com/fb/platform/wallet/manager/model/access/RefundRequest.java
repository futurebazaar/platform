package com.fb.platform.wallet.manager.model.access;

import java.math.BigDecimal;

public class RefundRequest {

	private String sessionToken;
	private long userId;
	private long clientId;
	private BigDecimal amount;
	private boolean ignoreExpiry;

	public String getSessionToken() {
		return sessionToken;
	}

	public void setSessionToken(String sessionToken) {
		this.sessionToken = sessionToken;
	}

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
	
	public boolean getIgnoreExpiry(){
		return ignoreExpiry;
	}
	
	public void setIgnoreExpiry(boolean ignoreExpiry){
		this.ignoreExpiry = ignoreExpiry;
	}

}
