package com.fb.platform.wallet.manager.model.access;

public class RefundResponse {

	private String sessionToken;
	private RefundStatusEnum status;

	public String getSessionToken(){
		return sessionToken;
	}
	
	public void setSessionToken(String sessionToken){
		this.sessionToken = sessionToken;
	}
	
	public RefundStatusEnum getStatus(){
		return status;
	}
	
	public void setStatus(RefundStatusEnum status){
		this.status = status;
	}

}
