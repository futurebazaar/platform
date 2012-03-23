/**
 * 
 */
package com.fb.platform.franchise.dao.interfaces;

import com.fb.platform.franchise.domain.NetworkBO;

/**
 * @author ashish
 *
 */
public interface IFranchiseOrderDAO {

	NetworkBO getNetwork(String networkID);
}
