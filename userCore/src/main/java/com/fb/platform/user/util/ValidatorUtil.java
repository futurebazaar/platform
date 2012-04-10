package com.fb.platform.user.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidatorUtil {
	
	public static boolean isValidEmailAddress(String email){
			 String expression = "^[\\w\\-]([\\.\\w])+[\\w]+@([\\w\\-]+\\.)+[A-Z]{2,8}$";
			 CharSequence inputStr = email;
			 Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
			 Matcher matcher = pattern.matcher(inputStr);
			 return matcher.matches();
		}
	
	
	public static boolean isValidPhoneNumber(String phoneNumber){
		String expression = "[0-9]{9,12}";  
		CharSequence inputStr = phoneNumber;  
		Pattern pattern = Pattern.compile(expression);  
		Matcher matcher = pattern.matcher(inputStr);  
		return matcher.matches();		
	}
}
