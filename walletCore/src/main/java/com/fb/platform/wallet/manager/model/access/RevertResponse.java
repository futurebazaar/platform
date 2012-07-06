package com.fb.platform.wallet.manager.model.access;

public class RevertResponse {

	private String sessionToken;
	private String transactionId;
	private RevertStatusEnum status;

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

	public RevertStatusEnum getStatus(){
		return status;
	}
	
	public void setStatus(RevertStatusEnum status){
		this.status = status;
	}

}
