
package com.linkage.module.gwms.resource.dao;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.DBUtil;
import com.linkage.litms.common.database.DBOperation;
import com.linkage.litms.common.database.PrepareSQL;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.util.StringUtil;

public class BatchHttpTestDAO extends SuperDAO
{
	private static Logger logger = LoggerFactory.getLogger(BatchHttpTestDAO.class);

	/**
	 * 插入批量测速任务表
	 * @author fanjm
	 * @param param0  页面输入的参数
	 * @param param 参数
	 * @return sql更改表的行数
	 */
	public int createHttpTaskSQL(Object[] param, String param0) {
		logger.warn("createHttpTaskSQL({})",param);
		
		StringBuilder sql = new StringBuilder();
		sql.append("insert into tab_batchhttp_task (");
		sql.append("task_name,task_id,acc_oid,add_time,task_status,http_url,report_url");
		if(null != param[7]){
			sql.append(",sql");
			logger.warn("cpe_allocatedstatus="+param[14]);
			logger.warn("online_status="+param[10]);
			if(null != param0){
				if(null!=param[9] && !"".equals(param[9]) && !"-1".equals(param[9])){
					sql.append(",city_id");
				}
				if(null!=param[10] && !"".equals(param[10]) && !"-1".equals(param[10])){
					logger.warn("param[10] key.............");
					sql.append(",online_status");
				}
				if(null!=param[11] && !"".equals(param[11]) && !"-1".equals(param[11])){
					sql.append(",vendor_id");
				}
				if(null!=param[12] && !"".equals(param[12]) && !"-1".equals(param[12])){
					sql.append(",device_model_id");
				}
				if(null!=param[13] && !"".equals(param[13]) && !"-1".equals(param[13])){
					sql.append(",devicetype_id");
				}
				if(null!=param[14] && !"".equals(param[14]) && !"-1".equals(param[14])){
					sql.append(",cpe_allocatedstatus");
				}
				if(null!=param[15] && !"".equals(param[15]) && !"-1".equals(param[15])){
					sql.append(",device_serialnumber");
				}
			}
		}
		else if(null != param[8]){
			sql.append(",filePath");
		}
		sql.append(",task_desc ");
		
		//河北联通任务表增加type
		if (Global.HBLT.equals(Global.instAreaShortName)) {
			sql.append(" ,type ");
		}
		else if (Global.ZJLT.equals(Global.instAreaShortName)) {
			sql.append(" ,BEGIN_TIME,END_TIME,TOTAL_TIMES,PERIOD ");
		}
		
		sql.append(" ) values (?,?,?,?,?,?,?");
		if(null != param[7]){
			sql.append(",?");
			
			if(null != param0){
				if(null!=param[9] && !"null".equals(param[9]) && !"".equals(param[9]) && !"-1".equals(param[9])){
					sql.append(",?");
				}
				if(null!=param[10] && !"".equals(param[10]) && !"-1".equals(param[10])){
					logger.warn("param[10] ?.............");
					sql.append(",?");
				}
				if(null!=param[11] && !"null".equals(param[11]) && !"".equals(param[11]) && !"-1".equals(param[11])){
					sql.append(",?");
				}
				if(null!=param[12] && !"null".equals(param[12]) && !"".equals(param[12]) && !"-1".equals(param[12])){
					sql.append(",?");
				}
				if(null!=param[13] && !"".equals(param[13]) && !"-1".equals(param[13])){
					sql.append(",?");
				}
				if(null!=param[14] && !"".equals(param[14]) && !"-1".equals(param[14])){
					sql.append(",?");
				}
				if(null!=param[15] && !"null".equals(param[15]) && !"".equals(param[15]) && !"-1".equals(param[15])){
					sql.append(",?");
				}
			}
		}
		else if(null != param[8]){
			sql.append(",?");
		}
		sql.append(",?");//描述
		if (Global.HBLT.equals(Global.instAreaShortName)) {
			sql.append(",?");//type
		}
		else if (Global.ZJLT.equals(Global.instAreaShortName)) {
			sql.append(",?,?,?,?");//type
		}
		sql.append(" )");
		
		PrepareSQL psql = new PrepareSQL(sql.toString());
		psql.setString(1, (String)param[0]);//task_name
		psql.setLong(2, (Long)param[1]);//task_id
		psql.setLong(3, (Long)param[2]);//acc_oid
		psql.setLong(4, (Long)param[3]);//add_time
		psql.setInt(5, (Integer)param[4]); //状态 0：未执行，1：执行过
		psql.setString(6, (String)param[5]);//
		psql.setString(7, (String)param[6]);//
		int index = 7;
		if(null != param[7]){
			psql.setString(++index, (String)param[7]);
			
			if(null != param0){
				if(null!=param[9] && !"null".equals(param[9]) && !"".equals(param[9]) && !"-1".equals(param[9])){
					psql.setString(++index, (String)param[9]);
				}
				if(null!=param[10] && !"".equals(param[10]) && !"-1".equals(param[10])){
					logger.warn("param[10] value.............");
					psql.setInt(++index, StringUtil.getIntegerValue(param[10]));
				}
				if(null!=param[11] && !"null".equals(param[11]) && !"".equals(param[11]) && !"-1".equals(param[11])){
					psql.setString(++index, (String)param[11]);
				}
				if(null!=param[12] && !"null".equals(param[12]) && !"".equals(param[12]) && !"-1".equals(param[12])){
					psql.setString(++index, (String)param[12]);
				}
				if(null!=param[13] && !"".equals(param[13]) && !"-1".equals(param[13])){
					psql.setLong(++index, StringUtil.getLongValue(param[13]));
				}
				if(null!=param[14] && !"".equals(param[14]) && !"-1".equals(param[14])){
					psql.setInt(++index, StringUtil.getIntegerValue(param[14]));
				}
				if(null!=param[15] && !"null".equals(param[15]) && !"".equals(param[15]) && !"-1".equals(param[15])){
					psql.setString(++index, (String)param[15]);
				}
			}
		}
		else if(null != param[8]){
			psql.setString(++index, (String)param[8]);
		}
		psql.setString(++index, (String)param[16]);//最后赋值描述
		if (Global.HBLT.equals(Global.instAreaShortName)) {
			psql.setString(++index, (String)param[17]);//type 测速模式 1下行 2上行
		}
		else if (Global.ZJLT.equals(Global.instAreaShortName)) {
			psql.setLong(++index, StringUtil.getLongValue(param[17]));//type 测速模式 1下行 2上行
			psql.setLong(++index, StringUtil.getLongValue(param[18]));
			psql.setInt(++index, StringUtil.getIntegerValue(param[19]));
			psql.setInt(++index, StringUtil.getIntegerValue(param[20]));
		}
		
		logger.warn("插入批量测速任务表-->{}",psql.getSQL());
		return jt.update(psql.getSQL());
	}

