/**
 * 
 */
package com.fb.platform.promotion.to;

import java.io.Serializable;

/**
 * @author ashish
 *
 */
public enum AlphaNumericType implements Serializable {
	
	ALPHABETS("ALPHABETS"),
	ALPHA_NUMERIC("ALPHA_NUMERIC"),
	NUMBERS("NUMBERS");

	private String message = null;

	private AlphaNumericType(String status) {
		this.message = status;
	}

	@Override
	public String toString() {
		return this.message;
	}
}
