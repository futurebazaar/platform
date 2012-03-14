/**
 * 
 */
package com.future.test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.fb.platform.promotion.dao.PromotionDao;

/**
 * @author Keith Fernandez
 *
 */
public class TestPromotions {

	public static void main(String [] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext-resources.xml", "applicationContext-dao.xml", "applicationContext-service.xml");
		
		PromotionDao promotionDao = (PromotionDao) context.getBean("promotionDao");
		promotionDao.get(10070);
		
	}
}
