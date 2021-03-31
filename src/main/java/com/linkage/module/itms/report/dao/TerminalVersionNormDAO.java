package com.linkage.module.itms.report.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
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

public class TerminalVersionNormDAO extends SuperDAO 
{
	public static Logger logger = LoggerFactory.getLogger(TerminalVersionNormDAO.class);
	private Map<String, String> cityMap = null;
	private Map<String, String> venderMap = null;
	private Map<String, Object> deviceModelMap = null;

	/**
	 * 
	 * 要求加两个条件，用户竣工（tab_hgwcustomer opmode =1）和已绑定 add by zhangchy 20130114
	 * 
	 * @param vendorId
	 * @param deviceModelId
	 * @param cityId
	 * @param starttime
	 * @param endtime
	 * @return
	 */
	public List<Map> queryAllResult(String vendorId, String deviceModelId,
			String cityId, String accessType, String userType,
			String starttime, String endtime, String gw_type, String isActive) 
	{
		logger.debug("queryAllResult({},{},{},{},{},{},{})", new Object[] {
				vendorId, deviceModelId, cityId, accessType, userType,
				starttime, endtime });

		StringBuffer sql = new StringBuffer();
		if(DBUtil.GetDB()==3){
			//TODO wait
			sql.append(" select a.city_id,b.is_check,count(*) as num ");
		}else{
			sql.append(" select a.city_id,b.is_check,count(1) as num ");
		}
		
		sql.append(" from tab_gw_device a,tab_devicetype_info b, tab_hgwcustomer_report c ");
		sql.append(" where a.devicetype_id = b.devicetype_id ");
		sql.append("   and a.vendor_id = b.vendor_id ");
		sql.append("   and a.device_model_id = b.device_model_id ");
		sql.append("   and a.device_id = c.device_id "); // 用户已绑定 add by
		sql.append("   and c.user_state in('1','2') "); // 用户已绑定 add by zhangchy
		sql.append("   and c.opmode = '1' "); // 用户已竣工 add by zhangchy 20130114

		if (!StringUtil.IsEmpty(vendorId) && !"-1".equals(vendorId)) { // 设备厂商ID
			sql.append(" and a.vendor_id = '" + vendorId + "'");
		}
		if (!StringUtil.IsEmpty(deviceModelId) && !"-1".equals(deviceModelId)) { // 设备型号ID
			sql.append(" and a.device_model_id = '" + deviceModelId + "'");
		}
		if (!StringUtil.IsEmpty(accessType) && !"-1".equals(accessType)) {
			sql.append(" and b.access_style_relay_id = " + accessType); // 上行接入方式
		}
		if (!StringUtil.IsEmpty(userType) && !"-1".equals(userType)) {
			sql.append(" and b.rela_dev_type_id = " + userType); // 终端类型
		}
		if (!StringUtil.IsEmpty(starttime)) { // 竣工时间
			sql.append(" and c.onlinedate >= ").append(starttime);
		}
		if (!StringUtil.IsEmpty(endtime)) { // 竣工时间
			sql.append(" and c.onlinedate <= ").append(endtime);
		}
		if (!StringUtil.IsEmpty(gw_type)) {
			sql.append(" and a.gw_type= ").append(gw_type);
		}
		if (!"-1".equals(isActive)) {
			sql.append(" and c.is_active= ").append(isActive);
		}
		if (!StringUtil.IsEmpty(cityId) && !"00".equals(cityId)) { // 设备属地
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and a.city_id in (")
					.append(StringUtils.weave(cityIdList)).append(")");
			cityIdList = null;
		}
		sql.append(" group by  a.city_id,b.is_check");

		PrepareSQL psql = new PrepareSQL(sql.toString());
		return jt.queryForList(psql.getSQL());
	}

	/**
	 * 当flag=1时为规范版本，2时为不规范版本，3时查询全部 要求加两个条件，用户竣工（tab_hgwcustomer opmode =1）和已绑定
	 * add by zhangsb3 2013年2月1日 16:02:34
	 * 
	 * @param flag
	 * @param vendorId
	 * @param deviceModelId
	 * @param cityId
	 * @param starttime
	 * @param endtime
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @return
	 */
	public List<Map> queryDeviceResult(String flag, String vendorId,
			String deviceModelId, String cityId, String gw_type,
			String accessType, String userType, String starttime,
			String endtime, String isActive, int curPage_splitPage,int num_splitPage) 
	{
		logger.debug("queryDeviceResult({},{},{},{},{},{},{},{},{},{})",
				new Object[] { vendorId, deviceModelId, cityId, accessType,
						userType, starttime, endtime, curPage_splitPage,num_splitPage });

		StringBuffer sql = new StringBuffer();
		/**	if(DBUtil.GetDB()==3){
			//TODO wait
		}else{
			
		}*/
		sql.append("select distinct a.device_id,a.vendor_id,a.devicetype_id,a.device_model_id,");
		sql.append("a.city_id,a.device_serialnumber,a.device_id_ex,a.device_type,a.complete_time,b.is_check,c.last_time ");
		sql.append("  from tab_gw_device a, tab_devicetype_info b, gw_devicestatus c ,tab_hgwcustomer_report d  ");
		sql.append("   where a.devicetype_id = b.devicetype_id ");
		sql.append("   and a.vendor_id = b.vendor_id ");
		sql.append("   and a.device_model_id = b.device_model_id ");
		sql.append("   and a.device_id = c.device_id");
		sql.append("   and a.device_id = d.device_id ");
		sql.append("   and d.user_state in('1','2')");
		sql.append("   and d.opmode = '1' ");
		if (!StringUtil.IsEmpty(vendorId) && !"-1".equals(vendorId)) { // 设备厂商ID
			sql.append(" and a.vendor_id = '" + vendorId + "'");
		}
		if (!StringUtil.IsEmpty(deviceModelId) && !"-1".equals(deviceModelId)) { // 设备型号ID
			sql.append(" and a.device_model_id = '" + deviceModelId + "'");
		}
		if (!StringUtil.IsEmpty(accessType) && !"-1".equals(accessType)) {
			sql.append(" and b.access_style_relay_id = " + accessType); // 上行接入方式
		}
		if (!StringUtil.IsEmpty(userType) && !"-1".equals(userType)) {
			sql.append(" and b.rela_dev_type_id = " + userType); // 终端类型
		}
		if (!StringUtil.IsEmpty(starttime)) { // 竣工时间
			sql.append(" and d.onlinedate >= ").append(starttime);
		}
		if (!StringUtil.IsEmpty(endtime)) { // 竣工时间
			sql.append(" and d.onlinedate <= ").append(endtime);
		}
		if (!StringUtil.IsEmpty(gw_type)) {
			sql.append(" and a.gw_type= ").append(gw_type);
		}
		if (!"-1".equals(isActive)) {
			sql.append(" and d.is_active= ").append(isActive);
		}
		if (!StringUtil.IsEmpty(cityId) && !"00".equals(cityId)) { // 设备属地
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and a.city_id in (")
					.append(StringUtils.weave(cityIdList)).append(")");
			cityIdList = null;
		}
		if ("1".equals(flag)) { // 如果规范版本
			sql.append(" and b.is_check = 1");
		} else if ("2".equals(flag)) {// 如果是不规范版本
			sql.append(" and b.is_check <> 1");
		}

		PrepareSQL psql = new PrepareSQL(sql.toString());

		cityMap = CityDAO.getCityIdCityNameMap();
		venderMap = getVenderMap();
		deviceModelMap = getDeviceModelMap();

		List<Map> list = querySP(psql.getSQL(), (curPage_splitPage - 1)
				* num_splitPage + 1, num_splitPage, new RowMapper() {

			public Object mapRow(ResultSet rs, int arg1) throws SQLException {

				Map<String, String> map = new HashMap<String, String>();

				// 设备ID
				map.put("device_id", rs.getString("device_id"));

				// 设备厂商
				map.put("vendorName", venderMap.get(rs.getString("vendor_id")));

				// 设备型号 设备版本
				String devicetype_id = rs.getString("devicetype_id");
				if (false == StringUtil.IsEmpty(devicetype_id)) {

					String[] tmp = (String[]) deviceModelMap.get(devicetype_id);
					if (tmp != null && tmp.length == 2) {
						map.put("devicemodel", tmp[0]);
						map.put("softwareversion", tmp[1]);
					} else {
						map.put("devicemodel", "");
						map.put("softwareversion", "");
					}
				} else {
					map.put("devicemodel", "");
					map.put("softwareversion", "");
				}

				// 设备属地
				String city_id = rs.getString("city_id");
				if (false == StringUtil.IsEmpty(city_id)) {
					String city_name = StringUtil.getStringValue(cityMap
							.get(city_id));
					if (false == StringUtil.IsEmpty(city_name)) {
						map.put("city_name", city_name);
					} else {
						map.put("city_name", "");
					}
				} else {
					map.put("city_name", "");
				}

				// 设备序列号
				map.put("deviceSerialnumber",
						rs.getString("device_serialnumber"));
				// 设备逻辑SN
				map.put("deviceIdEx", rs.getString("device_id_ex"));
				// 终端类型
				map.put("device_type", rs.getString("device_type"));

				// 设备注册时间 将complete_time转换成时间
				try {
					long completeTime = StringUtil.getLongValue(rs
							.getString("complete_time"));
					DateTimeUtil dt = new DateTimeUtil(completeTime * 1000);
					map.put("completeTime", dt.getLongDate());
				} catch (NumberFormatException e) {
					map.put("completeTime", "");
				} catch (Exception e) {
					map.put("completeTime", "");
				}

				// 设备最近上报时间 将last_time转换成时间
				try {
					long lastTime = StringUtil.getLongValue(rs
							.getString("last_time"));
					DateTimeUtil dt = new DateTimeUtil(lastTime * 1000);
					map.put("lastTime", dt.getLongDate());
				} catch (NumberFormatException e) {
					map.put("lastTime", "");
				} catch (Exception e) {
					map.put("lastTime", "");
				}

				return map;
			}
		});

		cityMap = null;
		venderMap = null;
		deviceModelMap = null;
		return list;

	}

	/**
	 * 当flag=1时为规范版本，2时为不规范版本，3时查询全部
	 * 
	 * @param flag
	 * @param cityId
	 * @param starttime1
	 * @param endtime1
	 * @param vendorId
	 * @param deviceModelId
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @return
	 */
	public int getDeviceListCount(String flag, String cityId, String gw_type,
			String accessType, String userType, String starttime1,
			String endtime1, String isActive, String vendorId,
			String deviceModelId, int curPage_splitPage, int num_splitPage)
	{
		logger.debug("getDeviceListCount({},{},{},{},{},{},{},{},{},{},{})",
				new Object[] { flag, cityId, accessType, userType, starttime1,
						endtime1, vendorId, deviceModelId, curPage_splitPage,
						num_splitPage });

		StringBuffer sql = new StringBuffer();
		if(DBUtil.GetDB()==3){
			//TODO wait
			sql.append("select count(*) ");
		}else{
			sql.append("select count(1) ");
		}
		
		sql.append("  from tab_gw_device a, tab_devicetype_info b, gw_devicestatus c ,tab_hgwcustomer_report d ");
		sql.append("   where a.devicetype_id = b.devicetype_id ");
		sql.append("   and a.vendor_id = b.vendor_id ");
		sql.append("   and a.device_model_id = b.device_model_id ");
		sql.append("   and a.device_id = c.device_id");
		sql.append("   and a.device_id = d.device_id ");
		sql.append("   and d.user_state in('1','2')");
		sql.append("   and d.opmode = '1' ");
		if (!StringUtil.IsEmpty(vendorId) && !"-1".equals(vendorId)) { // 设备厂商ID
			sql.append(" and a.vendor_id = '" + vendorId + "'");
		}
		if (!StringUtil.IsEmpty(deviceModelId) && !"-1".equals(deviceModelId)) { // 设备型号ID
			sql.append(" and a.device_model_id = '" + deviceModelId + "'");
		}
		if (!StringUtil.IsEmpty(accessType) && !"-1".equals(accessType)) {
			sql.append(" and b.access_style_relay_id = " + accessType); // 上行接入方式
		}
		if (!StringUtil.IsEmpty(userType) && !"-1".equals(userType)) {
			sql.append(" and b.rela_dev_type_id = " + userType); // 终端类型
		}
		if (!StringUtil.IsEmpty(starttime1)) {
			sql.append(" and d.onlinedate >= ").append(starttime1); // 竣工时间
		}
		if (!StringUtil.IsEmpty(endtime1)) {
			sql.append(" and d.onlinedate <= ").append(endtime1); // 竣工时间
		}
		if (!"-1".equals(isActive)) {
			sql.append(" and d.is_active= ").append(isActive);
		}
		if (!StringUtil.IsEmpty(cityId) && !"00".equals(cityId)) { // 设备属地
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and a.city_id in (")
					.append(StringUtils.weave(cityIdList)).append(")");
			cityIdList = null;
		}
		if ("1".equals(flag)) { // 如果规范版本
			sql.append(" and b.is_check = 1");
		} else if ("2".equals(flag)) {// 如果是不规范版本
			sql.append(" and b.is_check <> 1");
		}
		if (!StringUtil.IsEmpty(gw_type)) {
			sql.append(" and a.gw_type= ").append(gw_type);
		}

		PrepareSQL psql = new PrepareSQL(sql.toString());

		int total = jt.queryForInt(psql.getSQL());
		int totalPageNum = 1;
		if (total % num_splitPage == 0) {
			totalPageNum = total / num_splitPage;
		} else {
			totalPageNum = total / num_splitPage + 1;
		}
		logger.warn("返回值totalPageNum" + totalPageNum);
		return totalPageNum;
	}

	public List<Map> getDeviceExcel(String flag, String vendorId,
			String deviceModelId, String cityId, String accessType,
			String userType, String starttime1, String endtime1,
			String gw_type, String isActive) 
	{
		logger.debug("getDeviceExcel({},{},{},{},{},{},{},{},{})",
				new Object[] { flag, vendorId, deviceModelId, cityId,
						accessType, userType, starttime1, endtime1 });

		StringBuffer sql = new StringBuffer();

		sql.append("select distinct a.device_id, a.vendor_id, a.devicetype_id, a.device_model_id, a.city_id, ");
		sql.append("a.device_serialnumber, a.device_id_ex, a.device_type, a.complete_time, c.last_time   ");
		sql.append("  from tab_gw_device a, tab_devicetype_info b, gw_devicestatus c ,tab_hgwcustomer_report d ");
		sql.append("   where a.devicetype_id = b.devicetype_id ");
		sql.append("   and a.vendor_id = b.vendor_id ");
		sql.append("   and a.device_model_id = b.device_model_id ");
		sql.append("   and a.device_id = c.device_id");
		sql.append("   and a.device_id = d.device_id ");
		sql.append("   and d.user_state in('1','2')");
		sql.append("   and d.opmode = '1' ");
		if (!StringUtil.IsEmpty(vendorId) && !"-1".equals(vendorId)) { // 设备厂商ID
			sql.append(" and a.vendor_id = '" + vendorId + "'");
		}
		if (!StringUtil.IsEmpty(deviceModelId) && !"-1".equals(deviceModelId)) { // 设备型号ID
			sql.append(" and a.device_model_id = '" + deviceModelId + "'");
		}
		if (!StringUtil.IsEmpty(accessType) && !"-1".equals(accessType)) {
			sql.append(" and b.access_style_relay_id = " + accessType); // 上行接入方式
		}
		if (!StringUtil.IsEmpty(userType) && !"-1".equals(userType)) {
			sql.append(" and b.rela_dev_type_id = " + userType); // 终端类型
		}
		if (!StringUtil.IsEmpty(starttime1)) {
			sql.append(" and d.onlinedate >= ").append(starttime1);
		}
		if (!StringUtil.IsEmpty(endtime1)) {
			sql.append(" and d.onlinedate <= ").append(endtime1);
		}
		if (!StringUtil.IsEmpty(cityId) && !"00".equals(cityId)) { // 设备属地
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and a.city_id in (")
					.append(StringUtils.weave(cityIdList)).append(")");
			cityIdList = null;
		}
		if ("1".equals(flag)) { // 如果规范版本
			sql.append(" and b.is_check = 1");
		} else if ("2".equals(flag)) {// 如果是不规范版本
			sql.append(" and b.is_check <> 1");
		}
		if (!StringUtil.IsEmpty(gw_type)) {
			sql.append(" and a.gw_type= ").append(gw_type);
		}
		if (!"-1".equals(isActive)) {
			sql.append(" and d.is_active= ").append(isActive);
		}
		PrepareSQL psql = new PrepareSQL(sql.toString());

		cityMap = CityDAO.getCityIdCityNameMap();
		venderMap = getVenderMap();
		deviceModelMap = getDeviceModelMap();

		List<Map> list = jt.query(psql.getSQL(), new RowMapper() {
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {

				Map<String, String> map = new HashMap<String, String>();

				// 设备ID
				map.put("device_id", rs.getString("device_id"));

				// 设备厂商
				map.put("vendorName", venderMap.get(rs.getString("vendor_id")));

				// 设备型号 设备版本
				String devicetype_id = rs.getString("devicetype_id");
				if (false == StringUtil.IsEmpty(devicetype_id)) {

					String[] tmp = (String[]) deviceModelMap.get(devicetype_id);
					if (tmp != null && tmp.length == 2) {
						map.put("devicemodel", tmp[0]);
						map.put("softwareversion", tmp[1]);
					} else {
						map.put("devicemodel", "");
						map.put("softwareversion", "");
					}
				} else {
					map.put("devicemodel", "");
					map.put("softwareversion", "");
				}

				// 设备属地
				String city_id = rs.getString("city_id");
				if (false == StringUtil.IsEmpty(city_id)) {
					String city_name = StringUtil.getStringValue(cityMap
							.get(city_id));
					if (false == StringUtil.IsEmpty(city_name)) {
						map.put("city_name", city_name);
					} else {
						map.put("city_name", "");
					}
				} else {
					map.put("city_name", "");
				}

				String city_name = StringUtil.getStringValue(cityMap
						.get(city_id));
				if (false == StringUtil.IsEmpty(city_name)) {
					map.put("city_name", city_name);
				} else {
					map.put("city_name", "");
				}

				// 设备序列号
				map.put("deviceSerialnumber",
						rs.getString("device_serialnumber"));
				// 设备逻辑SN
				map.put("deviceIdEx", rs.getString("device_id_ex"));
				// 终端类型
				map.put("device_type", rs.getString("device_type"));

				// 设备注册时间 将complete_time转换成时间
				try {
					long completeTime = StringUtil.getLongValue(rs
							.getString("complete_time"));
					DateTimeUtil dt = new DateTimeUtil(completeTime * 1000);
					map.put("completeTime", dt.getLongDate());
				} catch (NumberFormatException e) {
					map.put("completeTime", "");
				} catch (Exception e) {
					map.put("completeTime", "");
				}

				// 设备最近上报时间 将last_time转换成时间
				try {
					long lastTime = StringUtil.getLongValue(rs
							.getString("last_time"));
					DateTimeUtil dt = new DateTimeUtil(lastTime * 1000);
					map.put("lastTime", dt.getLongDate());
				} catch (NumberFormatException e) {
					map.put("lastTime", "");
				} catch (Exception e) {
					map.put("lastTime", "");
				}

				return map;
			}
		});

		cityMap = null;
		venderMap = null;
		deviceModelMap = null;

		return list;
	}

	/**
	 * 取得所有vendorId和厂商名对应的MAP
	 * 
	 * @param request
	 * @return ArrayList
	 */
	public HashMap<String, String> getVenderMap() {

		logger.debug("getVenderMap()");

		StringBuffer sql = new StringBuffer(
				"select vendor_id,vendor_name,vendor_add from tab_vendor");

		PrepareSQL psql = new PrepareSQL(sql.toString());

		List<Map> list = jt.queryForList(psql.getSQL());

		HashMap<String, String> vendorMap = new HashMap<String, String>();

		for (Map<String, String> map : list) {

			String vendorAdd = StringUtil.getStringValue(map.get("vendor_add"));

			if (!StringUtil.IsEmpty(vendorAdd)) {
				vendorMap.put(StringUtil.getStringValue(map.get("vendor_id")),
						vendorAdd);
			} else {
				vendorMap.put(StringUtil.getStringValue(map.get("vendor_id")),
						StringUtil.getStringValue(map.get("vendor_name")));
			}
		}
		return vendorMap;
	}

	/**
	 * 获取设备型号版本map
	 * 
	 * @param request
	 * @return ArrayList
	 */
	public HashMap<String, Object> getDeviceModelMap() {

		logger.debug("getDeviceModelMap()");

		String devicemodel = "";
		String softwareversion = "";
		String devicetype_id = "";
		HashMap<String, Object> deviceTypeMap = new HashMap<String, Object>();

		StringBuffer sql = new StringBuffer();
		sql.append("select a.device_model_id,a.device_model,b.softwareversion,b.devicetype_id");
		sql.append("  from gw_device_model a, tab_devicetype_info b ");
		sql.append(" where a.device_model_id=b.device_model_id ");

		PrepareSQL psql = new PrepareSQL(sql.toString());
		List<Map> list = jt.queryForList(psql.getSQL());

		for (Map<String, String> map : list) {
			devicetype_id = StringUtil.getStringValue(map.get("devicetype_id"));
			devicemodel = StringUtil.getStringValue(map.get("device_model"));
			softwareversion = StringUtil.getStringValue(map.get("softwareversion"));

			// 型号，软件版本数组，modelsoft[0]为型号，modelsoft[1]为软件版本
			String[] modelsoft = new String[2];
			modelsoft[0] = devicemodel;
			modelsoft[1] = softwareversion;

			deviceTypeMap.put(devicetype_id, modelsoft);
		}

		return deviceTypeMap;
	}
}
