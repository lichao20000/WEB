
package com.linkage.module.gtms.stb.resource.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.util.StringUtil;

/**
 * 机顶盒策略或软件升级统计
 */
@SuppressWarnings("rawtypes")
public class StbSoftUpResultReportDAO extends SuperDAO
{
	private static Logger logger = LoggerFactory.getLogger(StbSoftUpResultReportDAO.class);
	/** Map<city_id,city_name>*/
	private static Map<String, String> cityMap = null;
	/** HashMap<vendor_id,vendor_add>*/
	private static HashMap<String, String> vendorMap = null;
	/** HashMap<device_model_id,device_model>*/
	private static HashMap<String, String> deviceModelMap = null;
	private static HashMap<String, String> devicetypeMap = null;

	static {
		cityMap = CityDAO.getCityIdCityNameMap();
		vendorMap = getVendorMap();
		deviceModelMap = getDeviceModelMap();
		devicetypeMap = getDevicetypeMap();
	}

	/**
	 * 所有配置的
	 * 由于数据库数据量较大，两次查询数据库很耗时，故采用countResult方法来计算出总数
	 */

	public Map countAll(String starttime1, String endtime1, String cityId,String gw_type,String isSoftUp)
	{
		logger.debug("countAll({},{},{})", new Object[] { starttime1, endtime1, cityId });
		Map<String, String> map = new HashMap<String, String>();
		StringBuffer sql = new StringBuffer();
		String tableName = "gw_serv_strategy";
		if("1".equals(isSoftUp))
		{
			tableName = "gw_serv_strategy_soft";
		}
		sql.append("select b.city_id,count(*) as total from " + tableName + " a,tab_gw_device b ");
		sql.append("where a.device_id=b.device_id and a.service_id=5 and a.is_last_one=1 ");

		if (!StringUtil.IsEmpty(cityId) && !"00".equals(cityId) && !"-1".equals(cityId))
		{
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and b.city_id in (").append(StringUtils.weave(cityIdList)).append(")");
			cityIdList = null;
		}
		if (!StringUtil.IsEmpty(starttime1))
		{
			sql.append(" and a.time>=").append(starttime1);
		}
		if (!StringUtil.IsEmpty(endtime1))
		{
			sql.append(" and a.time<=").append(endtime1);
		}
		if(!StringUtil.IsEmpty(gw_type)){
			sql.append(" and b.gw_type=").append(gw_type);
		}
		sql.append(" group by b.city_id");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		List list = jt.queryForList(psql.getSQL());
		if (!list.isEmpty())
		{
			for (int i = 0; i < list.size(); i++)
			{
				Map rmap = (Map) list.get(i);
				map.put(StringUtil.getStringValue(rmap.get("city_id")),
						StringUtil.getStringValue(rmap.get("total")));
			}
		}
		return map;
	}



	public List countResult(String starttime1, String endtime1, String cityId, String isSoftUp,
			String vendorId,String modelId) {
		logger.debug("countResult({},{},{})", new Object[] { starttime1, endtime1, cityId });
		StringBuffer sql = new StringBuffer();
		String tableName = "stb_gw_serv_strategy";

		if("1".equals(isSoftUp)) {
			tableName = "stb_gw_strategy_soft";
		}
		sql.append("select b.city_id, a.status, a.result_id, count(*) as total ");
		sql.append("from " + tableName + " a,stb_tab_gw_device b ");
		sql.append("where a.device_id=b.device_id and a.is_last_one=1 and a.service_id=5 ");

		if (!StringUtil.IsEmpty(cityId) && !"00".equals(cityId) && !"-1".equals(cityId)) {
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and b.city_id in (").append(StringUtils.weave(cityIdList)).append( ")");
			cityIdList = null;
		}
		if (!StringUtil.IsEmpty(vendorId) && !"-1".equals(vendorId)) {
			sql.append(" and b.vendor_id='"+vendorId+"'");
		}
		if (!StringUtil.IsEmpty(modelId) && !"-1".equals(modelId)) {
			sql.append(" and b.device_model_id='"+modelId+"'");
		}
		if (!StringUtil.IsEmpty(starttime1)) {
			sql.append(" and a.time >= "+starttime1);
		}
		if (!StringUtil.IsEmpty(endtime1)) {
			sql.append(" and a.time <= "+endtime1);
		}
		sql.append(" group by b.city_id,a.status,a.result_id");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		return jt.queryForList(psql.getSQL());
	}

