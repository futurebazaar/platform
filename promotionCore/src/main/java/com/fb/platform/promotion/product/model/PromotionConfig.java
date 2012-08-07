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

	private int promotionId = 0;
	private List<ConfigModule> modules = new ArrayList<ConfigModule>();
	private PriceApplicable priceApplicable = null;

	public boolean isApplicableOn(int productId) {
		for (ConfigModule module : modules) {
			if (module.isApplicableOn(productId)) {
				return true;
			}
		}
		return false;
	}

	public List<ConfigModule> getModules() {
		return modules;
	}

	public void setModules(List<ConfigModule> modules) {
		this.modules = modules;
	}

	public PriceApplicable getPriceApplicable() {
		return priceApplicable;
	}

	public void setPriceApplicable(PriceApplicable priceApplicable) {
		this.priceApplicable = priceApplicable;
	}

	public int getPromotionId() {
		return promotionId;
	}

	public void setPromotionId(int promotionId) {
		this.promotionId = promotionId;
	}

}
