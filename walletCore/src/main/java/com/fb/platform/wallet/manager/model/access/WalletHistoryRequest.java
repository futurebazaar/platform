package com.fb.platform.wallet.manager.model.access;

import org.joda.time.DateTime;

public class WalletHistoryRequest {

	private long walletId;
	private String sessionToken;
	private SubWalletEnum subWallet;
	private DateTime fromDate;
	private DateTime toDate;
	private int pageNumber;
	private int resultsPerPage;
	private Long userId;
	private Long clientId;

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

	/**
	 * @return the pageNumber
	 */
	public int getPageNumber() {
		return pageNumber;
	}

	/**
	 * @param pageNumber the pageNumber to set
	 */
	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	/**
	 * @return the resultsPerPage
	 */
	public int getResultsPerPage() {
		return resultsPerPage;
	}

	/**
	 * @param resultsPerPage the resultsPerPage to set
	 */
	public void setResultsPerPage(int resultsPerPage) {
		this.resultsPerPage = resultsPerPage;
	}

	/**
	 * @return the userId
	 */
	public Long getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(long userId) {
		this.userId = new Long(userId);
	}

	/**
	 * @return the clientId
	 */
	public Long getClientId() {
		return clientId;
	}

	/**
	 * @param clientId the clientId to set
	 */
	public void setClientId(long clientId) {
		this.clientId = new Long(clientId);
	}
	

}
