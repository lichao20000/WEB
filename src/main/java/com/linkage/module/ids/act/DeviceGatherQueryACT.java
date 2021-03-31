package com.linkage.module.ids.act;


import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.ids.bio.DeviceGatherQueryBIO;

public class DeviceGatherQueryACT extends splitPageAction implements SessionAware {
	
	private static final long serialVersionUID = 1L;

	private static Logger logger = LoggerFactory.getLogger(DeviceGatherQueryACT.class);
	
	private Map session;
	
	private String starttime;
	private String endtime;
	private String starttime1;
	private String endtime1;
	
	private String username;
	private String voipusername;
	
	private String routeInfo ;
	private String voiceRegistInfo;
	 
	private String serialnumber;
	private String loid;
	
	private List dataList;
	
	private DeviceGatherQueryBIO bio;
	/** 需求修改
	public String getDeviceInfo(){
		logger.warn("getDeviceInfo()");
		this.setTime();
		dataList = bio.getDeviceInfo(serialnumber,loid,starttime1,endtime1,username,voipusername,routeInfo,voiceRegistInfo);
		return "queryList";
	}
	**/
	//JSDX_ITMS-REQ-20131107-WJY-002
	/**
	 * 获取采集信息
	 * @return
	 */
	public String getDeviceInfo(){
		logger.warn("getDeviceInfo()");
		this.setTime();
		dataList = bio.getDeviceInfo(curPage_splitPage,num_splitPage,starttime1,endtime1,routeInfo,voiceRegistInfo);
		maxPage_splitPage=bio.getDeviceGatherCount(num_splitPage,starttime1,endtime1,routeInfo,voiceRegistInfo);
		return "queryList";
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
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getVoipusername() {
		return voipusername;
	}
	public void setVoipusername(String voipusername) {
		this.voipusername = voipusername;
	}
	public String getSerialnumber() {
		return serialnumber;
	}
	public void setSerialnumber(String serialnumber) {
		this.serialnumber = serialnumber;
	}
	public String getLoid() {
		return loid;
	}
	public void setLoid(String loid) {
		this.loid = loid;
	}
	public List getDataList() {
		return dataList;
	}
	public void setDataList(List dataList) {
		this.dataList = dataList;
	}
	public DeviceGatherQueryBIO getBio() {
		return bio;
	}
	public void setBio(DeviceGatherQueryBIO bio) {
		this.bio = bio;
	}
	public String getRouteInfo() {
		return routeInfo;
	}
	public void setRouteInfo(String routeInfo) {
		this.routeInfo = routeInfo;
	}
	public String getVoiceRegistInfo() {
		return voiceRegistInfo;
	}
	public void setVoiceRegistInfo(String voiceRegistInfo) {
		this.voiceRegistInfo = voiceRegistInfo;
	}
	public void setSession(Map session) {
		this.session = session;
	}
	
}
