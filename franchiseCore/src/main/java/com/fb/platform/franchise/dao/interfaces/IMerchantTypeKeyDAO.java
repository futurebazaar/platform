/**
 * 
 */
package com.fb.platform.franchise.dao.interfaces;

import com.fb.platform.franchise.domain.MerchantTypeKeyBO;

/**
 * @author ashish
 *
 */
public interface IMerchantTypeKeyDAO {

	MerchantTypeKeyBO getMerchantTypeKeyByID(String id);
	MerchantTypeKeyBO getMerchantTypeKeyByKey(String key);
}
