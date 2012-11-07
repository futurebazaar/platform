/**
 * 
 */
package com.fb.platform.promotion.product.util;

import com.fb.platform.promotion.product.model.Condition;
import com.fb.platform.promotion.product.model.ConfigModule;
import com.fb.platform.promotion.product.model.Result;
import com.fb.platform.promotion.product.model.condition.ProductCondition;
import com.fb.platform.promotion.product.model.result.ProductResult;

/**
 * @author vinayak
 *
 */
public class ConditionResultProcessorFactory {

	public static ConditionResultProcessor get(ConfigModule configModule) {
		if (configModule.isProdCondtion() && configModule.isValueResult()) {
			return new ProdConditionValueResult(configModule);
		} else if (configModule.isProdCondtion() && configModule.isProdResult()) {
			boolean sameProducts = sameProducts(configModule);
			if (sameProducts) {
				return new SameProdConditionProdResult(configModule);
			} else {
				return new ProdConditionProdResult(configModule);
			}
		}
		//TODO return a default implementation and log error
		return null;
	}

	private static boolean sameProducts(ConfigModule configModule) {
		//match the sorted proucts ids in the product condition and product result.
		//if they are equal return true
		ProductCondition productCondition = null;
		ProductResult productResult = null;
		for (Condition condition : configModule.getConditions().getConditions()) {
			if (condition instanceof ProductCondition) {
				productCondition = (ProductCondition) condition;
			}
		}
		for (Result result : configModule.getResults().getResults()) {
			if (result instanceof ProductResult) {
				productResult = (ProductResult) result;
			}
		}

		if (productCondition.getProductIds().equals(productResult.getProductIds())) {
			return true;
		}
		return false;
	}

}
