package com.fb.platform.promotion.admin.to;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;

import com.fb.commons.to.PlatformMessage;

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
	
	public List<PlatformMessage> isValid() {
		List<PlatformMessage> requestInvalidationList = new ArrayList<PlatformMessage>();
		requestInvalidationList.addAll(isSessionTokenValid());
		requestInvalidationList.addAll(isLimitValid());
		return requestInvalidationList;
	}
	
	private List<PlatformMessage> isLimitValid() {
		List<PlatformMessage> limitInvalidationList = new ArrayList<PlatformMessage>();
		if(startRecord < 0) {
			limitInvalidationList.add(new PlatformMessage("EPA14", new Object[] {startRecord}));
		}
		if(batchSize <= 0) {
			limitInvalidationList.add(new PlatformMessage("EPA15", new Object[] {batchSize}));
		}
		return limitInvalidationList;
	}
	
	private List<PlatformMessage> isSessionTokenValid() {
		List<PlatformMessage> sessionInvalidationList = new ArrayList<PlatformMessage>();
		if(StringUtils.isBlank(sessionToken)) {
			sessionInvalidationList.add(new PlatformMessage("EPA1", null));
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
