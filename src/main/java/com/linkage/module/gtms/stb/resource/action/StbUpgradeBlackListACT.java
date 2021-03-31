
package com.linkage.module.gtms.stb.resource.action;

import java.util.List;
import java.util.Map;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.WebUtil;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gtms.stb.resource.serv.StbUpgradeBlackListBIO;
import com.linkage.module.gwms.Global;

import action.splitpage.splitPageAction;

public class StbUpgradeBlackListACT	 extends splitPageAction implements SessionAware
{

	private static final long serialVersionUID = 23598541555L;
	private static Logger logger = LoggerFactory.getLogger(StbUpgradeBlackListACT.class);
	
	private String deviceIds = "";
	// Session
	private Map<String, Object> session;
	
	private String gwShare_fileName = "";
	private StbUpgradeBlackListBIO bio;
	private String ajax = "";
	private long userId;
	private String gw_type = "";
	private String task_desc = "";
	private String task_id;
	private String task_name;
	private String param;
	private String servAccount;
	private String deviceSerialnumber;
	private List deviceList = null;
	private int total;
	private List tasklist;
	// 查询开始时间
	private String startTime;
	// 查询结束时间
	private String endTime;
	//定制人
	private String accName;
	
	private String taskName;
	
	public String init()
	{
		return "init";
	}
	
	
	public String addBlackList()
	{
		UserRes curUser = (UserRes) session.get("curUser");
		long accoid = curUser.getUser().getId();
		long time = new DateTimeUtil().getLongTime();
			
		ajax = bio.addTaskHBLT(time,task_name,task_desc, accoid, deviceIds, param);
		if("true".equals(ajax))
		{
			ajax = "批量定制任务执行成功！";
		} 
		else if ("false".equals(ajax))
		{
			ajax = "批量定制任务执行失败！";
		}
		else if("false10000".equals(ajax))
		{
			ajax = "定制设备超过10000条，定制失败！";
		}
		else if("trueThread".equals(ajax))
		{
			ajax = "批量定制任务执行成功，请稍后在任务详情中查看！";
		}
		
		return "ajax";
	}
	
	
	public String queryBlackInfo()
	{
		return "queryBlackInfo";
	}
	
	
	public String queryBlackDeviceList() {
		
		this.deviceList = bio.getBlackDeviceList(curPage_splitPage,num_splitPage,deviceSerialnumber,servAccount);
		this.total = bio.getBlacDeviceListCount(deviceSerialnumber,servAccount);
		if (total % num_splitPage == 0) {
			maxPage_splitPage = total / num_splitPage;
		} else {
			maxPage_splitPage = total / num_splitPage + 1;
		}
		return "queryBlackList";
	}
	
	public String delete(){
		logger.debug("GwDeviceQueryACT=>delete()");
		this.ajax = bio.delete(deviceSerialnumber);
		return "ajax";
	}
	
	public String queryBlackDeviceTask()
	{
		UserRes curUser = WebUtil.getCurrentUser();
		long acc_oid = curUser.getUser().getId();
		tasklist = bio.getOrderTaskList(curPage_splitPage, num_splitPage,
				 startTime, endTime, taskName,acc_oid,accName);
		total = bio.countOrderTask(curPage_splitPage,
				num_splitPage,startTime, endTime, taskName,acc_oid,accName);
		if (total % num_splitPage == 0) {
			maxPage_splitPage = total / num_splitPage;
		} else {
			maxPage_splitPage = total / num_splitPage + 1;
		}
		return "queryBlackDeviceTask";
	}
	
	public String doDisable(){
	    ajax = bio.doDisable(task_id);
		return "ajax";
	}
	public String doAble(){
		ajax = bio.doAble(task_id);
		return "ajax";
	}
	
	
	@SuppressWarnings("unchecked")
	public String getdetailList()
	{
		deviceList = bio.getdetailList(task_id, curPage_splitPage, num_splitPage);
		total = bio.getdetailListCount(curPage_splitPage, num_splitPage, task_id);
		if (total % num_splitPage == 0) {
			maxPage_splitPage = total / num_splitPage;
		} else {
			maxPage_splitPage = total / num_splitPage + 1;
		}
		return "queryBlackDeviceTaskDetail";
	}
	
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}

	public Map<String, Object> getSession() {
		return session;
	}

	public String getDeviceIds() {
		return deviceIds;
	}

	public void setDeviceIds(String deviceIds) {
		this.deviceIds = deviceIds;
	}

	public String getAjax() {
		return ajax;
	}

	public void setAjax(String ajax) {
		this.ajax = ajax;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getGw_type() {
		return gw_type;
	}

	public void setGw_type(String gw_type) {
		this.gw_type = gw_type;
	}

	public String getGwShare_fileName() {
		return gwShare_fileName;
	}

	public void setGwShare_fileName(String gwShare_fileName) {
		this.gwShare_fileName = gwShare_fileName;
	}

	public String getTask_id() {
		return task_id;
	}

	public void setTask_id(String task_id) {
		this.task_id = task_id;
	}

	public String getTask_name() {
		return task_name;
	}

	public void setTask_name(String task_name) {
		this.task_name = task_name;
	}


	public StbUpgradeBlackListBIO getBio() {
		return bio;
	}

	public void setBio(StbUpgradeBlackListBIO bio) {
		this.bio = bio;
	}

	public String getTask_desc() {
		return task_desc;
	}

	public void setTask_desc(String task_desc) {
		this.task_desc = task_desc;
	}

	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}


	public String getServAccount() {
		return servAccount;
	}

	public void setServAccount(String servAccount) {
		this.servAccount = servAccount;
	}

	public String getDeviceSerialnumber() {
		return deviceSerialnumber;
	}

	public void setDeviceSerialnumber(String deviceSerialnumber) {
		this.deviceSerialnumber = deviceSerialnumber;
	}

	public List getDeviceList() {
		return deviceList;
	}

	public void setDeviceList(List deviceList) {
		this.deviceList = deviceList;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public List getTasklist() {
		return tasklist;
	}

	public void setTasklist(List tasklist) {
		this.tasklist = tasklist;
	}


	public String getStartTime() {
		return startTime;
	}


	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}


	public String getEndTime() {
		return endTime;
	}


	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}


	public String getAccName() {
		return accName;
	}


	public void setAccName(String accName) {
		this.accName = accName;
	}


	public String getTaskName() {
		return taskName;
	}


	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	
}
