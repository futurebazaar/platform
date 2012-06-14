/**
 * 
 */
package com.fb.platform.egv.to;

import java.io.Serializable;

/**
 * @author keith
 *
 */
public enum GetInfoResponseStatusEnum implements Serializable{

	NO_SESSION,
	INTERNAL_ERROR,
	SUCCESS,
	GIFT_VOUCHER_NOT_FOUND,
	INVALID_PIN;
	
}
