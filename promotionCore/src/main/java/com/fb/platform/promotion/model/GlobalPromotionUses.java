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
public class GlobalPromotionUses implements Serializable {

	private int promotionId = 0;
	private int currentCount = 0;
	private Money currentAmount = null;

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
}
