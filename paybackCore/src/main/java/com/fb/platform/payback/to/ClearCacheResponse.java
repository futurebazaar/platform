package com.fb.platform.payback.to;

public class ClearCacheResponse {
	
	private PointsResponseCodeEnum responseCode;
	private String sessionToken;
	private String ruleName;
	
	public PointsResponseCodeEnum getResponseCode() {
		return responseCode;
	}
	public void setResponseCode(PointsResponseCodeEnum responseCode) {
		this.responseCode = responseCode;
	}
	public String getSessionToken() {
		return sessionToken;
	}
	public void setSessionToken(String sessionToken) {
		this.sessionToken = sessionToken;
	}
	public String getRuleName() {
		return ruleName;
	}
	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}

}
