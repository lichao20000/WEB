package com.linkage.module.ids.act;

import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.gwms.util.StringUtil;
import com.linkage.module.ids.bio.IdsQueryBIO;

public class IdsQueryACT extends splitPageAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2389555267415599411L;
	private static Logger logger = LoggerFactory.getLogger(IdsQueryACT.class);
	private String starttime;
	private String endtime;
	private String starttime1;
	private String endtime1;
	private String taskId;
	/*
	 * 新增loid 和 文件名
	 */
	private String loid;
	private String gwShare_fileName;
	// 查询时间类型
	private String quertTimeType = null;
	
	
	
	public String getQuertTimeType()
	{
		return quertTimeType;
	}

	
	public void setQuertTimeType(String quertTimeType)
	{
		this.quertTimeType = quertTimeType;
	}


	private List<Map> data; // 需要导出的结果集，必须是处理过的可以直接显示的结果
	private String[] column; // 需要导出的列名，对应data中的键值
	private String[] title; // 显示在导出文档上的列名
	private String fileName; // 导出的文件名（不包括后缀名）
	
	private List IdsList;
	private List devList;
	
	private IdsQueryBIO bio = null;
	
	public String doQueryStatus(){
		this.setTime();
		String indexName = "ids";
		String indexType = "ponstatus";
		if(!StringUtil.IsEmpty(gwShare_fileName))
		{
			IdsList = bio.getQueryStatusListByFile(indexName,indexType,curPage_splitPage,num_splitPage,starttime,endtime,"1",gwShare_fileName);
			maxPage_splitPage = bio.queryPonstatusCount2(indexName, indexType,
					curPage_splitPage, num_splitPage, starttime, endtime,
					quertTimeType, gwShare_fileName);

		}else {
			IdsList = bio.getQueryStatusListByLoid(indexName,indexType,curPage_splitPage,num_splitPage,starttime,endtime,"1",loid);
			
		}
		return "queryList";
	}
	
	@SuppressWarnings("unchecked")
	public String doQueryExcel(){
		this.setTime();
		String indexName = "ids";
		String indexType = "ponstatus";
		if(!StringUtil.IsEmpty(gwShare_fileName))
		{
			IdsList = bio.getQueryStatusListByFileToExcel(indexName,indexType,curPage_splitPage,num_splitPage,starttime,endtime,"1",gwShare_fileName);
		
		}else {
			IdsList = bio.getQueryStatusListByLoid(indexName,indexType,curPage_splitPage,num_splitPage,starttime,endtime,"1",loid);
		}
		String excelCol = "loid#device_serialnumber#vendor_name#device_model#softwareversion#hardwareversion#upload_time#state";
		String excelTitle = "LOID#设备序列号#厂商#型号#软件版本#硬件版本#上报时间#状态";
		column = excelCol.split("#");
		title = excelTitle.split("#");
		fileName = "上报信息查询结果";
		data = IdsList;
		return "excel";
	}
		
	
	
	
	/**
	 * 时间转化
	 */
	private void setTime(){
		logger.debug("IdsQueryACT---》setTime()" + starttime);
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
	
	
	public IdsQueryBIO getBio()
	{
		return bio;
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

	public void setBio(IdsQueryBIO bio) {
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

	
	public String getLoid()
	{
		return loid;
	}

	
	public void setLoid(String loid)
	{
		this.loid = loid;
	}


	
	public String getGwShare_fileName()
	{
		return gwShare_fileName;
	}


	
	public void setGwShare_fileName(String gwShare_fileName)
	{
		this.gwShare_fileName = gwShare_fileName;
	}

	
	
	
}
