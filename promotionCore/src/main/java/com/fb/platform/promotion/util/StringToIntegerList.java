package com.fb.platform.promotion.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public class StringToIntegerList {

	public static List<Integer> convert(List<String> strList){
		List<Integer> intList = new ArrayList<Integer>();
		for(String str : strList){
			if(StringUtils.isNotBlank(str)){
				intList.add(Integer.valueOf(str.trim()));	
			}
		}
		return intList;
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
