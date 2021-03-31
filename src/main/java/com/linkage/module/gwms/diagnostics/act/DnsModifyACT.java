package com.linkage.module.gwms.diagnostics.act;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.gwms.diagnostics.bio.DnsModifyBIO;
import com.linkage.module.gwms.obj.tabquery.HgwServUserObj;
import com.opensymphony.xwork2.ActionSupport;

public class DnsModifyACT extends ActionSupport{

	private static final long serialVersionUID = 1L;

	/** log */
	private static Logger logger = LoggerFactory
			.getLogger(DnsModifyACT.class);
	
	/** BIO */
	private DnsModifyBIO bio;
	
	private String ajax;
	
	private String result = "成功！";
	
	private String gw_type;
	
	private String deviceId;
	
	private String userId;
	
	private HgwServUserObj wanBussObj;
	
	private String dns;
	
	private String oldDns;
	
	/**
	 * 获取用户配置信息
	 * return String
	 */
	public String execute(){
		
		logger.debug("execute()");
		logger.warn("=======gw_type==="+gw_type+"==========");
		this.userId = bio.getUserId(this.deviceId);
		if(this.userId == null){
			logger.warn("deviceId为{}的userId为空", deviceId);
			this.result = "失败！";
			return "success";
		}
		
		// 获取用户信息
		this.wanBussObj = bio.getUserInfo(this.userId);	
		if(this.wanBussObj == null){
			logger.warn("userId为{}的用户信息为空", deviceId);
			this.result = "失败！";
		}
		return "success";
	}
	
	/**
	 * 配置dns
	 * @return String
	 */
	public String config() {
		logger.debug("config()");
		
		this.userId = bio.getUserId(this.deviceId);
		// dns值存到数据库
		Map<String,String> cellMap = new HashMap<String, String>();
		cellMap.put("adsl_ser", this.dns);
		
		if(!bio.updateDnsDB(cellMap,this.userId,"10")){
			logger.warn("更新数据库的dns失败！");
			this.ajax = "更新数据库失败";
			return "ajax";
		}
		
		// 下发dns
		this.ajax = bio.setDnsConfig(deviceId, gw_type, dns);
		
		//下发不成功时,回退数据库
		if(!"成功".equals(this.ajax)){
			cellMap.put("adsl_ser", this.oldDns);
			if(!bio.updateDnsDB(cellMap,this.userId,"10")){
				logger.warn("回退数据库dns失败！");
				this.ajax += this.ajax + "切回退数据库dns失败！";
				return "ajax";
			}
		}
		return "ajax";
	}

	public DnsModifyBIO getBio() {
		return bio;
	}

	public void setBio(DnsModifyBIO bio) {
		this.bio = bio;
	}

	public String getAjax() {
		return ajax;
	}

	public void setAjax(String ajax) {
		this.ajax = ajax;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getGw_type() {
		return gw_type;
	}

	public void setGw_type(String gw_type) {
		this.gw_type = gw_type;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public HgwServUserObj getWanBussObj() {
		return wanBussObj;
	}

	public void setWanBussObj(HgwServUserObj wanBussObj) {
		this.wanBussObj = wanBussObj;
	}

	public String getDns() {
		return dns;
	}

	public void setDns(String dns) {
		this.dns = dns;
	}

	public String getOldDns() {
		return oldDns;
	}

	public void setOldDns(String oldDns) {
		this.oldDns = oldDns;
	}	
}
