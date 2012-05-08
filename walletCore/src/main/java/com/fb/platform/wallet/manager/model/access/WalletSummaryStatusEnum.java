package com.fb.platform.wallet.manager.model.access;

public enum WalletSummaryStatusEnum {
	
	SUCCESS("SUCCESS"),
    INVALID_USER("INVALID USER"),
    NO_SESSION("NO SESSION"),
	ERROR_RETRIVING_WALLET_SUMMARY("ERROR RETRIVING WALLET SUMMARY");	
	
	 private String walletSummaryStatus = null;
	 
	 private WalletSummaryStatusEnum(String walletSummaryStatus) {
		this.walletSummaryStatus = walletSummaryStatus;
	 }
	 
	 @Override
	public String toString() {
		return this.walletSummaryStatus;
	}

}
