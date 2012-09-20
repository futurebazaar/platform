package com.fb.platform.sap.bapi.commons;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class SapUtils {
	
	public static String convertDateToFormat(DateTime currentTime, String dateFormat) {
		int formatLength = dateFormat.length();
		try {
			DateTimeFormatter format = DateTimeFormat.forPattern(dateFormat);
			return format.print(currentTime);
		} catch (Exception e) {
			String format = String.format("%%0%dd", formatLength);
			String result = String.format(format, 0);
			return result;

		}
	}

}
