/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fb.platform.ifs.dao;

/**
 *
 * @author sarvesh
 */
import java.util.List;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fb.platform.ifs.domain.DCBo;
import com.fb.platform.ifs.domain.LSPBo;
import com.fb.platform.ifs.domain.LspDcBo;
import com.fb.platform.ifs.domain.ProductBo;
import com.fb.platform.ifs.domain.SingleArticleServiceabilityRequestBo;

@Transactional
public interface IFSDao {
	
	@Transactional(propagation=Propagation.REQUIRED)
	public ProductBo getProductGroup(String articleId, String client, double itemPrice);
	
	@Transactional(propagation=Propagation.REQUIRED)
	public List<DCBo> getDcList(ProductBo productBo, SingleArticleServiceabilityRequestBo serviceabilityRequestBo);

	@Transactional(propagation=Propagation.REQUIRED)
	public List<LspDcBo> getLSPDCList(List<DCBo> dcBoList, List<LSPBo> lspBoList, ProductBo productBo, SingleArticleServiceabilityRequestBo serviceabilityRequestBo);

	@Transactional(propagation=Propagation.REQUIRED)
	public List<LSPBo> getLSPList(ProductBo productBo, SingleArticleServiceabilityRequestBo serviceabilityRequestBo);
	
	@Transactional(propagation=Propagation.REQUIRED)
	public List<LspDcBo> getLspDcBoForThirdPartyProduct(List<DCBo> dcBoList, ProductBo productBo,
			SingleArticleServiceabilityRequestBo serviceabilityRequestBo);

}