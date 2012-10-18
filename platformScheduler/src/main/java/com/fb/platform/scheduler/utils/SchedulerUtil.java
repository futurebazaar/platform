package com.fb.platform.scheduler.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class SchedulerUtil {

	public static Properties getProperties(String fileName){
		InputStream inStream = SchedulerUtil.class.getClassLoader().getResourceAsStream(fileName);
		Properties props = new Properties();
		try {
			props.load(inStream);
			inStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return props;
	}
}
