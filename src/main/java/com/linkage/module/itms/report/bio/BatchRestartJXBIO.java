package com.linkage.module.itms.report.bio;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.StringUtil;
import com.linkage.module.itms.report.dao.BatchRestartJXDAO;
import com.linkage.module.itms.report.dao.HttpTestReportDAO;

/**
 * 
 * @author yaoli (Ailk No.)
 * @version 1.0
 * @since 2019年8月9日
 * @category com.linkage.module.itms.report.bio
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 *
 */
public class BatchRestartJXBIO
{
	private Logger logger = LoggerFactory.getLogger(BatchRestartJXBIO.class);
	private BatchRestartJXDAO dao;
	
	public List<Map> qryBatchStartTask(String startTime,String endTime,int curPage_splitPage,int num_splitPage){
		return dao.qryBatchStartTask(startTime, endTime, curPage_splitPage, num_splitPage);
	}
	
	public int qryBatchStartTaskCount(String startTime,String endTime){
		return dao.qryBatchStartTaskCount(startTime, endTime);
	}
	
	public List<HashMap<String,String>> qryBatchStartTaskExcel(String startTime,String endTime){
		return dao.qryBatchStartTaskExcel(startTime, endTime);
	}
	
	public List<Map> qryDetail(String startTime,String endTime,String taskId,String type,int curPage_splitPage,int num_splitPage){
		return dao.qryDetail(startTime, endTime, taskId, type, curPage_splitPage, num_splitPage);
	}
	
	public int qryDetailCount(String startTime,String endTime,String taskId,String type){
		return dao.qryDetailCount(startTime, endTime, taskId, type);
	}
	public List<HashMap<String,String>> qryDetailExcel(String startTime,String endTime,String taskId,String type){
		return dao.qryDetailExcel(startTime, endTime, taskId, type);
	}
	public BatchRestartJXDAO getDao()
	{
		return dao;
	}
	public void setDao(BatchRestartJXDAO dao)
	{
		this.dao = dao;
	}

	
     
}
