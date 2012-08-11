package com.fb.platform.promotion.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrTokenizer;

import com.fb.platform.promotion.rule.config.RuleConfigDescriptorEnum;

public class StringToIntegerList {

	public static List<Integer> convert(List<String> strList){
		List<Integer> intList = new ArrayList<Integer>();
		for(Object str : strList){
			if(StringUtils.isNotBlank((String)str)){
				intList.add(Integer.valueOf(((String)str).trim()));	
			}
		}
		return intList;
	}
	
	public static List<Integer> csiToIntegerList(String commaSeparatedIntegerStr) {
		StrTokenizer strTokCategories = new StrTokenizer(commaSeparatedIntegerStr,",");
		return StringToIntegerList.convert(strTokCategories.getTokenList());
	}
	
	public static boolean isListValid(String[] idList){
		List<Integer> intList = new ArrayList<Integer>();
		boolean isValid = true;
		try {
			for(String str : idList){
				str = str.trim();
				intList.add(Integer.valueOf(str.trim()));	
			}
		} catch (NumberFormatException e) {
			isValid = false;
		}
		
		return isValid;
	}
}
