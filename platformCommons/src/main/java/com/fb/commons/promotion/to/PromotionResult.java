package com.fb.commons.promotion.to;

import java.sql.Timestamp;
import java.util.ArrayList;

public class PromotionResult {

	private int id;
	private  int promoId;
	private String promoDesc;
	private String promoText;
	private String promoType;
	private String discType;
	private String discValue;
	private ArrayList<PromoValuesTO> promoValues;
	private String couponCode;
	private boolean isActive;
	private Timestamp validFrom;
	private Timestamp validTill;
	int discountAmount;
	
	public int getDiscountAmount() {
		return discountAmount;
	}
	public void setDiscountAmount(int discountAmount) {
		this.discountAmount = discountAmount;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getPromoId() {
		return promoId;
	}
	public void setPromoId(int promoId) {
		this.promoId = promoId;
	}
	public String getPromoDesc() {
		return promoDesc;
	}
	public void setPromoDesc(String promoDesc) {
		this.promoDesc = promoDesc;
	}
	public String getPromoText() {
		return promoText;
	}
	public void setPromoText(String promoText) {
		this.promoText = promoText;
	}
	public String getPromoType() {
		return promoType;
	}
	public void setPromoType(String promoType) {
		this.promoType = promoType;
	}
	public String getDiscType() {
		return discType;
	}
	public void setDiscType(String discType) {
		this.discType = discType;
	}
	public String getDiscValue() {
		return discValue;
	}
	public void setDiscValue(String discValue) {
		this.discValue = discValue;
	}
	public ArrayList<PromoValuesTO> getPromoValues() {
		return promoValues;
	}
	public void setPromoValues(ArrayList<PromoValuesTO> promoValues) {
		this.promoValues = promoValues;
	}
	public String getCouponCode() {
		return couponCode;
	}
	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}
	public boolean isActive() {
		return isActive;
	}
	public void setActive(boolean isActive) {
		this.isActive = isActive;
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
	
	
	
}
