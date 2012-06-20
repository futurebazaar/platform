/**
 * 
 */
package com.fb.platform.egv.service.impl;

import java.math.BigDecimal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.mail.MailException;

import com.fb.commons.PlatformException;
import com.fb.commons.mail.MailSender;
import com.fb.commons.mail.exception.MailerException;
import com.fb.commons.mail.to.MailTO;
import com.fb.platform.egv.dao.GiftVoucherDao;
import com.fb.platform.egv.dao.OrderItemDao;
import com.fb.platform.egv.model.GiftVoucher;
import com.fb.platform.egv.model.GiftVoucherStatusEnum;
import com.fb.platform.egv.service.GiftVoucherAlreadyUsedException;
import com.fb.platform.egv.service.GiftVoucherExpiredException;
import com.fb.platform.egv.service.GiftVoucherNotFoundException;
import com.fb.platform.egv.service.GiftVoucherService;
import com.fb.platform.egv.service.InvalidPinException;
import com.fb.platform.egv.service.NoOrderItemExistsException;
import com.fb.platform.egv.util.GiftVoucherPinUtil;
import com.fb.platform.egv.util.RandomGenerator;
import com.fb.platform.egv.util.MailHelper;

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
	private OrderItemDao orderItemDao = null;
	
	@Autowired
	private MailSender mailSender = null;

	public void setGiftVoucherDao(GiftVoucherDao giftVoucherDao) {
		this.giftVoucherDao = giftVoucherDao;
	}
	
	public void setMailSender(MailSender mailSender) {
		this.mailSender = mailSender;
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
	public GiftVoucher applyGiftVoucher(long giftVoucherNumber,String giftVoucherPin) throws GiftVoucherNotFoundException, InvalidPinException, GiftVoucherExpiredException, PlatformException {
		
		GiftVoucher eGV;
		
		try {
			eGV = giftVoucherDao.load(giftVoucherNumber);
			if(!eGV.isValidPin(giftVoucherPin)) {
				throw new InvalidPinException();
			}
			if(eGV.isExpired()) {
				logger.info("Gift Voucher expiry date  " + eGV.getDates().getValidTill());
				throw new GiftVoucherExpiredException();
			}
			if(!eGV.isUsable()) {
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
			throw new GiftVoucherAlreadyUsedException("Gift Voucher has GV number : " + giftVoucherNumber + " has laready been used", e);
		} catch (DataAccessException e) {
			logger.error("Error while loading the GiftVoucher. GiftVoucherId  : " + giftVoucherNumber);
			throw new PlatformException("Error while loading the GiftVoucher. GiftVoucherId  : " + giftVoucherNumber, e);
		}
	
		return eGV;
	}
	
	@Override
	public GiftVoucher createGiftVoucher(String email, int userId,
			BigDecimal amount, int orderItemId) throws NoOrderItemExistsException, MailerException, PlatformException {
		String numGenerated = RandomGenerator.integerRandomGenerator(GV_NUMBER_LENGTH);
		long gvNumber = Long.parseLong(numGenerated);
		GiftVoucher eGV = new GiftVoucher();
		String gvPin = RandomGenerator.integerRandomGenerator(GV_PIN_LENGTH);
		 giftVoucherDao.createGiftVoucher(gvNumber,GiftVoucherPinUtil.getEncryptedPassword(gvPin),email,userId,amount,GiftVoucherStatusEnum.CONFIRMED,orderItemId);
		 try {
			 if(orderItemDao.isValidId(orderItemId)) {
				 throw new NoOrderItemExistsException("No such OrderItem " + orderItemId);  
			 }
		 	eGV = giftVoucherDao.load(gvNumber);
		 
		    //code to send email
		 	MailTO message = MailHelper.createMailTO(eGV.getEmail(),amount,Long.toString(gvNumber),gvPin);
			mailSender.send(message);
		 } catch (NoOrderItemExistsException e) {
			 throw new NoOrderItemExistsException("No such OrderItem " + orderItemId);
		 } catch (MailException e) {
			throw new MailerException("Error sending mail", e);
		 }

		return eGV;
		
	}

	@Override
	public void useGiftVoucher(int userId, BigDecimal amount, int orderId,
			long giftVoucherNumber) {
		GiftVoucher eGV = new GiftVoucher();
		try {
			eGV = giftVoucherDao.load(giftVoucherNumber);
			
			if(eGV.isUsable())
			{
				eGV.setStatus(GiftVoucherStatusEnum.USED);
				giftVoucherDao.changeState(giftVoucherNumber, GiftVoucherStatusEnum.USED);
				giftVoucherDao.createGiftVoucherUse(giftVoucherNumber, userId, orderId, amount);
			}
		} catch (GiftVoucherNotFoundException e) {
			logger.info("No Such Gift Voucher Exists :  " + giftVoucherNumber);
			throw new GiftVoucherNotFoundException("No Such Gift Voucher Exists :  " + giftVoucherNumber, e);
		} catch (GiftVoucherExpiredException e) {
			logger.info("Gift Voucher has expired GV number : " + giftVoucherNumber);
			throw new GiftVoucherExpiredException("Gift Voucher has expired GV number : " + giftVoucherNumber, e);
		} catch (GiftVoucherAlreadyUsedException e) {
			logger.info("Gift Voucher has GV number : " + giftVoucherNumber + " has laready been used");
			throw new GiftVoucherAlreadyUsedException("Gift Voucher has GV number : " + giftVoucherNumber + " has laready been used", e);
		} catch (DataAccessException e) {
			logger.error("Error while loading the GiftVoucher. GiftVoucherId  : " + giftVoucherNumber);
			throw new PlatformException("Error while loading the GiftVoucher. GiftVoucherId  : " + giftVoucherNumber, e);
		}
	}

	@Override
	public void cancelGiftVoucher(long giftVoucherNumber,int userId, int orderItemId) {
		GiftVoucher eGV = new GiftVoucher();
		try {
			eGV = giftVoucherDao.load(giftVoucherNumber);
			if(eGV.isUsable())
			{
				eGV.setStatus(GiftVoucherStatusEnum.CANCELLED);
				giftVoucherDao.changeState(giftVoucherNumber, GiftVoucherStatusEnum.CANCELLED);
			}
			else {
				throw new GiftVoucherAlreadyUsedException();
			}
		} catch (GiftVoucherNotFoundException e) {
			logger.info("No Such Gift Voucher Exists :  " + giftVoucherNumber);
			throw new GiftVoucherNotFoundException("No Such Gift Voucher Exists :  " + giftVoucherNumber, e);
		} catch (GiftVoucherAlreadyUsedException e) {
			logger.info("Gift Voucher has GV number : " + giftVoucherNumber + " has laready been used");
			throw new GiftVoucherAlreadyUsedException("Gift Voucher has GV number : " + giftVoucherNumber + " has laready been used", e);
		} catch (DataAccessException e) {
			logger.error("Error while loading the GiftVoucher. GiftVoucherId  : " + giftVoucherNumber);
			throw new PlatformException("Error while loading the GiftVoucher. GiftVoucherId  : " + giftVoucherNumber, e);
		}
	}
}
