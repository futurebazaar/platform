/**
 * 
 */
package platform.fb.com.migration.model;

/**
 * @author keith
 *
 */
public class OldPromoCoupon {

	private String couponCode;
	private String clientId;
	private int promotionId;
	public String getCouponCode() {
		return couponCode;
	}
	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	public int getPromotionId() {
		return promotionId;
	}
	public void setPromotionId(int promotionId) {
		this.promotionId = promotionId;
	}
	
}
