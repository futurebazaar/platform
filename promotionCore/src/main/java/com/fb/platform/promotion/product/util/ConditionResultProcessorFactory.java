/**
 * 
 */
package com.fb.platform.promotion.product.util;

import com.fb.platform.promotion.product.model.ConfigModule;

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
			}
		}
		//TODO return a default implementation and log error
		return null;
	}

	private static boolean sameProducts(ConfigModule configModule) {
		//TODO match the sorted proucts ids in the product condition and product result.
		//if they are equal return true
		return true;
	}

}
