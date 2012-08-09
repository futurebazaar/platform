/**
 * 
 */
package com.fb.platform.promotion.product.model.result;

import com.fb.commons.to.Money;
import com.fb.platform.promotion.product.model.Result;
import com.fb.platform.promotion.product.model.OfferType;

/**
 * @author vinayak
 *
 */
public class AbstractResult implements Result {

	private OfferType offerType = null;
	private Money offerValue = null;
	private int quantity = 0;

	public Money getOfferValue() {
		return offerValue;
	}
	public void setOfferValue(Money offerValue) {
		this.offerValue = offerValue;
	}
	public OfferType getOfferType() {
		return offerType;
	}
	public void setOfferType(OfferType offerType) {
		this.offerType = offerType;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
}
