/**
 * 
 */
package com.fb.platform.egv.util;

import java.util.List;

/**
 * @author ashish
 *
 */
public class ListUtils {

	public static boolean isValidList(List<?> list) {
		if (list == null) {
			return false;
		}
		if (list.size() == 0) {
			return false;
		}
		return true;
	}
}
