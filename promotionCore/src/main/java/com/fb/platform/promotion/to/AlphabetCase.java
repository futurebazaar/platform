/**
 * 
 */
package com.fb.platform.promotion.to;

import java.io.Serializable;

/**
 * @author ashish
 *
 */
public enum AlphabetCase implements Serializable {
	
	UPPER("UPPER"),
	MIXED("MIXED"),
	INVARIANT("INVARIANT"),
	LOWER("LOWER");

	private String message = null;

	private AlphabetCase(String status) {
		this.message = status;
	}

	@Override
	public String toString() {
		return this.message;
	}
}
