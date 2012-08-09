/**
 * 
 */
package com.fb.platform.promotion.product.to;

import java.io.Serializable;


/**
 * @author nehaga
 *
 */
public class CommitAutoPromotionResponse implements Serializable {
	
	private CommitAutoPromotionResponseStatusEnum commitAutoPromotionStatus;
	private String statusMessage;
	private String sessionToken;
	
	public CommitAutoPromotionResponseStatusEnum getCommitAutoPromotionStatus() {
		return commitAutoPromotionStatus;
	}
	public void setCommitAutoPromotionStatus(
			CommitAutoPromotionResponseStatusEnum commitAutoPromotionStatus) {
		this.commitAutoPromotionStatus = commitAutoPromotionStatus;
	}
	public String getStatusMessage() {
		return statusMessage;
	}
	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}
	public String getSessionToken() {
		return sessionToken;
	}
	public void setSessionToken(String sessionToken) {
		this.sessionToken = sessionToken;
	}
}
