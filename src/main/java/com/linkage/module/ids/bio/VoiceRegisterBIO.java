package com.linkage.module.ids.bio;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.ids.dao.VoiceRegisterDAO;
import com.linkage.module.ids.util.WSClientUtil;

public class VoiceRegisterBIO {
	private static Logger logger = LoggerFactory
			.getLogger(VoiceRegisterBIO.class);
	
	private VoiceRegisterDAO dao = null;
	
	public VoiceRegisterDAO getDao() {
		return dao;
	}
	public void setDao(VoiceRegisterDAO dao) {
		this.dao = dao;
	}
	
	public Map<String, String> queryVoiceRegister(String oui,String deviceno, String city_id,
			String servicesip) {
		Map<String, String> map = new HashMap<String, String>();
		DateTimeUtil dt = new DateTimeUtil();
		String cmdId = StringUtil.getStringValue(dt.getLongTime());
		StringBuffer inParam = new StringBuffer();
		inParam.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>	\n");
		inParam.append("<root>										\n");
		inParam.append("	<CmdID>").append(cmdId).append("</CmdID>						\n");
		inParam.append("	<CmdType>CX_01</CmdType>					\n");
		inParam.append("	<ClientType>5</ClientType>						\n");
		inParam.append("	<Param>									\n");
		inParam.append("		<DevSn>").append(deviceno).append("</DevSn>		\n");
		inParam.append("		<OUI>").append(oui).append("</OUI>		\n");
		inParam.append("		<CityId>").append(city_id).append("</CityId>		\n");
		inParam.append("        <RegisterServer>").append(servicesip).append("</RegisterServer>      \n");
		inParam.append("	</Param>								\n");
		inParam.append("</root>										\n");
		logger.warn(inParam.toString());
		final String url = LipossGlobals.getLipossProperty("IDSServiceUri");
		String callBack =  WSClientUtil.callItmsService(url, inParam.toString(), "voiceServicesRegister");
		logger.warn(callBack.toString());
		SAXReader reader = new SAXReader();
		Document document = null;
		try {
			document = reader.read(new StringReader(callBack));
			Element element = document.getRootElement();
			String DiagnosticResultValue ="";
			String reason ="";
			String server = "";
			String CmdID = element.elementTextTrim("CmdID");
			String RstCode = element.elementTextTrim("RstCode");
			String RstMsg = element.elementTextTrim("RstMsg");
			String DevSn = element.elementTextTrim("DevSn");
			String RegisterServer = element.elementTextTrim("RegisterServer");
			String DiagnosticResult = element.elementTextTrim("DiagnosticResult");
			
			if (DiagnosticResult.equals("0")) {
				DiagnosticResultValue="注册成功";
			}else {
				DiagnosticResultValue="注册失败";
			}
			String DiagnosticReason = element.elementTextTrim("DiagnosticReason");
			if (DiagnosticReason.equals("1")) {
				reason="IAD模块错误";
			}
			if (DiagnosticReason.equals("2")) {
				reason="访问路由不通";
			}
			if (DiagnosticReason.equals("3")) {
				reason="访问服务器无响应";
			}
			if (DiagnosticReason.equals("4")) {
				reason="帐号、密码错误";
			}
			if (DiagnosticReason.equals("5")) {
				reason="未知错误";
			}
			map.put("DevSn", DevSn);
			if (RegisterServer.equals("1")) {
				server="主用服务器";
			}else {
				server="备用服务器";
			}
			map.put("RegisterServer", server);
			map.put("DiagnosticReason", reason);
			map.put("RstMsg", RstMsg);
			map.put("CmdID", CmdID);
			map.put("RstCode", RstCode);
			map.put("DiagnosticResult", DiagnosticResultValue);
			if ("0".equals(RstCode)) {
				int count = dao.insertVoiceRegister(oui, DevSn, CmdID, RegisterServer,DiagnosticResult,DiagnosticReason,RstCode);
				if (count > 0 ) {
					logger.warn("入库成功");
				}else {
					logger.warn("入库失败");
				}
			}else {
				//int count = dao.insertVoiceRegister(oui, DevSn, CmdID, RegisterServer,DiagnosticResult,DiagnosticReason,RstCode);
				logger.warn("语音仿真注册失败："+RstMsg);
			}
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return map;
	}
	
}
