
package com.linkage.module.itms.resource.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.linkage.commons.db.DBUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.DBOperation;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.gw.VendorModelVersionDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.itms.resource.util.DeviceTypeUtil;
import com.linkage.system.utils.StringUtils;

public class DeviceWhiteListDAO extends SuperDAO
{

	private static Logger logger = LoggerFactory.getLogger(DeviceWhiteListDAO.class);
	
	private Map<String, String> cityMap = null;
	private HashMap<String, String> vendorMap = null;
	private HashMap<String, String> deviceModelMap = null;
	private HashMap<String, String> softwareversionMap = null;
	private HashMap<String, String> hardwareversionMap = null;

	/**
	 * 查询设备信息
	 * @param sqlSpell
	 */
	public  ArrayList<HashMap<String, String>> getDevIds(String sqlSpell){
		return DBOperation.getRecords(sqlSpell);
	}
	
	/**
	 * 增加任务信息
	 * @param name
	 * @param taskid
	 * @param accoid
	 * @param addtime
	 * @param starttime
	 * @param endtime
	 * @param enddate
	 * @return
	 */
	public int addTask(String task_name,long taskid, long accoid, String param, int type,int status){
		
		PrepareSQL psql = new PrepareSQL(
		"insert into tab_whitelist_task(task_id,task_name,acc_oid,add_time,param,type,status,update_time) values(?,?,?,?,?,?,?,?)");
		psql.setLong(1, taskid);
		psql.setString(2, task_name);
		psql.setLong(3, accoid);
		psql.setLong(4, taskid);
		psql.setString(5, param);
		psql.setInt(6, type);
		psql.setInt(7, status);
		psql.setLong(8, System.currentTimeMillis()/1000);
		
		return jt.update(psql.getSQL());
		
	}
	
	public int addWhiteList(long taskid){
		PrepareSQL psql = new PrepareSQL(
				"insert into tab_whitelist(device_id,device_serialnumber,add_time)  select a.device_id,a.device_serialnumber,"+System.currentTimeMillis()/1000+" from tab_whitelist_dev a where not exists (select * from tab_whitelist b where a.device_id=b.device_id and a.task_id=?) and a.task_id=?");
		psql.setLong(1, taskid);
		psql.setLong(2, taskid);
		return jt.update(psql.getSQL());
		
	}
	
	
	/**
	 * 把设备序列号插入临时表
	 * @param fileName
	 * @param dataList
	 */
	public void insertTmp(String fileName, List<String> dataList) {
		ArrayList<String> sqlList = new ArrayList<String>();
		PrepareSQL psql = null;
		for (int i = 0; i < dataList.size(); i++) {
			psql = new PrepareSQL();
			psql.append("insert into tab_temporary_device"
					+ "(filename,device_serialnumber)" + " values ('" + fileName + "','"
					+ dataList.get(i) + "')");
			sqlList.add(psql.getSQL());
		}
		int res = 0;
		if (LipossGlobals.inArea(Global.JSDX)) {
			res = DBOperation.executeUpdate(sqlList, "proxool.xml-report");
		} else {
			ArrayList<String> sqlListTmp = new ArrayList<String>();
			if(sqlList.size()>0){
				for(String sql : sqlList){
					sqlListTmp.add(sql);
					if(sqlListTmp.size()>=200){
						int resTmp = DBOperation.executeUpdate(sqlListTmp);
						if(resTmp>0){
							res += sqlListTmp.size();
						}
						sqlListTmp.clear();
					}
				}
			}
			if(sqlListTmp.size()>0){
				int resTmp = DBOperation.executeUpdate(sqlListTmp);
				if(resTmp>0){
					res += sqlListTmp.size();
				}
				sqlListTmp.clear();
			}
			logger.info("====成功插入tab_temporary_device表"+res+"条数据====");
		}
	}
	
	
	/**
	 * 获取设备信息
	 * @param serList
	 * @param filename
	 * @return
	 */
	public ArrayList<HashMap<String, String>> getTaskDevList(String filename) {

		PrepareSQL psql = new PrepareSQL();
		
		psql.append(" select a.device_id, a.oui, a.device_serialnumber ");
		psql.append(" from tab_gw_device a ,tab_temporary_device b where  ");
		psql.append(" a.device_id=b.device_serialnumber  ");
		psql.append(" and b.filename='"+filename+"'");
		
		
		ArrayList<HashMap<String, String>> list = DBOperation.getRecords(psql.getSQL());
		if(null==list || list.size()==0){
			return null;
		}
		
		return list;
	}
	
