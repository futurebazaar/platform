package com.fb.platform.test.util;

import org.joda.time.DateTime;

import java.util.Calendar;
import java.util.GregorianCalendar;
import javax.xml.bind.DatatypeConverter;

public class DateAdapter {

    public static DateTime parse(String dateTime) {
        return new DateTime(DatatypeConverter.parseDateTime(dateTime));
    }

    public static String print(DateTime dateTime) {
	    Calendar cal = new GregorianCalendar();
	    cal.setTimeInMillis(dateTime.getMillis());
	    return DatatypeConverter.printDateTime(cal);
    }
}
