package com.linkage.module.gwms.config.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.DBOperation;
import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.litms.resource.DeviceAct;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.gw.VendorModelVersionDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class SetMulticastBatchDAO extends SuperDAO 
{
	private static Logger logger = LoggerFactory.getLogger(SetMulticastBatchDAO.class);
	private SimpleDateFormat sdfDay = new SimpleDateFormat("yyyy-MM-dd");
	private SimpleDateFormat sdfSec = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private Map<String,String> fault_Map = null;
	
	public void getfaultMap()
	{
		fault_Map = new HashMap<String,String>();
		PrepareSQL psql = new PrepareSQL(" select fault_code,fault_desc  from tab_cpe_faultcode ");
		List<HashMap<String,String>> faultCodeList = DBOperation.getRecords(psql.getSQL());
		if(null!=faultCodeList){
			for (HashMap<String,String> map : faultCodeList)
			{
				fault_Map.put(map.get("fault_code"), map.get("fault_desc"));
			}
			//业务错误码（cpe错误码表不包含的）
			fault_Map.put("-100", "桥接设备未匹配到带宽，无法测速");
			fault_Map.put("-101", "没有获取到WAN通道信息");
			fault_Map.put("-102", "设备没有上网通道");
			fault_Map.put("-103", "设备在测速黑名单中");
		}
	}

	/**
	 * @param fileName
	 * @param dataList
	 * @param importQueryField
	 * @return
	 */
	public void insertTmp(String fileName, List<String> dataList, String importQueryField) {
		ArrayList<String> sqlList = new ArrayList<String>();
		PrepareSQL psql = null;
		if ("username".equals(importQueryField)) {
			for (int i = 0; i < dataList.size(); i++) {
				psql = new PrepareSQL(" insert into tab_seniorquery_tmp(filename,username) ");
				psql.append(" values('" + fileName + "','" + dataList.get(i) + "') ");
				sqlList.add(psql.getSQL());
			}
		}else{
			for (int i = 0; i < dataList.size(); i++) {
				psql = new PrepareSQL(" insert into tab_seniorquery_tmp(filename,devicesn) ");
				psql.append(" values('" + fileName + "','" + dataList.get(i) + "') ");
				sqlList.add(psql.getSQL());
			}
		}
		DBOperation.executeUpdate(sqlList);
	}

	/**
	 * @param fileName
	 * @return
	 */
	public ArrayList<HashMap<String, String>> queryDeviceByImportUsername(String fileName, String importQueryField) {
		logger.debug("SetMulticastBatchDAO.queryDeviceByImportUsername()");
		PrepareSQL pSQL = new PrepareSQL();
		
//		if("username".equals(importQueryField)){
//			pSQL.append(" select a.username file_username,b.username loid,b.user_id,c.city_id,c.device_id,c.device_serialnumber,c.oui ");
//			pSQL.append(" ,d.username net_username from tab_seniorquery_tmp a left join tab_hgwcustomer b ");
//			pSQL.append(" on (a.username=b.username and b.user_state in ('1','2')) left join tab_gw_device c ");
//			pSQL.append(" on (b.device_id=c.device_id) left join hgwcust_serv_info d on ");
//			pSQL.append(" (b.user_id=d.user_id and d.serv_type_id=11 and d.serv_status=1) where a.filename=? ");
//			pSQL.setString(1, fileName);
//		}else{
//			pSQL.append(" select a.devicesn file_username,c.username loid,c.user_id,b.city_id,b.device_id,b.device_serialnumber,b.oui ");
//			pSQL.append(" ,d.username net_username from tab_seniorquery_tmp a left join tab_gw_device b ");
//			pSQL.append(" on (a.devicesn=b.device_serialnumber) left join tab_hgwcustomer c ");
//			pSQL.append(" on (b.device_id=c.device_id and c.user_state in ('1','2')) left join hgwcust_serv_info d ");
//			pSQL.append(" on (c.user_id=d.user_id and d.serv_type_id=11 and d.serv_status=1) where a.filename=? ");
//			pSQL.setString(1, fileName);
//		}
		
		if("username".equals(importQueryField)){
			//TODO wait
			pSQL.append(" select a.username file_username,b.username loid,b.user_id,c.city_id,c.device_id,c.device_serialnumber,c.oui ");
			pSQL.append(" from tab_seniorquery_tmp a left join tab_hgwcustomer b ");
			pSQL.append(" on (a.username=b.username and b.user_state in ('1','2')) left join tab_gw_device c ");
			pSQL.append(" on (b.device_id=c.device_id) where a.filename=? ");
			pSQL.setString(1, fileName);
		}else{
			//TODO wait
			pSQL.append(" select a.devicesn file_username,c.username loid,c.user_id,b.city_id,b.device_id,b.device_serialnumber,b.oui ");
			pSQL.append(" from tab_seniorquery_tmp a left join tab_gw_device b ");
			pSQL.append(" on (a.devicesn=b.device_serialnumber) left join tab_hgwcustomer c ");
			pSQL.append(" on (b.device_id=c.device_id and c.user_state in ('1','2')) where a.filename=? ");
			pSQL.setString(1, fileName);
		}

		return DBOperation.getRecords(pSQL.getSQL());
	}

	/**
	 * 获取今天总数
	 */
	public long getTodayCount() 
	{
		long startTime = getStartTime();

		PrepareSQL pSQL = new PrepareSQL();
		if(DBUtil.GetDB()==3){
			pSQL.append(" select count(*) allnum ");
		}else{
			pSQL.append(" select count(1) allnum ");
		}
		pSQL.append("from tab_setmulticast_dev where settime>="+startTime+" and settime<"+(startTime + 86400));

		return StringUtil.getLongValue(DBOperation.getRecord(pSQL.getSQL()), "allnum", 0);
	}

	/**
	 * @param sql
	 * @return
	 */
	public int excuteSingle(String sql) {

		logger.debug("SetMulticastBatchDAO.excuteBatch()");
		return DBOperation.executeUpdate(sql);
	}

	/**
	 * @param sqlList
	 * @return
	 */
	public int excuteBatch(ArrayList<String> sqlList) {

		logger.debug("SetMulticastBatchDAO.excuteBatch()");
		return DBOperation.executeUpdate(sqlList);
	}

	public long getStartTime() {
		try {
			String day = sdfDay.format(new Date());
			return sdfSec.parse(day + " 00:00:00").getTime() / 1000L;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	public int doConfig (long accOid, List<String> list, String serviceId,
			long time, String faultList, String taskname) 
	{
		ArrayList<String> sqlList = new ArrayList<String>();

		PrepareSQL psql = new PrepareSQL();
		psql.append("insert into tab_setmulticast_task(task_id,task_name,acc_oid,add_time,service_id) ");
		psql.append("values(?,?,?,?,?) ");
		psql.setLong(1, time);
		psql.setString(2, taskname);
		psql.setLong(3, accOid);
		psql.setLong(4, time);
		psql.setInt(5, StringUtil.getIntegerValue(serviceId));
		sqlList.add(psql.getSQL());

		if (null!=list && list.size()>0) {
			for (String infoStr : list) {
				String[] infoStrArr = infoStr.split("##");
				if (null != infoStrArr && infoStrArr.length >= 4) {
					
					String devCityId = infoStrArr[0];
					String deviceId = infoStrArr[1];
					String devSn = infoStrArr[2];
					String fileUsername = infoStrArr[3];
					
					psql = new PrepareSQL("insert into tab_setmulticast_task_dev(task_id,file_username,");
				    psql.append("device_id,device_serialnumber,city_id,add_time,update_time) ");
					psql.append("values(?,?,?,?,?,?,?) ");
					
				    psql.setLong(1, time);
				    psql.setString(2, fileUsername);
				    psql.setString(3, deviceId);
					psql.setString(4, devSn);
					psql.setString(5, devCityId);
					psql.setLong(6, time);	
					psql.setLong(7, time);
					
					sqlList.add(psql.getSQL());
				}
			}
		}
		
		if(!StringUtil.IsEmpty(faultList) && faultList.contains("##")){
			String[] faultListArr = faultList.split(";");
			for (String faultInfo : faultListArr) {
				String[] faultInfoArr = faultInfo.split("##");
				if (null != faultInfoArr && faultInfoArr.length >= 2) {
					
					String fileUsername = faultInfoArr[0];
					String retMessage = faultInfoArr[1];
					
					psql = new PrepareSQL("insert into tab_setmulticast_task_dev(task_id,");
					psql.append("file_username,add_time,update_time,res) ");
					psql.append("values(?,?,?,?,?) ");
					
					psql.setLong(1, time);
					psql.setString(2, fileUsername);
					psql.setLong(3, time);
					psql.setLong(4, time);
					psql.setString(5, retMessage);
					
					sqlList.add(psql.getSQL());
				}
			}
		}
		
		if(sqlList.size()>0){
			return excuteBatch(sqlList);
		}	
		return 0;
	}
	
	
	/**
	 * 查看定制详细信息
	 * 
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @param startTime
	 * @param endTime
	 * @param taskName
	 * @param acc_oid
	 * @param accName
	 * @return
	 */
	public List getOrderTaskList(int curPage_splitPage, int num_splitPage,
			String startTime, String endTime, String taskname,long acc_oid,String accName) 
	{
		PrepareSQL psql = new PrepareSQL();
		psql.append("select a.task_id,a.task_name,a.acc_oid,a.add_time,a.service_id,b.acc_loginname ");
		psql.append("from tab_setmulticast_task a,tab_accounts b where a.acc_oid=b.acc_oid ");
		
		if(!StringUtil.IsEmpty(accName)){
			psql.append(" and b.acc_loginname like '%" + accName + "%'");
		}
		if (!StringUtil.IsEmpty(taskname)) {
			psql.append(" and a.task_name like '%" + taskname + "%'");
		}
		if (!StringUtil.IsEmpty(startTime)) {
			psql.append(" and a.add_time>=" + getTime(startTime));
		}
		if (!StringUtil.IsEmpty(endTime)) {
			psql.append(" and a.add_time<=" + getTime(endTime));
		}
		if(1L != acc_oid){
			psql.append(" and b.acc_oid=" + acc_oid);
		}
		
		psql.append(" order by a.add_time desc");
		
		List list = querySP(psql.getSQL(), (curPage_splitPage - 1)
				* num_splitPage+1, num_splitPage, new RowMapper() {

			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				
				Map<String, String> map = new HashMap<String, String>();
				
				map.put("acc_loginname", rs.getString("acc_loginname"));
				map.put("task_name", StringUtil.getStringValue(rs.getString("task_name")));
				map.put("task_id", rs.getString("task_id"));
				
				Date date = new Date();
				try {
					date.setTime(rs.getLong("add_time") * 1000L);
					map.put("add_time", sdfSec.format(date));
				} catch (Exception e) {
					map.put("add_time", "");
				}
				
				try {
					date.setTime(rs.getLong("update_time") * 1000L);
					map.put("update_time", sdfSec.format(date));
				} catch (Exception e) {
					map.put("update_time", "");
				}
				
				return map;
			}
		});
		
		return list;
	}
	
	/**
	 * 获取任务结果
	 * @param taskId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map> getTaskResult(String taskId,int curPage_splitPage,int num_splitPage) 
	{
		DeviceAct devAct = new DeviceAct();
		final Map<String,String> cityMap = devAct.getCityMap_All();
		PrepareSQL pSql = new PrepareSQL();
		pSql.append("select file_username,device_id,device_serialnumber,city_id,");
		pSql.append("result_id,status,add_time,update_time,res ");
		pSql.append("from tab_setmulticast_task_dev where device_id is not null and task_id=" + taskId);
		
		List<Map> list = querySP(pSql.getSQL(), (curPage_splitPage - 1)
				* num_splitPage+1, num_splitPage, new RowMapper() {
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				Map<String, String> map = new HashMap<String, String>();
				int status = rs.getInt("status");
				int resultId = rs.getInt("result_id");
				
				if (status == 0) {
					map.put("status", "尚未进行下发");
					map.put("res", "尚未进行下发");
				} else if (status == 100 && resultId==1) {
					map.put("status", "下发成功");
					map.put("res", "下发成功");
				} else {
					map.put("status", "下发失败");
					map.put("res", "下发失败");
				}
				
				map.put("file_username", StringUtil.getStringValue(rs.getString("file_username")));
				map.put("device_id", StringUtil.getStringValue(rs.getString("device_id")));
				map.put("device_serialnumber", StringUtil.getStringValue(rs.getString("device_serialnumber")));
				
				map.put("city_id", StringUtil.getStringValue(cityMap, StringUtil.getStringValue(rs.getString("city_id"))));
				map.put("result_id", StringUtil.getStringValue(rs.getInt("result_id")));
				
				Date date = new Date();
				try {
					date.setTime(rs.getLong("add_time") * 1000L);
					map.put("add_time", sdfSec.format(date));
				} catch (Exception e) {
					map.put("add_time", "");
				}
				
				try {
					date.setTime(rs.getLong("update_time") * 1000L);
					map.put("update_time", sdfSec.format(date));
				} catch (Exception e) {
					map.put("update_time", "");
				}
				return map;
			}
		});
		
		return list;
	}
	
	
	/**
	 * @param taskId
	 * @return
	 */
	public Map<String, String> getTaskDetail(String taskId) 
	{
		PrepareSQL psql = new PrepareSQL();
		if(DBUtil.GetDB()==3){
			psql.append("select a.task_name,a.add_time,a.update_time,b.acc_loginname ");
		}else{
			psql.append("select a.*,b.acc_loginname ");
		}
		psql.append("from tab_setmulticast_task a,tab_accounts b ");
		psql.append("where a.task_id=? and a.acc_oid=b.acc_oid ");
		psql.setLong(1, Long.parseLong(taskId));
		
		Map<String, String> resultMap = DBOperation.getRecord(psql.getSQL());
		
		if(null==resultMap || resultMap.size()==0){
			return null;
		}
		
		Date date = new Date();
		try {
			date.setTime(StringUtil.getLongValue(resultMap.get("add_time")) * 1000L);
			resultMap.put("add_time", sdfSec.format(date));
		} catch (Exception e) {
			resultMap.put("add_time", "");
		}
		try {
			date.setTime(Long.parseLong(resultMap.get("update_time")) * 1000L);
			resultMap.put("update_time", sdfSec.format(date));
		} catch (Exception e) {
			resultMap.put("update_time", "");
		}
		
		return resultMap;
	}
	
	/**
	 * 获取定制的总页数
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @param startTime
	 * @param endTime
	 * @param cityId
	 * @param taskName
	 * @return
	 */
	 
	public int countOrderTask(int curPage_splitPage, int num_splitPage,
			String startTime, String endTime, String taskname,long acc_oid,String accName) 
	{

		PrepareSQL psql = new PrepareSQL();
		if(DBUtil.GetDB()==3){
			psql.append("select count(*) allnum ");
		}else{
			psql.append("select count(1) allnum ");
		}
		psql.append("from tab_setmulticast_task a,tab_accounts b where a.acc_oid=b.acc_oid ");
		
		if(!StringUtil.IsEmpty(accName)){
			psql.append(" and b.acc_loginname like '%" + accName + "%'");
		}
		if (!StringUtil.IsEmpty(taskname)) {
			psql.append(" and a.task_name like '%" + taskname + "%'");
		}
		if (!StringUtil.IsEmpty(startTime)) {
			psql.append(" and a.add_time>=" + getTime(startTime));
		}
		if (!StringUtil.IsEmpty(endTime)) {
			psql.append(" and a.add_time<=" + getTime(endTime));
		}
		if(1L != acc_oid){
			psql.append(" b.acc_oid=" + acc_oid);
		}
		
		int total = StringUtil.getIntValue(DBOperation.getRecord(psql.getSQL()), "allnum", 0);
		int maxPage = 1;
		if (total % num_splitPage == 0) {
			maxPage = total / num_splitPage;
		} else {
			maxPage = total / num_splitPage + 1;
		}
		return maxPage;
	}
	
	/**
	 * 
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @param taskId
	 * @return
	 */
	public int countTaskResult(int curPage_splitPage, int num_splitPage,String taskId) 
	{
		StringBuffer sql = new StringBuffer();
		if(DBUtil.GetDB()==3){
			sql.append("select count(*) ");
		}else{
			sql.append("select count(1) ");
		}
		sql.append("from tab_setmulticast_task_dev ");
		sql.append("where device_id is not null and task_id="+taskId);
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
	
	/**
	 * 删除任务
	 */	
	public String doDelete(String taskId) 
	{
		List<String> sqllist = new ArrayList<String>();
		PrepareSQL taskSql = new PrepareSQL("delete from tab_setmulticast_task where task_id=?");
		taskSql.setLong(1, Long.parseLong(taskId));
		
		sqllist.add(taskSql.getSQL());
		
		taskSql = new PrepareSQL("delete from tab_setmulticast_task_dev where task_id=?");
		
		taskSql.setLong(1, Long.parseLong(taskId));
		
		sqllist.add(taskSql.getSQL());
		int[] ier = doBatch(sqllist);
		if (ier != null && ier.length > 0) {
			logger.debug("删除任务：  成功");
			return "1";
		} else {
			logger.debug("删除任务：  失败");
			return "0";
		}
	}
	
	/**
	 * 导出设备列表
	 * 
	 * @author wangsenbo
	 * @date Nov 18, 2009
	 * @return List<Map>
	 */
	public List<Map> getInfoExcelDevice(String taskId)
	{
		logger.debug("getInfoExcelDevice({})", taskId);
		PrepareSQL pSql = new PrepareSQL();
		if(DBUtil.GetDB()==3){
			pSql.append("select status,result_id,city_id,add_time,update_time,device_serialnumber ");
		}else{
			pSql.append("select * ");
		}
		pSql.append("from tab_setmulticast_task_dev ");
		pSql.append("where device_id is not null and task_id="+taskId);
		List<HashMap<String, String>> list = DBOperation.getRecords(pSql.getSQL());
		
		if(null==list || list.size()==0){
			return null;
		}
		
		DeviceAct devAct = new DeviceAct();
		final Map<String,String> cityMap = devAct.getCityMap_All();
		
		for(HashMap<String, String> map : list)
		{
			String status = StringUtil.getStringValue(map, "status", "0");
			String resultId = StringUtil.getStringValue(map, "result_id", "");
			
			if ("0".equals(status)) {
				map.put("status", "尚未进行下发");
				map.put("res", "尚未进行下发");
			} else if ("100".equals(status) && "1".equals(resultId)) {
				map.put("status", "下发成功");
				map.put("res", "下发成功");
			} else {
				map.put("status", "下发失败");
				map.put("res", "下发失败");
			}
			
			map.put("city_id", StringUtil.getStringValue(cityMap, StringUtil.getStringValue(map, "city_id", "")));
			
			Date date = new Date();
			
			try {
				date.setTime(StringUtil.getLongValue(map, "add_time", 0) * 1000L);
				map.put("add_time", sdfSec.format(date));
			} catch (Exception e) {
				map.put("add_time", "");
			}
			
			try {
				date.setTime(Long.parseLong(map.get("update_time")) * 1000L);
				map.put("update_time", sdfSec.format(date));
			} catch (Exception e) {
				map.put("update_time", "");
			}
		}
		List<Map> List_new = new ArrayList<Map>();
		List_new.addAll(list);
		return List_new;
	}
	
	private long getTime(String date) {
		DateTimeUtil dt = new DateTimeUtil(date);
		return dt.getLongTime();
		
	}
	
	/**
	 * 查询设备信息
	 * @param sqlSpell
	 */
	public  ArrayList<HashMap<String, String>> getDevIds4NX(String sqlSpell){
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
	public int addTask4NX(String taskname,long taskid, long accoid, String param, 
			int type, String serviceId, String strategy_type)
	{
		
		PrepareSQL psql = new PrepareSQL();
		psql.append("insert into tab_setmulticast_task(task_id,task_name,acc_oid,");
		psql.append("add_time,service_id,param,type,strategy_type) ");
		psql.append("values(?,?,?,?,?, ?,?,?) ");
		psql.setLong(1, taskid);
		psql.setString(2, taskname);
		psql.setLong(3, accoid);
		psql.setLong(4, taskid);
		psql.setInt(5, StringUtil.getIntegerValue(serviceId));
		psql.setString(6, param);
		psql.setInt(7, type);
		psql.setInt(8, StringUtil.getIntegerValue(strategy_type));
		
		return jt.update(psql.getSQL());
		
	}
	
	/**
	 * 获取设备信息
	 * @param serList
	 * @param filename
	 * @return
	 */
	public ArrayList<HashMap<String, String>> getTaskDevList4NX(String filename) 
	{
		PrepareSQL psql = new PrepareSQL();
		
//		psql.append(" select a.device_serialnumber a_device_id, b.device_id, b.oui, b.device_serialnumber, d.wan_type, ");
//		psql.append(" d.username serusername, c.username, b.city_id from tab_temporary_device a left join tab_gw_device b on ");
//		psql.append(" a.device_serialnumber=b.device_id left join tab_hgwcustomer c on b.device_id=c.device_id ");
//		psql.append(" left join hgwcust_serv_info d on (c.user_id=d.user_id and d.serv_type_id=11 and d.serv_status=1) ");
//		psql.append("  where a.filename=? order by d.opendate desc ");
		
	/**	if(DBUtil.GetDB()==3){
			//TODO wait
		}else{
			
		} */
		psql.append("select a.device_serialnumber a_device_id,b.device_id,b.oui,b.device_serialnumber,");
		psql.append("c.username,b.city_id from tab_temporary_device a left join tab_gw_device b on ");
		psql.append("a.device_serialnumber=b.device_id left join tab_hgwcustomer c on b.device_id=c.device_id ");
		psql.append("where a.filename=? ");
		psql.setString(1, filename);
		
		ArrayList<HashMap<String, String>> list = DBOperation.getRecords(psql.getSQL());
		
		return list;
	}
	
	public ArrayList<String> sqlList(List<HashMap<String, String>> devList,String name,long taskid)
	{
		ArrayList<String> sqlList = new ArrayList<String>();
		for (int i = 0; i < devList.size(); i++) {
			String device_serialnumber = String.valueOf(devList.get(i).get("device_serialnumber"));
			String a_device_id = String.valueOf(devList.get(i).get("a_device_id"));
			String device_id = String.valueOf(devList.get(i).get("device_id"));
			String city_id = String.valueOf(devList.get(i).get("city_id"));
			PrepareSQL pSql = new PrepareSQL();
			
			pSql = new PrepareSQL();
			pSql.append("insert into tab_setmulticast_task_dev(task_id,file_username,device_id,");
			pSql.append("device_serialnumber,city_id,add_time,update_time) ");
			pSql.append("values(?,?,?,?,?,?,?) ");
			pSql.setLong(1, taskid);
			pSql.setString(2, a_device_id);
			pSql.setString(3, device_id);
			pSql.setString(4, device_serialnumber);
			pSql.setString(5, city_id);
			pSql.setLong(6, taskid);	
			pSql.setLong(7, taskid);
			
			sqlList.add(pSql.getSQL());
		}
		return sqlList;
	}
	
	/**
	 * 批量插入设备信息
	 * @param sqlList
	 */
	public int insertTaskDev4NX(ArrayList<String> sqlList){
		return DBOperation.executeUpdate(sqlList);
	}
	
	/**
	 * 把设备序列号插入临时表
	 * @param fileName
	 * @param dataList
	 */
	public void insertTmp(String fileName, List<String> dataList) 
	{
		ArrayList<String> sqlList = new ArrayList<String>();
		PrepareSQL psql = null;
		for (int i = 0; i < dataList.size(); i++) {
			psql = new PrepareSQL();
			psql.append("insert into tab_temporary_device(filename,device_serialnumber) ");
			psql.append("values('" + fileName + "','"+ dataList.get(i) + "')");
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
	
	
	private Map<String, String> cityMap = null;
	private HashMap<String, String> vendorMap = null;
	private HashMap<String, String> deviceModelMap = null;
	private HashMap<String, String> devicetypeMap = null;

	public List<Map> queryModifyVlanInfo(String serviceId) 
	{
		logger.debug("ModifyVlanCountDAO--queryModifyVlanInfo({})", serviceId);
		String sql;
		if(DBUtil.GetDB()==3){
			//TODO wait
			sql = "select b.city_id,d.status,d.result_id,count(*) as total ";
		}else{
			sql = "select b.city_id,d.status,d.result_id,count(1) as total ";
		}
		
			sql+= "from tab_setmulticast_task_dev d,tab_setmulticast_task t,tab_gw_device b "
				+ "where d.device_id=b.device_id and t.task_id=d.task_id and t.service_id="+ serviceId 
				+ " group by b.city_id,d.status,d.result_id";
		PrepareSQL psql = new PrepareSQL(sql.toString());
		return this.jt.queryForList(psql.getSQL());
	}

	public List<Map> getDevList(String serviceId, String status, String cityId, 
			int curPage_splitPage, int num_splitPage) 
	{
		String sql = appendSqlMethod(serviceId, status, cityId);
		PrepareSQL psql = new PrepareSQL(sql);
		this.cityMap = CityDAO.getCityIdCityNameMap();
		this.vendorMap = VendorModelVersionDAO.getVendorMap();
		this.deviceModelMap = VendorModelVersionDAO.getDeviceModelMap();
		this.devicetypeMap = VendorModelVersionDAO.getDevicetypeMap();
		List list = querySP(psql.getSQL(), (curPage_splitPage - 1) * num_splitPage + 1, 
				num_splitPage, new RowMapper() 
		{
			public Object mapRow(ResultSet rs, int arg1) throws SQLException 
			{
				Map map = new HashMap();
				map.put("device_id", rs.getString("device_id"));
				String city_id = rs.getString("city_id");
				String city_name = StringUtil.getStringValue(cityMap.get(city_id));
				if (!StringUtil.IsEmpty(city_name)) {
					map.put("city_name", city_name);
				}
				else {
					map.put("city_name", "");
				}
				String vendor_id = rs.getString("vendor_id");
				String vendor_add = StringUtil.getStringValue(vendorMap.get(vendor_id));
				if (!StringUtil.IsEmpty(vendor_add)) {
					map.put("vendor_add", vendor_add);
				}
				else {
					map.put("vendor_add", "");
				}
				String device_model_id = rs.getString("device_model_id");
				String device_model = StringUtil.getStringValue(deviceModelMap.get(device_model_id));
				if (!StringUtil.IsEmpty(device_model)) {
					map.put("device_model", device_model);
				}
				else {
					map.put("device_model", "");
				}
				String devicetype_id = rs.getString("devicetype_id");
				String softwareversion = StringUtil.getStringValue(devicetypeMap.get(devicetype_id));
				if (!StringUtil.IsEmpty(softwareversion)) {
					map.put("softwareversion", softwareversion);
				}
				else {
					map.put("softwareversion", "");
				}
				map.put("oui", rs.getString("oui"));
				map.put("device_serialnumber", rs.getString("device_serialnumber"));
				map.put("device", rs.getString("oui") + "-" + rs.getString("device_serialnumber"));
				map.put("loopback_ip", rs.getString("loopback_ip"));
				try {
					if(null==fault_Map){
						getfaultMap();
					}
					map.put("fault_desc", fault_Map.get(rs.getString("result_id")));
				} catch (Exception e) {
				}
				
				return map;
			}
		});
		this.cityMap = null;
		this.vendorMap = null;
		this.deviceModelMap = null;
		return list;
	}

	private String appendSqlMethod(String serviceId, String status, String cityId) 
	{
		StringBuffer sql = new StringBuffer();
	/**	if(DBUtil.GetDB()==3){
			//TODO wait
		}else{
			
		} */
		sql.append("select b.device_id,b.city_id,b.vendor_id,b.devicetype_id,b.device_model_id,");
		sql.append("b.oui,b.device_serialnumber,b.loopback_ip,a.result_id ");
		sql.append("from tab_setmulticast_task_dev a,tab_gw_device b,tab_setmulticast_task t ");
		sql.append("where a.device_id=b.device_id and a.task_id=t.task_id ");
		sql.append("and t.service_id="+serviceId);
		if ((!StringUtil.IsEmpty(cityId)) && (!"00".equals(cityId))) {
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and b.city_id in (").append(StringUtils.weave(cityIdList)).append(")");
			cityIdList = null;
		}
		if ("11".equals(status) || "1".equals(status)) {
			sql.append(" and a.result_id=1 and a.status=100 ");
		}else if ("2".equals(status)) {
			sql.append(" and a.result_id is null and a.status is null");
		}else if ("3".equals(status)) {
			sql.append(" and (a.result_id <> 1 or a.status <> 100) ");
		}
		return sql.toString();
	}

	public int getDevCount(String serviceId, String status, String cityId,
			int curPage_splitPage, int num_splitPage) 
	{
		logger.warn("getDevCount(serviceId:{},status:{},cityId:{})", 
				new Object[] { serviceId, status, cityId });
		StringBuffer sql = new StringBuffer();
		if(DBUtil.GetDB()==3){
			//TODO wait
			sql.append("select count(*) ");
		}else{
			sql.append("select count(1) ");
		}
		sql.append("from tab_setmulticast_task_dev a,tab_gw_device b,tab_setmulticast_task t ");
		sql.append("where a.device_id=b.device_id and a.task_id=t.task_id ");
		sql.append("and t.service_id="+serviceId);
		
		if ((!StringUtil.IsEmpty(cityId)) && (!"00".equals(cityId))) {
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and b.city_id in (").append(StringUtils.weave(cityIdList)).append(")");
			cityIdList = null;
		}
		if ("11".equals(status) || "1".equals(status)) {
			sql.append(" and a.result_id=1 and a.status=100 ");
		}else if ("2".equals(status)) {
			sql.append(" and a.result_id is null and a.status is null");
		}else if ("3".equals(status)) {
			sql.append(" and (a.result_id <> 1 or a.status <> 100) ");
		}
		PrepareSQL psql = new PrepareSQL(sql.toString());
		int total = this.jt.queryForInt(psql.getSQL());
		int maxPage = 1;
		if (total % num_splitPage == 0) {
			maxPage = total / num_splitPage;
		}else {
			maxPage = total / num_splitPage + 1;
		}
		return maxPage;
	}

	public List<Map> getDevExcel(String serviceId1, String status, String cityId) 
	{
		String sql = appendSqlMethod(serviceId1, status, cityId);
		PrepareSQL psql = new PrepareSQL(sql.toString());
		this.cityMap = CityDAO.getCityIdCityNameMap();
		this.vendorMap = VendorModelVersionDAO.getVendorMap();
		this.deviceModelMap = VendorModelVersionDAO.getDeviceModelMap();
		this.devicetypeMap = VendorModelVersionDAO.getDevicetypeMap();
		List list = this.jt.query(psql.getSQL(), new RowMapper() 
		{
			public Object mapRow(ResultSet rs, int arg1) throws SQLException 
			{
				Map map = new HashMap();
				map.put("device_id", rs.getString("device_id"));
				String city_id = rs.getString("city_id");
				String city_name = StringUtil.getStringValue(cityMap.get(city_id));
				if (!StringUtil.IsEmpty(city_name)) {
					map.put("city_name", city_name);
				}
				else {
					map.put("city_name", "");
				}
				String vendor_id = rs.getString("vendor_id");
				String vendor_add = StringUtil.getStringValue(vendorMap.get(vendor_id));
				if (!StringUtil.IsEmpty(vendor_add)) {
					map.put("vendor_add", vendor_add);
				}
				else {
					map.put("vendor_add", "");
				}
				String device_model_id = rs.getString("device_model_id");
				String device_model = StringUtil.getStringValue(deviceModelMap.get(device_model_id));
				if (!StringUtil.IsEmpty(device_model)) {
					map.put("device_model", device_model);
				}
				else {
					map.put("device_model", "");
				}
				String devicetype_id = rs.getString("devicetype_id");
				String softwareversion = StringUtil.getStringValue(devicetypeMap.get(devicetype_id));
				if (!StringUtil.IsEmpty(softwareversion)) {
					map.put("softwareversion", softwareversion);
				}
				else {
					map.put("softwareversion", "");
				}
				map.put("oui", rs.getString("oui"));
				map.put("device_serialnumber", rs.getString("device_serialnumber"));
				map.put("device", rs.getString("oui") + "-" + rs.getString("device_serialnumber"));
				map.put("loopback_ip", rs.getString("loopback_ip"));
				try {
					if(null==fault_Map){
						getfaultMap();
					}
					map.put("fault_desc", fault_Map.get(rs.getString("result_id")));
				} catch (Exception e) {
				}
				return map;
			}
		});
		this.cityMap = null;
		this.vendorMap = null;
		this.deviceModelMap = null;
		logger.debug("devList:" + list.toString());
		return list;
	}

	/**
	 * 统计单双栈信息
	 * 
	 * @param serviceId
	 * @param cityId
	 * @param endtime1
	 * @param starttime1
	 * @return
	 */
	public List<Map> queryModifyVlanInfo(String serviceId, String starttime1, String endtime1, String cityId) 
	{
		logger.warn("ModifyVlanCountDAO--queryModifyVlanInfo({})", serviceId);
		/**
		 * select b.city_id, d.status, d.result_id,d.res,count(1) as total from
		 * tab_setmulticast_task_dev d , tab_setmulticast_task t ,tab_gw_device b where
		 * d.device_id=b.device_id and t.task_id = d.task_id and t.service_id = 2301 group
		 * by b.city_id,d.status,d.result_id,d.res
		 */
		StringBuffer sql = new StringBuffer();
		if(DBUtil.GetDB()==3){
			//TODO wait
			sql.append("select b.city_id, d.status, d.result_id,count(*) as total ");
		}else{
			sql.append("select b.city_id, d.status, d.result_id,count(1) as total ");
		}
		sql.append("from tab_setmulticast_task_dev d,tab_setmulticast_task t,tab_gw_device b ");
		sql.append("where d.device_id=b.device_id and t.task_id=d.task_id ");
		sql.append("and t.service_id=" + serviceId);
		if (false == StringUtil.IsEmpty(cityId) && !"00".equals(cityId)) {
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and b.city_id in (").append(StringUtils.weave(cityIdList)).append(") ");
			cityIdList = null;
		}
		if (!StringUtil.IsEmpty(starttime1)) {
			sql.append(" and d.add_time > " + starttime1 + " ");
		}
		if (!StringUtil.IsEmpty(endtime1)) {
			sql.append(" and d.add_time < " + endtime1 + " ");
		}
		sql.append(" group by b.city_id,d.status,d.result_id ");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		return jt.queryForList(psql.getSQL());
	}

	/**
	 * 查询详情列表
	 * 
	 * @param serviceId
	 * @param status
	 *            (结果状态 1成功 2未做 3失败 空串全部)
	 * @param cityId
	 * @param num_splitPage
	 * @param curPage_splitPage
	 * @param endtime1
	 * @param starttime1
	 * @return
	 */
	public List<Map> getDevList(String serviceId, String status, String cityId, int curPage_splitPage,
			int num_splitPage, String starttime1, String endtime1) 
	{
		String sql = appendSqlMethod(serviceId, status, cityId, starttime1, endtime1);
		PrepareSQL psql = new PrepareSQL(sql);
		cityMap = CityDAO.getCityIdCityNameMap();
		vendorMap = VendorModelVersionDAO.getVendorMap();
		deviceModelMap = VendorModelVersionDAO.getDeviceModelMap();
		devicetypeMap = VendorModelVersionDAO.getDevicetypeMap();
		List<Map> list = querySP(psql.getSQL(), (curPage_splitPage - 1) * num_splitPage + 1,
				num_splitPage,new RowMapper() 
		{
					public Object mapRow(ResultSet rs, int arg1) throws SQLException
					{
						Map<String, String> map = new HashMap<String, String>();
						map.put("device_id", rs.getString("device_id"));
						String city_id = rs.getString("city_id");
						String city_name = StringUtil.getStringValue(cityMap.get(city_id));
						if (false == StringUtil.IsEmpty(city_name)) {
							map.put("city_name", city_name);
						}
						else {
							map.put("city_name", "");
						}
						String vendor_id = rs.getString("vendor_id");
						String vendor_add = StringUtil.getStringValue(vendorMap.get(vendor_id));
						if (false == StringUtil.IsEmpty(vendor_add)) {
							map.put("vendor_add", vendor_add);
						}
						else {
							map.put("vendor_add", "");
						}
						String device_model_id = rs.getString("device_model_id");
						String device_model = StringUtil.getStringValue(deviceModelMap.get(device_model_id));
						if (false == StringUtil.IsEmpty(device_model)) {
							map.put("device_model", device_model);
						}
						else {
							map.put("device_model", "");
						}
						String devicetype_id = rs.getString("devicetype_id");
						String softwareversion = StringUtil.getStringValue(devicetypeMap.get(devicetype_id));
						if (false == StringUtil.IsEmpty(softwareversion)) {
							map.put("softwareversion", softwareversion);
						}
						else {
							map.put("softwareversion", "");
						}
						map.put("oui", rs.getString("oui"));
						map.put("device_serialnumber", rs.getString("device_serialnumber"));
						map.put("device", rs.getString("oui") + "-" + rs.getString("device_serialnumber"));
						map.put("loopback_ip", rs.getString("loopback_ip"));
						try {
							if(null==fault_Map){
								getfaultMap();
							}
							map.put("fault_desc", fault_Map.get(rs.getString("result_id")));
						} catch (Exception e) {
						}
						map.put("loid", rs.getString("loid"));
						map.put("serv_account", rs.getString("serv_account"));
						return map;
					}
				});
		cityMap = null;
		vendorMap = null;
		deviceModelMap = null;
		return list;
	}

	private String appendSqlMethod(String serviceId, String status, String cityId, 
			String starttime1, String endtime1) 
	{
		StringBuffer sql = new StringBuffer();
	/**	if(DBUtil.GetDB()==3){
			//TODO wait
		} */
		sql.append("select b.device_id,b.city_id,b.vendor_id,b.devicetype_id,");
		sql.append("b.device_model_id,b.oui,b.device_serialnumber,b.loopback_ip,");
		sql.append("h.username loid,c.username serv_account,a.result_id ");
		sql.append("from tab_setmulticast_task_dev a,tab_gw_device b,tab_setmulticast_task t,");
		sql.append("tab_hgwcustomer h,hgwcust_serv_info c ");
		sql.append("where a.device_id=b.device_id and a.task_id=t.task_id and a.loid=h.username ");
		sql.append("and h.user_id=c.user_id and c.serv_type_id=10 ");
		sql.append("and t.service_id="+ serviceId);
		if (false == StringUtil.IsEmpty(cityId) && !"00".equals(cityId)) {
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and b.city_id in (").append(StringUtils.weave(cityIdList)).append(") ");
			cityIdList = null;
		}
		if (!StringUtil.IsEmpty(starttime1)) {
			sql.append(" and a.add_time > " + starttime1 + " ");
		}
		if (!StringUtil.IsEmpty(endtime1)) {
			sql.append(" and a.add_time < " + endtime1 + " ");
		}
		// 查询成功的
		if ("11".equals(status) || "1".equals(status)) {
			sql.append(" and a.result_id=1 and a.status=100 "); 
		}else if ("2".equals(status)) {
			// 查询未作的
			sql.append(" and a.result_id is null and a.status is null ");
		}else if ("3".equals(status)) {
			// 查询失败的
			sql.append(" and (a.result_id <> 1 or a.status <> 100) ");
		}
		return sql.toString();
	}

	/**
	 * 查询总量
	 * 
	 * @param serviceId1
	 * @param status
	 * @param cityId
	 * @param num_splitPage
	 * @param curPage_splitPage
	 * @param endtime1
	 * @param starttime1
	 * @return
	 */
	public int getDevCount(String serviceId, String status, String cityId,
			int curPage_splitPage, int num_splitPage,String starttime1, String endtime1) 
	{
		logger.warn("getDevCount(serviceId:{},status:{},cityId:{})", serviceId, status, cityId);
		StringBuffer sql = new StringBuffer();
		if(DBUtil.GetDB()==3){
			//TODO wait
			sql.append("select count(*) ");
		}else{
			sql.append("select count(1) ");
		}
		sql.append("from tab_setmulticast_task_dev a,tab_gw_device b,tab_setmulticast_task t,");
		sql.append("tab_hgwcustomer h,hgwcust_serv_info c ");
		sql.append("where a.device_id=b.device_id and a.task_id=t.task_id ");
		sql.append("and a.loid=h.username and h.user_id=c.user_id ");
		sql.append("and c.serv_type_id=10 and t.service_id="+ serviceId);
		if (false == StringUtil.IsEmpty(cityId) && !"00".equals(cityId)) {
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and b.city_id in (").append(StringUtils.weave(cityIdList)).append(") ");
			cityIdList = null;
		}
		if (!StringUtil.IsEmpty(starttime1)) {
			sql.append(" and a.add_time > " + starttime1 + " ");
		}
		if (!StringUtil.IsEmpty(endtime1)) {
			sql.append(" and a.add_time < " + endtime1 + " ");
		}
		// 查询成功的
		if ("11".equals(status) || "1".equals(status)) {
			sql.append(" and a.result_id=1 and a.status=100 ");
		}else if ("2".equals(status)) {
			// 查询未作的
			sql.append(" and a.result_id is null and a.status is null ");
		}else if ("3".equals(status)) {
			// 查询失败的
			sql.append(" and (a.result_id <> 1 or a.status <> 100) ");
		}
		PrepareSQL psql = new PrepareSQL(sql.toString());
		int total = jt.queryForInt(psql.getSQL());
		int maxPage = 1;
		if (total % num_splitPage == 0) {
			maxPage = total / num_splitPage;
		}else {
			maxPage = total / num_splitPage + 1;
		}
		return maxPage;
	}

	/**
	 * 详情页面导出
	 * 
	 * @param serviceId1
	 * @param status
	 * @param cityId
	 * @param endtime1
	 * @param starttime1
	 * @return
	 */
	public List<Map> getDevExcel(String serviceId1, String status, String cityId,
			String starttime1, String endtime1) 
	{
		String sql = appendSqlMethod(serviceId1, status, cityId, starttime1, endtime1);
		PrepareSQL psql = new PrepareSQL(sql.toString());
		cityMap = CityDAO.getCityIdCityNameMap();
		vendorMap = VendorModelVersionDAO.getVendorMap();
		deviceModelMap = VendorModelVersionDAO.getDeviceModelMap();
		devicetypeMap = VendorModelVersionDAO.getDevicetypeMap();
		List<Map> list = jt.query(psql.getSQL(), new RowMapper() 
		{
			public Object mapRow(ResultSet rs, int arg1) throws SQLException 
			{
				Map<String, String> map = new HashMap<String, String>();
				map.put("device_id", rs.getString("device_id"));
				String city_id = rs.getString("city_id");
				String city_name = StringUtil.getStringValue(cityMap.get(city_id));
				if (false == StringUtil.IsEmpty(city_name)) {
					map.put("city_name", city_name);
				}
				else {
					map.put("city_name", "");
				}
				String vendor_id = rs.getString("vendor_id");
				String vendor_add = StringUtil.getStringValue(vendorMap.get(vendor_id));
				if (false == StringUtil.IsEmpty(vendor_add)) {
					map.put("vendor_add", vendor_add);
				}
				else {
					map.put("vendor_add", "");
				}
				String device_model_id = rs.getString("device_model_id");
				String device_model = StringUtil.getStringValue(deviceModelMap.get(device_model_id));
				if (false == StringUtil.IsEmpty(device_model)) {
					map.put("device_model", device_model);
				}
				else {
					map.put("device_model", "");
				}
				String devicetype_id = rs.getString("devicetype_id");
				String softwareversion = StringUtil.getStringValue(devicetypeMap.get(devicetype_id));
				if (false == StringUtil.IsEmpty(softwareversion)) {
					map.put("softwareversion", softwareversion);
				}
				else {
					map.put("softwareversion", "");
				}
				map.put("oui", rs.getString("oui"));
				map.put("device_serialnumber", rs.getString("device_serialnumber"));
				map.put("device", rs.getString("oui") + "-" + rs.getString("device_serialnumber"));
				map.put("loopback_ip", rs.getString("loopback_ip"));
				try {
					if(null==fault_Map){
						getfaultMap();
					}
					map.put("fault_desc", fault_Map.get(rs.getString("result_id")));
				} catch (Exception e) {
				}
				map.put("loid", rs.getString("loid"));
				map.put("serv_account", rs.getString("serv_account"));
				return map;
			}
		});
		cityMap = null;
		vendorMap = null;
		deviceModelMap = null;
		logger.debug("devList:" + list.toString());
		return list;
	}
	
}