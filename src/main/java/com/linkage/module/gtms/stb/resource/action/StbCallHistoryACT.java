package com.linkage.module.gtms.stb.resource.action;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.commons.util.StringUtil;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.gtms.stb.resource.serv.StbCallHistoryBIO;
import com.linkage.litms.uss.DateUtil;

/**
 * 机顶盒apk上报信息查询
 */
public class StbCallHistoryACT extends splitPageAction 
{
	private static final long serialVersionUID = 1L;
	private static Logger logger = LoggerFactory.getLogger(StbCallHistoryACT.class);
	
	/**请求IP地址*/
	private String login_ip;
	/**设备mac地址 */
	private String mac;
	/**请求业务账号*/
	private String request_username;
	/**返回业务账号*/
	private String result_username;
	/** 查询总数 */
	private int queryCount;
	/** 开始时间 */
	private String startTime;
	/** 结束时间 */
	private String endTime;
	
	private List<Map<String,String>> data;
	private String ajax;
	private StbCallHistoryBIO bio;
	
	private static final String TABLEPREX="stb_device_login_info_";
	private String table;
	
	private String login_time;
	
	
	public String init()
	{
		logger.debug("init");
		startTime = setTime(0);
		endTime = setTime(1);
		
		return "init";
	}
	
	/**
	 * 机顶盒上报记录列表查询
	 */
	public String query()
	{
		table=TABLEPREX+startTime.substring(0,7).replaceAll("-","");
		
		startTime = setTime(startTime);
		endTime = setTime(endTime);
		
		data = bio.query(curPage_splitPage,num_splitPage,login_ip,
						mac,request_username,result_username,startTime,endTime,table);
		maxPage_splitPage = bio.count(curPage_splitPage, num_splitPage,login_ip,
										mac,request_username,result_username,startTime,endTime,table);
		queryCount = bio.getQueryCount();

		return "list";
	}
	
	/**
	 * 查看详细
	 */
	public String detailDevice()
	{
		logger.warn("detailDevice({},{},{})",mac,login_time,table);
		data=bio.detailDevice(mac,login_time,table);

		return "detail";
	}
	
	/**
	 * 获取当前时间或本月的初始时间
	 */
	private String setTime(int i)
	{
		if(i==0){
			return DateUtil.firstDayOfCurrentMonth("yyyy-MM-dd HH:mm:ss");
		}else{
			Calendar now = Calendar.getInstance();
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(now.getTime());
		}
	}
	
	/**
	 * 时间转化
	 */
	private String setTime(String time)
	{
		String res="";
		if (!StringUtil.IsEmpty(time))
		{
			DateTimeUtil dt = new DateTimeUtil(time);
			res = StringUtil.getStringValue(dt.getLongTime());
		}
		
		return res;
	}
	
	
	public String getAjax(){
		return ajax;
	}

	public void setAjax(String ajax){
		this.ajax = ajax;
	}

	public int getQueryCount() {
		return queryCount;
	}

	public void setQueryCount(int queryCount) {
		this.queryCount = queryCount;
	}

	public String getLogin_ip() {
		return login_ip;
	}

	public void setLogin_ip(String login_ip) {
		this.login_ip = login_ip;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public String getRequest_username() {
		return request_username;
	}

	public void setRequest_username(String request_username) {
		this.request_username = request_username;
	}

	public String getResult_username() {
		return result_username;
	}

	public void setResult_username(String result_username) {
		this.result_username = result_username;
	}

	public StbCallHistoryBIO getBio() {
		return bio;
	}

	public void setBio(StbCallHistoryBIO bio) {
		this.bio = bio;
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

	public List<Map<String,String>> getData() {
		return data;
	}

	public void setData(List<Map<String,String>> data) {
		this.data = data;
	}

	public String getLogin_time() {
		return login_time;
	}

	public void setLogin_time(String login_time) {
		this.login_time = login_time;
	}

	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}

	public static String getTableprex() {
		return TABLEPREX;
	}
	
}
