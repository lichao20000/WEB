
package com.linkage.module.itms.resource.act;

import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.common.WebUtil;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.system.UserRes;
import com.linkage.module.itms.resource.bio.DeviceWhiteListBIO;

import action.splitpage.splitPageAction;

public class DeviceWhiteListACT	 extends splitPageAction implements SessionAware
{

	private static final long serialVersionUID = 23598541555L;
	private static Logger logger = LoggerFactory.getLogger(DeviceWhiteListACT.class);
	
	private String deviceIds = "";
	// Session
	private Map<String, Object> session;
	
	private String gwShare_fileName = "";
	private DeviceWhiteListBIO bio;
	private String ajax = "";
	private long userId;
	private String gw_type = "";
	private String task_desc = "";
	private String task_id;
	private String task_name;
	private String param;
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
	
	private String cityId;
	private String vendor;
	private String device_model;
	private String devicetypeId;
	
	
	public String queryWhiteInfo()
	{
		return "queryWhiteInfo";
	}
	
	public String queryWhiteList() {
		this.deviceList = bio.getWhiteDeviceList(curPage_splitPage,num_splitPage,cityId,vendor,device_model,
				devicetypeId,deviceSerialnumber);
		this.total = bio.getWhiteDeviceListCount(cityId,vendor,device_model,
				devicetypeId,deviceSerialnumber);
		if (total % num_splitPage == 0) {
			maxPage_splitPage = total / num_splitPage;
		} else {
			maxPage_splitPage = total / num_splitPage + 1;
		}
		return "queryWhiteList";
	}
	
	public String addWhiteList()
	{
		UserRes curUser = (UserRes) session.get("curUser");
		long accoid = curUser.getUser().getId();
		long time = new DateTimeUtil().getLongTime();
			
		ajax = bio.addWhiteListTask(time,task_name,task_desc, accoid, deviceIds, param);
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
			ajax = "批量定制任务执行成功，正在后台执行！";
		}
		
		return "ajax";
	}
	
	
	public String delete(){
		logger.debug("GwDeviceQueryACT=>delete()");
		this.ajax = bio.delete(deviceSerialnumber);
		return "ajax";
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


	public DeviceWhiteListBIO getBio() {
		return bio;
	}

	public void setBio(DeviceWhiteListBIO bio) {
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

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	public String getVendor() {
		return vendor;
	}

	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	public String getDevice_model() {
		return device_model;
	}

	public void setDevice_model(String device_model) {
		this.device_model = device_model;
	}

	public String getDevicetypeId() {
		return devicetypeId;
	}

	public void setDevicetypeId(String devicetypeId) {
		this.devicetypeId = devicetypeId;
	}
}
