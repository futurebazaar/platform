package com.fb.platform.scheduler.utils;

public enum OrderAck {
	
	CAN_ACK, 
	ORDER_ACK, 
	MOD_ACK, 
	RET_ACK;
	
	@Override
	public String toString() {
		return this.name();
	}
}
