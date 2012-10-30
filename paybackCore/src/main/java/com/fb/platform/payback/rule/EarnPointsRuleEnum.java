package com.fb.platform.payback.rule;

public enum EarnPointsRuleEnum {
	
	/*Please check the sequence
	 * 1) Give Normal Points
	 * 2) Check for Friday Offer
	 * 3) Check Product Offers
	 * 4) Check Bonus Points Offer
	 */
	
	ENTER_LOYALTY_CARD_EARN_X_POINTS,
	EARN_X_POINTS_ON_Y_DAY,
	BUY_PRODUCT_X_EARN_Y_POINTS,
	BUY_DOD_EARN_Y_POINTS,
	EARN_X_POINTS_ON_Y_PAYMENT_MODE,
	BUY_WORTH_X_EARN_Y_BONUS_POINTS,
	EARN_X_POINTS_ON_Y_CATEGORY_FOR_Z_PAYMENT_MODE,
	
	//WRITE_REVIEW_FOR_PRODUCT_X_EARN_Y_POINTS; 

}
