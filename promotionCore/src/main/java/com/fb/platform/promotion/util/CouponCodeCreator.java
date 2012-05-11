/**
 * 
 */
package com.fb.platform.promotion.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.fb.platform.promotion.admin.dao.CouponAdminDao;

/**
 * @author vinayak
 *
 */
public class CouponCodeCreator {

	//excludes 0, 1, I and O.
	private static final char [] chars = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789".toCharArray();

	@Autowired
	private CouponAdminDao couponAdminDao = null;

	private List<String> generatedCoupons = null;
	private int batchSize = 0;

	private int currentBatchNumber = 0;
	private int numberOfBatches = 0;

	public void init(int count, int length, String startsWith, String endsWith, int batchSize) {
		this.batchSize = batchSize;

		//generate the coupon codes
		generatedCoupons = create(count, length, startsWith, endsWith);
		List<String> existingCoupons = findExistingCouponCodes(generatedCoupons);

		removeExistingCodes(generatedCoupons, existingCoupons);

		while (existingCoupons.size() != 0) {
			List<String> newCodes = create(existingCoupons.size(), length, startsWith, endsWith);
			generatedCoupons.addAll(newCodes);
			existingCoupons = findExistingCouponCodes(newCodes);
			removeExistingCodes(generatedCoupons, existingCoupons);
		}

		numberOfBatches = generatedCoupons.size() / batchSize;
	}

	public boolean nextBatchAvailable() {
		if (currentBatchNumber <= numberOfBatches) {
			return true;
		}
		return false;
	}

	public List<String> getNextBatch() {
		int realBatchSize = 0;
		if (currentBatchNumber == numberOfBatches) {
			realBatchSize = generatedCoupons.size() % batchSize;
		} else {
			realBatchSize = batchSize;
		}
		int startRecord = currentBatchNumber * batchSize;
		int endRecord = currentBatchNumber * batchSize + realBatchSize;

		List<String> nextCoupons = generatedCoupons.subList(startRecord, endRecord);
		currentBatchNumber++;
		return nextCoupons;
	}

	public List<String> getGeneratedCoupons() {
		return this.generatedCoupons;
	}

	private List<String> create(int count, int length, String startsWith, String endsWith) {

		List<String> coupons = new ArrayList<String>();
		Random random = new Random();

		int effectiveLength = length;
		if (!StringUtils.isBlank(startsWith)) {
			effectiveLength = effectiveLength - startsWith.length();
		}

		if (!StringUtils.isBlank(endsWith)) {
			effectiveLength = effectiveLength - endsWith.length();
		}

		for (int i = 0; i < count; i++) {
			String coupon = createSingleCoupon(effectiveLength, random, startsWith, endsWith);
			if (!coupons.contains(coupon)) {
				coupons.add(coupon);
			} else {
				i--;
			}
		}

		return coupons;
	}

	private String createSingleCoupon(int length, Random random, String startsWith, String endsWith) {
		StringBuilder sb = new StringBuilder();

		if (!StringUtils.isBlank(startsWith)) {
			sb.append(startsWith);
		}

		for (int i = 0; i < length; i++) {
		    char c = chars[random.nextInt(chars.length)];
		    sb.append(c);
		}

		if (!StringUtils.isBlank(endsWith)) {
			sb.append(endsWith);
		}

		String couponCode = sb.toString();
		return couponCode;
	}

	private void removeExistingCodes(List<String> generatedCodes, List<String> existingCodes) {
		generatedCodes.removeAll(existingCodes);
	}

	private List<String> findExistingCouponCodes(List<String> couponCodes) {
		return couponAdminDao.findExistingCodes(couponCodes);
	}

	public void setCouponAdminDao(CouponAdminDao couponAdminDao) {
		this.couponAdminDao = couponAdminDao;
	}
}