	public List<Map> getDevList(String starttime1, String endtime1, String cityId,
			String status, String resultId, int curPage_splitPage,
			int num_splitPage, String isSoftUp,String vendorId,String modelId) {
		logger.debug("getDevList({},{},{},{},{},{},{})", new Object[] { starttime1,
				endtime1, cityId, status, resultId, curPage_splitPage, num_splitPage });
		String tableName = "stb_gw_serv_strategy";

		if("1".equals(isSoftUp)) {
			tableName = "stb_gw_strategy_soft";
		}

		StringBuffer sql = new StringBuffer();// TODO wait (more table related)
		sql.append("select b.device_id,b.city_id,b.vendor_id,b.devicetype_id,");
		sql.append("b.device_model_id,b.oui,b.device_serialnumber,b.loopback_ip,c.fault_desc ");
		sql.append("from " + tableName + " a,stb_tab_gw_device b,tab_cpe_faultcode c ");
		sql.append("where a.result_id=c.fault_code and a.device_id=b.device_id ");
		sql.append("and a.service_id=5 and a.is_last_one=1 ");

		if (!StringUtil.IsEmpty(starttime1)) {
			sql.append(" and a.time >= ").append(starttime1);
		}
		if (!StringUtil.IsEmpty(endtime1)) {
			sql.append(" and a.time <= ").append(endtime1);
		}
		if (!StringUtil.IsEmpty(cityId) && !"00".equals(cityId) && !"-1".equals(cityId)) {
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and b.city_id in (").append(StringUtils.weave(cityIdList)).append(")");
			cityIdList = null;
		}
		if (!StringUtil.IsEmpty(vendorId) && !"-1".equals(vendorId)) {
			sql.append(" and b.vendor_id='"+vendorId+"'");
		}
		if (!StringUtil.IsEmpty(modelId) && !"-1".equals(modelId)) {
			sql.append(" and b.device_model_id='"+modelId+"'");
		}
		if (!StringUtil.IsEmpty(status)) {
			sql.append(" and a.status = ").append(status);
		}
		if (!StringUtil.IsEmpty(resultId)) {
			if ("not1".equals(resultId)) {
				if("100".equals(status)){
					sql.append(" and a.result_id!=1");
				}else if("0".equals(status)){
					sql.append(" and a.result_id!=1 and a.result_id!=0");
				}
			}else {
				sql.append(" and a.result_id=").append(resultId);
			}
		}

		PrepareSQL psql = new PrepareSQL(sql.toString());
		List<Map> list = querySP(psql.getSQL(), (curPage_splitPage - 1) * num_splitPage
				+ 1, num_splitPage, new RowMapper()
		{
			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				Map<String, String> map = new HashMap<String, String>();
				map.put("device_id", rs.getString("device_id"));
				String city_id = rs.getString("city_id");
				String city_name = StringUtil.getStringValue(cityMap.get(city_id));
				if (!StringUtil.IsEmpty(city_name)){
					map.put("city_name", city_name);
				}else{
					map.put("city_name", "");
				}
				String vendor_id = rs.getString("vendor_id");
				map.put("vendorId", vendor_id);
				String vendor_add = StringUtil.getStringValue(vendorMap.get(vendor_id));
				if (!StringUtil.IsEmpty(vendor_add)){
					map.put("vendor_add", vendor_add);
				}else{
					map.put("vendor_add", "");
				}
				String device_model_id = rs.getString("device_model_id");
				map.put("modelId", device_model_id);
				String device_model = StringUtil.getStringValue(deviceModelMap.get(device_model_id));
				if (!StringUtil.IsEmpty(device_model)){
					map.put("device_model", device_model);
				}else{
					map.put("device_model", "");
				}
				String devicetype_id = rs.getString("devicetype_id");
				String softwareversion = StringUtil.getStringValue(devicetypeMap.get(devicetype_id));
				if (!StringUtil.IsEmpty(softwareversion)){
					map.put("softwareversion", softwareversion);
				}else{
					map.put("softwareversion", "");
				}
				map.put("oui", rs.getString("oui"));
				map.put("device_serialnumber", rs.getString("device_serialnumber"));
				map.put("device", rs.getString("oui") + "-" + rs.getString("device_serialnumber"));
				map.put("loopback_ip", rs.getString("loopback_ip"));
				map.put("fault_desc", rs.getString("fault_desc"));

				return map;
			}
		});

