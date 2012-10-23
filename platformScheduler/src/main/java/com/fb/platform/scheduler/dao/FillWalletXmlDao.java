package com.fb.platform.scheduler.dao;

import java.util.List;

import com.fb.platform.scheduler.to.FillWalletXmlTO;

public interface FillWalletXmlDao {
	
	public List<FillWalletXmlTO> getFillWalletXmlLst();
	public int updateFillWalletXml(long id,boolean status);

}
