package com.fb.platform.wallet.to;

public enum CreditWalletStatus {
	
	SUCCESS("WALLET CREDIT SUCCESSFULL"),
	FAILURE("WALLET ERROR"), 
	ZERO_AMOUNT ("ZERO AMOUNT TO BE CREDITED"), 
	INVALID_SUBWALLET ("INVALID SUBWALLET");
	
	private String creditWalletStatus;
	
	private CreditWalletStatus(String creditWalletStatus){
		this.creditWalletStatus = creditWalletStatus;
	}
	
	@Override
	public String toString(){
		return this.creditWalletStatus;
	}

}
