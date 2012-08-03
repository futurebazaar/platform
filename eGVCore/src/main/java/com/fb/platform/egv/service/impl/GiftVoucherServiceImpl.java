/**
 * 
 */
package com.fb.platform.egv.service.impl;

import java.math.BigDecimal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.mail.MailException;

import com.fb.commons.PlatformException;
import com.fb.commons.communication.to.SmsTO;
import com.fb.commons.mail.MailSender;
import com.fb.commons.mail.exception.MailerException;
import com.fb.commons.mail.exception.SmsException;
import com.fb.commons.mom.to.MailTO;
import com.fb.commons.sms.SmsSender;
import com.fb.platform.egv.dao.GiftVoucherDao;
import com.fb.platform.egv.exception.GiftVoucherAlreadyUsedException;
import com.fb.platform.egv.exception.GiftVoucherException;
import com.fb.platform.egv.exception.GiftVoucherExpiredException;
import com.fb.platform.egv.exception.GiftVoucherNotFoundException;
import com.fb.platform.egv.exception.InvalidPinException;
import com.fb.platform.egv.exception.NoOrderItemExistsException;
import com.fb.platform.egv.model.GiftVoucher;
import com.fb.platform.egv.model.GiftVoucherStatusEnum;
import com.fb.platform.egv.service.GiftVoucherService;
import com.fb.platform.egv.util.GiftVoucherPinUtil;
import com.fb.platform.egv.util.MailHelper;
import com.fb.platform.egv.util.RandomGenerator;
import com.fb.platform.egv.util.SmsHelper;

/**
 * @author keith
 * 
 */
public class GiftVoucherServiceImpl implements GiftVoucherService {

	private static Log logger = LogFactory.getLog(GiftVoucherServiceImpl.class);

	private static int GV_NUMBER_LENGTH = 11;

	private static int GV_PIN_LENGTH = 5;

	@Autowired
	private GiftVoucherDao giftVoucherDao = null;

	@Autowired
	private MailSender mailSender = null;

	@Autowired
	private SmsSender smsSender = null;

	public void setGiftVoucherDao(GiftVoucherDao giftVoucherDao) {
		this.giftVoucherDao = giftVoucherDao;
	}

	public void setMailSender(MailSender mailSender) {
		this.mailSender = mailSender;
	}

	public void setSmsSender(SmsSender smsSender) {
		this.smsSender = smsSender;
	}

	@Override
	public GiftVoucher getGiftVoucher(int giftVoucherId) throws GiftVoucherNotFoundException, PlatformException {
		GiftVoucher giftVoucher;

		try {
			giftVoucher = giftVoucherDao.load(giftVoucherId);
		} catch (DataAccessException e) {
			throw new PlatformException("Error while loading the GiftVoucher. GiftVoucherId  : " + giftVoucherId, e);
		}

		return giftVoucher;
	}

	@Override
	public GiftVoucher getGiftVoucher(long giftVoucherNumber) throws GiftVoucherNotFoundException, PlatformException {

		GiftVoucher giftVoucher;

		try {
			giftVoucher = giftVoucherDao.load(giftVoucherNumber);
		} catch (GiftVoucherNotFoundException e) {
			logger.info("No Such Gift Voucher Exists :  " + giftVoucherNumber);
			throw new GiftVoucherNotFoundException("No Such Gift Voucher Exists :  " + giftVoucherNumber, e);
		} catch (DataAccessException e) {
			logger.error("Error while loading the GiftVoucher. GiftVoucherId  : " + giftVoucherNumber);
			throw new PlatformException("Error while loading the GiftVoucher. GiftVoucherId  : " + giftVoucherNumber, e);
		}

		return giftVoucher;
	}

