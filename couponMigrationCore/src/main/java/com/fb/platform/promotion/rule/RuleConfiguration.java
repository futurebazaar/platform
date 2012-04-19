/**
 * 
 */
package com.fb.platform.promotion.rule;

import java.util.ArrayList;
import java.util.List;

import com.fb.commons.PlatformException;


/**
 * @author vinayak
 *
 */
public class RuleConfiguration {

	private int promotionId = 0;
	private int ruleId = 0;
	private List<RuleConfigItem> configItems = null;

	public RuleConfiguration () {
		configItems = new ArrayList<RuleConfigItem>();
	}

	public void add(RuleConfigItem configItem) {
		this.configItems.add(configItem);
	}

	public RuleConfiguration(List<RuleConfigItem> configItems) {
		this.configItems = configItems;
	}

	public List<RuleConfigItem> getConfigItems() {
		return configItems;
	}

	public void setConfigItems(List<RuleConfigItem> configItems) {
		this.configItems = configItems;
	}
	
	public RuleConfigItem getConfigItem(int index){
		return configItems.get(index);
	}
	
	public RuleConfigItem getConfigItem(String key){
		for(RuleConfigItem rci : configItems){
			if(rci.getKey().equalsIgnoreCase(key)){
				return rci;
			}
		}
		return null;
	}
	
	public String getConfigItemValue(int index){
		return configItems.get(index).getValue();
	}
	
	public String getConfigItemValue(String key){
		for(RuleConfigItem rci : configItems){
			if(rci.getKey().equalsIgnoreCase(key)){
				return rci.getValue();
			}
		}
		throw new PlatformException("Config Item Value not found for the key - "+key);
	}

	public int getPromotionId() {
		return promotionId;
	}

	public void setPromotionId(int promotionId) {
		this.promotionId = promotionId;
	}

	public int getRuleId() {
		return ruleId;
	}

	public void setRuleId(int ruleId) {
		this.ruleId = ruleId;
	}
}
