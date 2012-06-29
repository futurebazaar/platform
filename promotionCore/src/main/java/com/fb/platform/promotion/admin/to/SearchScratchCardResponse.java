/**
 * 
 */
package com.fb.platform.promotion.admin.to;

import java.io.Serializable;
import java.util.Date;

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
		private DateTime usedDate = null;
		private String scratchCardNumber = null;
		private String cardStatus = null;
		
	
		

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
		public DateTime getUsedDate() {
			return usedDate;
		}
		public void setUsedDate(DateTime usedDate) {
			this.usedDate = usedDate;
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
		
	}
