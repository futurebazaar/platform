/**
 * 
 */
package com.fb.platform.egv.dao;

import java.math.BigDecimal;

import org.joda.time.DateTime;

import com.fb.platform.egv.model.GiftVoucher;
import com.fb.platform.egv.model.GiftVoucherStatusEnum;
import com.fb.platform.egv.model.GiftVoucherUse;

/**
 * @author keith
 * 
 */
public interface GiftVoucherDao {

	public GiftVoucher load(int giftVoucherId);

	public GiftVoucher load(long giftVoucherNumber);

	public void changeState(long giftVoucherNumber, GiftVoucherStatusEnum newState);

	public boolean createGiftVoucher(final long gvNumber, final String pin, final String email, final int userId,
			final BigDecimal amount, final GiftVoucherStatusEnum status, final int orderItemId, final String mobile,
			final DateTime validFrom, final DateTime validTill);

	public boolean createGiftVoucherUse(final long gvNumber, final int userId, final int orderId,
			final BigDecimal amountUsed);

	public boolean deleteGiftVoucher(final long gvNumber, final int userId, final int orderItemId);

	public GiftVoucherUse loadUse(long giftVoucherNumber);

	public void deleteUse(long giftVoucherNumber, final int userId, final int orderId);

	public boolean updateGiftVoucher(final long gvNumber, final String pin, final String email, final int userId,
			final BigDecimal amount, final GiftVoucherStatusEnum status, final int orderItemId, final String mobile,
			final DateTime validFrom, final DateTime validTill);

}
