/**
 * 
 */
package com.fb.platform.egv.service.impl;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.fb.commons.PlatformException;
import com.fb.platform.auth.AuthenticationService;
import com.fb.platform.auth.AuthenticationTO;
import com.fb.platform.egv.model.GiftVoucher;
import com.fb.platform.egv.service.GiftVoucherManager;
import com.fb.platform.egv.service.GiftVoucherNotFoundException;
import com.fb.platform.egv.service.GiftVoucherService;
import com.fb.platform.egv.to.CreateRequest;
import com.fb.platform.egv.to.CreateResponse;
import com.fb.platform.egv.to.CreateResponseStatusEnum;
import com.fb.platform.egv.to.GetInfoRequest;
import com.fb.platform.egv.to.GetInfoResponse;
import com.fb.platform.egv.to.GetInfoResponseStatusEnum;

/**
 * @author keith
 *
 */
public class GiftVoucherManagerImpl implements GiftVoucherManager {

	private static Log logger = LogFactory.getLog(GiftVoucherManagerImpl.class);

	@Autowired
	private AuthenticationService authenticationService;

	@Autowired
	private GiftVoucherService giftVoucherService = null;

	@Override
	public CreateResponse create(CreateRequest request) {
		if(logger.isDebugEnabled()) {
			logger.debug("Creating Gift Voucher worth : " + request.getAmount());
		}
		CreateResponse response = new CreateResponse();

		if (request == null || StringUtils.isBlank(request.getSessionToken())) {
			response.setStatus(CreateResponseStatusEnum.NO_SESSION);
			return response;
		}

		//authenticate the session token and find out the userId
		AuthenticationTO authentication = authenticationService.authenticate(request.getSessionToken());
		if (authentication == null) {
			//invalid session token
			response.setStatus(CreateResponseStatusEnum.NO_SESSION);
			return response;
		}
		
		response.setSessionToken(request.getSessionToken());
		
		int userId = authentication.getUserID();

		try {
			//create the gift voucher
			giftVoucherService.createGiftVoucher(request.getEmail(), userId, request.getAmount(), request.getOrderItemId());
			response.setStatus(CreateResponseStatusEnum.SUCCESS);

		} catch (GiftVoucherNotFoundException e) {
			response.setStatus(CreateResponseStatusEnum.INTERNAL_ERROR);
		} catch (PlatformException e) {
			response.setStatus(CreateResponseStatusEnum.INTERNAL_ERROR);
		}

		return response;
	}

	public void setAuthenticationService(AuthenticationService authenticationService) {
		this.authenticationService = authenticationService;
	}

	public void setGiftVoucherService(GiftVoucherService giftVoucherService) {
		this.giftVoucherService = giftVoucherService;
	}

	@Override
	public GetInfoResponse getInfo(
			GetInfoRequest request) {
		
		GetInfoResponse response = new GetInfoResponse();

		if (request == null || StringUtils.isBlank(request.getSessionToken())) {
			response.setResponseStatus(GetInfoResponseStatusEnum.NO_SESSION);
			return response;
		}

		//authenticate the session token and find out the userId
		AuthenticationTO authentication = authenticationService.authenticate(request.getSessionToken());
		if (authentication == null) {
			//invalid session token
			response.setResponseStatus(GetInfoResponseStatusEnum.NO_SESSION);
			return response;
		}
		
		response.setSessionToken(request.getSessionToken());
		
		int userId = authentication.getUserID();

		try {
			//Find the gift voucher
			GiftVoucher eGV = giftVoucherService.getGiftVoucher(request.getGiftVoucherNumber(),request.getGiftVoucherPin());
			response.setAmount(eGV.getAmount().getAmount());
			response.setEmail(eGV.getEmail());
			response.setNumber(Long.parseLong(eGV.getNumber()));
			response.setResponseStatus(GetInfoResponseStatusEnum.SUCCESS);
			
		} catch (GiftVoucherNotFoundException e) {
			response.setResponseStatus(GetInfoResponseStatusEnum.GIFT_VOUCHER_NOT_FOUND);
		} catch (PlatformException e) {
			response.setResponseStatus(GetInfoResponseStatusEnum.INTERNAL_ERROR);
		}

		return response;

	}

}
