/**
 * 
 */
package com.fb.platform.ifs.dao.product.impl;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.fb.platform.ifs.dao.product.IFSProductDao;
import com.fb.platform.ifs.to.product.IfsProductRestrictionsTO;
import com.fb.platform.ifs.to.product.ProductVolumeTO;
import com.fb.platform.ifs.to.product.VolumetricWeightTO;

/**
 * @author vinayak
 *
 */
public class IFSProductDaoJdbcImpl implements IFSProductDao {

	private JdbcTemplate jdbcTemplate;

	private Log log = LogFactory.getLog(IFSProductDaoJdbcImpl.class);

	/*
	 * SELECT id, product_id, actual_weight, length, height, width FROM product_weight_volume WHERE product_id = ?
	 */
	private static final String LOAD_PRODUCT_VOLUME_WEIGHT_QUERY = 
			"SELECT " +
			"	id, " +
			"	product_id, " +
			"	actual_weight, " +
			"	length, " +
			"	height, " +
			"	width " +
			"FROM product_weight_volume " +
			"WHERE product_id = ?";

	/*
	 * SELECT id, product_id, shipping_mode, lsp FROM ifs_product_restrictions WHERE product_id = ?
	 */
	private static final String LOAD_IFS_PRODUCT_RESTRICTIONS_QUERY = 
			"SELECT " +
			"	id, " +
			"	product_id, " +
			"	shipping_mode, " +
			"	lsp " +
			"FROM ifs_product_restrictions " +
			"WHERE product_id = ?";

	/* (non-Javadoc)
	 * @see com.fb.platform.ifs.dao.product.IFSProductDao#getVolumetricWeight(long)
	 */
	@Override
	public VolumetricWeightTO getVolumetricWeight(long productId) {
		VolumetricWeightTO volumetricWeight = jdbcTemplate.queryForObject(LOAD_PRODUCT_VOLUME_WEIGHT_QUERY, new ProductVolumeWeightRowMapper(), productId);
		return volumetricWeight;
	}

	@Override
	public IfsProductRestrictionsTO getRestrictions(long productId) {
		IfsProductRestrictionsTO restrictions = jdbcTemplate.queryForObject(LOAD_IFS_PRODUCT_RESTRICTIONS_QUERY, new RestrictionsRowMapper(), productId);
		return restrictions;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	private static class ProductVolumeWeightRowMapper implements RowMapper<VolumetricWeightTO> {

		@Override
		public VolumetricWeightTO mapRow(ResultSet rs, int rowNum) throws SQLException {
			VolumetricWeightTO volumeWeight = new VolumetricWeightTO();

			volumeWeight.setProductId(rs.getLong("product_id"));
			BigDecimal actualWeight = rs.getBigDecimal("actual_weight");
			if (actualWeight != null) {
				volumeWeight.setWeight(actualWeight);
			} else {
				volumeWeight.setWeight(BigDecimal.ZERO);
			}

			ProductVolumeTO volume = new ProductVolumeTO();
			BigDecimal height = rs.getBigDecimal("height");
			if (height != null) {
				volume.setHeight(height);
			} else {
				volume.setHeight(BigDecimal.ZERO);
			}

			BigDecimal length = rs.getBigDecimal("length");
			if (length != null) {
				volume.setLength(length);
			} else {
				volume.setLength(BigDecimal.ZERO);
			}

			BigDecimal width = rs.getBigDecimal("width");
			if (width != null) {
				volume.setWidth(width);
			} else {
				volume.setWidth(BigDecimal.ZERO);
			}

			volumeWeight.setVolume(volume);
			return volumeWeight;
		}
	}

	private static class RestrictionsRowMapper implements RowMapper<IfsProductRestrictionsTO> {

		@Override
		public IfsProductRestrictionsTO mapRow(ResultSet rs, int rowNum) throws SQLException {
			IfsProductRestrictionsTO restrictions = new IfsProductRestrictionsTO();

			restrictions.setPreferredLsp(rs.getString("lsp"));
			restrictions.setProductId(rs.getLong("product_id"));
			restrictions.setShippingMode(rs.getString("shipping_mode"));

			return restrictions;
		}
	}
}
