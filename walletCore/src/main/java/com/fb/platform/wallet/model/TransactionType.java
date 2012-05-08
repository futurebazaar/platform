package com.fb.platform.wallet.model;

public enum TransactionType {

	CREDIT("CREDIT"),
	DEBIT("DEBIT");
	
	private String transactionType;
	
	private TransactionType(String transactionType){
		this.transactionType = transactionType;
	}
	
	@Override
	public String toString(){
		return this.transactionType;
	}
}
