package com.linkage.module.itms.resource.dao;


import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.module.gwms.dao.SuperDAO;

public class GwVendorModelVersionDAO extends SuperDAO{

	// 日志记录
	private static Logger m_logger = LoggerFactory
			.getLogger(GwVendorModelVersionDAO.class);

	/**
	 * @category getVendor 获取所有的厂商
	 * 
	 * @param city_id
	 * 
	 * @return List
	 */
	public List getVendor() {
		m_logger.debug("getVendor()");
		PrepareSQL pSQL = new PrepareSQL(
				"select vendor_id,vendor_name, vendor_add from tab_vendor order by vendor_name");
		return jt.queryForList(pSQL.getSQL());
	}

	/**
	 * @category getDevicetype 获取所有的设备型号
	 * 
	 * @param vendorId
	 * 
	 * @return List
	 */
	public List getDeviceModel(String vendorId) {
		m_logger.debug("getDeviceModel(vendorId:{})", vendorId);
		PrepareSQL pSQL = new PrepareSQL();
		pSQL.append(" select a.device_model_id,a.device_model from gw_device_model a where 1=1 ");
		if (null != vendorId && !"".equals(vendorId) && !"-1".equals(vendorId)) {
			pSQL.append(" and a.vendor_id='");
			pSQL.append(vendorId);
			pSQL.append("'");
		}
		pSQL.append(" order by a.device_model"); 
		return jt.queryForList(pSQL.getSQL());
	}
}
