package com.linkage.module.itms.service.bio;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.util.HttpUtil;
import com.linkage.module.itms.service.obj.SheetObj;
import com.linkage.module.itms.service.util.HttpClientCallSoapUtil;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


/**
 * 工单处理类
 */
public class BssSheetByHand4GSDXBIOServ 
{
	private static Logger logger = LoggerFactory.getLogger(BssSheetByHand4HBBIO.class);
	
	private static final String endPointReference = LipossGlobals.getLipossProperty("webServiceUri");

	/**
	 * 开通用户工单
	 */
	public String doUserInfo(SheetObj sheetObj)
	{
		logger.debug("doUserInfo(sheetObj)");
		StringBuffer inParam = new StringBuffer();
		inParam.append("<soapenv:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:com=\"com.ailk.module.gtms.soup\">\n");
		inParam.append("<soapenv:Header/>\n");
		inParam.append("<soapenv:Body>\n");
		inParam.append("<com:dealOrder soapenv:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\">\n");
		inParam.append("<order xsi:type=\"com:Order\">\n");
		
		inParam.append("		<order_Time>").append(sheetObj.getDealDate()).append("</order_Time>				\n");
		inParam.append("		<user_Type>").append(sheetObj.getUserType()).append("</user_Type>				\n");
		inParam.append("		<user_name>").append(sheetObj.getLinkman()).append("</user_name>					\n");
		inParam.append("		<userPhone>").append(sheetObj.getLinkPhone()).append("</userPhone>				\n");
		inParam.append("		<userEmail>").append(sheetObj.getEmail()).append("</userEmail>						\n");
		inParam.append("		<user_address>").append(sheetObj.getLinkAddress()).append("</user_address>			\n");
		inParam.append("		<userCard>").append(sheetObj.getLinkmanCredno()).append("</userCard>		\n");
		//必要参数
		inParam.append("		<area_code>").append(sheetObj.getCityId()).append("</area_code>					\n");
		inParam.append("		<order_Type>").append("cpe-Z").append("</order_Type>				\n");
		inParam.append("		<ad_userid>").append(sheetObj.getLoid()).append("</ad_userid>				\n");
		
		inParam.append("</order>\n");
		inParam.append("</com:dealOrder>\n");
		inParam.append("</soapenv:Body>\n");
		inParam.append("</soapenv:Envelope>\n");
		logger.warn(inParam.toString());
		return HttpClientCallSoapUtil.doPostSoap(endPointReference, inParam.toString(), "");
	}
	
	/**
	 * 修改用户工单
	 */
	public String changeUserInfo(SheetObj sheetObj)
	{
		logger.debug("changeUserInfo(sheetObj)");
		StringBuffer inParam = new StringBuffer();
		inParam.append("<soapenv:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:com=\"com.ailk.module.gtms.soup\">\n");
		inParam.append("<soapenv:Header/>\n");
		inParam.append("<soapenv:Body>\n");
		inParam.append("<com:dealOrder soapenv:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\">\n");
		inParam.append("<order xsi:type=\"com:Order\">\n");
		
		inParam.append("		<order_Time>").append(sheetObj.getDealDate()).append("</order_Time>				\n");
		inParam.append("		<user_Type>").append(sheetObj.getUserType()).append("</user_Type>				\n");
		inParam.append("		<user_name>").append(sheetObj.getLinkman()).append("</user_name>					\n");
		inParam.append("		<userPhone>").append(sheetObj.getLinkPhone()).append("</userPhone>				\n");
		inParam.append("		<userEmail>").append(sheetObj.getEmail()).append("</userEmail>						\n");
		inParam.append("		<user_address>").append(sheetObj.getLinkAddress()).append("</user_address>			\n");
		inParam.append("		<userCard>").append(sheetObj.getLinkmanCredno()).append("</userCard>		\n");
		//必要参数
		inParam.append("		<area_code>").append(sheetObj.getCityId()).append("</area_code>					\n");
		inParam.append("		<order_Type>").append("cpe-G").append("</order_Type>				\n");
		inParam.append("		<ad_userid>").append(sheetObj.getLoid()).append("</ad_userid>				\n");
		
		inParam.append("</order>\n");
		inParam.append("</com:dealOrder>\n");
		inParam.append("</soapenv:Body>\n");
		inParam.append("</soapenv:Envelope>\n");
		logger.warn(inParam.toString());
		return HttpClientCallSoapUtil.doPostSoap(endPointReference, inParam.toString(), "");
	}
	
