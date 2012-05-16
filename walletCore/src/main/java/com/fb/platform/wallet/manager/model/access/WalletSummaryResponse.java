package com.fb.platform.wallet.manager.model.access;

/**
 * 
 * @author kaushik
 * 
 */

public class WalletSummaryResponse {

	private String sessionToken;
	private WalletSummaryStatusEnum walletSummaryStatus;
	private WalletSummaryDetails walletSummaryDetails = null;

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

	public WalletSummaryDetails getWalletSummaryDetails() {
		return walletSummaryDetails;
	}

	public void setWalletSummaryDetails(
			WalletSummaryDetails walletSummaryDetails) {
		this.walletSummaryDetails = walletSummaryDetails;
	}
}
