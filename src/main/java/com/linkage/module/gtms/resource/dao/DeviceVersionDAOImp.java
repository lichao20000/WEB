package com.linkage.module.gtms.resource.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.module.gwms.dao.SuperDAO;

/**
 *
 * @author Administrator(工号) Tel:
 * @version 1.0
 * @since Apr 19, 2012 11:00:28 AM
 * @category com.linkage.module.gtms.resource.dao
 * @copyright 南京联创科技 网管科技部
 *
 */
public class DeviceVersionDAOImp extends SuperDAO implements DeviceVersionDAO {

	private static Logger logger = LoggerFactory.getLogger(DeviceVersionDAOImp.class);

	public List<Map> queryDeviceList(String vendorId, String deviceModelId,
			String devicetypeId, int rela_dev_type, String bssDevPort,int curPage_splitPage,
			int num_splitPage) {

		logger.debug("DeviceVersionDAOImp==>queryDeviceList({},{},{},{},{},{})",
				new Object[] { vendorId, deviceModelId, devicetypeId, curPage_splitPage,
						num_splitPage });

		PrepareSQL psql = new PrepareSQL();// TODO wait (more table related)

		psql.append("select a.devicetype_id, c.vendor_add, b.device_model, a.vendor_id, a.device_model_id, a.hardwareversion, a.softwareversion, a.is_check,a.spec_id,t.spec_name ");
		psql.append("  from tab_devicetype_info a left join tab_bss_dev_port t on a.spec_id=t.id , gw_device_model b, tab_vendor c");
		psql.append(" where 1=1 ");
		psql.append("   and a.device_model_id = b.device_model_id ");
		psql.append("   and a.vendor_id = c.vendor_id ");

		if (null != vendorId && !"-1".equals(vendorId)) {
			psql.append("   and c.vendor_id ='"+vendorId+"'");
		}

		if (null != deviceModelId && !"-1".equals(deviceModelId)) {
			psql.append("   and b.device_model_id = '"+deviceModelId+"'");
		}

		if (null != devicetypeId && !"-1".equals(devicetypeId)) {
			psql.append("   and a.devicetype_id = "+devicetypeId);
		}

		if (rela_dev_type != -1) {
			psql.append("   and a.rela_dev_type_id = " + rela_dev_type);
		}

		if(null != bssDevPort && !"-1".equals(bssDevPort)){
			psql.append(" and a.spec_id = "+bssDevPort);
		}

		psql.append("   and a.is_check = 1 ");  // 经过审核的版本(规范版本)

		List<Map> bssDeportList = querySpecName();

		List<Map> list = querySP(psql.getSQL(), (curPage_splitPage - 1) * num_splitPage
				+ 1, num_splitPage, new RowMapper()
		{
			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				Map<String, String> map = new HashMap<String, String>();
				map.put("devicetype_id", rs.getString("devicetype_id"));
				map.put("vendor_add", rs.getString("vendor_add"));
				map.put("device_model", rs.getString("device_model"));
				map.put("vendor_id", rs.getString("vendor_id"));
				map.put("device_model_id", rs.getString("device_model_id"));
				map.put("hardwareversion", rs.getString("hardwareversion"));
				map.put("softwareversion", rs.getString("softwareversion"));
				map.put("is_check", rs.getString("is_check"));
				map.put("spec_name", rs.getString("spec_name"));
				return map;
			}
		});
		return list;

	}


	public List<Map> queryDeviceList(String vendorId, String deviceModelId,
			String devicetypeId, int rela_dev_type, String bssDevPort) {

		logger.debug("DeviceVersionDAOImp==>queryDeviceList({},{},{},{},{},{})",
				new Object[] { vendorId, deviceModelId, devicetypeId});

		PrepareSQL psql = new PrepareSQL();// TODO wait (more table related)

		psql.append("select a.devicetype_id, c.vendor_add, b.device_model, a.vendor_id, a.device_model_id, a.hardwareversion, a.softwareversion, a.is_check,a.spec_id,t.spec_name ");
		psql.append("  from tab_devicetype_info a left join tab_bss_dev_port t on a.spec_id=t.id , gw_device_model b, tab_vendor c");
		psql.append(" where 1=1 ");
		psql.append("   and a.device_model_id = b.device_model_id ");
		psql.append("   and a.vendor_id = c.vendor_id ");

		if (null != vendorId && !"-1".equals(vendorId)) {
			psql.append("   and c.vendor_id ='"+vendorId+"'");
		}

		if (null != deviceModelId && !"-1".equals(deviceModelId)) {
			psql.append("   and b.device_model_id = '"+deviceModelId+"'");
		}

		if (null != devicetypeId && !"-1".equals(devicetypeId)) {
			psql.append("   and a.devicetype_id = "+devicetypeId);
		}

		if (rela_dev_type != -1) {
			psql.append("   and a.rela_dev_type_id = " + rela_dev_type);
		}

		if(null != bssDevPort && !"-1".equals(bssDevPort)){
			psql.append(" and a.spec_id = "+bssDevPort);
		}

		psql.append("   and a.is_check = 1 ");  // 经过审核的版本(规范版本)


		List<Map> list = jt.queryForList(psql.getSQL());
		return list;

	}



	public int queryDeviceCount(String vendorId, String deviceModelId,
			String devicetypeId, int rela_dev_type,String bssDevPort, int curPage_splitPage,
			int num_splitPage) {

		logger.debug("DeviceVersionDAOImp==>queryDeviceCount({},{},{},{},{},{})",
				new Object[] { vendorId, deviceModelId, devicetypeId, curPage_splitPage,
						num_splitPage });

		PrepareSQL psql = new PrepareSQL();// TODO wait (more table related)

		psql.append("select count(*) ");
		psql.append("  from tab_devicetype_info a, gw_device_model b, tab_vendor c");
		psql.append(" where 1=1 ");
		psql.append("   and a.device_model_id = b.device_model_id ");
		psql.append("   and a.vendor_id = c.vendor_id ");

		if (null != vendorId && !"-1".equals(vendorId)) {
			psql.append("   and c.vendor_id ='"+vendorId+"'");
		}

		if (null != deviceModelId && !"-1".equals(deviceModelId)) {
			psql.append("   and b.device_model_id = '"+deviceModelId+"'");
		}

		if (null != devicetypeId && !"-1".equals(devicetypeId)) {
			psql.append("   and a.devicetype_id = "+devicetypeId);
		}

		if (rela_dev_type != -1) {
			psql.append("   and a.rela_dev_type_id = " + rela_dev_type);
		}

		if(null != bssDevPort && !"-1".equals(bssDevPort)){
			psql.append(" and a.spec_id ="+bssDevPort);
		}

		psql.append("   and a.is_check = 1 ");  // 经过审核的版本(规范版本)


		int total = jt.queryForInt(psql.getSQL());

		int maxPage = 1;

		if (total % num_splitPage == 0) {
			maxPage = total / num_splitPage;
		} else {
			maxPage = total / num_splitPage + 1;
		}
		return maxPage;

	}


	public List<Map> querySpecName(){
		logger.debug("DeviceVersionDAOImp-->querySpecName");
		PrepareSQL psql = new PrepareSQL("select id,spec_name from tab_bss_dev_port");
		List<Map> list = new ArrayList<Map>();
		list = jt.query(psql.getSQL(), new RowMapper(){

			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				Map<String, String> map = new HashMap<String, String>();
				map.put("spec_id", rs.getString("id"));
				map.put("spec_name", rs.getString("spec_name"));
				return map;
			}

		});

		return list;
	}

}
