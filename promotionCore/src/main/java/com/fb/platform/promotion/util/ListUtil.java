/**
 * 
 */
package com.fb.platform.promotion.util;

import java.util.List;
import java.util.Set;

/**
 * @author ashish
 *
 */
public class ListUtil {

	public static boolean isValidList(List<?> list) {
		if (list == null) {
			return false;
		}
		if (list.size() == 0) {
			return false;
		}
		return true;
	}

	public static boolean isValidSet(Set<?> set) {
		if (set == null) {
			return false;
		}
		if (set.isEmpty()) {
			return false;
		}
		return true;
	}
}
