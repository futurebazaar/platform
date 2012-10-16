/**
 * 
 */
package com.fb.platform.egv.to;

import java.math.BigDecimal;

import org.joda.time.DateTime;

/**
 * @author keith
 *
 */
public class CreateRequest implements GiftVoucherRequest{

	private String sessionToken;
	private int orderItemId;
	private String email;
	private String mobile;
	private BigDecimal amount;
	private String senderName;
	private String receiverName;
	private String giftMessage;
	private DateTime validFrom;
	private DateTime validTill;
	private boolean isDeferActivation;

	public DateTime getValidTill() {
		return validTill;
	}

	public void setValidTill(DateTime validTill) {
		this.validTill = validTill;
	}

	public String getSessionToken() {
		return sessionToken;
	}

	public void setSessionToken(String sessionToken) {
		this.sessionToken = sessionToken;
	}

	public int getOrderItemId() {
		return orderItemId;
	}

	public void setOrderItemId(int orderItemId) {
		this.orderItemId = orderItemId;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getSenderName() {
		return senderName;
	}

	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}

	public String getReceiverName() {
		return receiverName;
	}

	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}

	public String getGiftMessage() {
		return giftMessage;
	}

	public void setGiftMessage(String giftMessage) {
		this.giftMessage = giftMessage;
	}

	public boolean isDeferActivation() {
		return isDeferActivation;
	}

	public void setDeferActivation(boolean isDeferActivation) {
		this.isDeferActivation = isDeferActivation;
	}

	public DateTime getValidFrom() {
		return validFrom;
	}

	public void setValidFrom(DateTime validFrom) {
		this.validFrom = validFrom;
	}
	
}
