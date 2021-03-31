package com.linkage.module.gtms.config.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.alibaba.fastjson.JSON;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gtms.config.serv.DelWanConnToolsBIO;
import com.linkage.module.gwms.util.StringUtil;

/**
 * @author zhaixx
 * 批量光猫业务参数删除功能
 *
 */
public class DelWanConnToolsACT  extends splitPageAction implements SessionAware {
	
	private static final long serialVersionUID = 1L;
	private static Logger logger = LoggerFactory.getLogger(DelWanConnToolsACT.class);
	private DelWanConnToolsBIO bio;
	// 传参
	private String deviceIds = "";
	// Session
	private Map<String, Object> session;
	private String gwShare_fileName = "";
	private String gwShare_cityId = "";
	// 查询方式
	private String gwShare_queryField = "";
	private int total;
	private int maxupline;
	// 查询参数
	private String gwShare_queryParam = "";
	private String gwShare_msg = "";
	/** 错误账号*/
	private String faultList = "";
	/**
	 * 
	 */
	private String resStr = "";
	private long userId;
	private String ajax = "";
	// 查询结果
	private List<HashMap<String, String>> deviceList;
	
	/**
	 * 查询设备（带列表）
	 * 
	 * @return
	 */
	public String queryDeviceList() {
		logger.debug("DelWanConnToolsACT=>getDeviceList()");
		UserRes curUser = (UserRes) session.get("curUser");
		userId = curUser.getUser().getId();
		if (!StringUtil.IsEmpty(gwShare_cityId)) {
			gwShare_cityId.trim();
		} else {
			this.gwShare_cityId = curUser.getCityId();
		}
		if (!StringUtil.IsEmpty(gwShare_cityId)) {
			gwShare_fileName.trim();
		}
		//没个文件最大个数
		if( maxupline == 0){
			maxupline = 1000;
		}
		List<HashMap<String, String>> deviceListTmp = bio.getDeviceList(userId,curUser, gwShare_cityId, gwShare_fileName, faultList,maxupline);
		if (null == deviceListTmp || deviceListTmp.isEmpty()) {
			this.gwShare_msg = bio.getMsg();
		}else{
			//this.faultList = deviceListTmp.get(deviceListTmp.size()-1).get("faultMap");
			this.deviceList = new ArrayList<HashMap<String, String>>();
			for(int i=0;i<deviceListTmp.size();i++){
				HashMap<String, String> map = deviceListTmp.get(i);
				this.deviceList.add(map);
			}
		}
		total = deviceList == null ? 0 : this.deviceList.size();
		resStr = JSON.toJSONString(deviceList);
		return "shareList";
	}
	/**
	 * 查询设备（带列表）
	 * 
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String doConfigAll() {
		try{
			
			UserRes curUser = (UserRes) session.get("curUser");
			userId = curUser.getUser().getId();
			List<Map> jsonToObject = (List) JSON.parseArray(resStr);
			ajax = 	bio.doConfigAll(userId,jsonToObject);
			return "ajax";
		}catch(Exception e){
			logger.error("执行错误:{}",ExceptionUtils.getStackTrace(e));
			ajax = 	"-4";
			return "ajax";
		}
	}
	public DelWanConnToolsBIO getBio() {
		return bio;
	}


	public void setBio(DelWanConnToolsBIO bio) {
		this.bio = bio;
	}


	public String getDeviceIds() {
		return deviceIds;
	}


	public void setDeviceIds(String deviceIds) {
		this.deviceIds = deviceIds;
	}


	public String getGwShare_fileName() {
		return gwShare_fileName;
	}


	public void setGwShare_fileName(String gwShare_fileName) {
		this.gwShare_fileName = gwShare_fileName;
	}


	public String getGwShare_cityId() {
		return gwShare_cityId;
	}


	public void setGwShare_cityId(String gwShare_cityId) {
		this.gwShare_cityId = gwShare_cityId;
	}


	public String getGwShare_queryField() {
		return gwShare_queryField;
	}


	public void setGwShare_queryField(String gwShare_queryField) {
		this.gwShare_queryField = gwShare_queryField;
	}


	public int getTotal() {
		return total;
	}


	public void setTotal(int total) {
		this.total = total;
	}


	public int getMaxupline() {
		return maxupline;
	}


	public void setMaxupline(int maxupline) {
		this.maxupline = maxupline;
	}


	public String getGwShare_queryParam() {
		return gwShare_queryParam;
	}


	public void setGwShare_queryParam(String gwShare_queryParam) {
		this.gwShare_queryParam = gwShare_queryParam;
	}


	public String getGwShare_msg() {
		return gwShare_msg;
	}


	public void setGwShare_msg(String gwShare_msg) {
		this.gwShare_msg = gwShare_msg;
	}


	public String getFaultList() {
		return faultList;
	}


	public void setFaultList(String faultList) {
		this.faultList = faultList;
	}


	public long getUserId() {
		return userId;
	}


	public void setUserId(long userId) {
		this.userId = userId;
	}


	public List<HashMap<String, String>> getDeviceList() {
		return deviceList;
	}


	public void setDeviceList(List<HashMap<String, String>> deviceList) {
		this.deviceList = deviceList;
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}


	public Map<String, Object> getSession() {
		return session;
	}

	public void setSession(Map<String, Object> session) {
		this.session = session;
	}
	public String getAjax() {
		return ajax;
	}
	public void setAjax(String ajax) {
		this.ajax = ajax;
	}
	public String getResStr() {
		return resStr;
	}
	public void setResStr(String resStr) {
		this.resStr = resStr;
	}

}
