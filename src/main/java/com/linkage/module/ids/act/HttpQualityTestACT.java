package com.linkage.module.ids.act;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.system.UserRes;
import com.linkage.module.ids.bio.HttpQualityTestBIO;

@SuppressWarnings("serial")
public class HttpQualityTestACT extends splitPageAction implements SessionAware,ServletRequestAware {

	private Logger logger = LoggerFactory.getLogger(HttpQualityTestACT.class);

	private HttpQualityTestBIO bio;
	// 导入的文件名
	private String fileNames;
	// 测试结果文件名
	private String filename;
	private String ajax;
   private	HttpServletRequest request = null;
	private Map session;
	private String taskname;

	private String deviceIds;
	private String taskid;

	private String accoid;

	private String addtime;

	// 开始时间
	private String starttime;
	private String starttime1;
	// 结束时间
	private String endtime;
	private String endtime1;
	private String acc_loginname;
	private List<Map> list;
	private List<Map> deviceList;
	private String url;
	private String gwShare_queryResultType;
	private String checkedStr;
	private int total;
	private List<Map> data; // 需要导出的结果集，必须是处理过的可以直接显示的结果
	private String[] column; // 需要导出的列名，对应data中的键值
	private String[] title; // 显示在导出文档上的列名
	private String fileName; // 导出的文件名（不包括后缀名）

	private String levelreport;

	private Map map;


	private Object gwShare_msg;

	public String countTest() {

		ajax = bio.countTest(fileNames);

		return "ajax";

	}
	
//	public String getDevList(){
//		gwShare_queryResultType="checkbox";
//		deviceList = bio.getDevList(fileNames);
//		total = bio.getDevListCount(fileNames);
//		return "devList0";
//	}
	/**
	 * 查询设备（带列表）
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String queryDeviceList() {
		gwShare_queryResultType="checkbox";
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		long areaId = curUser.getAreaId();
		if(null!=fileNames){
			fileNames.trim();
		}
		this.deviceList = bio.getDeviceList(fileNames);
		if(null==this.deviceList || this.deviceList.size()<1){
			this.gwShare_msg = bio.getMsg();
		}
		total = deviceList == null?0:this.deviceList.size();
		return "shareList0";
	}

	public String addTask() {
		Date date = new Date();
		DateTimeUtil dt = new DateTimeUtil(date);
		addtime = String.valueOf(dt.getLongTime());
		UserRes curUser = (UserRes) session.get("curUser");
		long accoid = curUser.getUser().getId();
		taskid = getTask();
		ajax = bio.addTask(taskname, taskid, accoid, addtime, url, levelreport,
				filename,fileNames, deviceIds);
		return "ajax";

	}
	
	public String checkName(){
		ajax = bio.checkName(filename, taskname);
		return "ajax";
	}

	public String initQueryTask(){
		starttime=getStart2TempTime();
		endtime=getEnd2TempTime();
		return "taskInfo";
	}
	public String queryTask() {
		this.setTime();
		list = bio.queryTask(taskname, acc_loginname, starttime1, endtime1,
				curPage_splitPage, num_splitPage);
		maxPage_splitPage = bio.queryTaskCount(taskname, acc_loginname,
				starttime1, endtime1, curPage_splitPage, num_splitPage);
		return "taskList";
	}
	public String queryTaskInfo(){
		map = bio.queryTaskInfo(taskid);
		return "taskDetail";
	}
	public String getTaskDevList(){
		list = bio.getTaskDevList(taskid, curPage_splitPage, num_splitPage);
		maxPage_splitPage=bio.getTaskCount(taskid, curPage_splitPage, num_splitPage);
		return "devList";
	}
	public String queryTaskExcel(){
		this.setTime();
		column = new String[] { "task_name", "acc_loginname", "add_time","url","file_name","level_report" };
		title = new String[] { "任务名称", "定制人", "定制时间","url","测试结果文件名","报文优先级"};
		fileName = "任务清单";
		data=bio.queryTaskExcel(taskname, acc_loginname, starttime1, endtime1);
		return "excel";
	}
	private String getTask() {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String taskid = sdf.format(date);
		return taskid;
	}
	public String delTask(){
		ajax=bio.delTask(taskid);
		return "ajax";
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
	private void setTime() {
		DateTimeUtil dt = null;// 定义DateTimeUtil


		if (starttime == null || "".equals(starttime)) {
			starttime1 = null;
		} else {
			dt = new DateTimeUtil(starttime);
			starttime1 = String.valueOf(dt.getLongTime());
		}
		if (endtime == null || "".equals(endtime)) {
			endtime1 = null;
		} else {
			dt = new DateTimeUtil(endtime);
			endtime1 = String.valueOf(dt.getLongTime());
		}

	}

	public HttpQualityTestBIO getBio() {
		return bio;
	}

	public void setBio(HttpQualityTestBIO bio) {
		this.bio = bio;
	}


	public String getFileNames() {
		return fileNames;
	}

	public void setFileNames(String fileNames) {
		this.fileNames = fileNames;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
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

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getLevelreport() {
		return levelreport;
	}

	public void setLevelreport(String levelreport) {
		this.levelreport = levelreport;
	}

	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;

	}

	public String getStarttime() {
		return starttime;
	}

	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}

	public String getStarttime1() {
		return starttime1;
	}

	public void setStarttime1(String starttime1) {
		this.starttime1 = starttime1;
	}

	public String getEndtime() {
		return endtime;
	}

	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}

	public String getEndtime1() {
		return endtime1;
	}

	public void setEndtime1(String endtime1) {
		this.endtime1 = endtime1;
	}

	public String getAcc_loginname() {
		return acc_loginname;
	}

	public void setAcc_loginname(String acc_loginname) {
		this.acc_loginname = acc_loginname;
	}

	public List<Map> getList() {
		return list;
	}

	public void setList(List<Map> list) {
		this.list = list;
	}

	public Map getMap() {
		return map;
	}

	public void setMap(Map map) {
		this.map = map;
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

	public List<Map> getDeviceList() {
		return deviceList;
	}

	public void setDeviceList(List<Map> deviceList) {
		this.deviceList = deviceList;
	}

	public String getGwShare_queryResultType() {
		return gwShare_queryResultType;
	}

	public void setGwShare_queryResultType(String gwShare_queryResultType) {
		this.gwShare_queryResultType = gwShare_queryResultType;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public String getCheckedStr() {
		return checkedStr;
	}

	public void setCheckedStr(String checkedStr) {
		this.checkedStr = checkedStr;
	}

	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request=request;
	}

	public String getDeviceIds() {
		return deviceIds;
	}

	public void setDeviceIds(String deviceIds) {
		this.deviceIds = deviceIds;
	}

	public Object getGwShare_msg() {
		return gwShare_msg;
	}

	public void setGwShare_msg(Object gwShare_msg) {
		this.gwShare_msg = gwShare_msg;
	}
 
}
