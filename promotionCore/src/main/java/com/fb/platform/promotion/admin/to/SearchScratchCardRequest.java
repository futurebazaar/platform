/**
 * 
 */
package com.fb.platform.promotion.admin.to;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

	
	/**
	 * @author SalimM
	 *
	 */


	public class SearchScratchCardRequest {
		
		private String sessionToken;
		private String scratchCardNumber;
		
		public String getScratchCardNumber() {
			return scratchCardNumber;
		}
		public void setScratchCardNumber(String scratchCardNumber) {
			this.scratchCardNumber = scratchCardNumber;
		}

		
		public String getSessionToken() {
			return sessionToken;
		}
		public void setSessionToken(String sessionToken) {
			this.sessionToken = sessionToken;
		}
		public String validate() {
			List<String> requestInvalidationList = new ArrayList<String>();
			requestInvalidationList.addAll(isSessionTokenValid());
			requestInvalidationList.addAll(validateInput());
			return StringUtils.join(requestInvalidationList.toArray(), ",");
		}
		
		
		private List<String> isSessionTokenValid() {
			List<String> sessionInvalidationList = new ArrayList<String>();
			if(StringUtils.isBlank(sessionToken)) {
				sessionInvalidationList.add("Session token cannot be empty");
			}
			return sessionInvalidationList;
		}
		
		private List<String> validateInput() {
			List<String> sessionInvalidationList = new ArrayList<String>();
			if(StringUtils.isBlank(scratchCardNumber) ) {
				sessionInvalidationList.add("No search criteria found");
			}
			return sessionInvalidationList;
		}

		
		
		

	}
