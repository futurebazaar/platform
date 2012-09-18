/**
 * 
 */
package com.fb.commons.sms.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author keith
 *
 */
public class SMSPropertiesUtil {

	public static final String SMS_SETTING_FILE = "sms_settings.properties";
	
	public static Properties prop;
	
	static {
		try {
			prop = new Properties();
			InputStream propStream = SMSPropertiesUtil.class.getClassLoader().getResourceAsStream(SMS_SETTING_FILE);
			prop.load(propStream);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static String getProperty(String key) {
		return prop.getProperty(key);
	}
	
}
