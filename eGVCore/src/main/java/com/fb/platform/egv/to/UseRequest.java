/**
 * 
 */
package com.fb.platform.egv.to;

import java.math.BigDecimal;
import java.util.HashMap;

/**
 * @author keith
 * 
 */
public class UseRequest implements GiftVoucherRequest {

	private String sessionToken;
	private HashMap<Long, BigDecimal> giftVoucherDetails;
	private int orderId;

	public UseRequest() {
		giftVoucherDetails = new HashMap<Long, BigDecimal>();
	}

	public String getSessionToken() {
		return sessionToken;
	}

	public void setSessionToken(String sessionToken) {
		this.sessionToken = sessionToken;
	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public HashMap<Long, BigDecimal> getGiftVoucherDetails() {
		return giftVoucherDetails;
	}

	public void setGiftVoucherDetails(HashMap<Long, BigDecimal> giftVoucherDetails) {
		this.giftVoucherDetails = giftVoucherDetails;
	}

	public void addGiftVoucherDetails(long gvNumber, BigDecimal amount) {
		this.giftVoucherDetails.put(Long.valueOf(gvNumber), amount);
	}

}