	/**
	 * 插入批量测速任务表
	 * @author fanjm
	 * @param param0  页面输入的参数
	 * @param param 参数
	 * @return sql更改表的行数
	 */
	public int createHttpTaskSQLAHLT(Object[] param, String param0) {
		logger.warn("createHttpTaskSQL({})",param);
		StringBuilder sql = new StringBuilder();
		sql.append("insert into tab_batchhttp_task (");
		sql.append("task_name,task_id,acc_oid,add_time,task_status,http_url,report_url");
		if(null != param[7]){
			sql.append(",sql");
			logger.warn("cpe_allocatedstatus="+param[14]);
			logger.warn("online_status="+param[10]);
			if(null != param0){
				if(null!=param[9] && !"".equals(param[9]) && !"-1".equals(param[9])){
					sql.append(",city_id");
				}
				if(null!=param[10] && !"".equals(param[10]) && !"-1".equals(param[10])){
					logger.warn("param[10] key.............");
					sql.append(",online_status");
				}
				if(null!=param[11] && !"".equals(param[11]) && !"-1".equals(param[11])){
					sql.append(",vendor_id");
				}
				if(null!=param[12] && !"".equals(param[12]) && !"-1".equals(param[12])){
					sql.append(",device_model_id");
				}
				if(null!=param[13] && !"".equals(param[13]) && !"-1".equals(param[13])){
					sql.append(",devicetype_id");
				}
				if(null!=param[14] && !"".equals(param[14]) && !"-1".equals(param[14])){
					sql.append(",cpe_allocatedstatus");
				}
				if(null!=param[15] && !"".equals(param[15]) && !"-1".equals(param[15])){
					sql.append(",device_serialnumber");
				}
			}
		}
		else if(null != param[8]){
			sql.append(",filePath");
		}
		sql.append(",task_desc,speed_frequency,startDate,endDate ");
		sql.append(" ) values (?,?,?,?,?,?,?");
		if(null != param[7]){
			sql.append(",?");

			if(null != param0){
				if(null!=param[9] && !"null".equals(param[9]) && !"".equals(param[9]) && !"-1".equals(param[9])){
					sql.append(",?");
				}
				if(null!=param[10] && !"".equals(param[10]) && !"-1".equals(param[10])){
					logger.warn("param[10] ?.............");
					sql.append(",?");
				}
				if(null!=param[11] && !"null".equals(param[11]) && !"".equals(param[11]) && !"-1".equals(param[11])){
					sql.append(",?");
				}
				if(null!=param[12] && !"null".equals(param[12]) && !"".equals(param[12]) && !"-1".equals(param[12])){
					sql.append(",?");
				}
				if(null!=param[13] && !"".equals(param[13]) && !"-1".equals(param[13])){
					sql.append(",?");
				}
				if(null!=param[14] && !"".equals(param[14]) && !"-1".equals(param[14])){
					sql.append(",?");
				}
				if(null!=param[15] && !"null".equals(param[15]) && !"".equals(param[15]) && !"-1".equals(param[15])){
					sql.append(",?");
				}
			}
		}
		else if(null != param[8]){
			sql.append(",?");
		}
		sql.append(",?,?,?,?");//描述
		sql.append(" )");

		PrepareSQL psql = new PrepareSQL(sql.toString());
		psql.setString(1, (String)param[0]);//task_name
		psql.setLong(2, (Long)param[1]);//task_id
		psql.setLong(3, (Long)param[2]);//acc_oid
		psql.setLong(4, (Long)param[3]);//add_time
		psql.setInt(5, (Integer)param[4]); //状态 0：未执行，1：执行过
		psql.setString(6, (String)param[5]);//
		psql.setString(7, (String)param[6]);//
		int index = 7;
		if(null != param[7]){
			psql.setString(++index, (String)param[7]);

			if(null != param0){
				if(null!=param[9] && !"null".equals(param[9]) && !"".equals(param[9]) && !"-1".equals(param[9])){
					psql.setString(++index, (String)param[9]);
				}
				if(null!=param[10] && !"".equals(param[10]) && !"-1".equals(param[10])){
					logger.warn("param[10] value.............");
					psql.setInt(++index, StringUtil.getIntegerValue(param[10]));
				}
				if(null!=param[11] && !"null".equals(param[11]) && !"".equals(param[11]) && !"-1".equals(param[11])){
					psql.setString(++index, (String)param[11]);
				}
				if(null!=param[12] && !"null".equals(param[12]) && !"".equals(param[12]) && !"-1".equals(param[12])){
					psql.setString(++index, (String)param[12]);
				}
				if(null!=param[13] && !"".equals(param[13]) && !"-1".equals(param[13])){
					psql.setLong(++index, StringUtil.getLongValue(param[13]));
				}
				if(null!=param[14] && !"".equals(param[14]) && !"-1".equals(param[14])){
					psql.setInt(++index, StringUtil.getIntegerValue(param[14]));
				}
				if(null!=param[15] && !"null".equals(param[15]) && !"".equals(param[15]) && !"-1".equals(param[15])){
					psql.setString(++index, (String)param[15]);
				}
			}
		}
		else if(null != param[8]){
			psql.setString(++index, (String)param[8]);
		}
		psql.setString(++index, (String)param[16]);//赋值描述
		psql.setLong(++index, (Long)param[17]);//频次
		psql.setLong(++index, (Long)param[18]);//开始时间
		psql.setLong(++index, (Long)param[19]);//结束时间

		logger.debug("插入批量测速任务表-->{}",psql.getSQL());
		return jt.update(psql.getSQL());
	}
	