	/**
	 * 删除用户资料
	 */
	public String delUserSheetResult(SheetObj sheetObj)
	{
		logger.debug("delUserInfo(sheetObj)");
		StringBuffer inParam = new StringBuffer();
		inParam.append("<soapenv:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:com=\"com.ailk.module.gtms.soup\">\n");
		inParam.append("<soapenv:Header/>\n");
		inParam.append("<soapenv:Body>\n");
		inParam.append("<com:dealOrder soapenv:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\">\n");
		inParam.append("<order xsi:type=\"com:Order\">\n");
		
		//必要参数
		inParam.append("		<area_code>").append(sheetObj.getCityId()).append("</area_code>					\n");
		inParam.append("		<order_Type>").append("cpe-C").append("</order_Type>				\n");
		inParam.append("		<ad_userid>").append(sheetObj.getLoid()).append("</ad_userid>				\n");
		
		inParam.append("</order>\n");
		inParam.append("</com:dealOrder>\n");
		inParam.append("</soapenv:Body>\n");
		inParam.append("</soapenv:Envelope>\n");
		logger.warn(inParam.toString());
		String result=HttpClientCallSoapUtil.doPostSoap(endPointReference, inParam.toString(), "");
		return getResult(result);
	}
	
	/**
	 * 开通SipVoip工单
	 */
	public String doSipVoip(SheetObj sheetObj)
	{
		
		logger.debug("doUserInfo(sheetObj)");
		StringBuffer inParam = new StringBuffer();
		inParam.append("<soapenv:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:com=\"com.ailk.module.gtms.soup\">\n");
		inParam.append("<soapenv:Header/>\n");
		inParam.append("<soapenv:Body>\n");
		inParam.append("<com:dealOrder soapenv:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\">\n");
		inParam.append("<order xsi:type=\"com:Order\">\n");
		
		inParam.append("		<order_Time>").append(sheetObj.getDealDate()).append("</order_Time>				\n");
		inParam.append("		<user_Type>").append(sheetObj.getUserType()).append("</user_Type>				\n");
		//必要参数
		inParam.append("		<area_code>").append(sheetObj.getCityId()).append("</area_code>					\n");
		inParam.append("		<order_Type>").append("voip-Z").append("</order_Type>				\n");
		inParam.append("		<ad_userid>").append(sheetObj.getLoid()).append("</ad_userid>				\n");
		
		inParam.append("<vector_argues>")
		.append("voip_AuthName="+sheetObj.getSipVoipUsername()+"^")
		.append("voip_AuthPass="+sheetObj.getSipVoipPwd()+"^")
		.append("voip_serverAddr="+sheetObj.getSipProxServ()+"^")
		.append("voip_serverPort="+sheetObj.getSipProxPort()+"^")
		.append("voip_PortNum="+sheetObj.getSipPortNum()+"^")
		.append("voip_mode="+sheetObj.getSipProtocol()+"^")
		.append("voip_URI="+sheetObj.getSipVoipUri()+"")
		.append("</vector_argues> \n");
		
		inParam.append("</order>\n");
		inParam.append("</com:dealOrder>\n");
		inParam.append("</soapenv:Body>\n");
		inParam.append("</soapenv:Envelope>\n");
		logger.warn(inParam.toString());
		return HttpClientCallSoapUtil.doPostSoap(endPointReference, inParam.toString(), "");
		
	}
		
		
		
		
	
