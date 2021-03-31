package com.linkage.module.gtms.config.action;

import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.gtms.config.serv.BatchConfigWifiBIO;

import action.splitpage.splitPageAction;

/**
 * 
 * @author yaoli (Ailk No.)
 * @version 1.0
 * @since 2019年9月27日
 * @category com.linkage.module.gtms.config.action
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 *
 */
public class BatchConfigWifiACT extends splitPageAction implements SessionAware
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2780034976624225236L;
	private static Logger logger = LoggerFactory.getLogger(BatchConfigWifiACT.class);
	private   BatchConfigWifiBIO bio = new BatchConfigWifiBIO();

    private String deviceIds;
    private int apEnable;
    private String ajax = "";
    
    
    public String doConfigAll(){
    	 ajax = bio.doConfig(deviceIds, apEnable);
    	 return "ajax";
    }
	@Override
	public void setSession(Map<String, Object> arg0)
	{
		
	}
	public String getDeviceIds()
	{
		return deviceIds;
	}
	public void setDeviceIds(String deviceIds)
	{
		this.deviceIds = deviceIds;
	}
	public int getApEnable()
	{
		return apEnable;
	}
	public void setApEnable(int apEnable)
	{
		this.apEnable = apEnable;
	}
	public BatchConfigWifiBIO getBio()
	{
		return bio;
	}
	public void setBio(BatchConfigWifiBIO bio)
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
	
}
