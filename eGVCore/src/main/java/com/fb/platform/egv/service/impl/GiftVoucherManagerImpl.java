/**
 * 
 */
package com.fb.platform.egv.service.impl;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;

import com.fb.commons.PlatformException;
import com.fb.commons.mail.exception.MailerException;
import com.fb.platform.auth.AuthenticationService;
import com.fb.platform.auth.AuthenticationTO;
import com.fb.platform.egv.model.GiftVoucher;
import com.fb.platform.egv.service.GiftVoucherAlreadyUsedException;
import com.fb.platform.egv.service.GiftVoucherExpiredException;
import com.fb.platform.egv.service.GiftVoucherManager;
import com.fb.platform.egv.service.GiftVoucherNotFoundException;
import com.fb.platform.egv.service.GiftVoucherService;
import com.fb.platform.egv.service.InvalidPinException;
import com.fb.platform.egv.to.ApplyRequest;
import com.fb.platform.egv.to.ApplyResponse;
import com.fb.platform.egv.to.ApplyResponseStatusEnum;
import com.fb.platform.egv.to.CancelRequest;
import com.fb.platform.egv.to.CancelResponse;
import com.fb.platform.egv.to.CancelResponseStatusEnum;
import com.fb.platform.egv.to.CreateRequest;
import com.fb.platform.egv.to.CreateResponse;
import com.fb.platform.egv.to.CreateResponseStatusEnum;
import com.fb.platform.egv.to.GetInfoRequest;
import com.fb.platform.egv.to.GetInfoResponse;
import com.fb.platform.egv.to.GetInfoResponseStatusEnum;
import com.fb.platform.egv.to.UseRequest;
import com.fb.platform.egv.to.UseResponse;
import com.fb.platform.egv.to.UseResponseStatusEnum;

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
			response.setResponseStatus(CreateResponseStatusEnum.NO_SESSION);
			return response;
		}

		//authenticate the session token and find out the userId
		AuthenticationTO authentication = authenticationService.authenticate(request.getSessionToken());
		if (authentication == null) {
			//invalid session token
			response.setResponseStatus(CreateResponseStatusEnum.NO_SESSION);
			return response;
		}
		
		response.setSessionToken(request.getSessionToken());
		
		int userId = authentication.getUserID();
		
		GiftVoucher gv = new GiftVoucher();
		try {
			//create the gift voucher
			gv = giftVoucherService.createGiftVoucher(request.getEmail(), userId, request.getAmount(), request.getOrderItemId());
			response.setGvNumber(Long.parseLong(gv.getNumber()));
			response.setValidFrom(gv.getValidFrom());
			response.setValidTill(gv.getValidTill());
			response.setResponseStatus(CreateResponseStatusEnum.SUCCESS);

		} catch (MailException e) {
			logger.error("Problem in sending mail to " + gv.getEmail(),e);
			response.setResponseStatus(CreateResponseStatusEnum.SENDING_MAIL_ERROR);
			throw new MailerException("Error sending mail", e);
		} catch (PlatformException e) {
			logger.error("Problem while creating new Gift Voucher of Amount : " + request.getAmount() + " for email " + request.getEmail());
			response.setResponseStatus(CreateResponseStatusEnum.INTERNAL_ERROR);
		}

		return response;
	}
	
	@Override
	public CancelResponse cancel(CancelRequest request) {
		if(logger.isDebugEnabled()) {
			logger.debug("Cancelling Gift Voucher : " + request.getGiftVoucherNumber());
		}
		CancelResponse response = new CancelResponse();

		if (request == null || StringUtils.isBlank(request.getSessionToken())) {
			response.setResponseStatus(CancelResponseStatusEnum.NO_SESSION);
			return response;
		}

		//authenticate the session token and find out the userId
		AuthenticationTO authentication = authenticationService.authenticate(request.getSessionToken());
		if (authentication == null) {
			//invalid session token
			response.setResponseStatus(CancelResponseStatusEnum.NO_SESSION);
			return response;
		}
		
		response.setSessionToken(request.getSessionToken());
		
		int userId = authentication.getUserID();

		try {
			//cancel the gift voucher
			giftVoucherService.cancelGiftVoucher(request.getGiftVoucherNumber(),userId,request.getOrderItemId());
			response.setResponseStatus(CancelResponseStatusEnum.SUCCESS);

		} catch (GiftVoucherNotFoundException e) {
			response.setResponseStatus(CancelResponseStatusEnum.INTERNAL_ERROR);
		} catch (PlatformException e) {
			response.setResponseStatus(CancelResponseStatusEnum.INTERNAL_ERROR);
		}

		return response;
	}

	@Override
	public UseResponse use(UseRequest request) {
		if(logger.isDebugEnabled()) {
			logger.debug("Using Gift Voucher worth : " + request.getAmount() + " as payment in order : " + request.getOrderId() + " worth : " +        request.getAmount());
		}
		UseResponse response = new UseResponse();

		if (request == null || StringUtils.isBlank(request.getSessionToken())) {
			response.setResponseStatus(UseResponseStatusEnum.NO_SESSION);
			return response;
		}

		//authenticate the session token and find out the userId
		AuthenticationTO authentication = authenticationService.authenticate(request.getSessionToken());
		if (authentication == null) {
			//invalid session token
			response.setResponseStatus(UseResponseStatusEnum.NO_SESSION);
			return response;
		}
		
		response.setSessionToken(request.getSessionToken());
		
		int userId = authentication.getUserID();

		try {
			//use the gift voucher
			giftVoucherService.useGiftVoucher(userId,request.getAmount(), request.getOrderId(),request.getGiftVoucherNumber(),request.getGiftVoucherPin());
			response.setResponseStatus(UseResponseStatusEnum.SUCCESS);

		} catch (GiftVoucherNotFoundException e) {
			response.setResponseStatus(UseResponseStatusEnum.INTERNAL_ERROR);
		} catch (InvalidPinException e) {
			response.setResponseStatus(UseResponseStatusEnum.INVALID_GIFT_VOUCHER_PIN);
		} catch (GiftVoucherExpiredException e) {
			response.setResponseStatus(UseResponseStatusEnum.GIFT_VOUCHER_EXPIRED);
		} catch (GiftVoucherAlreadyUsedException e) {
			response.setResponseStatus(UseResponseStatusEnum.ALREADY_USED);
		} catch (PlatformException e) {
			response.setResponseStatus(UseResponseStatusEnum.INTERNAL_ERROR);
		}

		return response;
	}
	
	@Override
	public GetInfoResponse getInfo(GetInfoRequest request) {
		if(logger.isDebugEnabled()) {
			logger.debug("Getting Gift Voucher  : " + request.getGiftVoucherNumber());
		}
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

		try {
			//Find the gift voucher
			GiftVoucher eGV = giftVoucherService.getGiftVoucher(request.getGiftVoucherNumber());
			response.setAmount(eGV.getAmount().getAmount());
			response.setEmail(eGV.getEmail());
			response.setNumber(Long.parseLong(eGV.getNumber()));
			response.setResponseStatus(GetInfoResponseStatusEnum.SUCCESS);
			
		} catch (GiftVoucherNotFoundException e) {
			response.setResponseStatus(GetInfoResponseStatusEnum.INVALID_GIFT_VOUCHER_NUMBER);
		} catch (PlatformException e) {
			response.setResponseStatus(GetInfoResponseStatusEnum.INTERNAL_ERROR);
		}

		return response;

	}

	@Override
	public ApplyResponse apply(ApplyRequest request) {
		if(logger.isDebugEnabled()) {
			logger.debug("Getting Gift Voucher  : " + request.getGiftVoucherNumber());
		}
		ApplyResponse response = new ApplyResponse();

		if (request == null || StringUtils.isBlank(request.getSessionToken())) {
			response.setResponseStatus(ApplyResponseStatusEnum.NO_SESSION);
			return response;
		}

		//authenticate the session token and find out the userId
		AuthenticationTO authentication = authenticationService.authenticate(request.getSessionToken());
		if (authentication == null) {
			//invalid session token
			response.setResponseStatus(ApplyResponseStatusEnum.NO_SESSION);
			return response;
		}
		
		response.setSessionToken(request.getSessionToken());

		try {
			//Find the gift voucher
			GiftVoucher eGV = giftVoucherService.applyGiftVoucher(request.getGiftVoucherNumber(),request.getGiftVoucherPin());
			response.setAmount(eGV.getAmount().getAmount());
			response.setNumber(Long.parseLong(eGV.getNumber()));
			response.setResponseStatus(ApplyResponseStatusEnum.SUCCESS);
			
		} catch (GiftVoucherNotFoundException e) {
			response.setResponseStatus(ApplyResponseStatusEnum.INVALID_GIFT_VOUCHER_NUMBER);
		} catch (InvalidPinException e) {
			response.setResponseStatus(ApplyResponseStatusEnum.INVALID_GIFT_VOUCHER_PIN);
		} catch (GiftVoucherExpiredException e) {
			response.setResponseStatus(ApplyResponseStatusEnum.GIFT_VOUCHER_EXPIRED);
		} catch (GiftVoucherAlreadyUsedException e) {
			response.setResponseStatus(ApplyResponseStatusEnum.ALREADY_USED);
		} catch (PlatformException e) {
			response.setResponseStatus(ApplyResponseStatusEnum.INTERNAL_ERROR);
		}

		return response;

	}
	
	public void setAuthenticationService(AuthenticationService authenticationService) {
		this.authenticationService = authenticationService;
	}

	public void setGiftVoucherService(GiftVoucherService giftVoucherService) {
		this.giftVoucherService = giftVoucherService;
	}

}
