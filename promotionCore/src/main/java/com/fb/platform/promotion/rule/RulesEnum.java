/**
 * 
 */
package com.fb.platform.promotion.rule;

/**
 * This Enum identifies the Java Class associated with a Rule
 * @author vinayak
 *
 */
public enum RulesEnum {

	BUY_X_GET_Y_FREE;

	private String className;

	public String getImplementationClassName() {
		return className;
	}
}
