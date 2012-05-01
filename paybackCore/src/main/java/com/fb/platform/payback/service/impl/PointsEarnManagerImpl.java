package com.fb.platform.payback.service.impl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Properties;

import org.joda.time.DateTime;

import com.fb.commons.PlatformException;
import com.fb.commons.util.SFTPConnector;
import com.fb.platform.payback.model.PointsItems;
import com.fb.platform.payback.service.PointsEarnManager;
import com.fb.platform.payback.service.PointsEarnService;
import com.fb.platform.payback.service.PointsService;
import com.fb.platform.payback.util.EarnActionCodesEnum;
import com.fb.platform.payback.util.PartnerMerchantIdEnum;
import com.fb.platform.payback.util.PointsUtil;

public class PointsEarnManagerImpl implements PointsEarnManager{
	
	private PointsEarnService pointsEarnService;
	private PointsService pointsService;
	private PointsUtil pointsUtil;
	
	public void setPointsEarnService(PointsEarnService pointsEarnService){
		this.pointsEarnService = pointsEarnService;
	}
	
	public void setPointsService(PointsService pointsService){
		this.pointsService = pointsService;
	}
	
	public void setPointsUtil(PointsUtil pointsUtil){
		this.pointsUtil = pointsUtil;
	}

	@Override
	public int putEarnData() {
		for (EarnActionCodesEnum txnActionCode : EarnActionCodesEnum.values()){
			for (PartnerMerchantIdEnum partnerMerchantId : PartnerMerchantIdEnum.values()){
				String[] partnerIds = partnerMerchantId.toString().split(",");
				String merchantId = partnerIds[0];
				pointsEarnService.postEarnData(txnActionCode.name(), merchantId);
				pointsService.sendMail(txnActionCode.name(), merchantId);
				
			}
		}
		return 0;
	}
	
}
