package com.fb.platform.promotion.admin.to;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

/**
 * @author ashish
 *
 */
public class SearchCouponRequest {
	
	private String sessionToken;
	private String couponCode;
	private String userName;
	private SearchCouponOrderBy orderBy;
	private	SortOrder sortOrder;
	private int startRecord;
	private int batchSize;
	
	private final int DEFAULT_BATCH_SIZE = 100;
	private final int MAX_BATCH_SIZE = 1000;
	
	public String getSessionToken() {
		return sessionToken;
	}
	public void setSessionToken(String sessionToken) {
		this.sessionToken = sessionToken;
	}
	public String getCouponCode() {
		return couponCode;
	}
	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public int getStartRecord() {
		return startRecord;
	}
	public void setStartRecord(int startRecord) {
		this.startRecord = startRecord;
	}
	public int getBatchSize() {
		return batchSize;
	}
	public void setBatchSize(int batchSize) {
		this.batchSize = batchSize;
	}
	
	public String validate() {
		List<String> requestInvalidationList = new ArrayList<String>();
		checkLimits();
		requestInvalidationList.addAll(isSessionTokenValid());
		requestInvalidationList.addAll(validateInput());
		return StringUtils.join(requestInvalidationList.toArray(), ",");
	}
	
	private void checkLimits() {
		if(startRecord < 0) {
			startRecord = 0;
		}
		if(batchSize <= 0) {
			batchSize = DEFAULT_BATCH_SIZE;
		}else if(batchSize > MAX_BATCH_SIZE){
			batchSize = MAX_BATCH_SIZE;
		}
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
		if(StringUtils.isBlank(couponCode) && StringUtils.isBlank(userName)) {
			sessionInvalidationList.add("No search criteria found");
		}
		return sessionInvalidationList;
	}
	public SearchCouponOrderBy getOrderBy() {
		return orderBy;
	}
	public void setOrderBy(SearchCouponOrderBy orderBy) {
		this.orderBy = orderBy;
	}
	public SortOrder getSortOrder() {
		return sortOrder;
	}
	public void setSortOrder(SortOrder sortOrder) {
		this.sortOrder = sortOrder;
	}
}
