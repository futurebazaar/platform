/**
 * 
 */
package com.fb.platform.promotion.product.model.condition;

import com.fb.commons.to.Money;
import com.fb.platform.promotion.product.model.Condition;
import com.fb.platform.promotion.to.OrderRequest;

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
	@Override
	public boolean isApplicableOn(int productId) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isOrderWithinLimits(OrderRequest orderRequest) {
		Money orderValue = new Money(orderRequest.getOrderValue());
		if (minimumOrderValue != null) {
			if (orderValue.lt(minimumOrderValue)) {
				return false;
			}
		}
		if (maximumOrderValue != null) {
			if (orderValue.gt(maximumOrderValue)) {
				return false;
			}
		}
		return true;
	}
}
