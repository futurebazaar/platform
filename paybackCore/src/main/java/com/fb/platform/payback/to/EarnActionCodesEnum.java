package com.fb.platform.payback.to;

import java.io.Serializable;

public enum EarnActionCodesEnum implements Serializable{
	
	PREALLOC_EARN("ACTIVITY_IMINT"),
	EARN_REVERSAL("REVERSAL_IMINT");
	
	private String actionCode = null;
	
	private EarnActionCodesEnum(String actionCode){
		this.actionCode = actionCode;
	}
	
	@Override
	public String toString(){
		return this.actionCode;
	}

}
