package com.fb.platform.wallet.to;

public enum CreditWalletStatus {
	
	SUCCESS("WALLET CREDIT SUCCESSFULL"),
	FAILURE("WALLET ERROR");
	
	private String creditWalletStatus;
	
	private CreditWalletStatus(String creditWalletStatus){
		this.creditWalletStatus = creditWalletStatus;
	}
	
	@Override
	public String toString(){
		return this.creditWalletStatus;
	}

}
