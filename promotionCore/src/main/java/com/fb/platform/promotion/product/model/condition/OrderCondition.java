/**
 * 
 */
package com.fb.platform.promotion.product.model.condition;

import com.fb.commons.to.Money;
import com.fb.platform.promotion.product.model.Condition;

/**
 * @author vinayak
 *
 */
public class OrderCondition implements Condition {

	private Money minimumOrderValue = null;
	private Money maximumOrderValue = null;

	public Money getMinimumOrderValue() {
		return minimumOrderValue;
	}
	public void setMinimumOrderValue(Money minimumOrderValue) {
		this.minimumOrderValue = minimumOrderValue;
	}
	public Money getMaximumOrderValue() {
		return maximumOrderValue;
	}
	public void setMaximumOrderValue(Money maximumOrderValue) {
		this.maximumOrderValue = maximumOrderValue;
	}
}
