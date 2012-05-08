package com.fb.platform.wallet.model;

public enum SubWalletType {
	CASH_SUB_WALLET("CASH SUB WALLET"),
	GIFT_SUB_WALLET("GIFT SUB WALLET"),
	REFUND_SUB_WALLET("REFUND SUB WALLET");
	
	private String subWalletType = null;
	
	private SubWalletType(String subWalletType){
		this.subWalletType = subWalletType;
	}
	
	@Override
	public String toString(){
		return this.subWalletType;
	}

}
