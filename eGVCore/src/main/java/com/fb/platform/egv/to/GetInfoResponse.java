/**
 * 
 */
package com.fb.platform.egv.to;

import java.io.Serializable;
import java.math.BigDecimal;

import org.joda.time.DateTime;

import com.fb.commons.to.Money;
import com.fb.platform.egv.model.GiftVoucherDates;
import com.fb.platform.egv.model.GiftVoucherStatusEnum;

/**
 * @author keith
 *
 */

public class GetInfoResponse implements Serializable{

	private String sessionToken;
	private GetInfoResponseStatusEnum responseStatus;
	
	private int id;
	private long number;
	private DateTime validFrom;
	private DateTime validTill;
	private int orderItemId;
	private String email;
	private int userId;
	private GiftVoucherStatusEnum gvStatus;
	private BigDecimal amount;


	public String getSessionToken() {
		return sessionToken;
	}

	public void setSessionToken(String sessionToken) {
		this.sessionToken = sessionToken;
	}

	public GetInfoResponseStatusEnum getResponseStatus() {
		return responseStatus;
	}

	public void setResponseStatus(GetInfoResponseStatusEnum responseStatus) {
		this.responseStatus = responseStatus;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public long getNumber() {
		return number;
	}

	public void setNumber(long number) {
		this.number = number;
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

	public int getOrderItemId() {
		return orderItemId;
	}

	public void setOrderItemId(int orderItemId) {
		this.orderItemId = orderItemId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public GiftVoucherStatusEnum getStatus() {
		return gvStatus;
	}

	public void setStatus(GiftVoucherStatusEnum status) {
		this.gvStatus = status;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	
}
