/**
 * 
 */
package com.fb.platform.egv.to;

import java.io.Serializable;

/**
 * @author keith
 *
 */
public enum ApplyResponseStatusEnum implements Serializable{
	
	NO_SESSION,
	INTERNAL_ERROR,
	SUCCESS,
	INVALID_GIFT_VOUCHER_NUMBER,
	INVALID_GIFT_VOUCHER_PIN,
	GIFT_VOUCHER_EXPIRED,
	ALREADY_USED;

}
