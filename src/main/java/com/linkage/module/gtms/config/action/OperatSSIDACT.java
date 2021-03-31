package com.linkage.module.gtms.config.action;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.commons.util.StringUtil;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.uss.DateUtil;
import com.linkage.module.gtms.config.serv.OperatSSIDBIO;


/**
 * NXDX-REQ-ITMS-20200817-LX-001(宁夏电信新增批量或单个修改光猫的ITV无线开关页面需求)-8.26修改
 */
@SuppressWarnings({"rawtypes"})
public class OperatSSIDACT extends splitPageAction implements ServletRequestAware 
{
	private static final long serialVersionUID = 1L;
	private static Logger logger = LoggerFactory.getLogger(OperatSSIDACT.class);
	private OperatSSIDBIO bio = null;
	private String ajax = null;
	private List deviceList = null;

	private String gwShare_queryResultType=null;
	private String gwShare_queryType = null;
	private String gwShare_queryField=null;
	private String gwShare_queryParam = null;
	private String gwShare_fileName = null;

	private String deviceIds=null;
	private String task_name=null;
	private String gwShare_msg = null;
	//回传消息
	private String msg = null;

	//查询结果的合计条数
	private int total = 0;
	
	/** 设备序列号 */
	public String sn;
	/** LOID */
	public String loid;
	/**宽带账号*/
	public String user_name;
	/** 开始时间 */
	public String startTime;
	/** 结束时间 */
	public String endTime;

	/** 工单集 */
	public List data;

	/** 查询总数 */
	public int queryCount;
	
	//导出excel
	private String fileName=null;
	private String[] title=null;
	private  String[] column=null;

	
	/**
	 * 查询设备（带列表）
	 */
	public String queryDeviceList() 
	{
		logger.warn("OperatSSIDACT getDeviceList({},{})",
						gwShare_queryParam,gwShare_queryField);
		deviceList = bio.getDeviceList(gwShare_queryParam,gwShare_queryField);
		if(null==deviceList || deviceList.size()<1){
			this.gwShare_msg = "账号不存在或账号未绑定设备！";
		}
		total = deviceList==null?0:deviceList.size();
		return "list";
	}
	
	public String addTask()
	{
		logger.warn("OperatSSIDACT addTask({},{},{},{})",
					gwShare_queryType,deviceIds,gwShare_fileName,task_name);
		ajax="定制任务失败，无设备或未上传文件！";
		if(("1".equals(gwShare_queryType) && !StringUtil.IsEmpty(deviceIds)) 
				|| ("3".equals(gwShare_queryType) && !StringUtil.IsEmpty(gwShare_fileName)))
		{
			ajax=bio.addTask(gwShare_queryType,deviceIds,gwShare_fileName,task_name);
			return "ajax";
		}
			
		logger.warn("OperatSSIDACT addTask result:"+ajax);
		return "ajax";
	}
	
	/**
	 * 统计页面初始日期
	 */
	public String init()
	{
		startTime=setTime(0);
		endTime=setTime(1);
		return "init";
	}
	
	/**
	 * 统计
	 */
	public String queryList()
	{
		if(!StringUtil.IsEmpty(task_name))
		{
			try {
				//解密
				task_name=URLDecoder.decode(task_name,"utf8");
			} catch (UnsupportedEncodingException e) {
				logger.error("OperatSSIDACT queryList,task_name[{}]解密失败，err:{}",task_name,e);
				e.printStackTrace();
			}
		}
		logger.warn("OperatSSIDACT queryList({},{},{},{},{},{},{},{})",
						new Object[]{curPage_splitPage,num_splitPage,
					sn,loid,user_name,task_name,startTime,endTime});
		long start_time = setTime(startTime);
		long end_time = setTime(endTime);
		
		data = bio.queryList(curPage_splitPage,num_splitPage,
				sn,loid,user_name,task_name,start_time,end_time);
		maxPage_splitPage = bio.countList(num_splitPage,sn,loid,user_name,task_name,start_time,end_time);
		queryCount = bio.getQueryCount();
		
		return "result_list";
	}
	
