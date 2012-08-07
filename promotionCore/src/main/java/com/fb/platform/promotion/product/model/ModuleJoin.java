/**
 * 
 */
package com.fb.platform.promotion.product.model;

/**
 * @author vinayak
 *
 */
public enum ModuleJoin {
	AND,
	OR;
	
	@Override
	public String toString() {
		return this.name();
	}
}
