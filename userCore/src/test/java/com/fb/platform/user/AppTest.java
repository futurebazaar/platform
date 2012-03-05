package com.fb.platform.user;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.fb.platform.user.dao.interfaces.UserDao;
import com.fb.platform.user.domain.UserBo;
import com.fb.platform.user.manager.interfaces.UserManager;
import com.fb.platform.user.manager.model.UserTO;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class AppTest extends TestCase {
	
	private static final String key  = "jasvipul@gmail.com";
	private UserDao userDao;
	private UserManager userManager;
	protected static ApplicationContext appContext;


	/**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    public static Test suite()
    {
	    TestSuite suite = new TestSuite();
	    suite.addTest(new AppTest("testUserDAO"));
	    suite.addTest(new AppTest("testUserManager"));
	    return suite;
    }
    public void setUp() throws Exception
    {
    	userDao = (UserDao) getBean("userDao");
    	userManager = (UserManager) getBean("userManager");
    }
    public static void main(String[] args) {
    	junit.textui.TestRunner.run(suite());
    }

    protected Object getBean(String beanName) {
    	try{
    		
    		ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath*:**/applicationContext*.xml");
	    	return ctx.getBean(beanName);
    	}catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
			return null;
		}
    }
    
    public void testUserDAO()
    {
	    UserBo record = new UserBo();
	    record = userDao.load(key);
	    System.out.println("Test is running for user DAO ::::: " + record.getName());
	    System.out.println("Test is running for user DAO user email::::: " + record.getUserEmail().get(0).getEmail());
	    System.out.println("Test is running for user DAO user phone::::: " + record.getUserPhone().get(0).getPhoneno());
    }
    /*public void testUserManager()
    {
	    UserTO record = new UserTO();
	    record = userManager.getuser(key);
	    System.out.println("Test is running for user Manager ::::: " + record.getName());
	    System.out.println("Test is running for user Manager user email::::: " + record.getUserEmail().get(0).getEmail());
	    System.out.println("Test is running for user Manager user phone::::: " + record.getUserPhone().get(0).getPhoneno());
    }*/


}
