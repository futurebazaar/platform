package com.fb.platform.payback.service.impl;

import java.io.IOException;
import java.util.Properties;

import com.fb.platform.payback.service.PointsEarnManager;
import com.fb.platform.payback.service.PointsEarnService;
import com.fb.platform.payback.util.EarnActionCodesEnum;
import com.fb.platform.payback.util.PointsUtil;

public class PointsEarnManagerImpl implements PointsEarnManager{
	
	private PointsEarnService pointsEarnService;
	private PointsUtil pointsUtil;
	
	public void setPointsEarnService(PointsEarnService pointsEarnService){
		this.pointsEarnService = pointsEarnService;
	}
	
	public void setPointsUtil(PointsUtil pointsUtil){
		this.pointsUtil = pointsUtil;
	}

	@Override
	public int putEarnDataOnSftp() {
		try {
			Properties props = pointsUtil.getProperties("points.properties");
			String[] clients =  props.getProperty("CLIENTS").split(",");
			for(String client : clients){
				String[] partnerIds = props.getProperty(client + "_IDS").split(",");
				String merchantId = partnerIds[0];
				for (EarnActionCodesEnum txnActionCode : EarnActionCodesEnum.values()){
					String dataToUpload = pointsEarnService.postEarnData(txnActionCode, merchantId);
					pointsUtil.sendMail(txnActionCode.name(), merchantId, txnActionCode.toString()+ ".txt", dataToUpload);
					
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return 0;
	}
	
	public void storeEarnReversalData(){
		EarnActionCodesEnum txnActionCode = EarnActionCodesEnum.EARN_REVERSAL;
			
	}
}
