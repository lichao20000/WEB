
package com.linkage.module.gwms.resource.act;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.WebUtil;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.resource.bio.BatchHttpTestManaBIO;

@SuppressWarnings("rawtypes")
public class BatchHttpTestManaACT extends splitPageAction 
{
	private static final long serialVersionUID = 1L;
	private static Logger logger = LoggerFactory.getLogger(BatchHttpTestManaACT.class);
	/** 任务id */
	private String taskId = "";
	private String upResult = "";
	/** 任务名 */
	private String taskName = "";
	/** 下级属地 */
	private String cityIds = "";
	/** 属地 */
	private String cityId = "";
	private String task_desc = "";
	private String type = "";
	private Map taskResultMap = null;
	
	/** 初始化 */
	private List<Map<String, String>> cityList = null;
	/** 厂商 */
	private List vendorList;
	// 导出数据
	private List<Map> data; // 需要导出的结果集，必须是处理过的可以直接显示的结果
	private String[] column; // 需要导出的列名，对应data中的键值
	private String[] title; // 显示在导出文档上的列名
	private String fileName; // 导出的文件名（不包括后缀名）
	
	// 查询开始时间
	private String startTime;
	// 查询结束时间
	private String endTime;
	//定制人
	private String accName;
	
	private List tasklist;
	private List taskResultList = null;
	private Map<String, String> taskDetailMap = null;
	private String ajax = "";
	private BatchHttpTestManaBIO bio;
	
	/**
	 * 初始化
	 * @author fanjm
	 * @date 2017年7月4日 
	 * @return String 初始化查询
	 */
	public String init()
	{
		logger.debug("init");
		UserRes curUser = WebUtil.getCurrentUser();
		long acc_oid = curUser.getUser().getId();
		String cityId = StringUtil.getStringValue(curUser.getCityId());
		tasklist = bio.getOrderTaskList(curPage_splitPage, num_splitPage,
				 startTime, endTime, cityId, taskName,acc_oid,accName,type);
		maxPage_splitPage = bio.countOrderTask(curPage_splitPage,
				num_splitPage,startTime, endTime, cityId, taskName,acc_oid,accName,type);
		return "init";
	}
	
	/**
	 * @author fanjm
	 * @date July 18, 2017
	 * @param
	 * @return String
	 */
	public String getTaskCount() {
		taskResultMap = bio.getTaskCount(taskId);
		return "taskResultCount";
	}
	
	/**
	 * 查看结果
	 */
	public String getTestSpeedTaskResult()
	{
		taskResultList = bio.getTaskResult(taskId, upResult, curPage_splitPage, num_splitPage);
		maxPage_splitPage = bio.countDeviceTask(curPage_splitPage, num_splitPage, taskId, upResult);
		
		if("3".equals(upResult) && LipossGlobals.inArea(Global.JLLT)){
			return "taskDoSuccResult";
		}
		return "taskResult";
	}
	
