package com.linkage.module.itms.report.dao;

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
import com.linkage.litms.LipossGlobals;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.SuperDAO;

/**
 * 
 * @author yaoli (Ailk No.)
 * @version 1.0
 * @since 2019年8月9日
 * @category com.linkage.module.itms.report.dao
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 *
 */
public class BatchRestartJXDAO extends SuperDAO
{
	private Logger logger = LoggerFactory.getLogger(BatchRestartJXDAO.class);
	
	@SuppressWarnings("unchecked")
	public List<Map> qryBatchStartTask(String startTime,String endTime,
			int curPage_splitPage,int num_splitPage)
	{
		logger.warn("BatchRestartJXDAO => qryBatchStartTask: start({}),end({})",new Object[]{startTime,endTime});
		
		StringBuilder sb = new StringBuilder();
		sb.append(" select task_id,");
		sb.append(" sum(case when(restart_status = 1) then 1 else 0 end) succ,");
		sb.append(" sum(case when(restart_status != 1) then 1 else 0 end) fail");
		sb.append(" from stb_tab_dev_restart where 1=1");
		
		if(!StringUtil.IsEmpty(startTime)){
			sb.append(" and restart_time >= "+startTime);
		}
		
		if(!StringUtil.IsEmpty(endTime)){
			sb.append(" and restart_time <= "+endTime);
		}
		
		sb.append(" group by task_id order by task_id desc ");
		PrepareSQL pSQL = new PrepareSQL(sb.toString());
		 if(-1 != curPage_splitPage){
		    	return querySP(pSQL.getSQL(), (curPage_splitPage - 1) * num_splitPage + 1,
		    			num_splitPage,new RowMapper() {
					  public Object mapRow(ResultSet rs, int arg1) throws SQLException {
						Map<String, String> map = new HashMap<String, String>();
						try {
							String succ = rs.getString("succ");
							String fail = rs.getString("fail");
							String taskId = rs.getString("task_id");
							map.put("succ", succ);
							map.put("fail", fail);
							map.put("total", StringUtil.getStringValue(
									Integer.valueOf(succ) + Integer.valueOf(fail)));
							map.put("taskId", taskId);
							long time = Long.valueOf(taskId);
							map.put("time",  new DateTimeUtil(time * 1000).getYYYY_MM_DD()); 
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
							String succ = rs.getString("succ");
							String fail = rs.getString("fail");
							String taskId = rs.getString("task_id");
							map.put("succ", succ);
							map.put("fail", fail);
							map.put("total", StringUtil.getStringValue(
									Integer.valueOf(succ) + Integer.valueOf(fail)));
							map.put("taskId", taskId);
							long time = Long.valueOf(taskId);
							map.put("time",  new DateTimeUtil(time * 1000).getYYYY_MM_DD()); 

						} catch (SQLException e) {
							logger.error(e.getMessage());
						}
						return map;  
					}
				});
		    }
	}
	
