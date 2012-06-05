/**
 * 
 */
package com.fb.platform.egv.service;

import com.fb.platform.egv.to.CreateRequest;
import com.fb.platform.egv.to.CreateResponse;
import com.fb.platform.egv.to.GetInfoRequest;
import com.fb.platform.egv.to.GetInfoResponse;

/**
 * @author keith
 *
 */
public interface GiftVoucherManager {

	public GetInfoResponse getInfo(GetInfoRequest request);
	
	public CreateResponse create(CreateRequest request);
	
}
