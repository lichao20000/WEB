package com.linkage.module.ids.act;

import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.ids.bio.IdsStatusQueryBIO;

public class IdsStatusQueryACT extends splitPageAction implements SessionAware {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2389555267415599411L;
	private static Logger logger = LoggerFactory.getLogger(IdsStatusQueryACT.class);
	private Map session;
	private String starttime;
	private String endtime;
	private String starttime1;
	private String endtime1;
	private String taskId;
	
	private List<Map> data; // 需要导出的结果集，必须是处理过的可以直接显示的结果
	private String[] column; // 需要导出的列名，对应data中的键值
	private String[] title; // 显示在导出文档上的列名
	private String fileName; // 导出的文件名（不包括后缀名）
	
	private List IdsList;
	private List devList;
	
	private IdsStatusQueryBIO bio;
	
	public String doQueryStatus(){
		this.setTime();
		IdsList = bio.getQueryStatusList(curPage_splitPage,num_splitPage,starttime1,endtime1);
		maxPage_splitPage=bio.getQueryStatusCount(num_splitPage,starttime1,endtime1);
		return "queryList";
	}
	
	@SuppressWarnings("unchecked")
	public String doQueryStatusExcel(){
		this.setTime();
		IdsList = bio.getQueryStatusListExcel(starttime1,endtime1);
		String excelCol = "acc_loginname#add_time#enable#timelist#serverurl#tftp_port#paralist#devCount";
		String excelTitle = "定制人#定制时间#开启或关闭#上报周期#文件上传域名#端口#监控参数#设备数";
		column = excelCol.split("#");
		title = excelTitle.split("#");
		fileName = "定制信息查询结果";
		data = IdsList;
		return "excel";
	}
		
	public String getDevInfo(){
		devList = bio.getDevInfo(curPage_splitPage,num_splitPage,taskId);
		maxPage_splitPage=bio.getDevInfoCount(num_splitPage,taskId);
		return "devInfo";
	}
	
	@SuppressWarnings("unchecked")
	public String getDevInfoExcel(){
		devList = bio.getDevInfoExcel(taskId);
		String excelCol = "oui#device_serialnumber#result_id#status";
		String excelTitle = "oui#设备序列号#下发结果#状态";
		column = excelCol.split("#");
		title = excelTitle.split("#");
		fileName = "定制信息查询结果详细信息";
		data = devList;
		return "excel";
	}

	/**
	 * 时间转化
	 */
	private void setTime(){
		logger.debug("IdsStatusQueryACT---》setTime()" + starttime);
		DateTimeUtil dt = null;// 定义DateTimeUtil
		if (starttime == null || "".equals(starttime)){
			starttime1 = null;
		}else{
			dt = new DateTimeUtil(starttime);
			starttime1 = String.valueOf(dt.getLongTime());
		}
		if (endtime == null || "".equals(endtime)){
			endtime1 = null;
		}else{
			dt = new DateTimeUtil(endtime);
			endtime1 = String.valueOf(dt.getLongTime());
		}
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

	public String getStarttime1() {
		return starttime1;
	}

	public void setStarttime1(String starttime1) {
		this.starttime1 = starttime1;
	}

	public String getEndtime1() {
		return endtime1;
	}

	public void setEndtime1(String endtime1) {
		this.endtime1 = endtime1;
	}

	public void setBio(IdsStatusQueryBIO bio) {
		this.bio = bio;
	}

	public void setSession(Map<String, Object> arg0) {

	}

	public List getIdsList() {
		return IdsList;
	}

	public void setIdsList(List idsList) {
		IdsList = idsList;
	}

	public List getDevList() {
		return devList;
	}

	public void setDevList(List devList) {
		this.devList = devList;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	
	public List<Map> getData() {
		return data;
	}

	public void setData(List<Map> data) {
		this.data = data;
	}

	public String[] getColumn() {
		return column;
	}

	public void setColumn(String[] column) {
		this.column = column;
	}

	public String[] getTitle() {
		return title;
	}

	public void setTitle(String[] title) {
		this.title = title;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
}
