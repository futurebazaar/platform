/**
 * 
 */
package com.fb.platform.promotion.admin.to;

import java.io.Serializable;
import java.util.Date;

import javax.xml.datatype.XMLGregorianCalendar;

import org.joda.time.DateTime;

/**
 * @author SalimM
 *
 */
public class SearchScratchCardResponse implements Serializable{

		private String sessionToken = null;
		private SearchScratchCardStatusEnum status = null;
		//private String status = null;
		private String email = null;
		private String mobile  = null;
		private String user = null;
		private XMLGregorianCalendar usedDate = null;
		private String scratchCardNumber = null;
		private String cardStatus = null;
		private int userId = 0;
		private String store = null;
		private String couponCode = null; 
		private XMLGregorianCalendar timeStamp = null;
				
		
		
	
		

		private String errorCause;
		
		public String getSessionToken() {
			return sessionToken;
		}
		public void setSessionToken(String sessionToken) {
			this.sessionToken = sessionToken;
		}
		
		public String getErrorCause() {
			return errorCause;
		}
		public void setErrorCause(String errorCause) {
			this.errorCause = errorCause;
		}
		
		public SearchScratchCardStatusEnum getStatus() {
			return status;
		}
		
		public void setStatus(SearchScratchCardStatusEnum status) {
			this.status = status;
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
		public String getUser() {
			return user;
		}
		public void setUser(String user) {
			this.user = user;
		}
		public XMLGregorianCalendar getUsedDate() {
			return usedDate;
		}
		public void setUsedDate(XMLGregorianCalendar dateTime) {
			this.usedDate = dateTime;
		}
		public String getScratchCardNumber() {
			return scratchCardNumber;
		}
		public void setScratchCardNumber(String scratchCardNumber) {
			this.scratchCardNumber = scratchCardNumber;
		}
		
		public String getCardStatus() {
			return cardStatus;
		}
		
		public void setCardStatus(String cardStatus) {
			this.cardStatus = cardStatus;
		}
		public int getUserId() {
			return userId;
		}
		public void setUserId(int userId) {
			this.userId = userId;
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
		public XMLGregorianCalendar getTimeStamp() {
			return timeStamp;
		}
		public void setTimeStamp(XMLGregorianCalendar timeStamp) {
			this.timeStamp = timeStamp;
		}

		
		
		
		
	}