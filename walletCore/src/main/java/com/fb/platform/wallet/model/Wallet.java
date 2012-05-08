package com.fb.platform.wallet.model;

import java.io.Serializable;
import java.util.Date;

import com.fb.platform.user.manager.model.address.DeleteAddressStatusEnum;
import com.fb.platform.user.manager.model.admin.User;
import com.fb.platform.wallet.to.CreditWalletStatus;
import com.fb.platform.wallet.to.DebitWalletStatus;
import com.fb.platform.wallet.to.RefundWalletStatus;

public class Wallet implements Serializable {
	
	private int id;
	private CashSubWallet cashSubWallet;
	private GiftSubWallet giftSubWallet;
	private RefundSubWallet refundSubWallet;
	private Double totalAmount;
	private Date createdOn;
	private Date modifiedOn;
	private User user;
	
	
	public CreditWalletStatus Credit(double amount,SubWalletType subWalletType,long paymentId){
		return null;
	}
	public DebitWalletStatus Debit(double amount,long orderId){
		if(this.totalAmount < amount){
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
	public Date getCreatedOn() {
		return createdOn;
	}
	/**
	 * @param createdOn the createdOn to set
	 */
	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}
	/**
	 * @return the modifiedOn
	 */
	public Date getModifiedOn() {
		return modifiedOn;
	}
	/**
	 * @param modifiedOn the modifiedOn to set
	 */
	public void setModifiedOn(Date modifiedOn) {
		this.modifiedOn = modifiedOn;
	}
	/**
	 * @return the totalAmount
	 */
	public Double getTotalAmount() {
		return totalAmount;
	}
	/**
	 * @param totalAmount the totalAmount to set
	 */
	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}
	
	
	
}
