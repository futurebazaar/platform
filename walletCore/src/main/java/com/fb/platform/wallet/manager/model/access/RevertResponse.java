package com.fb.platform.wallet.manager.model.access;

import com.fb.platform.wallet.to.WalletTransaction;

public class RevertResponse {

	private String sessionToken;
	private WalletTransaction walletTransaction;
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

	/**
	 * @return the walletTransaction
	 */
	public WalletTransaction getWalletTransaction() {
		return walletTransaction;
	}

	/**
	 * @param walletTransaction the walletTransaction to set
	 */
	public void setWalletTransaction(WalletTransaction walletTransaction) {
		this.walletTransaction = walletTransaction;
	}
	

}
