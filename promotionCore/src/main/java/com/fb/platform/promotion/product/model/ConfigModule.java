/**
 * 
 */
package com.fb.platform.promotion.product.model;

/**
 * @author vinayak
 *
 */
public class ConfigModule {

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
}
