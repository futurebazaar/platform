package com.fb.platform.ifs.manager;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fb.platform.ifs.manager.model.SingleArticleServiceabilityRequestTO;
import com.fb.platform.ifs.manager.model.SingleArticleServiceabilityResponseTO;

/**
 * 
 * @author sarvesh
 */
@Transactional
public interface IFSManager  {

	@Transactional(propagation=Propagation.REQUIRED)
	SingleArticleServiceabilityResponseTO getSingleArticleServiceabilityInfo(SingleArticleServiceabilityRequestTO serviceabilityRequestTO);
	
}
