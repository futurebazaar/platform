/**
 * 
 */
package com.fb.platform.promotion.rule.config.type;

import java.math.BigDecimal;

import com.fb.commons.to.Money;

/**
 * @author keith
 *
 */
public class StringToMoneyConvertor implements Convertor {

	/* (non-Javadoc)
	 * @see com.fb.platform.promotion.rule.config.type.Convertor#convert(java.lang.String)
	 */
	@Override
	public Object convert(String toConvert) {
		return new Money(BigDecimal.valueOf(Double.valueOf(toConvert)));
	}

}
