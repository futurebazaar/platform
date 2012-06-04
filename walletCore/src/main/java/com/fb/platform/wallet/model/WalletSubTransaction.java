package com.fb.platform.wallet.model;

import org.joda.time.DateTime;

import com.fb.commons.to.Money;

public class WalletSubTransaction {
	private SubWalletType subWalletType;
	private long id;
	private long transactionId;
	private Money amount;
	private long orderId;
	private long refundId;
	private long paymentId;
	private long paymentReversalId;
	private long giftId;
	private String notes;
	
	
	
	WalletSubTransaction(SubWalletType subWalletType, Money amount,
			long orderId, long refundId, long paymentId,long paymentReversalId,long giftId) {
		super();
		this.subWalletType = subWalletType;
		this.amount = amount;
		this.orderId = orderId;
		this.refundId = refundId;
		this.paymentId = paymentId;
		this.giftId = giftId;
		this.paymentReversalId =paymentReversalId;
	}
	
	WalletSubTransaction(SubWalletType subWalletType, Money amount,long orderId)
	{
		super();
		this.subWalletType = subWalletType;
		this.amount = amount;
		this.orderId = orderId;
	}
	
	WalletSubTransaction(Money amount,long giftId,String notes)
	{
		super();
		this.subWalletType = SubWalletType.GIFT;
		this.amount = amount;
		this.giftId = giftId;
		this.notes = notes;
	}
	
	public WalletSubTransaction(){
		
	}
	/**
	 * @return the subWalletType
	 */
	public SubWalletType getSubWalletType() {
		return subWalletType;
	}
	/**
	 * @param subWalletType the subWalletType to set
	 */
	public void setSubWalletType(SubWalletType subWalletType) {
		this.subWalletType = subWalletType;
	}
	/**
	 * @return the amount
	 */
	public Money getAmount() {
		return amount;
	}
	/**
	 * @param amount the amount to set
	 */
	public void setAmount(Money amount) {
		this.amount = amount;
	}
	/**
	 * @return the orderId
	 */
	public long getOrderId() {
		return orderId;
	}
	/**
	 * @param orderId the orderId to set
	 */
	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}
	/**
	 * @return the refundId
	 */
	public long getRefundId() {
		return refundId;
	}
	/**
	 * @param refundId the refundId to set
	 */
	public void setRefundId(long refundId) {
		this.refundId = refundId;
	}
	/**
	 * @return the paymentId
	 */
	public long getPaymentId() {
		return paymentId;
	}
	/**
	 * @param paymentId the paymentId to set
	 */
	public void setPaymentId(long paymentId) {
		this.paymentId = paymentId;
	}
	/**
	 * @return the paymentReversalId
	 */
	public long getPaymentReversalId() {
		return paymentReversalId;
	}

	/**
	 * @param paymentReversalId the paymentReversalId to set
	 */
	public void setPaymentReversalId(long paymentReversalId) {
		this.paymentReversalId = paymentReversalId;
	}

	/**
	 * @return the notes
	 */
	public String getNotes() {
		return notes;
	}

	/**
	 * @param notes the notes to set
	 */
	public void setNotes(String notes) {
		this.notes = notes;
	}

	/**
	 * @return the transactionId
	 */
	public long getTransactionId() {
		return transactionId;
	}

	/**
	 * @param transactionId the transactionId to set
	 */
	public void setTransactionId(long transactionId) {
		this.transactionId = transactionId;
	}

	/**
	 * @return the giftId
	 */
	public long getGiftId() {
		return giftId;
	}

	/**
	 * @param giftId the giftId to set
	 */
	public void setGiftId(long giftId) {
		this.giftId = giftId;
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

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((amount == null) ? 0 : amount.hashCode());
		result = prime * result + (int) (giftId ^ (giftId >>> 32));
		result = prime * result + (int) (orderId ^ (orderId >>> 32));
		result = prime * result + (int) (paymentId ^ (paymentId >>> 32));
		result = prime * result
				+ (int) (paymentReversalId ^ (paymentReversalId >>> 32));
		result = prime * result + (int) (refundId ^ (refundId >>> 32));
		result = prime * result
				+ ((subWalletType == null) ? 0 : subWalletType.hashCode());
		result = prime * result
				+ (int) (transactionId ^ (transactionId >>> 32));
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
		WalletSubTransaction other = (WalletSubTransaction) obj;
		if (amount == null) {
			if (other.amount != null) {
				return false;
			}
		} else if (!amount.equals(other.amount)) {
			return false;
		}
		if (giftId != other.giftId) {
			return false;
		}
		if (orderId != other.orderId) {
			return false;
		}
		if (paymentId != other.paymentId) {
			return false;
		}
		if (paymentReversalId != other.paymentReversalId) {
			return false;
		}
		if (refundId != other.refundId) {
			return false;
		}
		if (subWalletType != other.subWalletType) {
			return false;
		}
		if (transactionId != other.transactionId) {
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
		builder.append("WalletSubTransaction [subWalletType=");
		builder.append(subWalletType);
		builder.append(", transactionId=");
		builder.append(transactionId);
		builder.append(", amount=");
		builder.append(amount);
		builder.append(", orderId=");
		builder.append(orderId);
		builder.append(", refundId=");
		builder.append(refundId);
		builder.append(", paymentId=");
		builder.append(paymentId);
		builder.append(", paymentReversalId=");
		builder.append(paymentReversalId);
		builder.append(", giftId=");
		builder.append(giftId);
		builder.append(", notes=");
		builder.append(notes);
		builder.append("]");
		return builder.toString();
	}
	
	
}
