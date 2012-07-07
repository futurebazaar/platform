/**
 * 
 */
package com.fb.platform.promotion.rule.config.type;

import java.math.BigDecimal;
import java.util.HashMap;

/**
 * @author keith
 *
 */
public class QuantityDiscountMap {

	private int maxSupportedQuantity = 0;
	private int minSupportedQuantity = 9999999; // Some Random Large number
	private HashMap<Integer, BigDecimal> quantityDiscountMap = null;	//map of quantity vs discount

	public boolean isQuantityApplicable(int quantity) {
		return (quantity >= minSupportedQuantity);
	}

	public BigDecimal getDiscount(int quantity) {
		if (quantity > maxSupportedQuantity) {
			quantity = maxSupportedQuantity;
		}
		return (quantityDiscountMap.get(quantity) == null)? BigDecimal.ZERO:(quantityDiscountMap.get(quantity));
	}

	public int getMaxSupportedQuantity() {
		return maxSupportedQuantity;
	}

	public void setMaxSupportedQuantity(int maxSupportedQuantity) {
		this.maxSupportedQuantity = maxSupportedQuantity;
	}

	public int getMinSupportedQuantity() {
		return minSupportedQuantity;
	}

	public void setMinSupportedQuantity(int minSupportedQuantity) {
		this.minSupportedQuantity = minSupportedQuantity;
	}

	public HashMap<Integer, BigDecimal> getQuantityDiscountMap() {
		return quantityDiscountMap;
	}

	public void setQuantityDiscountMap(
			HashMap<Integer, BigDecimal> quantityDiscountMap) {
		this.quantityDiscountMap = quantityDiscountMap;
	}
	
}
