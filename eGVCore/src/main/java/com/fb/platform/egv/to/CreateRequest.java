/**
 * 
 */
package com.fb.platform.egv.to;

import java.math.BigDecimal;

/**
 * @author keith
 *
 */
public class CreateRequest {

	private String sessionToken;
	private int orderItemId;
	private String email;
	private BigDecimal amount;

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
	
}
