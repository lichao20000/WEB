package com.linkage.module.ids.bio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.ids.dao.TaskMonitorDAO;

public class TaskMonitorBIO {
	private static Logger logger = LoggerFactory
			.getLogger(TaskMonitorBIO.class);

	private TaskMonitorDAO dao;

	public int addTaskMonitor(String task_starttime, String task_peroid,
			String mail_receiver, String mail_subject, String mail_content,
			String monitor_type, String monitor_content) {
         logger.debug("addTaskMonitor()");
		return dao.addTaskMonitor(task_starttime, task_peroid, mail_receiver,
				mail_subject, mail_content, monitor_type, monitor_content);
	}

	public void setDao(TaskMonitorDAO dao) {
		this.dao = dao;
	}
	
}
