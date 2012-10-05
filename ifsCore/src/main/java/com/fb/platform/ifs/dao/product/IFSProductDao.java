/**
 * 
 */
package com.fb.platform.ifs.dao.product;

import com.fb.platform.ifs.to.product.IfsProductRestrictionsTO;
import com.fb.platform.ifs.to.product.VolumetricWeightTO;

/**
 * @author vinayak
 *
 */
public interface IFSProductDao {

	public VolumetricWeightTO getVolumetricWeight(long productId);

	public IfsProductRestrictionsTO getRestrictions(long productId);

}
