package com.test;
import java.math.BigDecimal;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.fb.platform.payback.service.impl.PointsEarnManagerImpl;
import com.fb.platform.payback.service.impl.PointsManagerImpl;
import com.fb.platform.payback.to.EarnActionCodesEnum;
import com.fb.platform.payback.to.StorePointsHeaderRequest;
import com.fb.platform.payback.to.StorePointsItemRequest;
import com.fb.platform.payback.to.StorePointsResponse;
import com.fb.platform.payback.util.PointsUtil;



public class TestPayback {
	
	private static String host;
	private static int port = 22;
	private static String username;
	private static String password;
	private static String remoteDirectory;
	
	public static void main(String[] args) {
		host = "10.40.15.119";
		port = 22;
		username = "futureba";
		password = "17d@Gil+2";
		remoteDirectory = "/futureba/in";
		
		String settlementDate = "2012-04-16";
		String txnActionCode = "EARN_REVERSAL";
		
		String[] serviceResources = {"applicationContext-dao.xml", "applicationContext-service.xml", 
				"applicationContext-resources.xml"};	

		ApplicationContext orderServiceContext = new ClassPathXmlApplicationContext(serviceResources);
		Object earnManager = orderServiceContext.getBean("pointsEarnManager");
		//((PointsEarnManagerImpl) manager).storeEarnData();
		//int manager1 = ((PointsManagerImpl) manager).mailBurnData();
		//int earnManager = ((PointsEarnManagerImpl) manager).putEarnDataOnSftp();
		
		
		Object burnManager = orderServiceContext.getBean("pointsBurnManager");
		//((PointsBurnManagerImpl) burnManager).mailBurnData();
		// SFTP put
		/*try {
			upload("/home/anubhav/Documents/a.txt");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		//sendMail();
		PointsUtil pointsUtil = new PointsUtil();
		//pointsUtil.sendMail("EARN", "1234", "anubhav", "anubhav");
		
		/*BigDecimal txnPoints = (new BigDecimal(10.6)).setScale(0, BigDecimal.ROUND_HALF_DOWN);
		System.out.println(txnPoints.setScale(2));
		
		DateTimeFormatter format = DateTimeFormat.forPattern("ddMMyyyy");
		System.out.println(format.print(DateTime.now()));*/
		//String ab = "1234";
		//System.out.println(ab.substring(0, 29));
		/*StringTokenizer factorIterator = new StringTokenizer("1234", ",");
		int earnFactor = Integer.parseInt(factorIterator.nextToken());
		System.out.println(earnFactor);
		
		
		System.out.println(DateTime.now());
		System.out.println(DateTime.now().isAfter(validTillDate));
		System.out.println(validTillDate.toDate().before(DateTime.now().toDate()));
		System.out.println(new BigDecimal(3).compareTo(new BigDecimal(2.25)));
		
		
		InputStream inStream = TestPayback.class.getClassLoader().getResourceAsStream("payback.properties");
		Properties props = new Properties();
		System.out.println(props.getProperty("sftpUsername"));
		
		File files = new File("/");
		if (files.isDirectory()){
			String[] file = files.list();
			System.out.println(file[0]);
		}*/
		
		//DateTimeFormatter format = DateTimeFormat.forPattern("yyyyMMdd");
		//DateTime validTillDate = format.parseDateTime(null);
		//System.out.println(validTillDate);
		
		EarnActionCodesEnum e = EarnActionCodesEnum.valueOf("PREALLOC_EARN");
		System.out.println(e.name() + e.toString());
		System.out.println(new BigDecimal("0.03"));
		StorePointsHeaderRequest storePointsHeaderRequest = new StorePointsHeaderRequest();
		storePointsHeaderRequest.setAmount(new BigDecimal(3000));
		storePointsHeaderRequest.setLoyaltyCard("1234567812345678");
		storePointsHeaderRequest.setOrderId(1245150);
		storePointsHeaderRequest.setTxnActionCode("PREALLOC_EARN");
		storePointsHeaderRequest.setReason("");
		
		StorePointsItemRequest storePointsItemRequest = new StorePointsItemRequest();
		storePointsItemRequest.setAmount(BigDecimal.TEN);
		storePointsItemRequest.setId(12345);
		storePointsItemRequest.setArticleId(12345);
		storePointsItemRequest.setQuantity(1);
		storePointsItemRequest.setDepartmentCode(1234);
		storePointsItemRequest.setDepartmentName("TEST");
		storePointsHeaderRequest.getStorePointsItemRequest().add(storePointsItemRequest);
		
		/*for (OrderItem xmlOrderItem : xmlStorePointsRequest.getOrderItem()) {
			com.fb.platform.payback.to.StorePointsItemRequest storePointsItemRequest = createStorePointsItem(xmlOrderItem);
			storePointsHeaderRequest.getStorePointsItemRequest().add(storePointsItemRequest);
		}*/
		Object manager = orderServiceContext.getBean("pointsManager");
		StorePointsResponse storePointsResponse = ((PointsManagerImpl) manager).getPointsReponse(storePointsHeaderRequest);
		System.out.println(storePointsResponse.getStorePointsResponseCodeEnum().name());
		System.out.println(storePointsResponse.getActionCode());
		System.out.println(storePointsResponse.getStorePointsResponseCodeEnum().toString());

		
	}
	
}