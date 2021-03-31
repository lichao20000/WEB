package com.linkage.module.itms.service.bio;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.LipossGlobals;
import com.linkage.module.gwms.Global;
import com.linkage.module.itms.service.obj.SheetObj;
import com.linkage.module.itms.service.util.HttpClientCallSoapUtil;

/**
 * 工单处理类
 */
public class BssSheetByHand4JXLTBIOServ 
{
	private static Logger logger = LoggerFactory.getLogger(BssSheetByHand4HBBIO.class);
	
	private static final String endPointReference = LipossGlobals.getLipossProperty("webServiceUri");

	
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
		inParam.append("<order_Time>").append(sheetObj.getDealDate()).append("</order_Time>				\n");
		inParam.append("<user_Type>").append(sheetObj.getUserType()).append("</user_Type>				\n");
		//必要参数
		inParam.append("<area_code>").append(sheetObj.getCityId()).append("</area_code>					\n");
		inParam.append("<order_Type>").append("voip-Z").append("</order_Type>				\n");
		inParam.append("<device_ID>").append(sheetObj.getLoid()).append("</device_ID>				\n");
		inParam.append("<vector_argues>");
		//voip_Domain=GWZ044HW0002H16018629100^voip_EID=GWZ044HW0002H16018629100^voip_LocalPort=2944^voip_MGCIP=172.16.255.1^voip_MGCPort=2944
		// ^voip_MidFormat=DomainName^voip_RTPPrerfix=RTP/^voip_TIDStart=10001^voip_lineName=A0^voip_mode=2^
		//  voip_standbyMGCIP=172.16.255.4^voip_standbyMGCPort=2944^voip_vlan=11
		inParam.append("WANIPAddress=").append(sheetObj.getHvoipIpaddress()).append("^");
		inParam.append("voip_vlan=").append(sheetObj.getHvoipVlanId()).append("^");
		inParam.append("WANDefaultGateway=").append(sheetObj.getHvoipGateway()).append("^");
		inParam.append("WANSubnetMask=").append(sheetObj.getHvoipIpmask()).append("^");
		inParam.append("voip_MGCIP=").append(sheetObj.getHvoipMgcIp()).append("^");
		inParam.append("voip_standbyMGCIP=").append(sheetObj.getHvoipStandMgcIp()).append("^");
		inParam.append("voip_Domain=").append(sheetObj.getSipUserAgentDomain()).append("^");
		inParam.append("voip_localPort=").append(sheetObj.getHvoipPort()).append("^");
		inParam.append("voip_MGCPort=").append(sheetObj.getHvoipMgcPort()).append("^");
		inParam.append("voip_standbyMGCPort=").append(sheetObj.getHvoipStandMgcPort()).append("^");
		inParam.append("voip_TID=").append(sheetObj.getHvoipEid()).append("^");
		inParam.append("voip_Prefix=").append(sheetObj.getHvoipRtpPrefix()).append("^");
		inParam.append("voip_DNSServers=").append(sheetObj.getHvoipIpdns());
		inParam.append("</vector_argues> \n");
		inParam.append("</order>\n");
		inParam.append("</com:dealOrder>\n");
		inParam.append("</soapenv:Body>\n");
		inParam.append("</soapenv:Envelope>\n");
		logger.warn(inParam.toString());
		return HttpClientCallSoapUtil.doPostSoap(endPointReference, inParam.toString(), "");
		
	}

	/**
	 * 浙江联通开通H248Voip工单
	 */
	public String doH248VoipSheetZjlt(SheetObj sheetObj)
	{
		StringBuffer inParam = new StringBuffer();
		inParam.append("<soapenv:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:com=\"com.ailk.module.gtms.soup\">\n");
		inParam.append("<soapenv:Header/>\n");
		inParam.append("<soapenv:Body>\n");
		inParam.append("<com:dealOrder soapenv:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\">\n");
		inParam.append("<order xsi:type=\"com:Order\">\n");
		inParam.append("<order_Time>").append(sheetObj.getDealDate()).append("</order_Time>				\n");
		inParam.append("<user_Type>").append(sheetObj.getUserType()).append("</user_Type>				\n");
		//必要参数
		inParam.append("<area_code>").append(sheetObj.getCityId()).append("</area_code>					\n");
		inParam.append("<city_code>").append(sheetObj.getCityId()).append("</city_code>					\n");
		inParam.append("<order_Type>").append("voip-Z^cpe-Z").append("</order_Type>				\n");
		inParam.append("<ad_userid>").append(sheetObj.getLoid()).append("</ad_userid>				\n");
		inParam.append("<vector_argues>");

		inParam.append("voip_MidFormat=").append("DomainName").append("^");
		inParam.append("voip_Domain=").append(sheetObj.getHvoipRegId()).append("^");
		inParam.append("voip_lineName=").append(sheetObj.getHvoipPort()).append("^");
		inParam.append("voip_MGCIP=").append(sheetObj.getHvoipMgcIp()).append("^");
		inParam.append("voip_standbyMGCIP=").append(sheetObj.getHvoipStandMgcIp()).append("^");

		inParam.append("voip_MGCPort=").append(sheetObj.getHvoipMgcPort()).append("^");
		inParam.append("voip_standbyMGCPort=").append(sheetObj.getHvoipStandMgcPort()).append("^");
		inParam.append("voip_EID=").append(sheetObj.getHvoipEid()).append("^");

		inParam.append("voip_vlan=").append(sheetObj.getHvoipVlanId()).append("^");

		inParam.append("voip_RTPPrerfix=").append(sheetObj.getHvoipRtpPrefix()).append("^");
		inParam.append("</vector_argues> \n");
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
		inParam.append("<order_Time>").append(sheetObj.getDealDate()).append("</order_Time>				\n");
		inParam.append("<user_Type>").append(sheetObj.getUserType()).append("</user_Type>				\n");
		//必要参数
		inParam.append("<area_code>").append(sheetObj.getCityId()).append("</area_code>					\n");
		inParam.append("<order_Type>").append("voip-C").append("</order_Type>				\n");
		inParam.append("<device_ID>").append(sheetObj.getLoid()).append("</device_ID>				\n");
		inParam.append("<vector_argues>");
		inParam.append("WANIPAddress=").append(sheetObj.getHvoipIpaddress()).append("^");
		inParam.append("voip_vlan=").append(sheetObj.getHvoipVlanId()).append("^");
		inParam.append("WANDefaultGateway=").append(sheetObj.getHvoipGateway()).append("^");
		inParam.append("WANSubnetMask=").append(sheetObj.getHvoipIpmask()).append("^");
		inParam.append("voip_MGCIP=").append(sheetObj.getHvoipMgcIp()).append("^");
		inParam.append("voip_standbyMGCIP=").append(sheetObj.getHvoipStandMgcIp()).append("^");
		inParam.append("voip_Domain=").append(sheetObj.getSipUserAgentDomain()).append("^");
		inParam.append("voip_localPort=").append(sheetObj.getHvoipPort()).append("^");
		inParam.append("voip_MGCPort=").append(sheetObj.getHvoipMgcPort()).append("^");
		inParam.append("voip_standbyMGCPort=").append(sheetObj.getHvoipStandMgcPort()).append("^");
		inParam.append("voip_TID=").append(sheetObj.getHvoipEid()).append("^");
		inParam.append("voip_Prefix=").append(sheetObj.getHvoipRtpPrefix()).append("^");
		inParam.append("voip_DNSServers=").append(sheetObj.getHvoipIpdns());
		inParam.append("</vector_argues> \n");
		inParam.append("</order>\n");
		inParam.append("</com:dealOrder>\n");
		inParam.append("</soapenv:Body>\n");
		inParam.append("</soapenv:Envelope>\n");
		logger.warn(inParam.toString());
		String result=HttpClientCallSoapUtil.doPostSoap(endPointReference, inParam.toString(), "");
		return getResult(result);
	}


	/**
	 * 销户H248Voip工单(浙江联通)
	 */
	public String delH248VoipSheetResultZjlt(SheetObj sheetObj)
	{
		StringBuffer inParam = new StringBuffer();
		inParam.append("<soapenv:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:com=\"com.ailk.module.gtms.soup\">\n");
		inParam.append("<soapenv:Header/>\n");
		inParam.append("<soapenv:Body>\n");
		inParam.append("<com:dealOrder soapenv:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\">\n");
		inParam.append("<order xsi:type=\"com:Order\">\n");
		inParam.append("<order_Time>").append(sheetObj.getDealDate()).append("</order_Time>				\n");
		inParam.append("<user_Type>").append(sheetObj.getUserType()).append("</user_Type>				\n");
		//必要参数
		inParam.append("<area_code>").append(sheetObj.getCityId()).append("</area_code>					\n");
		inParam.append("<order_Type>").append("voip-C").append("</order_Type>				\n");
		inParam.append("<ad_userid>").append(sheetObj.getLoid()).append("</ad_userid>				\n");
		inParam.append("<vector_argues>");

		inParam.append("voip_MidFormat=").append("DomainName").append("^");
		inParam.append("voip_Domain=").append(sheetObj.getHvoipRegId()).append("^");
		inParam.append("voip_lineName=").append(sheetObj.getHvoipPort()).append("^");
		inParam.append("voip_MGCIP=").append(sheetObj.getHvoipMgcIp()).append("^");
		inParam.append("voip_standbyMGCIP=").append(sheetObj.getHvoipStandMgcIp()).append("^");

		inParam.append("voip_MGCPort=").append(sheetObj.getHvoipMgcPort()).append("^");
		inParam.append("voip_standbyMGCPort=").append(sheetObj.getHvoipStandMgcPort()).append("^");
		inParam.append("voip_EID=").append(sheetObj.getHvoipEid()).append("^");

		inParam.append("voip_vlan=").append(sheetObj.getHvoipVlanId()).append("^");

		inParam.append("voip_RTPPrerfix=").append(sheetObj.getHvoipRtpPrefix()).append("^");
		inParam.append("</vector_argues> \n");
		inParam.append("</order>\n");
		inParam.append("</com:dealOrder>\n");
		inParam.append("</soapenv:Body>\n");
		inParam.append("</soapenv:Envelope>\n");
		logger.warn(inParam.toString());
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
		inParam.append("		<device_ID>").append(sheetObj.getLoid()).append("</device_ID>				\n");
		//必要参数
		inParam.append("		<area_code>").append(sheetObj.getCityId()).append("</area_code>					\n");
		inParam.append("		<order_Type>").append("wband-Z").append("</order_Type>				\n");
		inParam.append("<vector_argues>")
		.append("wband_name="+sheetObj.getNetUsername()+"^")
		.append("wband_password="+sheetObj.getNetPasswd()+"^")
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
	 * 浙江联通开通宽带工单
	 */
	public String doNetSheetResultZjlt(SheetObj sheetObj)
	{
		StringBuffer inParam = new StringBuffer();
		inParam.append("<soapenv:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:com=\"com.ailk.module.gtms.soup\">\n");
		inParam.append("<soapenv:Header/>\n");
		inParam.append("<soapenv:Body>\n");
		inParam.append("<com:dealOrder soapenv:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\">\n");
		inParam.append("<order xsi:type=\"com:Order\">\n");

		inParam.append("		<order_Time>").append(sheetObj.getDealDate()).append("</order_Time>				\n");
		inParam.append("		<ad_userid>").append(sheetObj.getLoid()).append("</ad_userid>				\n");
		//必要参数
		inParam.append("		<area_code>").append(sheetObj.getCityId()).append("</area_code>					\n");
		inParam.append("		<city_code>").append(sheetObj.getCityId()).append("</city_code>					\n");
		inParam.append("		<order_Type>").append("wband-Z^cpe-Z").append("</order_Type>				\n");
		inParam.append("<vector_argues>")
				.append("wband_name="+sheetObj.getNetUsername()+"^")
				.append("wband_password="+sheetObj.getNetPasswd()+"^")
				.append("wband_mode="+sheetObj.getNetWanType()+"^")
				.append("wband_vlan="+sheetObj.getNetVlanId()+"")
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
	 * 销户宽带工单(浙江联通)
	 */
	public String delNetSheetResultZjlt(SheetObj sheetObj)
	{
		StringBuffer inParam = new StringBuffer();
		inParam.append("<soapenv:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:com=\"com.ailk.module.gtms.soup\">\n");
		inParam.append("<soapenv:Header/>\n");
		inParam.append("<soapenv:Body>\n");
		inParam.append("<com:dealOrder soapenv:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\">\n");
		inParam.append("<order xsi:type=\"com:Order\">\n");

		inParam.append("		<order_Time>").append(sheetObj.getDealDate()).append("</order_Time>				\n");
		inParam.append("		<ad_userid>").append(sheetObj.getLoid()).append("</ad_userid>				\n");
		//必要参数
		inParam.append("		<area_code>").append(sheetObj.getCityId()).append("</area_code>					\n");
		inParam.append("		<order_Type>").append("wband-C").append("</order_Type>				\n");
		inParam.append("<vector_argues>")
				.append("wband_name="+sheetObj.getNetUsername()+"^")
				.append("wband_password="+sheetObj.getNetPasswd()+"^")
				.append("wband_mode="+sheetObj.getNetWanType()+"^")
				.append("wband_vlan="+sheetObj.getNetVlanId()+"")
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
			if(Global.ZJLT.equals(Global.instAreaShortName))
			{
				result = rootElt.element("Body").element("dealOrderResponse").elementTextTrim("dealOrderReturn");
			}
			else
			{
				result = rootElt.element("Body").elementTextTrim("multiRef");
			}

		}
		catch (Exception e)
		{
			logger.error("exception:"+e);
			e.printStackTrace();
		} 
		return result;
	}
}
