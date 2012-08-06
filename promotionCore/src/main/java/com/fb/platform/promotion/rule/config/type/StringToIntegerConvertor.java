/**
 * 
 */
package com.fb.platform.promotion.rule.config.type;


/**
 * @author keith
 * 
 */
public class StringToIntegerConvertor implements Convertor {

	@Override
	public Object convert(String toConvert) {
		return Integer.valueOf(toConvert);
	}

}
