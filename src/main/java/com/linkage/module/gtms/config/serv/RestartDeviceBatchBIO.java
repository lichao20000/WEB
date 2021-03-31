package com.linkage.module.gtms.config.serv;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.module.gtms.config.dao.RestartDeviceBatchDAO;
import com.linkage.module.gwms.Global;

public class RestartDeviceBatchBIO {
	
	private RestartDeviceBatchDAO dao;

	private static Logger logger = LoggerFactory.getLogger(RestartDeviceBatchBIO.class);
	// 回传消息
	private String msg = null;
	// 查询条件
	private String importQueryField = "username";

	public static boolean flag = true;
	

	/**
	 * 获取今天总数
	 */
	public long getTodayCount() { 
		return dao.getTodayCount();		
	}
	
	public String addTask4CQ(long taskid, String taskName, long accoid, String deviceIds, String param,
			String restartNum, String restartInterval){
		try {
			logger.warn("RestartDeviceBatchBIO.addTask4CQ()");
			
			List<String> netAccounts = new ArrayList<String>();
			
			if(StringUtil.IsEmpty(deviceIds) || "0".equals(deviceIds)){
				String sqlSpell = null;
				if(!StringUtil.IsEmpty(param)){
					String[] paramArr = param.split("\\|");
					if(null!=paramArr &&  paramArr.length>=11){
						sqlSpell = paramArr[10];
					}
				}
				
				if(StringUtil.IsEmpty(sqlSpell)){
					logger.warn("==[{}]设备为空，查询sql为空，程序结束==", taskid);
					return "false";
				}
				
				if((LipossGlobals.inArea(Global.CQDX) || LipossGlobals.inArea(Global.HBLT)) && sqlSpell.contains("[")){
					sqlSpell = sqlSpell.replaceAll("\\[", "\\'");
				}
				
				ArrayList<HashMap<String, String>> devList = dao.getDevIds4NX(sqlSpell);
				if(null==devList || devList.size()==0){
					logger.warn("==[{}]设备为空，程序结束==", taskid);
					return "false";
				}
				
				int num = dao.addTask4CQ(taskid, taskName, accoid, sqlSpell, 2, restartNum, restartInterval);
				if(num>0){
					return "true";
				}else{
					return "false";
				}
			}
			else
			{
			
				String[] deviceIdsArr = null==deviceIds ? null : deviceIds.split(",");
				if(null==deviceIdsArr || 0==deviceIdsArr.length){
					logger.warn("==[{}]设备为空，程序结束==", taskid);
					return "false";
				}
			
				for(String deviceId : deviceIdsArr){
					if(!StringUtil.IsEmpty(deviceId)){
						netAccounts.add(deviceId);
					}
				}
			}
			
			if(null==netAccounts || netAccounts.size()==0){
				logger.warn("==[{}]设备查询结果为空，程序结束==", taskid);
				return "false";
			}
			// 批量插入设备ID到临时表
			String filenameTmp = taskid + "";
			dao.insertTmp(filenameTmp, netAccounts);
			// 根据设备序列号获取设备信息
			ArrayList<HashMap<String, String>> devList = dao.getTaskDevList4CQ(filenameTmp);
			List<HashMap<String, String>> devListNew = new ArrayList<HashMap<String, String>>();
			
			if(null!=devList && devList.size()>0){
				for(HashMap<String, String> map : devList)
				{
					String aDeviceId = StringUtil.getStringValue(map, "a_device_id", "");
					
					if(StringUtil.IsEmpty(map.get("device_id")))
					{
						logger.warn("taskid[{}]-aDeviceId[{}]没有查到设备", taskid, aDeviceId);
					}
					else if(StringUtil.IsEmpty(map.get("username")))
					{
						logger.warn("taskid[{}]-aDeviceId[{}]没有查到loid", taskid, aDeviceId);
					}
					else{
						devListNew.add(map);
					}
				}
				if(null==devListNew || devListNew.size()==0)
				{
					logger.warn("taskid[{}] devListNew is null", taskid);
					return "false";
				}
			}else{
				logger.warn("taskid[{}] devList is null", taskid);
				return "false";
			}
			
			logger.warn("taskid[{}]-devListNew.size[{}]", taskid, devListNew.size());
			if(devListNew.size()>10000){
				logger.warn("taskid[{}] 定制设备超过10000条，程序结束==", taskid);
				return "false10000";
			}
			// 获取sqlList
			ArrayList<String> sqlList = dao.sqlList4CQ(devListNew, taskid);
			ArrayList<String> sqlListTmp = new ArrayList<String>();
			int count = 0;
			
			// 批量插入设备信息
			if(null!=sqlList && sqlList.size()>0){
				for(String sql : sqlList){
					sqlListTmp.add(sql);
					if(sqlListTmp.size()>=200){
						int res = dao.insertTaskDev4CQ(sqlListTmp);
						if(res>0){
							count += sqlListTmp.size();
						}
						sqlListTmp.clear();
					}
				}
			}
			if(sqlListTmp.size()>0){
				int res = dao.insertTaskDev4CQ(sqlListTmp);
				if(res>0){
					count += sqlListTmp.size();
				}
				sqlListTmp.clear();
			}
			
			logger.warn("==[{}]插入tab_restart_task_dev表[{}]条数据==", taskid, count);
			
			int num = dao.addTask4CQ(taskid, taskName, accoid, "", 1, restartNum, restartInterval);
			
			if(num>0){
				logger.warn("==[{}]入tab_restart_task表成功==", taskid);
				return "true";
			}else{
				logger.warn("==[{}]入tab_restart_task表失败==", taskid);
				return "false";
			}
		} catch (Exception e) {
			logger.warn("批量定制任务执行失败e[{}]", e);
			return "false";
		}
	}
	
