/**
 * 
 */
package com.fb.platform.promotion.product.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author vinayak
 *
 */
public class Conditions {

	private List<Condition> conditions = new ArrayList<Condition>();
	private List<ModuleJoin> joins = new ArrayList<ModuleJoin>();

	public List<Condition> getConditions() {
		return conditions;
	}
	public void setConditions(List<Condition> conditions) {
		this.conditions = conditions;
	}
	public List<ModuleJoin> getJoins() {
		return joins;
	}
	public void setJoins(List<ModuleJoin> joins) {
		this.joins = joins;
	}

	public boolean isApplicableOn(int productId) {
		for (Condition condition : conditions) {
			if (condition.isApplicableOn(productId)) {
				return true;
			}
		}
		return false;
	}
}
