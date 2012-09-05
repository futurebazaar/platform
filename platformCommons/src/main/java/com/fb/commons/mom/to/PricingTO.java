package com.fb.commons.mom.to;

import java.io.Serializable;
import java.math.BigDecimal;

public class PricingTO implements Serializable {
	
	private BigDecimal extraDiscount;
	private BigDecimal couponDiscount;
	private BigDecimal shippingAmount;
	private BigDecimal payableAmount;
	private BigDecimal offerPrice;
	private BigDecimal listPrice;
	private String currency;
	private BigDecimal pointsEarn;
	private BigDecimal pointsBurn;
	private BigDecimal pointsEarnValue;
	private BigDecimal pointsBurnValue;
	private BigDecimal commissionAmount;
	private BigDecimal nlc;
	private BigDecimal warrantyPrice;
	
	public BigDecimal getExtraDiscount() {
		return extraDiscount;
	}
	public void setExtraDiscount(BigDecimal extraDiscount) {
		this.extraDiscount = extraDiscount;
	}
	public BigDecimal getCouponDiscount() {
		return couponDiscount;
	}
	public void setCouponDiscount(BigDecimal couponDiscount) {
		this.couponDiscount = couponDiscount;
	}
	public BigDecimal getShippingAmount() {
		return shippingAmount;
	}
	public void setShippingAmount(BigDecimal shippingAmount) {
		this.shippingAmount = shippingAmount;
	}
	public BigDecimal getPayableAmount() {
		return payableAmount;
	}
	public void setPayableAmount(BigDecimal payableAmount) {
		this.payableAmount = payableAmount;
	}
	public BigDecimal getOfferPrice() {
		return offerPrice;
	}
	public void setOfferPrice(BigDecimal offerPrice) {
		this.offerPrice = offerPrice;
	}
	public BigDecimal getListPrice() {
		return listPrice;
	}
	public void setListPrice(BigDecimal listPrice) {
		this.listPrice = listPrice;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public BigDecimal getPointsEarn() {
		return pointsEarn;
	}
	public void setPointsEarn(BigDecimal pointsEarn) {
		this.pointsEarn = pointsEarn;
	}
	public BigDecimal getPointsBurn() {
		return pointsBurn;
	}
	public void setPointsBurn(BigDecimal pointsBurn) {
		this.pointsBurn = pointsBurn;
	}
	public BigDecimal getPointsEarnValue() {
		return pointsEarnValue;
	}
	public void setPointsEarnValue(BigDecimal pointsEarnValue) {
		this.pointsEarnValue = pointsEarnValue;
	}
	public BigDecimal getPointsBurnValue() {
		return pointsBurnValue;
	}
	public void setPointsBurnValue(BigDecimal pointsBurnValue) {
		this.pointsBurnValue = pointsBurnValue;
	}
	public BigDecimal getCommissionAmount() {
		return commissionAmount;
	}
	public void setCommissionAmount(BigDecimal commissionAmount) {
		this.commissionAmount = commissionAmount;
	}
	public BigDecimal getNlc() {
		return nlc;
	}
	public void setNlc(BigDecimal nlc) {
		this.nlc = nlc;
	}
	public BigDecimal getWarrantyPrice() {
		return warrantyPrice;
	}
	public void setWarrantyPrice(BigDecimal warrantyPrice) {
		this.warrantyPrice = warrantyPrice;
	}
	
	@Override
	public String toString() {
		String order = "\nExtra discount: " + extraDiscount
				+ "\nCoupon discount: " + couponDiscount
				+ "\nShipping amount: " + shippingAmount
				+ "\nOffer price: " + offerPrice
				+ "\nPayable amount: " + payableAmount
				+ "\nList price: " + listPrice
				+ "\nCurrency: " + currency
				+ "\nPoints earned: " + pointsEarn
				+ "\nPoints earn value: " + pointsEarnValue
				+ "\nPoints burned: " + pointsBurn
				+ "\nPoints burn value: " + pointsBurnValue
				+ "\nCommission : " + commissionAmount
				+ "\nNLC : " + nlc
				+ "\nWarranty price: " + warrantyPrice;
		
		return order;
	}
	
}
