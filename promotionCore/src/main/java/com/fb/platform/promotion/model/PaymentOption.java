/**
 * 
 */
package com.fb.platform.promotion.model;

import java.util.List;

/**
 * @author keith
 * 
 */
public class PaymentOption {

	private List<String> includeList;
	private List<String> excludeList;

	public List<String> getIncludeList() {
		return includeList;
	}

	public void setIncludeList(List<String> includeList) {
		this.includeList = includeList;
	}

	public List<String> getExcludeList() {
		return excludeList;
	}

	public void setExcludeList(List<String> excludeList) {
		this.excludeList = excludeList;
	}

}
