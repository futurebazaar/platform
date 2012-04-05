package com.fb.platform.ifs.util;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class FbLogger {
	
	
	
	public static Logger getLogInfo() {
		PropertyConfigurator.configure("src/main/resources/info.properties");
		final Logger info = Logger.getLogger("infoLog");		
		return info;
	}
	
	public static Logger getLogDebug() {
		PropertyConfigurator.configure("src/main/resources/debug.properties");
		final Logger debug = Logger.getLogger("debuglog");
		return debug;
	}
	
	public static Logger getLogError() {
		PropertyConfigurator.configure("src/main/resources/error.properties");
		final Logger error = Logger.getLogger("errorlog");
		return error;
	}

	public static Logger getLogClass(Class<?> className) {
		final Logger logger = Logger.getLogger(className);
		return logger;
	}
	
	
}
