package com.fb.platform.wallet.manager.model.access;


/**
 * 
 * @author kaushik
 *
 */

public class WalletSummaryRequest{

	private int userId;
	private int clientId;
	private String sessionToken;
	
	public int getUserId(){
		return userId;
	}
	
	public void setUserId(int userId){
		this.userId = userId;
	}
	
	public int getClientId(){
		return clientId;
	}
	
	public void setClientId(int clientId){
		this.clientId = clientId;				
	}
	
	public String getSessionToken(){
		return sessionToken;
	}
	public void setSessionToken(String sessionToken){
		this.sessionToken = sessionToken;
	}
}
