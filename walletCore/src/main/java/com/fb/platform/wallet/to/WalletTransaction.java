package com.fb.platform.wallet.to;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

import com.fb.commons.to.Money;
import com.fb.platform.wallet.model.Wallet;
import com.fb.platform.wallet.model.TransactionType;
import com.fb.platform.wallet.to.WalletSubTransaction;

public class WalletTransaction {
	
	private Wallet wallet;
	private String transactionId = null;
	private String notes;
	private TransactionType transactionType = null;
	private List<WalletSubTransaction> walletSubTransaction;
	private Money amount;
	private DateTime timeStamp;
	private Money walletBalance;
	
	/**
    * @return the wallet
    */
    public Wallet getWallet() {
    	return wallet;
    }
    /**
    * @param wallet the wallet to set
    */
    public void setWallet(Wallet wallet) {
    	this.wallet = wallet;
	}

	/**
	 * @return the transactionId
	 */
	public String getTransactionId() {
		return transactionId;
	}
	/**
	 * @param transactionId the transactionId to set
	 */
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
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
	 * @return the type
	 */
	public TransactionType getTransactionType() {
		return transactionType;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setTransactionType(TransactionType transactionType) {
		this.transactionType = transactionType;
	}

	/**
	 * @return the amount
	 */
	public Money getAmount() {
		return amount;
	}

	/**
	 * @param amount
	 *            the amount to set
	 */
	public void setAmount(Money amount) {
		this.amount = amount;
	}

	/**
	 * @return the timeStamp
	 */
	public DateTime getTimeStamp() {
		return timeStamp;
	}

	/**
	 * @param timeStamp
	 *            the timeStamp to set
	 */
	public void setTimeStamp(DateTime timeStamp) {
		this.timeStamp = timeStamp;
	}
	/**
	 * @return the walletSubTransaction
	 */
	 public List<WalletSubTransaction> getWalletSubTransaction() {
	        if (walletSubTransaction == null) {
	        	walletSubTransaction = new ArrayList<WalletSubTransaction>();
	        }
	        return this.walletSubTransaction;
	}

	 /**
	  * 
	  * @param walletSubTransaction
	  * 	walletSubTransaction List to set
	  */
	 public void setWalletSubTransaction(List<WalletSubTransaction> walletSubTransaction) {
	        this.walletSubTransaction = walletSubTransaction;
	}
	/**
	 * @return the walletBalance
	 */
	public Money getWalletBalance() {
		return walletBalance;
	}
	/**
	 * @param walletBalance the walletBalance to set
	 */
	public void setWalletBalance(Money walletBalance) {
		this.walletBalance = walletBalance;
	}
	 
	
}
