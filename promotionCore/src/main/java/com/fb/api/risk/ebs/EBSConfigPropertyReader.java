package com.fb.api.risk.ebs;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * EBSConfigPropertyReader.java - [This static utility class loads and * 
 * reads the values from the EBS application properties file] 
 * 
 * @author Tuhin
 * @version 1.0
 *
 */
public class EBSConfigPropertyReader {
	
	private static final String BUNDLE_NAME = "com.fb.resource.properties.ebs.ebsConfig";

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

	public static String getValue(String key) {

		try {

			return RESOURCE_BUNDLE.getString(key);

		} catch (MissingResourceException mex) {

			return "";

		} catch (Exception ex) {

			return "NOT FOUND";

		}

	}


}
