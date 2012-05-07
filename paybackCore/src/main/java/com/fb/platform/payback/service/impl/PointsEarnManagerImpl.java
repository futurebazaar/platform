package com.fb.platform.payback.service.impl;

import java.io.IOException;
import java.util.Properties;

import com.fb.platform.payback.service.PointsEarnManager;
import com.fb.platform.payback.service.PointsEarnService;
import com.fb.platform.payback.to.EarnActionCodesEnum;
import com.fb.platform.payback.to.StorePointsRequest;
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
					if (dataToUpload != null && !dataToUpload.equals("")){
						pointsUtil.sendMail(txnActionCode.name(), merchantId, txnActionCode.toString()+ ".txt", dataToUpload);
					}
					
				}
			}
			System.out.println("Successfully completed the Task on SFTP");
		} catch (Exception e) {
			e.printStackTrace();
		}

		return 0;
	}
	
	@Override
	public int storeEarnPoints(StorePointsRequest request, String actionCode){
		EarnActionCodesEnum txnActionCode = EarnActionCodesEnum.valueOf(actionCode);
		try {
			pointsEarnService.saveEarnData(txnActionCode, request);
			return 0;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return -1;
	}
}
