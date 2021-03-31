
package com.linkage.module.itms.service.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.util.StringUtil;

/**
 * @author Administrator (Ailk No.)
 * @version 1.0
 * @since 2013-9-16
 * @category com.linkage.module.itms.service.dao
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class MonitorFlowInfoQueryDAO extends SuperDAO
{

	private static Logger logger = LoggerFactory.getLogger(MonitorFlowInfoQueryDAO.class);

	private Map<String,String> statusMap = new HashMap<String, String>();
	
	public MonitorFlowInfoQueryDAO(){
		statusMap.put("0", "正在采集");
		statusMap.put("1", "成功");
	}
	
	public List<Map> getMonitorFlowQuery(String start_time, String end_time,
			String device_serialnumber, String dev_sub_sn, int curPage_splitPage,
			int num_splitPage)
	{
		logger.debug("MonitorFlowInfoQueryDAO=>getMonitorFlowQuery()");
		StringBuffer sql = new StringBuffer();
		if(DBUtil.GetDB()==3){
			sql.append("select a.task_id,a.device_id,a.start_time,a.end_time,");
			sql.append("a.interval,a.times,a.status,b.device_serialnumber ");
		}else{
			sql.append("select a.*,b.device_serialnumber ");
		}
		sql.append("from gw_monitor_task a,tab_gw_device b where a.device_id=b.device_id ");
		if (!StringUtil.IsEmpty(device_serialnumber))
		{
			sql.append(" and b.device_serialnumber like '%").append(device_serialnumber)
					.append("' ");
		}
		if (!StringUtil.IsEmpty(dev_sub_sn))
		{
			sql.append(" and b.dev_sub_sn='").append(dev_sub_sn).append("' ");
		}
		if (!StringUtil.IsEmpty(start_time))
		{
			sql.append(" and a.start_time>=").append(start_time);
		}
		if (!StringUtil.IsEmpty(end_time))
		{
			sql.append(" and a.start_time<").append(end_time);
		}
		PrepareSQL psql = new PrepareSQL(sql.toString());
		List<Map> list = new ArrayList<Map>();
		list = querySP(psql.getSQL(), (curPage_splitPage - 1) * num_splitPage + 1,
				num_splitPage, new RowMapper()
				{
					@Override
					public Object mapRow(ResultSet rs, int arg1) throws SQLException
					{
						Map<String, String> map = new HashMap<String, String>();
						map.put("task_id",
								StringUtil.getStringValue(rs.getString("task_id")));
						map.put("device_id",
								StringUtil.getStringValue(rs.getString("device_id")));
						map.put("device_serialnumber", StringUtil.getStringValue(rs
								.getString("device_serialnumber")));
						map.put("start_time", getDate(StringUtil.getLongValue(rs
								.getString("start_time"))));
						map.put("end_time",
								getDate(StringUtil.getLongValue(rs.getString("end_time"))));
						map.put("interval",
								StringUtil.getStringValue(rs.getString("interval")));
						map.put("times", StringUtil.getStringValue(rs.getString("times")));
						map.put("status",statusMap.get(StringUtil.getStringValue(rs.getString("status"))));
						return map;
					}
				});
		return list;
	}
	
	public int getMonitorFlowQueryCount(String start_time, String end_time,
			String device_serialnumber, String dev_sub_sn, int curPage_splitPage,
			int num_splitPage)
	{
		logger.debug("MonitorFlowInfoQueryDAO=>getMonitorFlowQueryCount()");
		StringBuffer sql = new StringBuffer();
		if(DBUtil.GetDB()==3){
			sql.append("select count(*) ");
		}else{
			sql.append("select count(1) ");
		}
		sql.append("from gw_monitor_task a,tab_gw_device b where a.device_id=b.device_id ");
		if (!StringUtil.IsEmpty(device_serialnumber))
		{
			sql.append(" and b.device_serialnumber like '%").append(device_serialnumber)
					.append("' ");
		}
		if (!StringUtil.IsEmpty(dev_sub_sn))
		{
			sql.append(" and b.dev_sub_sn='").append(dev_sub_sn).append("' ");
		}
		if (!StringUtil.IsEmpty(start_time))
		{
			sql.append(" and a.start_time>=").append(start_time);
		}
		if (!StringUtil.IsEmpty(end_time))
		{
			sql.append(" and a.start_time<").append(end_time);
		}
		
		PrepareSQL psql = new PrepareSQL(sql.toString());
		int total = jt.queryForInt(psql.getSQL());
		int maxPage = 1;
		if (total % num_splitPage == 0)
		{
			maxPage = total / num_splitPage;
		}
		else
		{
			maxPage = total / num_splitPage + 1;
		}
		return maxPage;
		
	}

	public List<Map> getePonLanInfo(String task_id, String device_id,
			int curPage_splitPage, int num_splitPage)
	{
		logger.debug("MonitorFlowInfoQueryDAO=>getePonLanInfo()");
		StringBuffer sql = new StringBuffer();
		if(DBUtil.GetDB()==3){
			sql.append("select start_time,status,pon_send,pon_send_average,pon_receive,");
			sql.append("pon_receive_average,lan1_send,lan1_send_average,lan1_receive,");
			sql.append("lan1_receive_average,lan2_send,lan2_send_average,lan2_receive,");
			sql.append("lan2_receive_average,lan3_send,lan3_send_average,lan3_receive,");
			sql.append("lan3_receive_average,lan4_send,lan4_send_average,lan4_receive,lan4_receive_average ");
		}else{
			sql.append("select * ");
		}
		sql.append("from gw_epon_lan_info where 1=1 ");
		if (!StringUtil.IsEmpty(task_id))
		{
			sql.append(" and task_id=").append(task_id);
		}
		if (!StringUtil.IsEmpty(device_id))
		{
			sql.append(" and device_id='").append(device_id).append("' ");
		}
		sql.append(" order by gather_count");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		List<Map> list = new ArrayList<Map>();
		list = querySP(psql.getSQL(), (curPage_splitPage - 1) * num_splitPage + 1,
				num_splitPage);
		if(list.size()>0){
			for (int i = 0; i < list.size(); i++)
			{
				long time = StringUtil.getLongValue(list.get(i).get("start_time"));
				list.get(i).put("start_time", getDate(time));
				list.get(i).put("status", statusMap.get(StringUtil.getStringValue(list.get(i).get("status"))));
			}
		}
		return list;
	}
	
	public int getePonLanInfoCount(String task_id, String device_id,
			int curPage_splitPage, int num_splitPage)
	{
		logger.debug("MonitorFlowInfoQueryDAO=>getePonLanInfoCount()");
		StringBuffer sql = new StringBuffer();
		if(DBUtil.GetDB()==3){
			sql.append("select count(*) from gw_epon_lan_info where 1=1 ");
		}else{
			sql.append("select count(1) from gw_epon_lan_info where 1=1 ");
		}
		
		if (!StringUtil.IsEmpty(task_id))
		{
			sql.append(" and task_id=").append(task_id);
		}
		if (!StringUtil.IsEmpty(device_id))
		{
			sql.append(" and device_id='").append(device_id).append("' ");
		}
		PrepareSQL psql = new PrepareSQL(sql.toString());
		int total = jt.queryForInt(psql.getSQL());
		int maxPage = 1;
		if (total % num_splitPage == 0)
		{
			maxPage = total / num_splitPage;
		}
		else
		{
			maxPage = total / num_splitPage + 1;
		}
		return maxPage;
	}
	
	public int[] deleteMonitorFlow(String task_id){
		logger.debug("MonitorFlowInfoQueryDAO=>deleteMonitorFlow()"); 
		
		if(!StringUtil.IsEmpty(task_id)){
			PrepareSQL psql1 = new PrepareSQL("delete from gw_monitor_task where task_id="+task_id);
			PrepareSQL psql2 = new PrepareSQL("delete from gw_epon_lan_info where task_id="+task_id);
			String[] arr = {psql1.getSQL(),psql2.getSQL()};
			return jt.batchUpdate(arr);
		}else {
			return null;
		}
	}

	// 获取当前日期，日期格式为：yyyy-MM-dd HH:MM:SS
	private String getDate(long serverDateTime)
	{
		if (0 == serverDateTime)
		{
			return "";
		}
		else
		{
			serverDateTime = serverDateTime*1000;
			DateTimeUtil dt = new DateTimeUtil(serverDateTime);
			return dt.getLongDate();
		}
	}
}
