package com.fb.platform.franchise.dao.interfaces;

import com.fb.platform.franchise.domain.FranchiseBO;



public interface IFranchiseDAO {
	
	FranchiseBO getFranchise(int franchiseID);
	FranchiseBO getFranchiseNetwork(int franchiseID);

	void addFranchise();
	boolean isFranchise(FranchiseBO franchiseBO);
}
