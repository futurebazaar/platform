/**
 * 
 */
package com.fb.platform.caching;

/**
 * @author vinayak
 *
 */
public enum NamedCachesEnum {

	SSO_SESSION_CACHE("ssoSessionCache"),
	SESSION_CACHE("sessionCache"),
	CRYPTO_KEY_CACHE("cryptoKeyCache"),
	PROMOTION_CACHE("promotionCache"),
	COUPON_CACHE("couponCache"),
	PRODUCT_GROUP_CACHE("productGroupCache"),
	LSP_CACHE("lspCache"),
	POINTS_CACHE("pointsCache"),
	FULFILMENT_CACHE("fulfilmentCache");

	private String name = null;

	private NamedCachesEnum(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}

	public String getName() {
		return name;
	}
}
