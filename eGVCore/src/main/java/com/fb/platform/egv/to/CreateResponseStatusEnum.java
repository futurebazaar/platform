/**
 * 
 */
package com.fb.platform.egv.to;


/**
 * @author keith
 *
 */
public enum CreateResponseStatusEnum implements GiftVoucherResponseEnum {
	
	NO_SESSION,
	INTERNAL_ERROR,
	SMS_SEND_FAILURE,
	UNABLE_TO_CREATE_ERROR,
	EMAIL_SEND_FAILURE,
	USER_NOT_AUTHORIZED,
	SUCCESS;

}
