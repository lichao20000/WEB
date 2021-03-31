package com.linkage.module.gwms.diagnostics.bio;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.gwms.obj.tr069.ParameValueOBJ;
import com.linkage.module.gwms.util.corba.ACSCorba;

/**
 * BIO:UPNP
 * @author Administrator
 *
 */
public class UpnpBIO {
	
	private static Logger logger = LoggerFactory.getLogger(UpnpBIO.class);
	// upnp查询参数
	private String upnpParamName = "InternetGatewayDevice.DeviceInfo.X_CT-COM_UPNP.Enable";
	
	
	/**
	 * 获取upnp参数配置结果
	 * @param deviceId
	 * @param gw_type
	 * @return
	 */
	public ArrayList<ParameValueOBJ> getUpnp(String deviceId, String gw_type){
		logger.debug("getUpnp({})",deviceId);
		ACSCorba acsCorba = new ACSCorba(gw_type);
		return acsCorba.getValue(deviceId,upnpParamName);
	}
	
	
	public int setUpnp(String deviceId, String gw_type,String upnpParamValue){
		logger.debug("setUpnp({},{})",deviceId);
		ACSCorba acsCorba = new ACSCorba(gw_type);
		ParameValueOBJ parameValueOBJ = new ParameValueOBJ(upnpParamName, upnpParamValue, "4");
		return acsCorba.setValue(deviceId,parameValueOBJ);		
	}
}
