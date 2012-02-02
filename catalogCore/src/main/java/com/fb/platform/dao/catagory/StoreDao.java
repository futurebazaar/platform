/**
 * 
 */
package com.fb.platform.dao.catagory;

import com.fb.platform.catagory.Store;

/**
 * @author vinayak
 *
 */
public interface StoreDao {

	/**
	 * @param id Store ID
	 * @return the Store object corresponding to the ID.
	 */
	public Store get(Integer id);

	/**
	 * Creates a new Store in the DB.
	 * @param store the Store to create. Ignores the id if populated as id will be auto created.
	 * @return the newly created Store object with id populated.
	 */
	public Store create(Store store);

}
