/**
 * 
 */
package com.fb.platform.promotion.admin.to;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * @author ashish
 *
 */
public class ViewCouponRequest implements Serializable {

	private String sessionToken;
	private Integer couponId;
	private String couponCode;
	
	public String getSessionToken() {
		return sessionToken;
	}
	public void setSessionToken(String sessionToken) {
		this.sessionToken = sessionToken;
	}
	public Integer getCouponId() {
		return couponId;
	}
	public void setCouponId(Integer couponId) {
		this.couponId = couponId;
	}
	public String getCouponCode() {
		return couponCode;
	}
	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}
	
	public String validate() {
		List<String> requestInvalidationList = new ArrayList<String>();
		requestInvalidationList.addAll(isSessionTokenValid());
		requestInvalidationList.addAll(isInputValid());
		return StringUtils.join(requestInvalidationList.toArray(), ",");
	}
	
	private List<String> isSessionTokenValid() {
		List<String> sessionInvalidationList = new ArrayList<String>();
		if(StringUtils.isBlank(sessionToken)) {
			sessionInvalidationList.add("Session token cannot be empty");
		}
		return sessionInvalidationList;
	}
	
	private List<String> isInputValid() {
		List<String> sessionInvalidationList = new ArrayList<String>();
		if(StringUtils.isBlank(couponCode) && couponId==null) {
			sessionInvalidationList.add("Coupon Code and coupon Id both cannot be invalid");
		}
		return sessionInvalidationList;
	}
}
