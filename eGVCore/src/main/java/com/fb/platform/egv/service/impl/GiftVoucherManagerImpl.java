/**
 * 
 */
package com.fb.platform.egv.service.impl;

import java.math.BigDecimal;
import java.util.HashMap;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;

import com.fb.commons.PlatformException;
import com.fb.commons.mail.exception.MailerException;
import com.fb.commons.mail.exception.SmsException;
import com.fb.platform.auth.AuthenticationService;
import com.fb.platform.auth.AuthenticationTO;
import com.fb.platform.egv.exception.GiftVoucherAlreadyUsedException;
import com.fb.platform.egv.exception.GiftVoucherException;
import com.fb.platform.egv.exception.GiftVoucherExpiredException;
import com.fb.platform.egv.exception.GiftVoucherNotFoundException;
import com.fb.platform.egv.exception.InvalidPinException;
import com.fb.platform.egv.model.GiftVoucher;
import com.fb.platform.egv.service.GiftVoucherManager;
import com.fb.platform.egv.service.GiftVoucherService;
import com.fb.platform.egv.to.ActivateRequest;
import com.fb.platform.egv.to.ActivateResponse;
import com.fb.platform.egv.to.ActivateResponseStatusEnum;
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
import com.fb.platform.egv.to.GetPinRequest;
import com.fb.platform.egv.to.GetPinResponse;
import com.fb.platform.egv.to.GetPinResponseStatusEnum;
import com.fb.platform.egv.to.RollbackUseRequest;
import com.fb.platform.egv.to.RollbackUseResponse;
import com.fb.platform.egv.to.RollbackUseResponseStatusEnum;
import com.fb.platform.egv.to.SendPinRequest;
import com.fb.platform.egv.to.SendPinResponse;
import com.fb.platform.egv.to.SendPinResponseStatusEnum;
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
		if (logger.isDebugEnabled()) {
			logger.debug("Creating Gift Voucher worth : " + request.getAmount());
		}
		CreateResponse response = new CreateResponse();

		if (request == null || StringUtils.isBlank(request.getSessionToken())) {
			response.setResponseStatus(CreateResponseStatusEnum.NO_SESSION);
			return response;
		}

		// authenticate the session token and find out the userId
		AuthenticationTO authentication = authenticationService.authenticate(request.getSessionToken());
		if (authentication == null) {
			// invalid session token
			response.setResponseStatus(CreateResponseStatusEnum.NO_SESSION);
			return response;
		}

		response.setSessionToken(request.getSessionToken());

		int userId = authentication.getUserID();

		GiftVoucher gv = new GiftVoucher();
		try {
			// create the gift voucher
			System.out.println("In mngr, valid till = " + request.getValidTill() + " and validFrom = "
					+ request.getValidFrom() + " and isDefer = " + request.isDeferActivation());
			gv = giftVoucherService.createGiftVoucher(request.getEmail(), userId, request.getAmount(),
					request.getOrderItemId(), request.getSenderName(), request.getReceiverName(),
					request.getGiftMessage(), request.getMobile(), request.getValidFrom(), request.getValidTill(),
					!request.isDeferActivation());
			response.setGvNumber(Long.parseLong(gv.getNumber()));
			response.setValidFrom(gv.getValidFrom());
			response.setValidTill(gv.getValidTill());
			response.setResponseStatus(CreateResponseStatusEnum.SUCCESS);

		} catch (SmsException e) {
			logger.error("Problem Sending SMS ", e);
			response.setResponseStatus(CreateResponseStatusEnum.SMS_SEND_FAILURE);
		} catch (MailException e) {
			logger.error("Problem in sending mail to " + gv.getEmail(), e);
			response.setResponseStatus(CreateResponseStatusEnum.EMAIL_SEND_FAILURE);
			throw new MailerException("Error sending mail", e);
		} catch (PlatformException e) {
			logger.error("Problem while creating new Gift Voucher of Amount : " + request.getAmount() + " for email "
					+ request.getEmail());
			response.setResponseStatus(CreateResponseStatusEnum.INTERNAL_ERROR);
		}

		return response;
	}

	@Override
	public CancelResponse cancel(CancelRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("Cancelling Gift Voucher : " + request.getGiftVoucherNumber());
		}
		CancelResponse response = new CancelResponse();

		if (request == null || StringUtils.isBlank(request.getSessionToken())) {
			response.setResponseStatus(CancelResponseStatusEnum.NO_SESSION);
			return response;
		}

		// authenticate the session token and find out the userId
		AuthenticationTO authentication = authenticationService.authenticate(request.getSessionToken());
		if (authentication == null) {
			// invalid session token
			response.setResponseStatus(CancelResponseStatusEnum.NO_SESSION);
			return response;
		}

		response.setSessionToken(request.getSessionToken());

		int userId = authentication.getUserID();

		try {
			// cancel the gift voucher
			giftVoucherService.cancelGiftVoucher(request.getGiftVoucherNumber(), userId, request.getOrderItemId());
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
		if (logger.isDebugEnabled()) {
			logger.debug("Use Request Manager Start");
		}
		UseResponse response = new UseResponse();

		response.setSessionToken(request.getSessionToken());
		if (request == null || StringUtils.isBlank(request.getSessionToken())) {
			response.setResponseStatus(UseResponseStatusEnum.NO_SESSION);
			return response;
		}

		// authenticate the session token and find out the userId
		AuthenticationTO authentication = authenticationService.authenticate(request.getSessionToken());
		if (authentication == null) {
			// invalid session token
			response.setResponseStatus(UseResponseStatusEnum.NO_SESSION);
			return response;
		}

		response.setSessionToken(request.getSessionToken());

		int userId = authentication.getUserID();

		HashMap<Long, BigDecimal> gvDetails = request.getGiftVoucherDetails();
		try {
			for (Long gvNumber : gvDetails.keySet()) {

				if (logger.isDebugEnabled()) {
					logger.debug("Using Gift Voucher : " + gvNumber + " as payment in order : " + request.getOrderId()
							+ " worth : " + gvDetails.get(gvNumber));
				}

				// use the gift voucher
				giftVoucherService.useGiftVoucher(userId, gvDetails.get(gvNumber), request.getOrderId(), gvNumber);
			}
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
		if (logger.isDebugEnabled()) {
			logger.debug("Getting Gift Voucher  : " + request.getGiftVoucherNumber());
		}
		GetInfoResponse response = new GetInfoResponse();

		if (request == null || StringUtils.isBlank(request.getSessionToken())) {
			response.setResponseStatus(GetInfoResponseStatusEnum.NO_SESSION);
			return response;
		}

		// authenticate the session token and find out the userId
		AuthenticationTO authentication = authenticationService.authenticate(request.getSessionToken());
		if (authentication == null) {
			// invalid session token
			response.setResponseStatus(GetInfoResponseStatusEnum.NO_SESSION);
			return response;
		}

		response.setSessionToken(request.getSessionToken());

		try {
			// Find the gift voucher
			GiftVoucher eGV = giftVoucherService.getGiftVoucher(request.getGiftVoucherNumber());
			response.setAmount(eGV.getAmount().getAmount());
			response.setEmail(eGV.getEmail());
			response.setNumber(Long.parseLong(eGV.getNumber()));
			response.setOrderItemId(eGV.getOrderItemId());
			response.setStatus(eGV.getStatus());
			response.setValidFrom(eGV.getValidFrom());
			response.setValidTill(eGV.getValidTill());
			response.setUserId(eGV.getUserId());
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
		if (logger.isDebugEnabled()) {
			logger.debug("Getting Gift Voucher  : " + request.getGiftVoucherNumber());
		}
		ApplyResponse response = new ApplyResponse();

		if (request == null || StringUtils.isBlank(request.getSessionToken())) {
			response.setResponseStatus(ApplyResponseStatusEnum.NO_SESSION);
			return response;
		}

		// authenticate the session token and find out the userId
		AuthenticationTO authentication = authenticationService.authenticate(request.getSessionToken());
		if (authentication == null) {
			// invalid session token
			response.setResponseStatus(ApplyResponseStatusEnum.NO_SESSION);
			return response;
		}

		response.setSessionToken(request.getSessionToken());

		try {
			// Find the gift voucher
			GiftVoucher eGV = giftVoucherService.applyGiftVoucher(request.getGiftVoucherNumber(),
					request.getGiftVoucherPin());
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

	@Override
	public RollbackUseResponse rollbackUse(RollbackUseRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("Rollback Use of Gift Voucher : " + request.getGiftVoucherNumber() + " as payment in order : "
					+ request.getOrderId());
		}
		RollbackUseResponse response = new RollbackUseResponse();

		if (request == null || StringUtils.isBlank(request.getSessionToken())) {
			response.setResponseStatus(RollbackUseResponseStatusEnum.NO_SESSION);
			return response;
		}

		// authenticate the session token and find out the userId
		AuthenticationTO authentication = authenticationService.authenticate(request.getSessionToken());
		if (authentication == null) {
			// invalid session token
			response.setResponseStatus(RollbackUseResponseStatusEnum.NO_SESSION);
			return response;
		}

		response.setSessionToken(request.getSessionToken());

		int userId = authentication.getUserID();

		try {
			// use the gift voucher
			giftVoucherService.rollbackUseGiftVoucher(userId, request.getOrderId(), request.getGiftVoucherNumber());
			response.setResponseStatus(RollbackUseResponseStatusEnum.SUCCESS);

		} catch (GiftVoucherNotFoundException e) {
			logger.error("Rollback eGV Usage Error : GV not found, GV num = " + request.getGiftVoucherNumber());
			response.setResponseStatus(RollbackUseResponseStatusEnum.INTERNAL_ERROR);
		} catch (GiftVoucherExpiredException e) {
			logger.error("Rollback eGV Usage Error : GV Expired, GV num = " + request.getGiftVoucherNumber());
			response.setResponseStatus(RollbackUseResponseStatusEnum.GIFT_VOUCHER_EXPIRED);
		} catch (GiftVoucherException e) {
			logger.error("Rollback eGV Usage Error : GV num = " + request.getGiftVoucherNumber(), e);
			response.setResponseStatus(RollbackUseResponseStatusEnum.INTERNAL_ERROR);
		} catch (PlatformException e) {
			logger.error("Rollback eGV Usage Error : GV num = " + request.getGiftVoucherNumber(), e);
			response.setResponseStatus(RollbackUseResponseStatusEnum.INTERNAL_ERROR);
		}

		return response;
	}

	@Override
	public ActivateResponse activate(ActivateRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("Getting Gift Voucher  : " + request.getGiftVoucherNumber());
		}
		ActivateResponse response = new ActivateResponse();

		System.out.println("Session Token is " + request.getSessionToken());
		logger.debug("Session Token is " + request.getSessionToken());

		if (request == null || StringUtils.isBlank(request.getSessionToken())) {
			response.setResponseStatus(ActivateResponseStatusEnum.NO_SESSION);
			return response;
		}

		// authenticate the session token and find out the userId
		AuthenticationTO authentication = authenticationService.authenticate(request.getSessionToken());
		if (authentication == null) {
			// invalid session token
			response.setResponseStatus(ActivateResponseStatusEnum.NO_SESSION);
			return response;
		}

		response.setSessionToken(request.getSessionToken());

		try {
			// Find the gift voucher
			GiftVoucher eGV = giftVoucherService.activateGiftVoucher(request.getGiftVoucherNumber(),
					request.getValidFrom(), request.getValidTill(), request.getAmount());
			response.setAmount(eGV.getAmount().getAmount());
			response.setNumber(Long.parseLong(eGV.getNumber()));
			response.setNumber(Long.parseLong(eGV.getNumber()));
			response.setResponseStatus(ActivateResponseStatusEnum.SUCCESS);

		} catch (GiftVoucherNotFoundException e) {
			response.setResponseStatus(ActivateResponseStatusEnum.INVALID_GIFT_VOUCHER_NUMBER);
		} catch (PlatformException e) {
			response.setResponseStatus(ActivateResponseStatusEnum.INTERNAL_ERROR);
		}

		return response;

	}

	@Override
	public SendPinResponse sendPin(SendPinRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("SendPin of Gift Voucher  : " + request.getGiftVoucherNumber());
		}
		SendPinResponse response = new SendPinResponse();

		if (request == null || StringUtils.isBlank(request.getSessionToken())) {
			response.setResponseStatus(SendPinResponseStatusEnum.NO_SESSION);
			response.setNumber(request.getGiftVoucherNumber());
			return response;
		}

		// authenticate the session token and find out the userId
		AuthenticationTO authentication = authenticationService.authenticate(request.getSessionToken());
		if (authentication == null) {
			// invalid session token
			response.setResponseStatus(SendPinResponseStatusEnum.NO_SESSION);
			response.setNumber(request.getGiftVoucherNumber());
			return response;
		}

		response.setSessionToken(request.getSessionToken());
		response.setNumber(request.getGiftVoucherNumber());

		try {
			// Find the gift voucher
			giftVoucherService.sendGiftVoucherPin(request.getGiftVoucherNumber(), request.getEmail(),
					request.getMobile(), request.getSenderName(), request.getReceiverName(), request.getGiftMessage());
			response.setResponseStatus(SendPinResponseStatusEnum.SUCCESS);

		} catch (GiftVoucherNotFoundException e) {
			response.setResponseStatus(SendPinResponseStatusEnum.INVALID_GIFT_VOUCHER_NUMBER);
		} catch (SmsException e) {
			logger.error("Problem Sending SMS ", e);
			response.setResponseStatus(SendPinResponseStatusEnum.SMS_SEND_FAILURE);
		} catch (MailException e) {
			logger.error("Problem Sending Email", e);
			response.setResponseStatus(SendPinResponseStatusEnum.EMAIL_SEND_FAILURE);
		} catch (PlatformException e) {
			response.setResponseStatus(SendPinResponseStatusEnum.INTERNAL_ERROR);
		}

		return response;

	}

	@Override
	public GetPinResponse getPin(GetPinRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("GetPin of Gift Voucher  : " + request.getGiftVoucherNumber());
		}
		GetPinResponse response = new GetPinResponse();

		if (request == null || StringUtils.isBlank(request.getSessionToken())) {
			response.setResponseStatus(GetPinResponseStatusEnum.NO_SESSION);
			response.setNumber(request.getGiftVoucherNumber());
			return response;
		}

		// authenticate the session token and find out the userId
		AuthenticationTO authentication = authenticationService.authenticate(request.getSessionToken());
		if (authentication == null) {
			// invalid session token
			response.setResponseStatus(GetPinResponseStatusEnum.NO_SESSION);
			response.setNumber(request.getGiftVoucherNumber());
			return response;
		}

		response.setSessionToken(request.getSessionToken());

		try {
			// Find the gift voucher
			String pin = giftVoucherService.getGiftVoucherPin(request.getGiftVoucherNumber());
			response.setNumber(request.getGiftVoucherNumber());
			response.setPin(pin);
			response.setResponseStatus(GetPinResponseStatusEnum.SUCCESS);

		} catch (GiftVoucherNotFoundException e) {
			response.setResponseStatus(GetPinResponseStatusEnum.INVALID_GIFT_VOUCHER_NUMBER);
		} catch (SmsException e) {
			logger.error("Problem Sending SMS ", e);
			response.setResponseStatus(GetPinResponseStatusEnum.SMS_SEND_FAILURE);
		} catch (MailException e) {
			logger.error("Problem Sending Email", e);
			response.setResponseStatus(GetPinResponseStatusEnum.EMAIL_SEND_FAILURE);
		} catch (PlatformException e) {
			response.setResponseStatus(GetPinResponseStatusEnum.INTERNAL_ERROR);
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
