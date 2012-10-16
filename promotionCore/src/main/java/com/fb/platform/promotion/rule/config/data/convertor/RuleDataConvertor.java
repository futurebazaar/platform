/**
 * 
 */
package com.fb.platform.promotion.rule.config.data.convertor;

import com.fb.platform.promotion.rule.config.RuleConfiguration;
import com.fb.platform.promotion.rule.config.data.RuleData;
import com.fb.platform.promotion.rule.metadata.RuleConfigMetadata;

/**
 * @author keith
 *
 */
public interface RuleDataConvertor {

	public RuleData convert(RuleConfiguration ruleConfig, RuleConfigMetadata metadata);
	
}
