package com.fb.platform.wallet.to;

public enum DebitWalletStatus {
	
	SUCCESS("WALLET DEBIT SUCCESSFULL"),
	INSUFFICIENT_FUND("INSUFFICIENT BALANCE IN ACCOUNT"),
	FAILURE("WALLET ERROR");
	
	private String debitWalletStatus;
	
	private DebitWalletStatus(String debitWalletStatus){
		this.debitWalletStatus = debitWalletStatus;
	}
	
	@Override
	public String toString(){
		return this.debitWalletStatus;
	}

}
