/**
 * 
 */
package com.fb.platform.promotion.rule.config.type;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrTokenizer;

import com.fb.platform.promotion.util.StringToIntegerList;

/**
 * @author keith
 *
 */
public class StringToIntegerListConvertor implements Convertor {

	@Override
	public Object convert(String toConvert) {
		StrTokenizer strTokCategories = new StrTokenizer(toConvert,",");
		return convertStringToIntegerList(strTokCategories.getTokenList());
	}
	
	private List<Integer> convertStringToIntegerList(List<String> strList) {
		List<Integer> intList = new ArrayList<Integer>();
		for(Object str : strList) {
			if(StringUtils.isNotBlank((String)str)) {
				intList.add(Integer.valueOf(((String)str).trim()));	
			}
		}
		return intList;
	}
	
}
