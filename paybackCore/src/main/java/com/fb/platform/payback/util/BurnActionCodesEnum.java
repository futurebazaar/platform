package com.fb.platform.payback.util;

public enum BurnActionCodesEnum {
	
	BURN_REVERSAL("BURN_REVERSAL");
	
	private String actionCode = null;
	
	private BurnActionCodesEnum(String actionCode){
		this.actionCode = actionCode;
	}
	
	@Override
	public String toString(){
		return this.actionCode;
	}

}
