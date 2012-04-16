/**
 * 
 */
package com.fb.platform.promotion.migration.oldModel;

/**
 * @author keith
 *
 */
public class OldPromoCoupon {

	private String couponCode;
	private int clientId;
	private int promotionId;
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
	
}
