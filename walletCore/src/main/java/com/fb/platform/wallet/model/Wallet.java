package com.fb.platform.wallet.model;

import java.io.Serializable;
import org.joda.time.DateTime;
import com.fb.commons.to.Money;
import com.fb.platform.user.manager.model.admin.User;

public class Wallet implements Serializable {
	
	private long id;
	private Money cashSubWallet;
	private Money giftSubWallet;
	private Money refundSubWallet;
	private Money refundableAmount;
	private Money giftExpiryAmt1;
	private DateTime giftExpiryDt1;
	private Money giftExpiryAmt2;
	private DateTime giftExpiryDt2;
	private Money totalAmount;
	private DateTime createdOn;
	private DateTime modifiedOn;
	private User user;

	public WalletTransaction credit(Money amount,SubWalletType subWalletType,long paymentId,long refundId,long giftId){
		if (subWalletType.equals(SubWalletType.CASH)) {
				cashSubWallet = cashSubWallet.plus(amount);
			} else if (subWalletType.equals(SubWalletType.GIFT)) {
				giftSubWallet = giftSubWallet.plus(amount);
			} else if (subWalletType.equals(SubWalletType.REFUND)) {
				refundSubWallet = refundSubWallet.plus(amount);
			}
			totalAmount = totalAmount.plus(amount);
			WalletTransaction walletTransaction = new WalletTransaction(
					this, TransactionType.CREDIT, amount,DateTime.now());
			walletTransaction.getWalletSubTransaction().add(new WalletSubTransaction(subWalletType, amount, 0, refundId, paymentId,0, giftId));
			return walletTransaction;
	}
	
	public WalletTransaction reverseTransaction(WalletTransaction walletTransaction,Money amount,Money alreadyReversed) {
		    if(walletTransaction.getTransactionType().equals(TransactionType.CREDIT)){
		    	return reverseTransactionCredit(walletTransaction, amount);
		    }else{
		    	return reverseTransactionDebit(walletTransaction, amount,alreadyReversed);
		    }
			
	}
	private WalletTransaction reverseTransactionDebit(WalletTransaction walletTransaction , Money amount,Money alreadyReversed){
		WalletTransaction walletTransactionRes = new WalletTransaction(this, TransactionType.CREDIT, amount,DateTime.now());
		Money amountTobeReversed = amount;
		WalletSubTransaction walletSubTransactionRefund = walletTransaction.subTransactionBySubWallet(SubWalletType.REFUND);
		WalletSubTransaction walletSubTransactionCash = walletTransaction.subTransactionBySubWallet(SubWalletType.CASH);
		WalletSubTransaction walletSubTransactionGift = walletTransaction.subTransactionBySubWallet(SubWalletType.GIFT);
		if(walletSubTransactionRefund != null){
			if(walletSubTransactionRefund.getAmount().gt(alreadyReversed)){
				if(amountTobeReversed.lteq(walletSubTransactionRefund.getAmount().minus(alreadyReversed))){
					refundSubWallet = refundSubWallet.plus(amountTobeReversed);
					walletTransactionRes.getWalletSubTransaction().add(new WalletSubTransaction(SubWalletType.REFUND,amountTobeReversed,0,0,0,walletTransaction.getId(),0));
					amountTobeReversed = amountTobeReversed.minus(amountTobeReversed);
				}else{
					refundSubWallet = refundSubWallet.plus(walletSubTransactionRefund.getAmount().minus(alreadyReversed));
					walletTransactionRes.getWalletSubTransaction().add(new WalletSubTransaction(SubWalletType.REFUND,walletSubTransactionRefund.getAmount().minus(alreadyReversed),0,0,0,walletTransaction.getId(),0));
					amountTobeReversed = amountTobeReversed.minus(walletSubTransactionRefund.getAmount().minus(alreadyReversed));
				}
				alreadyReversed = alreadyReversed.minus(alreadyReversed);
			}else{
				alreadyReversed = alreadyReversed.minus(walletSubTransactionRefund.getAmount());
			}
		}
		if(walletSubTransactionCash != null){
			if(walletSubTransactionCash.getAmount().gt(alreadyReversed)){
				if(amountTobeReversed.lteq(walletSubTransactionCash.getAmount().minus(alreadyReversed))){
					cashSubWallet = cashSubWallet.plus(amountTobeReversed);
					walletTransactionRes.getWalletSubTransaction().add(new WalletSubTransaction(SubWalletType.CASH,amountTobeReversed,0,0,0,walletTransaction.getId(),0));
					amountTobeReversed = amountTobeReversed.minus(amountTobeReversed);
				}else{
					cashSubWallet = cashSubWallet.plus(walletSubTransactionCash.getAmount().minus(alreadyReversed));
					walletTransactionRes.getWalletSubTransaction().add(new WalletSubTransaction(SubWalletType.CASH,walletSubTransactionCash.getAmount().minus(alreadyReversed),0,0,0,walletTransaction.getId(),0));
					amountTobeReversed = amountTobeReversed.minus(walletSubTransactionCash.getAmount().minus(alreadyReversed));
				}
				alreadyReversed = alreadyReversed.minus(alreadyReversed);
			}else{
				alreadyReversed = alreadyReversed.minus(walletSubTransactionCash.getAmount());
			}
		}
		if(walletSubTransactionGift != null){
			if(amountTobeReversed.lteq(walletSubTransactionGift.getAmount().minus(alreadyReversed))){
				giftSubWallet = giftSubWallet.plus(amountTobeReversed);
				walletTransactionRes.getWalletSubTransaction().add(new WalletSubTransaction(SubWalletType.GIFT,amountTobeReversed,0,0,0,walletTransaction.getId(),0));
				amountTobeReversed = amountTobeReversed.minus(amountTobeReversed);
			}
		}
		totalAmount = totalAmount.plus(amount);
		return walletTransactionRes;
	}
	
