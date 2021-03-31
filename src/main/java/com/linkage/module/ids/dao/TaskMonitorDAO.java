package com.linkage.module.ids.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.gwms.dao.SuperDAO;

public class TaskMonitorDAO extends SuperDAO {
	private static Logger logger = LoggerFactory
			.getLogger(TaskMonitorDAO.class);

	/**
	 * 监控任务配置
	 * @param task_starttime
	 * @param task_peroid
	 * @param mail_receiver
	 * @param mail_subject
	 * @param mail_content
	 * @param monitor_type
	 * @param monitor_content
	 * @return
	 */
	public int addTaskMonitor(String task_starttime, String task_peroid,
			String mail_receiver, String mail_subject, String mail_content,
			String monitor_type, String monitor_content) {
		DateTimeUtil st = new DateTimeUtil();
		String currentTime = String.valueOf(st.getLongTime());
		
		StringBuffer sb = new StringBuffer();
		PrepareSQL psql = new PrepareSQL();
		psql.append("insert into tab_task_monitor values(?,?,?,?,?,?,?,?,?)");
		psql.setString(1, currentTime);
		psql.setString(2,task_starttime);
		psql.setInt(3,StringUtil.getIntegerValue(task_peroid));
		psql.setString(4,mail_receiver);
		psql.setString(5,mail_subject);
		psql.setString(6,mail_content);
		psql.setInt(7,StringUtil.getIntegerValue(monitor_type));
		psql.setString(8,monitor_content);
		psql.setLong(9, StringUtil.getLongValue(currentTime));
		
		int result = jt.update(psql.getSQL());
		return result;
	}
}
