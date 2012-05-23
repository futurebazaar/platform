package com.fb.platform.wallet.manager.model.access;

import org.joda.time.DateTime;

public class WalletHistoryRequest {

	private long walletId;
	private String sessionToken;
	private SubWalletEnum subWallet;
	private DateTime fromDate;
	private DateTime toDate;

	public long getWalletId() {
		return walletId;
	}

	public void setWalletId(long walletId) {
		this.walletId = walletId;
	}

	public String getSessionToken() {
		return sessionToken;
	}

	public void setSessionToken(String sessionToken) {
		this.sessionToken = sessionToken;
	}
	
	public SubWalletEnum getSubWallet(){
		return subWallet;
	}

	public void setSubWallet(SubWalletEnum subWallet){
		this.subWallet = subWallet;
	}
	
	public DateTime getFromDate(){
		return fromDate;
	}
	
	public void setFromDate(DateTime fromDate){
		this.fromDate = fromDate;
	}

	public DateTime getToDate(){
		return toDate;
	}
	
	public void setToDate(DateTime toDate){
		this.toDate = toDate;
	}

}