	/**
	 * 销户SIP VOIP工单
	 */
	public String delSipVoipSheetResult(SheetObj sheetObj)
	{
		logger.debug("doUserInfo(sheetObj)");
		StringBuffer inParam = new StringBuffer();
		inParam.append("<soapenv:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:com=\"com.ailk.module.gtms.soup\">\n");
		inParam.append("<soapenv:Header/>\n");
		inParam.append("<soapenv:Body>\n");
		inParam.append("<com:dealOrder soapenv:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\">\n");
		inParam.append("<order xsi:type=\"com:Order\">\n");
		
		inParam.append("		<order_Time>").append(sheetObj.getDealDate()).append("</order_Time>				\n");
		inParam.append("		<user_Type>").append(sheetObj.getUserType()).append("</user_Type>				\n");
		//必要参数
		inParam.append("		<area_code>").append(sheetObj.getCityId()).append("</area_code>					\n");
		inParam.append("		<order_Type>").append("voip-C").append("</order_Type>				\n");
		inParam.append("		<ad_userid>").append(sheetObj.getLoid()).append("</ad_userid>				\n");
		
		inParam.append("<vector_argues>")
		.append("voip_AuthName="+sheetObj.getSipVoipUsername()+"^")
		.append("voip_AuthPass="+sheetObj.getSipVoipPwd()+"^")
		.append("voip_serverAddr="+sheetObj.getSipProxServ()+"^")
		.append("voip_serverPort="+sheetObj.getSipProxPort()+"^")
		.append("voip_PortNum="+sheetObj.getSipPortNum()+"^")
		.append("voip_mode=0^")
		.append("voip_URI="+sheetObj.getSipVoipUri()+"")
		.append("</vector_argues> \n");
		
		inParam.append("</order>\n");
		inParam.append("</com:dealOrder>\n");
		inParam.append("</soapenv:Body>\n");
		inParam.append("</soapenv:Envelope>\n");
		logger.warn(inParam.toString());
		String result=HttpClientCallSoapUtil.doPostSoap(endPointReference, inParam.toString(), "");
		return getResult(result);
	}
	
	/**
	 * 开通H248Voip工单
	 */
	public String doH248VoipSheet(SheetObj sheetObj)
	{
		StringBuffer inParam = new StringBuffer();
		inParam.append("<soapenv:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:com=\"com.ailk.module.gtms.soup\">\n");
		inParam.append("<soapenv:Header/>\n");
		inParam.append("<soapenv:Body>\n");
		inParam.append("<com:dealOrder soapenv:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\">\n");
		inParam.append("<order xsi:type=\"com:Order\">\n");
		
		inParam.append("		<order_Time>").append(sheetObj.getDealDate()).append("</order_Time>				\n");
		inParam.append("		<user_Type>").append(sheetObj.getUserType()).append("</user_Type>				\n");
		//必要参数
		inParam.append("		<area_code>").append(sheetObj.getCityId()).append("</area_code>					\n");
		inParam.append("		<order_Type>").append("voip-Z").append("</order_Type>				\n");
		inParam.append("		<ad_userid>").append(sheetObj.getLoid()).append("</ad_userid>				\n");
		
		inParam.append("<vector_argues>")
		.append("voip_phoneNumber="+sheetObj.getHvoipPhone()+"^")
		.append("voip_deviceID="+sheetObj.getHvoipRegId()+"^")
		.append("voip_mode=2")
		.append("</vector_argues> \n");
		
		inParam.append("</order>\n");
		inParam.append("</com:dealOrder>\n");
		inParam.append("</soapenv:Body>\n");
		inParam.append("</soapenv:Envelope>\n");
		logger.warn(inParam.toString());
		return HttpClientCallSoapUtil.doPostSoap(endPointReference, inParam.toString(), "");
		
	}
	
