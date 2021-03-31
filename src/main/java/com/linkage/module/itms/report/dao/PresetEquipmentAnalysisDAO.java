package com.linkage.module.itms.report.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class PresetEquipmentAnalysisDAO extends SuperDAO 
{
	private static Logger logger = LoggerFactory.getLogger(PresetEquipmentAnalysisDAO.class);
	/**
	 * Map<city_id,city_name>
	 */
	private Map<String, String> cityMap = null;

	private void SQLAppend(String starttime, String endtime, String vendorId,
			String city_id, String modelId, StringBuffer sql) 
	{
		if (!StringUtil.IsEmpty(starttime)) {
			sql.append(" and add_date>=").append(starttime);
		}
		if (!StringUtil.IsEmpty(endtime)) {
			sql.append(" and add_date<=").append(endtime);
		}
		if (!StringUtil.IsEmpty(vendorId) && !"-1".equals(vendorId)) {
			sql.append(" and vendor_name='").append(vendorId).append("'");
		}
		if (!StringUtil.IsEmpty(modelId) && !"-1".equals(modelId)) {
			sql.append(" and model_name='").append(modelId).append("'");
		}
		if (!StringUtil.IsEmpty(city_id) && !"00".equals(city_id)
				&& !"-1".equals(city_id)) {
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(city_id);
			sql.append(" and city_id in (")
					.append(StringUtils.weave(cityIdList)).append(")");
			cityIdList = null;
		}
	}

	/**
	 * 获取设备厂商
	 * 
	 * @return
	 */
	public Map<String, String> getVendor() 
	{
		logger.debug("getVendor()");
		Map<String, String> map = new HashMap<String, String>();
		PrepareSQL pSQL = new PrepareSQL(
				"select distinct(vendor_name) from tab_gw_device_init where vendor_name is not null");
		List<Map> list = new ArrayList<Map>();
		list = jt.queryForList(pSQL.getSQL());
		if (list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				map.put(StringUtil.getStringValue(list.get(i).get("vendor_name")), 
						StringUtil.getStringValue(list.get(i).get("vendor_name")));
			}
		}
		return map;
	}

	/**
	 * 获取设备型号
	 * 
	 * @param vendorId
	 * @return
	 */
	public List getDeviceModel(String vendorId) 
	{
		PrepareSQL pSQL = new PrepareSQL();
		pSQL.append("select distinct(model_name) model_name from tab_gw_device_init where model_name is not null ");
		if (!StringUtil.IsEmpty(vendorId) && !"-1".equals(vendorId)) {
			pSQL.append(" and vendor_name='"+vendorId+"'");
		}
		return jt.queryForList(pSQL.getSQL());
	}

	public List<String> getDeviceModel1(String vendorId) 
	{
		StringBuffer sql = new StringBuffer();
		sql.append("select distinct(model_name) model_name from tab_gw_device_init where model_name is not null ");
		if (!StringUtil.IsEmpty(vendorId) && !"-1".equals(vendorId)) {
			sql.append(" and vendor_name='"+vendorId+"'");
		}
		List<String> tempList = new ArrayList<String>();
		List<Map> list = new ArrayList<Map>();
		PrepareSQL pSQL = new PrepareSQL(sql.toString());
		list = jt.queryForList(pSQL.getSQL());
		for (int i = 0; i < list.size(); i++) {
			String model_name = StringUtil.getStringValue(list.get(i).get("model_name"));
			tempList.add(model_name);
		}
		return tempList;
	}

	public List<String> getVendorList() {
		List<String> tempList = new ArrayList<String>();
		String sql = "select distinct(vendor_name) from tab_gw_device_init where vendor_name is not null  ";
		PrepareSQL psql = new PrepareSQL(sql);
		List<Map> list = new ArrayList<Map>();
		list = jt.queryForList(psql.getSQL());
		for (int i = 0; i < list.size(); i++) {
			String vendor_id = StringUtil.getStringValue(list.get(i).get("vendor_name"));
			tempList.add(vendor_id);
		}
		return tempList;
	}

	/**
	 * 已导入未使用
	 * 
	 * @param starttime
	 * @param endtime
	 * @param vendorId
	 * @param city_id
	 * @param modelId
	 * @return
	 */
	public Map<String, String> importnouse(String starttime, String endtime,
			String vendorId, String city_id, String modelId) 
	{
		StringBuffer sql = new StringBuffer();
		if(DBUtil.GetDB()==3){
			sql.append("select vendor_name,model_name,status, count(*) num ");
		}else{
			sql.append("select vendor_name,model_name,status, count(1) num ");
		}
		sql.append("from tab_gw_device_init where status in (1,2,3,4)");
		SQLAppend(starttime, endtime, vendorId, city_id, modelId, sql);
		sql.append(" group by vendor_name,model_name,status");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		Map<String, String> map = new HashMap<String, String>();
		List<Map> list = new ArrayList<Map>();
		list = jt.queryForList(psql.getSQL());
		if (list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				map.put(StringUtil.getStringValue(list.get(i).get("vendor_name"))
							+ "#" + StringUtil.getStringValue(list.get(i).get("model_name"))
							+ "#" + StringUtil.getStringValue(list.get(i).get("status")),
						StringUtil.getStringValue(list.get(i).get("num")));
			}
		}
		return map;
	}

	public List<Map> getDeviceListForWBdTerminal(String city_id,
			String starttime, String endtime, String vendorId, String modelId,
			String status, int curPage_splitPage, int num_splitPage) 
	{
		StringBuffer sql = new StringBuffer();
		/**	if(DBUtil.GetDB()==3){
			//TODO wait
		}else{
			
		}*/
		sql.append("select a.oui,a.device_serialnumber,a.vendor_name,a.model_name,a.city_id,"
				+ "a.buy_time,a.cpe_mac,a.status,b.complete_time,a.cpe_currentupdatetime,b.device_id_ex,"
				+ "c.softwareversion,c.hardwareversion from tab_gw_device_init a "
				+ "left join tab_gw_device b on a.device_id=b.device_id "
				+ "left join tab_devicetype_info c on c.devicetype_id=b.device_id "
				+ "where 1=1 ");
		if (!StringUtil.IsEmpty(starttime)) {
			sql.append(" and a.add_date>=").append(starttime);
		}
		if (!StringUtil.IsEmpty(endtime)) {
			sql.append(" and a.add_date<=").append(endtime);
		}
		if (!StringUtil.IsEmpty(vendorId) && !"-1".equals(vendorId)) {
			sql.append(" and a.vendor_name='").append(vendorId).append("'");
		}
		if (!StringUtil.IsEmpty(modelId) && !"-1".equals(modelId)) {
			sql.append(" and a.model_name='").append(modelId).append("'");
		}
		if (!StringUtil.IsEmpty(status)) {
			sql.append(" and a.status=").append(status);
		}
		if (!StringUtil.IsEmpty(city_id) && !"00".equals(city_id)
				&& !"-1".equals(city_id)) {
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(city_id);
			sql.append(" and a.city_id in (")
					.append(StringUtils.weave(cityIdList)).append(")");
			cityIdList = null;
		}
		sql.append(" order by a.city_id");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		cityMap = CityDAO.getCityIdCityNameMap();
		List<Map> list = querySP(psql.getSQL(), (curPage_splitPage - 1)
				* num_splitPage + 1, num_splitPage, new RowMapper() {

			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				Map<String, String> map = new HashMap<String, String>();
				map.put("oui", StringUtil.getStringValue(rs.getString("oui")));
				map.put("device_serialnumber", StringUtil.getStringValue(rs
						.getString("device_serialnumber")));
				map.put("vendor_name",
						StringUtil.getStringValue(rs.getString("vendor_name")));
				map.put("model_name",
						StringUtil.getStringValue(rs.getString("model_name")));
				String city_id = rs.getString("city_id");
				map.put("city_id", city_id);
				String city_name = StringUtil.getStringValue(cityMap
						.get(city_id));
				if (false == StringUtil.IsEmpty(city_name)) {
					map.put("city_name", city_name);
				} else {
					map.put("city_name", "");
				}
				try {
					long buy_time = StringUtil.getLongValue(rs
							.getString("buy_time"));
					DateTimeUtil dt = new DateTimeUtil(buy_time * 1000);
					map.put("buy_time", dt.getLongDate());
				} catch (NumberFormatException e) {
					map.put("buy_time", "");
				} catch (Exception e) {
					map.put("buy_time", "");
				}
				map.put("cpe_mac",
						StringUtil.getStringValue(rs.getString("cpe_mac")));
				String status = StringUtil.getStringValue(rs
						.getString("status"));
				if ("1".equals(status)) {
					status = "已导入未使用";
				}
				if ("2".equals(status)) {
					status = "已上线未绑定";
				}
				if ("3".equals(status)) {
					status = "已使用已绑定";
				}
				if ("4".equals(status)) {
					status = "曾经使用";
				}
				map.put("status", status);
				try {
					long complete_time = StringUtil.getLongValue(rs
							.getString("complete_time"));
					DateTimeUtil dt = new DateTimeUtil(complete_time * 1000);
					map.put("complete_time", dt.getLongDate());
				} catch (NumberFormatException e) {
					map.put("complete_time", "");
				} catch (Exception e) {
					map.put("complete_time", "");
				}
				try {
					long cpe_currentupdatetime = StringUtil.getLongValue(rs
							.getString("cpe_currentupdatetime"));
					DateTimeUtil dt = new DateTimeUtil(
							cpe_currentupdatetime * 1000);
					map.put("cpe_currentupdatetime", dt.getLongDate());
				} catch (NumberFormatException e) {
					map.put("cpe_currentupdatetime", "");
				} catch (Exception e) {
					map.put("cpe_currentupdatetime", "");
				}
				map.put("device_id_ex",
						StringUtil.getStringValue(rs.getString("device_id_ex")));
				map.put("softwareversion", StringUtil.getStringValue(rs
						.getString("softwareversion")));
				map.put("hardwareversion", rs.getString("hardwareversion"));
				return map;
			}
		});
		cityMap = null;
		return list;
	}
	
	public List<Map> getDevExcel(String city_id,String starttime, String endtime, 
			String vendorId, String modelId,String status) 
	{
		StringBuffer sql = new StringBuffer();
		/**	if(DBUtil.GetDB()==3){
			//TODO wait
		}else{
			
		}*/
		sql.append("select a.oui,a.device_serialnumber,a.vendor_name,a.model_name,a.city_id,"
				+ "a.buy_time,a.cpe_mac,a.status,b.complete_time,a.cpe_currentupdatetime,b.device_id_ex,"
				+ "c.softwareversion,c.hardwareversion from tab_gw_device_init a "
				+ "left join tab_gw_device b on a.device_id=b.device_id "
				+ "left join tab_devicetype_info c on c.devicetype_id=b.device_id "
				+ "where 1=1 ");
		if (!StringUtil.IsEmpty(starttime)) {
			sql.append(" and a.add_date>=").append(starttime);
		}
		if (!StringUtil.IsEmpty(endtime)) {
			sql.append(" and a.add_date<=").append(endtime);
		}
		if (!StringUtil.IsEmpty(vendorId) && !"-1".equals(vendorId)) {
			sql.append(" and a.vendor_name='").append(vendorId).append("'");
		}
		if (!StringUtil.IsEmpty(modelId) && !"-1".equals(modelId)) {
			sql.append(" and a.model_name='").append(modelId).append("'");
		}
		if (!StringUtil.IsEmpty(status)) {
			sql.append(" and a.status=").append(status);
		}
		if (!StringUtil.IsEmpty(city_id) && !"00".equals(city_id)
				&& !"-1".equals(city_id)) {
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(city_id);
			sql.append(" and a.city_id in (")
					.append(StringUtils.weave(cityIdList)).append(")");
			cityIdList = null;
		}
		sql.append(" order by a.city_id");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		cityMap = CityDAO.getCityIdCityNameMap();
		List<Map> list = jt.query(psql.getSQL(), new RowMapper() {

			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				Map<String, String> map = new HashMap<String, String>();
				map.put("oui", StringUtil.getStringValue(rs.getString("oui")));
				map.put("device_serialnumber", StringUtil.getStringValue(rs
						.getString("device_serialnumber")));
				map.put("vendor_name",
						StringUtil.getStringValue(rs.getString("vendor_name")));
				map.put("model_name",
						StringUtil.getStringValue(rs.getString("model_name")));
				String city_id = rs.getString("city_id");
				map.put("city_id", city_id);
				String city_name = StringUtil.getStringValue(cityMap
						.get(city_id));
				if (false == StringUtil.IsEmpty(city_name)) {
					map.put("city_name", city_name);
				} else {
					map.put("city_name", "");
				}
				try {
					long buy_time = StringUtil.getLongValue(rs
							.getString("buy_time"));
					DateTimeUtil dt = new DateTimeUtil(buy_time * 1000);
					map.put("buy_time", dt.getLongDate());
				} catch (NumberFormatException e) {
					map.put("buy_time", "");
				} catch (Exception e) {
					map.put("buy_time", "");
				}
				map.put("cpe_mac",
						StringUtil.getStringValue(rs.getString("cpe_mac")));
				map.put("success","成功");
				String status = StringUtil.getStringValue(rs
						.getString("status"));
				if ("1".equals(status)) {
					status = "已导入未使用";
				}
				if ("2".equals(status)) {
					status = "已上线未绑定";
				}
				if ("3".equals(status)) {
					status = "已使用已绑定";
				}
				if ("4".equals(status)) {
					status = "曾经使用";
				}
				map.put("status", status);
				try {
					long complete_time = StringUtil.getLongValue(rs
							.getString("complete_time"));
					DateTimeUtil dt = new DateTimeUtil(complete_time * 1000);
					map.put("complete_time", dt.getLongDate());
				} catch (NumberFormatException e) {
					map.put("complete_time", "");
				} catch (Exception e) {
					map.put("complete_time", "");
				}
				try {
					long cpe_currentupdatetime = StringUtil.getLongValue(rs
							.getString("cpe_currentupdatetime"));
					DateTimeUtil dt = new DateTimeUtil(
							cpe_currentupdatetime * 1000);
					map.put("cpe_currentupdatetime", dt.getLongDate());
				} catch (NumberFormatException e) {
					map.put("cpe_currentupdatetime", "");
				} catch (Exception e) {
					map.put("cpe_currentupdatetime", "");
				}
				map.put("device_id_ex",
						StringUtil.getStringValue(rs.getString("device_id_ex")));
				map.put("softwareversion", StringUtil.getStringValue(rs
						.getString("softwareversion")));
				map.put("hardwareversion", rs.getString("hardwareversion"));
				return map;
			}
		});
		cityMap = null;
		return list;
	}

	public int getDeviceListForWBdTerminalCount(String city_id,
			String starttime, String endtime, String vendorId, String modelId,
			String status, int curPage_splitPage, int num_splitPage) 
	{
		StringBuffer sql = new StringBuffer();
		if(DBUtil.GetDB()==3){
			//TODO wait
			sql.append("select count(*) ");
		}else{
			sql.append("select count(1) ");
		}
		sql.append("from tab_gw_device_init a ");
		sql.append("left join tab_gw_device b on a.device_id=b.device_id ");
		sql.append("left join tab_devicetype_info c on c.devicetype_id=b.device_id ");
		sql.append("where 1=1 ");
		if (!StringUtil.IsEmpty(starttime)) {
			sql.append(" and a.add_date>=").append(starttime);
		}
		if (!StringUtil.IsEmpty(endtime)) {
			sql.append(" and a.add_date<=").append(endtime);
		}
		if (!StringUtil.IsEmpty(vendorId) && !"-1".equals(vendorId)) {
			sql.append(" and a.vendor_name='").append(vendorId).append("'");
		}
		if (!StringUtil.IsEmpty(modelId) && !"-1".equals(modelId)) {
			sql.append(" and a.model_name='").append(modelId).append("'");
		}
		if (!StringUtil.IsEmpty(status)) {
			sql.append(" and a.status=").append(status);
		}
		if (!StringUtil.IsEmpty(city_id) && !"00".equals(city_id)
				&& !"-1".equals(city_id)) {
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(city_id);
			sql.append(" and a.city_id in (")
					.append(StringUtils.weave(cityIdList)).append(")");
			cityIdList = null;
		}
		PrepareSQL psql = new PrepareSQL(sql.toString());
		int total = jt.queryForInt(psql.getSQL());
		int maxPage = 1;
		if (total % num_splitPage == 0) {
			maxPage = total / num_splitPage;
		} else {
			maxPage = total / num_splitPage + 1;
		}
		return maxPage;
	}
}
