/**
 * 
 */
package com.fb.platform.test.franchise.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.fb.platform.franchise.dao.interfaces.IFranchiseDAO;
import com.fb.platform.franchise.domain.FranchiseBO;
import com.fb.platform.test.franchise.BasedaoTestCase;

/**
 * @author ashish
 *
 */
public class FranchiseDaoTest extends BasedaoTestCase {

	@Autowired
	private IFranchiseDAO franchiseDAO;

	@Test
	public void testGetClient() {
		FranchiseBO franchise = franchiseDAO.getFranchise(-1);

		assertNotNull(franchise);
		assertEquals("test role", franchise.getRole());
		assertEquals("-1", franchise.getFranchiseID());
		assertEquals("-3", franchise.getNetworkID());
		assertEquals("-2", franchise.getUserID());
		assertEquals("12", franchise.isActive());
	}
}
