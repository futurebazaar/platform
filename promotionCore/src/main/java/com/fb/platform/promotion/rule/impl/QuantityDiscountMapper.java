/**
 * 
 */
package com.fb.platform.promotion.rule.impl;

import java.math.BigDecimal;
import java.util.HashMap;

import org.apache.commons.lang.text.StrTokenizer;

import com.fb.commons.PlatformException;

/**
 * @author keith
 *
 */
public class QuantityDiscountMapper {

	private int maxSupportedQuantity = 0;
	HashMap<Integer, BigDecimal> quantityDiscountMap = null;	//map of quantity vs discount

	public QuantityDiscountMapper(String percentQuantityKeyValueMapString) throws PlatformException{
		quantityDiscountMap = new HashMap<Integer, BigDecimal>();
		StrTokenizer strTokPercentMap = new StrTokenizer(percentQuantityKeyValueMapString,",");
		int quantity = 0;
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
				quantityDiscountMap.put(quantity,percent);
			}
		
		} catch(Exception e) {
			//Assumption of syntax being correct. If any error throw Platform Exception
			throw new PlatformException("Invalid Format in Quantity Percent Map : Input = " + percentQuantityKeyValueMapString + "\n Error : " + e);
		}
	}

	public BigDecimal getDiscount(int quantity) {
		if (quantity > maxSupportedQuantity) {
			quantity = maxSupportedQuantity;
		}
		return (quantityDiscountMap.get(quantity) == null)? BigDecimal.ZERO:(quantityDiscountMap.get(quantity));
	}
	
}
