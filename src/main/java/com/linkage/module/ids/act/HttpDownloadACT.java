package com.linkage.module.ids.act;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.Global;
import com.linkage.module.ids.bio.HTTPDownloadBIO;


public class HttpDownloadACT extends splitPageAction implements SessionAware {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	// 日志记录
	private static Logger logger = LoggerFactory
			.getLogger(HttpDownloadACT.class);
	// 开始时间
	private String starttime;
	private String starttime1;
	
	private String starttime2;
	private String starttime3;
	// 结束时间
	private String endtime;
	private String endtime1;
	
	private String endtime2;
	private String endtime3;
	// 截止日期
	private String enddate;
	private String enddate1;
	private String ajax;
	//任务名称
	private String taskname;
	//任务ID
	private String taskid;
	//文件民称
	private String filename;
	//定制人
	private String accoid;
	//定制时间
	private String addtime;
	private String url;
	private List<Map> list;
	private Map map;
	private String acc_loginname;
	private String serialnumber;
	private List<Map> data; // 需要导出的结果集，必须是处理过的可以直接显示的结果
	private String[] column; // 需要导出的列名，对应data中的键值
	private String[] title; // 显示在导出文档上的列名
	private String fileName; // 导出的文件名（不包括后缀名）
	String nowDate = "";
	private String deviceIds;
	private String gwShare_queryType;
	private String param;
	
	/**
	 * 上网类型： 宽带、tr069 
	 */
	private String httpType;
	
	/**
	 * 测速账号
	 */
	private String testUserName;
	
	/**
	 * 测速账号密码
	 */
	private String testPWD;
	
	//session
	private Map session;
	private HTTPDownloadBIO bio;

	@Override
	public String execute() throws Exception {
		starttime=getStartTempTime();
		endtime=getEndTempTime();
		enddate=getEndTempDate();
		if(LipossGlobals.inArea(Global.NXDX)){
			return "success4NX";
		}
		if(LipossGlobals.inArea(Global.JXDX)){
			return "success4JX";
		}
		return "success";
	}
	
	public String addTaskInfo(){
		this.setTime();
		UserRes curUser = (UserRes) session.get("curUser");
		long accoid = curUser.getUser().getId();
		taskid = getTask();

		if(LipossGlobals.inArea(Global.JXDX)){
			//JXDX-REQ-ITMS-20200618-WWF-001 ITMS+家庭网关批量测速集群改造
			ajax = bio.addTask4JX(taskname, taskid, accoid, nowDate, starttime1, endtime1, enddate1, filename);
			return "ajax";
		}
		ajax = bio.addTask(taskname, taskid, accoid, nowDate, starttime1, endtime1, enddate1, filename,url,testUserName,testPWD);

		return "ajax";
	}
	
	public String addTaskInfo4NX(){
		this.setTime();
		UserRes curUser = (UserRes) session.get("curUser");
		long accoid = curUser.getUser().getId();
		taskid = getTask();
		
		ajax = bio.addTask4NX(taskname, taskid, accoid, nowDate, starttime1, endtime1, enddate1, deviceIds, param, url,testUserName,testPWD);
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
		
		return "ajax";
	}
	
	public String initQueryTask(){
		starttime2=getStart2TempTime();
		endtime2=getEnd2TempTime();
		return "taskInfo";
	}
	
	
	public String queryTask(){
		this.setTime();
		list = bio.queryTask(taskname, acc_loginname, starttime3,endtime3, curPage_splitPage, num_splitPage);
		maxPage_splitPage=bio.queryTaskCount(taskname, acc_loginname, starttime3, endtime3, curPage_splitPage, num_splitPage);
		return "taskList";
	}
	
	public String queryTaskExcel(){
		this.setTime();
		column = new String[] { "task_name", "acc_loginname", "add_time" };
		title = new String[] { "任务名称", "定制人", "定制时间"};
		fileName = "任务列表";
		data=bio.queryTaskExcel(taskname, acc_loginname, starttime3, endtime3);
		return "excel";
	}
	
	public String delTask(){
		ajax=bio.delTask(taskid);
		return "ajax";
	}
	
	public String queryTaskInfo(){
		map = bio.queryTaskInfo(taskid);
		return "taskDetail";
	}
	
	public String initQueryDevPert(){
		starttime2=getStart2TempTime();
		endtime2=getEnd2TempTime();
		return "httppert";
	}
	
	
	public String getTaskDevList(){
		list = bio.getTaskDevList(taskid, curPage_splitPage, num_splitPage);
		maxPage_splitPage=bio.getTaskCount(taskid, curPage_splitPage, num_splitPage);
		return "devList";
	}
	
	
	public String getTaskDevResToExcel(){
		this.setTime();
		if(LipossGlobals.inArea(Global.NXDX)){
			column = new String[] { "task_name","city_name", "vendor_name", "device_model", "device_serialnumber","bomtime", "eomtime","total_bytes_rece","downPert" };
			title = new String[] { "任务名称","属  地", "设备厂商", "型  号", "设备序列号", "采集开始时间", "采集结束时间","下载字节数","测速下载速率" };
		}else{
			column = new String[] { "task_name","city_name", "vendor_name", "device_model", "device_serialnumber","bomtime", "eomtime","total_bytes_rece","downPert","totalDownPert" };
			title = new String[] { "任务名称","属  地", "设备厂商", "型  号", "设备序列号", "采集开始时间", "采集结束时间","下载字节数","测速下载速率","总体速率" };
		}
		fileName = "HTTP测速结果";
		data= bio.getTaskDevResToExcel(taskid);
		return "excel";
	}
	
