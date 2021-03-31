package com.linkage.module.gtms.config.action;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.gtms.config.serv.StackRefreshQueryBIO;
import com.opensymphony.xwork2.ActionSupport;


public class StackRefreshQueryACT extends ActionSupport implements ServletRequestAware
{
	private static final long serialVersionUID = -4038458265090279530L;

	private static Logger logger = LoggerFactory.getLogger(StackRefreshQueryACT.class);
	StackRefreshQueryBIO bio = null;
	
	private HttpServletRequest request;
	private String device_id;
	private String gw_type;
	private Map<String,String> httpMap;
	private String code;
	
	private String ajax;
	public String init() throws Exception
	{
		return "init";
	}

	public String queryStackRefreshDetail(){
		httpMap = bio.queryStackRefreshDetail(gw_type,device_id);
		code = httpMap.get("code");
		httpMap.remove("code");
		return "list";
	}
	
	
	@Override
	public void setServletRequest(HttpServletRequest arg0)
	{
		this.request = arg0;
	}
	
	public StackRefreshQueryBIO getBio()
	{
		return bio;
	}
	
	public void setBio(StackRefreshQueryBIO bio)
	{
		this.bio = bio;
	}
	
	public String getAjax()
	{
		return ajax;
	}
	
	public void setAjax(String ajax)
	{
		this.ajax = ajax;
	}

	public String getDevice_id() {
		return device_id;
	}

	public void setDevice_id(String device_id) {
		this.device_id = device_id;
	}

	
	public Map<String, String> getHttpMap() {
		return httpMap;
	}

	public void setHttpMap(Map<String, String> httpMap) {
		this.httpMap = httpMap;
	}

	public String getGw_type() {
		return gw_type;
	}

	public void setGw_type(String gw_type) {
		this.gw_type = gw_type;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	
}
