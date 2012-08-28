package com.fb.commons.mom.to;

public enum TinlaAckType {
	DEL_ACK,
	ORDER_ACK,
	MOD_ACK,
	CAN_ACK,
	ITEM_ACK,
	RET_ACK;
	
	@Override
	public String toString() {
		return this.name();
	}
}
