package com.fb.api.test;

import java.util.MissingResourceException;

import java.util.ResourceBundle;

/**
 * 
 * PropertyReader.java - [This static utility class loads and
 * 
 * reads the values from the application properties file]
 * 
 * 
 * 
 * @author TechCuBeTalk
 * 
 * @version 1.0
 */

public class PropertyReader {

	private static final String BUNDLE_NAME = "com.fb.resource.properties.config";

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle
			.getBundle(BUNDLE_NAME);

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