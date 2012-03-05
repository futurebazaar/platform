/**
 * 
 */
package com.fb.commons.test;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

/**
 * The base class for JUnit test cases in Platform. 
 * This class will initialise the spring contexts required for all test classes.
 * @author vinayak
 *
 */
@ContextConfiguration(
		locations={"classpath:/applicationContext-resources.xml",
				"classpath:/applicationContext-dao.xml",
				"classpath:/applicationContext-service.xml",
				"classpath*:/applicationContext.xml",
				"classpath*:/applicationContext-service.xml",
				"classpath*:/applicationContext-dao.xml",
				"classpath:**/applicationContext*.xml"})
public abstract class BaseTestCase extends AbstractTransactionalJUnit4SpringContextTests {

}
