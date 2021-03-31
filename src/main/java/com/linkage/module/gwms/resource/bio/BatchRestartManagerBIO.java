package com.linkage.module.gwms.resource.bio;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.gwms.resource.dao.BatchRestartManagerDAO;

public class BatchRestartManagerBIO {
	
	private static Logger logger = LoggerFactory.getLogger(BindLogViewBIO.class);
	
	private BatchRestartManagerDAO dao;
	
	
	public List<Map> qryTaskList(String taskNmae,String taskDesc,String addTime,String finalTime,String startTime,String endTime,int status,String subDevSn,int curPage_splitPage,int num_splitPage){
		logger.debug("BatchRestartManagerBIO=>qryTaskList");
		return dao.qryTaskList(taskNmae, taskDesc, addTime, finalTime, startTime, endTime, status, subDevSn, curPage_splitPage, num_splitPage);
	}

	public int qryTaskcountList(String taskNmae,String taskDesc,String addTime,String finalTime,String startTime,String endTime,int status,String subDevSn){
		logger.debug("BatchRestartManagerBIO=>qryTaskcountList");
		return dao.qryTaskCountList(taskNmae, taskDesc, addTime, finalTime, startTime, endTime, status, subDevSn);
	}
	public List<HashMap<String,String>> qryTaskExcelList(String taskNmae,String taskDesc,String addTime,String finalTime,String startTime,String endTime,int status,String subDevSn){
		logger.debug("BatchRestartManagerBIO=>qryTaskExcelList");
		return dao.qryTaskExcelList(taskNmae, taskDesc, addTime, finalTime, startTime, endTime, status, subDevSn);
	}
	
	public List<Map> qryTaskDetail(String taskId,String type,String subDevSn,int curPage_splitPage,int num_splitPage){
		logger.debug("BatchRestartManagerBIO=>qryTaskDetail");
		return dao.qryTaskDetail(taskId, type, subDevSn, curPage_splitPage, num_splitPage);
	}

	public int qryTaskDetailCount(String taskId,String type,String subDevSn){
		return dao.qryTaskDetailCount(taskId, type, subDevSn);
	}
	
	public List<HashMap<String,String>> qryTaskDetailExcel(String taskId,String type,String subDevSn){
		logger.debug("BatchRestartManagerBIO=>qryTaskExcelList");
		return dao.qryTaskDetailExcel(taskId, type, subDevSn);
	}
	
	public String operTask(String taskId,String operType){
		return dao.operTask(taskId, operType);
		
	}
	
	public BatchRestartManagerDAO getDao()
	{
		return dao;
	}


	public void setDao(BatchRestartManagerDAO dao)
	{
		this.dao = dao;
	}
}
