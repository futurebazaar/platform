/**
 * 
 */
package com.fb.platform.egv.service;

import java.math.BigDecimal;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fb.commons.PlatformException;
import com.fb.platform.egv.model.GiftVoucher;

/**
 * @author keith
 *
 */
@Transactional
public interface GiftVoucherService {

	/**
	 * This functions loads the Gift Voucher with the given VoucherId
	 * 
	 * @param id of the gift voucher to be loaded
	 * @throws GiftVoucherNotFoundException if the Gift Voucher is not found
	 * @return The GiftVoucher Object if found
	 *  
	 */
	public GiftVoucher getGiftVoucher(int giftVoucherId) throws GiftVoucherNotFoundException, PlatformException;
	
	public GiftVoucher getGiftVoucher(long giftVoucherNumber) throws GiftVoucherNotFoundException, PlatformException;
	
	public GiftVoucher applyGiftVoucher(long giftVoucherNumber,String giftVoucherPin);
	
	@Transactional(propagation=Propagation.REQUIRED)
	public boolean createGiftVoucher(String email, int userId, BigDecimal amount,int orderItemId) throws PlatformException;

	@Transactional(propagation=Propagation.REQUIRED)
	public boolean useGiftVoucher(int userId, BigDecimal amount, int orderId,
			long giftVoucherNumber, String giftVoucherPin);

	@Transactional(propagation=Propagation.REQUIRED)
	public boolean cancelGiftVoucher(long giftVoucherNumber, int userId, int orderItemId);

}
