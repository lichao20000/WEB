package com.linkage.module.itms.resource.act;


import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.resource.obj.BatchSoftUpBean;
import com.linkage.module.itms.resource.bio.BatchSetTempManaBIO;


/**
 * 新批量参数设置统计后台
 * @author HP (AILK No.fanjm)
 * @version 1.0
 * @since 2020-3-19
 * @category com.linkage.module.gtms.config.action
 * @copyright AILK NBS-Network Mgt. RD Dept.
 */
public class BatchSetTempManaACT extends splitPageAction implements SessionAware
{

	//日志记录
	private static Logger logger = LoggerFactory.getLogger(BatchSetTempManaACT.class);

	private String excuteResult;
	private BatchSoftUpBean bsuBean;
	private BatchSetTempManaBIO bio;
	private Map session;
	private String task_name_query;
	private String status_query;
	private String expire_time_start;
	private String expire_time_end;
	private String upStatus;
	private String upTaskName;
	private List<Map> taskList;
	private List<Map> taskByCityList;
	private String ajax;
	private String starttime;
	private String endtime;
	private String task_id;
	private String task_name;
	private String city_id;
	private String type;
	private String countNum;
	private String gw_type;
	
	private List<Map> data; // 需要导出的结果集，必须是处理过的可以直接显示的结果
	private String[] column; // 需要导出的列名，对应data中的键值
	private String[] title; // 显示在导出文档上的列名
	private String fileName; // 导出的文件名（不包括后缀名）
	private Map taskResultMap = null;
	
	public String execute()
	{
		DateTimeUtil dt = new DateTimeUtil();
		this.endtime = dt.getDate();
		this.starttime = dt.getFirtDayOfMonth();
		dt = new DateTimeUtil(this.endtime);
		long end_time = dt.getLongTime();
		dt = new DateTimeUtil((end_time + 86400L - 1L) * 1000L);
		this.endtime = dt.getLongDate();
		dt = new DateTimeUtil(this.starttime);
		this.starttime = dt.getLongDate();
		return "init";
	}
	
	public String queryList() {

		taskList = bio.queryTaskList(task_name_query, status_query, expire_time_start, expire_time_end,
				curPage_splitPage, num_splitPage, gw_type);
		maxPage_splitPage = bio.queryTaskCount(task_name_query, status_query, expire_time_start, expire_time_end,
				curPage_splitPage, num_splitPage, gw_type);

		return "list";
	}
	
	/**
	 * 查询各状态设备详情
	 * @return
	 */
	public String queryDevList() {
		taskList = bio.queryDevList(task_id, type, curPage_splitPage, num_splitPage);
		maxPage_splitPage = bio.queryDevCount(countNum, curPage_splitPage, num_splitPage);

		return "devList";
	}
	
	
	
	public String queryTaskDetailById() 
	{
		taskResultMap = bio.queryTaskById(task_id);
		return "taskDetail";
	}
	
	
	/**
	 * 导出设备列表
	 * @return
	 */
	public String queryDevListExcel() {
		logger.debug("queryDevListExcel()");
		taskList = bio.queryDevList(task_id, type, 0, 0);
		String excelCol = null;
		String excelTitle = null;
		excelTitle = "oui#设备序列号#IP#厂商#型号#版本#属地#结果";
		excelCol = "oui#device_serialnumber#loopback_ip#vendor_id#device_model_id#version#city_name#status";
		
		column = excelCol.split("#");
		title = excelTitle.split("#");
		fileName = "devList";
		data = taskList;
		logger.warn("....."+data.toString());
		return "excel";
	}
	
	
	public String update() {
		ajax = bio.update(task_id,type);
		return "ajax";
	}
	
	public String updateCount() {
		int num = bio.updateCount(task_id,type);
		ajax = "ok";
		if(num>400000){
		//if(num>2){
			ajax = "tooMuch";
		}
		
		return "ajax";
	}
	
	
	public String del() {
		ajax = bio.del(task_id);
		return "ajax";
	}
	
	public String delCount() {
		int num = bio.delCount(task_id);
		ajax = "ok";
		if(num>400000){
			ajax = "tooMuch";
		}
		
		return "ajax";
	}
	
	public String updateStatus(){
		ajax = bio.updateStatus(upStatus, upTaskName);
		return "ajax";
	}

