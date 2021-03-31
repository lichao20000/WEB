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

import com.linkage.litms.LipossGlobals;
import com.linkage.module.ids.dao.VoiceQualityMonitoringDAO;
import com.linkage.module.ids.util.WSClientUtil;

public class VoiceQualityMonitoringBIO {

	// 日志记录
	private static Logger logger = LoggerFactory.getLogger(VoiceQualityMonitoringBIO.class);
	private  VoiceQualityMonitoringDAO dao;
	
	public Map<String, String> queryVoiceMonitoring(String oui,String devSn,String cityId)
	{
		Map<String, String> map = new HashMap<String, String>();
		StringBuffer inParam = new StringBuffer();
		inParam.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>			\n");
		inParam.append("<root>												\n");
		inParam.append("	<CmdID>123456789012345</CmdID >					\n");
		inParam.append("	<CmdType>CX_01</CmdType>						\n");
		inParam.append("	<ClientType>3</ClientType>						\n");
		inParam.append("	<Param>											\n");
		inParam.append("        <OUI>").append(oui).append("</OUI>  		\n");
		inParam.append("		<DevSn>").append(devSn).append("</DevSn>	\n");
		inParam.append("		<CityId>").append(cityId).append("</CityId>	\n");
		inParam.append("	</Param>										\n");
		inParam.append("</root>												\n");
		logger.warn("VoiceMonitoring:" + inParam.toString());
		final String url = LipossGlobals.getLipossProperty("IDSServiceUri");
		String callBack = WSClientUtil.callItmsService(url, inParam.toString(),
				"voiceQuality");
		logger.warn("回参：" + callBack);
		// 解析回参
		SAXReader reader = new SAXReader();
		Document document = null;
		try
		{
			document = reader.read(new StringReader(callBack));
			Element root = document.getRootElement();
			String rstCode = root.elementTextTrim("RstCode");
			String RstMsg = root.elementTextTrim("RstMsg");
			map.put("errMessage", RstMsg);
			if ("0".equals(rstCode))
			{	
				map.put("code", "0");
				map.put("RstCode", rstCode);
				map.put("RstMsg", RstMsg);
				map.put("CmdID", root.elementTextTrim("CmdID"));
				map.put("DevSn", root.elementTextTrim("DevSn"));
				map.put("StatTime", root.elementTextTrim("StatTime"));
				map.put("TxPackets", root.elementTextTrim("TxPackets"));
				map.put("RxPackets", root.elementTextTrim("RxPackets"));
				map.put("MeanDelay", root.elementTextTrim("MeanDelay"));
				map.put("MeanJitter", root.elementTextTrim("MeanJitter"));
				map.put("FractionLoss", root.elementTextTrim("FractionLoss"));
				map.put("LocalIPAddress", root.elementTextTrim("LocalIPAddress"));
				map.put("LocalUDPPort", root.elementTextTrim("LocalUDPPort"));
				map.put("FarEndIPAddress", root.elementTextTrim("FarEndIPAddress"));
				map.put("FarEndUDPPort", root.elementTextTrim("FarEndUDPPort"));
				map.put("MosLq", root.elementTextTrim("MosLq"));
				map.put("Codec", root.elementTextTrim("Codec"));
				//入库
				int num = dao.addVoiceQualityResult(oui,devSn,map);
			}
			else
			{
				map.put("code", "1");
			}
		}
		catch (DocumentException e)
		{
			map.put("code", "1");
			map.put("errMessage", "终端返回失败!");
		}
		logger.warn("map:" + map.toString());
		return map;
	}

	
	public VoiceQualityMonitoringDAO getDao()
	{
		return dao;
	}

	
	public void setDao(VoiceQualityMonitoringDAO dao)
	{
		this.dao = dao;
	}

	
}
