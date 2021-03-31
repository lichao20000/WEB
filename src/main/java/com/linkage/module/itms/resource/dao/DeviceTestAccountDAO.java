package com.linkage.module.itms.resource.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.linkage.commons.db.DBUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;

public class DeviceTestAccountDAO  extends SuperDAO
{

	private static Logger logger = LoggerFactory.getLogger(DeviceTestAccountDAO.class);
	private Map<String, String> venderMap = null;       // 设备厂商
	private Map<String, String> deviceModelMap = null;  // 设备型号
	private Map<String, String> deviceSoftVersionMap = null;  // 设备版本
	private Map<String, String> devicehardwareversionMap = null;  // 设备硬件版本
	private Map<Long, String>  accnamp = null;
	/**
	 * 获取当前型号的所有硬件版本
	 * @param
	 * @param deviceModelId
	 * @return
	 */
	public List getDeviceHardVersion(String deviceModelId) {
		StringBuffer sql = new StringBuffer();
		sql.append("select distinct(hardwareversion) as hardwareversion from ");
		sql.append(" tab_devicetype_info where 1=1 ");
		if (!StringUtil.IsEmpty(deviceModelId) && !"-1".equals(deviceModelId)) {
			sql.append(" and device_model_id= ? ");
		}
		PrepareSQL sqlstr = new PrepareSQL(sql.toString());
		if (!StringUtil.IsEmpty(deviceModelId) && !"-1".equals(deviceModelId)) {
			sqlstr.setString(1, deviceModelId);
		}
		return jt.queryForList(sqlstr.getSQL());
	}

	public List getSoftVersion(String hardVercion, String deviceModelId) {
		StringBuffer pSQL = new StringBuffer();
		pSQL.append("select devicetype_id,softwareversion from tab_devicetype_info where 1=1 ");
		if (!StringUtil.IsEmpty(deviceModelId) && !"-1".equals(deviceModelId)) {
			pSQL.append("and device_model_id=? ");
		}
		if (!StringUtil.IsEmpty(hardVercion) && !"-1".equals(hardVercion)) {
			pSQL.append("and hardwareversion=? ");
		}
		PrepareSQL sqlstr = new PrepareSQL(pSQL.toString());
		int i= 1;
		if (!StringUtil.IsEmpty(deviceModelId) && !"-1".equals(deviceModelId)) {
			sqlstr.setString(i, deviceModelId);
			i=i+1;
		}
		if (!StringUtil.IsEmpty(hardVercion) && !"-1".equals(hardVercion)) {
			sqlstr.setString(i, hardVercion);
			i=i+1;
		}
		return jt.queryForList(sqlstr.getSQL());
	}

