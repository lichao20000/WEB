package com.linkage.module.gtms.config.serv;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.util.DateTimeUtil;

import com.linkage.module.gtms.config.dao.OperatSSIDDAO;

/**
 * NXDX-REQ-ITMS-20200817-LX-001(宁夏电信新增批量或单个修改光猫的ITV无线开关页面需求)-8.26修改
 */
@SuppressWarnings("rawtypes")
public class OperatSSIDBIO 
{
	private static Logger logger = LoggerFactory.getLogger(OperatSSIDBIO.class);
	private static String filePath=LipossGlobals.getLipossProperty("operatSSID.filePath");
	private OperatSSIDDAO dao = null;
	
	
	/**
	 * 查询设备
	 */
	public List getDeviceList(String queryParam,String queryField)
	{
		logger.debug("OperatSSIDBIO getDeviceList({},{})",queryField,queryParam);
		return dao.queryDevice(queryParam,queryField);
	}

	/**
	 * 定制任务
	 */
	public String addTask(String queryType,String deviceIds,String fileName,String task_name) 
	{
		int r=dao.addTask(queryType,deviceIds,filePath+fileName,task_name);
		if(r>0){
			return "任务定制成功！";
		}
		return "任务定制失败！";
	}
	
	/**
	 * 查询下发结果信息
	 */
	public List queryList(int curPage_splitPage,int num_splitPage,
			String sn,String loid,String user_name,String task_name,long start_time,long end_time) 
	{
		return dao.queryList(curPage_splitPage,num_splitPage,sn,loid,user_name,task_name,start_time,end_time);
	}
	
	/**
	 * 分页统计
	 */
	public int countList(int num_splitPage, String sn, String loid,
			String user_name,String task_name,long start_time, long end_time) 
	{
		return dao.countList(num_splitPage, sn, loid, user_name,task_name, start_time, end_time);
	}
	
	/**
	 * 总数统计
	 */
	public int getQueryCount() 
	{
		return dao.getQueryCount();
	}
	
	/**
	 * 秒数转成日期
	 */
	public String transDate(long seconds)
	{
		try{
			return new DateTimeUtil(seconds * 1000).getLongDate();
		}catch (Exception e){
			e.printStackTrace();
			return "";
		}
	}
	

	public OperatSSIDDAO getDao() {
		return dao;
	}

	public void setDao(OperatSSIDDAO dao) {
		this.dao = dao;
	}
	
}
