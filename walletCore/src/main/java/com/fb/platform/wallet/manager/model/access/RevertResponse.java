package com.fb.platform.wallet.manager.model.access;

public class RevertResponse {

	private String sessionToken;
	private RevertStatusEnum status;

	public String getSessionToken(){
		return sessionToken;
	}
	
	public void setSessionToken(String sessionToken){
		this.sessionToken = sessionToken;
	}
	
	public RevertStatusEnum getStatus(){
		return status;
	}
	
	public void setStatus(RevertStatusEnum status){
		this.status = status;
	}

}
