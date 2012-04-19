package com.fb.platform.promotion.util;

import java.util.ArrayList;
import java.util.List;

public class StringToIntegerList {

	public static List<Integer> convert(List<String> strList){
		List<Integer> intList = new ArrayList<Integer>();
		for(String str : strList){
			intList.add(Integer.valueOf(str));
		}
		return intList;
	}
}
