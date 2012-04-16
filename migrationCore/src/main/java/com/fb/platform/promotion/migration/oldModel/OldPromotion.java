package com.fb.platform.promotion.migration.oldModel;

import java.sql.Timestamp;

/**
 * 
 * @author Keith
 */
public class OldPromotion {
    private int promotionId;    
    private String appliesOn;
    private String discountType;
    private double discountValue;
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
 
	public String getPromotionType() {
		return promotionType;
	}

	public void setPromotionType(String promotionType) {
		this.promotionType = promotionType;
	}

	public String getAppliesOn() {
		return appliesOn;
	}

	public void setAppliesOn(String appliesOn) {
		this.appliesOn = appliesOn;
	}

	public Timestamp getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Timestamp createdOn) {
		this.createdOn = createdOn;
	}

	public String getDiscountType() {
		return discountType;
	}

	public void setDiscountType(String discountType) {
		this.discountType = discountType;
	}

	public double getDiscountValue() {
		return discountValue;
	}

	public void setDiscountValue(double discountValue) {
		this.discountValue = discountValue;
	}

	public Timestamp getEndDate() {
		return endDate;
	}

	public void setEndDate(Timestamp endDate) {
		this.endDate = endDate;
	}

	public Timestamp getLastModifedOn() {
		return lastModifedOn;
	}

	public void setLastModifedOn(Timestamp lastModifedOn) {
		this.lastModifedOn = lastModifedOn;
	}

	public int getMaxUses() {
		return maxUses;
	}

	public void setMaxUses(int maxUses) {
		this.maxUses = maxUses;
	}

	public int getMaxUsesPerUser() {
		return maxUsesPerUser;
	}

	public void setMaxUsesPerUser(int maxUsesPerUser) {
		this.maxUsesPerUser = maxUsesPerUser;
	}

	public double getMinAmountValue() {
		return minAmountValue;
	}

	public void setMinAmountValue(double minAmountValue) {
		this.minAmountValue = minAmountValue;
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
		this.startDate = startDate;
	}

	public int getTotalUses() {
		return totalUses;
	}

	public void setTotalUses(int totalUses) {
		this.totalUses = totalUses;
	}

    public int getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(int promotionId) {
        this.promotionId = promotionId;
    }


	public int getGlobal() {
		return global;
	}

	public void setGlobal(int global) {
		this.global = global;
	}


public int getActive() {
		return active;
	}

	public void setActive(int active) {
		this.active = active;
	}

	public int getMaxUsesPerCoupon() {
		return maxUsesPerCoupon;
	}

	public void setMaxUsesPerCoupon(int maxUsesPerCoupon) {
		this.maxUsesPerCoupon = maxUsesPerCoupon;
	}
	
	
	
}
