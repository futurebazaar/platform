package com.fb.platform.wallet.manager.model.access;

import java.math.BigDecimal;

public class FillWalletRequest {
	
	private long walletId;
	private String sessionToken;
	private BigDecimal amount;
	private SubWalletEnum subwallet;
	private long paymentId;
	private long refundId;
	
	public long getWalletId(){
		return walletId;
	}

	public void setWalletId(long walletId){
		this.walletId = walletId;
	}
	
	public String getSessionToken(){
		return sessionToken;
	}
	
	public void setSessionToken(String sessionToken){
		this.sessionToken = sessionToken;
	}
	
	public BigDecimal getAmount(){
		return this.amount;
	}
	
	public void setAmount(BigDecimal amount){
		this.amount = amount;
	}
	
	public SubWalletEnum getSubWallet(){
		return subwallet;
	}
	
	public void setSubWallet(SubWalletEnum subWallet){
		this.subwallet = subWallet;
	}

	public long getPaymentId(){
		return paymentId;
	}

	public void setPaymentId(long paymentId){
		this.paymentId = paymentId;
	}
	public long getRefundId(){
		return refundId;
	}

	public void setRefundId(long refundId){
		this.refundId = refundId;
	}

}
