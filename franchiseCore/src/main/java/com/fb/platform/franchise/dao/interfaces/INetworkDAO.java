/**
 * 
 */
package com.fb.platform.franchise.dao.interfaces;

import java.util.Set;

import com.fb.platform.franchise.domain.NetworkBO;

/**
 * @author ashish
 *
 */
public interface INetworkDAO {

	NetworkBO getNetwork(String networkID);
	Set<NetworkBO> getNetworkByParentID(String parentNetworkID);
}
