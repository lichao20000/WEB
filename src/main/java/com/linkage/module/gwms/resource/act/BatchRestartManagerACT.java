package com.linkage.module.gwms.resource.act;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.gwms.resource.bio.BatchRestartManagerBIO;
public class BatchRestartManagerACT extends splitPageAction implements SessionAware{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5970585537653736789L;
	private static Logger logger = LoggerFactory.getLogger(BatchRestartManagerACT.class);
	private BatchRestartManagerBIO bio;
	
	private Map<String, Object> session;
	//查询条件
	private String taskName;
	private String taskDesc;
	private String addTime;
	private String finalTime;
	private String startTime;
	private String endTime;
	private int status;
	private String subDevSn;
	//选择条件
	private String type ;
	private String ajax;
	private String taskId;
	private String operType;//1:删除 ,2:暂停
	//結果
	private List<Map> taskList = null;
	private List<Map> taskDetailList = null;
	
	//导出
	/** 导出数据 */
	private List<HashMap<String,String>> data;
	
	/** 导出文件列标题 */
	private String[] title;
	/** 导出文件列 */
	private String[] column;
	/** 导出文件名 */
	private String fileName;
	
	public String init(){
		return "init";
	}
	
	//查询
	public String qryList(){
		logger.warn("start:{}.end:{},add:{},final:{}",new Object[]{startTime,endTime,addTime,finalTime});
		setTime();
		taskList = bio.qryTaskList(taskName, taskDesc, addTime, finalTime, startTime, endTime, status, subDevSn, curPage_splitPage, num_splitPage);
		int total = bio.qryTaskcountList(taskName, taskDesc, addTime, finalTime, startTime, endTime, status, subDevSn);
		if (total % num_splitPage == 0) {
			maxPage_splitPage = total / num_splitPage;
		} else {
			maxPage_splitPage = total / num_splitPage + 1;
		}
		return "list";
	}
	
	//查询导出
	public String qryExcel(){
		logger.warn("start:{}.end:{},add:{},final:{}",new Object[]{startTime,endTime,addTime,finalTime});
		setTime();
		fileName = "长时间在线光猫重启记录";
		title = new String[] { "任务号", "任务描述", "执行状态", "定制时间","执行时间","执行数量","执行成功数量","执行失败数量" };
		column = new String[] { "task_id", "task_desc", "task_status", "add_time","start_time","total","succ","fail"};
		data = bio.qryTaskExcelList(taskName, taskDesc, addTime, finalTime, startTime, endTime, status, subDevSn);
		return "excel";
	}
	
	//查询详情
	public String qryDetail(){
		logger.warn("qryDetail => {},{}",new Object[]{taskId,type});
		taskDetailList = bio.qryTaskDetail(taskId, type, subDevSn, curPage_splitPage, num_splitPage);
		int total = bio.qryTaskDetailCount(taskId, type, subDevSn);
		if (total % num_splitPage == 0) {
			maxPage_splitPage = total / num_splitPage;
		} else {
			maxPage_splitPage = total / num_splitPage + 1;
		}
		return "detail";
	}
	
	//详情页导出
	public String qryDetailExcel(){
		fileName = "长时间在线光猫重启详细记录";
		title = new String[] { "厂商", "型号", "软件版本", "属地","设备序列号","LOID","设备版本类型","设备更新时间","重启结果","失败原因" };
		column = new String[] { "vendor_name", "device_model", "softwareversion", "city_name","device_serialnumber","username","device_version_type","time","restart_res","faile_reason"};
		data = bio.qryTaskDetailExcel(taskId, operType, subDevSn);
		return "excel";
	}
	//删除任务
	public String operTask(){
		ajax = bio.operTask(taskId, operType);
		return "ajax";
	}
	
	private void setTime(){
		DateTimeUtil dt = null;// 定义DateTimeUtil
		if (startTime == null || "".equals(startTime)){
			startTime = null;
		}else{
			dt = new DateTimeUtil(startTime);
			startTime = String.valueOf(dt.getLongTime());
		}
		if (endTime == null || "".equals(endTime)){
			endTime = null;
		}else{
			dt = new DateTimeUtil(endTime);
			endTime = String.valueOf(dt.getLongTime());
		}
		if (addTime == null || "".equals(addTime)){
			addTime = null;
		}else{
			dt = new DateTimeUtil(addTime);
			addTime = String.valueOf(dt.getLongTime());
		}
		if (finalTime == null || "".equals(finalTime)){
			finalTime = null;
		}else{
			dt = new DateTimeUtil(finalTime);
			finalTime = String.valueOf(dt.getLongTime());
		}
	}
	 
	public String getStartTime()
	{
		return startTime;
	}

	
	public void setStartTime(String startTime)
	{
		this.startTime = startTime;
	}

	
	public String getEndTime()
	{
		return endTime;
	}

	
	public void setEndTime(String endTime)
	{
		this.endTime = endTime;
	}
	public BatchRestartManagerBIO getBio()
	{
		return bio;
	}
	
	public void setBio(BatchRestartManagerBIO bio)
	{
		this.bio = bio;
	}
	
	public String getTaskName()
	{
		return taskName;
	}
	
	public void setTaskName(String taskName)
	{
		this.taskName = taskName;
	}
	
	public String getTaskDesc()
	{
		return taskDesc;
	}
	
	public void setTaskDesc(String taskDesc)
	{
		this.taskDesc = taskDesc;
	}
	
	public String getAddTime()
	{
		return addTime;
	}
	
	public void setAddTime(String addTime)
	{
		this.addTime = addTime;
	}
	
	public String getFinalTime()
	{
		return finalTime;
	}
	
	public void setFinalTime(String finalTime)
	{
		this.finalTime = finalTime;
	}
	
	public String getFileName()
	{
		return fileName;
	}
	
	public void setFileName(String fileName)
	{
		this.fileName = fileName;
	}
	
	public int getStatus()
	{
		return status;
	}
	
	public void setStatus(int status)
	{
		this.status = status;
	}
	
	public String getSubDevSn()
	{
		return subDevSn;
	}
	
	public void setSubDevSn(String subDevSn)
	{
		this.subDevSn = subDevSn;
	}
	
	public String getType()
	{
		return type;
	}
	
	public void setType(String type)
	{
		this.type = type;
	}
	
	public String getAjax()
	{
		return ajax;
	}
	
	public void setAjax(String ajax)
	{
		this.ajax = ajax;
	}

	public List<Map> getTaskList()
	{
		return taskList;
	}

	public void setTaskList(List<Map> taskList)
	{
		this.taskList = taskList;
	}

	public String getTaskId()
	{
		return taskId;
	}

	public void setTaskId(String taskId)
	{
		this.taskId = taskId;
	}

	public String getOperType()
	{
		return operType;
	}

	public void setOperType(String operType)
	{
		this.operType = operType;
	}

	public List<HashMap<String, String>> getData()
	{
		return data;
	}

	
	public void setData(List<HashMap<String, String>> data)
	{
		this.data = data;
	}

	
	public String[] getTitle()
	{
		return title;
	}

	
	public void setTitle(String[] title)
	{
		this.title = title;
	}

	
	public String[] getColumn()
	{
		return column;
	}

	
	public void setColumn(String[] column)
	{
		this.column = column;
	}

	public List<Map> getTaskDetailList()
	{
		return taskDetailList;
	}

	public void setTaskDetailList(List<Map> taskDetailList)
	{
		this.taskDetailList = taskDetailList;
	}
	
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}

	public Map<String, Object> getSession() {
		return session;
	}

}
