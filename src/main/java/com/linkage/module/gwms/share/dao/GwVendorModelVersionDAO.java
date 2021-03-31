/**
 * 
 */
package com.linkage.module.gwms.share.dao;

import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import com.linkage.commons.db.PrepareSQL;

/**
 * @author zxj E-mail：qixq@lianchuang.com
 * @version $Revision$
 * @since 2009-12-25
 * @category com.linkage.module.stb.dao
 * 
 */
public class GwVendorModelVersionDAO {

	// 日志记录
	private static Logger m_logger = LoggerFactory
			.getLogger(GwVendorModelVersionDAO.class);

	private JdbcTemplate jt;

	public void setDao(DataSource dao) {
		jt = new JdbcTemplate(dao);
	}
	
	/**
	 * @category getVendor 获取所有的厂商
	 * 
	 * @param city_id
	 * 
	 * @return List
	 */
	public List getVendor() {
		m_logger.debug("getVendor()");
		PrepareSQL pSQL = new PrepareSQL("select vendor_id,vendor_name, vendor_add from tab_vendor order by vendor_name");
		return jt.queryForList(pSQL.getSQL());
	}
	
	public List getVendorStb() {
		m_logger.debug("getVendor()");
		PrepareSQL pSQL = new PrepareSQL("select vendor_id,vendor_name, vendor_add from stb_tab_vendor order by vendor_name");
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
		m_logger.debug("getDeviceModel(vendorId:{})",vendorId);
		PrepareSQL pSQL = new PrepareSQL();
		pSQL.append(" select a.device_model_id,a.device_model from gw_device_model a where 1=1 ");
		if (null != vendorId && !"".equals(vendorId) && !"-1".equals(vendorId)) {
			pSQL.append(" and a.vendor_id='");
			pSQL.append(vendorId);
			pSQL.append("'");
		}
		pSQL.append(" order by device_model");
		return jt.queryForList(pSQL.getSQL());
	}

	public List getDeviceOui(String gwShare_modelId) {
		m_logger.debug("getDeviceModel(getDeviceOui:{})",gwShare_modelId);
		PrepareSQL pSQL = new PrepareSQL();
		pSQL.append(" select a.oui from tab_vendor_oui  a where 1=1 ");
		if (null != gwShare_modelId && !"".equals(gwShare_modelId) && !"-1".equals(gwShare_modelId)) {
			pSQL.append(" and a.vendor_id='");
			pSQL.append(gwShare_modelId);
			pSQL.append("'");
		}
		return jt.queryForList(pSQL.toString());
	}


