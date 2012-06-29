/**
 * 
 */
package com.fb.platform.egv.to;


/**
 * @author keith
 *
 */
public enum GetInfoResponseStatusEnum implements GiftVoucherResponseEnum {

	NO_SESSION,
	INTERNAL_ERROR,
	SUCCESS,
	INVALID_GIFT_VOUCHER_NUMBER,
	INVALID_GIFT_VOUCHER_PIN;
	
}
