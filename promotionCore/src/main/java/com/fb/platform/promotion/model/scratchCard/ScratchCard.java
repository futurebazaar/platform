/**
 * 
 */
package com.fb.platform.promotion.model.scratchCard;

import java.io.Serializable;

import org.joda.time.DateTime;

/**
 * @author vinayak
 *
 */
public class ScratchCard implements Serializable {

	private int id = 0;
	private boolean isActive = false;
	private int userId = 0;
	private String cardNumber = null;
	private String store = null;
	private String couponCode = null;
	private String mobile = null;
	private String email = null;
	private String name = null;
	private DateTime timestamp = null;
	private DateTime usedDate = null; 
	private String user = null;
	private String cardStatus = null;
			
		
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public boolean isActive() {
		return isActive;
	}
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
	public String getCardNumber() {
		return cardNumber;
	}
	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}
	public String getStore() {
		return store;
	}
	public void setStore(String store) {
		this.store = store;
	}
	public String getCouponCode() {
		return couponCode;
	}
	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public DateTime getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(DateTime timestamp) {
		this.timestamp = timestamp;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public DateTime getUsedDate() {
		return usedDate;
	}
	public void setUsedDate(DateTime usedDate) {
		this.usedDate = usedDate;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getCardStatus() {
		return cardStatus;
	}
	public void setCardStatus(String cardStatus) {
		this.cardStatus = cardStatus;
	}
}
