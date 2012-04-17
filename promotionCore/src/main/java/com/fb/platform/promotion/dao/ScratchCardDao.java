/**
 * 
 */
package com.fb.platform.promotion.dao;

import java.util.List;

import com.fb.platform.promotion.model.scratchCard.ScratchCard;

/**
 * @author vinayak
 *
 */
public interface ScratchCardDao {

	public ScratchCard load(String cardNumber);

	public List<String> getCouponCodesForStore(String store);

	void commitUse(int id, int userId, String couponCode);
	
	public int getUserOrderCount(int userId);
}
