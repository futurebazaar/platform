package com.fb.platform.payback.util;

public enum PointsTxnClassificationCodeEnum {
	
	BURN_REVERSAL("RECONCILIATION, NO_PAYMENT"),
	EARN_REVERSAL("RECONCILIATION, NO_PAYMENT"),
	PREALLOC_EARN("CASH_CASH, OTHERS");
	
	private String txnClassificationCode = null;
	
	private PointsTxnClassificationCodeEnum(String txnClassificationCode){
		this.txnClassificationCode = txnClassificationCode;
	}
	
	@Override
	public String toString(){
		return this.txnClassificationCode;
	}


}