	/**
	 * 导出excel
	 */
	public String toExcel()
	{
		if(!StringUtil.IsEmpty(task_name))
		{
			try {
				//解密
				task_name=URLDecoder.decode(task_name,"utf8");
			} catch (UnsupportedEncodingException e) {
				logger.error("OperatSSIDACT toExcel,task_name[{}]转码失败，err:{}",task_name,e);
				e.printStackTrace();
			}
		}
		logger.warn("OperatSSIDACT toExcel({},{},{},{},{},{})",sn,loid,user_name,task_name,startTime,endTime);
		fileName="光猫iTV无线关闭结果";
		title=new String[]{"任务名称","属地","厂商","型号","设备序列号","时间","状态"};
		column=new String[]{"task_name","city_name","vendor_name","device_model","sn","operat_time","result_desc"};
		long start_time = setTime(startTime);
		long end_time = setTime(endTime);
		
		data = bio.queryList(-1,-1,sn,loid,user_name,task_name,start_time,end_time);
		return "excel";
	}
	
	/**
	 * 获取当前时间或本月的初始时间
	 */
	private String setTime(int i)
	{
		if(i==0){
			return DateUtil.firstDayOfCurrentMonth("yyyy-MM-dd HH:mm:ss");
		}else{
			return DateUtil.lastTimeOfCurrentDay("yyyy-MM-dd HH:mm:ss");
		}
	}
	
	/**
	 * 时间转化
	 */
	private long setTime(String time)
	{
		if (!StringUtil.IsEmpty(time)){
			return new DateTimeUtil(time).getLongTime();
		}
		return 0;
	}
	
	
	public String getAjax() {
		return ajax;
	}

	public void setAjax(String ajax) {
		this.ajax = ajax;
	}

	public OperatSSIDBIO getBio() {
		return bio;
	}

	public void setBio(OperatSSIDBIO bio) {
		this.bio = bio;
	}

	public void setDeviceList(List deviceList) {
		this.deviceList = deviceList;
	}

	public List getDeviceList() {
		return deviceList;
	}

	public String getGwShare_queryParam() {
		return gwShare_queryParam;
	}

	public void setGwShare_queryParam(String gwShare_queryParam) {
		this.gwShare_queryParam = gwShare_queryParam;
	}

	public String getGwShare_queryType() {
		return gwShare_queryType;
	}

	public void setGwShare_queryType(String gwShare_queryType) {
		this.gwShare_queryType = gwShare_queryType;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public String getDeviceIds() {
		return deviceIds;
	}

	public void setDeviceIds(String deviceIds) {
		this.deviceIds = deviceIds;
	}

	public String getTask_name() {
		return task_name;
	}

	public void setTask_name(String task_name) {
		this.task_name = task_name;
	}

	public String getGwShare_queryField() {
		return gwShare_queryField;
	}

	public void setGwShare_queryField(String gwShare_queryField) {
		this.gwShare_queryField = gwShare_queryField;
	}

	public String getGwShare_fileName() {
		return gwShare_fileName;
	}

	public void setGwShare_fileName(String gwShare_fileName) {
		this.gwShare_fileName = gwShare_fileName;
	}

	public String getGwShare_msg() {
		return gwShare_msg;
	}

	public void setGwShare_msg(String gwShare_msg) {
		this.gwShare_msg = gwShare_msg;
	}
	
	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	public String getGwShare_queryResultType() {
		return gwShare_queryResultType;
	}

	public void setGwShare_queryResultType(String gwShare_queryResultType) {
		this.gwShare_queryResultType = gwShare_queryResultType;
	}

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public String getLoid() {
		return loid;
	}

	public void setLoid(String loid) {
		this.loid = loid;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
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

	public List getData() {
		return data;
	}

	public void setData(List data) {
		this.data = data;
	}

	public int getQueryCount() {
		return queryCount;
	}

	public void setQueryCount(int queryCount) {
		this.queryCount = queryCount;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
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

	@Override
	public void setServletRequest(HttpServletRequest arg0) {}
}
