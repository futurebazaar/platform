/**
 * 
 */
package com.fb.platform.egv.service;

import java.math.BigDecimal;

import org.springframework.transaction.annotation.Isolation;
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
	
	/**
	 * @param giftVoucherNumber
	 * @return
	 * @throws GiftVoucherNotFoundException
	 * @throws PlatformException
	 */
	public GiftVoucher getGiftVoucher(long giftVoucherNumber) throws GiftVoucherNotFoundException, PlatformException;
	
	/**
	 * @param giftVoucherNumber
	 * @param giftVoucherPin
	 * @return
	 */
	public GiftVoucher applyGiftVoucher(long giftVoucherNumber,String giftVoucherPin);
	
	/**
	 * @param email
	 * @param userId
	 * @param amount
	 * @param orderItemId
	 * @return
	 * @throws PlatformException
	 */
	@Transactional(propagation=Propagation.REQUIRED,isolation=Isolation.READ_COMMITTED)
	public GiftVoucher createGiftVoucher(String email, int userId, BigDecimal amount, int orderItemId, String senderName, String receiverName) throws PlatformException;

	/**
	 * @param userId
	 * @param amount
	 * @param orderId
	 * @param giftVoucherNumber
	 * @param giftVoucherPin
	 * @return
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public void useGiftVoucher(int userId, BigDecimal amount, int orderId,
			long giftVoucherNumber);

	/**
	 * @param giftVoucherNumber
	 * @param userId
	 * @param orderItemId
	 * @return
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public void cancelGiftVoucher(long giftVoucherNumber, int userId, int orderItemId);

}
