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
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.gw.VendorModelVersionDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.util.StringUtil;

/**
 * 考核所有的用户
 * 
 * @author 王森博
 */
@SuppressWarnings({"unchecked","rawtypes"})
public class SoftUpResultReportDAO extends SuperDAO
{
	private static Logger logger = LoggerFactory.getLogger(SoftUpResultReportDAO.class);
	/** Map<city_id,city_name> */
	private Map<String, String> cityMap = null;
	/** HashMap<vendor_id,vendor_add> */
	private HashMap<String, String> vendorMap = null;
	/** HashMap<device_model_id,device_model> */
	private HashMap<String, String> deviceModelMap = null;
	private HashMap<String, String> devicetypeMap = null;
	private Map<String, Map<String,String>> deviceTypeInfoMap = null;

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
		if("1".equals(isSoftUp)){
			tableName = "gw_serv_strategy_soft";
		}
		
		if(DBUtil.GetDB()==3){
			sql.append("select b.city_id,count(*) as total ");
		}else{
			sql.append("select b.city_id,count(1) as total ");
		}
		
		sql.append("from " + tableName + " a,tab_gw_device b ");
		sql.append("where a.device_id=b.device_id and a.service_id=5 and a.is_last_one=1 ");
		if (!StringUtil.IsEmpty(cityId)&&!"00".equals(cityId))
		{
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and b.city_id in (").append(StringUtils.weave(cityIdList)+")");
			cityIdList = null;
		}
		if (!StringUtil.IsEmpty(starttime1)){
			sql.append(" and a.time>=").append(starttime1);
		}
		if (!StringUtil.IsEmpty(endtime1)){
			sql.append(" and a.time<=").append(endtime1);
		}
		if(!StringUtil.IsEmpty(gw_type)){
			sql.append(" and b.gw_type=").append(gw_type);
		}
		sql.append(" group by b.city_id");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		List list = jt.queryForList(psql.getSQL());
		if (list!=null && !list.isEmpty())
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

//	public Map<String, Map<String,String>> countSucc(String starttime1, String endtime1, String cityId)
//	{
//		logger.debug("countSucc({},{},{})", new Object[] { starttime1, endtime1, cityId });
//		
//		StringBuffer sql = new StringBuffer();
//		sql.append("select b.city_id,c.is_mgr,count(1) as total from gw_serv_strategy a,tab_gw_device b,cpe_mgr c ");
//		sql.append("where a.device_id=b.device_id and a.device_id=c.device_id and a.service_id=5 and a.is_last_one=1 and a.status=100 and a.result_id=1 ");	
//		if (false == StringUtil.IsEmpty(cityId)||"00".equals(cityId))
//		{
//			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
//			sql.append(" and b.city_id in (").append(StringUtils.weave(cityIdList)+")");
//			cityIdList = null;
//		}
//		if (false == StringUtil.IsEmpty(starttime1)){
//			sql.append(" and a.time>=").append(starttime1);
//		}
//		if (false == StringUtil.IsEmpty(endtime1)){
//			sql.append(" and a.time<=").append(endtime1);
//		}
//		sql.append(" group by b.city_id,c.is_mgr");
//		List list = jt.queryForList(sql.toString());
//		Map<String, Map<String,String>> map = new HashMap<String, Map<String,String>>();
//		if (false == list.isEmpty())
//		{
//			for (int i = 0; i < list.size(); i++)
//			{
//				Map rmap = (Map) list.get(i);
//				String city_id = StringUtil.getStringValue(rmap.get("city_id"));
//				Map tmap = map.get(city_id);
//				if(tmap==null){
//					tmap = new HashMap<String, String>();				
//				}
//				tmap.put(StringUtil.getStringValue(rmap.get("is_mgr")), StringUtil
//						.getStringValue(rmap.get("total")));
//				map.put(city_id, tmap);
//			}
//		}
//		return map;
//	}

	
	public List countResult(String starttime1,String endtime1,String cityId,
			String gw_type,String isSoftUp,String vendorId,String deviceModelId)
	{
		logger.debug("countResult({},{},{})",
				new Object[] { starttime1, endtime1, cityId });
		StringBuffer sql = new StringBuffer();
		String tableName = "gw_serv_strategy";
		
//		if(LipossGlobals.inArea("js_dx")){
//			tableName = "gw_serv_strategy";
//		}else if(LipossGlobals.inArea("ah_dx")){
//			tableName = "gw_serv_strategy_soft";
//		}
//暂时失效
		if("1".equals(isSoftUp)){
			tableName = "gw_serv_strategy_soft";
		}
		
		if(DBUtil.GetDB()==3){
			sql.append("select b.city_id,a.status,a.result_id,count(*) as total ");
		}else{
			sql.append("select b.city_id,a.status,a.result_id,count(1) as total ");
		}
		
		sql.append("from " + tableName + " a,tab_gw_device b ");
		sql.append("where a.device_id=b.device_id and a.is_last_one=1 and a.service_id=5 ");
		if (!StringUtil.IsEmpty(cityId) && !"00".equals(cityId))
		{
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and city_id in ("+StringUtils.weave(cityIdList)+")");
			cityIdList = null;
		}
		if (!StringUtil.IsEmpty(starttime1)){
			sql.append(" and a.time>=").append(starttime1);
		}
		if (!StringUtil.IsEmpty(endtime1)){
			sql.append(" and a.time<=").append(endtime1);
		}
		if(!StringUtil.IsEmpty(gw_type)){
			sql.append(" and b.gw_type=").append(gw_type);
		}
		if(!StringUtil.IsEmpty(vendorId) && !"-1".equals(vendorId)){
			sql.append(" and b.vendor_id='").append(vendorId).append("'");
		}
		if(!StringUtil.IsEmpty(deviceModelId) && !"-1".equals(deviceModelId)){
			sql.append(" and b.device_model_id='").append(deviceModelId).append("'");
		}
		sql.append(" group by b.city_id,a.status,a.result_id");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		return jt.queryForList(psql.getSQL());
	}

