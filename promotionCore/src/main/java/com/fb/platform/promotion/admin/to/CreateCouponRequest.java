/**
 * 
 */
package com.fb.platform.promotion.admin.to;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang.StringUtils;

import com.fb.platform.promotion.model.coupon.CouponType;
import com.fb.platform.promotion.to.AlphaNumericType;
import com.fb.platform.promotion.to.AlphabetCase;

/**
 * @author vinayak
 *
 */
public class CreateCouponRequest implements Serializable {

	//the max number of coupons that can be created in one go
	private static final int MAX_COUNT = 10000;

	private String sessionToken = null;

	//how many coupons to create
	private int count;
	//length of the coupon code string
	private int length;

	private CouponType type;

	//do all the couponCodes start with a specific string?
	private String startsWith;
	//do all the couponCodes end with a specific string?
	private String endsWith;

	private AlphabetCase alphabetCase;
	private AlphaNumericType alphaNumericType;
	
	private int promotionId;

	private int maxUses = 0;
	private BigDecimal maxAmount = null;
	private int maxUsesPerUser = 0;
	private BigDecimal maxAmountPerUser = null;

	/**
	 * Validates the request. 
	 * @return response with corresponding error status correctly populated if request is invalid. Null if request is valid.
	 */
	public CreateCouponResponse validate() {
		CreateCouponResponse response = new CreateCouponResponse();
		response.setSessionToken(getSessionToken());
		
		if (StringUtils.isBlank(sessionToken)) {
			response.setStatus(CreateCouponStatusEnum.NO_SESSION);
			return response;
		}

		if (count <= 0) {
			response.setStatus(CreateCouponStatusEnum.INVALID_COUNT);
			return response;
		}
		if (count > MAX_COUNT) {
			response.setStatus(CreateCouponStatusEnum.MAX_COUNT_EXCEEDED);
			return response;
		}

		if (promotionId == 0) {
			response.setStatus(CreateCouponStatusEnum.INVALID_PROMOTION);
			return response; 
		}

		// the length is not allowed to be more than 25 char and less than 5 char
		if (length <= 4 || length > 25) {
			response.setStatus(CreateCouponStatusEnum.INVALID_LENGTH);
			return response;
		}
		
		int startsWithLength = 0;
		if (!StringUtils.isBlank(startsWith)) {
			startsWithLength = startsWith.length();
		}
		int endsWithLength = 0;
		if (!StringUtils.isBlank(endsWith)) {
			endsWithLength = endsWith.length();
		}
		if (length < startsWithLength + endsWithLength) {
			response.setStatus(CreateCouponStatusEnum.INVALID_LENGTH);
			return response;
		}

		return null;
	}

	public String getSessionToken() {
		return sessionToken;
	}
	public void setSessionToken(String sessionToken) {
		this.sessionToken = sessionToken;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public String getStartsWith() {
		return startsWith;
	}
	public void setStartsWith(String startsWith) {
		this.startsWith = startsWith;
	}
	public String getEndsWith() {
		return endsWith;
	}
	public void setEndsWith(String endsWith) {
		this.endsWith = endsWith;
	}
	public int getPromotionId() {
		return promotionId;
	}
	public void setPromotionId(int promotionId) {
		this.promotionId = promotionId;
	}
	public int getMaxUses() {
		return maxUses;
	}
	public void setMaxUses(int maxUses) {
		this.maxUses = maxUses;
	}
	public int getMaxUsesPerUser() {
		return maxUsesPerUser;
	}
	public void setMaxUsesPerUser(int maxUsesPerUser) {
		this.maxUsesPerUser = maxUsesPerUser;
	}
	public CouponType getType() {
		return type;
	}
	public void setType(CouponType type) {
		this.type = type;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}
	public BigDecimal getMaxAmount() {
		return maxAmount;
	}
	public void setMaxAmount(BigDecimal maxAmount) {
		this.maxAmount = maxAmount;
	}
	public BigDecimal getMaxAmountPerUser() {
		return maxAmountPerUser;
	}
	public void setMaxAmountPerUser(BigDecimal maxAmountPerUser) {
		this.maxAmountPerUser = maxAmountPerUser;
	}

	public AlphabetCase getAlphabetCase() {
		return alphabetCase;
	}

	public void setAlphabetCase(AlphabetCase alphabetCase) {
		this.alphabetCase = alphabetCase;
	}

	public AlphaNumericType getAlphaNumericType() {
		return alphaNumericType;
	}

	public void setAlphaNumericType(AlphaNumericType alphaNumericType) {
		this.alphaNumericType = alphaNumericType;
	}
}
