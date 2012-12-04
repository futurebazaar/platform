package com.fb.platform.scheduler.service;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author anubhav
 *
 */
public interface OrderSchedulerService {

	@Transactional(propagation=Propagation.REQUIRED)
	public void postXmlsTOBapi();

}
