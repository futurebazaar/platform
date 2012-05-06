package com.fb.platform.payback.service.impl;

import java.math.BigDecimal;
import java.util.Properties;

import org.joda.time.DateTime;

import com.fb.platform.payback.dao.PointsDao;
import com.fb.platform.payback.model.PointsHeader;
import com.fb.platform.payback.service.PointsService;
import com.fb.platform.payback.to.BurnActionCodesEnum;
import com.fb.platform.payback.to.EarnActionCodesEnum;
import com.fb.platform.payback.to.PointsTxnClassificationCodeEnum;
import com.fb.platform.payback.util.PointsUtil;

public class PointsServiceImpl implements PointsService {
	
	private static String OFFER_TYPE_EARN_FACTOR = "EARN_FACTOR";
	private static String OFFER_TYPE_EARN_BONUS = "EARN_BONUS";
	private static String EARN_FACTOR_TYPE = "EARN_FACTOR";
	private static String BURN_FACTOR_TYPE = "BURN_FACTOR";
	
	private PointsUtil pointsUtil;
	private PointsDao pointsDao;
	
	public void setPointsDao(PointsDao pointsDao) {
		this.pointsDao = pointsDao;
	}

	public void setPointsUtil(PointsUtil pointsUtil){
		this.pointsUtil = pointsUtil;
	}
	
	@Override
	public BigDecimal getEarnRatio(String day, Properties props, String clientName, long orderId, 
			EarnActionCodesEnum txnActionCode){
		BigDecimal earnRatio = null;
		if (txnActionCode.equals(EarnActionCodesEnum.EARN_REVERSAL)){
			PointsTxnClassificationCodeEnum earnCode = PointsTxnClassificationCodeEnum.EARN_REVERSAL;
			String txnClassificationCode = earnCode.toString().split(",")[0];
			PointsHeader pointsHeader = pointsDao.getHeaderDetails(orderId, earnCode.name(), txnClassificationCode);
			if (pointsHeader != null){
				earnRatio = pointsHeader.getEarnRatio();
			}
		}
		if (earnRatio == null ){
			String earnPointsRatio = props.getProperty(clientName + "_EARN_POINTS");
			earnRatio = new BigDecimal(earnPointsRatio == null ? "1" : earnPointsRatio);
			if (isApplicableForOffer(day, props, clientName, OFFER_TYPE_EARN_FACTOR))
				return earnRatio.multiply(getFactor(clientName, props, day, EARN_FACTOR_TYPE));
		}
		return earnRatio;
	}
	
	@Override
	public BigDecimal getBurnRatio(String day, Properties props, String clientName){
		String burnPointsRatio = props.getProperty(clientName + "_BURN_POINTS");
		BigDecimal burnRatio = new BigDecimal(burnPointsRatio == null ? "1" : burnPointsRatio);
		if (isApplicableForOffer(day, props, clientName, BURN_FACTOR_TYPE)){
			return burnRatio.multiply(getFactor(clientName, props, day, BURN_FACTOR_TYPE));
		}
		return burnRatio;
		
	}
		
	@Override
	public int getBonusPoints(BigDecimal amount, String day, Properties props, String clientName,
			long orderId, String txnActionCode){
		PointsHeader pointsHeader = pointsDao.getHeaderDetails(orderId, "PREALLOC_EARN", "BONUS_POINTS");
		if (pointsHeader != null){
			if (txnActionCode.equals(EarnActionCodesEnum.EARN_REVERSAL.name())){
				return pointsHeader.getTxnPoints();	
			}
			return 0;
		}
		if (isApplicableForOffer(day, props, clientName, OFFER_TYPE_EARN_BONUS)){
			return calculateBonusPoints(amount, props, clientName);
		}
		return 0;
	}


	@Override
	public int getTxnPoints(BigDecimal amount, String day, Properties props, String clientName, 
			String txnCode, long orderId){
		BigDecimal ratio = BigDecimal.ZERO;
		if (txnCode.equals(EarnActionCodesEnum.PREALLOC_EARN.name()) || 
				txnCode.equals(EarnActionCodesEnum.EARN_REVERSAL.name())){
			ratio = getEarnRatio(day, props, clientName, orderId, EarnActionCodesEnum.valueOf(txnCode));
		}
		else if (txnCode.equals(BurnActionCodesEnum.BURN_REVERSAL.name())){
			ratio = getBurnRatio(day, props, clientName);
		}
		return amount.multiply(ratio).setScale(0, BigDecimal.ROUND_HALF_DOWN).intValue();
		
	}
	
	@Override
	public String getSequenceNumber() {
		DateTime datetime = new DateTime();
		int seconds = (datetime.getSecondOfDay()%999999) + 1;
		String sequenceNumber = String.valueOf(seconds);
		while(sequenceNumber.length() < 6){
			sequenceNumber = "0" + sequenceNumber;
		}
		return sequenceNumber;
	}
	
	private boolean isApplicableForOffer(String day, Properties props, String clientName, String offerType) {
		String validTill = props.getProperty(clientName + "_" + offerType + "_VALIDITY");
		if (pointsUtil.isValidDate(validTill)){
			if (!day.equalsIgnoreCase(getOfferDay(props, clientName)) && offerType.equals(OFFER_TYPE_EARN_BONUS)){
				return true;
			}
			else if(day.equalsIgnoreCase(getOfferDay(props, clientName)) && offerType.equals(OFFER_TYPE_EARN_FACTOR)){
				return true;
			}
			return false;
		}
		return false;
	}
	
	private String getOfferDay(Properties props, String clientName){
		String day = props.getProperty(clientName + "_OFFER_DAY");
		return day;
	}
	
	private int calculateBonusPoints(BigDecimal amount, Properties props, String clientName) {
		int bonusPoints = 0;
		BigDecimal minAmount = new BigDecimal(props.getProperty(clientName + "_EARN_BONUS_MIN_AMOUNT"));
		if (amount.compareTo(minAmount) == 1){
			String extraPoints = props.getProperty(clientName.toUpperCase() + "_EARN_BONUS_POINTS");
			return Integer.parseInt(extraPoints != null ? extraPoints : "0");
			}
		return bonusPoints;
	}
	
	private BigDecimal getFactor(String clientName, Properties props, String day, String factorType) {
		String factorValue = props.getProperty(clientName + "_" + factorType);
		BigDecimal factor = new BigDecimal(factorValue == null ? "0" : factorValue);
		return factor;
	}
	
}
