
package com.linkage.module.ids.bio;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import org.apache.struts2.components.Else;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.StringUtil;
import com.linkage.litms.Global;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.ids.dao.VoiceDeviceQueryDAO;
import com.linkage.module.ids.util.WSClientUtil;

/**
 * @author Administrator (Ailk No.)
 * @version 1.0
 * @since 2013-10-18
 * @category com.linkage.module.ids.bio
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class VoiceDeviceQueryBIO
{
	private static Logger logger = LoggerFactory.getLogger(VoiceDeviceQueryBIO.class);
	private VoiceDeviceQueryDAO dao;
	private Map<String, String> rstCodeMap = new HashMap<String, String>();
	public static final String JLLT="jl_lt";
	
	public VoiceDeviceQueryBIO()
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
		sb.append(StringUtil.getStringValue(map.get("column4"))).append("#");
		sb.append(StringUtil.getStringValue(map.get("column5"))).append("#");
		sb.append(StringUtil.getStringValue(map.get("column6")));
		return sb.toString();
	}

	public Map<String, String> queryVoiceService(String city_id, String oui,
			String device_id, String column1, String column2, String column3,
			String column4)
	{
		Map<String, String> map = new HashMap<String, String>();
		DateTimeUtil dt = new DateTimeUtil();
		String cmdId = StringUtil.getStringValue(dt.getLongTime());
		StringBuffer inParam = new StringBuffer();
		inParam.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>	\n");
		inParam.append("<root>										\n");
		inParam.append("	<CmdID>").append(cmdId).append("</CmdID >						\n");
		inParam.append("	<CmdType>CX_01</CmdType>					\n");
		inParam.append("	<ClientType>5</ClientType>						\n");
		inParam.append("	<Param>									\n");
		inParam.append("		<DevSn>").append(device_id).append("</DevSn>		\n");
		inParam.append("        <OUI>").append(oui).append("</OUI>     \n");
		inParam.append("		<CityId>").append(city_id).append("</CityId>	\n");
		inParam.append("		<TestType>").append(column1).append("</TestType>		\n");
		inParam.append("		<CalledNumber>").append(column2).append("</CalledNumber>		\n");
		inParam.append("		<DialDTMFConfirmEnable>").append(column3).append("</DialDTMFConfirmEnable>		\n");
		inParam.append("		<DialDTMFConfirmNumber>").append(column4).append("</DialDTMFConfirmNumber>		\n");
		inParam.append("	</Param>								\n");
		inParam.append("</root>										\n");
		logger.warn("voice:" + inParam.toString());
		final String url = LipossGlobals.getLipossProperty("IDSServiceUri");
		String callBack = WSClientUtil.callItmsService(url, inParam.toString(),
				"VoiceDial");
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
				String devSn = root.elementTextTrim("DevSn");
				String testType = root.elementTextTrim("TestType");
				String calledNumber = root.elementTextTrim("CalledNumber");
				String dialDTMFConfirmEnable = root
						.elementTextTrim("DialDTMFConfirmEnable");
				String status = root.elementTextTrim("Status");
				String conclusion = root.elementTextTrim("Conclusion");
				String dialDTMFConfirmResult = root
						.elementTextTrim("DialDTMFConfirmResult");
				String callerFailReason = root.elementTextTrim("CallerFailReason");
				String failedResponseCode = root.elementTextTrim("FailedResponseCode");
				DateTimeUtil dd = new DateTimeUtil();
				// 入库
				int num = dao.addVoiceDiagResult(oui, device_id, CmdID, testType,
						calledNumber, dialDTMFConfirmEnable, column4,
						dialDTMFConfirmResult, status, conclusion, callerFailReason,
						failedResponseCode);
				map.put("DevSn", devSn);
				map.put("TestType", testType);
				map.put("CalledNumber", calledNumber);
				if(JLLT.equals(com.linkage.module.gwms.Global.instAreaShortName))
				{
					if("1".equals(dialDTMFConfirmEnable))
					{
						map.put("DialDTMFConfirmEnable", "开启");
						if("1".equals(dialDTMFConfirmResult))
						{
							map.put("DialDTMFConfirmResult", "成功");
						}
						else
						{
							map.put("DialDTMFConfirmResult", "失败");
						}
					}
					else
					{
						map.put("DialDTMFConfirmEnable", "关闭");
						map.put("DialDTMFConfirmResult", "关闭");
					}
					
				}
				else {
					map.put("DialDTMFConfirmEnable", dialDTMFConfirmEnable);
					map.put("DialDTMFConfirmResult", dialDTMFConfirmResult);
				}
				map.put("Status", status);
				map.put("Conclusion", conclusion);
				
				map.put("CallerFailReason", callerFailReason);
				map.put("FailedResponseCode", failedResponseCode);
			}
			else
			{
				map.put("code", "1");
			}
		}
		catch (DocumentException e)
		{
			map.put("code", "1");
			map.put("errMessage", "解析PPPoE通道返回值有误!");
		}
		logger.warn("map:" + map.toString());
		return map;
	}

	public VoiceDeviceQueryDAO getDao()
	{
		return dao;
	}

	public void setDao(VoiceDeviceQueryDAO dao)
	{
		this.dao = dao;
	}
}
