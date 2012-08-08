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
		this.modules = sortModules(modules);
		//this.modules = modules;
	}

	private List<ConfigModule> sortModules(List<ConfigModule> dbModules) {
		//sort the modules based on the quantity and order max amount, the highest first.
		//this is required since highest quantity and order amount results should be appllied first
		//this also assumes that whenever there are multiple modules, they will only contain
		//a similar set of products, categories, brand conditions. the only variable will be 
		//quantity or order max amount
		
		return null;
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
