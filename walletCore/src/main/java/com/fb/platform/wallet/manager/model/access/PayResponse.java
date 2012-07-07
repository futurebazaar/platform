package com.fb.platform.wallet.manager.model.access;

public class PayResponse {
	
	private String sessionToken;
	private String transactionId;
	private PayStatusEnum status;

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
	
	public PayStatusEnum getStatus(){
		return status;
	}
	
	public void setStatus(PayStatusEnum status){
		this.status = status;
	}

}