	/**
	 * 查询任务列表
	 */
	public List<Map> queryTaskList(String task_name_query, String status_query,
			String expire_time_start, String expire_time_end,int curPage_splitPage,
			int num_splitPage){
		int status = -1;
		if(!StringUtil.IsEmpty(status_query)){
			status = Integer.valueOf(status_query);
		}
		long expireTimeStart;
		long expireTimeEnd;
		if(null==expire_time_start||"".equals(expire_time_start)){
			expireTimeStart = -1;
		}else{
			expireTimeStart = dealTime(expire_time_start);
		}
		if(null==expire_time_end||"".equals(expire_time_end)){
			expireTimeEnd = -1;
		}else{
			expireTimeEnd = dealTime(expire_time_end);
		}
		
		return dao.queryTaskList4CQ(task_name_query, status, expireTimeStart, expireTimeEnd, curPage_splitPage, num_splitPage);
	}
	
	public int queryTaskCount(String task_name_query, String status_query,
			String expire_time_start, String expire_time_end, int curPage_splitPage,
			int num_splitPage) {
		int status = -1;
		long expireTimeStart;
		long expireTimeEnd;
		if(!StringUtil.IsEmpty(status_query)){
			status = Integer.valueOf(status_query);
		}
		if(null==expire_time_start||"".equals(expire_time_start)){
			expireTimeStart = -1;
		}else{
			expireTimeStart = dealTime(expire_time_start);
		}
		if(null==expire_time_end||"".equals(expire_time_end)){
			expireTimeEnd = -1;
		}else{
			expireTimeEnd = dealTime(expire_time_end);
		}
		return dao.queryTaskCount4CQ(task_name_query, status, expireTimeStart, expireTimeEnd, curPage_splitPage, num_splitPage);
	}
	
	public List<Map> queryTaskGyCityList(String task_id,String task_name, int curPage_splitPage, int num_splitPage){
		return dao.queryTaskGyCityList(task_id, task_name, curPage_splitPage, num_splitPage);
	}
	
	public List<Map> queryTaskGyCityList(String task_id,String task_name){
	return dao.queryTaskGyCityList(task_id, task_name);
	}
	
	public int queryTaskGyCityCount(String task_id,String task_name, int curPage_splitPage, int num_splitPage){
		int total =  dao.queryTaskGyCityCount(task_id);
		int maxPage = 1;
		if (total % num_splitPage == 0) {
			maxPage = total / num_splitPage;
		} else {
			maxPage = total / num_splitPage + 1;
		}
		return maxPage;
	}
	
	/**
	 * 根据条件查询设备列表
	 * @param task_id 任务id
	 * @param city_id 属地id
	 * @param type 
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @return
	 */
	public List<Map> queryDevList4CQ(String task_id, String city_id, String type, int curPage_splitPage, int num_splitPage){
		return dao.queryDevList4CQ(task_id, city_id, type, curPage_splitPage, num_splitPage);
	}
	
	/**
	 * 设备列表最大页数
	 * @param countNum
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @return
	 */
	public int queryDevCount4CQ(String countNum, int curPage_splitPage, int num_splitPage)
	{
		int total = StringUtil.getIntegerValue(countNum);
		int maxPage = 1;
		if (total % num_splitPage == 0) {
			maxPage = total / num_splitPage;
		} else {
			maxPage = total / num_splitPage + 1;
		}
		return maxPage;
	}
	
	public String update(String task_id,String type){
		String res = dao.update(task_id,type)+"";
		return res;
	}
	
	public int updateCount(String task_id,String type){
		return dao.updateCount(task_id,type);
	}
	
	public String del(String task_id){
		return(dao.del(task_id)+"");
	}
	
	public int delCount(String task_id){
		return(dao.delCount(task_id));
	}
	
	public long dealTime(String time) {
		SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date str = new Date();
		try {
			str = date.parse(time);
		}
		catch (ParseException e) {
			logger.warn("选择开始或者结束的时间格式不对:" + time);
		}

		return str.getTime() / 1000L;
	}
	
	
	public String removeStr(String str)
	{
		if(!StringUtil.IsEmpty(str))
		{
			if(str.startsWith(","))
			{
				str = str.substring(1,str.length());
			}
			
			if(str.endsWith(","))
			{
				str = str.substring(0,str.length()-1);
			}
		}
		return str;
	}

	public RestartDeviceBatchDAO getDao() {
		return dao;
	}

	public void setDao(RestartDeviceBatchDAO dao) {
		this.dao = dao;
	}

	public static Logger getLogger() {
		return logger;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

}
