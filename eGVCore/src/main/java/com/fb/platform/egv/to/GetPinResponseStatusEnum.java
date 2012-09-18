/**
 * 
 */
package com.fb.platform.egv.to;

import java.io.Serializable;

/**
 * @author keith
 *
 */
public enum GetPinResponseStatusEnum implements Serializable{
	
	NO_SESSION,
	INTERNAL_ERROR,
	SUCCESS,
	INVALID_GIFT_VOUCHER_NUMBER,
	USER_NOT_AUTHORIZED,
	SMS_SEND_FAILURE,
	EMAIL_SEND_FAILURE;

}
