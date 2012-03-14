package com.fb.platform.promotion.model;

public class PromotionLimit {

 private int id ;
 private int maxTotalUsagesAllowed;
 private int noOfUsesTillNow;
 private int maxPerUser;
 private long maxBudget;
 private long valueClaimedTillNow;
 private int viaChannel;
 private int paymentModeToBeUsed;
 private int promoId;
public int getId() {
	return id;
}
public void setId(int id) {
	this.id = id;
}
public int getNoOfUsesTillNow() {
	return noOfUsesTillNow;
}
public void setNoOfUsesTillNow(int noOfUsesTillNow) {
	this.noOfUsesTillNow = noOfUsesTillNow;
}
public int getMaxPerUser() {
	return maxPerUser;
}
public void setMaxPerUser(int maxPerUser) {
	this.maxPerUser = maxPerUser;
}
public int getMaxTotalUsagesAllowed() {
	return maxTotalUsagesAllowed;
}
public void setMaxTotalUsagesAllowed(int maxTotalUsagesAllowed) {
	this.maxTotalUsagesAllowed = maxTotalUsagesAllowed;
}
public long getMaxBudget() {
	return maxBudget;
}
public void setMaxBudget(long maxBudget) {
	this.maxBudget = maxBudget;
}
public long getValueClaimedTillNow() {
	return valueClaimedTillNow;
}
public void setValueClaimedTillNow(long valueClaimedTillNow) {
	this.valueClaimedTillNow = valueClaimedTillNow;
}
public int getViaChannel() {
	return viaChannel;
}
public void setViaChannel(int viaChannel) {
	this.viaChannel = viaChannel;
}
public int getPaymentModeToBeUsed() {
	return paymentModeToBeUsed;
}
public void setPaymentModeToBeUsed(int paymentModeToBeUsed) {
	this.paymentModeToBeUsed = paymentModeToBeUsed;
}
public int getPromoId() {
	return promoId;
}
public void setPromoId(int promoId) {
	this.promoId = promoId;
}

	
}
