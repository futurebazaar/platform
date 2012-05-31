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
import com.fb.platform.egv.to.CreateGiftVoucherRequest;
import com.fb.platform.egv.to.CreateGiftVoucherResponse;
import com.fb.platform.egv.to.CreateGiftVoucherResponseStatusEnum;
import com.fb.platform.egv.to.GetGiftVoucherInfoRequest;
import com.fb.platform.egv.to.GetGiftVoucherInfoResponse;
import com.fb.platform.egv.to.GetGiftVoucherInfoResponseStatusEnum;

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
	public CreateGiftVoucherResponse createGiftVoucher(CreateGiftVoucherRequest request) {
		if(logger.isDebugEnabled()) {
			logger.debug("Creating Gift Voucher worth : " + request.getAmount());
		}
		CreateGiftVoucherResponse response = new CreateGiftVoucherResponse();

		if (request == null || StringUtils.isBlank(request.getSessionToken())) {
			response.setStatus(CreateGiftVoucherResponseStatusEnum.NO_SESSION);
			return response;
		}

		//authenticate the session token and find out the userId
		AuthenticationTO authentication = authenticationService.authenticate(request.getSessionToken());
		if (authentication == null) {
			//invalid session token
			response.setStatus(CreateGiftVoucherResponseStatusEnum.NO_SESSION);
			return response;
		}
		
		response.setSessionToken(request.getSessionToken());
		
		int userId = authentication.getUserID();

		try {
			//create the gift voucher
			giftVoucherService.createGiftVoucher(request.getEmail(), userId, request.getAmount(), request.getOrderItemId());
			response.setStatus(CreateGiftVoucherResponseStatusEnum.SUCCESS);

		} catch (GiftVoucherNotFoundException e) {
			response.setStatus(CreateGiftVoucherResponseStatusEnum.INTERNAL_ERROR);
		} catch (PlatformException e) {
			response.setStatus(CreateGiftVoucherResponseStatusEnum.INTERNAL_ERROR);
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
	public GetGiftVoucherInfoResponse getGiftVoucherInfo(
			GetGiftVoucherInfoRequest request) {
		
		GetGiftVoucherInfoResponse response = new GetGiftVoucherInfoResponse();

		if (request == null || StringUtils.isBlank(request.getSessionToken())) {
			response.setResponseStatus(GetGiftVoucherInfoResponseStatusEnum.NO_SESSION);
			return response;
		}

		//authenticate the session token and find out the userId
		AuthenticationTO authentication = authenticationService.authenticate(request.getSessionToken());
		if (authentication == null) {
			//invalid session token
			response.setResponseStatus(GetGiftVoucherInfoResponseStatusEnum.NO_SESSION);
			return response;
		}
		
		response.setSessionToken(request.getSessionToken());
		
		int userId = authentication.getUserID();

		try {
			//Find the gift voucher
			GiftVoucher eGV = giftVoucherService.getGiftVoucher(request.getGiftVoucherNumber(),request.getGiftVoucherPin());
			response.setAmount(eGV.getAmount().getAmount());
			response.setEmail(eGV.getEmail());
			response.setNumber(eGV.getNumber());
			response.setResponseStatus(GetGiftVoucherInfoResponseStatusEnum.SUCCESS);
			
		} catch (GiftVoucherNotFoundException e) {
			response.setResponseStatus(GetGiftVoucherInfoResponseStatusEnum.GIFT_VOUCHER_NOT_FOUND);
		} catch (PlatformException e) {
			response.setResponseStatus(GetGiftVoucherInfoResponseStatusEnum.INTERNAL_ERROR);
		}

		return response;

	}

}
