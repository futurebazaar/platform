/**
 * 
 */
package com.fb.platform.egv.to;


/**
 * @author keith
 *
 */
public enum RollbackUseResponseStatusEnum implements GiftVoucherResponseEnum {
	
	NO_SESSION,
	INTERNAL_ERROR,
	SUCCESS,
	INVALID_GIFT_VOUCHER_NUMBER,
	INVALID_GIFT_VOUCHER_PIN,
	GIFT_VOUCHER_EXPIRED,
	ROLLBACK_FAILED,
	ALREADY_USED;

}
