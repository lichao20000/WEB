package com.linkage.module.gtms.resource.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.StringUtil;
import com.linkage.module.gtms.resource.serv.DeviceInitBIO;

/**
 * 设备数据是否录入初始表tab_gw_device_init功能查询
 * @author wanghong
 *
 */
public class DeviceInitAction 
{
	private static Logger logger = LoggerFactory.getLogger(DeviceInitBIO.class);
	
	private String sn=null;
	private String ajax=null;
	private DeviceInitBIO bio=null;
	
	public String query()
	{
		logger.warn("DeviceInitAction.query({})",sn);
		if(StringUtil.IsEmpty(sn)){
			ajax="请先输入设备序列号";
		}else if(sn.length()<6){
			ajax="请至少输入设备序列号后六位";
		}else{
			ajax=bio.query(sn);
		}
		
		return "ajax";
	}
	
	
	
	
	
	public String getSn() {
		return sn;
	}
	
	public void setSn(String sn) {
		this.sn = sn;
	}
	
	public String getAjax() {
		return ajax;
	}
	
	public void setAjax(String ajax) {
		this.ajax = ajax;
	}
	
	public DeviceInitBIO getBio() {
		return bio;
	}
	
	public void setBio(DeviceInitBIO bio) {
		this.bio = bio;
	}

	
}