	private WalletTransaction reverseTransactionCredit(WalletTransaction walletTransaction , Money amount){
		WalletTransaction walletTransactionRes = new WalletTransaction(this, TransactionType.DEBIT, amount,DateTime.now());
		Money amountTobeReversed = amount;
		WalletSubTransaction walletSubTransactionRefund = walletTransaction.subTransactionBySubWallet(SubWalletType.REFUND);
		WalletSubTransaction walletSubTransactionCash = walletTransaction.subTransactionBySubWallet(SubWalletType.CASH);
		WalletSubTransaction walletSubTransactionGift = walletTransaction.subTransactionBySubWallet(SubWalletType.GIFT);
		if(walletSubTransactionRefund != null){
			if(amountTobeReversed.lteq(walletSubTransactionRefund.getAmount())){
				refundSubWallet = refundSubWallet.minus(amountTobeReversed);
				walletTransactionRes.getWalletSubTransaction().add(new WalletSubTransaction(SubWalletType.REFUND,amountTobeReversed,0,0,0,walletTransaction.getId(),0));
				amountTobeReversed = amountTobeReversed.minus(amountTobeReversed);
			}else{
				refundSubWallet = refundSubWallet.minus(walletSubTransactionRefund.getAmount());
				walletTransactionRes.getWalletSubTransaction().add(new WalletSubTransaction(SubWalletType.REFUND,walletSubTransactionRefund.getAmount(),0,0,0,walletTransaction.getId(),0));
				amountTobeReversed = amountTobeReversed.minus(walletSubTransactionRefund.getAmount());
			}
		}
		if(walletSubTransactionCash != null){
			if(amountTobeReversed.lteq(walletSubTransactionCash.getAmount())){
				cashSubWallet = cashSubWallet.minus(amountTobeReversed);
				walletTransactionRes.getWalletSubTransaction().add(new WalletSubTransaction(SubWalletType.CASH,amountTobeReversed,0,0,0,walletTransaction.getId(),0));
				amountTobeReversed = amountTobeReversed.minus(amountTobeReversed);
			}else{
				cashSubWallet = cashSubWallet.minus(walletSubTransactionCash.getAmount());
				walletTransactionRes.getWalletSubTransaction().add(new WalletSubTransaction(SubWalletType.CASH,walletSubTransactionCash.getAmount(),0,0,0,walletTransaction.getId(),0));
				amountTobeReversed = amountTobeReversed.minus(walletSubTransactionCash.getAmount());
			}
		}
		if(walletSubTransactionGift != null){
			if(amountTobeReversed.lteq(walletSubTransactionGift.getAmount())){
				giftSubWallet = giftSubWallet.minus(amountTobeReversed);
				walletTransactionRes.getWalletSubTransaction().add(new WalletSubTransaction(SubWalletType.GIFT,amountTobeReversed,0,0,0,walletTransaction.getId(),walletSubTransactionGift.getGiftId()));
				amountTobeReversed = amountTobeReversed.minus(amountTobeReversed);
			}
		}
		totalAmount = totalAmount.minus(amount);
		return walletTransactionRes;
	}
	
	public WalletTransaction debit(Money amount,long giftId,String notes){
		WalletTransaction walletTransaction = new WalletTransaction(this, TransactionType.DEBIT, amount,DateTime.now());
		totalAmount = totalAmount.minus(amount);
		giftSubWallet =giftSubWallet.minus(amount);
		walletTransaction.getWalletSubTransaction().add(new WalletSubTransaction(amount,giftId,notes));
		return walletTransaction;
	}