	public List getDeviceModel1(String vendorId) {
		m_logger.debug("getDeviceModel(vendorId:{})",vendorId);
		PrepareSQL pSQL = new PrepareSQL();
		pSQL.append(" select a.device_model_id,a.device_model from stb_gw_device_model a where 1=1 ");
		if (null != vendorId && !"".equals(vendorId) && !"-1".equals(vendorId)) {
			pSQL.append(" and a.vendor_id='");
			pSQL.append(vendorId);
			pSQL.append("'");
		}
		pSQL.append(" order by device_model");
		return jt.queryForList(pSQL.getSQL());
	}
	/**
	 * @category getVersionList 获取所有的设备版本
	 * 
	 * @param vendor_id
	 * @param deviceModelId
	 * 
	 * @return List
	 */
	public List getVersionList(String deviceModelId,String isBatch) {
		m_logger.debug("getVersionList(deviceModelId:{})",deviceModelId);
		PrepareSQL pSQL = new PrepareSQL();
		if("1".equals(isBatch)){
			pSQL.append("select a.devicetype_id,a.hardwareversion,a.softwareversion from tab_devicetype_info a, gw_soft_upgrade_temp_map b   where a.devicetype_id=b.devicetype_id_old ");
			if (null != deviceModelId && !"".equals(deviceModelId) && !"-1".equals(deviceModelId)) {
				pSQL.append(" and a.device_model_id='");
				pSQL.append(deviceModelId);
				pSQL.append("'");
				pSQL.append(" order by a.softwareversion ");
			}
		}else if("is_check".equals(isBatch)){
			pSQL.append("select a.devicetype_id,a.softwareversion,a.hardwareversion from tab_devicetype_info a where 1=1 and a.is_check=1 ");
			if (null != deviceModelId && !"".equals(deviceModelId) && !"-1".equals(deviceModelId)) {
				pSQL.append(" and a.device_model_id='");
				pSQL.append(deviceModelId);
				pSQL.append("'");
			}
		}else{
			pSQL.append("select a.devicetype_id,a.softwareversion,a.hardwareversion from tab_devicetype_info a where 1=1 ");
			if (null != deviceModelId && !"".equals(deviceModelId) && !"-1".equals(deviceModelId)) {
				pSQL.append(" and a.device_model_id='");
				pSQL.append(deviceModelId);
				pSQL.append("'");
			}
		}
		
		return jt.queryForList(pSQL.getSQL());
	}
	public List getVersionList1(String deviceModelId,String isBatch) {
		m_logger.debug("getVersionList(deviceModelId:{})",deviceModelId);
		PrepareSQL pSQL = new PrepareSQL();
		if("1".equals(isBatch)){
			pSQL.append("select a.devicetype_id,a.hardwareversion,a.softwareversion from stb_tab_devicetype_info a, gw_soft_upgrade_temp_map b   where a.devicetype_id=b.devicetype_id_old ");
			if (null != deviceModelId && !"".equals(deviceModelId) && !"-1".equals(deviceModelId)) {
				pSQL.append(" and a.device_model_id='");
				pSQL.append(deviceModelId);
				pSQL.append("'");
				pSQL.append(" order by a.softwareversion ");
			}
		}else if("is_check".equals(isBatch)){
			pSQL.append("select a.devicetype_id,a.softwareversion,a.hardwareversion from stb_tab_devicetype_info a where 1=1 and a.is_check=1 ");
			if (null != deviceModelId && !"".equals(deviceModelId) && !"-1".equals(deviceModelId)) {
				pSQL.append(" and a.device_model_id='");
				pSQL.append(deviceModelId);
				pSQL.append("'");
			}
		}else{
			pSQL.append("select a.devicetype_id,a.softwareversion,a.hardwareversion from stb_tab_devicetype_info a where 1=1 ");
			if (null != deviceModelId && !"".equals(deviceModelId) && !"-1".equals(deviceModelId)) {
				pSQL.append(" and a.device_model_id='");
				pSQL.append(deviceModelId);
				pSQL.append("'");
			}
		}
		
		return jt.queryForList(pSQL.getSQL());
	}
	/**
	 * @category getVersionList 获取所有的设备硬件版本
	 * 
	 * @param vendor_id
	 * @param deviceModelId
	 * 
	 * @return List
	 */
	public List getVersionList(String deviceModelId) {
		m_logger.debug("getVersionList(deviceModelId:{})",deviceModelId);
		PrepareSQL pSQL = new PrepareSQL();
		pSQL.append("select distinct a.hardwareversion from tab_devicetype_info a where 1=1 ");
		if (null != deviceModelId && !"".equals(deviceModelId) && !"-1".equals(deviceModelId)) {
			pSQL.append(" and a.device_model_id='");
			pSQL.append(deviceModelId);
			pSQL.append("'");
		}
		return jt.queryForList(pSQL.getSQL());
	}
	
	public List getSoftVersionList(String deviceModelId) {
		PrepareSQL pSQL = new PrepareSQL();
		pSQL.append("select distinct a.softwareversion from tab_devicetype_info a where 1=1 ");
		if (null != deviceModelId && !"".equals(deviceModelId) && !"-1".equals(deviceModelId)) {
			pSQL.append(" and a.device_model_id='");
			pSQL.append(deviceModelId);
			pSQL.append("'");
		}
		return jt.queryForList(pSQL.getSQL());
	}
}