	/**
	 * 获取详细信息
	 * @param vendorId
	 * @param deviceModelId
	 * @param goal_devicetypeId
	 * @param endtime1 
	 * @param starttime1 
	 * @param num_splitPage 
	 * @param curPage_splitPage 
	 * @return
	 */
	public List<Map<String, String>> getList(String vendorId,
			String deviceModelId, String goal_devicetypeId, String starttime1, String endtime1, int curPage_splitPage, int num_splitPage) {
		StringBuffer pSQL = new StringBuffer();
		// teledb
		if (DBUtil.GetDB() == 3) {
			pSQL.append("select vendor_id, device_model_id, devicetype_id, device_serialnumber, complete_time, file_path, acc_oid from tab_device_test_account where 1=1 ");
		}
		else {
			pSQL.append("select * from tab_device_test_account where 1=1 ");
		}
		if (!StringUtil.IsEmpty(deviceModelId) && !"-1".equals(deviceModelId)) {
			pSQL.append("and device_model_id= ? ");
		}
		if (!StringUtil.IsEmpty(vendorId) && !"-1".equals(vendorId)) {
			pSQL.append("and vendor_id= ? ");
		}
		if (!StringUtil.IsEmpty(goal_devicetypeId) && !"-1".equals(goal_devicetypeId)) {
			pSQL.append("and devicetype_id= ? ");
		}
		if (!StringUtil.IsEmpty(starttime1) && !"-1".equals(starttime1)) {
			pSQL.append("and complete_time >= ? ");
		}
		if (!StringUtil.IsEmpty(endtime1) && !"-1".equals(endtime1)) {
			pSQL.append("and complete_time <= ? ");
		}
		
		pSQL.append("order by  complete_time  desc ");
		PrepareSQL pSQLstr = new PrepareSQL(pSQL.toString());
		int i = 1;
		if (!StringUtil.IsEmpty(deviceModelId) && !"-1".equals(deviceModelId)) {
			pSQLstr.setString(i,deviceModelId);
			i = i+1;
		}
		if (!StringUtil.IsEmpty(vendorId) && !"-1".equals(vendorId)) {
			pSQLstr.setString(i,vendorId);
			i = i+1;
		}
		if (!StringUtil.IsEmpty(goal_devicetypeId) && !"-1".equals(goal_devicetypeId)) {
			pSQLstr.setString(i,goal_devicetypeId);
			i = i+1;
		}
		if (!StringUtil.IsEmpty(starttime1) && !"-1".equals(starttime1)) {
			pSQLstr.setLong(i,StringUtil.getLongValue(starttime1));
			i = i+1;
		}
		if (!StringUtil.IsEmpty(endtime1) && !"-1".equals(endtime1)) {
			pSQLstr.setLong(i,StringUtil.getLongValue(endtime1));
			i = i+1;
		}
		
		venderMap = getVenderMap();                        // 设备厂商
		deviceModelMap = getDeviceModelMap();              // 设备型号
		deviceSoftVersionMap = getDeviceSoftVersionMap();  // 设备版本
		accnamp = getaccIDName();
		 List queryForList = querySP(pSQLstr.getSQL(), (curPage_splitPage - 1) * num_splitPage
					+ 1, num_splitPage, new RowMapper()
		{
				public Object mapRow(ResultSet rs, int arg1) throws SQLException{
								Map<String, String> map = new HashMap<String, String>();
								String vendor_id = StringUtil.getStringValue(rs.getString("vendor_id"));  // 厂商ID
								String device_model_id = StringUtil.getStringValue(rs.getString("device_model_id"));  // 型号ID
								String devicetype_id = StringUtil.getStringValue(rs.getString("devicetype_id"));   // 版本ID
								String device_serialnumber = StringUtil.getStringValue(rs.getString("device_serialnumber"));   // 版本ID
								String complete_time = StringUtil.getStringValue(rs.getLong("complete_time"));   // 版本ID
								long opertime = StringUtil.getLongValue(complete_time) * 1000L;
								DateTimeUtil dt = new DateTimeUtil(opertime);
								map.put("complete_time", dt.getLongDate());
								String file_path = StringUtil.getStringValue(rs.getString("file_path"));   // 版本ID
								String acc_oid = StringUtil.getStringValue(StringUtil.getLongValue(rs.getLong("acc_oid")));  
								map.put("accname", StringUtil.getStringValue(accnamp.get(StringUtil.getLongValue(acc_oid))));
								map.put("file_path", file_path);
								map.put("device_serialnumber", device_serialnumber);
								if (!StringUtil.IsEmpty(vendor_id)) {
									map.put("vendorName", venderMap.get(vendor_id));  // 设备厂商
								}else {
									map.put("vendorName", "-");  // 设备厂商
								}
								
								if (!StringUtil.IsEmpty(device_model_id)) {
									map.put("deviceModel", deviceModelMap.get(device_model_id));  // 设备型号
								} else {
									map.put("deviceModel", "-");  // 设备型号
								}
								
								if(!StringUtil.IsEmpty(devicetype_id)){
									map.put("softwareversion", deviceSoftVersionMap.get(devicetype_id));  // 设备版本
									map.put("hardwareversion", devicehardwareversionMap.get(devicetype_id));  // 设备硬件版本
								} else {
									map.put("softwareversion", "-");   // 设备版本
									map.put("hardwareversion", "-");   // 设备硬件版本
								}
				return map;
				}
			});
		 return queryForList;
	}
	


