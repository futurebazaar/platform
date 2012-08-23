package com.fb.platform.wallet.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.joda.time.DateTime;
import com.fb.commons.to.Money;

public class WalletTransaction {

	private Wallet wallet;
	private long id;
	private String transactionId = null;
	private TransactionType transactionType = null;
	private List<WalletSubTransaction> walletSubTransaction;
	private Money amount;
	private DateTime timeStamp;
	private String transactionNote = null;
	private Money walletBalance;

	WalletTransaction(Wallet wallet,
			TransactionType transactionType, Money amount ,DateTime datetime) {
		super();
		this.wallet = wallet;
		this.transactionType = transactionType;
		this.amount = amount;
		this.timeStamp = datetime;
		this.walletBalance = wallet.getTotalAmount();
	}
	
	public WalletTransaction() {
		// TODO Auto-generated constructor stub
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
	 * @return the wallet
	 */
	public Wallet getWallet() {
		return wallet;
	}

	/**
	 * @param wallet
	 *            the wallet to set
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
	 * @param transactionId
	 *            the transactionId to set
	 */
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
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
	 * @return the transactionNote
	 */
	public String getTransactionNote() {
		return transactionNote;
	}

	/**
	 * @param transactionNote the transactionNote to set
	 */
	public void setTransactionNote(String transactionNote) {
		this.transactionNote = transactionNote;
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

	public boolean isSubTransactionBySubWallet(SubWalletType subWalletType){
		if(this.walletSubTransaction.isEmpty()){
			return false;
		}else{
			for (Iterator<WalletSubTransaction> iterator = walletSubTransaction.iterator(); iterator.hasNext(); ) {
				WalletSubTransaction walletSubTransactionItr = (WalletSubTransaction) iterator.next();
				if(walletSubTransactionItr.getSubWalletType().equals(subWalletType)){
					return true;
				}
			}
			return false;
		}
	}
	
	public WalletSubTransaction subTransactionBySubWallet(SubWalletType subWalletType){
		if(this.walletSubTransaction.isEmpty()){
			return null;
		}else{
			for (Iterator<WalletSubTransaction> iterator = walletSubTransaction.iterator(); iterator.hasNext(); ) {
				WalletSubTransaction walletSubTransactionItr = (WalletSubTransaction) iterator.next();
				if(walletSubTransactionItr.getSubWalletType().equals(subWalletType)){
					return walletSubTransactionItr;
				}
			}
			return null;
		}
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

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((amount == null) ? 0 : amount.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result
				+ ((timeStamp == null) ? 0 : timeStamp.hashCode());
		result = prime * result
				+ ((transactionId == null) ? 0 : transactionId.hashCode());
		result = prime * result
				+ ((transactionType == null) ? 0 : transactionType.hashCode());
		result = prime * result + ((wallet == null) ? 0 : wallet.hashCode());
		result = prime
				* result
				+ ((walletSubTransaction == null) ? 0 : walletSubTransaction
						.hashCode());
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
		WalletTransaction other = (WalletTransaction) obj;
		if (amount == null) {
			if (other.amount != null) {
				return false;
			}
		} else if (!amount.equals(other.amount)) {
			return false;
		}
		if (id != other.id) {
			return false;
		}
		if (timeStamp == null) {
			if (other.timeStamp != null) {
				return false;
			}
		} else if (!timeStamp.equals(other.timeStamp)) {
			return false;
		}
		if (transactionId == null) {
			if (other.transactionId != null) {
				return false;
			}
		} else if (!transactionId.equals(other.transactionId)) {
			return false;
		}
		if (transactionType != other.transactionType) {
			return false;
		}
		if (wallet == null) {
			if (other.wallet != null) {
				return false;
			}
		} else if (!wallet.equals(other.wallet)) {
			return false;
		}
		if (walletSubTransaction == null) {
			if (other.walletSubTransaction != null) {
				return false;
			}
		} else if (!walletSubTransaction.equals(other.walletSubTransaction)) {
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
		builder.append("WalletTransaction [wallet=");
		builder.append(wallet);
		builder.append(", id=");
		builder.append(id);
		builder.append(", transactionId=");
		builder.append(transactionId);
		builder.append(", transactionType=");
		builder.append(transactionType);
		builder.append(", walletSubTransaction=");
		builder.append(walletSubTransaction);
		builder.append(", amount=");
		builder.append(amount);
		builder.append(", timeStamp=");
		builder.append(timeStamp);
		builder.append("]");
		return builder.toString();
	}	
	 
	
}
