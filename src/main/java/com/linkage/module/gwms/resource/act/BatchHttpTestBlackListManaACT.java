package com.linkage.module.gwms.resource.act;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.commons.util.StringUtil;
import com.linkage.litms.common.WebUtil;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.resource.bio.BatchHttpTestBlackListManaBIO;

/**
 * 
 * @author zszhao6 (Ailk No.78987)
 * @version 1.0
 * @since 2018-8-6
 * @category com.linkage.module.gwms.resource.act
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 *
 */
public class BatchHttpTestBlackListManaACT extends splitPageAction
{

	private static final long serialVersionUID = 1L;
	private static Logger logger = LoggerFactory
			.getLogger(BatchHttpTestManaACT.class);
	/** 任务id */
	private String taskId = "";
	/**  */
	private String upResult = "";
	/** 任务名 */
	private String taskName = "";
	/** 下级属地 */
	private String cityIds = "";
	/** 属地 */
	private String cityId = "";
	private String task_desc = "";
	private String ajax = "";
	private Map taskResultMap = null;
	/** 初始化 */
	private List<Map<String, String>> cityList = null;
	/** 厂商 */
	private List vendorList;
	// 查询开始时间
	private String startTime;
	// 查询结束时间
	private String endTime;
	// 定制人
	private String accName;
	private List tasklist;
	private List taskResultList = null;
	private Map<String, String> taskDetailMap = null;
	
	private BatchHttpTestBlackListManaBIO bio;
	
	/**
	 * 初始化
	 * @author zzs
	 * @date 2018年8月6日 
	 * @return String 初始化查询
	 */
	public String init()
	{
		logger.debug("init");
		UserRes curUser = WebUtil.getCurrentUser();
		long acc_oid = curUser.getUser().getId();
		String cityId = StringUtil.getStringValue(curUser.getCityId());
		tasklist = bio.getOrderTaskList(curPage_splitPage, num_splitPage,
				 startTime, endTime, cityId, taskName,acc_oid,accName);
		maxPage_splitPage = bio.countOrderTask(curPage_splitPage,
				num_splitPage,startTime, endTime, cityId, taskName,acc_oid,accName);
		return "init";
	}

	/**
	 * @author zzs
	 * @date July 18, 2017
	 * @param
	 * @return String
	 */
	public String getTaskCount() {
		taskResultMap = bio.getTaskCount(taskId);
		return "taskResultCount";
	}
	
	/**
	 * 失效
	 * @return
	 */
	public String doDelete(){
		logger.warn("doDelete()");
	    ajax = bio.doDelete(taskId);
		return "ajax";
	}
	
	/**
	 * 提交修改描述
	 * @return
	 */
	public String commitDesc(){
		logger.warn("commitDesc()");
	    ajax = bio.commitDesc(taskId,task_desc);
		return "ajax";
	}
	