	private Map getaccIDName() {
		PrepareSQL sQL_user = new PrepareSQL();
		sQL_user.append("select acc_oid,acc_loginname from tab_accounts ");
		List<Map> userMap = jt.queryForList(sQL_user.toString());
		Map<Long,String> result = new HashMap<Long, String>();
		for (Map map : userMap) {
			result.put(StringUtil.getLongValue(map,"acc_oid"), StringUtil.getStringValue(map,"acc_loginname"));
		}
		return result;
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
		psql.getSQL();
		
		List<Map> list = jt.queryForList(sql.toString());
		
		HashMap<String, String> vendorMap = new HashMap<String, String>();
		
		for(Map<String, String> map : list){
			
			String vendorAdd = StringUtil.getStringValue(map.get("vendor_add"));
			
			if(false == StringUtil.IsEmpty(vendorAdd)){
				vendorMap.put(StringUtil.getStringValue(map.get("vendor_id")), vendorAdd);
			}else {
				vendorMap.put(StringUtil.getStringValue(map.get("vendor_id")), StringUtil.getStringValue(map.get("vendor_name")));
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
		psql.append("select a.device_model_id, a.device_model ");
		psql.append("  from gw_device_model a  ");
    	psql.getSQL();
    	
    	List<Map> list = jt.queryForList(psql.toString());
    	
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
		String hardwareversion = "";
		String devicetype_id = "";
		HashMap<String, String> deviceTypeMap = new HashMap<String,String>();
		devicehardwareversionMap = new HashMap<String,String>();

		PrepareSQL psql = new PrepareSQL();
		psql.append("select b.softwareversion,b.hardwareversion,b.devicetype_id");
		psql.append("  from tab_devicetype_info b ");
		
    	psql.getSQL();
    	
    	List<Map> list = jt.queryForList(psql.toString());
    	
    	for (Map<String, String> map : list) {
    		softwareversion = StringUtil.getStringValue(map.get("softwareversion"));
    		hardwareversion = StringUtil.getStringValue(map.get("hardwareversion"));
			devicetype_id = StringUtil.getStringValue(map.get("devicetype_id"));
			deviceTypeMap.put(devicetype_id, softwareversion);
			devicehardwareversionMap.put(devicetype_id, hardwareversion);
		}
    	return deviceTypeMap;
	}

	/**
	 * 新增设备测试台账
	 * @param device_serialnumber
	 * @param vendorId
	 * @param deviceModelId
	 * @param goal_devicetypeId
	 * @param gwShare_fileName
	 * @param userid 
	 * @return
	 */
	public String addTestAccountPath(String device_serialnumber,
			String vendorId, String deviceModelId, String goal_devicetypeId,
			String gwShare_fileName, long userid) {
		StringBuffer sql = new StringBuffer("insert into tab_device_test_account(id,device_serialnumber,vendor_id,device_model_id,"
				+ "devicetype_id,complete_time,file_path,acc_oid)values(?,?,?,?,?,?,?,?) ");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		long time = new DateTimeUtil().getLongTime();
		psql.setLong(1, time);
		psql.setString(2, device_serialnumber);
		psql.setString(3, vendorId);
		psql.setString(4, deviceModelId);
		psql.setString(5, goal_devicetypeId);
		psql.setLong(6, time);
		psql.setString(7, gwShare_fileName);
		psql.setLong(8, userid);
	
		psql.getSQL();
		int update = jt.update(psql.toString());
		if(1 == update){
			return "1";
		}
		return "0";
	}

	public int getListCount(String vendorId, String deviceModelId,
			String goal_devicetypeId, String starttime1, String endtime1, int curPage_splitPage, int num_splitPage) {
		StringBuffer pSQL = new StringBuffer();
		pSQL.append("select count(*) from tab_device_test_account where 1=1 ");
		if (!StringUtil.IsEmpty(deviceModelId) && !"-1".equals(deviceModelId)) {
			pSQL.append("and device_model_id= ? ");
		}
		if (!StringUtil.IsEmpty(vendorId) && !"-1".equals(vendorId)) {
			pSQL.append("and vendor_id= ? ");
		}
		if (!StringUtil.IsEmpty(goal_devicetypeId) && !"-1".equals(goal_devicetypeId)) {
			pSQL.append("and devicetype_id= ? ");
		}
		if (!StringUtil.IsEmpty(starttime1) && !"-1".equals(starttime1)) {
			pSQL.append("and complete_time >= ? ");
		}
		if (!StringUtil.IsEmpty(endtime1) && !"-1".equals(endtime1)) {
			pSQL.append("and complete_time <= ? ");
		}
		
		PrepareSQL pSQLstr = new PrepareSQL(pSQL.toString());
		int i = 1;
		if (!StringUtil.IsEmpty(deviceModelId) && !"-1".equals(deviceModelId)) {
			pSQLstr.setString(i,deviceModelId);
			i = i+1;
		}
		if (!StringUtil.IsEmpty(vendorId) && !"-1".equals(vendorId)) {
			pSQLstr.setString(i,vendorId);
			i = i+1;
		}
		if (!StringUtil.IsEmpty(goal_devicetypeId) && !"-1".equals(goal_devicetypeId)) {
			pSQLstr.setString(i,goal_devicetypeId);
			i = i+1;
		}
		if (!StringUtil.IsEmpty(starttime1) && !"-1".equals(starttime1)) {
			pSQLstr.setLong(i,StringUtil.getLongValue(starttime1));
			i = i+1;
		}
		if (!StringUtil.IsEmpty(endtime1) && !"-1".equals(endtime1)) {
			pSQLstr.setLong(i,StringUtil.getLongValue(endtime1));
			i = i+1;
		}
		int total = jt.queryForInt(pSQLstr.getSQL());
		int maxPage = 1;
		if (total % num_splitPage == 0){
			maxPage = total / num_splitPage;
		}else{
			maxPage = total / num_splitPage + 1;
		}
		
		return maxPage;
	}

}
