package com.linkage.module.gtms.config.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.DBOperation;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.DateTimeUtil;
import com.linkage.commons.util.StringUtil;
import com.linkage.module.gwms.dao.SuperDAO;

public class VoipChangeConfigDAO  extends SuperDAO
{

	private static Logger logger = LoggerFactory.getLogger(VoipChangeConfigDAO.class);

	/**
	 * 查询是否还有语音参数
	 */
	public Map<String, String> getInfoByLoid(String loid)
	{
		PrepareSQL pSQL = new PrepareSQL();
		pSQL.append("select c.user_id,c.device_id ");
		pSQL.append("from tab_hgwcustomer c,hgwcust_serv_info s ");
		pSQL.append("where c.user_id=s.user_id and s.serv_type_id=14 and c.username=? ");
		pSQL.setString(1, loid);
		return DBOperation.getRecord(pSQL.getSQL());
	}

	/**
	 * 入临时表
	 */
	public void insertvoipChangeReport_tmp(String taskid,List<HashMap<String, String>> listNew)
	{
		logger.debug("insertvoipChangeReport_tmp");
		PrepareSQL psql = new PrepareSQL();
		//删除三天前的临时数据
		psql.append("delete from tab_voip_change_report_tmp ");
		psql.append("where task_id<"+(new DateTimeUtil().getLongTime()-259200));
		DBOperation.executeUpdate(psql.getSQL());

		ArrayList<String> sqlList = new ArrayList<String>();
		psql = new PrepareSQL();
		psql.append("insert into tab_voip_change_report_tmp(username,city_id,");
		psql.append("voip_port,reg_id_type,prox_serv,prox_port,stand_prox_serv,");
		psql.append("stand_prox_port,set_result,device_id,task_id,user_id,rtp_prefix) ");
		psql.append("values(?,?,?,?,?,?,?,?,?,?,?,?,?) ");

		for (Map<String, String> map : listNew)
		{
			psql.setString(1, map.get("username"));
			psql.setLong(2, StringUtil.getLongValue(map.get("city_id")));
			psql.setString(3, map.get("voip_port"));
			// 0：IP地址，1：域名，2：设备名，当前为1
			if ("IP".equalsIgnoreCase((String) map.get("reg_id_type"))){
			    // 终端标识的类型
				psql.setInt(4, StringUtil.getIntegerValue("0"));
			}
			else if ("DomainName".equalsIgnoreCase((String) map.get("reg_id_type"))){
				psql.setInt(4, StringUtil.getIntegerValue("1"));
			}
			psql.setString(5, map.get("prox_serv"));
			psql.setInt(6, StringUtil.getIntegerValue(map.get("prox_port")));
			psql.setString(7,map.get("stand_prox_serv"));
			psql.setInt(8, StringUtil.getIntegerValue(map.get("stand_prox_port")));
			psql.setInt(9,0);
			psql.setString(10, map.get("device_id"));
			psql.setLong(11, StringUtil.getLongValue(taskid));
			psql.setLong(12, StringUtil.getLongValue(map.get("user_id")));
			psql.setString(13, map.get("rtp_prefix"));

			sqlList.add(psql.getSQL());
			//每500条处理一次，避免一个任务，待插入的数量太多，导致插入挂掉。
			if(sqlList.size() == 500)
			{
				DBOperation.executeUpdate(sqlList);
				sqlList.clear();
			}

		}

		if(sqlList.size()>0){
			DBOperation.executeUpdate(sqlList);
		}
		sqlList.clear();
		sqlList=null;
	}

	/**
	 * 将临时表数据插入正式表
	 */
	public void insertvoipChangeReport(String taskid)
	{
		//JLLT-RMS-BUG-20200407－JH-001【吉林联通语音NGN割接统计报表】  bug单  修改
		//破坏以task_id 为维度的统计结果，在每次任务入tab_voip_change_report 之前，删除之前所有任务中，与该次任务重复的记录。以username为基础
		PrepareSQL deleteSql = new PrepareSQL();
		deleteSql.append("delete from tab_voip_change_report a  where exists ("
				+ " select task_id from tab_voip_change_report_tmp b "
				+ " where a.username = b.username and b.task_id = ?) ");
		deleteSql.setLong(1, StringUtil.getLongValue(taskid));
		DBOperation.executeUpdate(deleteSql.getSQL());

		// 查询 tab_voip_change_report_tmp
		PrepareSQL selectSql = new PrepareSQL();
		selectSql.append("select username,city_id,voip_port,reg_id_type,prox_serv,");
		selectSql.append("prox_port,stand_prox_serv,stand_prox_port,set_result,device_id,task_id,user_id,rtp_prefix ");
		selectSql.append("from tab_voip_change_report_tmp where task_id=? ");
		selectSql.setLong(1, StringUtil.getLongValue(taskid));
		ArrayList<HashMap<String, String>> list = DBOperation.getRecords(selectSql.getSQL());
		if(list == null || list.isEmpty()){
			return;
		}
		// 将查询结果插入 tab_voip_change_report
		ArrayList insertSqlList = new ArrayList();
		PrepareSQL insertSql = new PrepareSQL("insert into tab_voip_change_report(username, city_id," +
			"voip_port, reg_id_type, prox_serv, prox_port, stand_prox_serv," +
			"stand_prox_port, set_result, device_id, task_id, user_id, rtp_prefix) " +
			"values (?,?,?,?,?, ?,?,?,?,?, ?,?,?)");
		for (HashMap<String, String> map : list) {
			insertSql.setString(1, StringUtil.getStringValue(map, "username"));
			insertSql.setString(2, StringUtil.getStringValue(map, "city_id"));
			insertSql.setString(3, StringUtil.getStringValue(map, "voip_port"));
			insertSql.setInt(4, StringUtil.getIntValue(map, "reg_id_type"));
			insertSql.setString(5, StringUtil.getStringValue(map, "prox_serv"));
			insertSql.setInt(6, StringUtil.getIntValue(map, "prox_port"));
			insertSql.setString(7, StringUtil.getStringValue(map, "stand_prox_serv"));
			insertSql.setInt(8, StringUtil.getIntValue(map, "stand_prox_port"));
			insertSql.setString(9, StringUtil.getStringValue(map, "set_result"));
			insertSql.setString(10, StringUtil.getStringValue(map, "device_id"));
			insertSql.setInt(11, StringUtil.getIntValue(map, "task_id"));
			insertSql.setInt(12, StringUtil.getIntValue(map, "user_id"));
			insertSql.setString(13, StringUtil.getStringValue(map, "rtp_prefix"));
			insertSqlList.add(insertSql.getSQL());
			//每500条处理一次
			if(insertSqlList.size() == 500)
			{
				DBOperation.executeUpdate(insertSqlList);
				insertSqlList.clear();
			}
		}
		if(insertSqlList.size()>0){
			DBOperation.executeUpdate(insertSqlList);
		}
	}

	public int inserttask(String taskid, long userId)
	{
		/*task_id 	number(14) not null,
		ocid 		number(14) not null,
		task_status number(1) default 1 not null,,
	    start_time  number(14) not null*/

	    PrepareSQL psql = new PrepareSQL(
				"insert into tab_voip_change_task(task_id,ocid,task_status,start_time) values(?,?,?,?)");
		psql.setLong(1, StringUtil.getLongValue(taskid));
		psql.setLong(2, userId);
		psql.setInt(3, 1);
		psql.setLong(4, new DateTimeUtil().getLongTime());
		return DBOperation.executeUpdate(psql.getSQL());
	}

}
