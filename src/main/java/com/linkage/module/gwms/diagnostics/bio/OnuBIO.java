package com.linkage.module.gwms.diagnostics.bio;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.gwms.obj.tr069.ParameValueOBJ;
import com.linkage.module.gwms.util.corba.ACSCorba;

public class OnuBIO {

	private static Logger logger = LoggerFactory.getLogger(OnuBIO.class);
	
	/** 时间服务器地址*/
	private String ntpserver1Name = "InternetGatewayDevice.Time.NTPServer1";
	
	private String ntpserver2Name = "InternetGatewayDevice.Time.NTPServer2";
	
	private String ntpserver3Name = "InternetGatewayDevice.Time.NTPServer3";
	
	private String ntpserver4Name = "InternetGatewayDevice.Time.NTPServer4";
	
	private String ntpserver5Name = "InternetGatewayDevice.Time.NTPServer5";
	
	/** 同步间隔时间*/
	private String ntpintervalName = "InternetGatewayDevice.Time.X_CT-COM_NTPInterval";
	
	/** 时间同步方式*/
	private String ntpservertypeName = "InternetGatewayDevice.Time.X_CT-COM_NTPServerType";
	
	
	/**
	 * 获取upnp参数配置结果
	 * @param deviceId
	 * @param gw_type
	 * @return
	 */
	public ArrayList<ParameValueOBJ> getOnu(String deviceId, String gw_type){
		logger.debug("getOnu({})",deviceId);
		ACSCorba acsCorba = new ACSCorba(gw_type);
		String arr[] = {"InternetGatewayDevice.Time.NTPServer1",
				"InternetGatewayDevice.Time.NTPServer2","InternetGatewayDevice.Time.NTPServer3",
				"InternetGatewayDevice.Time.NTPServer4","InternetGatewayDevice.Time.NTPServer5",
				"InternetGatewayDevice.Time.X_CT-COM_NTPInterval","InternetGatewayDevice.Time.X_CT-COM_NTPServerType"};		
		return acsCorba.getValue(deviceId,arr);
	}
	
	
	public int setOnu(String deviceId, String gw_type,List<String> onuParamList){
		logger.debug("setOnu({},{})",deviceId);
		
		ArrayList<ParameValueOBJ> parameValueOBJList = new ArrayList<ParameValueOBJ>();
		parameValueOBJList.add(new ParameValueOBJ(this.ntpserver1Name,onuParamList.get(0),"1"));
		parameValueOBJList.add(new ParameValueOBJ(this.ntpserver2Name,onuParamList.get(1),"1"));
		parameValueOBJList.add(new ParameValueOBJ(this.ntpserver3Name,onuParamList.get(2),"1"));
		parameValueOBJList.add(new ParameValueOBJ(this.ntpserver4Name,onuParamList.get(3),"1"));
		parameValueOBJList.add(new ParameValueOBJ(this.ntpserver5Name,onuParamList.get(4),"1"));
		parameValueOBJList.add(new ParameValueOBJ(this.ntpintervalName,onuParamList.get(5),"3"));
		parameValueOBJList.add(new ParameValueOBJ(this.ntpservertypeName,onuParamList.get(6),"3"));
		
		ACSCorba acsCorba = new ACSCorba(gw_type);
		return acsCorba.setValue(deviceId,parameValueOBJList);		
	}
}
