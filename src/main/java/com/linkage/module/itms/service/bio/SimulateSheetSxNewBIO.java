package com.linkage.module.itms.service.bio;

import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.LipossGlobals;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.util.SocketUtil;
import com.linkage.module.gwms.util.StringUtil;
import com.linkage.module.itms.service.dao.SimulateSheetSxNewDAO;

public class SimulateSheetSxNewBIO {
	private static Logger logger = LoggerFactory
			.getLogger(SimulateSheetSxNewBIO.class);
	private SimulateSheetSxNewDAO dao;
	private static final String endPointReference = LipossGlobals
			.getLipossProperty("webServiceUri");
	public String checkUsername(String username, String gw_type) {
		List<Map<String, String>> userList = this.dao.getUserInfo(username,
				gw_type);
		int size = userList.size();
		if (size == 1) {
			return "1#"
					+ (String) ((Map<String, String>) userList.get(0))
							.get("orderType");
		}
		if (size > 1) {
			return "0#请输入完整的逻辑SN";
		}
		return "-1#逻辑SN不存在，请先走建设流程";
	}

	/**
	 * 资料开户工单xml
	 */
	public String sendOpenSheet(String servTypeId, String operateType,
			String dealdate, String userType, String username, String cityId,
			String officeId, String zoneId, String orderType, String linkman,
			String linkphone, String linkEmail, String linkMobile,
			String homeAddr, String credNo, String customerId,
			String customerAccount, String customerPwd,String deviceType) 
	{
		Document document = DocumentHelper.createDocument();
		document.setXMLEncoding("UTF-8");
		Element root = document.addElement("root");
		root.addElement("cmdId").addText(transferDateFormate(dealdate));
		root.addElement("authUser").addText("1");
		root.addElement("authPwd").addText("1");
		Element param = root.addElement("param");
		param.addElement("dealDate").addText(transferDateFormate(dealdate));
		param.addElement("userType").addText("1");
		param.addElement("loid").addText(username);
		param.addElement("cityId").addText(cityId);
		root.addElement("operateId").addText("1");
		root.addElement("servTypeId").addText("20");
		param.addElement("officeId").addText("");
		param.addElement("areaId").addText("");
		param.addElement("accessStyle").addText(orderType);
		param.addElement("linkman").addText(linkman);
		param.addElement("linkPhone").addText("");
		param.addElement("email").addText("");
		param.addElement("mobile").addText("");
		param.addElement("linkAddress").addText(homeAddr);
		param.addElement("linkmanCredno").addText("");
		param.addElement("customerId").addText("");
		param.addElement("customerAccount").addText("");
		param.addElement("customerPwd").addText("");
		param.addElement("specId").addText("GPON(hgu21)");
		param.addElement("deviceType").addText(deviceType);
		String sendXml = document.asXML();
		logger.warn("资料 xml:" + sendXml);
		String returnParam = "";
		try
		{
			Service service = new Service();
			Call call = null;
			call = (Call) service.createCall();
			call.setTargetEndpointAddress(new URL(endPointReference));
			call.setOperationName("call");
			returnParam = (String) call.invoke(new Object[] { sendXml });
			logger.warn("结果：" + returnParam);
		}
		catch (Exception e)
		{
			logger.error("err:" + e.getMessage());
		}
		return returnParam;
	}

	public String sendUpdateSheet(String servTypeId, String operateType,
			String dealdate, String username, String cityId, String userType,String newusername)
	{
		Document document = DocumentHelper.createDocument();
		document.setXMLEncoding("UTF-8");
		Element root = document.addElement("root");
		root.addElement("cmdId").addText(transferDateFormate(dealdate));
		root.addElement("authUser").addText("1");
		root.addElement("authPwd").addText("1");
		Element param = root.addElement("param");
		param.addElement("dealDate").addText(transferDateFormate(dealdate));
		param.addElement("userType").addText("1");
		param.addElement("loid").addText(newusername);
		param.addElement("cityId").addText(cityId);
		param.addElement("oldloid").addText(username);
		root.addElement("operateId").addText("2");
		root.addElement("servTypeId").addText("20");
		param.addElement("specId").addText("GPON(hgu21)");
		param.addElement("deviceType").addText("e8-c");
		String sendXml = document.asXML();
		logger.warn("del user xml:" + sendXml);
		String returnParam = "";
		try
		{
			Service service = new Service();
			Call call = null;
			call = (Call) service.createCall();
			call.setTargetEndpointAddress(new URL(endPointReference));
			call.setOperationName("call");
			returnParam = (String) call.invoke(new Object[] { sendXml });
			logger.warn("结果：" + returnParam);
		}
		catch (Exception e)
		{
			logger.error("err:" + e.getMessage());
		}
		return returnParam;
	}
	