	public ArrayList<String> sqlList(List<HashMap<String, String>> devList,long taskid){
		ArrayList<String> sqlList = new ArrayList<String>();
		for (int i = 0; i < devList.size(); i++) {
			String device_serialnumber = String.valueOf(devList.get(i).get("device_serialnumber"));
			String device_id = String.valueOf(devList.get(i).get("device_id"));
			String oui = String.valueOf(devList.get(i).get("oui"));
			
			PrepareSQL pSql = new PrepareSQL();
			pSql.append(" insert into tab_whitelist_dev (task_id,device_id,device_serialnumber,oui,status,add_time)");
			pSql.append("  values(?,?,?,?,?,?)");
			
			pSql.setLong(1, taskid);
			pSql.setString(2, device_id);
			pSql.setString(3, device_serialnumber);
			pSql.setString(4, oui);
			pSql.setLong(5, 0);
			pSql.setLong(6, System.currentTimeMillis()/1000);
			
			sqlList.add(pSql.getSQL());
		}
		return sqlList;
	}
	
	
	/**
	 * 批量插入设备信息
	 * @param sqlList
	 */
	public int insertTaskDev(ArrayList<String> sqlList){
		return DBOperation.executeUpdate(sqlList);
	}
	

	public List<Map> getWhiteDeviceList(int curPage_splitPage, int num_splitPage,String cityId,String vendor_id,String device_model_id,
			String devicetype_id,String deviceSerialnumber)
	{
		PrepareSQL pSql = new PrepareSQL();

		// teledb
		if (DBUtil.GetDB() == 3) {
			pSql.setSQL("select device_id, city_id, vendor_id, devicetype_id, device_model_id, oui, device_serialnumber from tab_whitelist a,tab_gw_device b where a.device_serialnumber=b.device_serialnumber"
					+ " and 1=1");
		}
		else {
			pSql.setSQL("select * from tab_whitelist a,tab_gw_device b where a.device_serialnumber=b.device_serialnumber"
					+ " and 1=1");
		}
		if (!StringUtil.IsEmpty(deviceSerialnumber))
		{
			if (deviceSerialnumber.length() > 5)
			{
				pSql.append(" and b.dev_sub_sn ='"+ deviceSerialnumber.substring(deviceSerialnumber.length() - 6,
								deviceSerialnumber.length()) + "'");
			}
			pSql.append(" and b.device_serialnumber like '%" + deviceSerialnumber + "'");
		}
		if (!StringUtil.IsEmpty(vendor_id) && !"-1".equals(vendor_id))
		{
			pSql.append(" and b.vendor_id = '" + vendor_id + "'");
		}
		if (!StringUtil.IsEmpty(device_model_id) && !"-1".equals(device_model_id))
		{
			pSql.append(" and b.device_model_id = '" + device_model_id + "'");
		}
		if (!StringUtil.IsEmpty(devicetype_id) && !"-1".equals(devicetype_id))
		{
			pSql.append(" and b.devicetype_id = " + devicetype_id + "");
		}
		
		if(!"00".equals(cityId) && !"-1".equals(cityId)){
			pSql.append("   and b.city_id in (");
			List list = CityDAO.getAllNextCityIdsByCityPid(cityId);
			pSql.append(StringUtils.weave(list));
			pSql.append(") ");
			list = null;
		}
		
		cityMap = CityDAO.getCityIdCityNameMap();
		vendorMap = VendorModelVersionDAO.getVendorMap();
		deviceModelMap = VendorModelVersionDAO.getDeviceModelMap();
		softwareversionMap = VendorModelVersionDAO.getDevicetypeMap();
		hardwareversionMap = VendorModelVersionDAO.getDeviceTypeInfoMap();
		List<Map> list = querySP(pSql.getSQL(), (curPage_splitPage - 1) * num_splitPage + 1,
			num_splitPage, new RowMapper()
			{

				public Object mapRow(ResultSet rs, int arg1) throws SQLException
				{
					Map<String, String> map = new HashMap<String, String>();
					map.put("device_id", rs.getString("device_id"));
					String city_id = rs.getString("city_id");
					String city_name = StringUtil.getStringValue(cityMap.get(city_id));
					if (false == StringUtil.IsEmpty(city_name))
					{
						map.put("city_name", city_name);
					}
					else
					{
						map.put("city_name", "");
					}
					String vendor_id = rs.getString("vendor_id");
					String vendor_add = StringUtil.getStringValue(vendorMap.get(vendor_id));
					if (false == StringUtil.IsEmpty(vendor_add))
					{
						map.put("vendor_add", vendor_add);
					}
					else
					{
						map.put("vendor_add", "");
					}
					map.put("softwareversion", softwareversionMap.get(rs.getString("devicetype_id")));
					map.put("hardwareversion", hardwareversionMap.get(rs.getString("devicetype_id")));
					String device_model_id = rs.getString("device_model_id");
					String device_model = StringUtil.getStringValue(deviceModelMap.get(device_model_id));
					if (false == StringUtil.IsEmpty(device_model))
					{
						map.put("device_model", device_model);
					}
					else
					{
						map.put("device_model", "");
					}
					map.put("oui", rs.getString("oui"));
					map.put("device_serialnumber", rs.getString("device_serialnumber"));
					map.put("device", rs.getString("oui") + "-"+ rs.getString("device_serialnumber"));
					return map;
				}
			});
		cityMap = null;
		vendorMap = null;
		deviceModelMap = null;
		softwareversionMap = null;
		hardwareversionMap = null;
		return list;
	}
		
	
	public int getWhiteDeviceListCount(String cityId,String vendor_id,String device_model_id,
			String devicetype_id,String deviceSerialnumber)
	{
		PrepareSQL pSql = new PrepareSQL();
		pSql.setSQL("select count(1) from tab_whitelist a,tab_gw_device b where a.device_serialnumber=b.device_serialnumber"
					+ " and 1=1");
		if (!StringUtil.IsEmpty(deviceSerialnumber))
		{
			if (deviceSerialnumber.length() > 5)
			{
				pSql.append(" and b.dev_sub_sn ='"+ deviceSerialnumber.substring(deviceSerialnumber.length() - 6,
								deviceSerialnumber.length()) + "'");
			}
			pSql.append(" and b.device_serialnumber like '%" + deviceSerialnumber + "'");
		}
		if (!StringUtil.IsEmpty(vendor_id) && !"-1".equals(vendor_id))
		{
			pSql.append(" and b.vendor_id = '" + vendor_id + "'");
		}
		if (!StringUtil.IsEmpty(device_model_id) && !"-1".equals(device_model_id))
		{
			pSql.append(" and b.device_model_id = '" + device_model_id + "'");
		}
		if (!StringUtil.IsEmpty(devicetype_id) && !"-1".equals(devicetype_id))
		{
			pSql.append(" and b.devicetype_id = " + devicetype_id + "");
		}
		
		if(!"00".equals(cityId) && !"-1".equals(cityId)){
			pSql.append("   and b.city_id in (");
			List list = CityDAO.getAllNextCityIdsByCityPid(cityId);
			pSql.append(StringUtils.weave(list));
			pSql.append(") ");
			list = null;
		}
		return jt.queryForInt(pSql.getSQL());
	}
	
	public int deleteDevice(String device_serialnumber)
	{
		PrepareSQL pSQL = new PrepareSQL();
		pSQL.setSQL("delete from tab_whitelist where device_serialnumber =?");
		pSQL.setString(1, device_serialnumber);
		return jt.update(pSQL.getSQL());
	}
}
