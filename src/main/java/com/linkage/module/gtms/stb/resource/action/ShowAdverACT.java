
package com.linkage.module.gtms.stb.resource.action;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.module.gtms.stb.resource.serv.ShowAdverBIO;

/**
 * @author yinlei3 (73167)
 * @version v4.0.0.23_20150528
 * @since 2015年5月31日
 * @category com.linkage.module.gtms.stb.resource.action
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class ShowAdverACT extends splitPageAction
{

	/** 序列化 */
	private static final long serialVersionUID = 1L;
	/** 日志 */
	private static Logger logger = LoggerFactory.getLogger(ShowAdverACT.class);
	/** 厂商集合 */
	@SuppressWarnings("rawtypes")
	private List vendorList;
	/** 厂商ID */
	private String vendorId = "";
	/** 型号 */
	private String device_model = "";
	/** 版本 */
	private String device_version = "";
	/** 设备序列号 */
	private String deviceSerialnumber = "";
	/** 最近一次上报IP */
	private String loopbackIp = "";
	/** 设备MAC地址 */
	private String cpeMac = "";
	/** 业务账号 */
	private String servAccount = "";
	/** 下发状态 */
	private String faultType = "";
	/** 节点路径 */
	private String ajax = "";
	/** 下发结果集合 */
	@SuppressWarnings("rawtypes")
	private List tasklist;
	/** 详情界面用Map */
	private Map<String, String> taskDetailMap = null;
	/** 执行策略名称 */
	private String taskName = "";
	/** 任务id */
	private String taskId = "";
	/** ip集合 */
	@SuppressWarnings("rawtypes")
	private List ipList = null;
	/** mac集合 */
	@SuppressWarnings("rawtypes")
	private List macList = null;
	/** 下发结果查询用service */
	private ShowAdverBIO bio;

	/**
	 * 页面初始化
	 * 
	 * @author
	 * @return
	 */
	public String initShowAdver()
	{
		logger.debug("initShowAdver");
		// 获取厂商集合
		vendorList = bio.getVendorList();
		return "initShowAdver";
	}

	/**
	 * 根据vendorId查询型号
	 * 
	 * @return
	 */
	public String getDeviceModel()
	{
		ajax = bio.getDeviceModel(vendorId);
		return "ajax";
	}

	/**
	 * 根据型号查询年版本
	 * 
	 * @return
	 */
	public String getSoftVersion()
	{
		ajax = bio.getSoftVersion(device_model);
		return "ajax";
	}

	/**
	 * 开机广告下发结果查询（带列表）
	 * 
	 * @return
	 */
	public String adverResultList()
	{
		logger.debug("adverResultList");
		tasklist = bio.getAdverResultList(curPage_splitPage, num_splitPage, vendorId,
				device_model, device_version, loopbackIp, deviceSerialnumber, cpeMac,
				servAccount, faultType, taskName);
		logger.warn("-------ACT------tasklist-------");
		logger.warn(tasklist.toString());
		logger.warn("-------ACT------tasklist-------");
		maxPage_splitPage = bio.countAdverResultList(num_splitPage, vendorId,
				device_model, device_version, loopbackIp, deviceSerialnumber, cpeMac,
				servAccount, faultType, taskName);
		return "adverResultList";
	}

	/**
	 * 点击执行策略名称，弹出任务详情界面
	 * 
	 * @return
	 */
	public String getShowAdverDetail()
	{
		logger.debug("getShowAdverDetail");
		taskDetailMap = bio.getTaskDetail(taskId);
		ipList = bio.getIpList(taskId);
		macList = bio.getMacList(taskId);
		return "taskDetail";
	}

	@SuppressWarnings("rawtypes")
	public List getVendorList()
	{
		return vendorList;
	}

	@SuppressWarnings("rawtypes")
	public void setVendorList(List vendorList)
	{
		this.vendorList = vendorList;
	}

	public String getVendorId()
	{
		return vendorId;
	}

	public void setVendorId(String vendorId)
	{
		this.vendorId = vendorId;
	}

	public String getDevice_model()
	{
		return device_model;
	}

	public void setDevice_model(String device_model)
	{
		this.device_model = device_model;
	}

	public String getDevice_version()
	{
		return device_version;
	}

	public void setDevice_version(String device_version)
	{
		this.device_version = device_version;
	}

	public String getDeviceSerialnumber()
	{
		return deviceSerialnumber;
	}

	public void setDeviceSerialnumber(String deviceSerialnumber)
	{
		this.deviceSerialnumber = deviceSerialnumber;
	}

	public String getLoopbackIp()
	{
		return loopbackIp;
	}

	public void setLoopbackIp(String loopbackIp)
	{
		this.loopbackIp = loopbackIp;
	}

	public String getCpeMac()
	{
		return cpeMac;
	}

	public void setCpeMac(String cpeMac)
	{
		this.cpeMac = cpeMac;
	}

	public String getServAccount()
	{
		return servAccount;
	}

	public void setServAccount(String servAccount)
	{
		this.servAccount = servAccount;
	}

	public String getFaultType()
	{
		return faultType;
	}

	public void setFaultType(String faultType)
	{
		this.faultType = faultType;
	}

	public String getAjax()
	{
		return ajax;
	}

	public void setAjax(String ajax)
	{
		this.ajax = ajax;
	}

	@SuppressWarnings("rawtypes")
	public List getTasklist()
	{
		return tasklist;
	}

	@SuppressWarnings("rawtypes")
	public void setTasklist(List tasklist)
	{
		this.tasklist = tasklist;
	}

	public Map<String, String> getTaskDetailMap()
	{
		return taskDetailMap;
	}

	public void setTaskDetailMap(Map<String, String> taskDetailMap)
	{
		this.taskDetailMap = taskDetailMap;
	}

	public String getTaskName()
	{
		return taskName;
	}

	public void setTaskName(String taskName)
	{
		this.taskName = taskName;
	}

	public String getTaskId()
	{
		return taskId;
	}

	public void setTaskId(String taskId)
	{
		this.taskId = taskId;
	}

	@SuppressWarnings("rawtypes")
	public List getIpList()
	{
		return ipList;
	}

	@SuppressWarnings("rawtypes")
	public void setIpList(List ipList)
	{
		this.ipList = ipList;
	}

	@SuppressWarnings("rawtypes")
	public List getMacList()
	{
		return macList;
	}

	@SuppressWarnings("rawtypes")
	public void setMacList(List macList)
	{
		this.macList = macList;
	}

	public void setBio(ShowAdverBIO bio)
	{
		this.bio = bio;
	}
}
