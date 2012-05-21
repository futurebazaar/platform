package com.fb.platform.wallet.model;

import java.io.Serializable;
import org.joda.time.DateTime;

import com.fb.commons.PlatformException;
import com.fb.commons.to.Money;
import com.fb.platform.user.manager.model.admin.User;

public class Wallet implements Serializable {
	
	private long id;
	private Money cashSubWallet;
	private Money giftSubWallet;
	private Money refundSubWallet;
	private Money totalAmount;
	private DateTime createdOn;
	private DateTime modifiedOn;
	private User user;

	public WalletTransaction credit(Money amount,SubWalletType subWalletType,long paymentId,long refundId,String giftCode){
		if (subWalletType.equals(SubWalletType.CASH_SUB_WALLET)) {
				cashSubWallet = cashSubWallet.plus(amount);
			} else if (subWalletType.equals(SubWalletType.GIFT_SUB_WALLET)) {
				giftSubWallet = giftSubWallet.plus(amount);
			} else if (subWalletType.equals(SubWalletType.REFUND_SUB_WALLET)) {
				refundSubWallet = refundSubWallet.plus(amount);
			}
			totalAmount = totalAmount.plus(amount);
			WalletTransaction walletTransaction = new WalletTransaction(
					this, TransactionType.CREDIT, amount,DateTime.now());
			walletTransaction.getWalletSubTransaction().add(new WalletSubTransaction(subWalletType, amount, 0, refundId, paymentId,0, giftCode));
			return walletTransaction;
	}
	
	public WalletTransaction reverseTransaction(WalletTransaction walletTransaction) {
		WalletTransaction walletTransactionRes = new WalletTransaction(
				this, TransactionType.CREDIT, walletTransaction.getAmount(),DateTime.now());
		for(WalletSubTransaction walletSubTransaction : walletTransaction.getWalletSubTransaction()){
			if (walletSubTransaction.getSubWalletType().equals(SubWalletType.CASH_SUB_WALLET)) {
				cashSubWallet = cashSubWallet.plus(walletSubTransaction.getAmount());
				walletTransactionRes.getWalletSubTransaction().add(new WalletSubTransaction(SubWalletType.CASH_SUB_WALLET,walletSubTransaction.getAmount(),0,0,0,walletTransaction.getId(),null));
			} else if (walletSubTransaction.getSubWalletType().equals(SubWalletType.GIFT_SUB_WALLET)) {
				giftSubWallet = giftSubWallet.plus(walletSubTransaction.getAmount());
				walletTransactionRes.getWalletSubTransaction().add(new WalletSubTransaction(SubWalletType.GIFT_SUB_WALLET,walletSubTransaction.getAmount(),0,0,0,walletTransaction.getId(),null));
			} else if (walletSubTransaction.getSubWalletType().equals(SubWalletType.REFUND_SUB_WALLET)) {
				refundSubWallet = refundSubWallet.plus(walletSubTransaction.getAmount());
				walletTransactionRes.getWalletSubTransaction().add(new WalletSubTransaction(SubWalletType.REFUND_SUB_WALLET,walletSubTransaction.getAmount(),0,0,0,walletTransaction.getId(),null));
			}
		}
		totalAmount = totalAmount.plus(walletTransaction.getAmount());
		return walletTransactionRes;
	}

