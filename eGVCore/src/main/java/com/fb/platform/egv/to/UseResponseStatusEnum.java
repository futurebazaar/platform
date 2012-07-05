/**
 * 
 */
package com.fb.platform.egv.to;


/**
 * @author keith
 *
 */
public enum UseResponseStatusEnum implements GiftVoucherResponseEnum {
	
	NO_SESSION,
	INTERNAL_ERROR,
	SUCCESS,
	INVALID_GIFT_VOUCHER_NUMBER,
	INVALID_GIFT_VOUCHER_PIN,
	GIFT_VOUCHER_EXPIRED,
	ALREADY_USED;

}
