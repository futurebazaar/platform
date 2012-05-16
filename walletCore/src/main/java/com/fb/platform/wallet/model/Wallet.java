package com.fb.platform.wallet.model;

import java.io.Serializable;
import org.joda.time.DateTime;

import com.fb.commons.to.Money;
import com.fb.platform.user.manager.model.admin.User;
import com.fb.platform.wallet.to.CreditWalletStatus;
import com.fb.platform.wallet.to.DebitWalletStatus;
import com.fb.platform.wallet.to.RefundWalletStatus;

public class Wallet implements Serializable {
	
	private long id;
	private Money cashSubWallet;
	private Money giftSubWallet;
	private Money refundSubWallet;
	private Money totalAmount;
	private DateTime createdOn;
	private DateTime modifiedOn;
	private User user;
	
	
	public WalletTransaction Credit(Money amount,SubWalletType subWalletType,String refCode){
		try {
			if (amount.isPlus()) {
				long refundId = 0;
				long paymentId = 0;
				long tansactionReverseId = 0;
				String giftCode = null;
				if (subWalletType.equals(SubWalletType.CASH_SUB_WALLET)) {
					cashSubWallet = cashSubWallet.plus(amount);
					paymentId = Long.parseLong(refCode);
				} else if (subWalletType.equals(SubWalletType.GIFT_SUB_WALLET)) {
					giftSubWallet = giftSubWallet.plus(amount);
					giftCode = refCode;
				} else if (subWalletType
						.equals(SubWalletType.REFUND_SUB_WALLET)) {
					refundSubWallet = refundSubWallet.plus(amount);
					refundId = Long.parseLong(refCode);
				} else {
					return null;
				}
				totalAmount = totalAmount.plus(amount);
				WalletTransaction walletTransaction = new WalletTransaction(
						this, subWalletType, TransactionType.CREDIT, amount, 0,
						refundId, paymentId, tansactionReverseId, giftCode);
				return walletTransaction;
			} else {
				return null;
			}
		} catch (Exception e) {
			return null;
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
	public long getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}
	/**
	 * @return the cashSubWallet
	 */
	public Money getCashSubWallet() {
		return cashSubWallet;
	}
	/**
	 * @param money the cashSubWallet to set
	 */
	public void setCashSubWallet(Money money) {
		this.cashSubWallet = money;
	}
	/**
	 * @return the giftSubWallet
	 */
	public Money getGiftSubWallet() {
		return giftSubWallet;
	}
	/**
	 * @param giftSubWallet the giftSubWallet to set
	 */
	public void setGiftSubWallet(Money giftSubWallet) {
		this.giftSubWallet = giftSubWallet;
	}
	/**
	 * @return the refundSubWallet
	 */
	public Money getRefundSubWallet() {
		return refundSubWallet;
	}
	/**
	 * @param refundSubWallet the refundSubWallet to set
	 */
	public void setRefundSubWallet(Money refundSubWallet) {
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
