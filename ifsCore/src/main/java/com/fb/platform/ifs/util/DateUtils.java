package com.fb.platform.ifs.util;


import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class DateUtils {
 
	public static Timestamp parseDate(String date){
		try{
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			java.util.Date parsedDate = dateFormat.parse(date);
			java.sql.Timestamp timestamp = new java.sql.Timestamp(parsedDate.getTime());
			return timestamp;
		}catch(Exception e){
			return null;
		}
	}
	
	public static Timestamp parseDate(String date, String format){
		try{
			SimpleDateFormat dateFormat = new SimpleDateFormat(format);
			java.util.Date parsedDate = dateFormat.parse(date);
			java.sql.Timestamp timestamp = new java.sql.Timestamp(parsedDate.getTime());
			return timestamp;
		}catch(Exception e){
			return null;
		}
	}
}