	@Override
	public GiftVoucher applyGiftVoucher(long giftVoucherNumber, String giftVoucherPin)
			throws GiftVoucherNotFoundException, InvalidPinException, GiftVoucherExpiredException, PlatformException {

		GiftVoucher eGV;

		try {
			eGV = giftVoucherDao.load(giftVoucherNumber);
			if (!eGV.isValidPin(giftVoucherPin)) {
				throw new InvalidPinException();
			}
			if (eGV.isExpired()) {
				logger.info("Gift Voucher expiry date  " + eGV.getDates().getValidTill());
				throw new GiftVoucherExpiredException();
			}
			if (!eGV.isUsable()) {
				throw new GiftVoucherAlreadyUsedException();
			}
		} catch (GiftVoucherNotFoundException e) {
			logger.info("No Such Gift Voucher Exists :  " + giftVoucherNumber);
			throw new GiftVoucherNotFoundException("No Such Gift Voucher Exists :  " + giftVoucherNumber, e);
		} catch (InvalidPinException e) {
			logger.info("Pin entered is invalid " + giftVoucherPin);
			throw new InvalidPinException("Pin entered is invalid " + giftVoucherPin, e);
		} catch (GiftVoucherExpiredException e) {
			logger.info("Gift Voucher has expired GV number : " + giftVoucherNumber);
			throw new GiftVoucherExpiredException("Gift Voucher has expired GV number : " + giftVoucherNumber, e);
		} catch (GiftVoucherAlreadyUsedException e) {
			logger.info("Gift Voucher has GV number : " + giftVoucherNumber + " has laready been used");
			throw new GiftVoucherAlreadyUsedException("Gift Voucher has GV number : " + giftVoucherNumber
					+ " has laready been used", e);
		} catch (DataAccessException e) {
			logger.error("Error while loading the GiftVoucher. GiftVoucherId  : " + giftVoucherNumber);
			throw new PlatformException("Error while loading the GiftVoucher. GiftVoucherId  : " + giftVoucherNumber, e);
		}

		return eGV;
	}

	@Override
	public GiftVoucher createGiftVoucher(String email, int userId, BigDecimal amount, int orderItemId,
			String senderName, String receiverName, String giftMessage, String mobile, DateTime validFrom,
			DateTime validTill, boolean active) throws NoOrderItemExistsException, MailerException, PlatformException {
		String numGenerated = RandomGenerator.integerRandomGenerator(GV_NUMBER_LENGTH);
		long gvNumber = Long.parseLong(numGenerated);
		GiftVoucher eGV = new GiftVoucher();
		String gvPin = RandomGenerator.integerRandomGenerator(GV_PIN_LENGTH);
		giftVoucherDao.createGiftVoucher(gvNumber, GiftVoucherPinUtil.getEncryptedPassword(gvPin), email, userId,
				amount, active ? GiftVoucherStatusEnum.CONFIRMED : GiftVoucherStatusEnum.INACTIVE, orderItemId, mobile,
				validFrom, validTill);
		// logger.debug("eGV created, now checking if valid orderItemId " +
		// orderItemId);
		try {

			// Removed the constraint (due to Txn prob ) and Added below check :
			// Remove after constraint is added
			// if(!orderItemDao.isValidId(orderItemId)) {
			// throw new NoOrderItemExistsException("No such OrderItem " +
			// orderItemId);
			// }
			// logger.debug("OrderItem is Valid");

			// load the eGV info
			eGV = giftVoucherDao.load(gvNumber);
			if (active) {
				// Send email
				if (!(email == null || email.isEmpty())) {
					// code to send email
					logger.debug("Sending Email to " + email);
					MailTO message = MailHelper.createMailTO(eGV.getEmail(), amount, Long.toString(gvNumber), gvPin,
							eGV.getValidTill(), senderName, receiverName, giftMessage);
					mailSender.send(message);
				}
				// Send SMS

				if (!(mobile == null || mobile.isEmpty())) {
					logger.debug("Sending SMS to " + mobile);
					// code to send email
					SmsTO smsTo = SmsHelper.createSmsTO(eGV.getMobile(), amount, Long.toString(gvNumber), gvPin,
							eGV.getValidTill(), senderName, receiverName, giftMessage);
					smsSender.send(smsTo);
				}
			}
		} catch (MailException e) {
			throw new MailerException("Error sending mail", e);
		} catch (SmsException e) {
			throw new SmsException("Error sending SMS", e);
		}

		return eGV;

	}

