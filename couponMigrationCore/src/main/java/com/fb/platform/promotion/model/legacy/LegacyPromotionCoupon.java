/**
 * 
 */
package com.fb.platform.promotion.model.legacy;

import java.util.List;

/**
 * @author vinayak
 *
 */
public class LegacyPromotionCoupon {

	private String couponCode;
	private int clientId;
	private int promotionId;

	private List<LegacyCouponUser> couponUsers = null;

	public String getCouponCode() {
		return couponCode;
	}
	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}
	public int getClientId() {
		return clientId;
	}
	public void setClientId(int clientId) {
		this.clientId = clientId;
	}
	public int getPromotionId() {
		return promotionId;
	}
	public void setPromotionId(int promotionId) {
		this.promotionId = promotionId;
	}
	public List<LegacyCouponUser> getCouponUsers() {
		return couponUsers;
	}
	public void setCouponUsers(List<LegacyCouponUser> couponUsers) {
		this.couponUsers = couponUsers;
	}
}
