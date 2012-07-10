/**
 * 
 */
package com.fb.platform.promotion.model;

import java.io.Serializable;

import com.fb.commons.to.Money;

/**
 * @author vinayak
 *
 */
public class UserPromotionUses implements Serializable {

	private int promotionId = 0;
	private int userId = 0;
	private int currentCount;
	private Money currentAmount;
	private int noOfUseInMonth = 0;

	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getCurrentCount() {
		return currentCount;
	}
	public void setCurrentCount(int currentCount) {
		this.currentCount = currentCount;
	}
	public Money getCurrentAmount() {
		return currentAmount;
	}
	public void setCurrentAmount(Money currentAmount) {
		this.currentAmount = currentAmount;
	}
	public int getPromotionId() {
		return promotionId;
	}
	public void setPromotionId(int promotionId) {
		this.promotionId = promotionId;
	}
	
	public void increment(Money discountAmount){
		this.currentAmount = this.currentAmount.plus(discountAmount);
		this.currentCount++;
	}
	
	public void decrement(Money discountAmount){
		this.currentAmount = this.currentAmount.minus(discountAmount);
		this.currentCount--;
	}
	public int getNoOfUseInMonth() {
		return noOfUseInMonth;
	}
	public void setNoOfUseInMonth(int noOfUseInMonth) {
		this.noOfUseInMonth = noOfUseInMonth;
	}
}
