/**
 * 
 */
package com.fb.platform.egv.dao;

import java.math.BigDecimal;

import com.fb.platform.egv.model.GiftVoucher;
import com.fb.platform.egv.model.GiftVoucherStatusEnum;

/**
 * @author keith
 *
 */
public interface GiftVoucherDao {

	public GiftVoucher load(int giftVoucherId);
	
	public GiftVoucher load(long giftVoucherNumber,int giftVoucherPin);
	
	public boolean createGiftVoucher(final long gvNumber, final String pin,final String email, final int userId, final BigDecimal amount, final GiftVoucherStatusEnum status, final int orderItemId);

}
