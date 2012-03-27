/**
 * 
 */
package com.fb.platform.promotion.to;

/**
 * @author keith
 *
 */
public enum PromotionResponseStatusCode {
		
	VALID_SUCCESS("VALID_SUCCESS"),
	INVALID_COUPON_CODE("INVALID_COUPON_CODE"),
	COUPON_CODE_EXPIRED("COUPON_CODE_EXPIRED"),
	NOT_AUTHORIZED_USER("NOT_AUTHORIZED_USER"),
	NUMBER_OF_USES_EXCEEDED("NUMBER_OF_USES_EXCEEDED"),
	NOT_APPLICABLE_ON_PRODUCT("NOT_APPLICABLE_ON_PRODUCT"),
	NOT_APPLICABLE_ON_CATEGORY("NOT_APPLICABLE_ON_CATEGORY"),
	LESS_ORDER_AMOUNT("LESS_ORDER_AMOUNT");

		private String status = null;

		private PromotionResponseStatusCode(String status) {
			this.status = status;
		}

		@Override
		public String toString() {
			return this.status;
		}

	
}
