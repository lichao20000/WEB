package com.linkage.module.ids.bio;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.ids.dao.IdsStatusQueryDAO;

public class IdsStatusQueryBIO {
	private static Logger logger = LoggerFactory.getLogger(IdsStatusQueryBIO.class);

	private IdsStatusQueryDAO dao;
	
	public void setDao(IdsStatusQueryDAO dao) {
		this.dao = dao;
	}

	public List getQueryStatusList(int curPage_splitPage, int num_splitPage,
			String starttime, String endtime) {
		logger.debug("getQueryStatusList()");
		return dao.getQueryStatusList(curPage_splitPage,num_splitPage,starttime, endtime);
	}

	public int getQueryStatusCount(int num_splitPage, String starttime,
			String endtime) {
		logger.debug("getQueryStatusCount()");
		return dao.getQueryStatusCount(num_splitPage,starttime, endtime);
	}

	public List getDevInfo(int curPage_splitPage, int num_splitPage,
			String taskId) {
		
		return dao.getDevInfo(curPage_splitPage,num_splitPage,taskId);
	}

	public int getDevInfoCount(int num_splitPage, String taskId) {
		// TODO Auto-generated method stub
		return dao.getDevInfoCount(num_splitPage,taskId);
	}

	@SuppressWarnings("rawtypes")
	public List getQueryStatusListExcel(String starttime, String endtime) {
		return dao.getQueryStatusListExcel(starttime, endtime);
	}

	public List getDevInfoExcel(String taskId) {
		return dao.getDevInfoExcel(taskId);
	}
	
	
}
