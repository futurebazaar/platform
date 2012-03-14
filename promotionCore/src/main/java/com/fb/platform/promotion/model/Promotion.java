/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fb.platform.promotion.model;

import java.sql.Timestamp;
import java.util.ArrayList;

/**
 * 
 * @author Keith Fernandez
 */
public class Promotion {
    private int promotionId;    
    private String appliesOn;
    
	private String createdBy;
    private Timestamp createdOn;
    
	private Timestamp validFrom;
    private Timestamp validTill;
    private Timestamp lastModifiedOn;

	private String displayText;
	private String promotionDescription;	
	private Timestamp lastUsedOn;
	private boolean isCoupon;
	private AmountType amountType;
    
    private PromotionType promotionType; 
    
    private boolean isActive;
    
	private String promotionName;
	private CouponUses promotionUses;

    private Priority priority;
    
	private ArrayList<PromotionValue> promoValues;
	 
	public ArrayList<PromotionValue> getPromoValues() {
		return promoValues;
	}

	public void setPromoValues(ArrayList<PromotionValue> promoValues) {
		this.promoValues = promoValues;
	}

	
    public Timestamp getValidFrom() {
		return validFrom;
	}

	public void setValidFrom(Timestamp validFrom) {
		this.validFrom = validFrom;
	}

	public Timestamp getValidTill() {
		return validTill;
	}

	public void setValidTill(Timestamp validTill) {
		this.validTill = validTill;
	}

	public String getDisplayText() {
		return displayText;
	}

	public void setDisplayText(String displayText) {
		this.displayText = displayText;
	}

	public String getPromotionDescription() {
		return promotionDescription;
	}

	public void setPromotionDescription(String description) {
		this.promotionDescription = description;
	}

	public Timestamp getLastUsedOn() {
		return lastUsedOn;
	}

	public void setLastUsedOn(Timestamp lastUsed) {
		this.lastUsedOn = lastUsed;
	}

	public boolean isCoupon() {
		return isCoupon;
	}

	public void setCoupon(boolean isCoupon) {
		this.isCoupon = isCoupon;
	}

	public AmountType getAmountType() {
		return amountType;
	}

	public void setAmountType(AmountType amtType) {
		this.amountType = amtType;
	}

	public PromotionType getPromotionType() {
		return promotionType;
	}

	public void setPromotionType(PromotionType promotionType) {
		this.promotionType = promotionType;
	}

	public boolean isActive() {
		return isActive;
	}
	
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public Timestamp getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Timestamp createdOn) {
		this.createdOn = createdOn;
	}

	public Timestamp getLastModifiedOn() {
		return lastModifiedOn;
	}

	public void setLastModifiedOn(Timestamp lastModifedOn) {
		this.lastModifiedOn = lastModifedOn;
	}
	
	public String getPromotionName() {
		return promotionName;
	}

	public void setPromotionName(String promotionName) {
		this.promotionName = promotionName;
	}

    public int getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(int promotionId) {
        this.promotionId = promotionId;
    }


//	@Override
//	/**
//	 * Convert object into json
//	 */
//	public JsonObject toJson() throws Exception {
//
//		String jsonString = gson.toJson(this);
//		JsonParser parser = new JsonParser();
//		JsonObject jsonObject = (JsonObject) parser.parse(jsonString);
//		return jsonObject;
//	}
	/**
	 * Converts json into object
	 * @param postData
	 * @return
	 */
//	public static PromotionBO fromJson(JsonObject postData) {
//		PromotionBO promotionBo = gson.fromJson(postData, PromotionBO.class);
//		return promotionBo;
//
//	}

//	@Override
//	public String toString() {
//		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
//		.append("promotionId", promotionId)
//		//.append("appliesOn", this.appliesOn)
//		.toString();
//	}

	public CouponUses getPromotionUses() {
		return promotionUses;
	}

	public void setPromotionUses(CouponUses promotionUses) {
		this.promotionUses = promotionUses;
	}

	public Priority getPriority() {
		return priority;
	}

	public void setPriority(Priority promotionPriority) {
		this.priority = promotionPriority;
	}
	
	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

    public String getAppliesOn() {
		return appliesOn;
	}

	public void setAppliesOn(String appliesOn) {
		this.appliesOn = appliesOn;
	}

	
}
