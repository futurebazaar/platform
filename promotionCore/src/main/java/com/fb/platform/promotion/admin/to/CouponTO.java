/**
 * 
 */
package com.fb.platform.promotion.admin.to;

import java.io.Serializable;

import org.joda.time.DateTime;

import com.fb.commons.to.Money;
import com.fb.platform.promotion.model.coupon.CouponType;

/**
 * @author ashish
 *
 */
public class CouponTO implements Serializable {

	private int couponId;
	private CouponType couponType;
	private int promotionId;
	private String couponCode;
	private int maxUses;
	private Money maxAmount;
	private int maxUsesPerUser;
	private Money maxAmountPerUser;
	private DateTime createdOn;
	private DateTime lastModifiedOn;
	
	public int getCouponId() {
		return couponId;
	}
	public void setCouponId(int couponId) {
		this.couponId = couponId;
	}
	public CouponType getCouponType() {
		return couponType;
	}
	public void setCouponType(CouponType couponType) {
		this.couponType = couponType;
	}
	public int getPromotionId() {
		return promotionId;
	}
	public void setPromotionId(int promotionId) {
		this.promotionId = promotionId;
	}
	public String getCouponCode() {
		return couponCode;
	}
	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}
	public int getMaxUses() {
		return maxUses;
	}
	public void setMaxUses(int maxUses) {
		this.maxUses = maxUses;
	}
	public Money getMaxAmount() {
		return maxAmount;
	}
	public void setMaxAmount(Money maxAmount) {
		this.maxAmount = maxAmount;
	}
	public int getMaxUsesPerUser() {
		return maxUsesPerUser;
	}
	public void setMaxUsesPerUser(int maxUsesPerUser) {
		this.maxUsesPerUser = maxUsesPerUser;
	}
	public Money getMaxAmountPerUser() {
		return maxAmountPerUser;
	}
	public void setMaxAmountPerUser(Money maxAmountPerUser) {
		this.maxAmountPerUser = maxAmountPerUser;
	}
	public DateTime getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(DateTime createdOn) {
		this.createdOn = createdOn;
	}
	public DateTime getLastModifiedOn() {
		return lastModifiedOn;
	}
	public void setLastModifiedOn(DateTime lastModifiedOn) {
		this.lastModifiedOn = lastModifiedOn;
	}
}
