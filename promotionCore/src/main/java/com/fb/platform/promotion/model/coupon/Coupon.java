/**
 * 
 */
package com.fb.platform.promotion.model.coupon;

import java.io.Serializable;

/**
 * @author vinayak
 *
 */
public class Coupon implements Serializable {

	private int id;
	private String code;
	private int promotionId;
	private CouponType type;
	private CouponLimitsConfig limitsConfig;

	public int getId() {
		return id;
	}
	public CouponType getType() {
		return type;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setPromotionId(int promotionId) {
		this.promotionId = promotionId;
	}
	public void setType(CouponType type) {
		this.type = type;
	}
	public void setLimitsConfig(CouponLimitsConfig limitsConfig) {
		this.limitsConfig = limitsConfig;
	}
	public int getPromotionId() {
		return promotionId;
	}
	public CouponLimitsConfig getLimitsConfig() {
		return limitsConfig;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
}
