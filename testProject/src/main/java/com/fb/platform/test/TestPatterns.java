/**
 * 
 */
package com.fb.platform.test;

import java.util.regex.Pattern;

/**
 * @author vinayak
 *
 */
public class TestPatterns {

	private static final Pattern VALID_SESSION_CHARS = Pattern.compile("[0-9A-Fa-f/-]+");

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String sessionId = "41ed858c-74c9-43e4-b49f-a3faa7807f8d";
		String sessionId2 = "41ed858c74c943e4b49fa3faa7807f8d";
		boolean match2 = VALID_SESSION_CHARS.matcher(sessionId2).matches();
		boolean match = VALID_SESSION_CHARS.matcher(sessionId).matches();
		System.out.println(match2);
		System.out.println(match);
	}
}
