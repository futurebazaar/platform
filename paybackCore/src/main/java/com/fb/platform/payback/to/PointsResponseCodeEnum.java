package com.fb.platform.payback.to;

public enum PointsResponseCodeEnum {
	SUCCESS("Task completed successfully."),
	FAILURE("Failed to complete the Task."),
	INTERNAL_ERROR("Error Occurred."),
	INVALID_CARD_NO("Card No. is not proper. Please enter a valid 16 digits card no."),
	INVALID_REFERENCE_ID("Reference ID cannot be null."),
	INVALID_POINTS("No Points to Store."),
	INVALID_ACTION_CODE("Action Code specified is invalid"), 
	INVALID_REQUEST("This Request is not Valid. Please check."), 
	NO_SESSION("No session attached/ Invalid session."), 
	HEADER_DOES_NOT_EXIST("No Earn Data exists."), 
	POINTS_ALREADY_GIVEN("Points already allocated for the given order");
	
	private String responseCode = null;
	
	private PointsResponseCodeEnum(String responseCode) {
		this.responseCode = responseCode;
	}
	
	@Override
	public String toString() {
		return this.responseCode;
	}
}
