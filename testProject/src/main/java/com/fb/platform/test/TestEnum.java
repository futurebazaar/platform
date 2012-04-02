/**
 * 
 */
package com.fb.platform.test;

import java.math.BigDecimal;

import com.fb.platform.auth._1_0.GetUserStatus;

/**
 * @author vinayak
 *
 */
public class TestEnum {

	public static void main(String[] args) {
		GetUserStatus status = GetUserStatus.NO_SESSION;
		Class<?> clazz = status.getClass();
		if (clazz.isAssignableFrom(Enum.class)) {
			System.out.println("clazz.isAssignableFrom(Enum.class)");
		} else {
			System.out.println("NOOOOOOO clazz.isAssignableFrom(Enum.class)");
		}

		if (Enum.class.isAssignableFrom(clazz)) {
			System.out.println("Enum.class.isAssignableFrom(clazz)");
		} else {
			System.out.println("NO NO NO Enum.class.isAssignableFrom(clazz)");
		}
		
		BigDecimal bd = new BigDecimal("10.00");
		Class<?> bdClass = bd.getClass();
		if (bdClass.isAssignableFrom(Number.class)) {
			System.out.println("bdClass.isAssignableFrom(Number.class)");
		} else {
			System.out.println("NOOOOOO bdClass.isAssignableFrom(Number.class)");
		}
	}
}