	/**
	 * 导出excel文件
	 */
	@SuppressWarnings("unchecked")
	public String getTaskExcel()
	{
		if(LipossGlobals.inArea(Global.JLLT))
		{
			if("0".equals(upResult)){
				fileName = "测速指令未下发设备列表";
			}else if("1".equals(upResult)){
				fileName = "测速指令下发成功设备列表";
			}else if("2".equals(upResult)){
				fileName = "测速指令下发失败设备列表";
			}else if("3".equals(upResult)){
				fileName="测速成功设备列表";
			}else if("all".equals(upResult)){
				fileName = "全部测速设备列表";
			}
			
			if("3".equals(upResult)){
				column = new String[]{"cityName","vendorName","deviceModel","deviceTypeName",
						"deviceSerialNumber","pppoe_name","rate","speed_dev","average_speed",
						"is_sure","start_time","end_time"};
				title = new String[]{"属 地","厂商","型号","软件版本","设备序列号","宽带账号","签约带宽",
						"测速终端","平均下载速率","是否达标","开始时间","结束时间"};
			}else{
				column = new String[]{"cityName","vendorName","deviceModel","deviceTypeName",
						"deviceSerialNumber","pppoe_name","rate","result","update_time"};
				title = new String[]{"属 地","厂商","型号","软件版本","设备序列号","宽带账号","签约带宽","配置结果","操作时间"};
			}
		}
		else
		{
			if("0".equals(upResult)){
				fileName = "测速结果未做设备列表";
			}else if("1".equals(upResult)){
				fileName = "测速结果成功设备列表";
			}else if("2".equals(upResult)){
				fileName = "测速结果失败设备列表";
			}else if("all".equals(upResult)){
				fileName = "全部测速设备列表";
			}
			
			column = new String[]{"cityName","vendorName","deviceModel","deviceTypeName",
					"deviceSerialNumber","pppoe_name","result","update_time"};
			title = new String[]{"属 地","厂商","型号","软件版本","设备序列号","宽带账号","配置结果","操作时间"};
		}
		
		data = bio.getTaskResult(taskId, upResult, 1, 1000000);
		return "excel";
	}
	
	/**
	 * 失效
	 */
	public String doDisable(){
		logger.warn("doDisable()");
	    ajax = bio.doDisable(taskId);
		return "ajax";
	}
	
	/**
	 * 提交修改描述
	 */
	public String commitDesc(){
		logger.warn("commitDesc()");
	    ajax = bio.commitDesc(taskId,task_desc);
		return "ajax";
	}
	
	/**
	 * 删除
	 */
	public String doDelete(){
		logger.warn("doDelete()");
	    ajax = bio.doDelete(taskId);
		return "ajax";
	}
	

	public String getTaskName(){
		return taskName;
	}

	public void setTaskName(String taskName){
		this.taskName = taskName;
	}

	public String getCityId(){
		return cityId;
	}

	public void setCityId(String cityId){
		this.cityId = cityId;
	}

	public List getVendorList(){
		return vendorList;
	}

	public void setVendorList(List vendorList){
		this.vendorList = vendorList;
	}

	public void setBio(BatchHttpTestManaBIO bio){
		this.bio = bio;
	}

	public String getCityIds() {
		return cityIds;
	}

	public void setCityIds(String cityIds) {
		this.cityIds = cityIds;
	}

	public List<Map<String, String>> getCityList(){
		return cityList;
	}

	public void setCityList(List<Map<String, String>> cityList){
		this.cityList = cityList;
	}

	public String getAjax(){
		return ajax;
	}

	public void setAjax(String ajax){
		this.ajax = ajax;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
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

	public List getData() {
		return data;
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
	
	public List getTasklist() {
		return tasklist;
	}

	public void setTasklist(List tasklist) {
		this.tasklist = tasklist;
	}

	public List getTaskResultList() {
		return taskResultList;
	}

	public void setTaskResultList(List taskResultList) {
		this.taskResultList = taskResultList;
	}

	public Map<String, String> getTaskDetailMap() {
		return taskDetailMap;
	}

	public void setTaskDetailMap(Map<String, String> taskDetailMap) {
		this.taskDetailMap = taskDetailMap;
	}
	
	public String getUpResult(){
		return upResult;
	}
	
	public void setUpResult(String upResult){
		this.upResult = upResult;
	}

	public Map getTaskResultMap(){
		return taskResultMap;
	}
	
	public void setTaskResultMap(Map taskResultMap){
		this.taskResultMap = taskResultMap;
	}
	
	public String getFileName(){
		return fileName;
	}
	
	public void setFileName(String fileName){
		this.fileName = fileName;
	}
	
	public void setData(List<Map> data){
		this.data = data;
	}
	
	public String getTask_desc(){
		return task_desc;
	}
	
	public void setTask_desc(String task_desc){
		this.task_desc = task_desc;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	
}
