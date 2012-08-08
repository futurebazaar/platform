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
		}
		return null;
	}
}
