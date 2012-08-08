/**
 * 
 */
package com.fb.platform.promotion.product.model;

import com.fb.platform.promotion.to.OrderRequest;

/**
 * @author vinayak
 *
 */
public class ConfigModule implements Comparable<ConfigModule>{

	private Conditions conditions = null;
	private Results results = null;

	public Conditions getConditions() {
		return conditions;
	}
	public void setConditions(Conditions conditions) {
		this.conditions = conditions;
	}
	public Results getResults() {
		return results;
	}
	public void setResults(Results results) {
		this.results = results;
	}

	public boolean isApplicableOn(int productId) {
		return conditions.isApplicableOn(productId);
	}

	public boolean isOrderValueConditionMet(OrderRequest orderRequest) {
		return conditions.isOrderValueConditionMet(orderRequest);
	}

	public boolean isProdCondtion() {
		return conditions.isProdCondition();
	}

	public boolean isValueResult() {
		return results.isValueResult();
	}
	@Override
	public int compareTo(ConfigModule o) {
		int quantityDiff = o.getConditions().getMaxQuantity() - this.getConditions().getMaxQuantity();
		return quantityDiff;
	}
}
