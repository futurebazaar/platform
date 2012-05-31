/**
 * 
 */
package com.fb.platform.egv.to;

import java.math.BigDecimal;

import com.fb.commons.to.Money;
import com.fb.platform.egv.model.GiftVoucherDates;
import com.fb.platform.egv.model.GiftVoucherStatusEnum;

/**
 * @author keith
 *
 */

public class GetGiftVoucherInfoResponse {

	private String sessionToken;
	private GetGiftVoucherInfoResponseStatusEnum responseStatus;
	
	private int id;
	private String number;
	private int pin;
	private GiftVoucherDates dates;
	private int orderItemId;
	private String email;
	private int userId;
	private GiftVoucherStatusEnum status;
	private BigDecimal amount;


	public String getSessionToken() {
		return sessionToken;
	}

	public void setSessionToken(String sessionToken) {
		this.sessionToken = sessionToken;
	}

	public GetGiftVoucherInfoResponseStatusEnum getResponseStatus() {
		return responseStatus;
	}

	public void setResponseStatus(GetGiftVoucherInfoResponseStatusEnum responseStatus) {
		this.responseStatus = responseStatus;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public int getPin() {
		return pin;
	}

	public void setPin(int pin) {
		this.pin = pin;
	}

	public GiftVoucherDates getDates() {
		return dates;
	}

	public void setDates(GiftVoucherDates dates) {
		this.dates = dates;
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
		return status;
	}

	public void setStatus(GiftVoucherStatusEnum status) {
		this.status = status;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	
}
