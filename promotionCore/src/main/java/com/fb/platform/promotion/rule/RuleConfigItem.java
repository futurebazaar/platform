/**
 * 
 */
package com.fb.platform.promotion.rule;

import java.io.Serializable;

/**
 * @author vinayak
 *
 */
public class RuleConfigItem implements Serializable {

	private String key;
	private String value;
	
	public RuleConfigItem(String key, String value) {
		this.key = key;
		this.value = value;
	}
	public String getKey() {
		return key;
	}
	public String getValue() {
		return value;
	}
}
