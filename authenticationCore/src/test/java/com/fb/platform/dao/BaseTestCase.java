/**
 * 
 */
package com.fb.platform.dao;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

/**
 * @author vinayak
 *
 */
@ContextConfiguration(
		locations={"classpath:/applicationContext-resources.xml",
				"classpath:/applicationContext-dao.xml",
				"classpath:/applicationContext-service.xml",
				"classpath*:/applicationContext.xml",
				"classpath*:/applicationContext-service.xml",
				"classpath:**/applicationContext*.xml"})
public abstract class BaseTestCase extends AbstractTransactionalJUnit4SpringContextTests {

}