	/**
	 *  全业务销户工单
	 */
	public String sendStopSheet(String servTypeId, String operateType,
			String dealdate, String username, String cityId, String userType) 
	{
		Document document = DocumentHelper.createDocument();
		document.setXMLEncoding("UTF-8");
		Element root = document.addElement("root");
		root.addElement("cmdId").addText(transferDateFormate(dealdate));
		root.addElement("authUser").addText("1");
		root.addElement("authPwd").addText("1");
		Element param = root.addElement("param");
		param.addElement("dealDate").addText(transferDateFormate(dealdate));
		param.addElement("userType").addText("1");
		param.addElement("loid").addText(username);
		param.addElement("cityId").addText(cityId);
		root.addElement("operateId").addText("3");
		root.addElement("servTypeId").addText("20");
		String sendXml = document.asXML();
		logger.warn("del user xml:" + sendXml);
		String returnParam = "";
		try
		{
			Service service = new Service();
			Call call = null;
			call = (Call) service.createCall();
			call.setTargetEndpointAddress(new URL(endPointReference));
			call.setOperationName("call");
			returnParam = (String) call.invoke(new Object[] { sendXml });
			logger.warn("结果：" + returnParam);
		}
		catch (Exception e)
		{
			logger.error("err:" + e.getMessage());
		}
		return returnParam;
	}

	/**
	 *  宽带销户工单
	 */
	public String sendNetStopSheet(String servTypeId, String operateType,
			String dealdate, String username, String cityId, String userType,String userAccount) 
	{
		Document document = DocumentHelper.createDocument();
		document.setXMLEncoding("UTF-8");
		Element root = document.addElement("root");
		root.addElement("cmdId").addText(transferDateFormate(dealdate));
		root.addElement("authUser").addText("1");
		root.addElement("authPwd").addText("1");
		Element param = root.addElement("param");
		param.addElement("dealDate").addText(transferDateFormate(dealdate));
		param.addElement("userType").addText("1");
		param.addElement("loid").addText(username);
		param.addElement("cityId").addText(cityId);
		param.addElement("userName").addText(userAccount);
		root.addElement("operateId").addText("3");
		root.addElement("servTypeId").addText("22");
		
		String sendXml = document.asXML();
		logger.warn("del user xml:" + sendXml);
		String returnParam = "";
		try
		{
			Service service = new Service();
			Call call = null;
			call = (Call) service.createCall();
			call.setTargetEndpointAddress(new URL(endPointReference));
			call.setOperationName("call");
			returnParam = (String) call.invoke(new Object[] { sendXml });
			logger.warn("结果：" + returnParam);
		}
		catch (Exception e)
		{
			logger.error("err:" + e.getMessage());
		}
		return returnParam;
	}
	