	@Override
	public void useGiftVoucher(int userId, BigDecimal amount, int orderId, long giftVoucherNumber) {
		GiftVoucher eGV = new GiftVoucher();
		try {
			eGV = giftVoucherDao.load(giftVoucherNumber);

			if (eGV.isUsable()) {
				eGV.setStatus(GiftVoucherStatusEnum.USED);
				giftVoucherDao.changeState(giftVoucherNumber, GiftVoucherStatusEnum.USED);
				giftVoucherDao.createGiftVoucherUse(giftVoucherNumber, userId, orderId, amount);
			} else {
				throw new GiftVoucherAlreadyUsedException("Gift Voucher has GV number : " + giftVoucherNumber
						+ " has laready been used");
			}
		} catch (GiftVoucherNotFoundException e) {
			logger.info("No Such Gift Voucher Exists :  " + giftVoucherNumber);
			throw new GiftVoucherNotFoundException("No Such Gift Voucher Exists :  " + giftVoucherNumber, e);
		} catch (GiftVoucherExpiredException e) {
			logger.info("Gift Voucher has expired GV number : " + giftVoucherNumber);
			throw new GiftVoucherExpiredException("Gift Voucher has expired GV number : " + giftVoucherNumber, e);
		} catch (GiftVoucherAlreadyUsedException e) {
			logger.info("Gift Voucher has GV number : " + giftVoucherNumber + " has laready been used");
			throw new GiftVoucherAlreadyUsedException("Gift Voucher has GV number : " + giftVoucherNumber
					+ " has laready been used", e);
		} catch (DataAccessException e) {
			logger.error("Error while loading the GiftVoucher. GiftVoucherId  : " + giftVoucherNumber);
			throw new PlatformException("Error while loading the GiftVoucher. GiftVoucherId  : " + giftVoucherNumber, e);
		}
	}

	@Override
	public void cancelGiftVoucher(long giftVoucherNumber, int userId, int orderItemId) {
		GiftVoucher eGV = new GiftVoucher();
		try {
			eGV = giftVoucherDao.load(giftVoucherNumber);
			if (eGV.isUsable()) {
				eGV.setStatus(GiftVoucherStatusEnum.CANCELLED);
				giftVoucherDao.changeState(giftVoucherNumber, GiftVoucherStatusEnum.CANCELLED);
			} else {
				throw new GiftVoucherAlreadyUsedException();
			}
		} catch (GiftVoucherNotFoundException e) {
			logger.info("No Such Gift Voucher Exists :  " + giftVoucherNumber);
			throw new GiftVoucherNotFoundException("No Such Gift Voucher Exists :  " + giftVoucherNumber, e);
		} catch (GiftVoucherAlreadyUsedException e) {
			logger.info("Gift Voucher has GV number : " + giftVoucherNumber + " has laready been used");
			throw new GiftVoucherAlreadyUsedException("Gift Voucher has GV number : " + giftVoucherNumber
					+ " has laready been used", e);
		} catch (DataAccessException e) {
			logger.error("Error while loading the GiftVoucher. GiftVoucherId  : " + giftVoucherNumber);
			throw new PlatformException("Error while loading the GiftVoucher. GiftVoucherId  : " + giftVoucherNumber, e);
		}
	}

	@Override
	public void rollbackUseGiftVoucher(int userId, int orderId, long giftVoucherNumber) {
		GiftVoucher eGV = new GiftVoucher();
		try {
			eGV = giftVoucherDao.load(giftVoucherNumber);
			if (eGV.isUsed()) {
				eGV.setStatus(GiftVoucherStatusEnum.USE_ROLLBACKED);
				giftVoucherDao.changeState(giftVoucherNumber, GiftVoucherStatusEnum.USE_ROLLBACKED);
				giftVoucherDao.deleteUse(giftVoucherNumber, userId, orderId);
			} else {
				throw new GiftVoucherException("Gift Voucher with GV number : " + giftVoucherNumber + " is not used");
			}
		} catch (GiftVoucherNotFoundException e) {
			logger.error("Unable to Rollback : No Such Gift Voucher Exists :  " + giftVoucherNumber);
			throw new GiftVoucherNotFoundException("No Such Gift Voucher Exists :  " + giftVoucherNumber, e);
		} catch (GiftVoucherExpiredException e) {
			logger.info("Unable to Rollback : Gift Voucher has expired, GV number : " + giftVoucherNumber);
			throw new GiftVoucherExpiredException("Gift Voucher has expired GV number : " + giftVoucherNumber, e);
		} catch (DataAccessException e) {
			logger.error("Unable to Rollback : Error while loading the GiftVoucher. GiftVoucherId  : "
					+ giftVoucherNumber);
			throw new GiftVoucherException(
					"Error while loading the GiftVoucher. GiftVoucherId  : " + giftVoucherNumber, e);
		} catch (GiftVoucherException e) {
			logger.error("Unable to Rollback : Gift Voucher with GV number : " + giftVoucherNumber + " is not used");
			throw new GiftVoucherException("Gift Voucher with GV number : " + giftVoucherNumber + " is not used");
		} catch (PlatformException e) {
			throw new PlatformException("Gift Voucher with GV number : " + giftVoucherNumber + " is not used");
		} catch (Exception e) {
			throw new PlatformException("Gift Voucher with GV number : " + giftVoucherNumber + " is not used");
		}

	}

