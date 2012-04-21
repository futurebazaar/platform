/**
 * 
 */
package com.fb.platform.promotion.model.legacy;

import java.sql.Timestamp;
import java.util.List;

/**
 * @author vinayak
 *
 */
public class LegacyPromotion {
    private int promotionId;
    private String appliesOn;
    private String discountType;
    private String discountValue;
    private double minAmountValue;
    private int maxUsesPerUser;
    private int maxUsesPerCoupon;
    private int maxUses;
    private int totalUses;
    private String promotionName;
    private Timestamp startDate;
    private Timestamp endDate;
    private Timestamp createdOn;
    private Timestamp lastModifedOn;
    
    private String promotionType; 
    
    private int active;
    private int global;

    private List<LegacyPromotionCoupon> coupons = null;

    public int getPromotionId() {
		return promotionId;
	}
	public void setPromotionId(int promotionId) {
		this.promotionId = promotionId;
	}
	public String getAppliesOn() {
		return appliesOn;
	}
	public void setAppliesOn(String appliesOn) {
		this.appliesOn = appliesOn;
	}
	public String getDiscountType() {
		return discountType;
	}
	public void setDiscountType(String discountType) {
		this.discountType = discountType;
	}
	public String getDiscountValue() {
		return discountValue;
	}
	public void setDiscountValue(String discountValue) {
		this.discountValue = discountValue;
	}
	public double getMinAmountValue() {
		return minAmountValue;
	}
	public void setMinAmountValue(double minAmountValue) {
		this.minAmountValue = minAmountValue;
	}
	public int getMaxUsesPerUser() {
		return maxUsesPerUser;
	}
	public void setMaxUsesPerUser(int maxUsesPerUser) {
		this.maxUsesPerUser = maxUsesPerUser;
	}
	public int getMaxUsesPerCoupon() {
		return maxUsesPerCoupon;
	}
	public void setMaxUsesPerCoupon(int maxUsesPerCoupon) {
		this.maxUsesPerCoupon = maxUsesPerCoupon;
	}
	public int getMaxUses() {
		return maxUses;
	}
	public void setMaxUses(int maxUses) {
		this.maxUses = maxUses;
	}
	public int getTotalUses() {
		return totalUses;
	}
	public void setTotalUses(int totalUses) {
		this.totalUses = totalUses;
	}
	public String getPromotionName() {
		return promotionName;
	}
	public void setPromotionName(String promotionName) {
		this.promotionName = promotionName;
	}
	public Timestamp getStartDate() {
		return startDate;
	}
	public void setStartDate(Timestamp startDate) {
		if(startDate == null) {
			this.startDate = new Timestamp(System.currentTimeMillis());
		} else {
			this.startDate = startDate;
		}
	}
	public Timestamp getEndDate() {
		return endDate;
	}
	public void setEndDate(Timestamp endDate) {
		if(endDate == null) {
			this.endDate = new Timestamp(System.currentTimeMillis());
		} else {
			this.endDate = endDate;
		}
	}
	public Timestamp getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(Timestamp createdOn) {
		if(createdOn == null) {
			this.createdOn = new Timestamp(System.currentTimeMillis());
		} else {
			this.createdOn = createdOn;
		}
	}
	public Timestamp getLastModifedOn() {
		return lastModifedOn;
	}
	public void setLastModifedOn(Timestamp lastModifedOn) {
		if(lastModifedOn == null) {
			this.lastModifedOn = new Timestamp(System.currentTimeMillis());
		} else {
			this.lastModifedOn = lastModifedOn;
		}
	}
	public String getPromotionType() {
		return promotionType;
	}
	public void setPromotionType(String promotionType) {
		this.promotionType = promotionType;
	}
	public int getActive() {
		return active;
	}

	public boolean isActive() {
		if (active == 0) {
			return false;
		}
		return true;
	}

	public void setActive(int active) {
		this.active = active;
	}
	public int getGlobal() {
		return global;
	}
	public void setGlobal(int global) {
		this.global = global;
	}
	public List<LegacyPromotionCoupon> getCoupons() {
		return coupons;
	}
	public void setCoupons(List<LegacyPromotionCoupon> coupons) {
		this.coupons = coupons;
	}
}
