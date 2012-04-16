/**
 * 
 */
package com.fb.platform.promotion.to;


/**
 * @author vinayak
 *
 */
public class ApplyScratchCardRequest {

	private String cardNumber;
    private String sessionToken;

    public String getCardNumber() {
		return cardNumber;
	}
	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}
	public String getSessionToken() {
		return sessionToken;
	}
	public void setSessionToken(String sessionToken) {
		this.sessionToken = sessionToken;
	}
}
