package com.linkage.module.gwms.report.dao;

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
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.util.StringUtil;

@SuppressWarnings({"unchecked","rawtypes"})
public class DeviceBindReportDAO extends SuperDAO
{
	private static Logger logger = LoggerFactory.getLogger(DeviceBindReportDAO.class);
	
	private Map<String, String> cityMap = null;
	private Map<String, String> userTypeMap = null;
	private Map<String, String> bindTypeMap = null;
	private Map<String, String> venderMap = null;
	private Map<String, Object> deviceModelMap = null;
	
	/**
	 * 统计已注册的设备
	 * 
	 * @param vendorId
	 * @param deviceTypeId
	 * @param deviceModelId
	 * @param cityId
	 * @param accessType
	 * @param usertype
	 * @param starttime1
	 * @param endtime1
	 * @return
	 */
	public Map<String, String> haveRegisteredDevice(String vendorId, String deviceTypeId,
			String deviceModelId, String cityId, String accessType,
			String usertype, String starttime1, String endtime1) 
	{
		
		logger.debug("haveRegisteredDevice({},{},{},{},{},{},{},{})", new Object[] {
				vendorId, deviceTypeId, deviceModelId, cityId, accessType,
				usertype, starttime1, endtime1 });
		
		Map<String, String> map = new HashMap<String, String>();
		StringBuffer sql = new StringBuffer();
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			sql.append("select a.city_id,count(*) as haveRegistTotal ");
		}else{
			sql.append("select a.city_id,count(1) as haveRegistTotal ");
		}
		sql.append("from tab_gw_device a,tab_devicetype_info b ");
		sql.append("where a.devicetype_id=b.devicetype_id ");
		sql.append("and a.vendor_id=b.vendor_id ");
		sql.append("and a.device_model_id=b.device_model_id ");
		
		if(false == StringUtil.IsEmpty(vendorId) && !"-1".equals(vendorId)){ // 设备厂商ID
			sql.append(" and a.vendor_id = '"+vendorId+"'");
		}
		if(false == StringUtil.IsEmpty(deviceTypeId) && !"-1".equals(deviceTypeId)){ // 设备版本ID
			sql.append(" and a.devicetype_id = "+deviceTypeId);
		}
		if(false == StringUtil.IsEmpty(deviceModelId) && !"-1".equals(deviceModelId)){ // 设备型号ID
			sql.append(" and a.device_model_id = '"+deviceModelId+"'");
		}
		if(false == StringUtil.IsEmpty(accessType)){ // 接入方式
			sql.append(" and b.access_style_relay_id = " + accessType);
		}
		if(false == StringUtil.IsEmpty(usertype)){  // 终端类型
			sql.append(" and b.rela_dev_type_id = " + usertype);
		}
		if (false == StringUtil.IsEmpty(starttime1)){ // 注册时间
			sql.append(" and a.complete_time >= ").append(starttime1);
		}
		if (false == StringUtil.IsEmpty(endtime1)){  // 注册时间
			sql.append(" and a.complete_time <= ").append(endtime1);
		}
		if (false == StringUtil.IsEmpty(cityId)&&!"00".equals(cityId)){ // 设备属地
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and a.city_id in (").append(StringUtils.weave(cityIdList)).append(")");
			cityIdList = null;
		}
		sql.append(" group by a.city_id ");
		