	/**
	 * 插入批量测速任务明细表
	 * @author fanjm
	 * @param  参数
	 * @return sql更改表的行数
	 */
	public String createHttpTask_devSQL(Object[] param) {
		logger.warn("createHttpTask_devSQL({})",param);
		
		StringBuilder sql = new StringBuilder();
		sql.append("insert into tab_batchhttp_task_dev (");
		sql.append("task_id,device_id,oui,Device_serialnumber,status,add_time,city_id");
		if(null!=param[7]){
			sql.append(",wan_type");
			
		}
		if (Global.ZJLT.equals(Global.instAreaShortName)) {
			sql.append(",begin_time,end_time,total_times,period");
		}
		sql.append(") values (?,?,?,?,?,?,?");
		if(null!=param[7]){
			sql.append(",?");
			
		}
		if (Global.ZJLT.equals(Global.instAreaShortName)) {
			sql.append(") values (,?,?,?,?");
		}
		sql.append(")");
		
		PrepareSQL psql = new PrepareSQL(sql.toString());
		psql.setLong(1, (Long)param[0]);
		psql.setString(2, (String)param[1]);
		psql.setString(3, (String)param[2]);
		psql.setString(4, (String)param[3]);
		psql.setInt(5, (Integer)param[4]);//状态 0：未执行，1：执行过
		psql.setLong(6, (Long)param[5]);
		psql.setString(7, (String)param[6]);
		psql.setInt(8, StringUtil.getIntegerValue(param[7]));
		if (Global.ZJLT.equals(Global.instAreaShortName)) {
			psql.setLong(9, (Long)param[8]);
			psql.setLong(10, (Long)param[9]);
			psql.setLong(11, (Long)param[10]);
			psql.setLong(12, (Long)param[11]);
		}
		
		logger.debug("生成插入批量测速任务明细表sql-->{}",psql.getSQL());
		return psql.getSQL();
	}
	