	public List<Map> getDevList(String gw_type, String starttime1, String endtime1, String cityId,
			String status, String resultId, String isMgr, int curPage_splitPage,
			int num_splitPage, String isSoftUp,String vendorId,String deviceModelId)
	{
		logger.debug("getDevList({},{},{},{},{},{},{},{})", new Object[] { starttime1,
				endtime1, cityId, status, resultId, isMgr, curPage_splitPage,
				num_splitPage });
		String tableName = "gw_serv_strategy";
		
//		if(LipossGlobals.inArea("js_dx")){
//			tableName = "gw_serv_strategy";
//		}else if(LipossGlobals.inArea("ah_dx")){
//			tableName = "gw_serv_strategy_soft";
//		}
		
		if("1".equals(isSoftUp))
		{
			tableName = "gw_serv_strategy_soft";
		}
		
		StringBuffer sql = new StringBuffer();

		if(LipossGlobals.inArea(Global.JXDX)){
			//新增字段：硬件版本、接入类型、终端支持速率、注册系统时间、最近更新时间、用户帐号
			sql.append("select b.device_id,b.city_id,b.vendor_id,b.devicetype_id,b.device_model_id,b.oui,b.device_serialnumber,b.loopback_ip," +
					"c.fault_desc,f.device_version_type,e.device_sn,f.gbbroadband,b.complete_time,h.username,s.last_time,g.type_name ");
			sql.append(" from gw_serv_strategy_soft a left join tab_gw_device b on a.device_id=b.device_id left join tab_hgwcustomer h on b.device_id = h.device_id " +
					"left join tab_cpe_faultcode c on a.result_id=c.fault_code left join gw_devicestatus s on b.device_id = s.device_id " +
					"left join tab_gw_ht_megabytes e on b.device_name = e.device_sn left join tab_device_version_attribute f on b.devicetype_id = f.devicetype_id " +
					"left join gw_access_type g on h.access_style_id = g.type_id ");
			sql.append("where a.service_id=5 and a.is_last_one=1 ");
		}else {
			sql.append("select b.device_id,b.city_id,b.vendor_id,b.devicetype_id,b.device_model_id,b.oui,b.device_serialnumber,b.loopback_ip,c.fault_desc ");
			sql.append("from "+tableName + " a,tab_gw_device b,tab_cpe_faultcode c ");
			sql.append("where a.result_id=c.fault_code and a.device_id=b.device_id and a.service_id=5 and a.is_last_one=1 ");
		}

		if (false == StringUtil.IsEmpty(starttime1)){
			sql.append(" and a.time>=").append(starttime1);
		}
		if (false == StringUtil.IsEmpty(endtime1)){
			sql.append(" and a.time<=").append(endtime1);
		}
		if (false == StringUtil.IsEmpty(cityId)&&!"00".equals(cityId))
		{
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and b.city_id in (").append(StringUtils.weave(cityIdList)+")");
			cityIdList = null;
		}
		if (false == StringUtil.IsEmpty(status)){
			if (status.equals("1234")) {
				sql.append(" and (a.status=1 or status=2 or status=3 or status=4) ");
			}else {
				if("0".equals(status) && StringUtil.IsEmpty(resultId) 
						&& LipossGlobals.inArea(Global.SDDX))
				{
					sql.append(" and ( (a.status=1 or a.status=2 or a.status=3 or a.status=4) ");
					sql.append("or (a.status=0 and a.result_id=0) ) ");
				}else{
					sql.append(" and a.status=").append(status);
				}
			}
		}
		if(!StringUtil.IsEmpty(gw_type)){
			sql.append(" and b.gw_type=").append(gw_type);
		}
		if (false == StringUtil.IsEmpty(resultId))
		{
			if ("not1".equals(resultId))
			{
				if(LipossGlobals.inArea(Global.JSDX)){
					//新增加的判断的页面
					if("100".equals(status)){
						sql.append(" and a.result_id=-1");
					}
				}else{
					if("100".equals(status)){
						sql.append(" and a.result_id!=1");
					}else if("0".equals(status)){
						sql.append(" and a.result_id!=1 and a.result_id!=0");
					}
				}
			}
			else
			{
				if (LipossGlobals.inArea(Global.JSDX) && "1".equals(resultId)){
					sql.append(" and a.result_id != -1");//江苏，不等于-1都算成功
				}else{
					sql.append(" and a.result_id=").append(resultId);
				}
			}
		}
		
		if(!StringUtil.IsEmpty(vendorId) && !"-1".equals(vendorId)){
			sql.append(" and b.vendor_id='").append(vendorId).append("'");
		}
		if(!StringUtil.IsEmpty(deviceModelId) && !"-1".equals(deviceModelId)){
			sql.append(" and b.device_model_id='").append(deviceModelId).append("'");
		}
		PrepareSQL psql = new PrepareSQL(sql.toString());
		cityMap = CityDAO.getCityIdCityNameMap();
		vendorMap = VendorModelVersionDAO.getVendorMap();
		deviceModelMap = VendorModelVersionDAO.getDeviceModelMap();
		if(LipossGlobals.inArea(Global.JXDX)){
			//新增字段 JXDX-REQ-ITMS-20201223-WWF-001(江西电信ITMS+家庭网关软件升级报表优化需求)
			deviceTypeInfoMap = VendorModelVersionDAO.getDeviceTypeSomeMap();
		}else {
			devicetypeMap = VendorModelVersionDAO.getDevicetypeMap();
		}
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
				map.put("vendor_id", vendor_id);
				String vendor_add = StringUtil.getStringValue(vendorMap.get(vendor_id));
				if (!StringUtil.IsEmpty(vendor_add)){
					map.put("vendor_add", vendor_add);
				}else{
					map.put("vendor_add", "");
				}
				String device_model_id = rs.getString("device_model_id");
				map.put("device_model_id", device_model_id);
				String device_model = StringUtil.getStringValue(deviceModelMap.get(device_model_id));
				if (!StringUtil.IsEmpty(device_model)){
					map.put("device_model", device_model);
				}else{
					map.put("device_model", "");
				}
				String devicetype_id = rs.getString("devicetype_id");
				if(LipossGlobals.inArea(Global.JXDX)) {
					//注册系统时间
					setCompeteTime(rs, map);
					//最近更新时间
					setLastTime(rs,map);
					//用户帐号
					map.put("loid",rs.getString("username"));
					//终端支持速率
					setTerminalRate(rs, map);
					//软件版本 硬件版本 计入类型
					setDevTypeSome(rs, map, devicetype_id);
				} else {
					String softwareversion = StringUtil.getStringValue(devicetypeMap.get(devicetype_id));
					if (!StringUtil.IsEmpty(softwareversion)){
						map.put("softwareversion", softwareversion);
					}else{
						map.put("softwareversion", "");
					}
				}

				map.put("oui", rs.getString("oui"));
				map.put("device_serialnumber", rs.getString("device_serialnumber"));
				map.put("device", rs.getString("oui") + "-"
						+ rs.getString("device_serialnumber"));
				map.put("loopback_ip", rs.getString("loopback_ip"));
				map.put("fault_desc", rs.getString("fault_desc"));
				return map;
			}
		});
		cityMap = null;
		vendorMap = null;
		deviceModelMap = null;
		return list;
	}

	private void setDevTypeSome(ResultSet rs, Map<String, String> map, String devicetype_id) throws SQLException {
		Map<String, String> devTypeMap = deviceTypeInfoMap.get(devicetype_id);
		if (devTypeMap == null || devTypeMap.size() == 0) {
			map.put("softwareversion", "");
			map.put("hardwareversion", "");
			map.put("accessTypeStr", rs.getString("type_name") == null ? "" : rs.getString("type_name"));
		} else {
			map.put("softwareversion", !StringUtil.IsEmpty(devTypeMap.get("softwareversion")) ? devTypeMap.get("softwareversion") : "");
			map.put("hardwareversion", !StringUtil.IsEmpty(devTypeMap.get("hardwareversion")) ? devTypeMap.get("hardwareversion") : "");
			String accessType = devTypeMap.get("accessId");
			if(!StringUtil.IsEmpty(accessType) && accessType.equals("3")){
				map.put("accessTypeStr", "EPON");
			}else if( !StringUtil.IsEmpty(accessType) && accessType.equals("4")){
				map.put("accessTypeStr", "GPON");
			}else {
				map.put("accessTypeStr", rs.getString("type_name") == null ? "" : rs.getString("type_name"));
			}
		}
		//接入类型 优先取 表tab_device_version_attribute的device_version_type字段
		String devVersionType = rs.getString("device_version_type");
		if (!StringUtil.IsEmpty(devVersionType) && devVersionType.equals("4")) {
			map.put("accessTypeStr", "10GEPON");
		} else if (!StringUtil.IsEmpty(devVersionType) && devVersionType.equals("5")) {
			map.put("accessTypeStr", "XGPON");
		}
	}

	private void setTerminalRate(ResultSet rs, Map<String, String> map) throws SQLException {
		//表tab_gw_ht_megabytes为手工维护的千兆设备表 该表中未匹配到设备 则正常从设备属性表中的gbbroadband字段获取
		if(StringUtil.isNotEmpty(rs.getString("device_sn"))){
			map.put("terminalRate","千兆");
		}else {
			String gbbroadband = rs.getString("gbbroadband");
			if(StringUtil.isNotEmpty(gbbroadband) && gbbroadband.equals("1")){
				map.put("terminalRate","千兆");
			}else if(StringUtil.isNotEmpty(gbbroadband) && gbbroadband.equals("2")){
				map.put("terminalRate","百兆");
			}else if(StringUtil.isNotEmpty(gbbroadband) && gbbroadband.equals("3")){
				map.put("terminalRate","万兆");
			}
		}
	}

	private void setCompeteTime(ResultSet rs, Map<String, String> map) throws SQLException {
		try{
			long completeTime = StringUtil.getLongValue(rs.getString("complete_time"));
			DateTimeUtil dt = new DateTimeUtil(completeTime * 1000);
			map.put("completeTime", dt.getLongDate());
		}catch (NumberFormatException e){
			logger.error("timesec:{},",rs.getString("complete_time"),e);
			map.put("completeTime", "");
		}catch (Exception e){
			logger.error("timesec:{},",rs.getString("complete_time"),e);
			map.put("completeTime", "");
		}
	}

	private void setLastTime(ResultSet rs, Map<String, String> map) throws SQLException {
		try{
			long lastTime = StringUtil.getLongValue(rs.getString("last_time"));
			DateTimeUtil dt = new DateTimeUtil(lastTime * 1000);
			map.put("lastTime", dt.getLongDate());
		}catch (NumberFormatException e){
			logger.error("timesec:{},",rs.getString("last_time"),e);
			map.put("lastTime", "");
		}catch (Exception e){
			logger.error("timesec:{},",rs.getString("last_time"),e);
			map.put("lastTime", "");
		}
	}
	public int getDevCount(String gw_type, String starttime1, String endtime1, String cityId,
			String status, String resultId, String isMgr, int curPage_splitPage,
			int num_splitPage, String isSoftUp,String vendorId,String deviceModelId)
	{
		logger.debug("getDevList({},{},{},{},{},{},{},{})", new Object[] { starttime1,
				endtime1, cityId, status, resultId, isMgr, curPage_splitPage,
				num_splitPage });
		StringBuffer sql = new StringBuffer();
		String tableName = "gw_serv_strategy";
		
//		if(LipossGlobals.inArea("js_dx")){
//			tableName = "gw_serv_strategy";
//		}else if(LipossGlobals.inArea("ah_dx")){
//			tableName = "gw_serv_strategy_soft";
//		}
		
		if("1".equals(isSoftUp)){
			tableName = "gw_serv_strategy_soft";
		}
		
		sql.append("select count(1) from " + tableName + " a,tab_gw_device b ");
		sql.append("where a.device_id=b.device_id and a.service_id=5 and a.is_last_one=1 ");
		if (!StringUtil.IsEmpty(starttime1)){
			sql.append(" and a.time>=").append(starttime1);
		}
		if (!StringUtil.IsEmpty(endtime1)){
			sql.append(" and a.time<=").append(endtime1);
		}
		if (!StringUtil.IsEmpty(cityId)&&!"00".equals(cityId)){
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and b.city_id in ("+StringUtils.weave(cityIdList)+")");
			cityIdList = null;
		}
		if (!StringUtil.IsEmpty(status))
		{
			if (status.equals("1234")) {
				sql.append(" and (a.status=1 or status=2 or status=3 or status=4) ");
			}else {
				if("0".equals(status) && StringUtil.IsEmpty(resultId) 
						&& LipossGlobals.inArea(Global.SDDX))
				{
					sql.append(" and ( (a.status=1 or a.status=2 or a.status=3 or a.status=4) ");
					sql.append("or (a.status=0 and a.result_id=0) ) ");
				}else{
					sql.append(" and a.status=").append(status);
				}
			}
		}
		if(!StringUtil.IsEmpty(gw_type)){
			sql.append(" and b.gw_type=").append(gw_type);
		}
		if (!StringUtil.IsEmpty(resultId))
		{
			if ("not1".equals(resultId))
			{
				if(LipossGlobals.inArea(Global.JSDX))
				{
					//新增加的判断的页面
					if("100".equals(status)){
						sql.append(" and a.result_id=-1");
					}
				}
				else
				{
					if("100".equals(status)){
						sql.append(" and a.result_id!=1");
					}else if("0".equals(status)){
						sql.append(" and a.result_id!=1 and a.result_id!=0");
					}
				}
			}
			else
			{
				if (LipossGlobals.inArea(Global.JSDX) && "1".equals(resultId)){
					sql.append(" and a.result_id != -1");//江苏，不等于-1都算成功
				}else{
					sql.append(" and a.result_id=").append(resultId);
				}
			}
		}
		if(!StringUtil.IsEmpty(vendorId) && !"-1".equals(vendorId)){
			sql.append(" and b.vendor_id='").append(vendorId).append("'");
		}
		if(!StringUtil.IsEmpty(deviceModelId) && !"-1".equals(deviceModelId)){
			sql.append(" and b.device_model_id='").append(deviceModelId).append("'");
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

	public List<Map> getDevExcel(String gw_type,String starttime1, String endtime1, String cityId,
			String status, String resultId, String isMgr, String isSoftUp,String vendorId,String deviceModelId)
	{
		logger.debug("getDevExcel({},{},{},{},{},{})", new Object[] { starttime1,
				endtime1, cityId, status, resultId, isMgr });
		StringBuffer sql = new StringBuffer();
		String tableName = "gw_serv_strategy";
		
//		if(LipossGlobals.inArea("js_dx")){
//			tableName = "gw_serv_strategy";
//		}else if(LipossGlobals.inArea("ah_dx")){
//			tableName = "gw_serv_strategy_soft";
//		}
		
		if("1".equals(isSoftUp)){
			tableName = "gw_serv_strategy_soft";
		}

		if(LipossGlobals.inArea(Global.JXDX)){
			//新增字段：硬件版本、接入类型、终端支持速率、注册系统时间、最近更新时间、用户帐号
			sql.append("select b.device_id,b.city_id,b.vendor_id,b.devicetype_id,b.device_model_id,b.oui,b.device_serialnumber,b.loopback_ip," +
					"c.fault_desc,f.device_version_type,e.device_sn,f.gbbroadband,b.complete_time,h.username,s.last_time,g.type_name ");
			sql.append(" from gw_serv_strategy_soft a left join tab_gw_device b on a.device_id=b.device_id left join tab_hgwcustomer h on b.device_id = h.device_id " +
					"left join tab_cpe_faultcode c on a.result_id=c.fault_code left join gw_devicestatus s on b.device_id = s.device_id " +
					"left join tab_gw_ht_megabytes e on b.device_name = e.device_sn left join tab_device_version_attribute f on b.devicetype_id = f.devicetype_id " +
					"left join gw_access_type g on h.access_style_id = g.type_id ");
			sql.append("where a.service_id=5 and a.is_last_one=1 ");
		}else {
			sql.append("select b.device_id,b.city_id,b.vendor_id,b.devicetype_id,b.device_model_id,b.oui,b.device_serialnumber,b.loopback_ip,c.fault_desc ");
			sql.append("from " + tableName + " a,tab_gw_device b,tab_cpe_faultcode c ");
			sql.append("where a.result_id=c.fault_code and a.device_id=b.device_id and a.service_id=5 and a.is_last_one=1 ");
		}

		if (!StringUtil.IsEmpty(starttime1)){
			sql.append(" and a.time>=").append(starttime1);
		}
		if (!StringUtil.IsEmpty(endtime1)){
			sql.append(" and a.time<=").append(endtime1);
		}
		if (!StringUtil.IsEmpty(cityId)&&!"00".equals(cityId)){
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and b.city_id in ("+StringUtils.weave(cityIdList)+")");
			cityIdList = null;
		}
		if (!StringUtil.IsEmpty(status))
		{
			if (status.equals("1234")) {
				sql.append(" and (a.status=1 or status=2 or status=3 or status=4) ");
			}else {
				if("0".equals(status) && StringUtil.IsEmpty(resultId) 
						&& LipossGlobals.inArea(Global.SDDX))
				{
					sql.append(" and ( (a.status=1 or a.status=2 or a.status=3 or a.status=4) ");
					sql.append("or (a.status=0 and a.result_id=0) ) ");
				}else{
					sql.append(" and a.status=").append(status);
				}
			}
		}
		if(!StringUtil.IsEmpty(gw_type)){
			sql.append(" and b.gw_type=").append(gw_type);
		}
		if (!StringUtil.IsEmpty(resultId))
		{
			if ("not1".equals(resultId))
			{
				if(LipossGlobals.inArea(Global.JSDX)){
					//新增加的判断的页面
					if("100".equals(status)){
						sql.append(" and a.result_id=-1");
					}
				}else{
					if("100".equals(status)){
						sql.append(" and a.result_id!=1");
					}else if("0".equals(status)){
						sql.append(" and a.result_id!=1 and a.result_id!=0");
					}
				}
			}
			else
			{
				if (LipossGlobals.inArea(Global.JSDX) && "1".equals(resultId)){
					sql.append(" and a.result_id != -1");////江苏，不等于-1都算成功
				}else{
					sql.append(" and a.result_id=").append(resultId);
				}
			}
		}
		if(!StringUtil.IsEmpty(vendorId) && !"-1".equals(vendorId)){
			sql.append(" and b.vendor_id='").append(vendorId).append("'");
		}
		if(!StringUtil.IsEmpty(deviceModelId) && !"-1".equals(deviceModelId)){
			sql.append(" and b.device_model_id='").append(deviceModelId).append("'");
		}
		PrepareSQL psql = new PrepareSQL(sql.toString());
		cityMap = CityDAO.getCityIdCityNameMap();
		vendorMap = VendorModelVersionDAO.getVendorMap();
		deviceModelMap = VendorModelVersionDAO.getDeviceModelMap();
		if(LipossGlobals.inArea(Global.JXDX)){
			//新增字段 JXDX-REQ-ITMS-20201223-WWF-001(江西电信ITMS+家庭网关软件升级报表优化需求)
			deviceTypeInfoMap = VendorModelVersionDAO.getDeviceTypeSomeMap();
		}else {
			devicetypeMap = VendorModelVersionDAO.getDevicetypeMap();
		}
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
				String vendor_add = StringUtil.getStringValue(vendorMap.get(vendor_id));
				if (!StringUtil.IsEmpty(vendor_add)){
					map.put("vendor_add", vendor_add);
				}else{
					map.put("vendor_add", "");
				}
				String device_model_id = rs.getString("device_model_id");
				String device_model = StringUtil.getStringValue(deviceModelMap.get(device_model_id));
				if (!StringUtil.IsEmpty(device_model)){
					map.put("device_model", device_model);
				}else{
					map.put("device_model", "");
				}
				String devicetype_id = rs.getString("devicetype_id");
				if(LipossGlobals.inArea(Global.JXDX)) {
					//注册系统时间
					setCompeteTime(rs, map);
					//最近更新时间
					setLastTime(rs,map);
					//用户帐号
					map.put("loid",rs.getString("username"));
					//终端支持速率
					setTerminalRate(rs, map);
					//软件版本 硬件版本 接入类型
					setDevTypeSome(rs, map, devicetype_id);
				} else {
					String softwareversion = StringUtil.getStringValue(devicetypeMap.get(devicetype_id));
					if (!StringUtil.IsEmpty(softwareversion)){
						map.put("softwareversion", softwareversion);
					}else{
						map.put("softwareversion", "");
					}
				}

				map.put("oui", rs.getString("oui"));
				map.put("device_serialnumber", rs.getString("device_serialnumber"));
				map.put("device", rs.getString("oui") + "-"+ rs.getString("device_serialnumber"));
				map.put("loopback_ip", rs.getString("loopback_ip"));
				map.put("fault_desc", rs.getString("fault_desc"));
				return map;
			}
		});
		
		cityMap = null;
		vendorMap = null;
		deviceModelMap = null;
		logger.debug("devList:"+list.toString());
		return list;
	}

	public static HashMap<String, String> getAccessTypeMap()
	{
		logger.debug("getAccessTypeMap()");
		HashMap<String, String> mapObj = new HashMap<String, String>();
		logger.info("select type_id,type_name from gw_access_type");
		Cursor cursor = DataSetBean.getCursor("select type_id,type_name from gw_access_type");
		Map fields = cursor.getNext();
		if (fields == null){
			logger.debug("设备接入类型表没数据");
		}else{
			while (fields != null)
			{
				mapObj.put(String.valueOf(fields.get("type_id")), (String) fields.get("type_name"));
				fields = cursor.getNext();
			}
		}
		return mapObj;
	}
}
