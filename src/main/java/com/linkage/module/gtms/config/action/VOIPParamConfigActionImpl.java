package com.linkage.module.gtms.config.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.gtms.config.serv.VOIPParamConfigServ;

public class VOIPParamConfigActionImpl implements VOIPParamConfigAction {
	private static Logger logger = LoggerFactory
	.getLogger(VOIPParamConfigActionImpl.class);
	
	/**deviceId 字符串以","隔开 */
	private String deviceIdArr 	;
	/**是否配置 1配置，0不配置*/
	private String configRadio ;
	/**长定时器*/
	private String longTimer ;
	/**短定时器*/
	private String shortTimer ;
	/**服务Id*/
	private String serviceId ;
	/**系统类型 1:ITMS,2:BBMS*/
	private String gwType ;
	
	private String ajax;
	private VOIPParamConfigServ bio;
	
	public String addToStrategy(){
		logger.debug("addToStrategy({},{})",new Object[]{deviceIdArr,serviceId});
		String[] paramArr = new String[2];
		String[] deviceIds = deviceIdArr.split(",");
		if("1".equals(configRadio)){
			paramArr[0] = longTimer;
			paramArr[1] = shortTimer;
		}
		ajax  = bio.addToStrategy(deviceIds,serviceId,paramArr,gwType)+"";
		return "ajax";
	}
	
	public String getDeviceIdArr() {
		return deviceIdArr;
	}
	
	public void setDeviceIdArr(String deviceIdArr) {
		this.deviceIdArr = deviceIdArr;
	}
	
	public String getConfigRadio() {
		return configRadio;
	}
	
	public void setConfigRadio(String configRadio) {
		this.configRadio = configRadio;
	}
	
	public String getLongTimer() {
		return longTimer;
	}
	
	public void setLongTimer(String longTimer) {
		this.longTimer = longTimer;
	}
	
	public String getShortTimer() {
		return shortTimer;
	}
	
	public void setShortTimer(String shortTimer) {
		this.shortTimer = shortTimer;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public VOIPParamConfigServ getBio() {
		return bio;
	}

	public void setBio(VOIPParamConfigServ bio) {
		this.bio = bio;
	}

	public String getGwType() {
		return gwType;
	}

	public void setGwType(String gwType) {
		this.gwType = gwType;
	}

	public String getAjax() {
		return ajax;
	}

	public void setAjax(String ajax) {
		this.ajax = ajax;
	}
}
