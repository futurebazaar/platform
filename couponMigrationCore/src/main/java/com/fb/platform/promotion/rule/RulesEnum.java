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

	BUY_X_GET_Y_FREE (-1),
	BUY_WORTH_X_GET_Y_RS_OFF (-2),
	BUY_WORTH_X_GET_Y_PERCENT_OFF (-3),
	BUY_WORTH_X_GET_Y_RS_OFF_ON_Z_CATEGORY (-4);
	
	private final int id;
	
	private RulesEnum(int id) {
		this.id = id;
	}
	
	public int getId() {
		return this.id;
	}
}
