package com.linkage.module.gwms.resource.bio;


import com.linkage.module.gwms.resource.dao.BatchConfigNodeDAO;

import java.util.List;
import java.util.Map;


public class BatchConfigNodeBIO {
	
	
	private BatchConfigNodeDAO dao;
	
	public BatchConfigNodeDAO getDao() {
		return dao;
	}

	public void setDao(BatchConfigNodeDAO dao) {
		this.dao = dao;
	}

	public void insertDevice(long taskId,long add_time,String[] deviceId_array)
	{
	    dao.insertDevice(taskId,add_time,deviceId_array);
	}
	
	public void insertNode(long taskId,String[] nodeIds)
	{
	    dao.insertNode(taskId,nodeIds);
	}
	
	public void insertTask(long taskId,long accountId,int status)
	{
	    dao.insertTask(taskId,accountId,status);
	}


    public List queryTaskList4XJ() {
		return dao.queryTaskList4XJ();
    }

	public String stopTask(String task_id) {
		return dao.changeTask(task_id,0);
	}

	public String startTask(String task_id) {
		return dao.changeTask(task_id,1);
	}

	public List getBatchDevList(String cityId, String startOpenDate, String endOpenDate) {
		return dao.getBatchDevList(cityId,startOpenDate,endOpenDate);
	}

	public List getBatchWifiDevList(String cityId, String startOpenDate, String endOpenDate) {
		return dao.getBatchWifiDevList(cityId,startOpenDate,endOpenDate);
	}


	public List<Map<String, String>> downloadDevInfo(String cityId, String startOpenDate, String endOpenDate) {
		return dao.downloadDevInfo(cityId,startOpenDate,endOpenDate);
	}

	public Map queryCityIdByAreaId(String area_id) {
		return dao.queryCityIdByAreaId(area_id);
	}
}