	public String sendIptvStopSheet(String servTypeId, String operateType,
			String dealdate, String username, String cityId, String userType,
			 String iptvPort) 
	{
		Document document = DocumentHelper.createDocument();
		document.setXMLEncoding("UTF-8");
		Element root = document.addElement("root");
		root.addElement("cmdId").addText(transferDateFormate(dealdate));
		root.addElement("authUser").addText("1");
		root.addElement("authPwd").addText("1");
		Element param = root.addElement("param");
		param.addElement("dealDate").addText(transferDateFormate(dealdate));
		param.addElement("userType").addText("1");
		param.addElement("loid").addText(username);
		param.addElement("cityId").addText(cityId);
		root.addElement("operateId").addText("3");
		root.addElement("servTypeId").addText("21");
		param.addElement("userName").addText("");
		param.addElement("bindPort").addText(iptvPort.replaceAll(" ", ""));
		String sendXml = document.asXML();
		logger.warn("del iptv xml:" + sendXml);
		String returnParam = "";
		try
		{
			Service service = new Service();
			Call call = null;
			call = (Call) service.createCall();
			call.setTargetEndpointAddress(new URL(endPointReference));
			call.setOperationName("call");
			returnParam = (String) call.invoke(new Object[] { sendXml });
			logger.warn("结果：" + returnParam);
		}
		catch (Exception e)
		{
			logger.error("err:" + e.getMessage());
		}
		return returnParam;
	}

	public String sendNetOpenSheet(String servTypeId, String operateType,
			String dealdate, String username, String cityId,
			String netUsername, String netPassword, String vlanId,
			String userType, String wlantype,String bindPort,String ipType) 
	{
		Document document = DocumentHelper.createDocument();
		document.setXMLEncoding("UTF-8");
		Element root = document.addElement("root");
		root.addElement("cmdId").addText(transferDateFormate(dealdate));
		root.addElement("authUser").addText("1");
		root.addElement("authPwd").addText("1");
		Element param = root.addElement("param");
		param.addElement("dealDate").addText(transferDateFormate(dealdate));
		param.addElement("userType").addText("1");
		param.addElement("loid").addText(username);
		param.addElement("cityId").addText(cityId);
		root.addElement("operateId").addText("1");
		param.addElement("orderNo").addText(transferDateFormate(dealdate));
		root.addElement("servTypeId").addText("22");
		param.addElement("wanType").addText(wlantype);
		param.addElement("vlanId").addText(vlanId);
		if(null == ipType || "-1" == ipType || ipType.isEmpty())
		{
			ipType = "";
		}
		param.addElement("ipType").addText(ipType);
		param.addElement("bindPort").addText(bindPort.replaceAll(" ", ""));
		param.addElement("userName").addText(netUsername);
		param.addElement("password").addText(netPassword);
		String sendXml = document.asXML();
		logger.warn("NET xml:" + sendXml);
		String returnParam = "";
		try
		{
			Service service = new Service();
			Call call = null;
			call = (Call) service.createCall();
			call.setTargetEndpointAddress(new URL(endPointReference));
			call.setOperationName("call");
			returnParam = (String) call.invoke(new Object[] { sendXml });
			logger.warn("结果：" + returnParam);
		}
		catch (Exception e)
		{
			logger.error("err:" + e.getMessage());
		}
		return returnParam;
	
	}

	public String sendH248VoipOpenSheet(String servTypeId, String operateType,
			String dealdate, String username, String cityId, String sipIp,
			String sipPort, String standSipIp, String standSipPort, String linePort,
			String ipaddress, String ipmask, String gateway, String eid) {
		Document document = DocumentHelper.createDocument();
		document.setXMLEncoding("UTF-8");
		Element root = document.addElement("root");
		root.addElement("cmdId").addText(transferDateFormate(dealdate));
		root.addElement("authUser").addText("1");
		root.addElement("authPwd").addText("1");
		Element param = root.addElement("param");
		param.addElement("dealDate").addText(transferDateFormate(dealdate));
		param.addElement("userType").addText("1");
		param.addElement("loid").addText(username);
		param.addElement("cityId").addText(cityId);
		root.addElement("operateId").addText("1");
		param.addElement("orderNo").addText(transferDateFormate(dealdate));
		root.addElement("servTypeId").addText("15");
		param.addElement("mgcIp").addText(sipIp);
		param.addElement("mgcPort").addText(sipPort);
		param.addElement("standMgcIp").addText(standSipIp);
		param.addElement("standMgcPort").addText(standSipPort);
		if(!ipaddress.isEmpty() && !ipmask.isEmpty() && !gateway.isEmpty())
		{
			param.addElement("wanType").addText("3");
		}
		else
		{
			param.addElement("wanType").addText("4");
		}
		param.addElement("ipaddress").addText(ipaddress);
		param.addElement("ipmask").addText(ipmask);
		param.addElement("gateway").addText(gateway);
		param.addElement("voipPort").addText(linePort);
		param.addElement("eid").addText(eid);
		String sendXml = document.asXML();
		logger.info("xml:" + sendXml);
		logger.warn("VOIP xml:" + sendXml);
		String returnParam = "";
		try
		{
			Service service = new Service();
			Call call = null;
			call = (Call) service.createCall();
			call.setTargetEndpointAddress(new URL(endPointReference));
			call.setOperationName("call");
			returnParam = (String) call.invoke(new Object[] { sendXml });
			logger.warn("结果：" + returnParam);
		}
		catch (Exception e)
		{
			logger.error("err:" + e.getMessage());
		}
		return returnParam;
	}

