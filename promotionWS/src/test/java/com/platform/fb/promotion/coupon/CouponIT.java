/**
 * 
 */
package com.platform.fb.promotion.coupon;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.math.BigDecimal;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.lang.math.RandomUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.fb.commons.test.BaseTestCase;
import com.fb.platform.promotion._1_0.ApplyCouponRequest;
import com.fb.platform.promotion._1_0.ApplyCouponResponse;
import com.fb.platform.promotion._1_0.CouponStatus;
import com.fb.platform.promotion._1_0.OrderItem;
import com.fb.platform.promotion._1_0.OrderRequest;
import com.fb.platform.promotion._1_0.Product;
import com.fb.platform.user.manager.interfaces.UserManager;
import com.fb.platform.user.manager.model.auth.LoginRequest;
import com.fb.platform.user.manager.model.auth.LoginResponse;

/**
 * @author vinayak
 *
 */
public class CouponIT extends BaseTestCase {

	private static final String HOSTNAME = "http://localhost:9090";
	@Autowired
	private UserManager userManager = null;

	private String sessionToken = null;
	int orderId = 0;

	@Before
	public void setup() {
		LoginRequest request = new LoginRequest();
		request.setUsername("jasvipul@gmail.com");
		request.setPassword("testpass");

		LoginResponse response = userManager.login(request);
		sessionToken = response.getSessionToken();
		orderId = RandomUtils.nextInt();
	}

	@Test
	public void applyCouponSuccess() throws JAXBException, HttpException, IOException {
		/*HttpClient httpClient = new HttpClient();

		PostMethod applyPromotionMethod = new PostMethod(HOSTNAME + "/promotionWeb/coupon/apply");

		CouponRequest couponRequest = new CouponRequest();
		couponRequest.setClientId(10);
		couponRequest.setCouponCode("GlobalCoupon1000Off");
		couponRequest.setSessionToken(sessionToken);

		OrderRequest orderRequest = createSampleOrderRequest(orderId);
		couponRequest.setOrderRequest(orderRequest);

		JAXBContext context = JAXBContext.newInstance("com.fb.platform.promotion._1_0");

		Marshaller marshaller = context.createMarshaller();
		StringWriter sw = new StringWriter();
		marshaller.marshal(couponRequest, sw);

		StringRequestEntity requestEntity = new StringRequestEntity(sw.toString());
		applyPromotionMethod.setRequestEntity(requestEntity);

		int statusCode = httpClient.executeMethod(applyPromotionMethod);
		assertEquals(HttpStatus.SC_OK, statusCode);

		String applyPromotionResponseStr = applyPromotionMethod.getResponseBodyAsString();
		Unmarshaller unmarshaller = context.createUnmarshaller();
		CouponResponse couponResponse = (CouponResponse) unmarshaller.unmarshal(new StreamSource(new StringReader(applyPromotionResponseStr)));

		assertNotNull(couponResponse);
		assertEquals(CouponStatus.SUCCESS, couponResponse.getCouponStatus());
		assertEquals(new BigDecimal("300.00"), couponResponse.getDiscountValue());*/
	}
	
	private OrderRequest createSampleOrderRequest(int orderId) {
		OrderRequest orderRequest = new OrderRequest();
		orderRequest.setOrderId(orderId);
		OrderItem orderItem = new OrderItem();
		orderItem.setQuantity(2);

		Product product = new Product();
		product.setPrice(new BigDecimal("2000"));
		product.setProductId(20000);
		product.getCategory().add(2000);
		product.getBrand().add(2000);

		orderItem.setProduct(product);

		orderRequest.getOrderItem().add(orderItem);
		return orderRequest;
	}

}
