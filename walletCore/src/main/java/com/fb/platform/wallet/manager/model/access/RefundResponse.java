package com.fb.platform.wallet.manager.model.access;

public class RefundResponse {

	private String sessionToken = null;
	private String transactionId = null;
	private RefundStatusEnum status = null;

	public String getSessionToken(){
		return sessionToken;
	}
	
	public void setSessionToken(String sessionToken){
		this.sessionToken = sessionToken;
	}

	public String getTransactionId(){
		return transactionId;
	}

	public void setTransactionId(String transactionId){
		this.transactionId = transactionId;
	}

	public RefundStatusEnum getStatus(){
		return status;
	}
	
	public void setStatus(RefundStatusEnum status){
		this.status = status;
	}

}
