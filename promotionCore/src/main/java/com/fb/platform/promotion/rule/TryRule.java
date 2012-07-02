/**
 * 
 */
package com.fb.platform.promotion.rule;


/**
 * @author keith
 *
 */
public class TryRule {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			System.out.println(RulesEnum.BUY_WORTH_X_GET_Y_RS_OFF.getDescription());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}

}
