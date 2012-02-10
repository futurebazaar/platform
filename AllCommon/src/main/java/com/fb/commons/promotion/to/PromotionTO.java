/**
 * 
 */
package com.fb.commons.promotion.to;

import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * This class represents the promotion data that gets transferred between web layer and manager layer.
 * 
 * @author Keith Fernandez
 *
 */
public class PromotionTO {

    private Integer promotionId;    
    private String appliesOn;
    private String discountType;
    private Double discountValue;
    private Double minAmountValue;
    private Integer maxUsesPerUser;
    private Integer maxUses;
    private Integer totalUses;
//    private String canBeClaimed;
    private String promotionName;
    private Date startDate;
    private Date endDate;
    private Date createdOn;
    private Date lastModifedOn;
    private String createdBy;
    private Double ceil;
    private boolean isActive;
    
    private Integer bundleId;
    private Integer discountBundleId;
    
    private String promotionType; 
    private PromotionBundleTO promotionBundle;
    private PromotionBundleTO discountBundle;
    private PromoValuesTO promoValue;
    private String couponCode;

	public Integer getPromotionId() {
		return promotionId;
	}

	public void setPromotionId(Integer promotionId) {
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

	public Double getDiscountValue() {
		return discountValue;
	}

	public void setDiscountValue(Double discountValue) {
		this.discountValue = discountValue;
	}

	public Double getMinAmountValue() {
		return minAmountValue;
	}

	public void setMinAmountValue(Double minAmountValue) {
		this.minAmountValue = minAmountValue;
	}

	public Integer getMaxUsesPerUser() {
		return maxUsesPerUser;
	}

	public void setMaxUsesPerUser(Integer maxUsesPerUser) {
		this.maxUsesPerUser = maxUsesPerUser;
	}

	public Integer getMaxUses() {
		return maxUses;
	}

	public void setMaxUses(Integer maxUses) {
		this.maxUses = maxUses;
	}

	public Integer getTotalUses() {
		return totalUses;
	}

	public void setTotalUses(Integer totalUses) {
		this.totalUses = totalUses;
	}

	public String getPromotionName() {
		return promotionName;
	}

	public void setPromotionName(String promotionName) {
		this.promotionName = promotionName;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public Date getLastModifedOn() {
		return lastModifedOn;
	}

	public void setLastModifedOn(Date lastModifedOn) {
		this.lastModifedOn = lastModifedOn;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Double getCeil() {
		return ceil;
	}

	public void setCeil(Double ceil) {
		this.ceil = ceil;
	}

	public Integer getBundleId() {
		return bundleId;
	}

	public void setBundleId(Integer bundleId) {
		this.bundleId = bundleId;
	}

	public Integer getDiscountBundleId() {
		return discountBundleId;
	}

	public void setDiscountBundleId(Integer discountBundleId) {
		this.discountBundleId = discountBundleId;
	}

	public String getPromotionType() {
		return promotionType;
	}

	public void setPromotionType(String promotionType) {
		this.promotionType = promotionType;
	}

	public PromotionBundleTO getPromotionBundle() {
		return promotionBundle;
	}

	public void setPromotionBundle(PromotionBundleTO promotionBundle) {
		this.promotionBundle = promotionBundle;
	}

	public PromotionBundleTO getDiscountBundle() {
		return discountBundle;
	}

	public void setDiscountBundle(PromotionBundleTO discountBundle) {
		this.discountBundle = discountBundle;
	}

	public String getCouponCode() {
		return couponCode;
	}

	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).append("promotionId is", this.getPromotionId()).toString();
	}
}
