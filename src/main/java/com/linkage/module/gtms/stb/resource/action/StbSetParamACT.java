package com.linkage.module.gtms.stb.resource.action;


import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.common.WebUtil;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gtms.stb.resource.serv.StbSetParamBIO;

import action.splitpage.splitPageAction;

@SuppressWarnings("rawtypes")
public class StbSetParamACT extends splitPageAction 
{
	private static final long serialVersionUID = 1234567890L;
	private static Logger logger = LoggerFactory.getLogger(StbSetParamACT.class);
	
	private String device_id;
	private String strategy_id;
	private String service_id;
	private String ajax;
	private List<Map> list;
	
	private StbSetParamBIO bio;
	
	/**
	 * 获取所有策略
	 */
	public String getStrategyResult()
	{
		logger.warn("[{}]-getStrategyResult",device_id);
		list=bio.getStrategyResult(device_id,curPage_splitPage, num_splitPage);
		maxPage_splitPage = bio.getCountStrategyResult(device_id,num_splitPage);

		return "list";
	}
	
	/**
	 * 获取设备信息
	 */
	public String getDeviceInfo()
	{
		ajax=bio.getDeviceInfo(device_id);
		return "ajax";
	}
	/**
	 * 新增策略，重启或恢复出厂设置
	 */
	public String stbAddStrategy()
	{
		logger.warn("[{}]-stbAddStrategy:[{}]",device_id,service_id);
		UserRes curUser = WebUtil.getCurrentUser();
		ajax = bio.add(curUser.getUser().getId(),device_id,service_id);
	
		return "ajax";
	}
	
	/**
	 * 删除策略
	 */
	public String deleteStrategy()
	{
		logger.warn("[{}]-deleteStrategy:[{}]",device_id,strategy_id);
		ajax = bio.delete(strategy_id);
	
		return "ajax";
	}

	
	public String getDevice_id() {
		return device_id;
	}

	public void setDevice_id(String device_id) {
		this.device_id = device_id;
	}

	public String getStrategy_id() {
		return strategy_id;
	}

	public void setStrategy_id(String strategy_id) {
		this.strategy_id = strategy_id;
	}

	public String getAjax() {
		return ajax;
	}

	public void setAjax(String ajax) {
		this.ajax = ajax;
	}

	public String getService_id() {
		return service_id;
	}

	public void setService_id(String service_id) {
		this.service_id = service_id;
	}

	public List<Map> getList() {
		return list;
	}

	public void setList(List<Map> list) {
		this.list = list;
	}

	public StbSetParamBIO getBio() {
		return bio;
	}

	public void setBio(StbSetParamBIO bio) {
		this.bio = bio;
	}
	
}