	public String sendVoipStopSheet(String servTypeId, String operateType,
			String dealdate, String username, String cityId,
			String linePort, String userType) {
		Document document = DocumentHelper.createDocument();
		document.setXMLEncoding("UTF-8");
		Element root = document.addElement("root");
		root.addElement("cmdId").addText(transferDateFormate(dealdate));
		root.addElement("authUser").addText("1");
		root.addElement("authPwd").addText("1");
		Element param = root.addElement("param");
		param.addElement("dealDate").addText(transferDateFormate(dealdate));
		param.addElement("userType").addText("1");
		param.addElement("loid").addText(username);
		param.addElement("cityId").addText(cityId);
		root.addElement("operateId").addText("3");
		root.addElement("servTypeId").addText("15");
		param.addElement("voipPort").addText(linePort);
		String sendXml = document.asXML();
		logger.warn("del voip xml:" + sendXml);
		String returnParam = "";
		try
		{
			Service service = new Service();
			Call call = null;
			call = (Call) service.createCall();
			call.setTargetEndpointAddress(new URL(endPointReference));
			call.setOperationName("call");
			returnParam = (String) call.invoke(new Object[] { sendXml });
			logger.warn("结果：" + returnParam);
		}
		catch (Exception e)
		{
			logger.error("err:" + e.getMessage());
		}
		return returnParam;
	}

	public String sendIptvOpenSheet(String servTypeId, String operateType,
			String dealdate, String username, String cityId, String iptvVlanId,
			String iptvPort, String userType, String oltFactory, String orderNo) 
	{
		Document document = DocumentHelper.createDocument();
		document.setXMLEncoding("UTF-8");
		Element root = document.addElement("root");
		root.addElement("cmdId").addText(transferDateFormate(dealdate));
		root.addElement("authUser").addText("1");
		root.addElement("authPwd").addText("1");
		Element param = root.addElement("param");
		param.addElement("dealDate").addText(transferDateFormate(dealdate));
		param.addElement("userType").addText("1");
		param.addElement("loid").addText(username);
		param.addElement("cityId").addText(cityId);
		root.addElement("operateId").addText("1");
		param.addElement("orderNo").addText(orderNo);
		root.addElement("servTypeId").addText("21");
		param.addElement("wanType").addText("1");
		param.addElement("vlanId").addText(iptvVlanId);
		param.addElement("bindPort").addText(iptvPort.replaceAll(" ", ""));
		param.addElement("oltFactory").addText(oltFactory);
		String sendXml = document.asXML();
		logger.warn("IPTV xml:" + sendXml);
		String returnParam = "";
		try
		{
			Service service = new Service();
			Call call = null;
			call = (Call) service.createCall();
			call.setTargetEndpointAddress(new URL(endPointReference));
			call.setOperationName("call");
			returnParam = (String) call.invoke(new Object[] { sendXml });
			logger.warn("结果：" + returnParam);
		}
		catch (Exception e)
		{
			logger.error("err:" + e.getMessage());
		}
		return returnParam;
	}
	   
	
	public String sendSIPCloseSheet(String servTypeId, String operateType, String dealdate,String userType,
			String  username,String cityId,String linePort)
	{
		Document document = DocumentHelper.createDocument();
		document.setXMLEncoding("UTF-8");
		Element root = document.addElement("root");
		root.addElement("cmdId").addText(transferDateFormate(dealdate));
		root.addElement("authUser").addText("1");
		root.addElement("authPwd").addText("1");
		Element param = root.addElement("param");
		param.addElement("dealDate").addText(transferDateFormate(dealdate));
		param.addElement("userType").addText("1");
		param.addElement("loid").addText(username);
		param.addElement("cityId").addText(cityId);
		root.addElement("operateId").addText("3");
		root.addElement("servTypeId").addText("14");
		param.addElement("voipPort").addText(linePort);
		String sendXml = document.asXML();
		logger.warn("sip xml:" + sendXml);
		String returnParam = "";
		try
		{
			Service service = new Service();
			Call call = null;
			call = (Call) service.createCall();
			call.setTargetEndpointAddress(new URL(endPointReference));
			call.setOperationName("call");
			returnParam = (String) call.invoke(new Object[] { sendXml });
			logger.warn("结果：" + returnParam);
		}
		catch (Exception e)
		{
			logger.error("err:" + e.getMessage());
		}
		return returnParam;
	}
	
