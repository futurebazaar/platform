/**
 * 
 */
package com.fb.platform.promotion.rule.impl;

import java.io.Serializable;

import com.fb.platform.promotion.to.ApplyCouponResponseStatusEnum;

/**
 * @author keith
 *
 */
public class ApplicableResponse implements Serializable{

	ApplyCouponResponseStatusEnum statusCode;
	private String statusMessage;
	private String statusDescription;
	public String getStatusMessage() {
		return statusMessage;
	}
	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}
	public String getStatusDescription() {
		return statusDescription;
	}
	public void setStatusDescription(String statusDescription) {
		this.statusDescription = statusDescription;
	}
	public ApplyCouponResponseStatusEnum getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(ApplyCouponResponseStatusEnum statusCode) {
		this.statusCode = statusCode;
	}
	
}
