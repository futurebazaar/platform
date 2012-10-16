/**
 * 
 */
package com.fb.platform.promotion.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fb.platform.promotion.admin.dao.CouponAdminDao;
import com.fb.platform.promotion.exception.CouponCodeGenerationException;
import com.fb.platform.promotion.to.AlphaNumericType;
import com.fb.platform.promotion.to.AlphabetCase;
import com.fb.platform.promotion.to.CouponCodeCharEnum;

/**
 * @author vinayak
 *
 */
public class CouponCodeCreator {

	private Log log = LogFactory.getLog(CouponCodeCreator.class);
	
	private char [] chars;

	private CouponAdminDao couponAdminDao = null;

	private List<String> generatedCoupons = null;
	private int batchSize = 0;

	private int currentBatchNumber = 0;
	private int numberOfBatches = 0;

	public void init(int count, int length, String startsWith, String endsWith, int batchSize, AlphabetCase alphabetCase, AlphaNumericType alphaNumericType) {
		this.batchSize = batchSize;

		// this API can throw InvalidAlphaNumericTypeException exception
		chars = CouponCodeCharEnum.from(alphaNumericType, alphabetCase);
		//generate the coupon codes
		generatedCoupons = create(count, length, startsWith, endsWith);
		List<String> existingCoupons = findExistingCouponCodes(generatedCoupons);

		removeExistingCodes(generatedCoupons, existingCoupons);

		/*
		 * Theoretically the loop may run infinitely so putting a number to come out of the loop
		 * As the max number of coupon to be created is 10000 and batch size is 1000, ideally the
		 * the maximum number of of times the loop should run is 10 (ideally!!).
		 * So putting a random number say 10 times 
		 */
		int i = 0;
		while (existingCoupons.size() != 0 && i < 10) {
			i++;
			List<String> newCodes = create(existingCoupons.size(), length, startsWith, endsWith);
			generatedCoupons.addAll(newCodes);
			existingCoupons = findExistingCouponCodes(newCodes);
			removeExistingCodes(generatedCoupons, existingCoupons);
		}

		if(generatedCoupons.size() != count){
			log.error("Could not generate requested number of coupon codes - requested number of counpons = "+ count);
			throw new CouponCodeGenerationException("Could not generate requested number of coupon codes - requested number of counpons = "+ count);
		}
		numberOfBatches = generatedCoupons.size() / batchSize;
	}

	public boolean nextBatchAvailable() {
		if (currentBatchNumber < numberOfBatches) {
			return true;
		}
		if (currentBatchNumber == numberOfBatches) {
			if ((generatedCoupons.size() % batchSize) > 0) {
				return true;
			}
			return false;
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
		if (StringUtils.isNotBlank(startsWith)) {
			effectiveLength = effectiveLength - startsWith.length();
		}

		if (StringUtils.isNotBlank(endsWith)) {
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

		if (StringUtils.isNotBlank(startsWith)) {
			sb.append(startsWith);
		}

		for (int i = 0; i < length; i++) {
		    char c = chars[random.nextInt(chars.length)];
		    sb.append(c);
		}

		if (StringUtils.isNotBlank(endsWith)) {
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
