package com.fb.platform.wallet.to;

import com.fb.commons.to.Money;
import com.fb.platform.wallet.model.SubWalletType;

public class WalletSubTransaction {

	private SubWalletType subWalletType;
	private Money amount;
	private long orderId;
	private long refundId;
	private long paymentId;
	private long paymentReversalId;
	private String giftCode;
	
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
	 * @return the giftCode
	 */
	public String getGiftCode() {
		return giftCode;
	}
	/**
	 * @param giftCode the giftCode to set
	 */
	public void setGiftCode(String giftCode) {
		this.giftCode = giftCode;
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
}
