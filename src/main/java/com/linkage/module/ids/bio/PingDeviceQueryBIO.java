
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
import com.linkage.module.gwms.Global;
import com.linkage.module.ids.dao.PingDeviceQueryDAO;
import com.linkage.module.ids.util.WSClientUtil;

/**
 * @author Administrator (Ailk No.)
 * @version 1.0
 * @since 2013-10-18
 * @category com.linkage.module.ids.bio
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class PingDeviceQueryBIO
{

	// 日志记录
	private static Logger logger = LoggerFactory.getLogger(PingDeviceQueryBIO.class);
	private PingDeviceQueryDAO dao;
	private Map<String, String> rstCodeMap = new HashMap<String, String>();

	public PingDeviceQueryBIO()
	{
		rstCodeMap.put("0", "成功");
		rstCodeMap.put("1", "数据格式错误");
		rstCodeMap.put("2", "客户端类型非法");
		rstCodeMap.put("3", "接口类型非法");
		rstCodeMap.put("1000", "未知错误");
		rstCodeMap.put("1001", "设备序列号不合法");
		rstCodeMap.put("1001", "属地非法");
		rstCodeMap.put("1003", "厂商OUI不能为空");
		rstCodeMap.put("1004", "设备不存在");
		rstCodeMap.put("1005", "设备未知错误");
		rstCodeMap.put("1007", "系统内部错误");
	}

	public String getDefaultdiag()
	{
		StringBuffer sb = new StringBuffer();
		Map<String, String> map = dao.getDefaultdiag();
		sb.append(StringUtil.getStringValue(map.get("column1"))).append("#");
		sb.append(StringUtil.getStringValue(map.get("column2"))).append("#");
		sb.append(StringUtil.getStringValue(map.get("column3"))).append("#");
		sb.append(StringUtil.getStringValue(map.get("column4")));
		return sb.toString();
	}

	public Map<String, String> queryPingService(String city_id, String oui,
			String device_id, String wanType, String column1, String column2,
			String column3, String column4, String wan_interface)
	{
		Map<String, String> map = new HashMap<String, String>();
		DateTimeUtil dt = new DateTimeUtil();
		String cmdId = StringUtil.getStringValue(dt.getLongTime());
		
		//通道值，传输过来的格式为：通道值+“$”+vlanid
		String wt = "";
		String vlanid ="";
		if(!StringUtil.IsEmpty(wanType)){
			String[] arr = wanType.split("￥");
			wt = arr[0];
			vlanid = arr[1];
		}
		StringBuffer inParam = new StringBuffer();
		inParam.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>	\n");
		inParam.append("<root>										\n");
		inParam.append("	<IsWebInvoke>").append("true").append("</IsWebInvoke>\n");//目前pingDiagnostic ping测试接口有web页面诊断和提供给外部系统诊断，增加IsWebInvoke标签区分是否从web页面测试的
		inParam.append("	<CmdID>").append(cmdId).append("</CmdID >						\n");
		inParam.append("	<CmdType>CX_01</CmdType>					\n");
		inParam.append("	<ClientType>5</ClientType>						\n");
		inParam.append("	<Param>									\n");
		inParam.append("        <OUI>").append(oui).append("</OUI>      \n");
		inParam.append("		<DevSn>").append(device_id).append("</DevSn>		\n");
		inParam.append("		<CityId>").append(city_id).append("</CityId>	\n");
		inParam.append("		<WanPassageWay>").append(wt)
				.append("</WanPassageWay>		\n");
		inParam.append("		<PackageByte>").append(column1).append("</PackageByte>		\n");
		inParam.append("		<IPOrDomainName>").append(column2)
				.append("</IPOrDomainName>		\n");
		inParam.append("		<PackageNum>").append(column3).append("</PackageNum>		\n");
		inParam.append("		<TimeOut>").append(column4).append("</TimeOut>		\n");
		inParam.append("	</Param>								\n");
		inParam.append("</root>										\n");
		
		logger.warn("pingXML:" + inParam.toString());
		final String url = LipossGlobals.getLipossProperty("IDSServiceUri");
		String callBack = WSClientUtil.callItmsService(url, inParam.toString(),
				"pingDiagnostic");
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
			String CmdID = root.elementTextTrim("CmdID");
			map.put("errMessage", RstMsg);
			if ("0".equals(rstCode))
			{
				map.put("code", "0");
				String DevSn = root.elementTextTrim("DevSn");
				String SuccesNum = root.elementTextTrim("SuccesNum");
				String FailNum = root.elementTextTrim("FailNum");
				String AvgResponseTime = root.elementTextTrim("AvgResponseTime");
				String MinResponseTime = root.elementTextTrim("MinResponseTime");
				String MaxResponseTime = root.elementTextTrim("MaxResponseTime");
				String PacketLossRate = root.elementTextTrim("PacketLossRate");
				String IPOrDomainName = root.elementTextTrim("IPOrDomainName");
				// 入库
				int num = dao.addPingDiagResult(oui, DevSn, CmdID, wan_interface,vlanid,column1, column2,column3, column4, rstCode,SuccesNum,FailNum,AvgResponseTime,MinResponseTime,MaxResponseTime,PacketLossRate);
				if (num == 1)
				{
					map.put("DevSn", DevSn);
					map.put("SuccesNum", SuccesNum);
					map.put("FailNum", FailNum);
					map.put("AvgResponseTime", AvgResponseTime);
					map.put("MinResponseTime", MinResponseTime);
					map.put("MaxResponseTime", MaxResponseTime);
					map.put("PacketLossRate", PacketLossRate);
					map.put("IPOrDomainName", IPOrDomainName);
				}
			}
			else
			{
				map.put("code", "1");
			//	int num = dao.addPingDiagResult(oui, "4430060DE44D4017C", CmdID, wan_interface,vlanid,column1, column2,column3, column4, rstCode,"2","3","23","11","35","56");
				logger.warn("Ping通道返回状态："+RstMsg);
			}
		}
		catch (DocumentException e)
		{
			map.put("code", "1");
			map.put("errMessage","解析Ping通道返回值有误!");
		}
		logger.warn("map:"+map.toString());
		return map;
	}


	public PingDeviceQueryDAO getDao()
	{
		return dao;
	}

	public void setDao(PingDeviceQueryDAO dao)
	{
		this.dao = dao;
	}
}
