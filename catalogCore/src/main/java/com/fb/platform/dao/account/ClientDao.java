/**
 * 
 */
package com.fb.platform.dao.account;

import com.fb.platform.account.Client;

/**
 * @author vinayak
 *
 */
public interface ClientDao {

	public Client get(Integer id);

	public Client create(Client client);
}
