
package com.linkage.module.itms.service.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.util.StringUtil;

/**
 * @author Administrator (Ailk No.)
 * @version 1.0
 * @since 2013-9-13
 * @category com.linkage.module.itms.service.dao
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class MonitorFlowDAO extends SuperDAO
{
	private static Logger logger = LoggerFactory.getLogger(MonitorFlowDAO.class);

	
	/**
	 * 定制监控任务
	 * @param task_id
	 * @param device_id
	 * @param interval
	 * @param times
	 * @param start_time
	 * @param end_time
	 * @param status
	 * @return
	 */
	public int customTask(String task_id, String device_id, String interval,
			String times, String start_time, String end_time, String status)
	{
		logger.debug("MonitorFlowDAO==>customTask");
		StringBuffer sql = new StringBuffer();
		sql.append("insert into gw_monitor_task(task_id");
		if (!StringUtil.IsEmpty(device_id))
		{
			sql.append(" ,device_id");
		}
		if (!StringUtil.IsEmpty(interval))
		{
			sql.append(" ,interval");
		}
		if (!StringUtil.IsEmpty(times))
		{
			sql.append(" ,times");
		}
		if (!StringUtil.IsEmpty(start_time))
		{
			sql.append(" ,start_time");
		}
		if (!StringUtil.IsEmpty(end_time))
		{
			sql.append(" ,end_time");
		}
		if (!StringUtil.IsEmpty(status))
		{
			sql.append(" ,status");
		}
		sql.append(") values(").append(task_id);
		
		if (!StringUtil.IsEmpty(device_id))
		{
			sql.append(" ,'").append(device_id).append("'");
		}
		if (!StringUtil.IsEmpty(interval))
		{
			sql.append(" ,").append(interval);
		}
		if (!StringUtil.IsEmpty(times))
		{
			sql.append(" ,").append(times);
		}
		if (!StringUtil.IsEmpty(start_time))
		{
			sql.append(" ,").append(start_time);
		}
		if (!StringUtil.IsEmpty(end_time))
		{
			sql.append(" ,").append(end_time);
		}
		if (!StringUtil.IsEmpty(status))
		{
			sql.append(" ,").append(status);
		}
		sql.append(")");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		return jt.update(psql.getSQL());
	}
}
