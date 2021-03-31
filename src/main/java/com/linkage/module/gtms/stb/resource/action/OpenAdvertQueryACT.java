package com.linkage.module.gtms.stb.resource.action;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.commons.util.StringUtil;
import com.linkage.litms.common.WebUtil;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gtms.stb.resource.serv.OpenAdvertQueryBIO;

/**
 * 开机广告结果统计
 * @author os_hanzz
 *
 */
@SuppressWarnings("serial")
public class OpenAdvertQueryACT extends splitPageAction
{
	//日志操作
	public Logger logger = LoggerFactory.getLogger(OpenAdvertQueryACT.class);
	
	private OpenAdvertQueryBIO bio;

	private String taskId;

	private String vendorId;

	private String deviceModelId;

	private String taskName;

	private List<Map> list;

	private String queryType;

	private List vendorList;
	
	private List modelList;

	private String ajax;
	
	/**
	 * 查询统计结果
	 * @return
	 */
	public String queryAdvertResultCount()
	{
		list = bio.queryAdvertResultCount(taskId, taskName, vendorId, deviceModelId);
		return "result";
	}
	/**
	 * 查询列表
	 * @return
	 */
	public String queryTotalList()
	{
		list = bio.queryTotalList(taskId, taskName, vendorId, deviceModelId, queryType, curPage_splitPage, num_splitPage);
		maxPage_splitPage = bio.queryTotalListCount(taskId, taskName, vendorId, deviceModelId, queryType, curPage_splitPage, num_splitPage);
		return "list";
	}
	/**
	 * 查询设备厂商
	 * 
	 * @return
	 */
	public String getVendor() {
		logger.debug("GwDeviceQueryACT=>getVendor()");
		this.ajax = bio.getVendor();
		return "ajax";
	}
	/**
	 * 查询设备型号
	 * 
	 * @param vendorId
	 * @return
	 */
	public String getDeviceModel()
	{
		logger.debug("getDeviceModel()");
		ajax = bio.getDeviceModel(vendorId);
		return "ajax";
	}
	
	public OpenAdvertQueryBIO getBio()
	{
		return bio;
	}
	
	public void setBio(OpenAdvertQueryBIO bio)
	{
		this.bio = bio;
	}
	
	public String getTaskId()
	{
		return taskId;
	}
	
	public void setTaskId(String taskId)
	{
		this.taskId = taskId;
	}
	
	public String getVendorId()
	{
		return vendorId;
	}
	
	public void setVendorId(String vendorId)
	{
		this.vendorId = vendorId;
	}
	
	public String getDeviceModelId()
	{
		return deviceModelId;
	}
	
	public void setDeviceModelId(String deviceModelId)
	{
		this.deviceModelId = deviceModelId;
	}
	
	public String getTaskName()
	{
		return taskName;
	}
	
	public void setTaskName(String taskName)
	{
		this.taskName = taskName;
	}
	
	public List<Map> getList()
	{
		return list;
	}
	
	public void setList(List<Map> list)
	{
		this.list = list;
	}
	
	public String getQueryType()
	{
		return queryType;
	}
	
	public void setQueryType(String queryType)
	{
		this.queryType = queryType;
	}
	
	public List getVendorList()
	{
		return vendorList;
	}
	
	public void setVendorList(List vendorList)
	{
		this.vendorList = vendorList;
	}
	
	public String getAjax()
	{
		return ajax;
	}
	
	public void setAjax(String ajax)
	{
		this.ajax = ajax;
	}
	
	public List getModelList()
	{
		return modelList;
	}
	
	public void setModelList(List modelList)
	{
		this.modelList = modelList;
	}
	
	
}
