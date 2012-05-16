package com.fb.platform.wallet.manager.model.access;

public class FillWalletResponse {
	
	private String sessionToken;
	private long walletId;
	private FillWalletStatusEnum status;

	public String getSessionToken(){
		return sessionToken;
	}
	
	public void setSessionToken(String sessionToken){
		this.sessionToken = sessionToken;
	}

	public long getWalletId(){
		return walletId;
	}

	public void setWalletId(long walletId){
		this.walletId = walletId;
	}
	
	public FillWalletStatusEnum getStatus(){
		return status;
	}
	
	public void setStatus(FillWalletStatusEnum status){
		this.status = status;
	}

}