	public String sendSIPUpdateSheet(String servTypeId, String operateType, String dealdate,String userType,
			String username,String cityId,String linePort,String voipAccount, String voipPassword, String voipOldAccount,
			String voipUri, String agreementType)
	{
		Document document = DocumentHelper.createDocument();
		document.setXMLEncoding("UTF-8");
		Element root = document.addElement("root");
		root.addElement("cmdId").addText(transferDateFormate(dealdate));
		root.addElement("authUser").addText("1");
		root.addElement("authPwd").addText("1");
		Element param = root.addElement("param");
		param.addElement("dealDate").addText(transferDateFormate(dealdate));
		param.addElement("userType").addText("1");
		param.addElement("loid").addText(username);
		param.addElement("cityId").addText(cityId);
		root.addElement("operateId").addText("2");
		root.addElement("servTypeId").addText("14");
		param.addElement("voipPort").addText(linePort);
		param.addElement("voipUsername").addText(voipAccount);
		param.addElement("voipPwd").addText(voipPassword);
		param.addElement("voipOldUsername").addText(voipOldAccount);
		param.addElement("voipUri").addText(voipUri); 
		param.addElement("protocol").addText(agreementType);
		String sendXml = document.asXML();
		logger.warn("sip xml:" + sendXml);
		String returnParam = "";
		try
		{
			Service service = new Service();
			Call call = null;
			call = (Call) service.createCall();
			call.setTargetEndpointAddress(new URL(endPointReference));
			call.setOperationName("call");
			returnParam = (String) call.invoke(new Object[] { sendXml });
			logger.warn("结果：" + returnParam);
		}
		catch (Exception e)
		{
			logger.error("err:" + e.getMessage());
		}
		return returnParam;
	}
	
