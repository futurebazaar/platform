package com.fb.platform.wallet.model;

import java.io.Serializable;
import org.joda.time.DateTime;

import com.fb.commons.to.Money;
import com.fb.platform.user.manager.model.admin.User;
import com.fb.platform.wallet.to.CreditWalletStatus;
import com.fb.platform.wallet.to.DebitWalletStatus;
import com.fb.platform.wallet.to.RefundWalletStatus;

public class Wallet implements Serializable {
	
	private int id;
	private CashSubWallet cashSubWallet;
	private GiftSubWallet giftSubWallet;
	private RefundSubWallet refundSubWallet;
	private Money totalAmount;
	private DateTime createdOn;
	private DateTime modifiedOn;
	private User user;
	
	
	public CreditWalletStatus Credit(Money amount,SubWalletType subWalletType){
		if (amount.isPlus()){
			totalAmount = totalAmount.plus(amount);
			if(subWalletType.equals(SubWalletType.CASH_SUB_WALLET)){
				cashSubWallet.amount = cashSubWallet.amount.plus(amount);		
			}else if (subWalletType.equals(SubWalletType.GIFT_SUB_WALLET)){
				giftSubWallet.amount = giftSubWallet.amount.plus(amount);				
			}else if (subWalletType.equals(SubWalletType.REFUND_SUB_WALLET)){
				refundSubWallet.amount = refundSubWallet.amount.plus(amount);				
			}else{
				return CreditWalletStatus.INVALID_SUBWALLET;
			}
			return CreditWalletStatus.SUCCESS;
		}else{
			return CreditWalletStatus.ZERO_AMOUNT;
		}
	}
	public DebitWalletStatus Debit(Money amount){
		if(this.totalAmount.lt(amount)){
			return DebitWalletStatus.INSUFFICIENT_FUND;
		}
		return null;
	}
	
	public RefundWalletStatus Refund(double amount){
		return null;
	}
	
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * @return the cashSubWallet
	 */
	public CashSubWallet getCashSubWallet() {
		return cashSubWallet;
	}
	/**
	 * @param cashSubWallet the cashSubWallet to set
	 */
	public void setCashSubWallet(CashSubWallet cashSubWallet) {
		this.cashSubWallet = cashSubWallet;
	}
	/**
	 * @return the giftSubWallet
	 */
	public GiftSubWallet getGiftSubWallet() {
		return giftSubWallet;
	}
	/**
	 * @param giftSubWallet the giftSubWallet to set
	 */
	public void setGiftSubWallet(GiftSubWallet giftSubWallet) {
		this.giftSubWallet = giftSubWallet;
	}
	/**
	 * @return the refundSubWallet
	 */
	public RefundSubWallet getRefundSubWallet() {
		return refundSubWallet;
	}
	/**
	 * @param refundSubWallet the refundSubWallet to set
	 */
	public void setRefundSubWallet(RefundSubWallet refundSubWallet) {
		this.refundSubWallet = refundSubWallet;
	}
	/**
	 * @return the user
	 */
	public User getUser() {
		return user;
	}
	/**
	 * @param user the user to set
	 */
	public void setUser(User user) {
		this.user = user;
	}
	/**
	 * @return the createdOn
	 */
	public DateTime getCreatedOn() {
		return createdOn;
	}
	/**
	 * @param createdOn the createdOn to set
	 */
	public void setCreatedOn(DateTime createdOn) {
		this.createdOn = createdOn;
	}
	/**
	 * @return the modifiedOn
	 */
	public DateTime getModifiedOn() {
		return modifiedOn;
	}
	/**
	 * @param modifiedOn the modifiedOn to set
	 */
	public void setModifiedOn(DateTime modifiedOn) {
		this.modifiedOn = modifiedOn;
	}
	/**
	 * @return the totalAmount
	 */
	public Money getTotalAmount() {
		return totalAmount;
	}
	/**
	 * @param totalAmount the totalAmount to set
	 */
	public void setTotalAmount(Money totalAmount) {
		this.totalAmount = totalAmount;
	}
	
	
	
}
