package com.fb.platform.payback.to;

public class StoreBurnPointsRequest {
	
	private String txnActionCode;
	private int merchantId;
	private int terminalId;	
	private BurnRequest burnRequest;
	
	public String getTxnActionCode() {
		return txnActionCode;
	}
	public void setTxnActionCode(String txnActionCode) {
		this.txnActionCode = txnActionCode;
	}
	public int getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(int merchantId) {
		this.merchantId = merchantId;
	}
	public int getTerminalId() {
		return terminalId;
	}
	public void setTerminalId(int terminalId) {
		this.terminalId = terminalId;
	}
	public BurnRequest getBurnRequest() {
		return burnRequest;
	}
	public void setBurnRequest(BurnRequest burnRequest) {
		this.burnRequest = burnRequest;
	}
	
}
