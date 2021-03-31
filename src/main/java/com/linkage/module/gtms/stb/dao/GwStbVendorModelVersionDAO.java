/**
 *
 */
package com.linkage.module.gtms.stb.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.system.utils.database.Cursor;
import com.linkage.system.utils.database.DataSetBean;

/**
 * @author zxj E-mail：qixq@lianchuang.com
 * @version $Revision$
 * @since 2009-12-25
 * @category com.linkage.module.stb.dao
 *
 */
public class GwStbVendorModelVersionDAO {

	// 日志记录
	private static Logger m_logger = LoggerFactory
			.getLogger(GwStbVendorModelVersionDAO.class);

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
		PrepareSQL pSQL = new PrepareSQL("select vendor_id,vendor_name, vendor_add from stb_tab_vendor");
		return jt.queryForList(pSQL.toString());
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
		pSQL.append(" select a.device_model_id,a.device_model from stb_gw_device_model a where 1=1 ");
		if (null != vendorId && !"".equals(vendorId) && !"-1".equals(vendorId)) {
			pSQL.append(" and a.vendor_id='");
			pSQL.append(vendorId);
			pSQL.append("'");
		}
		return jt.queryForList(pSQL.toString());
	}

	/**
	 * @category getVersionList 获取所有的设备版本
	 *
	 * @param vendor_id
	 * @param deviceModelId
	 *
	 * @return List
	 */
	public List getVersionList(String deviceModelId) {
		m_logger.debug("getVersionList(deviceModelId:{})",deviceModelId);
		PrepareSQL pSQL = new PrepareSQL();
		pSQL.append("select a.devicetype_id,a.softwareversion from stb_tab_devicetype_info a where 1=1 ");
		if (null != deviceModelId && !"".equals(deviceModelId) && !"-1".equals(deviceModelId)) {
			pSQL.append(" and a.device_model_id='");
			pSQL.append(deviceModelId);
			pSQL.append("'");
		}
		return jt.queryForList(pSQL.toString());
	}

	/**
	 * @category getVersionList 获取所有的设备硬件版本
	 *
	 * @param vendor_id
	 * @param deviceModelId
	 *
	 * @return List
	 */
	public List getVersionList1(String deviceModelId) {
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

	public List getHardVersionList(String vendorId,String deviceModelId)
	{
		PrepareSQL pSQL = new PrepareSQL();
		pSQL.append("select devicetype_id,hardwareversion from stb_tab_devicetype_info where 1=1 ");
		if (!StringUtil.IsEmpty(deviceModelId) && !"-1".equals(deviceModelId)) {
			pSQL.append("and device_model_id='"+deviceModelId+"' ");
		}

		if (!StringUtil.IsEmpty(vendorId) && !"-1".equals(vendorId)) {
			pSQL.append("and vendorId='"+vendorId+"' ");
		}

		return jt.queryForList(pSQL.toString());
	}


	public Map getSoftVersionMap(String devicetypeId)
	{
		PrepareSQL pSQL = new PrepareSQL();
		pSQL.append("select softwareversion from stb_tab_devicetype_info where 1=1 ");
		if (!StringUtil.IsEmpty(devicetypeId) && !"-1".equals(devicetypeId)) {
			pSQL.append("and devicetype_id="+devicetypeId+" ");
		}

		return jt.queryForMap(pSQL.toString());
	}

	/**
	 * 获取当前型号的所有硬件版本
	 * @param
	 * @param deviceModelId
	 * @return
	 */
	public List getDeviceHardVersion(String deviceModelId) {
		m_logger.debug("getVersionList(deviceModelId:{})",deviceModelId);
		PrepareSQL pSQL = new PrepareSQL();
		pSQL.append("select distinct(hardwareversion) as hardwareversion from ");
		pSQL.append("stb_tab_devicetype_info where 1=1 ");
		if (!StringUtil.IsEmpty(deviceModelId) && !"-1".equals(deviceModelId)) {
			pSQL.append(" and device_model_id='"+deviceModelId+"' ");
		}
		return jt.queryForList(pSQL.toString());
	}

	public List getSoftVersion(String hardVercion,String deviceModelId)
	{
		m_logger.debug("getVersionList(deviceModelId:{})",deviceModelId);
		PrepareSQL pSQL = new PrepareSQL();
		pSQL.append("select devicetype_id,softwareversion from stb_tab_devicetype_info where 1=1 ");
		if (!StringUtil.IsEmpty(deviceModelId) && !"-1".equals(deviceModelId)) {
			pSQL.append("and device_model_id='"+deviceModelId+"' ");
		}
		if (!StringUtil.IsEmpty(hardVercion) && !"-1".equals(hardVercion)) {
			pSQL.append("and hardwareversion='"+hardVercion+"' ");
		}

		return jt.queryForList(pSQL.toString());
	}

	/**
	 * 根据硬件版本获取所有的设备版本
	 * @param
	 * @param deviceModelId
	 * @return
	 */
	public List getDevicetypeByHardVersion(String hardVersion) {
		m_logger.debug("getVersionList(hardVersion:{})",hardVersion);
		PrepareSQL pSQL = new PrepareSQL();
		pSQL.append("select a.devicetype_id,a.softwareversion from stb_tab_devicetype_info a where 1=1 ");
		if (!StringUtil.IsEmpty(hardVersion) && !"-1".equals(hardVersion)) {
			pSQL.append(" and a.hardwareversion='");
			pSQL.append(hardVersion);
			pSQL.append("'");
		}
		return jt.queryForList(pSQL.toString());
	}

	public List getVersionPath(String vendorId,String deviceModelId)
	{
		PrepareSQL pSQL = new PrepareSQL();
		pSQL.append("select distinct(a.version_path) as version_path ");
		pSQL.append("from stb_gw_version_file_path a,stb_version_file_path_model b ");
		pSQL.append("where b.path_id=a.id and a.valid=1 ");

		if (!StringUtil.IsEmpty(vendorId) && !"-1".equals(vendorId)) {
			pSQL.append(" and a.vendor_id='"+vendorId+"' ");
		}
		if (!StringUtil.IsEmpty(deviceModelId) && !"-1".equals(deviceModelId)) {
			pSQL.append(" and b.device_model_id='"+deviceModelId+"' ");
		}

		return jt.queryForList(pSQL.toString());
	}

	public List getSpecialPath(String vendorId,String deviceModelId,String version_path)
	{
		PrepareSQL pSQL = new PrepareSQL();
		pSQL.append("select distinct(a.special_path) as special_path ");
		pSQL.append("from stb_gw_version_file_path a,stb_version_file_path_model b ");
		pSQL.append("where b.path_id=a.id and a.valid=1 ");

		if (!StringUtil.IsEmpty(vendorId) && !"-1".equals(vendorId)) {
			pSQL.append(" and a.vendor_id='"+vendorId+"' ");
		}
		if (!StringUtil.IsEmpty(deviceModelId) && !"-1".equals(deviceModelId)) {
			pSQL.append(" and b.device_model_id='"+deviceModelId+"' ");
		}

		if (!StringUtil.IsEmpty(version_path) && !"-1".equals(version_path)) {
			pSQL.append(" and a.version_path='"+version_path+"' ");
		}

		return jt.queryForList(pSQL.toString());
	}

	public List getDcnPath(String vendorId,String deviceModelId,String version_path,String special_path)
	{
		PrepareSQL pSQL = new PrepareSQL();
		pSQL.append("select b.path_id,a.dcn_path ");
		pSQL.append("from stb_gw_version_file_path a,stb_version_file_path_model b ");
		pSQL.append("where b.path_id=a.id and a.valid=1 ");

		if (!StringUtil.IsEmpty(vendorId) && !"-1".equals(vendorId)) {
			pSQL.append(" and a.vendor_id='"+vendorId+"' ");
		}
		if (!StringUtil.IsEmpty(deviceModelId) && !"-1".equals(deviceModelId)) {
			pSQL.append(" and b.device_model_id='"+deviceModelId+"' ");
		}

		if (!StringUtil.IsEmpty(version_path) && !"-1".equals(version_path)) {
			pSQL.append(" and a.version_path='"+version_path+"' ");
		}

		if (!StringUtil.IsEmpty(special_path) && !"-1".equals(special_path)) {
			pSQL.append("and a.special_path='"+special_path+"'");
		}

		return jt.queryForList(pSQL.toString());
	}



	/**
	 * 取得所有vendorId和厂商名对应的MAP
	 * @author 王森博
	 * 2009-11-17
	 */
	public static HashMap<String, String> getVendorMap()
	{
		m_logger.debug("getVendorMap()");
		HashMap<String, String> vendorMap = new HashMap<String, String>();
		vendorMap.clear();
		PrepareSQL psql = new PrepareSQL("select vendor_id,vendor_name,vendor_add from stb_tab_vendor");
		Cursor cursor = DataSetBean.getCursor(psql.getSQL());
		Map fields = cursor.getNext();
		if (fields == null){
			m_logger.debug("厂商资源表没数据");
		}else{
			while (fields != null)
			{
				String vendor_add = (String) fields.get("vendor_add");
				if (false==StringUtil.IsEmpty(vendor_add))
				{
					vendorMap.put((String) fields.get("vendor_id"), vendor_add);
				}
				else
				{
					vendorMap.put((String) fields.get("vendor_id"), (String) fields
							.get("vendor_name"));
				}
				fields = cursor.getNext();
			}
		}
		return vendorMap;
	}

	/**
	 * 取得devicetype_id和软件版本对应的MAP
	 *
	 * @author wangsenbo
	 * @date Nov 17, 2009
	 * @return HashMap<String,String>
	 */
	public static HashMap<String, String> getDevicetypeMap()
	{
		m_logger.debug("getDevicetypeMap()");
		HashMap<String, String> devicetypeMap = new HashMap<String, String>();
		devicetypeMap.clear();
		PrepareSQL psql = new PrepareSQL("select devicetype_id,softwareversion from stb_tab_devicetype_info");
		Cursor cursor = DataSetBean.getCursor(psql.getSQL());
		Map fields = cursor.getNext();
		if (fields == null){
			m_logger.debug("设备型号版本表没数据");
		}else{
			while (fields != null)
			{
				devicetypeMap.put((String) fields.get("devicetype_id"), (String) fields.get("softwareversion"));
				fields = cursor.getNext();
			}
		}
		return devicetypeMap;
	}

	/**
	 * 取得device_model_id和设备型号对应的MAP
	 *
	 * @author wangsenbo
	 * @date Nov 17, 2009
	 * @return HashMap<String,String>
	 */
	public static HashMap<String, String> getDeviceModelMap()
	{
		m_logger.debug("getDeviceModelMap()");
		HashMap<String, String> deviceModelMap = new HashMap<String, String>();
		deviceModelMap.clear();
		PrepareSQL psql = new PrepareSQL("select device_model_id,device_model from stb_gw_device_model");
		Cursor cursor = DataSetBean.getCursor(psql.getSQL());
		Map fields = cursor.getNext();
		if (fields == null){
			m_logger.debug("设备型号表没数据");
		}else{
			while (fields != null)
			{
				deviceModelMap.put((String) fields.get("device_model_id"), (String) fields.get("device_model"));
				fields = cursor.getNext();
			}
		}
		return deviceModelMap;
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

    public List getDeviceOui(String gwShare_modelId) {
		m_logger.debug("getDeviceModel(getDeviceOui:{})",gwShare_modelId);
		PrepareSQL pSQL = new PrepareSQL();
		pSQL.append(" select a.oui from stb_tab_vendor_oui  a where 1=1 ");
		if (null != gwShare_modelId && !"".equals(gwShare_modelId) && !"-1".equals(gwShare_modelId)) {
			pSQL.append(" and a.vendor_id='");
			pSQL.append(gwShare_modelId);
			pSQL.append("'");
		}
		return jt.queryForList(pSQL.toString());
	}


}