	public String sendSIPOpenSheet(String servTypeId, String operateType,String dealdate, String userType,
			String username, String bussinessTel, String cityId,
			String voipAccount, String voipPassword, String ProxyServer,
			String ProxyServerPort, String ProxyServer_a,String ProxyServerPort_a, String voiceport,
			String RegistrarServer, String RegistrarServerPort,String RegistrarServer_a, String RegistrarServerPort_a,
			String OutboundProxy, String OutboundProxyPort,String OutboundProxy_a, String OutboundProxyPort_a,
			String agreementType, String ipaddress,String code,
			String netway, String voipUri) 
	{
		Document document = DocumentHelper.createDocument();
		document.setXMLEncoding("UTF-8");
		Element root = document.addElement("root");
		root.addElement("cmdId").addText(transferDateFormate(dealdate));
		root.addElement("authUser").addText("1");
		root.addElement("authPwd").addText("1");
		Element param = root.addElement("param");
		param.addElement("dealDate").addText(transferDateFormate(dealdate));
		param.addElement("userType").addText("1");
		param.addElement("loid").addText(username);
		param.addElement("cityId").addText(cityId);
		root.addElement("operateId").addText("1");
		root.addElement("servTypeId").addText("14");
		param.addElement("wanType").addText("1");
		param.addElement("voipUsername").addText(voipAccount);
		param.addElement("voipPwd").addText(voipPassword);
		param.addElement("proxServ").addText(ProxyServer);
		param.addElement("proxPort").addText(ProxyServerPort);
		param.addElement("standProxServ").addText(ProxyServer_a);
		param.addElement("standProxPort").addText(ProxyServerPort_a);
		param.addElement("voipPort").addText(voiceport);
		param.addElement("regiServ").addText(RegistrarServer);
		param.addElement("regiPort").addText(RegistrarServerPort);
		param.addElement("standRegiServ").addText(RegistrarServer_a);
		param.addElement("standRegiPort").addText(RegistrarServerPort_a);
		param.addElement("outBoundProxy").addText(OutboundProxy);
		param.addElement("outBoundPort").addText(OutboundProxyPort);
		param.addElement("standOutBoundProxy").addText(OutboundProxy_a);
		param.addElement("standOutBoundPort").addText(OutboundProxyPort_a);
		param.addElement("protocol").addText(agreementType);
		if(!ipaddress.isEmpty() && !code.isEmpty() && !netway.isEmpty())
		{
			param.addElement("wanType").addText("3");
		}
		else
		{
			param.addElement("wanType").addText("4");
		}
		param.addElement("ipaddress").addText(ipaddress);
		param.addElement("ipmask").addText(code);
		param.addElement("gateway").addText(netway);
		param.addElement("voipUri").addText(voipUri);
		String sendXml = document.asXML();
		logger.warn("sip xml:" + sendXml);
		String returnParam = "";
		try
		{
			Service service = new Service();
			Call call = null;
			call = (Call) service.createCall();
			call.setTargetEndpointAddress(new URL(endPointReference));
			call.setOperationName("call");
			returnParam = (String) call.invoke(new Object[] { sendXml });
			logger.warn("结果：" + returnParam);
		}
		catch (Exception e)
		{
			logger.error("err:" + e.getMessage());
		}
		return returnParam;
	}

	private String transferDateFormate(String dealdate) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;
		try {
			date = dateFormat.parse(dealdate);
		} catch (ParseException e) {
			e.printStackTrace();
			logger.warn("工单受理时间转换异常");
		}
		dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		return dateFormat.format(date);
	}

	public String sendSheet(String bssSheet) {
		logger.debug("sendSheet({})", bssSheet);
		if (StringUtil.IsEmpty(bssSheet)) {
			logger.warn("sendSheet is null");
			return null;
		}
		String server = Global.G_ITMS_Sheet_Server;
		int port = Global.G_ITMS_Sheet_Port;
		String retResult = SocketUtil
				.sendStrMesg(server, port, bssSheet + "\n");
		if (null == retResult || "".equals(retResult)) {
			return "";
		}
		return retResult;
	}
	
	public SimulateSheetSxNewDAO getDao()
	{
		return dao;
	}
	
	public void setDao(SimulateSheetSxNewDAO dao)
	{
		this.dao = dao;
	}

	public static Logger getLogger() {
		return logger;
	}
	
	// 获取dealtime
	@SuppressWarnings({ "deprecation", "unused" })
	private static String getDealTime()
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		String dstr = "";
		Date d = new Date();
		dstr += sdf.format(d);
		if (d.getMonth() + 1 < 10)
		{
			dstr += '0';
		}
		dstr += d.getMonth() + 1;
		if (d.getDate() < 10)
		{
			dstr += '0';
		}
		dstr += d.getDate();
		if (d.getHours() < 10)
		{
			dstr += '0';
		}
		dstr += d.getHours();
		if (d.getMinutes() < 10)
		{
			dstr += '0';
		}
		dstr += d.getMinutes() + "00";
		return dstr;
	}
	
}