	/**
	 * 销户H248Voip工单
	 */
	public String delH248VoipSheetResult(SheetObj sheetObj)
	{
		StringBuffer inParam = new StringBuffer();
		inParam.append("<soapenv:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:com=\"com.ailk.module.gtms.soup\">\n");
		inParam.append("<soapenv:Header/>\n");
		inParam.append("<soapenv:Body>\n");
		inParam.append("<com:dealOrder soapenv:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\">\n");
		inParam.append("<order xsi:type=\"com:Order\">\n");
		
		inParam.append("		<order_Time>").append(sheetObj.getDealDate()).append("</order_Time>				\n");
		inParam.append("		<user_Type>").append(sheetObj.getUserType()).append("</user_Type>				\n");
		//必要参数
		inParam.append("		<area_code>").append(sheetObj.getCityId()).append("</area_code>					\n");
		inParam.append("		<order_Type>").append("voip-C").append("</order_Type>				\n");
		inParam.append("		<ad_userid>").append(sheetObj.getLoid()).append("</ad_userid>				\n");
		
		inParam.append("<vector_argues>")
		.append("voip_phoneNumber="+sheetObj.getHvoipPhone()+"^")
		.append("voip_deviceID="+sheetObj.getHvoipRegId()+"^")
		.append("voip_mode=2")
		.append("</vector_argues> \n");
		
		inParam.append("</order>\n");
		inParam.append("</com:dealOrder>\n");
		inParam.append("</soapenv:Body>\n");
		inParam.append("</soapenv:Envelope>\n");
		logger.warn(inParam.toString());
		String result=HttpClientCallSoapUtil.doPostSoap(endPointReference, inParam.toString(), "");
		return getResult(result);
	}
	
	
	/**
	 * 开通iptv工单
	 */
	public String doItvSheetResult(SheetObj sheetObj)
	{
		StringBuffer inParam = new StringBuffer();
		inParam.append("<soapenv:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:com=\"com.ailk.module.gtms.soup\">\n");
		inParam.append("<soapenv:Header/>\n");
		inParam.append("<soapenv:Body>\n");
		inParam.append("<com:dealOrder soapenv:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\">\n");
		inParam.append("<order xsi:type=\"com:Order\">\n");
		
		inParam.append("		<order_Time>").append(sheetObj.getDealDate()).append("</order_Time>				\n");
		inParam.append("		<user_Type>").append(sheetObj.getUserType()).append("</user_Type>				\n");
		//必要参数
		inParam.append("		<area_code>").append(sheetObj.getCityId()).append("</area_code>					\n");
		inParam.append("		<order_Type>").append("iptv-Z").append("</order_Type>				\n");
		inParam.append("		<ad_userid>").append(sheetObj.getLoid()).append("</ad_userid>				\n");
		
		inParam.append("<vector_argues>")
		.append("iptv_authName="+sheetObj.getIptvUserName()+"")
		.append("</vector_argues> \n");
		
		inParam.append("</order>\n");
		inParam.append("</com:dealOrder>\n");
		inParam.append("</soapenv:Body>\n");
		inParam.append("</soapenv:Envelope>\n");
		logger.warn(inParam.toString());
		return HttpClientCallSoapUtil.doPostSoap(endPointReference, inParam.toString(), "");
	}
	
	/**
	 * 销户IPTV工单
	 */
	public String delIptvSheetResult(SheetObj sheetObj)
	{
		StringBuffer inParam = new StringBuffer();
		inParam.append("<soapenv:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:com=\"com.ailk.module.gtms.soup\">\n");
		inParam.append("<soapenv:Header/>\n");
		inParam.append("<soapenv:Body>\n");
		inParam.append("<com:dealOrder soapenv:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\">\n");
		inParam.append("<order xsi:type=\"com:Order\">\n");
		
		inParam.append("		<order_Time>").append(sheetObj.getDealDate()).append("</order_Time>				\n");
		inParam.append("		<user_Type>").append(sheetObj.getUserType()).append("</user_Type>				\n");
		//必要参数
		inParam.append("		<area_code>").append(sheetObj.getCityId()).append("</area_code>					\n");
		inParam.append("		<order_Type>").append("iptv-C").append("</order_Type>				\n");
		inParam.append("		<ad_userid>").append(sheetObj.getLoid()).append("</ad_userid>				\n");
		
		inParam.append("<vector_argues>")
		.append("iptv_authName="+sheetObj.getIptvUserName()+"")
		.append("</vector_argues> \n");
		
		inParam.append("</order>\n");
		inParam.append("</com:dealOrder>\n");
		inParam.append("</soapenv:Body>\n");
		inParam.append("</soapenv:Envelope>\n");
		String result=HttpClientCallSoapUtil.doPostSoap(endPointReference, inParam.toString(), "");
		return getResult(result);
	}