	public String queryDevBySerialnumber(){
		this.setTime();
		list = bio.queryDevBySerialnumber(taskname,serialnumber,starttime3,endtime3, curPage_splitPage, num_splitPage);
		maxPage_splitPage=bio.queryDevBySerialnumberCount(taskname,serialnumber,starttime3,endtime3, curPage_splitPage, num_splitPage);
		return "httppertlist";
		
	}
	
	public String toExcel(){
		this.setTime();
		if(LipossGlobals.inArea(Global.NXDX)){
			column = new String[] { "task_name","city_name", "vendor_name", "device_model", "device_serialnumber","bomtime", "eomtime","total_bytes_rece","downPert" };
			title = new String[] { "任务名称","属  地", "设备厂商", "型  号", "设备序列号", "采集开始时间", "采集结束时间","下载字节数","测速下载速率" };
		}else{
			column = new String[] { "task_name","city_name", "vendor_name", "device_model", "device_serialnumber","bomtime", "eomtime","total_bytes_rece","downPert","totalDownPert" };
			title = new String[] { "任务名称","属  地", "设备厂商", "型  号", "设备序列号", "采集开始时间", "采集结束时间","下载字节数","测速下载速率","总体速率" };
		}
		fileName = "HTTP下载速率";
		data= bio.queryDevBySerialnumberExcel(taskname,serialnumber, starttime3, endtime3);
		return "excel";
	}
	
	public String getExcelRows(){
		ajax=bio.getExcelRow(filename);
		return "ajax";
	}

	public String initSingleHttp(){

		return "singleHttp";
	}

	public String querySingleHttpList(){
		this.setTime();
		//缺sql
		list = bio.querySingleHttpList(httpType,taskid, taskname, starttime3,endtime3, curPage_splitPage, num_splitPage);
		maxPage_splitPage=bio.querySingleHttpListCount(httpType,taskid, taskname, starttime3,endtime3, curPage_splitPage, num_splitPage);
		return "singleHttpList";
	}


	public String querySingleHttpExcel(){
		this.setTime();
		column = new String[] { "city_name", "loid", "desn", "vendor_name", "device_model", "hardwareversion", "softwareversion", "maxsampledtotalvalues", "isreach", "test_time", "downlink", "httptype" };
		title = new String[] { "属地", "LOID", "串码", "厂家", "型号", "硬件版本", "软件版本", "测速速率", "是否达标", "测速时间", "签约速率", "测速来源"};
		fileName = "单用户测速结果列表";
		data=bio.querySingleHttpExcel(httpType,taskid, taskname, starttime3,endtime3);
		return "excel";
	}




	private String getStartTempTime() {
		GregorianCalendar now = new GregorianCalendar();
		SimpleDateFormat fmtrq = new SimpleDateFormat("HH:mm", Locale.US);
		now.set(Calendar.HOUR_OF_DAY, 0);
		now.set(Calendar.MINUTE, 0);
		String time = fmtrq.format(now.getTime());
		return time;
	}

	private String getEndTempTime() {
		GregorianCalendar now = new GregorianCalendar();
		SimpleDateFormat fmtrq = new SimpleDateFormat("HH:mm", Locale.US);
		now.set(Calendar.HOUR_OF_DAY, 6);
		now.set(Calendar.MINUTE, 0);
		String time = fmtrq.format(now.getTime());
		return time;
	}

