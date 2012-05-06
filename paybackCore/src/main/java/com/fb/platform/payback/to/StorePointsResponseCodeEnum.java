package com.fb.platform.payback.to;

public enum StorePointsResponseCodeEnum {
	SUCCESS("Points storage completed successfully"),
	FAILURE("Failed to store points"),
	INTERNAL_ERROR("Error Occurred");
	
	private String responseCode = null;
	
	private StorePointsResponseCodeEnum(String responseCode){
		this.responseCode = responseCode;
	}
	
	@Override
	public String toString(){
		return this.responseCode;
	}
}