		PrepareSQL psql = new PrepareSQL(sql.toString());
		List list = jt.queryForList(psql.getSQL());
		if(false == list.isEmpty()){
			for(int i = 0; i < list.size(); i++){
				Map rmap = (Map)list.get(i);
				map.put(StringUtil.getStringValue(rmap.get("city_id")), 
						StringUtil.getStringValue(rmap.get("haveRegistTotal")));
			}
		}
		return map;
	}
	
	/**
	 * 近期活跃的设备 
	 * 
	 * @param vendorId
	 * @param deviceTypeId
	 * @param deviceModelId
	 * @param cityId
	 * @param accessType
	 * @param usertype
	 * @param starttime1
	 * @param endtime1
	 * @return
	 */
	public Map<String, String> recentActiveDevice(String vendorId, String deviceTypeId,
			String deviceModelId, String cityId, String accessType,
			String usertype, String starttime1, String endtime1) 
	{
		logger.debug("recentActiveDevice({},{},{},{},{},{},{},{})", new Object[] {
				vendorId, deviceTypeId, deviceModelId, cityId, accessType,
				usertype, starttime1, endtime1 });
		
		StringBuffer sql = new StringBuffer();
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			//TODO wait
			sql.append("select a.city_id, count(*) as recentActiveDevice ");
		}else{
			sql.append("select a.city_id, count(1) as recentActiveDevice ");
		}
		sql.append("from tab_gw_device a,tab_devicetype_info b,gw_devicestatus c");
		sql.append("where a.devicetype_id = b.devicetype_id ");
		sql.append("and a.vendor_id = b.vendor_id ");
		sql.append("and a.device_model_id = b.device_model_id ");
		sql.append("and a.device_id = c.device_id");
		
		if(false == StringUtil.IsEmpty(vendorId) && !"-1".equals(vendorId)){ // 设备厂商ID
			sql.append(" and a.vendor_id = '"+vendorId+"'");
		}
		if(false == StringUtil.IsEmpty(deviceTypeId) && !"-1".equals(deviceTypeId)){ // 设备版本ID
			sql.append(" and a.devicetype_id = "+deviceTypeId);
		}
		if(false == StringUtil.IsEmpty(deviceModelId) && !"-1".equals(deviceModelId)){ // 设备型号ID
			sql.append(" and a.device_model_id = '"+deviceModelId+"'");
		}
		if(false == StringUtil.IsEmpty(accessType)){ // 接入方式
			sql.append(" and b.access_style_relay_id = " + accessType);
		}
		if(false == StringUtil.IsEmpty(usertype)){  // 终端类型
			sql.append(" and b.rela_dev_type_id = " + usertype);
		}
		if (false == StringUtil.IsEmpty(starttime1)){ // 注册时间
			sql.append(" and c.last_time >= ").append(starttime1);
			sql.append(" and a.complete_time >= ").append(starttime1);
		}
		if (false == StringUtil.IsEmpty(endtime1)){  // 注册时间
			sql.append(" and a.complete_time <= ").append(endtime1);
		}
		if (false == StringUtil.IsEmpty(cityId)&&!"00".equals(cityId)){ // 设备属地
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and a.city_id in (").append(StringUtils.weave(cityIdList)).append(")");
			cityIdList = null;
		}
		sql.append(" group by a.city_id ");
		
		PrepareSQL psql = new PrepareSQL(sql.toString());
		psql.getSQL();
		
		List list = jt.queryForList(sql.toString());
		Map<String, String> map = new HashMap<String, String>();
		if(false == list.isEmpty()){
			for(int i = 0; i < list.size(); i++){
				Map rmap = (Map)list.get(i);
				map.put(StringUtil.getStringValue(rmap.get("city_id")), 
						StringUtil.getStringValue(rmap.get("recentActiveDevice")));
			}
		}
		
		return map;
	}
	
	/**
	 * 统计所有绑定方式的用户数
	 * 
	 * @param vendorId
	 * @param deviceTypeId
	 * @param deviceModelId
	 * @param cityId
	 * @param accessType
	 * @param usertype
	 * @param starttime1
	 * @param endtime1
	 * @return
	 */
	public List<Map<String, String>> countAllBindWay(String vendorId, String deviceTypeId,
			String deviceModelId, String cityId, String accessType,
			String usertype, String starttime1, String endtime1) 
	{
		logger.debug("countAllBindWay({},{},{},{},{},{},{},{})", new Object[] { vendorId,
				deviceTypeId, deviceModelId, cityId, accessType, usertype,
				starttime1, endtime1 });
		
		StringBuffer sql = new StringBuffer();
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			//TODO wait
			sql.append("select a.city_id,c.userline,count(*) as total ");
		}else{
			sql.append("select a.city_id,c.userline,count(1) as total ");
		}
		
		sql.append("  from tab_gw_device a, tab_devicetype_info b, tab_hgwcustomer c");
		sql.append(" where a.devicetype_id = b.devicetype_id ");
		sql.append("   and a.vendor_id = b.vendor_id ");
		sql.append("   and a.device_model_id = b.device_model_id ");
		sql.append("   and a.device_id = c.device_id");
		
		if(false == StringUtil.IsEmpty(vendorId) && !"-1".equals(vendorId)){ // 设备厂商ID
			sql.append(" and a.vendor_id = '"+vendorId+"'");
		}
		if(false == StringUtil.IsEmpty(deviceTypeId) && !"-1".equals(deviceTypeId)){ // 设备版本ID
			sql.append(" and a.devicetype_id = "+deviceTypeId);
		}
		if(false == StringUtil.IsEmpty(deviceModelId) && !"-1".equals(deviceModelId)){ // 设备型号ID
			sql.append(" and a.device_model_id = '"+deviceModelId+"'");
		}
		if(false == StringUtil.IsEmpty(accessType)){ // 接入方式
			sql.append(" and b.access_style_relay_id = " + accessType);
		}
		if(false == StringUtil.IsEmpty(usertype)){  // 终端类型
			sql.append(" and b.rela_dev_type_id = " + usertype);
		}
		if (false == StringUtil.IsEmpty(starttime1)){ // 注册时间
			sql.append(" and a.complete_time >= ").append(starttime1);
		}
		if (false == StringUtil.IsEmpty(endtime1)){  // 注册时间
			sql.append(" and a.complete_time <= ").append(endtime1);
		}
		if (false == StringUtil.IsEmpty(cityId)&&!"00".equals(cityId)){ // 设备属地
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and a.city_id in (").append(StringUtils.weave(cityIdList)).append(")");
			cityIdList = null;
		}
		
		sql.append(" group by a.city_id,c.userline");
		
		PrepareSQL psql = new PrepareSQL(sql.toString());
		return jt.queryForList(psql.getSQL());
	}
	
	/**
	 * 获取设备列表
	 * 
	 * @param flag  0：表示展示注册终端的列表  1：表示展示近期活跃的终端 2：表示展示自动绑定的用户
	 * @param cityId
	 * @param starttime1
	 * @param endtime1
	 * @param vendorId
	 * @param deviceModelId
	 * @param deviceTypeId
	 * @param accessType
	 * @param usertype
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @return
	 */
	public List<Map> getDeviceList(String flag, String cityId, String starttime1,
			String endtime1, String vendorId, String deviceModelId,
			String deviceTypeId, String accessType, String usertype,
			int curPage_splitPage, int num_splitPage) 
	{
		
		logger.debug("getDeviceList({},{},{},{},{},{},{},{},{},{},{})",
				new Object[] { flag, cityId, starttime1, endtime1, vendorId,
						deviceModelId, deviceTypeId, accessType, usertype,
						curPage_splitPage, num_splitPage });
		
		StringBuffer sql = new StringBuffer();
	/**	if(DBUtil.GetDB()==Global.DB_MYSQL){
			//TODO wait
		}else{
			
		} */
		sql.append("select distinct a.device_id,a.vendor_id,a.devicetype_id,a.device_model_id,a.city_id,");
		sql.append("a.device_serialnumber,a.device_id_ex,a.device_type,a.complete_time,c.last_time ");
		sql.append("  from tab_gw_device a, tab_devicetype_info b, gw_devicestatus c "); 
		sql.append(" where a.devicetype_id = b.devicetype_id ");
		sql.append("   and a.vendor_id = b.vendor_id ");
		sql.append("   and a.device_model_id = b.device_model_id ");
		sql.append("   and a.device_id = c.device_id");
		
		if(false == StringUtil.IsEmpty(vendorId) && !"-1".equals(vendorId)){ // 设备厂商ID
			sql.append(" and a.vendor_id = '"+vendorId+"'");
		}
		if(false == StringUtil.IsEmpty(deviceTypeId) && !"-1".equals(deviceTypeId)){ // 设备版本ID
			sql.append(" and a.devicetype_id = "+deviceTypeId);
		}
		if(false == StringUtil.IsEmpty(deviceModelId) && !"-1".equals(deviceModelId)){ // 设备型号ID
			sql.append(" and a.device_model_id = '"+deviceModelId+"'");
		}
		if(false == StringUtil.IsEmpty(accessType)){ // 接入方式
			sql.append(" and b.access_style_relay_id = " + accessType);
		}
		if(false == StringUtil.IsEmpty(usertype)){  // 终端类型
			sql.append(" and b.rela_dev_type_id = " + usertype);
		}
		if (false == StringUtil.IsEmpty(starttime1)){ // 注册时间
			// flag = 1 表示：展示近期活跃的设备 需要加上last_time最近上报时间
			if("1".equals(flag)){
				sql.append(" and c.last_time >= ").append(starttime1);
			}
			sql.append(" and a.complete_time >= ").append(starttime1);
		}
		if (false == StringUtil.IsEmpty(endtime1)){  // 注册时间
			sql.append(" and a.complete_time <= ").append(endtime1);
		}
		if (false == StringUtil.IsEmpty(cityId)&&!"00".equals(cityId)){ // 设备属地
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and a.city_id in (").append(StringUtils.weave(cityIdList)).append(")");
			cityIdList = null;
		}
		
		PrepareSQL psql = new PrepareSQL(sql.toString());
		
		cityMap = CityDAO.getCityIdCityNameMap();
		venderMap = getVenderMap();
		deviceModelMap = getDeviceModelMap();
		
		List<Map> list = querySP(psql.getSQL(), (curPage_splitPage - 1) * num_splitPage
				+ 1, num_splitPage, new RowMapper()
		{
			public Object mapRow(ResultSet rs, int arg1) throws SQLException{
				
				Map<String, String> map = new HashMap<String, String>();
				
				map.put("device_id", rs.getString("device_id"));
				map.put("vendorName", venderMap.get(rs.getString("vendor_id")));
				
				String devicetype_id = rs.getString("devicetype_id");
				if(false == StringUtil.IsEmpty(devicetype_id)){
					
					String[] tmp = (String[])deviceModelMap.get(devicetype_id);
					if (tmp != null && tmp.length==2){
						map.put("devicemodel", tmp[0]);
						map.put("softwareversion", tmp[1]);
					}else {
						map.put("devicemodel", "");
						map.put("softwareversion", "");
					}
				}else{
					map.put("devicemodel", "");
					map.put("softwareversion", "");
				}
				
				// 设备属地
				String city_id = rs.getString("city_id");
				if(false == StringUtil.IsEmpty(city_id)){
					String city_name = StringUtil.getStringValue(cityMap.get(city_id));
					if (false == StringUtil.IsEmpty(city_name)){
						map.put("city_name", city_name);
					}else{
						map.put("city_name", "");
					}
				}else {
					map.put("city_name", "");
				}
				
				map.put("deviceSerialnumber", rs.getString("device_serialnumber")); 
				// 设备逻辑SN
				map.put("deviceIdEx", rs.getString("device_id_ex"));
				// 终端类型
				map.put("device_type", rs.getString("device_type"));
				
				// 设备注册时间 将complete_time转换成时间
				try{
					long completeTime = StringUtil.getLongValue(rs.getString("complete_time"));
					DateTimeUtil dt = new DateTimeUtil(completeTime * 1000);
					map.put("completeTime", dt.getLongDate());
				}catch (NumberFormatException e){
					map.put("completeTime", "");
				}catch (Exception e){
					map.put("completeTime", "");
				}

				// 设备最近上报时间 将last_time转换成时间
				try{
					long lastTime = StringUtil.getLongValue(rs.getString("last_time"));
					DateTimeUtil dt = new DateTimeUtil(lastTime * 1000);
					map.put("lastTime", dt.getLongDate());
				}catch (NumberFormatException e){
					map.put("lastTime", "");
				}catch (Exception e){
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
	 * 统计终端
	 * 
	 * @param flag    0：表示展示注册终端的列表  1：表示展示近期活跃的终端 2：表示展示自动绑定的用户
	 * @param cityId
	 * @param starttime1
	 * @param endtime1
	 * @param vendorId
	 * @param deviceModelId
	 * @param deviceTypeId
	 * @param accessType
	 * @param usertype
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @return
	 */
	public int getDeviceListCount(String flag, String cityId, String starttime1,
			String endtime1, String vendorId, String deviceModelId,
			String deviceTypeId, String accessType, String usertype,
			int curPage_splitPage, int num_splitPage) 
	{
		logger.debug("getDeviceListCount({},{},{},{},{},{},{},{},{},{},{})",
				new Object[] { flag, cityId, starttime1, endtime1, vendorId,
						deviceModelId, deviceTypeId, accessType, usertype,
						curPage_splitPage, num_splitPage });
		
		StringBuffer sql = new StringBuffer();
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			//TODO wait
			sql.append("select count(*) ");
		}else{
			sql.append("select count(1) ");
		}
		
		sql.append("from tab_gw_device a, tab_devicetype_info b, gw_devicestatus c "); 
		sql.append(" where a.devicetype_id = b.devicetype_id ");
		sql.append("   and a.vendor_id = b.vendor_id ");
		sql.append("   and a.device_model_id = b.device_model_id ");
		sql.append("   and a.device_id = c.device_id");
		
		if(false == StringUtil.IsEmpty(vendorId) && !"-1".equals(vendorId)){ // 设备厂商ID
			sql.append(" and a.vendor_id = '"+vendorId+"'");
		}
		if(false == StringUtil.IsEmpty(deviceTypeId) && !"-1".equals(deviceTypeId)){ // 设备版本ID
			sql.append(" and a.devicetype_id = "+deviceTypeId);
		}
		if(false == StringUtil.IsEmpty(deviceModelId) && !"-1".equals(deviceModelId)){ // 设备型号ID
			sql.append(" and a.device_model_id = '"+deviceModelId+"'");
		}
		if(false == StringUtil.IsEmpty(accessType)){ // 接入方式
			sql.append(" and b.access_style_relay_id = " + accessType);
		}
		if(false == StringUtil.IsEmpty(usertype)){  // 终端类型
			sql.append(" and b.rela_dev_type_id = " + usertype);
		}
		if (false == StringUtil.IsEmpty(starttime1)){ // 注册时间
			// flag = 1 表示：展示近期活跃的设备 需要加上last_time最近上报时间
			if("1".equals(flag)){
				sql.append(" and c.last_time >= ").append(starttime1);
			}
			sql.append(" and a.complete_time >= ").append(starttime1);
		}
		if (false == StringUtil.IsEmpty(endtime1)){  // 注册时间
			sql.append(" and a.complete_time <= ").append(endtime1);
		}
		if (false == StringUtil.IsEmpty(cityId)&&!"00".equals(cityId)){ // 设备属地
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and a.city_id in (").append(StringUtils.weave(cityIdList)).append(")");
			cityIdList = null;
		}
			
		PrepareSQL psql = new PrepareSQL(sql.toString());
		psql.getSQL();
		int total = jt.queryForInt(sql.toString());
		int maxPage = 1;
		if (total % num_splitPage == 0){
			maxPage = total / num_splitPage;
		}else{
			maxPage = total / num_splitPage + 1;
		}
		return maxPage;
	}
	
	/**
	 * 获取自动绑定的用户列表
	 * 
	 * @param flag  0：表示展示注册终端的列表  1：表示展示近期活跃的终端 2：表示展示自动绑定的用户
	 * @param cityId
	 * @param starttime1
	 * @param endtime1
	 * @param vendorId
	 * @param deviceModelId
	 * @param deviceTypeId
	 * @param accessType
	 * @param usertype
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @return
	 */
	public List<Map> getUserList(String flag, String cityId, String starttime1,
			String endtime1, String vendorId, String deviceModelId,
			String deviceTypeId, String accessType, String usertype,
			int curPage_splitPage, int num_splitPage) 
	{
		
		logger.debug("getUserList({},{},{},{},{},{},{},{},{},{},{})",
				new Object[] { flag, cityId, starttime1, endtime1, vendorId, 
				deviceModelId, deviceTypeId, accessType, usertype,
						curPage_splitPage, num_splitPage });
		
		StringBuffer sql = new StringBuffer();
	/**	if(DBUtil.GetDB()==Global.DB_MYSQL){
			//TODO wait
		}else{
			
		} */
		sql.append("select a.city_id, a.oui, a.device_serialnumber, c.username, c.user_id, ");
		sql.append("c.user_type_id, c.opendate, c.opmode, c.userline ");
		sql.append("  from tab_gw_device a, tab_devicetype_info b, tab_hgwcustomer c ");
		sql.append(" where a.devicetype_id = b.devicetype_id ");
		sql.append("   and a.vendor_id = b.vendor_id ");
		sql.append("   and a.device_model_id = b.device_model_id ");
		sql.append("   and a.device_id = c.device_id ");
		
		if(false == StringUtil.IsEmpty(vendorId) && !"-1".equals(vendorId)){ // 设备厂商ID
			sql.append(" and a.vendor_id = '"+vendorId+"'");
		}
		if(false == StringUtil.IsEmpty(deviceTypeId) && !"-1".equals(deviceTypeId)){ // 设备版本ID
			sql.append(" and a.devicetype_id = "+deviceTypeId);
		}
		if(false == StringUtil.IsEmpty(deviceModelId) && !"-1".equals(deviceModelId)){ // 设备型号ID
			sql.append(" and a.device_model_id = '"+deviceModelId+"'");
		}
		if(false == StringUtil.IsEmpty(accessType)){ // 接入方式
			sql.append(" and b.access_style_relay_id = " + accessType);
		}
		if(false == StringUtil.IsEmpty(usertype)){  // 终端类型
			sql.append(" and b.rela_dev_type_id = " + usertype);
		}
		if (false == StringUtil.IsEmpty(starttime1)){ // 注册时间
			sql.append(" and a.complete_time >= ").append(starttime1);
		}
		if (false == StringUtil.IsEmpty(endtime1)){  // 注册时间
			sql.append(" and a.complete_time <= ").append(endtime1);
		}
		if (false == StringUtil.IsEmpty(cityId)&&!"00".equals(cityId)){ // 设备属地
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and a.city_id in (").append(StringUtils.weave(cityIdList)).append(")");
			cityIdList = null;
		}
		
		// 3,5,6,7 分别表示 物理SN自动绑定, 桥接账号自动绑定, 逻辑SN自动绑定, 路由账号自动绑定
		if (LipossGlobals.isOracle()) {
			sql.append(" and a.device_id is not null and c.userline in (3,5,6,7)");
		}else{
			sql.append(" and a.device_id != null and a.device_id != '' and c.userline in (3,5,6,7)");
		}
		
		PrepareSQL psql = new PrepareSQL(sql.toString());
		
		cityMap = CityDAO.getCityIdCityNameMap();
		userTypeMap = getUserType();
		bindTypeMap = getBindType();
		List<Map> list = querySP(psql.getSQL(), (curPage_splitPage - 1) * num_splitPage
				+ 1, num_splitPage, new RowMapper()
		{

			public Object mapRow(ResultSet rs, int arg1) throws SQLException{
				Map<String, String> map = new HashMap<String, String>();
				map.put("username", rs.getString("username"));
				String city_id = rs.getString("city_id");
				String city_name = StringUtil.getStringValue(cityMap.get(city_id));
				if (false == StringUtil.IsEmpty(city_name)){
					map.put("city_name", city_name);
				}else{
					map.put("city_name", "");
				}
				map.put("user_id", rs.getString("user_id"));
				String user_type_id = rs.getString("user_type_id");
				String tmp = "手工添加";
				if (false == StringUtil.IsEmpty(user_type_id)){
					tmp = userTypeMap.get(user_type_id);
					if (true == StringUtil.IsEmpty(tmp)){
						tmp = "其他";
					}
				}
				map.put("user_type", tmp);
				String oui = rs.getString("oui") == null ? "" : rs.getString("oui");
				String device_serialnumber = rs.getString("device_serialnumber") == null ? ""
						: rs.getString("device_serialnumber");
				map.put("device", oui + "-" + device_serialnumber);
				// 将opendate转换成时间
				try{
					long opendate = StringUtil.getLongValue(rs.getString("opendate"));
					DateTimeUtil dt = new DateTimeUtil(opendate * 1000);
					map.put("opendate", dt.getLongDate());
				}catch (NumberFormatException e){
					map.put("opendate", "");
				}catch (Exception e){
					map.put("opendate", "");
				}
				map.put("opmode", rs.getString("opmode"));
				// 绑定方式
				String bindtype = "-";
				String userline = rs.getString("userline");
				if (false == StringUtil.IsEmpty(userline)){
					bindtype = bindTypeMap.get(userline);
					if (true == StringUtil.IsEmpty(bindtype)){
						bindtype = "-";
					}
				}
				map.put("bindtype", bindtype);
				return map;
			}
		});
		cityMap = null;
		userTypeMap = null;
		bindTypeMap = null;
		return list;
	}
	
	/**
	 * 自动绑定用户数
	 * 
	 * @param flag    0：表示展示注册终端的列表  1：表示展示近期活跃的终端 2：表示展示自动绑定的用户
	 * @param cityId
	 * @param starttime1
	 * @param endtime1
	 * @param vendorId
	 * @param deviceModelId
	 * @param deviceTypeId
	 * @param accessType
	 * @param usertype
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @return
	 */
	public int getUserListCount(String flag, String cityId, String starttime1,
			String endtime1, String vendorId, String deviceModelId,
			String deviceTypeId, String accessType, String usertype,
			int curPage_splitPage, int num_splitPage) 
	{
		logger.debug("getUserListCount({},{},{},{},{},{},{},{},{},{},{})",
				new Object[] { flag, cityId, starttime1, endtime1, vendorId,
						deviceModelId, deviceTypeId, accessType, usertype,
						curPage_splitPage, num_splitPage });
		
		StringBuffer sql = new StringBuffer();
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			//TODO wait
			sql.append("select count(*) ");
		}else{
			sql.append("select count(1) ");
		}
		
		sql.append("  from tab_gw_device a, tab_devicetype_info b, tab_hgwcustomer c ");
		sql.append(" where a.devicetype_id = b.devicetype_id ");
		sql.append("   and a.vendor_id = b.vendor_id ");
		sql.append("   and a.device_model_id = b.device_model_id ");
		sql.append("   and a.device_id = c.device_id ");
		
		if(false == StringUtil.IsEmpty(vendorId) && !"-1".equals(vendorId)){ // 设备厂商ID
			sql.append(" and a.vendor_id = '"+vendorId+"'");
		}
		if(false == StringUtil.IsEmpty(deviceTypeId) && !"-1".equals(deviceTypeId)){ // 设备版本ID
			sql.append(" and a.devicetype_id = "+deviceTypeId);
		}
		if(false == StringUtil.IsEmpty(deviceModelId) && !"-1".equals(deviceModelId)){ // 设备型号ID
			sql.append(" and a.device_model_id = '"+deviceModelId+"'");
		}
		if(false == StringUtil.IsEmpty(accessType)){ // 接入方式
			sql.append(" and b.access_style_relay_id = " + accessType);
		}
		if(false == StringUtil.IsEmpty(usertype)){  // 终端类型
			sql.append(" and b.rela_dev_type_id = " + usertype);
		}
		if (false == StringUtil.IsEmpty(starttime1)){ // 注册时间
			sql.append(" and a.complete_time >= ").append(starttime1);
		}
		if (false == StringUtil.IsEmpty(endtime1)){  // 注册时间
			sql.append(" and a.complete_time <= ").append(endtime1);
		}
		
		if (false == StringUtil.IsEmpty(cityId)&&!"00".equals(cityId)){
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and a.city_id in (").append(StringUtils.weave(cityIdList)).append(")");
			cityIdList = null;
		}
	
		// 3,5,6,7 分别表示 物理SN自动绑定, 桥接账号自动绑定, 逻辑SN自动绑定, 路由账号自动绑定
		if (LipossGlobals.isOracle()) {
			sql.append(" and a.device_id is not null and c.userline in (3,5,6,7)");
		}else {
			sql.append(" and a.device_id != null and a.device_id != '' and c.userline in (3,5,6,7)");
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
	
	/**
	 * 导出设备列表 
	 * 
	 * @param flag  0:已注册设备   1：近期活跃设备
	 * @param vendorId
	 * @param deviceTypeId
	 * @param deviceModelId
	 * @param cityId
	 * @param accessType
	 * @param usertype
	 * @param starttime1
	 * @param endtime1
	 * @return
	 */
	public List<Map> getDeviceExcel(String flag, String vendorId,
			String deviceTypeId, String deviceModelId, String cityId,
			String accessType, String usertype, String starttime1,String endtime1) 
	{
		
		logger.debug("getDeviceExcel({},{},{},{},{},{},{},{},{})",
				new Object[] { flag, vendorId, deviceTypeId, deviceModelId,
						cityId, accessType, usertype, starttime1, endtime1 });
		
		StringBuffer sql = new StringBuffer();
	/**	if(DBUtil.GetDB()==Global.DB_MYSQL){
			//TODO wait
		}else{
			
		} */
		sql.append("select distinct a.device_id,a.vendor_id,a.devicetype_id,");
		sql.append("a.device_model_id,a.city_id,a.device_serialnumber,a.device_id_ex,");
		sql.append("a.device_type,a.complete_time,c.last_time ");
		sql.append("  from tab_gw_device a, tab_devicetype_info b, gw_devicestatus c "); 
		sql.append(" where a.devicetype_id = b.devicetype_id ");
		sql.append("   and a.vendor_id = b.vendor_id ");
		sql.append("   and a.device_model_id = b.device_model_id ");
		sql.append("   and a.device_id = c.device_id");
		
		if(false == StringUtil.IsEmpty(vendorId) && !"-1".equals(vendorId)){ // 设备厂商ID
			sql.append(" and a.vendor_id = '"+vendorId+"'");
		}
		if(false == StringUtil.IsEmpty(deviceTypeId) && !"-1".equals(deviceTypeId)){ // 设备版本ID
			sql.append(" and a.devicetype_id = "+deviceTypeId);
		}
		if(false == StringUtil.IsEmpty(deviceModelId) && !"-1".equals(deviceModelId)){ // 设备型号ID
			sql.append(" and a.device_model_id = '"+deviceModelId+"'");
		}
		if(false == StringUtil.IsEmpty(accessType)){ // 接入方式
			sql.append(" and b.access_style_relay_id = " + accessType);
		}
		if(false == StringUtil.IsEmpty(usertype)){  // 终端类型
			sql.append(" and b.rela_dev_type_id = " + usertype);
		}
		if (false == StringUtil.IsEmpty(starttime1)){ // 注册时间
			// flag = 1 表示：展示近期活跃的设备 需要加上last_time最近上报时间
			if("1".equals(flag)){
				sql.append(" and c.last_time >= ").append(starttime1);
			}
			sql.append(" and a.complete_time >= ").append(starttime1);
		}
		if (false == StringUtil.IsEmpty(endtime1)){  // 注册时间
			sql.append(" and a.complete_time <= ").append(endtime1);
		}
		if (false == StringUtil.IsEmpty(cityId)&&!"00".equals(cityId)){ // 设备属地
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and a.city_id in (").append(StringUtils.weave(cityIdList)).append(")");
			cityIdList = null;
		}
		
		PrepareSQL psql = new PrepareSQL(sql.toString());
		
		cityMap = CityDAO.getCityIdCityNameMap();
		venderMap = getVenderMap();
		deviceModelMap = getDeviceModelMap();
		
		List<Map> list = jt.query(psql.getSQL(), new RowMapper()
		{
			public Object mapRow(ResultSet rs, int arg1) throws SQLException{
				
				Map<String, String> map = new HashMap<String, String>();
				
				// 设备ID
				map.put("device_id", rs.getString("device_id"));
				
				// 设备厂商
				map.put("vendorName", venderMap.get(rs.getString("vendor_id")));
				
				// 设备型号  设备版本
				String devicetype_id = rs.getString("devicetype_id");
				if(false == StringUtil.IsEmpty(devicetype_id)){
					
					String[] tmp = (String[])deviceModelMap.get(devicetype_id);
					if (tmp != null && tmp.length==2){
						map.put("devicemodel", tmp[0]);
						map.put("softwareversion", tmp[1]);
					}else {
						map.put("devicemodel", "");
						map.put("softwareversion", "");
					}
				}else{
					map.put("devicemodel", "");
					map.put("softwareversion", "");
				}
				
				// 设备属地
				String city_id = rs.getString("city_id");
				if(false == StringUtil.IsEmpty(city_id)){
					String city_name = StringUtil.getStringValue(cityMap.get(city_id));
					if (false == StringUtil.IsEmpty(city_name)){
						map.put("city_name", city_name);
					}else{
						map.put("city_name", "");
					}
				}else {
					map.put("city_name", "");
				}
				
				String city_name = StringUtil.getStringValue(cityMap.get(city_id));
				if (false == StringUtil.IsEmpty(city_name)){
					map.put("city_name", city_name);
				}else{
					map.put("city_name", "");
				}
				
				// 设备序列号
				map.put("deviceSerialnumber", rs.getString("device_serialnumber")); 
				// 设备逻辑SN
				map.put("deviceIdEx", rs.getString("device_id_ex"));
				// 终端类型
				map.put("device_type", rs.getString("device_type"));
				
				// 设备注册时间 将complete_time转换成时间
				try{
					long completeTime = StringUtil.getLongValue(rs.getString("complete_time"));
					DateTimeUtil dt = new DateTimeUtil(completeTime * 1000);
					map.put("completeTime", dt.getLongDate());
				}catch (NumberFormatException e){
					map.put("completeTime", "");
				}catch (Exception e){
					map.put("completeTime", "");
				}

				// 设备最近上报时间 将last_time转换成时间
				try{
					long lastTime = StringUtil.getLongValue(rs.getString("last_time"));
					DateTimeUtil dt = new DateTimeUtil(lastTime * 1000);
					map.put("lastTime", dt.getLongDate());
				}catch (NumberFormatException e){
					map.put("lastTime", "");
				}catch (Exception e){
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
	
	
	public List<Map> getUserExcel(String flag, String vendorId,
			String deviceTypeId, String deviceModelId, String cityId,
			String accessType, String usertype, String starttime1,String endtime1) 
	{
		logger.debug("getUserExcel({},{},{},{},{},{},{},{},{})",
				new Object[] { flag, vendorId, deviceTypeId, deviceModelId,
						cityId, accessType, usertype, starttime1, endtime1 });
		
		StringBuffer sql = new StringBuffer();
	/**	if(DBUtil.GetDB()==Global.DB_MYSQL){
			//TODO wait
		}else{
			
		} */
		sql.append("select a.city_id, a.oui, a.device_serialnumber, c.username, c.user_id, ");
		sql.append("c.user_type_id, c.opendate, c.opmode, c.userline ");
		sql.append("  from tab_gw_device a, tab_devicetype_info b, tab_hgwcustomer c");
		sql.append(" where a.devicetype_id = b.devicetype_id ");
		sql.append("   and a.vendor_id = b.vendor_id ");
		sql.append("   and a.device_model_id = b.device_model_id ");
		sql.append("   and a.device_id = c.device_id");
		
		if(false == StringUtil.IsEmpty(vendorId) && !"-1".equals(vendorId)){ // 设备厂商ID
			sql.append(" and a.vendor_id = '"+vendorId+"'");
		}
		if(false == StringUtil.IsEmpty(deviceTypeId) && !"-1".equals(deviceTypeId)){ // 设备版本ID
			sql.append(" and a.devicetype_id = "+deviceTypeId);
		}
		if(false == StringUtil.IsEmpty(deviceModelId) && !"-1".equals(deviceModelId)){ // 设备型号ID
			sql.append(" and a.device_model_id = '"+deviceModelId+"'");
		}
		if(false == StringUtil.IsEmpty(accessType)){ // 接入方式
			sql.append(" and b.access_style_relay_id = " + accessType);
		}
		if(false == StringUtil.IsEmpty(usertype)){  // 终端类型
			sql.append(" and b.rela_dev_type_id = " + usertype);
		}
		if (false == StringUtil.IsEmpty(starttime1)){ // 注册时间
			sql.append(" and a.complete_time >= ").append(starttime1);
		}
		if (false == StringUtil.IsEmpty(endtime1)){  // 注册时间
			sql.append(" and a.complete_time <= ").append(endtime1);
		}
		
		if (false == StringUtil.IsEmpty(cityId)&&!"00".equals(cityId)){
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and a.city_id in (").append(StringUtils.weave(cityIdList)).append(")");
			cityIdList = null;
		}
		
		// 3,5,6,7 分别表示 物理SN自动绑定, 桥接账号自动绑定, 逻辑SN自动绑定, 路由账号自动绑定
		if (LipossGlobals.isOracle()) {
			sql.append(" and a.device_id is not null and c.userline in (3,5,6,7)");
		}else {
			sql.append(" and a.device_id != null and a.device_id != '' and c.userline in (3,5,6,7)");
		}
		
		PrepareSQL psql = new PrepareSQL(sql.toString());
		
		cityMap = CityDAO.getCityIdCityNameMap();
		userTypeMap = getUserType();
		bindTypeMap = getBindType();
		
		List<Map> list = jt.query(psql.getSQL(), new RowMapper()
		{

			public Object mapRow(ResultSet rs, int arg1) throws SQLException{
				Map<String, String> map = new HashMap<String, String>();
				map.put("username", rs.getString("username"));
				String city_id = rs.getString("city_id");
				String city_name = StringUtil.getStringValue(cityMap.get(city_id));
				if (false == StringUtil.IsEmpty(city_name)){
					map.put("city_name", city_name);
				}else{
					map.put("city_name", "");
				}
				map.put("user_id", rs.getString("user_id"));
				String user_type_id = rs.getString("user_type_id");
				String tmp = "手工添加";
				if (false == StringUtil.IsEmpty(user_type_id)){
					tmp = userTypeMap.get(user_type_id);
					if (true == StringUtil.IsEmpty(tmp)){
						tmp = "其他";
					}
				}
				map.put("user_type", tmp);
				String oui = rs.getString("oui") == null ? "" : rs.getString("oui");
				String device_serialnumber = rs.getString("device_serialnumber") == null ? ""
						: rs.getString("device_serialnumber");
				map.put("device", oui + "-" + device_serialnumber);
				// 将opendate转换成时间
				try{
					long opendate = StringUtil.getLongValue(rs.getString("opendate"));
					DateTimeUtil dt = new DateTimeUtil(opendate * 1000);
					map.put("opendate", dt.getLongDate());
				}catch (Exception e){
					map.put("opendate", "");
				}
				map.put("opmode", rs.getString("opmode"));
				// 绑定方式
				String bindtype = "-";
				String userline = rs.getString("userline");
				if (false == StringUtil.IsEmpty(userline)){
					bindtype = bindTypeMap.get(userline);
					if (true == StringUtil.IsEmpty(bindtype)){
						bindtype = "-";
					}
				}
				map.put("bindtype", bindtype);
				return map;
			}
		});
		
		cityMap = null;
		userTypeMap = null;
		bindTypeMap = null;
		
		return list;
	}
	
	
	/**
	 * 获取 用户类型ID 与用户类型名称 对应的Map
	 * 
	 * @return
	 */
	public Map<String, String> getUserType()
	{
		logger.debug("getUserType()");
		
		PrepareSQL psql = new PrepareSQL();
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			psql.append("select user_type_id,type_name from user_type");
		}else{
			psql.append("select * from user_type");
		}
		List<Map> list = jt.queryForList(psql.getSQL());
		Map<String, String> userTypeMap = new HashMap<String, String>();
		for (Map map : list)
		{
			userTypeMap.put(StringUtil.getStringValue(map.get("user_type_id")),	
					StringUtil.getStringValue(map.get("type_name")));
		}
		return userTypeMap;
	}
	
	/**
	 * 获取绑定方式ID 与 绑定方式名称对应的Map
	 * @return
	 */
	public Map<String, String> getBindType()
	{
		logger.debug("getBindType()");
		
		PrepareSQL psql = new PrepareSQL();
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			psql.append("select bind_type_id,type_name from bind_type");
		}else{
			psql.append("select * from bind_type");
		}
		List<Map> list = jt.queryForList(psql.getSQL());
		Map<String, String> bindTypeMap = new HashMap<String, String>();
		for (Map map : list)
		{
			bindTypeMap.put(StringUtil.getStringValue(map.get("bind_type_id")),
					StringUtil.getStringValue(map.get("type_name")));
		}
		return bindTypeMap;
	}
	
	/**
	 * 取得所有vendorId和厂商名对应的MAP
	 * 
	 * @param request
	 * @return ArrayList
	 */
	public HashMap<String, String> getVenderMap() 
	{
		logger.debug("getVenderMap()");
		
		PrepareSQL psql = new PrepareSQL();
		psql.append("select vendor_id,vendor_name,vendor_add from tab_vendor");
		
		List<Map> list = jt.queryForList(psql.getSQL());
		HashMap<String, String> vendorMap = new HashMap<String, String>();
		for(Map<String, String> map : list){
			String vendorAdd = StringUtil.getStringValue(map.get("vendor_add"));
			if(false == StringUtil.IsEmpty(vendorAdd)){
				vendorMap.put(StringUtil.getStringValue(map.get("vendor_id")), vendorAdd);
			}else {
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
	public HashMap<String, Object> getDeviceModelMap() 
	{
		logger.debug("getDeviceModelMap()");
		
		String devicemodel = "";
		String softwareversion = "";
		String devicetype_id = "";
		HashMap<String, Object> deviceTypeMap = new HashMap<String,Object>();

		PrepareSQL psql = new PrepareSQL();
		psql.append("select a.device_model_id,a.device_model,b.softwareversion,b.devicetype_id");
		psql.append("  from gw_device_model a, tab_devicetype_info b ");
		psql.append(" where a.device_model_id=b.device_model_id ");
		
		List<Map> list = jt.queryForList(psql.getSQL());
    	for (Map<String, String> map : list) {
    		
    		devicemodel = StringUtil.getStringValue(map.get("device_model"));
    		softwareversion = StringUtil.getStringValue(map.get("softwareversion"));
    		
    		//型号，软件版本数组，modelsoft[0]为型号，modelsoft[1]为软件版本
			String[] modelsoft = new String[2];
			modelsoft[0] = devicemodel;
			modelsoft[1] = softwareversion;
			
			devicetype_id = StringUtil.getStringValue(map.get("devicetype_id"));
			deviceTypeMap.put(devicetype_id, modelsoft);
		}
		
    	return deviceTypeMap;
	}

}