	public WalletTransaction debit(Money amount,long orderId) {
		WalletTransaction walletTransaction = new WalletTransaction(this, TransactionType.DEBIT, amount,DateTime.now());
		Money amountLeftToBeDebited = amount;
		if(giftSubWallet.isPlus()){
			if(giftSubWallet.gteq(amountLeftToBeDebited)){
				totalAmount = totalAmount.minus(amountLeftToBeDebited);
				giftSubWallet =giftSubWallet.minus(amountLeftToBeDebited);
				walletTransaction.getWalletSubTransaction().add(new WalletSubTransaction(SubWalletType.GIFT_SUB_WALLET,amountLeftToBeDebited,orderId,0,0,0,null));
				amountLeftToBeDebited = amountLeftToBeDebited.minus(amountLeftToBeDebited);
			}else{
				totalAmount = totalAmount.minus(giftSubWallet);
				amountLeftToBeDebited = amountLeftToBeDebited.minus(giftSubWallet);
				walletTransaction.getWalletSubTransaction().add(new WalletSubTransaction(SubWalletType.GIFT_SUB_WALLET,giftSubWallet,orderId,0,0,0,null));
				giftSubWallet = giftSubWallet.minus(giftSubWallet);
			}
		}
		if(cashSubWallet.isPlus() && amountLeftToBeDebited.isPlus()){
			if(cashSubWallet.gteq(amountLeftToBeDebited)){
				totalAmount = totalAmount.minus(amountLeftToBeDebited);
				cashSubWallet = cashSubWallet.minus(amountLeftToBeDebited);
				walletTransaction.getWalletSubTransaction().add(new WalletSubTransaction(SubWalletType.CASH_SUB_WALLET,amountLeftToBeDebited,orderId,0,0,0,null));
				amountLeftToBeDebited = amountLeftToBeDebited.minus(amountLeftToBeDebited);
			}else{
				totalAmount = totalAmount.minus(cashSubWallet);
				amountLeftToBeDebited = amountLeftToBeDebited.minus(cashSubWallet);
				walletTransaction.getWalletSubTransaction().add(new WalletSubTransaction(SubWalletType.CASH_SUB_WALLET,cashSubWallet,orderId,0,0,0,null));
				cashSubWallet = cashSubWallet.minus(cashSubWallet);
			}
		}
		if(refundSubWallet.isPlus() && amountLeftToBeDebited.isPlus()){
			if(refundSubWallet.gteq(amountLeftToBeDebited)){
				totalAmount = totalAmount.minus(amountLeftToBeDebited);
				refundSubWallet = refundSubWallet.minus(amountLeftToBeDebited);
				walletTransaction.getWalletSubTransaction().add(new WalletSubTransaction(SubWalletType.REFUND_SUB_WALLET,amountLeftToBeDebited,orderId,0,0,0,null));
				amountLeftToBeDebited = amountLeftToBeDebited.minus(amountLeftToBeDebited);
			}
		}
		return walletTransaction;
	}
	public boolean isSufficientFund(Money amount) {
		if (totalAmount.gteq(amount) && amount.isPlus()) {
			return true;
		}
		return false;
	}
	public WalletTransaction refund(Money amount,long refundId){
		WalletTransaction walletTransaction = new WalletTransaction(this, TransactionType.DEBIT, amount,DateTime.now());
		totalAmount = totalAmount.minus(amount);
		refundSubWallet = refundSubWallet.minus(amount);
		walletTransaction.getWalletSubTransaction().add(new WalletSubTransaction(SubWalletType.REFUND_SUB_WALLET,amount,0,refundId,0,0,null));
		return walletTransaction;
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
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
	 * @param cashSubWallet
	 *            the cashSubWallet to set
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
	 * @param giftSubWallet
	 *            the giftSubWallet to set
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
	 * @param refundSubWallet
	 *            the refundSubWallet to set
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
	 * @param user
	 *            the user to set
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
	 * @param createdOn
	 *            the createdOn to set
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
	 * @param modifiedOn
	 *            the modifiedOn to set
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
	 * @param totalAmount
	 *            the totalAmount to set
	 */
	public void setTotalAmount(Money totalAmount) {
		this.totalAmount = totalAmount;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((cashSubWallet == null) ? 0 : cashSubWallet.hashCode());
		result = prime * result
				+ ((giftSubWallet == null) ? 0 : giftSubWallet.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result
				+ ((refundSubWallet == null) ? 0 : refundSubWallet.hashCode());
		result = prime * result
				+ ((totalAmount == null) ? 0 : totalAmount.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Wallet other = (Wallet) obj;
		if (cashSubWallet == null) {
			if (other.cashSubWallet != null) {
				return false;
			}
		} else if (!cashSubWallet.equals(other.cashSubWallet)) {
			return false;
		}
		if (giftSubWallet == null) {
			if (other.giftSubWallet != null) {
				return false;
			}
		} else if (!giftSubWallet.equals(other.giftSubWallet)) {
			return false;
		}
		if (id != other.id) {
			return false;
		}
		if (refundSubWallet == null) {
			if (other.refundSubWallet != null) {
				return false;
			}
		} else if (!refundSubWallet.equals(other.refundSubWallet)) {
			return false;
		}
		if (totalAmount == null) {
			if (other.totalAmount != null) {
				return false;
			}
		} else if (!totalAmount.equals(other.totalAmount)) {
			return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Wallet [id=");
		builder.append(id);
		builder.append(", cashSubWallet=");
		builder.append(cashSubWallet);
		builder.append(", giftSubWallet=");
		builder.append(giftSubWallet);
		builder.append(", refundSubWallet=");
		builder.append(refundSubWallet);
		builder.append(", totalAmount=");
		builder.append(totalAmount);
		builder.append("]");
		return builder.toString();
	}

}
