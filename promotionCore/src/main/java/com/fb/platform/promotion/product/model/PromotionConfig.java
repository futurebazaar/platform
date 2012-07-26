/**
 * 
 */
package com.fb.platform.promotion.product.model;

import java.util.ArrayList;
import java.util.List;

/**
 * The top level promotion config class.
 * Holds the Conditions and Results of the config relation.
 * 
 * @author vinayak
 *
 */
public class PromotionConfig {

	private List<ConfigModule> modules = new ArrayList<ConfigModule>();
	private PriceApplicable priceApplicable = null;

	public PromotionConfig getApplicableResults(int productId) {
		for (ConfigModule module : modules) {
			
		}
		return null;
	}

}
