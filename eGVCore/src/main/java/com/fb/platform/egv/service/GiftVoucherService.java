/**
 * 
 */
package com.fb.platform.egv.service;

import java.math.BigDecimal;
import java.util.HashMap;

import org.joda.time.DateTime;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fb.commons.PlatformException;
import com.fb.platform.egv.exception.GiftVoucherNotFoundException;
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
	 * @param id
	 *            of the gift voucher to be loaded
	 * @throws GiftVoucherNotFoundException
	 *             if the Gift Voucher is not found
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

	public GiftVoucher applyGiftVoucher(long giftVoucherNumber, String giftVoucherPin);

	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	public GiftVoucher createGiftVoucher(String email, int userId, BigDecimal amount, int orderItemId,
			String senderName, String receiverName, String message, String mobile, DateTime validFrom,
			DateTime validTill, boolean active) throws PlatformException;

	@Transactional(propagation = Propagation.REQUIRED)
	public void useGiftVoucher(int userId, HashMap<Long, BigDecimal> gvDetails, int orderId);

	@Transactional(propagation = Propagation.REQUIRED)
	public void cancelGiftVoucher(long giftVoucherNumber, int userId, int orderItemId);

	@Transactional(propagation = Propagation.REQUIRED)
	public void rollbackUseGiftVoucher(int userId, int orderId, long giftVoucherNumber);

	@Transactional(propagation = Propagation.REQUIRED)
	public GiftVoucher activateGiftVoucher(long giftVoucherNumber, DateTime validFrom, DateTime validTill,
			BigDecimal amount);

	@Transactional(propagation = Propagation.REQUIRED)
	public void sendGiftVoucherPin(long giftVoucherNumber, String email, String mobile, String senderName,
			String receiverName, String giftMessage);

	@Transactional(propagation = Propagation.REQUIRED)
	public String getGiftVoucherPin(long giftVoucherNumber, int userId);

}