	/**
	 * 插入批量测速任务明细表
	 * @author fanjm
	 * @param  参数
	 * @return sql更改表的行数
	 */
	public int[] batchUpdate(String[] sqls) {
		logger.warn("batchUpdate({})",sqls);
		return jt.batchUpdate(sqls);
	}
	
	

	/**
	 * 获取tab_batchhttp_task当前最大任务id
	 * @return
	 */
	public Long getMaxTaskID()
	{
		String sql = "select max(task_id) max from tab_batchhttp_task";
		PrepareSQL psql = new PrepareSQL();
		psql.setSQL(sql);
		String task_id = StringUtil.getStringValue(queryForMap(psql.getSQL()).get("max")); 
		return StringUtil.IsEmpty(task_id) ? 0 : StringUtil.getLongValue(task_id);
	}
	

	
	/**
	 * 根据device_id获取oui 序列号 地区 wan_type等
	 * @return
	 */
	public Map getOuiSerial(String deviceid)
	{
		PrepareSQL psql = new PrepareSQL();
		psql.append("select a.oui,a.device_serialnumber,a.city_id,b.wan_type ");
		psql.append("from tab_gw_device a left join ");
		if(DBUtil.GetDB()==3){
			psql.append("(select wan_type,cast(user_id as signed) as user_id ");
		}else{
			psql.append("(select wan_type, to_char(user_id) as user_id ");
		}
		psql.append("from hgwcust_serv_info where serv_type_id=10) b ");
		psql.append("on a.customer_id=b.user_id where a.device_id = ?");
		
		psql.setString(1, deviceid);
		return DBOperation.getRecord(psql.getSQL()); 
	}
	
	public int updateHttpTask(Long task_id)
	{
		StringBuilder sql = new StringBuilder();
		sql.append("update tab_batchhttp_task set task_status= ? where task_id = ?");
		
		PrepareSQL psql = new PrepareSQL(sql.toString());
		psql.setInt(1, 1);
		psql.setLong(2, task_id);
		return jt.update(psql.getSQL());
	}

	/**
	 * 根据用户id获取用户创建的测速任务数
	 * @return
	 */
	public Map countTasks(Long acc_oid)
	{
		PrepareSQL psql = new PrepareSQL();
		if(DBUtil.GetDB()==3){
			psql.append("select count(*) as taskes ");
		}else{
			psql.append("select count(1) as taskes ");
		}
		psql.append("from tab_batchhttp_task where acc_oid="+acc_oid);
		
		return DBOperation.getRecord(psql.getSQL()); 
	}

	public List getTaskByTime(Long starttimeTemp, Long endtimeTemp) 
	{
		PrepareSQL psql = new PrepareSQL();
		psql.append("select t.startdate,t.enddate ");
		psql.append("from tab_batchhttp_task t ");
		psql.append("where startdate between ? and ? and enddate between ? and ? ");
		psql.setLong(1,starttimeTemp);
		psql.setLong(2,endtimeTemp);
		psql.setLong(3,starttimeTemp);
		psql.setLong(4,endtimeTemp);
		return DBOperation.getRecords(psql.getSQL());
	}
}
