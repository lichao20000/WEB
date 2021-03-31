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
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.util.StringUtil;

public class FTTHUserBindDAO extends SuperDAO{

	/** 日志记录 */
	private static Logger logger = LoggerFactory.getLogger(FTTHUserBindDAO.class);
	
	private Map<String, String> cityMap = null;
	private Map<String, String> userTypeMap = null;
	private Map<String, String> bindTypeMap = null;     // 绑定方式
	private Map<String, String> venderMap = null;       // 设备厂商
	private Map<String, String> deviceModelMap = null;  // 设备型号
	private Map<String, String> deviceSoftVersionMap = null;  // 设备版本
	
	/**
	 * 统计所有FTTH用户的绑定情况
	 * @param cityId
	 * @param starttime1
	 * @param endtime1
	 * @return
	 */
	public Map<String, String> getAllBindUser(String cityId, String starttime1, String endtime1) 
	{
		logger.debug("getAllBindUser({},{},{})", new Object[]{cityId, starttime1, endtime1});
		
		Map<String, String> map = new HashMap<String, String>();
		
		StringBuffer sql = new StringBuffer();
		if(DBUtil.GetDB()==3){
			sql.append("select a.city_id, count(*) as total ");
		}else{
			sql.append("select a.city_id, count(1) as total ");
		}
		
		sql.append("  from tab_hgwcustomer a ");
		sql.append(" where 1=1");
		
		// 数据库类型为Oracle
		if (DBUtil.GetDB() == 1) {
			sql.append("   and substr(username,length(username),1) = 'C' ");
		} else if(DBUtil.GetDB()==3){
			sql.append("   and substring(username,length(username),1) = 'C' ");
		} else {
			sql.append("   and substring(username,len(username),1) = 'C' ");
		}
		
		if (false == StringUtil.IsEmpty(starttime1)){
			sql.append(" and a.opendate >= ").append(starttime1);
		}
		
		if (false == StringUtil.IsEmpty(endtime1)){
			sql.append(" and a.opendate <= ").append(endtime1);
		}
		
		if (false == StringUtil.IsEmpty(cityId)&&!"00".equals(cityId)){
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and a.city_id in (").append(StringUtils.weave(cityIdList)).append(")");
			cityIdList = null;
		}
		
		sql.append(" group by a.city_id");
		
		PrepareSQL psql = new PrepareSQL(sql.toString());
		List list = jt.queryForList(psql.getSQL());
		if(false == list.isEmpty()){
			for (int i = 0; i < list.size(); i++) {
				Map rmap = (Map)list.get(i);
				map.put(StringUtil.getStringValue(rmap.get("city_id")), 
						StringUtil.getStringValue(rmap.get("total")));
			}
		}
		
		return map;
	}
	
	
	/**
	 * 统计FTTH用户的已绑定情况
	 * 
	 * @param cityId
	 * @param starttime1
	 * @param endtime1
	 * @return
	 */
	public Map<String, String> getBindUser(String cityId, String starttime1, String endtime1) 
	{
		logger.debug("getBindUser({},{},{})", new Object[]{cityId, starttime1, endtime1});
		
		Map<String, String> map = new HashMap<String, String>();
		
		StringBuffer sql = new StringBuffer();
		if(DBUtil.GetDB()==3){
			sql.append("select a.city_id, count(*) as total ");
		}else{
			sql.append("select a.city_id, count(1) as total ");
		}
		
		sql.append("  from tab_hgwcustomer a ");
		sql.append(" where 1=1");
		
		if (LipossGlobals.isOracle()) {
			sql.append("   and a.device_id is not null ");
		}else{
			sql.append("   and a.device_id != null and a.device_id != '' ");
		}
		
		// 数据库类型为Oracle
		if (DBUtil.GetDB() == 1) {
			sql.append("   and substr(username,length(username),1) = 'C' ");
		} else if(DBUtil.GetDB()==3){
			sql.append("   and substring(username,length(username),1) = 'C' ");
		}else {
			sql.append("   and substring(username,len(username),1) = 'C' ");
		}
		
		if (false == StringUtil.IsEmpty(starttime1)){
			sql.append(" and a.opendate >= ").append(starttime1);
		}
		
		if (false == StringUtil.IsEmpty(endtime1)){
			sql.append(" and a.opendate <= ").append(endtime1);
		}
		
		if (false == StringUtil.IsEmpty(cityId)&&!"00".equals(cityId)){
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and a.city_id in (").append(StringUtils.weave(cityIdList)).append(")");
			cityIdList = null;
		}
		
		sql.append(" group by a.city_id");
		
		PrepareSQL psql = new PrepareSQL(sql.toString());
		List list = jt.queryForList(psql.getSQL());
		if(false == list.isEmpty()){
			for (int i = 0; i < list.size(); i++) {
				Map rmap = (Map)list.get(i);
				map.put(StringUtil.getStringValue(rmap.get("city_id")), 
						StringUtil.getStringValue(rmap.get("total")));
			}
		}
		
		return map;
	}

	
	/**
	 * 统计FTTH用户的未绑定情况
	 * 
	 * @param cityId
	 * @param starttime1
	 * @param endtime1
	 * @return
	 */
	public Map<String, String> getNotBindUser(String cityId, String starttime1, String endtime1) {
		
		logger.debug("getBindUser({},{},{})", new Object[]{cityId, starttime1, endtime1});
		
		Map<String, String> map = new HashMap<String, String>();
		
		StringBuffer sql = new StringBuffer();
		if(DBUtil.GetDB()==3){
			sql.append("select a.city_id, count(*) as total ");
		}else{
			sql.append("select a.city_id, count(1) as total ");
		}
		
		sql.append("  from tab_hgwcustomer a ");
		sql.append(" where 1=1");
		
		if (LipossGlobals.isOracle() || DBUtil.GetDB()==3) {
			sql.append("   and a.device_id is null ");
		}else{
			sql.append("   and (a.device_id = null or a.device_id = '') ");
		}
		
		// 数据库类型为Oracle
		if (DBUtil.GetDB() == 1) {
			sql.append("   and substr(username,length(username),1) = 'C' ");
		}else if(DBUtil.GetDB()==3){
			sql.append("   and substring(username,length(username),1) = 'C' ");
		}else {
			sql.append("   and substring(username,len(username),1) = 'C' ");
		}
		
		if (false == StringUtil.IsEmpty(starttime1)){
			sql.append(" and a.opendate >= ").append(starttime1);
		}
		
		if (false == StringUtil.IsEmpty(endtime1)){
			sql.append(" and a.opendate <= ").append(endtime1);
		}
		
		if (false == StringUtil.IsEmpty(cityId)&&!"00".equals(cityId)){
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and a.city_id in (").append(StringUtils.weave(cityIdList)).append(")");
			cityIdList = null;
		}
		
		sql.append(" group by a.city_id");
		
		PrepareSQL psql = new PrepareSQL(sql.toString());
		List list = jt.queryForList(psql.getSQL());
		if(false == list.isEmpty()){
			for (int i = 0; i < list.size(); i++) {
				Map rmap = (Map)list.get(i);
				map.put(StringUtil.getStringValue(rmap.get("city_id")),
						StringUtil.getStringValue(rmap.get("total")));
			}
		}
		
		return map;
	}
	
	
	/**
	 * 获取用户列表 bindFlag = 1 表示获取已绑定用户列表  bindFlag=2表示获取未绑定用户列表  否则 表示获取全部
	 * 
	 * @param bindFlag
	 * @param cityId
	 * @param starttime1
	 * @param endtime1
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @return
	 */
	public List<Map> getUserList(String bindFlag, String cityId,
			String starttime1, String endtime1, int curPage_splitPage,int num_splitPage) 
	{
		logger.debug("getUserList({},{},{},{},{},{})",
				new Object[] { bindFlag, cityId, starttime1, endtime1,
						curPage_splitPage, num_splitPage });
		
		PrepareSQL psql = new PrepareSQL();
		
		psql.append("select a.device_id, a.city_id, a.username, a.user_id, a.user_type_id, a.oui, a.device_serialnumber, a.opendate, a.userline ");
		psql.append("  from tab_hgwcustomer a ");
		psql.append(" where 1=1");
		
		// 数据库类型为Oracle
		if (DBUtil.GetDB() == 1) {
			psql.append("   and substr(username,length(username),1) = 'C' ");
		}else if(DBUtil.GetDB()==3){
			psql.append("   and substring(username,length(username),1) = 'C' ");
		}else {
			psql.append("   and substring(username,len(username),1) = 'C' ");
		}
		
		/**已绑定用户*/
		if("1".equals(bindFlag)){
			if (LipossGlobals.isOracle() || DBUtil.GetDB()==3) {
				psql.append("   and a.device_id is not null ");
			}else {
				psql.append("   and a.device_id != null and a.device_id != '' ");
			}
		/**未绑定用户*/
		}else if("2".equals(bindFlag)){
			if (LipossGlobals.isOracle() || DBUtil.GetDB()==3) {
				psql.append("   and a.device_id is null ");
			}else{
				psql.append("   and (a.device_id = null or a.device_id = '') ");
			}
		}
		
		if (false == StringUtil.IsEmpty(starttime1)){
			psql.append(" and a.opendate >= ");
			psql.append(starttime1);
		}
		
		if (false == StringUtil.IsEmpty(endtime1)){
			psql.append(" and a.opendate <= ");
			psql.append(endtime1);
		}
		
		if (false == StringUtil.IsEmpty(cityId)&&!"00".equals(cityId)){
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			psql.append(" and a.city_id in (");
			psql.append(StringUtils.weave(cityIdList));
			psql.append(")");
			cityIdList = null;
		}
		
		psql.append(" order by a.city_id ");
		
		cityMap = CityDAO.getCityIdCityNameMap();
		userTypeMap = getUserType();
		bindTypeMap = getBindType();
		venderMap = getVenderMap();                        // 设备厂商
		deviceModelMap = getDeviceModelMap();              // 设备型号
		deviceSoftVersionMap = getDeviceSoftVersionMap();  // 设备版本
		
		List<Map> list = querySP(psql.getSQL(), (curPage_splitPage - 1) * num_splitPage
				+ 1, num_splitPage, new RowMapper()
		{

			public Object mapRow(ResultSet rs, int arg1) throws SQLException{
				
				Map<String, String> map = new HashMap<String, String>();
				
				map.put("username", StringUtil.getStringValue(rs.getString("username")));
				
				String city_id = StringUtil.getStringValue(rs.getString("city_id"));
				String city_name = StringUtil.getStringValue(cityMap.get(city_id));
				if (false == StringUtil.IsEmpty(city_name)){
					map.put("city_name", city_name);
				}else{
					map.put("city_name", "");
				}
				map.put("user_id", StringUtil.getStringValue(rs.getString("user_id")));
				
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
				
				String device_id = StringUtil.getStringValue(rs.getString("device_id"));
				if(false == StringUtil.IsEmpty(device_id)){
					PrepareSQL pSql = new PrepareSQL();
					pSql.append("select vendor_id, device_model_id, devicetype_id ");
					pSql.append("  from tab_gw_device ");
					pSql.append(" where 1=1");
					pSql.append("   and device_id = '");
					pSql.append(device_id);
					pSql.append("'");
					
					List list = jt.queryForList(pSql.getSQL());
					if (false == list.isEmpty()) {
						Map rmap = (Map) list.get(0);
						
						String vendor_id = StringUtil.getStringValue(rmap.get("vendor_id"));  // 厂商ID
						String device_model_id = StringUtil.getStringValue(rmap.get("device_model_id"));  // 型号ID
						String devicetype_id = StringUtil.getStringValue(rmap.get("devicetype_id"));   // 版本ID
						
						if (false == StringUtil.IsEmpty(vendor_id)) {
							map.put("vendor_add", venderMap.get(vendor_id));  // 设备厂商
						}else {
							map.put("vendor_add", "-");  // 设备厂商
						}
						
						if (false == StringUtil.IsEmpty(device_model_id)) {
							map.put("device_model", deviceModelMap.get(device_model_id));  // 设备型号
						} else {
							map.put("device_model", "-");  // 设备型号
						}
						
						if(false == StringUtil.IsEmpty(devicetype_id)){
							map.put("softversion", deviceSoftVersionMap.get(devicetype_id));  // 设备版本
						} else {
							map.put("softversion", "-");   // 设备版本
						}
					}
					
				}else {
					map.put("vendor_add", "-");    // 设备厂商
					map.put("device_model", "-");  // 设备型号
					map.put("softversion", "-");   // 设备版本
				}
				
				return map;
			}
		});
		cityMap = null;
		userTypeMap = null;
		bindTypeMap = null;
		venderMap = null;
		deviceModelMap = null;
		deviceSoftVersionMap = null;
		
		return list;
	}
	
	
	/**
	 * 获取用户记录 bindFlag = 1 表示获取已绑定用户  bindFlag=2表示获取未绑定用户  否则 表示获取全部
	 * 
	 * @param bindFlag
	 * @param cityId
	 * @param starttime1
	 * @param endtime1
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @return
	 */
	public int getUserCount(String bindFlag, String cityId, String starttime1,
			String endtime1, int curPage_splitPage, int num_splitPage) {
		
		logger.debug("getUserCount({},{},{},{},{},{})",
				new Object[] { bindFlag, cityId, starttime1, endtime1,
						curPage_splitPage, num_splitPage });
		
		StringBuffer sql = new StringBuffer();
		if(DBUtil.GetDB()==3){
			sql.append("select count(*) ");
		}else{
			sql.append("select count(1) ");
		}
		
		sql.append("  from tab_hgwcustomer a ");
		sql.append(" where 1=1");
		
		// 数据库类型为Oracle
		if (DBUtil.GetDB() == 1) {
			sql.append("   and substr(username,length(username),1) = 'C' ");
		}else if(DBUtil.GetDB()==3){
			sql.append("   and substring(username,length(username),1) = 'C' ");
		}else {
			sql.append("   and substring(username,len(username),1) = 'C' ");
		}
		
		/**已绑定用户*/
		if("1".equals(bindFlag)){
			if (LipossGlobals.isOracle() || DBUtil.GetDB()==3) {
				sql.append("   and a.device_id is not null ");
			}else{
				sql.append("   and a.device_id != null and a.device_id != '' ");
			}
		/**未绑定用户*/
		}else if("2".equals(bindFlag)){
			if (LipossGlobals.isOracle() || DBUtil.GetDB()==3) {
				sql.append("   and a.device_id is null ");
			}else{
				sql.append("   and (a.device_id = null or a.device_id = '') ");
			}
		}
		
		
		if (false == StringUtil.IsEmpty(starttime1)){
			sql.append(" and a.opendate >= ").append(starttime1);
		}
		
		if (false == StringUtil.IsEmpty(endtime1)){
			sql.append(" and a.opendate <= ").append(endtime1);
		}
		
		if (false == StringUtil.IsEmpty(cityId)&&!"00".equals(cityId)){
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and a.city_id in (").append(StringUtils.weave(cityIdList)).append(")");
			cityIdList = null;
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
	 * excel导出
	 * 
	 * @param bindFlag
	 * @param cityId
	 * @param starttime1
	 * @param endtime1
	 * @return
	 */
	public List<Map> getUserExcel(String bindFlag, String cityId, String starttime1, String endtime1) 
	{
		logger.debug("getUserExcel({},{},{},{})", new Object[]{bindFlag, cityId, starttime1, endtime1});
		
		StringBuffer sql = new StringBuffer();
		
		sql.append("select a.device_id, a.city_id, a.username, a.user_id, a.user_type_id,a.oui, a.device_serialnumber, a.opendate, a.userline ");
		sql.append("  from tab_hgwcustomer a ");
		sql.append(" where 1=1");
		
		// 数据库类型为Oracle
		if (DBUtil.GetDB() == 1) {
			sql.append("   and substr(username,length(username),1) = 'C' ");
		}else if(DBUtil.GetDB() == 3){
			sql.append("   and substring(username,length(username),1) = 'C' ");
		}else {
			sql.append("   and substring(username,len(username),1) = 'C' ");
		}
		
		/**已绑定用户*/
		if("1".equals(bindFlag)){
			if (LipossGlobals.isOracle() || DBUtil.GetDB() == 3) {
				sql.append("   and a.device_id is not null ");
			}else{
				sql.append("   and a.device_id != null and a.device_id != '' ");
			}
		/**未绑定用户*/
		}else if("2".equals(bindFlag)){
			if (LipossGlobals.isOracle() || DBUtil.GetDB() == 3) {
				sql.append("   and a.device_id is null ");
			}else{
				sql.append("   and (a.device_id = null or a.device_id = '') ");
			}
		}
		
		
		if (false == StringUtil.IsEmpty(starttime1)){
			sql.append(" and a.opendate >= ").append(starttime1);
		}
		
		if (false == StringUtil.IsEmpty(endtime1)){
			sql.append(" and a.opendate <= ").append(endtime1);
		}
		
		if (false == StringUtil.IsEmpty(cityId)&&!"00".equals(cityId)){
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and a.city_id in (").append(StringUtils.weave(cityIdList)).append(")");
			cityIdList = null;
		}
		
		sql.append(" order by a.city_id ");
		
		PrepareSQL psql = new PrepareSQL(sql.toString());
		
		cityMap = CityDAO.getCityIdCityNameMap();
		userTypeMap = getUserType();
		bindTypeMap = getBindType();
		venderMap = getVenderMap();                        // 设备厂商
		deviceModelMap = getDeviceModelMap();              // 设备型号
		deviceSoftVersionMap = getDeviceSoftVersionMap();  // 设备版本
		
		List<Map> list = jt.query(psql.getSQL(), new RowMapper(){

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
				
				String device_id = StringUtil.getStringValue(rs.getString("device_id"));
				if(false == StringUtil.IsEmpty(device_id)){
					PrepareSQL pSql = new PrepareSQL();
					pSql.append("select vendor_id, device_model_id, devicetype_id ");
					pSql.append("  from tab_gw_device ");
					pSql.append(" where 1=1");
					pSql.append("   and device_id = '");
					pSql.append(device_id);
					pSql.append("'");
					
					List list = jt.queryForList(pSql.getSQL());
					if (false == list.isEmpty()) {
						Map rmap = (Map) list.get(0);
						
						String vendor_id = StringUtil.getStringValue(rmap.get("vendor_id"));  // 厂商ID
						String device_model_id = StringUtil.getStringValue(rmap.get("device_model_id"));  // 型号ID
						String devicetype_id = StringUtil.getStringValue(rmap.get("devicetype_id"));   // 版本ID
						
						if (false == StringUtil.IsEmpty(vendor_id)) {
							map.put("vendor_add", venderMap.get(vendor_id));  // 设备厂商
						}else {
							map.put("vendor_add", "-");  // 设备厂商
						}
						
						if (false == StringUtil.IsEmpty(device_model_id)) {
							map.put("device_model", deviceModelMap.get(device_model_id));  // 设备型号
						} else {
							map.put("device_model", "-");  // 设备型号
						}
						
						if(false == StringUtil.IsEmpty(devicetype_id)){
							map.put("softversion", deviceSoftVersionMap.get(devicetype_id));  // 设备版本
						} else {
							map.put("softversion", "-");   // 设备版本
						}
					}
					
				}else {
					map.put("vendor_add", "-");    // 设备厂商
					map.put("device_model", "-");  // 设备型号
					map.put("softversion", "-");   // 设备版本
				}
				return map;
			}
		});
		cityMap = null;
		userTypeMap = null;
		bindTypeMap = null;
		venderMap = null;
		deviceModelMap = null;
		deviceSoftVersionMap = null;
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
		if(DBUtil.GetDB()==3){
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
		if(DBUtil.GetDB()==3){
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
	public HashMap<String, String> getVenderMap() {
		
		logger.debug("getVenderMap()");
		
		StringBuffer sql = new StringBuffer("select vendor_id,vendor_name,vendor_add from tab_vendor");
		
		PrepareSQL psql = new PrepareSQL(sql.toString());
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
	 * 获取设备型号map
	 * 
	 * @param request
	 * @return ArrayList
	 */
	public HashMap<String, String> getDeviceModelMap() {

		logger.debug("getDeviceModelMap()");
		
		String devicemodel = "";
		String device_model_id = "";
		HashMap<String, String> deviceTypeMap = new HashMap<String,String>();

		PrepareSQL psql = new PrepareSQL();
		psql.append("select a.device_model_id, a.device_model from gw_device_model a  ");
    	
    	List<Map> list = jt.queryForList(psql.getSQL());
    	
    	for (Map<String, String> map : list) {
    		devicemodel = StringUtil.getStringValue(map.get("device_model"));
    		device_model_id = StringUtil.getStringValue(map.get("device_model_id"));
			deviceTypeMap.put(device_model_id, devicemodel);
		}
    	return deviceTypeMap;
	}
	
	
	/**
	 * 获取设备版本map
	 * 
	 * @param request
	 * @return ArrayList
	 */
	public HashMap<String, String> getDeviceSoftVersionMap() {

		logger.debug("getDeviceModelMap()");
		
		String softwareversion = "";
		String devicetype_id = "";
		HashMap<String, String> deviceTypeMap = new HashMap<String,String>();

		PrepareSQL psql = new PrepareSQL();
		psql.append("select b.softwareversion,b.devicetype_id from tab_devicetype_info b ");
    	
    	List<Map> list = jt.queryForList(psql.getSQL());
    	
    	for (Map<String, String> map : list) {
    		softwareversion = StringUtil.getStringValue(map.get("softwareversion"));
			devicetype_id = StringUtil.getStringValue(map.get("devicetype_id"));
			deviceTypeMap.put(devicetype_id, softwareversion);
		}
    	return deviceTypeMap;
	}
	
	
}
