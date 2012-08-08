/**
 * 
 */
package com.fb.platform.promotion.product.model;

import java.util.ArrayList;
import java.util.List;

import com.fb.platform.promotion.product.model.condition.OrderCondition;
import com.fb.platform.promotion.product.model.condition.ProductCondition;
import com.fb.platform.promotion.to.OrderRequest;

/**
 * @author vinayak
 *
 */
public class Conditions {

	private List<Condition> conditions = new ArrayList<Condition>();
	private List<ModuleJoin> joins = new ArrayList<ModuleJoin>();
	private OrderCondition orderCondition = null;
	public boolean prodCondition = false;

	public List<Condition> getConditions() {
		return conditions;
	}
	public void setConditions(List<Condition> conditions) {
		this.conditions = conditions;
		for (Condition condition : conditions) {
			if (condition instanceof OrderCondition) {
				this.orderCondition = (OrderCondition) condition;
			}
			if (condition instanceof ProductCondition) {
				prodCondition = true;
			}
		}
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

	public boolean isOrderValueConditionMet(OrderRequest orderRequest) {
		if (this.orderCondition == null) {
			return true;
		}
		return this.orderCondition.isOrderWithinLimits(orderRequest);
	}

	public boolean isProdCondition() {
		return this.prodCondition;
	}
	
	public int getMaxQuantity() {
		int maxQuantity = 0;
		for(Condition promotionCondition : conditions) {
			if(promotionCondition.getQuantity() > maxQuantity) {
				maxQuantity = promotionCondition.getQuantity();
			}
		}
		return maxQuantity;
	}
}
