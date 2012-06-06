/**
 * 
 */
package com.fb.platform.payback.rule;

import java.io.Serializable;

/**
 * @author vinayak
 * 
 */
public class RuleConfigItem implements Serializable {

	private static final long serialVersionUID = 1L;

	private String key;
	private String value;

	// private String description;

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
