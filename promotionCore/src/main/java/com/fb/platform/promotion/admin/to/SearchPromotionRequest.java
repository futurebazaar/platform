package com.fb.platform.promotion.admin.to;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;

/**
 * @author nehaga
 *
 */
public class SearchPromotionRequest {
	
	private String sessionToken = null;
	private String promotionName;
	private DateTime validFrom;
	private DateTime validTill;
	private boolean isActive;
	private SearchPromotionOrderBy searchPromotionOrderBy;
	private SortOrder sortOrder;
	private int startRecord;
	private int batchSize;
	
	
	public String getSessionToken() {
		return sessionToken;
	}
	public void setSessionToken(String sessionToken) {
		this.sessionToken = sessionToken;
	}
	public String getPromotionName() {
		return promotionName;
	}
	public void setPromotionName(String promotionName) {
		this.promotionName = promotionName;
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
	
	public String isValid() {
		List<String> requestInvalidationList = new ArrayList<String>();
		requestInvalidationList.addAll(isSessionTokenValid());
		requestInvalidationList.addAll(isLimitValid());
		return StringUtils.join(requestInvalidationList.toArray(), ",");
	}
	
	private List<String> isLimitValid() {
		List<String> limitInvalidationList = new ArrayList<String>();
		if(startRecord < 0) {
			limitInvalidationList.add("Start record cannot be negative");
		}
		if(batchSize <= 0) {
			limitInvalidationList.add("Batch size cannot be negative or zero");
		}
		return limitInvalidationList;
	}
	
	private List<String> isSessionTokenValid() {
		List<String> sessionInvalidationList = new ArrayList<String>();
		if(StringUtils.isBlank(sessionToken)) {
			sessionInvalidationList.add("Session token cannot be empty");
		}
		return sessionInvalidationList;
	}
	public boolean isActive() {
		return isActive;
	}
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
	public SearchPromotionOrderBy getSearchPromotionOrderBy() {
		return searchPromotionOrderBy;
	}
	public void setSearchPromotionOrderBy(
			SearchPromotionOrderBy searchPromotionOrderBy) {
		this.searchPromotionOrderBy = searchPromotionOrderBy;
	}
	public SortOrder getSortOrder() {
		return sortOrder;
	}
	public void setSortOrder(SortOrder sortOrder) {
		this.sortOrder = sortOrder;
	}
	
}
