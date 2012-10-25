package com.fb.platform.sap.client.commons;

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
	
	public static DateTime getDateTimeFromString(String dateTime, String dateFormat) {
		try {
			DateTimeFormatter format = DateTimeFormat.forPattern(dateFormat);
			return format.parseDateTime(dateTime);
		} catch (Exception e) {
			return null;
		}
	}
	
	public static boolean isBigBazaar(TinlaClient client) {
		if (client.equals(TinlaClient.BIGBAZAAR) || client.equals(TinlaClient.SELLER)) {
			return true;
		}
		return false;
	}

}