	/**
	 * 查看任务详情
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String getTestSpeedTaskResult()
	{
		taskResultList = bio.getTaskResult(taskId, curPage_splitPage, num_splitPage);
		maxPage_splitPage = bio.countDeviceTask(curPage_splitPage, num_splitPage, taskId);
		logger.warn(taskResultList.toString());
		return "taskResult";
	}
	
	/**
	 * @return the taskId
	 */
	public String getTaskId()
	{
		return taskId;
	}

	
	/**
	 * @param taskId the taskId to set
	 */
	public void setTaskId(String taskId)
	{
		this.taskId = taskId;
	}

	
	/**
	 * @return the upResult
	 */
	public String getUpResult()
	{
		return upResult;
	}

	
	/**
	 * @param upResult the upResult to set
	 */
	public void setUpResult(String upResult)
	{
		this.upResult = upResult;
	}

	
	/**
	 * @return the taskName
	 */
	public String getTaskName()
	{
		return taskName;
	}

	
	/**
	 * @param taskName the taskName to set
	 */
	public void setTaskName(String taskName)
	{
		this.taskName = taskName;
	}

	
	/**
	 * @return the cityIds
	 */
	public String getCityIds()
	{
		return cityIds;
	}

	
	/**
	 * @param cityIds the cityIds to set
	 */
	public void setCityIds(String cityIds)
	{
		this.cityIds = cityIds;
	}

	
	/**
	 * @return the cityId
	 */
	public String getCityId()
	{
		return cityId;
	}

	
	/**
	 * @param cityId the cityId to set
	 */
	public void setCityId(String cityId)
	{
		this.cityId = cityId;
	}

	
	/**
	 * @return the task_desc
	 */
	public String getTask_desc()
	{
		return task_desc;
	}

	
	/**
	 * @param task_desc the task_desc to set
	 */
	public void setTask_desc(String task_desc)
	{
		this.task_desc = task_desc;
	}

	
	/**
	 * @return the ajax
	 */
	public String getAjax()
	{
		return ajax;
	}

	
	/**
	 * @param ajax the ajax to set
	 */
	public void setAjax(String ajax)
	{
		this.ajax = ajax;
	}

	
	/**
	 * @return the taskResultMap
	 */
	public Map getTaskResultMap()
	{
		return taskResultMap;
	}

	
	/**
	 * @param taskResultMap the taskResultMap to set
	 */
	public void setTaskResultMap(Map taskResultMap)
	{
		this.taskResultMap = taskResultMap;
	}

	
	/**
	 * @return the cityList
	 */
	public List<Map<String, String>> getCityList()
	{
		return cityList;
	}

	
	/**
	 * @param cityList the cityList to set
	 */
	public void setCityList(List<Map<String, String>> cityList)
	{
		this.cityList = cityList;
	}

	
	/**
	 * @return the vendorList
	 */
	public List getVendorList()
	{
		return vendorList;
	}

	
	/**
	 * @param vendorList the vendorList to set
	 */
	public void setVendorList(List vendorList)
	{
		this.vendorList = vendorList;
	}

	
	/**
	 * @return the startTime
	 */
	public String getStartTime()
	{
		return startTime;
	}

	
	/**
	 * @param startTime the startTime to set
	 */
	public void setStartTime(String startTime)
	{
		this.startTime = startTime;
	}

	
	/**
	 * @return the endTime
	 */
	public String getEndTime()
	{
		return endTime;
	}

	
	/**
	 * @param endTime the endTime to set
	 */
	public void setEndTime(String endTime)
	{
		this.endTime = endTime;
	}

	
	/**
	 * @return the accName
	 */
	public String getAccName()
	{
		return accName;
	}

	
	/**
	 * @param accName the accName to set
	 */
	public void setAccName(String accName)
	{
		this.accName = accName;
	}

	
	/**
	 * @return the tasklist
	 */
	public List getTasklist()
	{
		return tasklist;
	}

	
	/**
	 * @param tasklist the tasklist to set
	 */
	public void setTasklist(List tasklist)
	{
		this.tasklist = tasklist;
	}

	
	/**
	 * @return the taskResultList
	 */
	public List getTaskResultList()
	{
		return taskResultList;
	}

	
	/**
	 * @param taskResultList the taskResultList to set
	 */
	public void setTaskResultList(List taskResultList)
	{
		this.taskResultList = taskResultList;
	}

	
	/**
	 * @return the taskDetailMap
	 */
	public Map<String, String> getTaskDetailMap()
	{
		return taskDetailMap;
	}

	
	/**
	 * @param taskDetailMap the taskDetailMap to set
	 */
	public void setTaskDetailMap(Map<String, String> taskDetailMap)
	{
		this.taskDetailMap = taskDetailMap;
	}

	
	/**
	 * @return the bio
	 */
	public BatchHttpTestBlackListManaBIO getBio()
	{
		return bio;
	}

	
	/**
	 * @param bio the bio to set
	 */
	public void setBio(BatchHttpTestBlackListManaBIO bio)
	{
		this.bio = bio;
	}
	
}
