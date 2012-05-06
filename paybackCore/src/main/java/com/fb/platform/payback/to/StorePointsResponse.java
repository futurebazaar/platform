package com.fb.platform.payback.to;

public class StorePointsResponse {

	private String actionCode;
	private StorePointsResponseCodeEnum storePointsResponseCodeEnum;
	
	public String getActionCode() {
		return actionCode;
	}
	public void setActionCode(String actionCode) {
		this.actionCode = actionCode;
	}
	public StorePointsResponseCodeEnum getStorePointsResponseCodeEnum() {
		return storePointsResponseCodeEnum;
	}
	public void setStorePointsResponseCodeEnum(
			StorePointsResponseCodeEnum storePointsResponseCodeEnum) {
		this.storePointsResponseCodeEnum = storePointsResponseCodeEnum;
	}
}
