/**
 * 
 */
package com.fb.platform.egv.service;

import com.fb.platform.egv.to.CreateGiftVoucherRequest;
import com.fb.platform.egv.to.CreateGiftVoucherResponse;
import com.fb.platform.egv.to.GetGiftVoucherInfoRequest;
import com.fb.platform.egv.to.GetGiftVoucherInfoResponse;

/**
 * @author keith
 *
 */
public interface GiftVoucherManager {

	public GetGiftVoucherInfoResponse getGiftVoucherInfo(GetGiftVoucherInfoRequest request);
	
	public CreateGiftVoucherResponse createGiftVoucher(CreateGiftVoucherRequest request);
	
}