	/**
	 * 开通宽带工单
	 */
	public String doNetSheetResult(SheetObj sheetObj)
	{
		StringBuffer inParam = new StringBuffer();
		inParam.append("<soapenv:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:com=\"com.ailk.module.gtms.soup\">\n");
		inParam.append("<soapenv:Header/>\n");
		inParam.append("<soapenv:Body>\n");
		inParam.append("<com:dealOrder soapenv:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\">\n");
		inParam.append("<order xsi:type=\"com:Order\">\n");
		
		inParam.append("		<order_Time>").append(sheetObj.getDealDate()).append("</order_Time>				\n");
		inParam.append("		<user_Type>").append(sheetObj.getUserType()).append("</user_Type>				\n");
		//必要参数
		inParam.append("		<area_code>").append(sheetObj.getCityId()).append("</area_code>					\n");
		inParam.append("		<order_Type>").append("wband-Z").append("</order_Type>				\n");
		inParam.append("		<ad_userid>").append(sheetObj.getLoid()).append("</ad_userid>				\n");
		
		inParam.append("<vector_argues>")
		.append("wband_name="+sheetObj.getNetUsername()+"^")
		.append("wband_num="+sheetObj.getNetNum()+"^")
		.append("wband_mode="+sheetObj.getNetWanType()+"")
		.append("</vector_argues> \n");
		
		inParam.append("</order>\n");
		inParam.append("</com:dealOrder>\n");
		inParam.append("</soapenv:Body>\n");
		inParam.append("</soapenv:Envelope>\n");
		logger.warn(inParam.toString());
		return HttpClientCallSoapUtil.doPostSoap(endPointReference, inParam.toString(), "");
		
	}
	/**
	 * 销户宽带工单
	 */
	public String delNetSheetResult(SheetObj sheetObj)
	{
		StringBuffer inParam = new StringBuffer();
		inParam.append("<soapenv:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:com=\"com.ailk.module.gtms.soup\">\n");
		inParam.append("<soapenv:Header/>\n");
		inParam.append("<soapenv:Body>\n");
		inParam.append("<com:dealOrder soapenv:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\">\n");
		inParam.append("<order xsi:type=\"com:Order\">\n");
		
		inParam.append("		<order_Time>").append(sheetObj.getDealDate()).append("</order_Time>				\n");
		inParam.append("		<user_Type>").append(sheetObj.getUserType()).append("</user_Type>				\n");
		//必要参数
		inParam.append("		<area_code>").append(sheetObj.getCityId()).append("</area_code>					\n");
		inParam.append("		<order_Type>").append("wband-C").append("</order_Type>				\n");
		inParam.append("		<ad_userid>").append(sheetObj.getLoid()).append("</ad_userid>				\n");
		
		inParam.append("<vector_argues>")
		.append("wband_name="+sheetObj.getNetUsername()+"^")
		.append("wband_num="+sheetObj.getNetNum()+"^")
		.append("wband_mode="+sheetObj.getNetWanType()+"")
		.append("</vector_argues> \n");
		
		inParam.append("</order>\n");
		inParam.append("</com:dealOrder>\n");
		inParam.append("</soapenv:Body>\n");
		inParam.append("</soapenv:Envelope>\n");
		logger.warn(inParam.toString());
		String result=HttpClientCallSoapUtil.doPostSoap(endPointReference, inParam.toString(), "");
		return getResult(result);
	}
	
