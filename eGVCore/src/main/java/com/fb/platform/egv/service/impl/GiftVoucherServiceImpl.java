/**
 * 
 */
package com.fb.platform.egv.service.impl;

import java.math.BigDecimal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;

import com.fb.commons.PlatformException;
import com.fb.platform.egv.dao.GiftVoucherDao;
import com.fb.platform.egv.model.GiftVoucher;
import com.fb.platform.egv.model.GiftVoucherStatusEnum;
import com.fb.platform.egv.service.GiftVoucherNotFoundException;
import com.fb.platform.egv.service.GiftVoucherService;
import com.fb.platform.egv.service.InvalidPinException;
import com.fb.platform.egv.util.RandomGenerator;

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

	public void setGiftVoucherDao(GiftVoucherDao giftVoucherDao) {
		this.giftVoucherDao = giftVoucherDao;
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
	public GiftVoucher getGiftVoucher(long giftVoucherNumber,int giftVoucherPin) throws GiftVoucherNotFoundException, PlatformException {
		GiftVoucher giftVoucher;
		
		try {
			giftVoucher = giftVoucherDao.load(giftVoucherNumber,giftVoucherPin);
		} 
//		catch (InvalidPinException e) {
//			throw new PlatformException("Error while loading the GiftVoucher. GiftVoucherId  : " + giftVoucherNumber, e);
//		} 
		catch (DataAccessException e) {
			throw new PlatformException("Error while loading the GiftVoucher. GiftVoucherId  : " + giftVoucherNumber, e);
		}
		catch (GiftVoucherNotFoundException e) {
			throw new GiftVoucherNotFoundException("No Such Gift Voucher Exists :  " + giftVoucherNumber, e);
		}
		
		return giftVoucher;
	}
	
	@Override
	public boolean createGiftVoucher(String email, int userId,
			BigDecimal amount, int orderItemId) throws PlatformException {
		String numGenerated = RandomGenerator.integerRandomGenerator(GV_NUMBER_LENGTH);
		long gvNumber = Long.parseLong(numGenerated);
		String gvPin = RandomGenerator.integerRandomGenerator(GV_PIN_LENGTH);
		return giftVoucherDao.createGiftVoucher(gvNumber,gvPin,email,userId,amount,GiftVoucherStatusEnum.CONFIRMED,orderItemId);
	}
}