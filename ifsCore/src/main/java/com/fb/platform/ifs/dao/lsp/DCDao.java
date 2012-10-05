package com.fb.platform.ifs.dao.lsp;

import java.util.List;
import com.fb.platform.ifs.to.lsp.DeliveryCenter;

public interface DCDao {

	public List<DeliveryCenter> getAvailability(int sellerChartId, int quantity);
	
}
