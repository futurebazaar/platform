/**
 * 
 */
package com.fb.platform.egv.to;

import org.joda.time.DateTime;


/**
 * @author keith
 *
 */
public class CreateResponse implements GiftVoucherResponse{

	private String sessionToken;
	private long gvNumber;
	private DateTime validFrom;
	private DateTime validTill;
	private CreateResponseStatusEnum responseStatus;

	public String getSessionToken() {
		return sessionToken;
	}

	public void setSessionToken(String sessionToken) {
		this.sessionToken = sessionToken;
	}

	public CreateResponseStatusEnum getResponseStatus() {
		return responseStatus;
	}

	public void setResponseStatus(CreateResponseStatusEnum status) {
		this.responseStatus = status;
	}

	public long getGvNumber() {
		return gvNumber;
	}

	public void setGvNumber(long gvNumber) {
		this.gvNumber = gvNumber;
	}

	public DateTime getValidFrom() {
		return validFrom;
	}

	public void setValidFrom(DateTime validFrom) {
		this.validFrom = validFrom;
	}

	public DateTime getValidTill() {
		return validTill;
	}

	public void setValidTill(DateTime validTill) {
		this.validTill = validTill;
	}
	
}