	public String batchSoftwareUp(){
		UserRes curUser = (UserRes)this.session.get("curUser");
		long acc_oid = curUser.getUser().getId();
		bsuBean.setAcc_oid(String.valueOf(acc_oid));
		excuteResult = bio.batchSoftwareUp(bsuBean);
		return "batchSoftUpResponse";
	}




	public BatchSoftUpBean getBsuBean()
	{
		return bsuBean;
	}

	public void setBsuBean(BatchSoftUpBean bsuBean)
	{
		this.bsuBean = bsuBean;
	}

	public String getExcuteResult()
	{
		return excuteResult;
	}


	public void setExcuteResult(String excuteResult)
	{
		this.excuteResult = excuteResult;
	}


	public BatchSetTempManaBIO getBio()
	{
		return bio;
	}


	public void setBio(BatchSetTempManaBIO bio)
	{
		this.bio = bio;
	}





	public Map getSession()
	{
		return session;
	}





	public void setSession(Map session)
	{
		this.session = session;
	}





	public String getTask_name_query()
	{
		return task_name_query;
	}





	public void setTask_name_query(String task_name_query)
	{
		this.task_name_query = task_name_query;
	}






	public String getStatus_query()
	{
		return status_query;
	}

	public void setStatus_query(String status_query)
	{
		this.status_query = status_query;
	}
	public List<Map> getTaskList()
	{
		return taskList;
	}

	public void setTaskList(List<Map> taskList)
	{
		this.taskList = taskList;
	}
	public String getExpire_time_start()
	{
		return expire_time_start;
	}





	public void setExpire_time_start(String expire_time_start)
	{
		this.expire_time_start = expire_time_start;
	}





	public String getExpire_time_end()
	{
		return expire_time_end;
	}





	public void setExpire_time_end(String expire_time_end)
	{
		this.expire_time_end = expire_time_end;
	}






	public String getUpStatus()
	{
		return upStatus;
	}

	public void setUpStatus(String upStatus)
	{
		this.upStatus = upStatus;
	}
	public String getUpTaskName()
	{
		return upTaskName;
	}


	public void setUpTaskName(String upTaskName)
	{
		this.upTaskName = upTaskName;
	}

	public String getAjax()
	{
		return ajax;
	}

	public void setAjax(String ajax)
	{
		this.ajax = ajax;
	}


	public String getStarttime()
	{
		return starttime;
	}


	public void setStarttime(String starttime)
	{
		this.starttime = starttime;
	}


	public String getEndtime()
	{
		return endtime;
	}


	public void setEndtime(String endtime)
	{
		this.endtime = endtime;
	}

	
	public String getTask_id()
	{
		return task_id;
	}

	
	public void setTask_id(String task_id)
	{
		this.task_id = task_id;
	}

	
	public String getTask_name()
	{
		return task_name;
	}

	
	public void setTask_name(String task_name)
	{
		this.task_name = task_name;
	}

	
	public List<Map> getTaskByCityList()
	{
		return taskByCityList;
	}

	
	public void setTaskByCityList(List<Map> taskByCityList)
	{
		this.taskByCityList = taskByCityList;
	}

	
	public String getType()
	{
		return type;
	}

	
	public void setType(String type)
	{
		this.type = type;
	}

	
	public List<Map> getData()
	{
		return data;
	}

	
	public void setData(List<Map> data)
	{
		this.data = data;
	}

	
	public String[] getColumn()
	{
		return column;
	}

	
	public void setColumn(String[] column)
	{
		this.column = column;
	}

	
	public String[] getTitle()
	{
		return title;
	}

	
	public void setTitle(String[] title)
	{
		this.title = title;
	}

	
	public String getFileName()
	{
		return fileName;
	}

	
	public void setFileName(String fileName)
	{
		this.fileName = fileName;
	}

	
	public String getCountNum()
	{
		return countNum;
	}

	
	public void setCountNum(String countNum)
	{
		this.countNum = countNum;
	}

	
	public String getCity_id()
	{
		return city_id;
	}

	
	public void setCity_id(String city_id)
	{
		this.city_id = city_id;
	}

	
	public String getGw_type()
	{
		return gw_type;
	}

	
	public void setGw_type(String gw_type)
	{
		this.gw_type = gw_type;
	}

	
	public Map getTaskResultMap()
	{
		return taskResultMap;
	}

	
	public void setTaskResultMap(Map taskResultMap)
	{
		this.taskResultMap = taskResultMap;
	}

}
