/**
 * 
 */
package com.fb.platform.promotion.rule.config.type;

import java.math.BigDecimal;

/**
 * @author keith
 *
 */
public class StringToDecimalConvertor implements Convertor {

	/* (non-Javadoc)
	 * @see com.fb.platform.promotion.rule.config.type.Convertor#convert(java.lang.String)
	 */
	@Override
	public Object convert(String toConvert) {
		return BigDecimal.valueOf(Double.valueOf(toConvert));
	}

}