	public WalletTransaction debit(Money amount,long orderId) {
		WalletTransaction walletTransaction = new WalletTransaction(this, TransactionType.DEBIT, amount,DateTime.now());
		Money amountLeftToBeDebited = amount;
		if(giftSubWallet.isPlus()){
			if(giftSubWallet.gteq(amountLeftToBeDebited)){
				totalAmount = totalAmount.minus(amountLeftToBeDebited);
				giftSubWallet =giftSubWallet.minus(amountLeftToBeDebited);
				walletTransaction.getWalletSubTransaction().add(new WalletSubTransaction(SubWalletType.GIFT,amountLeftToBeDebited,orderId));
				amountLeftToBeDebited = amountLeftToBeDebited.minus(amountLeftToBeDebited);
			}else{
				totalAmount = totalAmount.minus(giftSubWallet);
				amountLeftToBeDebited = amountLeftToBeDebited.minus(giftSubWallet);
				walletTransaction.getWalletSubTransaction().add(new WalletSubTransaction(SubWalletType.GIFT,giftSubWallet,orderId));
				giftSubWallet = giftSubWallet.minus(giftSubWallet);
			}
		}
		if(cashSubWallet.isPlus() && amountLeftToBeDebited.isPlus()){
			if(cashSubWallet.gteq(amountLeftToBeDebited)){
				totalAmount = totalAmount.minus(amountLeftToBeDebited);
				cashSubWallet = cashSubWallet.minus(amountLeftToBeDebited);
				walletTransaction.getWalletSubTransaction().add(new WalletSubTransaction(SubWalletType.CASH,amountLeftToBeDebited,orderId));
				amountLeftToBeDebited = amountLeftToBeDebited.minus(amountLeftToBeDebited);
			}else{
				totalAmount = totalAmount.minus(cashSubWallet);
				amountLeftToBeDebited = amountLeftToBeDebited.minus(cashSubWallet);
				walletTransaction.getWalletSubTransaction().add(new WalletSubTransaction(SubWalletType.CASH,cashSubWallet,orderId));
				cashSubWallet = cashSubWallet.minus(cashSubWallet);
			}
		}
		if(refundSubWallet.isPlus() && amountLeftToBeDebited.isPlus()){
			if(refundSubWallet.gteq(amountLeftToBeDebited)){
				totalAmount = totalAmount.minus(amountLeftToBeDebited);
				refundSubWallet = refundSubWallet.minus(amountLeftToBeDebited);
				walletTransaction.getWalletSubTransaction().add(new WalletSubTransaction(SubWalletType.REFUND,amountLeftToBeDebited,orderId));
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
		walletTransaction.getWalletSubTransaction().add(new WalletSubTransaction(SubWalletType.REFUND,amount,0,refundId,0,0,0));
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
	

	/**
	 * @return the refundableAmount
	 */
	public Money getRefundableAmount() {
		return refundableAmount;
	}

	/**
	 * @param refundableAmount the refundableAmount to set
	 */
	public void setRefundableAmount(Money refundableAmount) {
		this.refundableAmount = refundableAmount;
	}

	/**
	 * @return the giftExpiryAmt1
	 */
	public Money getGiftExpiryAmt1() {
		return giftExpiryAmt1;
	}

	/**
	 * @param giftExpiryAmt1 the giftExpiryAmt1 to set
	 */
	public void setGiftExpiryAmt1(Money giftExpiryAmt1) {
		this.giftExpiryAmt1 = giftExpiryAmt1;
	}

	/**
	 * @return the giftExpiryDt1
	 */
	public DateTime getGiftExpiryDt1() {
		return giftExpiryDt1;
	}

	/**
	 * @param giftExpiryDt1 the giftExpiryDt1 to set
	 */
	public void setGiftExpiryDt1(DateTime giftExpiryDt1) {
		this.giftExpiryDt1 = giftExpiryDt1;
	}

	/**
	 * @return the giftExpiryAmt2
	 */
	public Money getGiftExpiryAmt2() {
		return giftExpiryAmt2;
	}

	/**
	 * @param giftExpiryAmt2 the giftExpiryAmt2 to set
	 */
	public void setGiftExpiryAmt2(Money giftExpiryAmt2) {
		this.giftExpiryAmt2 = giftExpiryAmt2;
	}

	/**
	 * @return the giftExpiryDt2
	 */
	public DateTime getGiftExpiryDt2() {
		return giftExpiryDt2;
	}

	/**
	 * @param giftExpiryDt2 the giftExpiryDt2 to set
	 */
	public void setGiftExpiryDt2(DateTime giftExpiryDt2) {
		this.giftExpiryDt2 = giftExpiryDt2;
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