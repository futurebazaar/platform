/**
 * @author vinayak
 */
package com.fb.platform.promotion.rule.config;

import java.io.Serializable;

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
