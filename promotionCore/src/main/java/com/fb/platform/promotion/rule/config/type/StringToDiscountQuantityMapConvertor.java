/**
 * 
 */
package com.fb.platform.promotion.rule.config.type;

import java.math.BigDecimal;
import java.util.HashMap;

import org.apache.commons.lang.text.StrTokenizer;

import com.fb.commons.PlatformException;

/**
 * @author keith
 *
 */
public class StringToDiscountQuantityMapConvertor implements Convertor {

	/* (non-Javadoc)
	 * @see com.fb.platform.promotion.rule.config.type.Convertor#convert(java.lang.String)
	 */
	@Override
	public Object convert(String toConvert) {
		QuantityDiscountMap map = new QuantityDiscountMap();
		HashMap<Integer, BigDecimal> quantityDiscountMap = new HashMap<Integer, BigDecimal>();
		StrTokenizer strTokPercentMap = new StrTokenizer(toConvert,",");
		int quantity = 0;
		int maxSupportedQuantity = 0;
		int minSupportedQuantity = 9999999; // Some Random Large number
		BigDecimal percent = null;
		try {
			while(strTokPercentMap.hasNext()) {
				String percentEntry = strTokPercentMap.nextToken();
				StrTokenizer strTokPercentEntry = new StrTokenizer(percentEntry,"=");

				// Quantity=Percent : 1=5,2=10,3=30
				quantity = Integer.parseInt(strTokPercentEntry.nextToken().trim());
				if(maxSupportedQuantity < quantity) {
					maxSupportedQuantity = quantity;
				}
				percent = new BigDecimal(strTokPercentEntry.nextToken().trim());
				if(minSupportedQuantity > quantity && (percent.compareTo(BigDecimal.ZERO) >= 0)) {
					minSupportedQuantity = quantity;
				}
				quantityDiscountMap.put(quantity,percent);
			}
		} catch(Exception e) {
			//Assumption of syntax being correct. If any error throw Platform Exception
			throw new PlatformException("Invalid Format in Quantity Percent Map : Input = " + toConvert + "\n Error : " + e);
		}
		map.setMaxSupportedQuantity(maxSupportedQuantity);
		map.setMinSupportedQuantity(minSupportedQuantity);
		map.setQuantityDiscountMap(quantityDiscountMap);
		return map;
	}

}
