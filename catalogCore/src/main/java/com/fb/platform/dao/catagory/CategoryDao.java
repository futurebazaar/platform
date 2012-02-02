/**
 * 
 */
package com.fb.platform.dao.catagory;

import com.fb.platform.catagory.Category;

/**
 * @author vinayak
 *
 */
public interface CategoryDao {

	public Category get(Integer id);

	public void create(Category category);
}