	/**
	 * 开通wifi工单
	 */
	public String doWifiSheetResult(SheetObj sheetObj)
	{
		StringBuffer inParam = new StringBuffer();
		inParam.append("<soapenv:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:com=\"com.ailk.module.gtms.soup\">\n");
		inParam.append("<soapenv:Header/>\n");
		inParam.append("<soapenv:Body>\n");
		inParam.append("<com:dealOrder soapenv:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\">\n");
		inParam.append("<order xsi:type=\"com:Order\">\n");
		
		inParam.append("		<order_Time>").append(sheetObj.getDealDate()).append("</order_Time>				\n");
		inParam.append("		<user_Type>").append(sheetObj.getUserType()).append("</user_Type>				\n");
		//必要参数
		inParam.append("		<area_code>").append(sheetObj.getCityId()).append("</area_code>					\n");
		inParam.append("		<order_Type>").append("wifi-Z").append("</order_Type>				\n");
		inParam.append("		<ad_userid>").append(sheetObj.getLoid()).append("</ad_userid>				\n");
		
		inParam.append("<vector_argues>")
		.append("ssid_name="+sheetObj.getSsidName()+"^")
		.append("ssid_userNumber="+sheetObj.getWifiNum()+"^")
		.append("wband_mode="+sheetObj.getNetWanType()+"^")
		.append("wifi_name="+sheetObj.getWifiUsername()+"^")
		.append("wifi_password="+sheetObj.getWifiPassword()+"")
		.append("</vector_argues> \n");
		
		inParam.append("</order>\n");
		inParam.append("</com:dealOrder>\n");
		inParam.append("</soapenv:Body>\n");
		inParam.append("</soapenv:Envelope>\n");
		logger.warn(inParam.toString());
		return HttpClientCallSoapUtil.doPostSoap(endPointReference, inParam.toString(), "");
		
	}
	/**
	 * 销户wifi工单
	 */
	public String delWifiSheetResult(SheetObj sheetObj)
	{
		StringBuffer inParam = new StringBuffer();
		inParam.append("<soapenv:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:com=\"com.ailk.module.gtms.soup\">\n");
		inParam.append("<soapenv:Header/>\n");
		inParam.append("<soapenv:Body>\n");
		inParam.append("<com:dealOrder soapenv:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\">\n");
		inParam.append("<order xsi:type=\"com:Order\">\n");
		
		inParam.append("		<order_Time>").append(sheetObj.getDealDate()).append("</order_Time>				\n");
		inParam.append("		<user_Type>").append(sheetObj.getUserType()).append("</user_Type>				\n");
		//必要参数
		inParam.append("		<area_code>").append(sheetObj.getCityId()).append("</area_code>					\n");
		inParam.append("		<order_Type>").append("wifi-C").append("</order_Type>				\n");
		inParam.append("		<ad_userid>").append(sheetObj.getLoid()).append("</ad_userid>				\n");
		
		inParam.append("<vector_argues>")
		.append("ssid_name="+sheetObj.getSsidName()+"^")
		.append("ssid_userNumber="+sheetObj.getWifiNum()+"^")
		.append("wband_mode="+sheetObj.getNetWanType()+"^")
		.append("wifi_name="+sheetObj.getWifiUsername()+"^")
		.append("wifi_password="+sheetObj.getWifiPassword()+"")
		.append("</vector_argues> \n");
		
		inParam.append("</order>\n");
		inParam.append("</com:dealOrder>\n");
		inParam.append("</soapenv:Body>\n");
		inParam.append("</soapenv:Envelope>\n");
		logger.warn(inParam.toString());
		String result=HttpClientCallSoapUtil.doPostSoap(endPointReference, inParam.toString(), "");
		return getResult(result);
	}
	
	/**
	 * 解析xml字符串，获取指定节点值
	 */
	public String getMessage(String code)
	{
		String result="成功";
		if (code.equals("1")) {
			result="成功";
		}else if (code.equals("-1")) {
			result="SN不存在 ";
		}else if (code.equals("-10")) {
			result="loid不存在";
		}else if (code.equals("-12")) {
			result="内部错误";
		}else if (code.equals("-6")) {
			result="其他解析错误";
		}else if (code.equals("-8")) {
			result="工单内容错误";
		}else {
			result="失败";
		}
		return result;
	}
	
	public String getResult(String xmlStr)
	{
		String result = "";
		Document doc = null;
		try
		{
			doc = DocumentHelper.parseText(xmlStr);
			Element rootElt = doc.getRootElement(); 
			result = rootElt.element("Body").elementTextTrim("multiRef");
		}
		catch (Exception e)
		{
			logger.error("exception:"+e);
			e.printStackTrace();
		} 
		return result;
	}

