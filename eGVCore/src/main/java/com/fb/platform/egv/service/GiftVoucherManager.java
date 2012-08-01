/**
 * 
 */
package com.fb.platform.egv.service;

import com.fb.platform.egv.to.ApplyRequest;
import com.fb.platform.egv.to.ApplyResponse;
import com.fb.platform.egv.to.CancelRequest;
import com.fb.platform.egv.to.CancelResponse;
import com.fb.platform.egv.to.CreateRequest;
import com.fb.platform.egv.to.CreateResponse;
import com.fb.platform.egv.to.GetInfoRequest;
import com.fb.platform.egv.to.GetInfoResponse;
import com.fb.platform.egv.to.RollbackUseRequest;
import com.fb.platform.egv.to.RollbackUseResponse;
import com.fb.platform.egv.to.UseRequest;
import com.fb.platform.egv.to.UseResponse;

/**
 * @author keith
 *
 */
public interface GiftVoucherManager {

	public GetInfoResponse getInfo(GetInfoRequest request);
	
	public CreateResponse create(CreateRequest request);
	
	public CancelResponse cancel(CancelRequest request);
	
	public UseResponse use(UseRequest request);
	
	public ApplyResponse apply(ApplyRequest request);
	
	public RollbackUseResponse rollbackUse(RollbackUseRequest request);
	
}
