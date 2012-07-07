package com.fb.platform.wallet.manager.model.access;

/**
 * 
 * @author kaushik
 * 
 */

public class WalletSummaryResponse {

	private String sessionToken;
	private WalletSummaryStatusEnum walletSummaryStatus;
	private WalletDetails walletDetails = null;

	public String getSessionToken() {
		return sessionToken;
	}

	public void setSessionToken(String sessionToken) {
		this.sessionToken = sessionToken;
	}

	public WalletSummaryStatusEnum getWalletSummaryStatus() {
		return walletSummaryStatus;
	}

	public void setWalletSummaryStatus(
			WalletSummaryStatusEnum walletSummaryStatus) {
		this.walletSummaryStatus = walletSummaryStatus;
	}

	public WalletDetails getWalletDetails() {
		return walletDetails;
	}

	public void setWalletDetails(
			WalletDetails walletDetails) {
		this.walletDetails = walletDetails;
	}
}