	// 当前时间的23:59:59,如 2011-05-11 23:59:59
	private String getEndTempDate() {
		GregorianCalendar now = new GregorianCalendar();
		SimpleDateFormat fmtrq = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
				Locale.US);
		now.set(Calendar.HOUR_OF_DAY, 23);
		now.set(Calendar.MINUTE, 59);
		now.set(Calendar.SECOND, 59);
		String time = fmtrq.format(now.getTime());
		return time;
	}
	private String getStart2TempTime() {
		GregorianCalendar now = new GregorianCalendar();
		SimpleDateFormat fmtrq = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
				Locale.US);
		now.set(GregorianCalendar.DATE, 1);
		now.set(GregorianCalendar.MONTH, 0);
		now.set(Calendar.HOUR_OF_DAY, 0);
		now.set(Calendar.MINUTE, 0);
		now.set(Calendar.SECOND, 0);
		String time = fmtrq.format(now.getTime());
		return time;
	}
	private String getEnd2TempTime() {
		GregorianCalendar now = new GregorianCalendar();
		SimpleDateFormat fmtrq = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
				Locale.US);
		now.set(Calendar.HOUR_OF_DAY, 23);
		now.set(Calendar.MINUTE, 59);
		now.set(Calendar.SECOND, 59);
		String time = fmtrq.format(now.getTime());
		return time;
	}


	/**
	 * 时间转化
	 */
	private void setTime()
	{
		DateTimeUtil dt = null;// 定义DateTimeUtil
		
		if(starttime!=null){
			String[] str = starttime.split(":");
			long hour = Long.parseLong(str[0]);
			long min = Long.parseLong(str[1]);
			starttime1 = String.valueOf((hour*3600+min*60));
		}
		
		if(endtime!=null){
			String[] str = endtime.split(":");
			long hour = Long.parseLong(str[0]);
			long min = Long.parseLong(str[1]);
			endtime1 = String.valueOf((hour*3600+min*60));
		}
		
		
		
		if (starttime2 == null || "".equals(starttime2)) {
			starttime3 = null;
		} else {
			dt = new DateTimeUtil(starttime2);
			starttime3 = String.valueOf(dt.getLongTime());
		}
		if (endtime2 == null || "".equals(endtime2)) {
			endtime3 = null;
		} else {
			dt = new DateTimeUtil(endtime2);
			endtime3 = String.valueOf(dt.getLongTime());
		}
		
		
		
		
		if (enddate == null || "".equals(enddate))
		{
			enddate1 = null;
		}
		else
		{
			dt = new DateTimeUtil(enddate);
			enddate1 = String.valueOf(dt.getLongTime());
		}
			Date date = new Date();
			dt = new DateTimeUtil(date);
			nowDate = String.valueOf(dt.getLongTime());
	}
	
	private String getTask(){
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String taskid = sdf.format(date);
		return taskid;
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

	public String getEnddate() {
		return enddate;
	}

	public void setEnddate(String enddate) {
		this.enddate = enddate;
	}

	public String getAjax() {
		return ajax;
	}

	public void setAjax(String ajax) {
		this.ajax = ajax;
	}

	public String getTaskname() {
		return taskname;
	}

	public void setTaskname(String taskname) {
		this.taskname = taskname;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public HTTPDownloadBIO getBio() {
		return bio;
	}

	public void setBio(HTTPDownloadBIO bio) {
		this.bio = bio;
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

	public String getEnddate1() {
		return enddate1;
	}

	public void setEnddate1(String enddate1) {
		this.enddate1 = enddate1;
	}

	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
		
	}

	public String getNowDate() {
		return nowDate;
	}

	public void setNowDate(String nowDate) {
		this.nowDate = nowDate;
	}


	public List<Map> getList() {
		return list;
	}

	public void setList(List<Map> list) {
		this.list = list;
	}

	public String getStarttime2() {
		return starttime2;
	}

	public void setStarttime2(String starttime2) {
		this.starttime2 = starttime2;
	}

	public String getStarttime3() {
		return starttime3;
	}

	public void setStarttime3(String starttime3) {
		this.starttime3 = starttime3;
	}

	public String getEndtime2() {
		return endtime2;
	}

	public void setEndtime2(String endtime2) {
		this.endtime2 = endtime2;
	}

	public String getEndtime3() {
		return endtime3;
	}

	public void setEndtime3(String endtime3) {
		this.endtime3 = endtime3;
	}

	public String getAccoid() {
		return accoid;
	}

	public void setAccoid(String accoid) {
		this.accoid = accoid;
	}

	public String getAddtime() {
		return addtime;
	}

	public void setAddtime(String addtime) {
		this.addtime = addtime;
	}

	public String getAcc_loginname() {
		return acc_loginname;
	}

	public void setAcc_loginname(String acc_loginname) {
		this.acc_loginname = acc_loginname;
	}

	public Map getMap() {
		return map;
	}

	public void setMap(Map map) {
		this.map = map;
	}

	public String getSerialnumber() {
		return serialnumber;
	}

	public void setSerialnumber(String serialnumber) {
		if(null != serialnumber)
		{
			this.serialnumber = serialnumber.trim();
		}
		else
		{
			this.serialnumber = "";
		}
		
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

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	
	public String getTestUserName()
	{
		return testUserName;
	}

	
	public void setTestUserName(String testUserName)
	{
		this.testUserName = testUserName;
	}

	
	public String getTestPWD()
	{
		return testPWD;
	}

	
	public void setTestPWD(String testPWD)
	{
		this.testPWD = testPWD;
	}

	public String getHttpType() {
		return httpType;
	}

	public void setHttpType(String httpType) {
		this.httpType = httpType;
	}

	public String getDeviceIds() {
		return deviceIds;
	}

	public void setDeviceIds(String deviceIds) {
		this.deviceIds = deviceIds;
	}

	public String getGwShare_queryType() {
		return gwShare_queryType;
	}

	public void setGwShare_queryType(String gwShare_queryType) {
		this.gwShare_queryType = gwShare_queryType;
	}

	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}

	

	
}
