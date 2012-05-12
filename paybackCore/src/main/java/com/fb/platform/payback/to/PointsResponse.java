package com.fb.platform.payback.to;

public class PointsResponse {

	private PointsTxnClassificationCodeEnum actionCode;
	private PointsResponseCodeEnum pointsResponseCodeEnum;
	private String statusMessage;
	
	public String getStatusMessage() {
		return statusMessage;
	}
	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}
	public PointsTxnClassificationCodeEnum getActionCode() {
		return actionCode;
	}
	public void setActionCode(PointsTxnClassificationCodeEnum txnActionCode) {
		this.actionCode = txnActionCode;
	}
	public PointsResponseCodeEnum getPointsResponseCodeEnum() {
		return pointsResponseCodeEnum;
	}
	public void setPointsResponseCodeEnum(
			PointsResponseCodeEnum pointsResponseCodeEnum) {
		this.pointsResponseCodeEnum = pointsResponseCodeEnum;
	}
}