	public int qryBatchStartTaskCount(String startTime,String endTime)
	{
         logger.warn("BatchRestartJXDAO => qryBatchStartTaskCount: start({}),end({})",
        		 new Object[]{startTime,endTime});
		
		StringBuilder sb = new StringBuilder();
		if(DBUtil.GetDB()==3){
			sb.append(" select count(*) num");
		}else{
			sb.append(" select count(1) num");
		}
		
		sb.append(" from stb_tab_dev_restart where 1=1");
		
		if(!StringUtil.IsEmpty(startTime)){
			sb.append(" and restart_time >= "+startTime);
		}
		
		if(!StringUtil.IsEmpty(endTime)){
			sb.append(" and restart_time <= "+endTime);
		}
		
		sb.append(" group by task_id order by task_id desc ");
		
		PrepareSQL pSQL = new PrepareSQL(sb.toString());
		try
		{
			List<HashMap<String,String>> mapsList = DBOperation.getRecords(pSQL.getSQL());
			if(null != mapsList && !mapsList.isEmpty() && mapsList.size() > 0){
				return mapsList.size();
			}
		}
		catch (Exception e)
		{
			logger.error("qryBatchStartTaskCount error");
			e.printStackTrace();
		}
		return 0;
		
	}
	public List<HashMap<String,String>> qryBatchStartTaskExcel(String startTime,String endTime){
		logger.warn("BatchRestartJXDAO => qryBatchStartTask: start({}),end({})",new Object[]{startTime,endTime});
		
		StringBuilder sb = new StringBuilder();
		sb.append("select count(*) total,");
		if(LipossGlobals.inArea(Global.JXDX)){
			if(DBUtil.GetDB()==3){
				sb.append("date_format(cast(task_id as signed)/(60*60*24)+str_to_date('1970-01-01 08:00:00','%Y-%m-%d %H:%i:%s'),'%Y/%m/%d') time,");
			}else{
				sb.append("to_char(to_number(task_id)/(60*60*24)+to_date('1970-01-01 08:00:00','YYYY-MM-DD HH24:MI:SS'),'YYYY/MM/DD') time,");
			}
		}else{
			if(DBUtil.GetDB()==3){
				sb.append("rtrim(CONVERT(char, dateadd(ss,cast(task_id as signed),'1970/01/01 08:00'), 111)) time,");
			}else{
				sb.append("rtrim(CONVERT(char, dateadd(ss,convert(numeric(10),task_id),'1970/01/01 08:00'), 111)) time,");
			}
			
		}
		sb.append(" sum(case when(restart_status = 1) then 1 else 0 end) succ,");
		sb.append(" sum(case when(restart_status != 1) then 1 else 0 end) fail");
		sb.append(" from stb_tab_dev_restart where 1=1");
		
		if(!StringUtil.IsEmpty(startTime)){
			sb.append(" and restart_time >= "+startTime);
		}
		
		if(!StringUtil.IsEmpty(endTime)){
			sb.append(" and restart_time <= "+endTime);
		}
		sb.append(" group by task_id order by task_id desc");
		PrepareSQL pSQL = new PrepareSQL(sb.toString());
		try
		{
			return DBOperation.getRecords(pSQL.getSQL());
		}
		catch (Exception e)
		{
			logger.warn("qryBatchStartTaskExcel error");
			e.printStackTrace();
		}
		return null;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Map> qryDetail(String startTime,String endTime,String taskId,String type,
			int curPage_splitPage,int num_splitPage)
	{
		/**	if(DBUtil.GetDB()==3){
			//TODO wait
		}else{
			
		} */
		PrepareSQL pSQL = new PrepareSQL();
		pSQL.append("select aa.vendor_name,c.device_model,d.softwareversion,");
		pSQL.append("bb.city_name,b.device_serialnumber,b.loopback_ip,a.serv_account ");
		pSQL.append("from stb_tab_dev_restart a,stb_tab_gw_device b,stb_gw_device_model c,");
		pSQL.append("stb_tab_devicetype_info d,stb_tab_vendor aa,tab_city bb ");
		pSQL.append("where a.device_id=b.device_id and b.device_model_id=c.device_model_id ");
		pSQL.append("and b.devicetype_id=d.devicetype_id and b.city_id=bb.city_id ");
		pSQL.append("and b.vendor_id=aa.vendor_id and a.task_id=? ");
		
		if("succ".equals(type)){
			pSQL.append(" and restart_status=1");
		}else if("fail".equals(type)){
			pSQL.append(" and restart_status!=1");
		}
		pSQL.setString(1, taskId);
		
		 if(-1 != curPage_splitPage){
		    	return querySP(pSQL.getSQL(), (curPage_splitPage - 1) * num_splitPage + 1,
		    			num_splitPage,new RowMapper() {
					  public Object mapRow(ResultSet rs, int arg1) throws SQLException {
						Map<String, String> map = new HashMap<String, String>();
						try {
							map.put("vendorName", rs.getString("vendor_name"));
							map.put("deviceModel", rs.getString("device_model"));
							map.put("softwareversion", rs.getString("softwareversion"));
							map.put("cityName", rs.getString("city_name"));
							map.put("devSn", rs.getString("device_serialnumber"));
							map.put("devIp", rs.getString("loopback_ip"));
							map.put("servAccount", rs.getString("serv_account"));
							 
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
							map.put("vendorName", rs.getString("vendor_name"));
							map.put("deviceModel", rs.getString("device_model"));
							map.put("softwareversion", rs.getString("softwareversion"));
							map.put("cityName", rs.getString("city_name"));
							map.put("devSn", rs.getString("device_serialnumber"));
							map.put("devIp", rs.getString("loopback_ip"));
							map.put("servAccount", rs.getString("serv_account"));
						} catch (SQLException e) {
							logger.error(e.getMessage());
						}
						return map;  
					}
				});
		    }
	}
	
	public int qryDetailCount(String startTime,String endTime,String taskId,String type)
	{
		PrepareSQL pSQL = new PrepareSQL();
		if(DBUtil.GetDB()==3){
			//TODO wait
			pSQL.append("select count(*) num ");
		}else{
			pSQL.append("select count(1) num ");
		}
		pSQL.append("from stb_tab_dev_restart a,stb_tab_gw_device b,stb_gw_device_model c,");
		pSQL.append("stb_tab_devicetype_info d,stb_tab_vendor aa,tab_city bb ");
		pSQL.append("where a.device_id=b.device_id and b.device_model_id=c.device_model_id ");
		pSQL.append("and b.devicetype_id=d.devicetype_id and b.city_id=bb.city_id ");
		pSQL.append("and b.vendor_id=aa.vendor_id and a.task_id=? ");
		if("succ".equals(type)){
			pSQL.append(" and restart_status = 1");
		}else if("fail".equals(type)){
			pSQL.append(" and restart_status != 1");
		}
		pSQL.setString(1, taskId);
		
		try
		{
			Map<String,String> mapsList = DBOperation.getRecord(pSQL.getSQL());
			if(null != mapsList && !mapsList.isEmpty()){
				return StringUtil.getIntegerValue(mapsList.get("num"));
			}
		}
		catch (Exception e)
		{
			logger.error("qryDetailCount error");
			e.printStackTrace();
		}
		return 0;
	}
	
	public List<HashMap<String,String>> qryDetailExcel(String startTime,String endTime,String taskId,String type)
	{
		/**	if(DBUtil.GetDB()==3){
			//TODO wait
		}else{
			
		} */
		PrepareSQL pSQL = new PrepareSQL();
		pSQL.append("select aa.vendor_name,c.device_model,d.softwareversion,");
		pSQL.append("bb.city_name,b.device_serialnumber,b.loopback_ip,a.serv_account ");
		pSQL.append("from stb_tab_dev_restart a,stb_tab_gw_device b,stb_gw_device_model c,");
		pSQL.append("stb_tab_devicetype_info d,stb_tab_vendor aa,tab_city bb ");
		pSQL.append("where a.device_id=b.device_id and b.device_model_id=c.device_model_id ");
		pSQL.append("and b.devicetype_id=d.devicetype_id and b.city_id=bb.city_id ");
		pSQL.append("and b.vendor_id=aa.vendor_id and a.task_id=? ");
		if("succ".equals(type)){
			pSQL.append(" and restart_status = 1");
		}else if("fail".equals(type)){
			pSQL.append(" and restart_status != 1");
		}	
		pSQL.setString(1, taskId);
		
		try
		{
			  return DBOperation.getRecords(pSQL.getSQL());
		}
		catch (Exception e)
		{
			logger.error("qryDetailCount error");
			e.printStackTrace();
		}
		return null;
	}
	 
}
