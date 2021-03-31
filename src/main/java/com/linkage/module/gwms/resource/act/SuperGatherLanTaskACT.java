package com.linkage.module.gwms.resource.act;

import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import action.splitpage.splitPageAction;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.resource.bio.SuperGatherLanTaskBIO;
import com.linkage.module.gwms.util.StringUtil;

public class SuperGatherLanTaskACT extends splitPageAction implements
		ServletRequestAware {

	private static final long serialVersionUID = 1L;

	// logger
	private static Logger logger = LoggerFactory
			.getLogger(SuperGatherLanTaskACT.class);

	// request
	private HttpServletRequest request = null;

	// 单台采集时，参数类型和值
	private String queryField;
	private String queryParam;

	// ajax
	private String ajax;

	// deviceId
	private String deviceId;

	// 设备信息
	private Map deviceMap;

	// 高级定制参数
	private String cityId;
	private String vendorId;
	private String deviceModelId;
	private String deviceTypeId;
	private String onlineStatus;

	// 导入定制
	private String fileName;

	// 导出文件列标题
	private String[] title;
	// 导出文件列
	private String[] column;

	private List<Map> data;

	// 查询条件
	private String starttime;
	private String endtime;

	private List<Map> taskMapList;

	private List<Map> taskDetailMapList;

	private long taskId;

	// bio
	private SuperGatherLanTaskBIO bio = new SuperGatherLanTaskBIO();

	/**
	 * 单台采集时，校验是否可以查询到多条数据
	 * 
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public String validateSingleGather() {

		logger.debug("SuperGatherLanTaskACT=>validateSingleGather()");
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		long areaId = curUser.getAreaId();

		List<Map> deviceIdList = bio.getDeviceIdList(areaId, queryParam,
				queryField, curUser.getCityId());
		if (null == deviceIdList || deviceIdList.isEmpty()
				|| null == deviceIdList.get(0)) {
			ajax = "empty";
		} else if (deviceIdList.size() > 1) {
			ajax = "many";
		} else {
			ajax = StringUtil.getStringValue(deviceIdList.get(0).get(
					"device_id"));
		}
		return "ajax";
	}

	/**
	 * 单台采集
	 * 
	 * @return
	 */
	public String singleSuperGather() {
		logger.debug("SuperGatherLanTaskACT=>singleSuperGather()");

		deviceMap = bio.gatherDeviceLan1(deviceId);
		return "single";
	}

	/**
	 * 高级任务定制
	 * 
	 * @return
	 */
	public String createTask() {
		logger.debug("SuperGatherLanTaskACT=>createTask()");

		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");

		ajax = bio.createTask(curUser.getUser().getAccount(), cityId, vendorId,
				deviceModelId, deviceTypeId, onlineStatus);
		return "ajax";
	}

	/**
	 * 导入任务定制
	 * 
	 * @return
	 */
	public String createFileTask() {
		logger.debug("SuperGatherLanTaskACT=>createTask()");

		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");

		ajax = bio.createFileTask(fileName, curUser.getUser().getAccount());
		return "ajax";
	}

	/**
	 * 定制任务列表页面
	 * 
	 * @return
	 */
	public String taskList() {
		logger.debug("SuperGatherLanTaskACT=>taskList()");

		// 时间转换
		setTime();

		taskMapList = bio.getTaskList(starttime, endtime, curPage_splitPage,
				num_splitPage);

		maxPage_splitPage = bio.getTaskMaxPage(starttime, endtime,
				curPage_splitPage, num_splitPage);

		return "list";
	}

	/**
	 * 定制任务详情页面
	 * 
	 * @return
	 */
	public String taskDeatil() {
		logger.debug("SuperGatherLanTaskACT=>taskDeatil()");

		taskDetailMapList = bio.getDetailList(taskId, curPage_splitPage,
				num_splitPage);

		maxPage_splitPage = bio.getDetailMaxPage(taskId, curPage_splitPage,
				num_splitPage);

		return "detail";
	}

	/**
	 * 任务删除
	 * 
	 * @return
	 */
	public String taskDel() {
		logger.debug("SuperGatherLanTaskACT=>taskDel()");

		ajax = StringUtil.getStringValue(bio.delTask(taskId));

		return "ajax";
	}

	/**
	 * 导出任务详情
	 * 
	 * @return
	 */
	public String getExcel() {
		logger.debug("getExcel()");
		fileName = StringUtil.getStringValue(taskId);
		title = new String[] { "属地", "设备序列号", "厂商", "型号", "版本（软件）", "用户账号",
				"连接速率（LAN1）", "是否千兆属性", "采集时间", "采集结果" };
		column = new String[] { "city_name", "device_serialnumber",
				"vendor_add", "device_model", "softwareversion", "username",
				"lan1", "gigabit_port", "update_time", "status" };
		data = bio.getTaskDetail(taskId);
		return "excel";
	}

	/**
	 * 时间转化
	 */
	private void setTime() {
		logger.debug("setTime()" + starttime);
		DateTimeUtil dt = null;// 定义DateTimeUtil

		if (!StringUtil.IsEmpty(starttime)) {
			dt = new DateTimeUtil(starttime);
			starttime = String.valueOf(dt.getLongTime());
		}

		if (!StringUtil.IsEmpty(endtime)) {
			dt = new DateTimeUtil(endtime);
			endtime = String.valueOf(dt.getLongTime());
		}
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public String getAjax() {
		return ajax;
	}

	public void setAjax(String ajax) {
		this.ajax = ajax;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public Map getDeviceMap() {
		return deviceMap;
	}

	public void setDeviceMap(Map deviceMap) {
		this.deviceMap = deviceMap;
	}

	public SuperGatherLanTaskBIO getBio() {
		return bio;
	}

	public void setBio(SuperGatherLanTaskBIO bio) {
		this.bio = bio;
	}

	public String getQueryField() {
		return queryField;
	}

	public void setQueryField(String queryField) {
		this.queryField = queryField;
	}

	public String getQueryParam() {
		return queryParam;
	}

	public void setQueryParam(String queryParam) {
		this.queryParam = queryParam;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	public String getVendorId() {
		return vendorId;
	}

	public void setVendorId(String vendorId) {
		this.vendorId = vendorId;
	}

	public String getDeviceModelId() {
		return deviceModelId;
	}

	public void setDeviceModelId(String deviceModelId) {
		this.deviceModelId = deviceModelId;
	}

	public String getDeviceTypeId() {
		return deviceTypeId;
	}

	public void setDeviceTypeId(String deviceTypeId) {
		this.deviceTypeId = deviceTypeId;
	}

	public String getOnlineStatus() {
		return onlineStatus;
	}

	public void setOnlineStatus(String onlineStatus) {
		this.onlineStatus = onlineStatus;
	}

	public String getStarttime() {
		return starttime;
	}

	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}

	public String getEndtime() {
		return endtime;
	}

	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}

	public List<Map> getTaskMapList() {
		return taskMapList;
	}

	public void setTaskMapList(List<Map> taskMapList) {
		this.taskMapList = taskMapList;
	}

	public long getTaskId() {
		return taskId;
	}

	public void setTaskId(long taskId) {
		this.taskId = taskId;
	}

	public String[] getTitle() {
		return title;
	}

	public void setTitle(String[] title) {
		this.title = title;
	}

	public String[] getColumn() {
		return column;
	}

	public void setColumn(String[] column) {
		this.column = column;
	}

	public List<Map> getData() {
		return data;
	}

	public void setData(List<Map> data) {
		this.data = data;
	}

	public List<Map> getTaskDetailMapList() {
		return taskDetailMapList;
	}

	public void setTaskDetailMapList(List<Map> taskDetailMapList) {
		this.taskDetailMapList = taskDetailMapList;
	}

	@Override
	public void setServletRequest(HttpServletRequest arg0) {
		this.request = arg0;
	}
}
