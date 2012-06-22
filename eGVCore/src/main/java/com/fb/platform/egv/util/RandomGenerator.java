/**
 * 
 */
package com.fb.platform.egv.util;

import java.util.Random;

/**
 * @author keith
 *
 */
public class RandomGenerator {
	
	public static String integerRandomGenerator(int length){

		char[] intSet = {'1','2','3','4','5','6','7','8','9','0'};
		char[] intSetFirstChar = {'1','2','3','4','5','6','7','8','9'};
		
		Random random = new Random();
		StringBuilder sb = new StringBuilder("");
		
		//First character : non-zero
		sb.append(intSetFirstChar[random.nextInt(intSetFirstChar.length)]);
		for (int i = 1; i < length; i++) {
		    char c = intSet[random.nextInt(intSet.length)];
		    sb.append(c);
		}
		
		return sb.toString();
	}
	
	//Code to Test it individually
	public static void main(String[] args) {
		System.out.println(RandomGenerator.integerRandomGenerator(11));
	}

}