	/*
	* "UserInfoType":"2",
	"UserInfo":"0012341234",
	"IPoE_UserName":"12345678",
	"IPoE_Password":"test1234",
	"IPoE_UpBandwidth":"200",
	"IPoE_DownBandwidth":"500",
	"IPoE_Vlan":"2001",
	"IPoE_Dscp":"0",
	"Op_Type":"Z",
	"App_Type":"03"

	* */
	public String doCloudNetSheetResult(SheetObj obj) {
		String result = "";
		JSONObject paramMap = new JSONObject();
		paramMap.put("UserInfoType","2");
		paramMap.put("UserInfo",obj.getLoid());
		paramMap.put("IPoE_UserName",obj.getCloudNetAccount());
		paramMap.put("IPoE_Password",obj.getCloudNetPass());
		paramMap.put("IPoE_UpBandwidth",obj.getCloudNetUpBandwidth());
		paramMap.put("IPoE_DownBandwidth",obj.getCloudNetDownBandwidth());
		paramMap.put("IPoE_Vlan",obj.getCloudNetVlanId());
		paramMap.put("IPoE_Dscp",obj.getCloudNetDscp());
		paramMap.put("Op_Type",obj.getCloudNetOperateId());
		paramMap.put("App_Type",obj.getCloudNetAppType());

		//http://ip:port/cloudbroadband /BroadbandConfiguration
		String url = LipossGlobals.getLipossProperty("cloudNetService");
		url = url + "/BroadbandConfiguration";
		Map<String, String> headers = new HashMap<String, String>();

		headers.put("X-APP-ID",LipossGlobals.getLipossProperty("X_APP_ID"));
		headers.put("X-APP-KEY",LipossGlobals.getLipossProperty("X_APP_KEY"));
		headers.put("X-CTG-Request-ID", UUID.randomUUID().toString().replace("-", "").toLowerCase());
		headers.put("X-CTG-Province-ID","8110000");
		headers.put("X-CTG-Lan-ID","8110100");
		headers.put("Version","V1.0.0");
		//Content-Type: application/json
		//X-APP-ID: FFnN2hso42Wego3pWq4X5qlu
		//X-APP-KEY: UtOCzqb67d3sN12Kts4URwy8
		//X-CTG-Request-ID：92598bee-7d30-4086-afc9-a7be6bd2cda0
		//X-CTG-Province-ID: 8110000
		//X-CTG-Lan-ID: 8110100
		//Version: V1.0.0

		logger.warn("云网超宽带调用url:"+url);
		try {
			result = HttpUtil.doPost(url, paramMap,headers);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return result;
	}


	public String delCloudNetSheetResult(SheetObj obj) {
		String cloudNetDelResult = "";
		JSONObject paramMap = new JSONObject();
		paramMap.put("UserInfoType","2");
		paramMap.put("UserInfo",obj.getLoid());
		paramMap.put("IPoE_UserName",obj.getCloudNetAccount());
		paramMap.put("IPoE_Password",obj.getCloudNetPass());
		paramMap.put("IPoE_UpBandwidth",obj.getCloudNetUpBandwidth());
		paramMap.put("IPoE_DownBandwidth",obj.getCloudNetDownBandwidth());
		paramMap.put("IPoE_Vlan",obj.getCloudNetVlanId());
		paramMap.put("IPoE_Dscp",obj.getCloudNetDscp());
		paramMap.put("Op_Type",obj.getCloudNetOperateId());
		paramMap.put("App_Type",obj.getCloudNetAppType());

		Map<String, String> headers = new HashMap<String, String>();

		headers.put("X-APP-ID",LipossGlobals.getLipossProperty("X_APP_ID"));
		headers.put("X-APP-KEY",LipossGlobals.getLipossProperty("X_APP_KEY"));
		headers.put("X-CTG-Request-ID", UUID.randomUUID().toString().replace("-", "").toLowerCase());
		headers.put("X-CTG-Province-ID","8110000");
		headers.put("X-CTG-Lan-ID","8110100");
		headers.put("Version","V1.0.0");

		//http://ip:port/cloudbroadband /BroadbandConfiguration
		String url = LipossGlobals.getLipossProperty("cloudNetService");
		url = url + "/BroadbandConfiguration";
		try {
			cloudNetDelResult = HttpUtil.doPost(url, paramMap,headers);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}

		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap = JSON.parseObject(cloudNetDelResult, resultMap.getClass());
		return StringUtil.getStringValue(resultMap.get("RstMsg"));

	}
}
