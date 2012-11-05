/**
 * 
 */
package com.fb.platform.promotion.product.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.fb.platform.promotion.product.util.ConditionResultProcessor;
import com.fb.platform.promotion.product.util.ConditionResultProcessorFactory;
import com.fb.platform.promotion.product.util.OrderItemPriceDistributor;
import com.fb.platform.promotion.to.ConfigResultApplyStatusEnum;
import com.fb.platform.promotion.to.OrderItem;
import com.fb.platform.promotion.to.OrderItemPromotionApplicationEnum;
import com.fb.platform.promotion.to.OrderRequest;

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
		
		Collections.sort(dbModules);
		
		return dbModules;
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

	public ConfigResultApplyStatusEnum apply(OrderRequest orderRequest) {
		ConfigResultApplyStatusEnum processed = null;
		for (ConfigModule configModule : modules) {
			ConditionResultProcessor processor = ConditionResultProcessorFactory.get(configModule);
			processed = processor.process(orderRequest);
			if (ConfigResultApplyStatusEnum.SUCESS == processed) {
				break;
			}
		}
		if(processed != ConfigResultApplyStatusEnum.ERROR) {
			OrderItemPriceDistributor.updateTotalPrice(orderRequest.getOrderItems(), orderRequest.getTotalPrice());
		} else {
			return ConfigResultApplyStatusEnum.ERROR;
		}
		return isProcessSuccessful(orderRequest.getOrderItems());
	}
	
	private ConfigResultApplyStatusEnum isProcessSuccessful(List<OrderItem> orderItems) {
		ConfigResultApplyStatusEnum configResultApplyStatus = ConfigResultApplyStatusEnum.SUCESS;
		for (OrderItem orderItem : orderItems) {
			if(orderItem.getOrderItemPromotionStatus().getOrderItemPromotionApplication() != OrderItemPromotionApplicationEnum.SUCCESS) {
				configResultApplyStatus = ConfigResultApplyStatusEnum.PARTIAL;
				break;
			}
		}
		return configResultApplyStatus;
	}

}