		return list;
	}

	public int getDevCount(String starttime1, String endtime1, String cityId,
			String status, String resultId, int curPage_splitPage,
			int num_splitPage, String isSoftUp,String vendorId,String modelId) {
		logger.debug("getDevList({},{},{},{},{},{},{},{})", new Object[] { starttime1,
				endtime1, cityId, status, resultId, curPage_splitPage,
				num_splitPage });

		String tableName = "stb_gw_serv_strategy";

		if("1".equals(isSoftUp)) {
			tableName = "stb_gw_strategy_soft";
		}

		StringBuffer sql = new StringBuffer();// TODO wait (more table related)
		sql.append("select count(*) from " + tableName + " a,stb_tab_gw_device b,tab_cpe_faultcode c ");
		sql.append("where a.result_id=c.fault_code and a.device_id=b.device_id ");
		sql.append("and a.service_id=5 and a.is_last_one=1 ");
		if (!StringUtil.IsEmpty(starttime1)) {
			sql.append(" and a.time >= ").append(starttime1);
		}
		if (!StringUtil.IsEmpty(endtime1)) {
			sql.append(" and a.time <= ").append(endtime1);
		}
		if (!StringUtil.IsEmpty(cityId) && !"00".equals(cityId) && !"-1".equals(cityId)) {
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and b.city_id in (").append(StringUtils.weave(cityIdList)).append(")");
			cityIdList = null;
		}
		if (!StringUtil.IsEmpty(vendorId) && !"-1".equals(vendorId)) {
			sql.append(" and b.vendor_id='"+vendorId+"'");
		}
		if (!StringUtil.IsEmpty(modelId) && !"-1".equals(modelId)) {
			sql.append(" and b.device_model_id='"+modelId+"'");
		}
		if (!StringUtil.IsEmpty(status)) {
			sql.append(" and a.status = ").append(status);
		}
		if (!StringUtil.IsEmpty(resultId)) {
			if ("not1".equals(resultId)) {
				if("100".equals(status)){
					sql.append(" and a.result_id!=1");
				}else if("0".equals(status)){
					sql.append(" and a.result_id!=1 and a.result_id!=0");
				}
			}else {
				sql.append(" and a.result_id=").append(resultId);
			}
		}

		PrepareSQL psql = new PrepareSQL(sql.toString());
		int total = jt.queryForInt(psql.getSQL());
		int maxPage = 1;
		if (total % num_splitPage == 0){
			maxPage = total / num_splitPage;
		}else{
			maxPage = total / num_splitPage + 1;
		}
		return maxPage;
	}



	@SuppressWarnings("unchecked")
	public List<Map> getDevExcel(String starttime1, String endtime1, String cityId,
			String status, String resultId, String isSoftUp,String vendorId,String modelId)
	{
		logger.debug("getDevExcel({},{},{},{},{})", new Object[] { starttime1,
				endtime1, cityId, status, resultId});
		String tableName = "stb_gw_serv_strategy";

		if("1".equals(isSoftUp)) {
			tableName = "stb_gw_strategy_soft";
		}

		StringBuffer sql = new StringBuffer();// TODO wait (more table related)
		sql.append("select b.device_id,b.city_id,b.vendor_id,b.devicetype_id,");
		sql.append("b.device_model_id,b.oui,b.device_serialnumber,b.loopback_ip,c.fault_desc ");
		sql.append("from " + tableName + " a,stb_tab_gw_device b,tab_cpe_faultcode c ");
		sql.append("where a.result_id=c.fault_code and a.device_id=b.device_id ");
		sql.append("and a.service_id=5 and a.is_last_one=1 ");
		if (!StringUtil.IsEmpty(starttime1)) {
			sql.append(" and a.time >= ").append(starttime1);
		}
		if (!StringUtil.IsEmpty(endtime1)) {
			sql.append(" and a.time <= ").append(endtime1);
		}
		if (!StringUtil.IsEmpty(cityId) && !"00".equals(cityId) && !"-1".equals(cityId)) {
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and b.city_id in (").append(StringUtils.weave(cityIdList)).append(")");
			cityIdList = null;
		}
		if (!StringUtil.IsEmpty(vendorId) && !"-1".equals(vendorId)) {
			sql.append(" and b.vendor_id='"+vendorId+"'");
		}
		if (!StringUtil.IsEmpty(modelId) && !"-1".equals(modelId)) {
			sql.append(" and b.device_model_id='"+modelId+"'");
		}
		if (!StringUtil.IsEmpty(status)) {
			sql.append(" and a.status = ").append(status);
		}
		if (!StringUtil.IsEmpty(resultId)) {
			if ("not1".equals(resultId)) {
				if("100".equals(status)){
					sql.append(" and a.result_id!=1");
				}else if("0".equals(status)){
					sql.append(" and a.result_id!=1 and a.result_id!=0");
				}
			}else {
				sql.append(" and a.result_id=").append(resultId);
			}
		}

		PrepareSQL psql = new PrepareSQL(sql.toString());
		List<Map> list = jt.query(psql.getSQL(), new RowMapper()
		{
			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				Map<String, String> map = new HashMap<String, String>();
				map.put("device_id", rs.getString("device_id"));
				String city_id = rs.getString("city_id");
				String city_name = StringUtil.getStringValue(cityMap.get(city_id));
				if (!StringUtil.IsEmpty(city_name)){
					map.put("city_name", city_name);
				}else{
					map.put("city_name", "");
				}
				String vendor_id = rs.getString("vendor_id");
				map.put("vendorId", vendor_id);
				String vendor_add = StringUtil.getStringValue(vendorMap.get(vendor_id));
				if (!StringUtil.IsEmpty(vendor_add)){
					map.put("vendor_add", vendor_add);
				}else{
					map.put("vendor_add", "");
				}
				String device_model_id = rs.getString("device_model_id");
				map.put("modelId", device_model_id);
				String device_model = StringUtil.getStringValue(deviceModelMap.get(device_model_id));
				if (!StringUtil.IsEmpty(device_model)){
					map.put("device_model", device_model);
				}else{
					map.put("device_model", "");
				}
				String devicetype_id = rs.getString("devicetype_id");
				String softwareversion = StringUtil.getStringValue(devicetypeMap.get(devicetype_id));
				if (!StringUtil.IsEmpty(softwareversion)){
					map.put("softwareversion", softwareversion);
				}else{
					map.put("softwareversion", "");
				}
				map.put("oui", rs.getString("oui"));
				map.put("device_serialnumber", rs.getString("device_serialnumber"));
				map.put("device", rs.getString("oui") + "-"+ rs.getString("device_serialnumber"));
				map.put("loopback_ip", rs.getString("loopback_ip"));
				map.put("fault_desc", rs.getString("fault_desc"));
				return map;
			}
		});

		return list;
	}

	/**
	 * 取得所有vendorId和厂商名对应的MAP
	 */
	public static HashMap<String, String> getVendorMap() {
		logger.debug("getVendorMap()");
		HashMap<String, String> vendorMap = new HashMap<String, String>();
		PrepareSQL psql = new PrepareSQL("select vendor_id, vendor_name, vendor_add from stb_tab_vendor");
		Cursor cursor = DataSetBean.getCursor(psql.getSQL());
		Map fields = cursor.getNext();
		while (fields != null)
		{
			String vendor_add = (String) fields.get("vendor_add");
			if (!StringUtil.IsEmpty(vendor_add)){
				vendorMap.put((String) fields.get("vendor_id"), vendor_add);
			}else{
				vendorMap.put((String) fields.get("vendor_id"), (String) fields.get("vendor_name"));
			}
			fields = cursor.getNext();
		}
		return vendorMap;
	}

	/**
	 * 取得device_model_id和设备型号对应的MAP
	 */
	public static HashMap<String, String> getDeviceModelMap()
	{
		logger.debug("getDeviceModelMap()");
		HashMap<String, String> deviceModelMap = new HashMap<String, String>();
		PrepareSQL psql = new PrepareSQL("select device_model_id,device_model from stb_gw_device_model");
		Cursor cursor = DataSetBean.getCursor(psql.getSQL());
		Map fields = cursor.getNext();
		while (fields != null)
		{
			deviceModelMap.put((String) fields.get("device_model_id"), (String) fields.get("device_model"));
			fields = cursor.getNext();
		}
		return deviceModelMap;
	}

	/**
	 * 取得devicetype_id和软件版本对应的MAP
	 */
	public static HashMap<String, String> getDevicetypeMap()
	{
		logger.debug("getDevicetypeMap()");
		HashMap<String, String> devicetypeMap = new HashMap<String, String>();
		PrepareSQL psql = new PrepareSQL("select devicetype_id,softwareversion from stb_tab_devicetype_info");
		Cursor cursor = DataSetBean.getCursor(psql.getSQL());
		Map fields = cursor.getNext();
		while (fields != null)
		{
			devicetypeMap.put((String) fields.get("devicetype_id"), (String) fields.get("softwareversion"));
			fields = cursor.getNext();
		}
		return devicetypeMap;
	}


}
