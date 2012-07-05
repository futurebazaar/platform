/**
 * 
 */
package com.fb.platform.promotion.rule.config.type;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fb.platform.promotion.rule.impl.MonthlyDiscountRsOffRuleImpl;

/**
 * @author SalimM
 *
 */
public class StringToIntegerConvertor implements Convertor {

	private static transient Log log = LogFactory.getLog(MonthlyDiscountRsOffRuleImpl.class);

	/* (non-Javadoc)
	 * @see com.fb.platform.promotion.rule.config.type.Convertor#convert(java.lang.String)
	 */
	@Override
	public Object convert(String toConvert) {
		int retObj = 0;
		try{
			retObj = Integer.parseInt(toConvert); 
		}catch(NullPointerException npe)
		{
			log.debug("Checking value of toConvert in MonthlyDiscountOffRuleImpl" );
		}
		return retObj;
		
	}
	

}
