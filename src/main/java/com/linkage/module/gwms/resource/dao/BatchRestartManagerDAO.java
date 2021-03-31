
package com.linkage.module.gwms.resource.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.DBOperation;
import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.DateTimeUtil;
import com.linkage.commons.util.StringUtil;
import com.linkage.module.gwms.dao.SuperDAO;

/**
 * 甘肃ITMS平台拉流测速与长时间在线光猫自动重启定制功能需
 * @author yaoli (Ailk No.)
 * @version 1.0
 * @since 2019年10月21日
 * @category com.linkage.module.gwms.resource.dao
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class BatchRestartManagerDAO extends SuperDAO
{
	private static Logger logger = LoggerFactory.getLogger(BatchRestartManagerDAO.class);

	/**
	 * 查询
	 * @param taskName
	 * @param taskDesc
	 * @param addTime
	 * @param finalTime
	 * @param startTime
	 * @param endTime
	 * @param status
	 * @param subDevSn
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map> qryTaskList(String taskName,String taskDesc,String addTime,String finalTime,String startTime,
			String endTime,int status,String subDevSn,int curPage_splitPage,int num_splitPage)
	{
		logger.warn("qryTaskList => taskNmae:{},taskDesc:{},addTime:{},finalTime:{},start:{},end:{},status:{},densn:{}",
				new Object[]{taskName,taskDesc,addTime,finalTime,startTime,endTime,status,subDevSn});
	/**	if(DBUtil.GetDB()==3){
			//TODO wait
		}else{
			
		} */
		StringBuffer sb = new StringBuffer();
		sb.append("select b.task_id,b.task_desc,b.status,b.add_time,b.start_time,a.succ,a.fail,a.total,a.wait");
		sb.append("from ");
		sb.append("(select b.task_id,count(*) total,");
		sb.append(" sum(case when(status = 2) then 1 else 0 end) succ,");
		sb.append(" sum(case when(status = 1) then 1 else 0 end) wait,");
		sb.append(" sum(case when(status != 1 and status != 2) then 1 else 0 end) fail");
		sb.append(" from ");
		if(!StringUtil.IsEmpty(subDevSn)){
			sb.append(" (select a.task_id from gw_device_restart_batch a, tab_gw_device b where");
			sb.append(" a.device_id=b.device_id and b.device_serialnumber like '%"+subDevSn+"%'");
			sb.append(" ) a,gw_device_restart_batch b");
			sb.append(" where a.task_id=b.task_id group by b.task_id ");
		}else{
			sb.append(" gw_device_restart_batch b group by task_id");
		}
		sb.append(") a , gw_device_restart_task b where a.task_id = b.task_id");
		
		if(!StringUtil.IsEmpty(taskName)){
			sb.append(" and b.task_id="+taskName);
		}
		
		if(!StringUtil.IsEmpty(taskDesc)){
			sb.append(" and b.task_desc like '%"+taskDesc+"%'");
		}
		if(!StringUtil.IsEmpty(addTime)){
			sb.append(" and b.add_time >"+addTime);
		}
		if(!StringUtil.IsEmpty(finalTime)){
			sb.append(" and b.add_time <"+finalTime);
		}
		if(!StringUtil.IsEmpty(startTime)){
			sb.append(" and b.start_time >"+startTime);
		}
		if(!StringUtil.IsEmpty(endTime)){
			sb.append(" and b.start_time <"+endTime);
		}
		if(status != -1){
			sb.append(" and b.status="+status);
		}
		
		sb.append(" order by b.add_time desc,b.start_time desc");
		PrepareSQL pSQL = new PrepareSQL(sb.toString());
		 if(-1 != curPage_splitPage){
		    	return querySP(pSQL.getSQL(), (curPage_splitPage - 1) * num_splitPage + 1,
		    			num_splitPage,new RowMapper() {
					  public Object mapRow(ResultSet rs, int arg1) throws SQLException {
						Map<String, String> map = new HashMap<String, String>();
						try {
							String addTime = rs.getString("add_time");
							String start_time = rs.getString("start_time");
							map.put("task_name", rs.getString("task_id"));
							map.put("task_desc", rs.getString("task_desc"));
							map.put("task_status", getStatus(rs.getInt("status")));
							map.put("status", StringUtil.getStringValue(rs.getInt("status")));
							map.put("add_time", new DateTimeUtil(Long.valueOf(addTime) * 1000).getYYYY_MM_DD_HH_mm_ss());
							map.put("start_time", new DateTimeUtil(Long.valueOf(start_time) * 1000).getYYYY_MM_DD_HH_mm_ss());
							map.put("succ", rs.getString("succ"));
							map.put("fail", rs.getString("fail"));
							map.put("total", rs.getString("total"));
							map.put("wait", rs.getString("wait"));
						} catch (SQLException e) {
							logger.error(e.getMessage());
						}
						return map; 
					}
				});
		    }else{
		    	return jt.query(pSQL.getSQL(), new RowMapper()
				{
					public Object mapRow(ResultSet rs, int arg1) throws SQLException
					{
						Map<String, String> map = new HashMap<String, String>();
						try {
							String addTime = rs.getString("add_time");
							String start_time = rs.getString("start_time");
							map.put("task_name", rs.getString("task_id"));
							map.put("task_desc", rs.getString("task_desc"));
							map.put("task_status", getStatus(rs.getInt("status")));
							map.put("status", StringUtil.getStringValue(rs.getInt("status")));
							map.put("add_time", new DateTimeUtil(Long.valueOf(addTime) * 1000).getYYYY_MM_DD_HH_mm_ss());
							map.put("start_time", new DateTimeUtil(Long.valueOf(start_time) * 1000).getYYYY_MM_DD_HH_mm_ss());
							map.put("succ", rs.getString("succ"));
							map.put("fail", rs.getString("fail"));
							map.put("total", rs.getString("total"));
							map.put("wait", rs.getString("wait"));

						} catch (SQLException e) {
							logger.error(e.getMessage());
						}
						return map;  
					}
				});
		    }
	}
	
	/**
	 * 
	 * @param taskName
	 * @param taskDesc
	 * @param addTime
	 * @param finalTime
	 * @param startTime
	 * @param endTime
	 * @param status
	 * @param subDevSn
	 * @return
	 */
	public List<HashMap<String,String>> qryTaskExcelList(String taskName,String taskDesc,String addTime,String finalTime,String startTime,String endTime,int status,String subDevSn){
		logger.warn("qryTaskList => taskNmae:{},taskDesc:{},addTime:{},finalTime:{},start:{},end:{},status:{},densn:{}",
				new Object[]{taskName,taskDesc,addTime,finalTime,startTime,endTime,status,subDevSn});
		
		StringBuffer sb = new StringBuffer();
		sb.append(" select b.task_id,b.task_desc,b.status,a.succ,a.fail,a.total,");
		if(DBUtil.GetDB()==3){
			//TODO wait
			sb.append(" date_format(b.add_time / ( 60 * 60 * 24) +str_to_date('1970-01-01 08:00:00', '%Y/%m/%d %H:%i:%s'), '%Y/%m/%d %H:%i:%s') add_time,");
			sb.append(" date_format(b.start_time / ( 60 * 60 * 24) +str_to_date('1970-01-01 08:00:00', '%Y/%m/%d %H:%i:%s'), '%Y/%m/%d %H:%i:%s') start_time,");
		}else{
			sb.append(" TO_CHAR(b.add_time / ( 60 * 60 * 24) +TO_DATE('1970-01-01 08:00:00', 'YYYY/MM/DD HH:MI:SS'), 'YYYY/MM/DD HH24:MI:SS') add_time,");
			sb.append(" TO_CHAR(b.start_time / ( 60 * 60 * 24) +TO_DATE('1970-01-01 08:00:00', 'YYYY/MM/DD HH:MI:SS'), 'YYYY/MM/DD HH24:MI:SS') start_time,");
		}
		
		sb.append(" case when b.status = 1 then '未执行'");
		sb.append(" when b.status = 2 then '执行中'");
		sb.append(" when b.status = 3 then '完成'");
		sb.append(" else '失败'");
		sb.append(" end as task_status ");
		sb.append(" from");
		sb.append(" (select b.task_id,count(*) total,");
		sb.append(" sum(case when(status = 2) then 1 else 0 end) succ,");
		sb.append(" sum(case when(status = 1) then 1 else 0 end) wait,");
		sb.append(" sum(case when(status != 1 and status != 2) then 1 else 0 end) fail");
		sb.append(" from");
		if(!StringUtil.IsEmpty(subDevSn)){
			sb.append(" (");
			sb.append(" select a.task_id from gw_device_restart_batch a, tab_gw_device b where");
			sb.append(" a.device_id=b.device_id and b.device_serialnumber like '%"+subDevSn+"%'");
			sb.append(" ) a,gw_device_restart_batch b");
			sb.append(" where a.task_id=b.task_id group by b.task_id ");
		}else{
			sb.append(" gw_device_restart_batch b group by task_id");
		}
		sb.append(") a , gw_device_restart_task b where a.task_id = b.task_id");
		
		if(!StringUtil.IsEmpty(taskName)){
			sb.append(" and b.task_id="+taskName);
		}
		
		if(!StringUtil.IsEmpty(taskDesc)){
			sb.append(" and b.task_desc like '%"+taskDesc+"%'");
		}
		if(!StringUtil.IsEmpty(addTime)){
			sb.append(" and b.add_time >"+addTime);
		}
		if(!StringUtil.IsEmpty(finalTime)){
			sb.append(" and b.add_time <"+finalTime);
		}
		if(!StringUtil.IsEmpty(startTime)){
			sb.append(" and b.start_time >"+startTime);
		}
		if(!StringUtil.IsEmpty(endTime)){
			sb.append(" and b.start_time <"+endTime);
		}
		if(status != -1){
			sb.append(" and b.status="+status);
		}
		sb.append(" order by b.add_time desc,b.start_time desc");
		PrepareSQL pSQL = new PrepareSQL(sb.toString());
		return DBOperation.getRecords(pSQL.getSQL()); 
	}
	
	public int qryTaskCountList(String taskName,String taskDesc,String addTime,
			String finalTime,String startTime,String endTime,int status,String subDevSn)
	{
		logger.warn("qryTaskCountList => taskNmae:{},taskDesc:{},addTime:{},finalTime:{},start:{},end:{},status:{},densn:{}",
				new Object[]{taskName,taskDesc,addTime,finalTime,startTime,endTime,status,subDevSn});
		
		StringBuffer sb = new StringBuffer();
		if(DBUtil.GetDB()==3){
			//TODO wait
			sb.append(" select count(*) num");
		}else{
			sb.append(" select count(1) num");
		}
		
		sb.append(" from ");
		sb.append(" (");
		sb.append(" select b.task_id,count(*) total,");
		sb.append(" sum(case when(status = 2) then 1 else 0 end) succ,");
		sb.append(" sum(case when(status = 1) then 1 else 0 end) wait,");
		sb.append(" sum(case when(status != 1 and status != 2) then 1 else 0 end) fail");
		sb.append(" from ");
		if(!StringUtil.IsEmpty(subDevSn)){
			sb.append(" (");
			sb.append(" select a.task_id from gw_device_restart_batch a, tab_gw_device b where");
			sb.append(" a.device_id=b.device_id and b.device_serialnumber like '%"+subDevSn+"%'");
			sb.append(" ) a,gw_device_restart_batch b");
			sb.append(" where a.task_id=b.task_id group by b.task_id ");
		}else{
			sb.append(" gw_device_restart_batch b group by task_id");
		}
		sb.append(") a , gw_device_restart_task b where a.task_id = b.task_id");
		
		if(!StringUtil.IsEmpty(taskName)){
			sb.append(" and b.task_id="+taskName);
		}
		
		if(!StringUtil.IsEmpty(taskDesc)){
			sb.append(" and b.task_desc like '%"+taskDesc+"%'");
		}
		if(!StringUtil.IsEmpty(addTime)){
			sb.append(" and b.add_time >"+addTime);
		}
		if(!StringUtil.IsEmpty(finalTime)){
			sb.append(" and b.add_time <"+finalTime);
		}
		if(!StringUtil.IsEmpty(startTime)){
			sb.append(" and b.start_time >"+startTime);
		}
		if(!StringUtil.IsEmpty(endTime)){
			sb.append(" and b.start_time <"+endTime);
		}
		if(status != -1){
			sb.append(" and b.status="+status);
		}
		
		PrepareSQL pSQL = new PrepareSQL(sb.toString());
		try
		{
			Map<String,String> mapsList = DBOperation.getRecord(pSQL.getSQL());
			if(null != mapsList && !mapsList.isEmpty() && mapsList.size() > 0){
				return StringUtil.getIntegerValue(mapsList.get("num"));
			}
		}
		catch (Exception e)
		{
			logger.error("qryBatchStartTaskCount error");
			e.printStackTrace();
		}
		return 0;
	}
	
	@SuppressWarnings("unchecked")
	public List<Map> qryTaskDetail(String taskId,String type,String subDevSn,
			int curPage_splitPage,int num_splitPage)
	{
		logger.warn("qryTaskDetail=>{},{}",new Object[]{taskId,type});
		
		StringBuffer sb = new StringBuffer();
	/**	if(DBUtil.GetDB()==3){
			//TODO wait
		}else{
			
		} */
		sb.append(" select a.status,f.vendor_name ,c.device_model,d.softwareversion,e.city_name,");
		sb.append(" b.device_serialnumber,aa.username,bb.device_version_type,ff.last_time");
		sb.append(" from");
		sb.append(" gw_device_restart_batch a,");
		sb.append(" tab_gw_device b");
		sb.append(" left join tab_hgwcustomer aa on b.customer_id=aa.user_id");
		sb.append(" left join tab_device_version_attribute bb on b.devicetype_id=bb.devicetype_id,");
		sb.append(" gw_device_model c,tab_devicetype_info d ,tab_city e,tab_vendor f ,gw_devicestatus ff");
		sb.append(" where");
		sb.append(" a.device_id=b.device_id and b.device_model_id=c.device_model_id and b.devicetype_id=d.devicetype_id");
		sb.append(" and b.city_id=e.city_id and b.vendor_id=f.vendor_id and b.device_id=ff.device_id and a.task_id=?");
		
		if(!StringUtil.IsEmpty(type)){
			if("succ".equals(type)){
				sb.append(" and a.status=2");
			}else if("fail".equals(type)){
				sb.append(" and a.status not in(1,2)");
			}else if("wait".equals(type)){
				sb.append(" and a.status=1");
			}
		}
		
		sb.append(" order by ff.last_time desc");
		PrepareSQL pSQL = new PrepareSQL(sb.toString());
		pSQL.setString(1, taskId);
		 if(-1 != curPage_splitPage){
		    	return querySP(pSQL.getSQL(), (curPage_splitPage - 1) * num_splitPage + 1,
		    			num_splitPage,new RowMapper() {
					  public Object mapRow(ResultSet rs, int arg1) throws SQLException {
						Map<String, String> map = new HashMap<String, String>();
						try {
							map.put("vendor_name", rs.getString("vendor_name"));
							map.put("device_model", rs.getString("device_model"));
							map.put("softwareversion", rs.getString("softwareversion"));
							map.put("city_name",  rs.getString("city_name"));
							map.put("devsn", rs.getString("device_serialnumber"));
							map.put("LOID", rs.getString("username"));
							String ver =  rs.getString("device_version_type");
							map.put("device_version_type",getDevType(StringUtil.getIntegerValue(ver,0)));
							map.put("time", new DateTimeUtil(rs.getLong("last_time") * 1000).getYYYY_MM_DD_HH_mm_ss());
							
							int status = rs.getInt("status");
							map.put("restart_res", getRes(status).split(":")[0]);
							map.put("faile_reason", "yy".equals(getRes(status).split(":")[1])?"":getRes(status).split(":")[1]);
						} catch (SQLException e) {
							logger.error("qryTaskDetail:"+e.getMessage());
						}
						return map; 
					}
				});
		    }else{
		    	return jt.query(pSQL.getSQL(), new RowMapper()
				{
					public Object mapRow(ResultSet rs, int arg1) throws SQLException
					{
						Map<String, String> map = new HashMap<String, String>();
						try {
							map.put("vendor_name", rs.getString("vendor_name"));
							map.put("device_model", rs.getString("device_model"));
							map.put("softwareversion", rs.getString("softwareversion"));
							map.put("city_name",  rs.getString("city_name"));
							map.put("devsn", rs.getString("device_serialnumber"));
							map.put("LOID", rs.getString("username"));
							String ver =  rs.getString("device_version_type");
							map.put("device_version_type",getDevType(StringUtil.getIntegerValue(ver,0)));
							map.put("time", new DateTimeUtil(rs.getLong("last_time") * 1000).getYYYY_MM_DD_HH_mm_ss());
							
							int status = rs.getInt("status");
							map.put("restart_res", getRes(status).split(":")[0]);
							map.put("faile_reason", "yy".equals(getRes(status).split(":")[1])?"":getRes(status).split(":")[1]);

						} catch (SQLException e) {
							logger.error(e.getMessage());
						}
						return map;  
					}
				});
		    }
	}
	
	public int qryTaskDetailCount(String taskId,String type,String subDevSn)
	{
		logger.warn("qryTaskDetailCount=>{},{}",new Object[]{taskId,type});
		
		StringBuffer sb = new StringBuffer();
		if(DBUtil.GetDB()==3){
			//TODO wait
			sb.append(" select count(*) num ");
		}else{
			sb.append(" select count(1) num ");
		}
		
		sb.append(" from gw_device_restart_batch a,tab_gw_device b ");
		sb.append(" left join tab_hgwcustomer aa on b.customer_id=aa.user_id ");
		sb.append(" left join tab_device_version_attribute bb on b.devicetype_id=bb.devicetype_id,");
		sb.append(" gw_device_model c,tab_devicetype_info d ,tab_city e,tab_vendor f ,gw_devicestatus ff ");
		sb.append(" where a.device_id=b.device_id and b.device_model_id=c.device_model_id and b.devicetype_id=d.devicetype_id");
		sb.append(" and b.city_id=e.city_id and b.vendor_id=f.vendor_id and b.device_id=ff.device_id and a.task_id=? ");
		
		if(!StringUtil.IsEmpty(type)){
			if("succ".equals(type)){
				sb.append(" and a.status=2");
			}else if("fail".equals(type)){
				sb.append(" and a.status not in(1,2)");
			}else if("wait".equals(type)){
				sb.append(" and a.status=1");
			}
		}
		
		PrepareSQL pSQL = new PrepareSQL(sb.toString());
		pSQL.setString(1, taskId);
		try
		{
			Map<String,String> mapsList = DBOperation.getRecord(pSQL.getSQL());
			if(null != mapsList && !mapsList.isEmpty() && mapsList.size() > 0){
				return StringUtil.getIntegerValue(mapsList.get("num"));
			}
		}
		catch (Exception e)
		{
			logger.error("qryBatchStartTaskCount error");
			e.printStackTrace();
		}
		return 0;
		 
	}
	
	public List<HashMap<String,String>> qryTaskDetailExcel(String taskId,String type,String subDevSn)
	{
		logger.warn("qryTaskDetailExcel=>{},{}",new Object[]{taskId,type});
		
		StringBuffer sb = new StringBuffer();
	/**	if(DBUtil.GetDB()==3){
			//TODO wait
		}else{
			
		} */
		sb.append(" select a.status,f.vendor_name ,c.device_model,d.softwareversion,e.city_name,");
		sb.append(" b.device_serialnumber,aa.username,bb.device_version_type,ff.last_time");
		sb.append(" from gw_device_restart_batch a,tab_gw_device b");
		sb.append(" left join tab_hgwcustomer aa on b.customer_id=aa.user_id");
		sb.append(" left join tab_device_version_attribute bb on b.devicetype_id=bb.devicetype_id,");
		sb.append(" gw_device_model c,tab_devicetype_info d ,tab_city e,tab_vendor f ,gw_devicestatus ff");
		sb.append(" where a.device_id=b.device_id and b.device_model_id=c.device_model_id and b.devicetype_id=d.devicetype_id");
		sb.append(" and b.city_id=e.city_id and b.vendor_id=f.vendor_id and b.device_id=ff.device_id and a.task_id=? ");
		
		if(!StringUtil.IsEmpty(type)){
			if("succ".equals(type)){
				sb.append(" and a.status=2");
			}else if("fail".equals(type)){
				sb.append(" and a.status not in(1,2)");
			}else if("wait".equals(type)){
				sb.append(" and a.status=1");
			}
		}
		
		sb.append(" order by ff.last_time desc");
		PrepareSQL pSQL = new PrepareSQL(sb.toString());
		pSQL.setString(1, taskId);
		try
		{
			List<HashMap<String,String>> mapList = DBOperation.getRecords(pSQL.getSQL());
			if(null != mapList && !mapList.isEmpty() && mapList.size() >0){
				for(HashMap<String,String> rs : mapList){
					try {
						String time = rs.get("last_time");
						rs.put("time", new DateTimeUtil(Long.valueOf(time) * 1000).getYYYY_MM_DD_HH_mm_ss());
						String ver =  rs.get("device_version_type");
						rs.put("device_version_type",getDevType(StringUtil.getIntegerValue(ver,0)));
						
						int status = StringUtil.getIntegerValue(rs.get("status"), 3);//默认失败
						rs.put("restart_res", getRes(status).split(":")[0]);
						rs.put("faile_reason", "yy".equals(getRes(status).split(":")[1])?"":getRes(status).split(":")[1]);
					} catch (Exception e) {
						logger.error("qryTaskDetailExcel:"+e.getMessage());
					}
				}
				return mapList;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 操作
	 * @param taskId
	 * @param operType
	 * @return
	 */
	public String operTask(String taskId,String operType){
		logger.warn("operTask=>[{}]-[{}]",new Object[]{taskId,operType});
		
		String status = qryTaskStatus(taskId);
		if(!"3".equals(operType) && ("2".equals(status) || "3".equals(status))){
			return "已执行或执行中的任务不能删除、停止";
		}
		//删除
		if("1".equals(operType)){
			return delTask(taskId);
		}
		//暂停
		else if("2".equals(operType)){
			return stopTask(taskId);
		}
		//激活
		else if("3".equals(operType)){
			return validTask(taskId);
		}else{
			logger.warn("[{}]-[{}]error operType is null ",new Object[]{taskId,operType});
		}
		return "不存在的操作类型";
	 }
	 /**
	  * 删除指定任务
	  * @param taskId
	  * @return
	  */
	public String delTask(String taskId){
		String  sql = "delete from gw_device_restart_task where task_id=?";
		PrepareSQL pSQL = new PrepareSQL(sql);
		pSQL.setString(1, taskId);
		try
		{
			 DBOperation.executeUpdate(pSQL.getSQL());
		}
		catch (Exception e)
		{
			logger.error("delTask error,msgs:{}",e.getMessage());
			return "系统原因，删除失败";
		}
	    return "删除成功"; 
	}
	
	/**
	 * 暂停任务
	 * @param taskId
	 * @return
	 */
	public String stopTask(String taskId){
		String   sql = "update gw_device_restart_task set status=? where task_id=?";
		PrepareSQL pSQL = new PrepareSQL(sql);
		 pSQL.setInt(1, -2);
		 pSQL.setString(2, taskId);
		try
		{
			 DBOperation.executeUpdate(pSQL.getSQL());
		}
		catch (Exception e)
		{
			logger.error("delTask error,msgs:{}",e.getMessage());
			return "系统原因，停止任务失败";
		}
	    return "停止任务成功"; 
	}
	
	/**
	 * 激活任务
	 * @param taskId
	 * @return
	 */
	public String validTask(String taskId){
		String   sql = "update gw_device_restart_task set status=? where task_id=?";
		PrepareSQL pSQL = new PrepareSQL(sql);
		 pSQL.setInt(1, 1);//设置为未执行状态
		 pSQL.setString(2, taskId);
		try
		{
			 DBOperation.executeUpdate(pSQL.getSQL());
		}
		catch (Exception e)
		{
			logger.error("delTask error,msgs:{}",e.getMessage());
			return "系统原因，激活失败";
		}
	    return "激活成功"; 
	}
	
	/**
	 * 获取当前任务状态
	 * @param taskId
	 * @return
	 */
	public String qryTaskStatus(String taskId){
		String   sql = "select status from gw_device_restart_task where task_id=?";
		PrepareSQL pSQL = new PrepareSQL(sql);
		 pSQL.setString(1, taskId);
		try
		{
			 Map<String,String> hm = DBOperation.getRecord(pSQL.getSQL());
			 if(null != hm && !hm.isEmpty() && hm.size() > 0){
				 return hm.get("status");
			 }
		}
		catch (Exception e)
		{
			logger.error("qryTaskStatus error,msgs:{}",e.getMessage());
		}
	    return null;
	}
	
	public String getStatus(int type)
	{
		String status = "失败";
		if(type == 1){
			status = "未执行";
		}else if(type == 2){
			status = "执行中";
		}else if(type == 3){
			status = "已执行";
		}else if(type == -2){
			status = "已暂停";
		}
		return status;
	}

	public String getRes(int type)
	{
		String status = "失败";
		String reason = "yy";
		if(type == 1){
			status = "未重启";
		}else if(type == 2){
			status = "成功";
		}else{
			reason = "其他";
			if(type == -1 || type == -2){
				reason = "设备不在线";
			}else if(type == -6){
				reason = "设备正在执行";
			} 
		}
		return status + ":" + reason;
	}

	public String getDevType(int deviceVerType)
	{
		String deviceVer = "未定";
		if(deviceVerType == 1){
			deviceVer = "E8-C";
		}else if(deviceVerType == 2){
			deviceVer = "PON融合";
		}else if(deviceVerType == 3){
			deviceVer = "10GPON";
		}else if(deviceVerType == 4){
			deviceVer = "政企网关";
		}else if(deviceVerType == 5){
			deviceVer = "天翼网关1.0";
		}else if(deviceVerType == 6){
			deviceVer = "天翼网关2.0";
		}else if(deviceVerType ==7){
			deviceVer = "天翼网关3.0";
		}
		return deviceVer;
	}
}
