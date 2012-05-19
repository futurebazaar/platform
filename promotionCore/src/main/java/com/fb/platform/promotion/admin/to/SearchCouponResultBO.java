/**
 * 
 */
package com.fb.platform.promotion.admin.to;

import java.io.Serializable;
import java.util.Set;

/**
 * @author ashish
 *
 */
public class SearchCouponResultBO implements Serializable{

	private Set<CouponBasicDetails> couponBasicDetailsSet;
	private int totalCount;
	
	public Set<CouponBasicDetails> getCouponBasicDetailsSet() {
		return couponBasicDetailsSet;
	}
	public void setCouponBasicDetailsSet(
			Set<CouponBasicDetails> couponBasicDetailsSet) {
		this.couponBasicDetailsSet = couponBasicDetailsSet;
	}
	public int getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
}
