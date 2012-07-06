package com.fb.platform.wallet.manager.model.access;

import java.math.BigDecimal;
import org.joda.time.DateTime;

public class FillWalletRequest {
	
	private long userId;
	private long clientId;
	private String sessionToken;
	private BigDecimal amount;
	private SubWalletEnum subwallet;
	private long paymentId;
	private long refundId;
	private String giftCode;
	private DateTime expiryDate;
	private boolean isEgv;
	
	public long getUserId(){
		return userId;
	}

	public void setUserId(long userId){
		this.userId = userId;
	}

	public long getClientId(){
		return clientId;
	}

	public void setClientId(long clientId){
		this.clientId = clientId;
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

	public String getGiftCode(){
		return giftCode;
	}

	public void setGiftCode(String giftCode){
		this.giftCode = giftCode;
	}

	public DateTime getExpiryDate(){
		return expiryDate;
	}

	public void setExpiryDate(DateTime expiryDate){
		this.expiryDate = expiryDate;
	}

	public boolean getIsEgv(){
		return isEgv;
	}

	public void setIsEgv(boolean isEgv){
		this.isEgv = isEgv;
	}

}