	@Override
	public GiftVoucher activateGiftVoucher(long giftVoucherNumber, DateTime validFrom, DateTime validTill,
			BigDecimal amount) {

		GiftVoucher eGV;

		String giftMessage = "";

		try {
			eGV = giftVoucherDao.load(giftVoucherNumber);
			String gvPin = RandomGenerator.integerRandomGenerator(GV_PIN_LENGTH);
			giftVoucherDao.updateGiftVoucher(giftVoucherNumber, GiftVoucherPinUtil.getEncryptedPassword(gvPin),
					eGV.getEmail(), eGV.getUserId(), amount, GiftVoucherStatusEnum.CONFIRMED, eGV.getOrderItemId(),
					eGV.getMobile(), (validFrom == null) ? eGV.getValidFrom() : validFrom,
					(validTill == null) ? eGV.getValidTill() : validTill);

			return eGV;

		} catch (GiftVoucherNotFoundException e) {
			logger.info("No Such Gift Voucher Exists :  " + giftVoucherNumber);
			throw new GiftVoucherNotFoundException("No Such Gift Voucher Exists :  " + giftVoucherNumber, e);
		} catch (DataAccessException e) {
			logger.error("Error while loading the GiftVoucher. GiftVoucherId  : " + giftVoucherNumber);
			e.printStackTrace();
			throw new PlatformException("Error while loading the GiftVoucher. GiftVoucherId  : " + giftVoucherNumber, e);
		}

	}

	@Override
	public void sendGiftVoucherPin(long giftVoucherNumber, String email, String mobile, String senderName,
			String receiverName, String giftMessage) {

		GiftVoucher eGV;

		giftMessage = giftMessage == null ? "" : giftMessage;

		try {
			eGV = giftVoucherDao.load(giftVoucherNumber);
			String gvPin = RandomGenerator.integerRandomGenerator(GV_PIN_LENGTH);
			giftVoucherDao.updateGiftVoucher(giftVoucherNumber, GiftVoucherPinUtil.getEncryptedPassword(gvPin),
					eGV.getEmail(), eGV.getUserId(), eGV.getAmount().getAmount(), GiftVoucherStatusEnum.CONFIRMED,
					eGV.getOrderItemId(), eGV.getMobile(), eGV.getValidFrom(), eGV.getValidTill());
			// Send email
			if (!(email == null || email.isEmpty())) {
				logger.debug("Sending Email to " + email);
				MailTO message = MailHelper.createMailTO(email, eGV.getAmount().getAmount(),
						Long.toString(giftVoucherNumber), gvPin, eGV.getValidTill(), senderName, receiverName,
						giftMessage);
				mailSender.send(message);
			}
			// Send SMS
			if (!(mobile == null || mobile.isEmpty())) {
				logger.debug("Sending Mobile to " + mobile);
				SmsTO smsTo = SmsHelper.createSmsTO(mobile, eGV.getAmount().getAmount(),
						Long.toString(giftVoucherNumber), gvPin, eGV.getValidTill(), senderName, receiverName,
						giftMessage);
				logger.debug("Sending SMS to " + mobile);
				String smsOutput = smsSender.send(smsTo);
				logger.debug(" SMS output is " + smsOutput);
			}
		} catch (GiftVoucherNotFoundException e) {
			logger.info("No Such Gift Voucher Exists :  " + giftVoucherNumber);
			throw new GiftVoucherNotFoundException("No Such Gift Voucher Exists :  " + giftVoucherNumber, e);
		} catch (DataAccessException e) {
			logger.error("Error while loading the GiftVoucher. GiftVoucherId  : " + giftVoucherNumber);
			throw new PlatformException("Error while loading the GiftVoucher. GiftVoucherId  : " + giftVoucherNumber, e);
		}

	}
}
