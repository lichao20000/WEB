package com.linkage.module.ids.bio;

import java.io.StringReader;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.ids.dao.HTTPDeviceQueryDAO;
import com.linkage.module.ids.util.WSClientUtil;

/**
 * 
 * @author Administrator (Ailk No.)
 * @version 1.0
 * @since 2013-10-18
 * @category com.linkage.module.ids.bio
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 *
 */
public class HTTPDeviceQueryBIO
{
	// 日志记录
			private static Logger logger = LoggerFactory
					.getLogger(HTTPDeviceQueryBIO.class);
	
	private  HTTPDeviceQueryDAO dao;
	
	private Map<String,String> rstCodeMap = new HashMap<String, String>();
	public HTTPDeviceQueryBIO(){
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
	
	public String getDefaultdiag(){
		StringBuffer sb = new StringBuffer();
		Map<String,String> map = dao.getDefaultdiag();
		sb.append(StringUtil.getStringValue(map.get("column1"))).append("#");
		sb.append(StringUtil.getStringValue(map.get("column2")));
		return sb.toString();
	}
	
	
	
	
	public Map<String,String> queryHTTPService4SD(String device_id, String testType){
		Map<String,String> map = new HashMap<String, String>();
		DateTimeUtil dt = new DateTimeUtil();
		String cmdId = StringUtil.getStringValue(dt.getLongTime());
		StringBuffer inParam = new StringBuffer();
		inParam.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>	\n");
		inParam.append("<root>										\n");
		inParam.append("	<CmdID>").append(cmdId).append("</CmdID>						\n");
		inParam.append("	<CmdType>CX_01</CmdType>					\n");
		inParam.append("	<Param>									\n");
		inParam.append("		<DevSn>").append(device_id).append("</DevSn>		\n");
		inParam.append("        <TestType>").append(testType).append("</TestType>      \n");
		inParam.append("	</Param>								\n");
		inParam.append("</root>										\n");
		logger.warn("http:"+inParam.toString());
		final String url = LipossGlobals.getLipossProperty("ItmsServiceUri");
		String methodName = "testSpeed4SDLT";
		logger.warn("测速url=============="+url);
		String callBack =  WSClientUtil.callItmsService(url, inParam.toString(), methodName);
		
		logger.warn("queryHTTPService4SD回参："+callBack);
		//解析回参
		SAXReader reader = new SAXReader();
		Document document = null;
		try {
			document = reader.read(new StringReader(callBack));
			Element root = document.getRootElement();
			String  rstCode = root.elementTextTrim("RstCode");
			String RstMsg = root.elementTextTrim("RstMsg");
			String CmdID = root.elementTextTrim("CmdID");
			map.put("errMessage", RstMsg);
			if("0".equals(rstCode)){
				map.put("code", "0");
				String DevSn =  root.elementTextTrim("DevSn");
				String Aspeed =  root.elementTextTrim("Aspeed");
				String maxspeed =  root.elementTextTrim("maxspeed");
				String cityName =  root.elementTextTrim("cityName");
				String deviceModel =  root.elementTextTrim("deviceModel");
				String deviceVendor =  root.elementTextTrim("deviceVendor");
				String softwareversion =  root.elementTextTrim("softwareversion");
				String loid =  root.elementTextTrim("loid");
				String userName =  root.elementTextTrim("userName");
				
				map.put("DevSn", StringUtil.IsEmpty(DevSn)?"--":DevSn);
				map.put("Aspeed", StringUtil.IsEmpty(Aspeed)?"--":Aspeed);
				map.put("maxspeed", StringUtil.IsEmpty(maxspeed)?"--":maxspeed);
				map.put("cityName", StringUtil.IsEmpty(cityName)?"--":cityName);
				map.put("deviceModel", StringUtil.IsEmpty(deviceModel)?"--":deviceModel);
				map.put("deviceVendor", StringUtil.IsEmpty(deviceVendor)?"--":deviceVendor);
				map.put("softwareversion", StringUtil.IsEmpty(softwareversion)?"--":softwareversion);
				map.put("loid", StringUtil.IsEmpty(loid)?"--":loid);
				map.put("userName", StringUtil.IsEmpty(userName)?"--":userName);
				logger.warn("ItmsService传回SD测速结果map:"+map);
			}
			else{
				map.put("code", "1");
			}
		} catch (DocumentException e) {
			map.put("code", "1");
			map.put("errMessage", "解析PPPoE通道返回值有误!");
			logger.warn("解析PPPoE通道返回值有误!");
		}
		return map;
	} 
	
	
	
	
	public Map<String,String>  queryHTTPService(String city_id,String oui,String device_id,String wanType,String wan_interface,
			                                    String column1,String column2,String userName,String password,String connType, String speed, 
			                                    String pppoeUserName,String idsShare_queryType,String downlink,String loids,
			                                    String netUser,String acc_loginname){
		 
		Map<String,String> map = new HashMap<String, String>();
			StringBuffer call = new StringBuffer();
			//通道值，传输过来的格式为：通道值+“$”+vlanid
			String wt = "";
			String vlanid =" ";
			String devId = "";
			if(!StringUtil.IsEmpty(wanType)){
				String[] arr = wanType.split("￥");
				wt = arr[0];
				vlanid = arr[1];
			}
			if(LipossGlobals.inArea(Global.JXDX) && !StringUtil.IsEmpty(idsShare_queryType) && "httpSpeed".equals(idsShare_queryType)){
				try
				{
					devId = device_id.split("#")[0];
					device_id = device_id.split("#")[1];
				}
				catch (Exception e)
				{
					logger.error("江西测速queryHTTPService失败,msgs:{}",e.getMessage());
					e.printStackTrace();
					map.put("code", "1");
					map.put("errMessage", "查询设备信息异常");
					return map;
				}
			}
			DateTimeUtil dt = new DateTimeUtil();
			String cmdId = StringUtil.getStringValue(dt.getLongTime());
			StringBuffer inParam = new StringBuffer();
			inParam.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>	\n");
			inParam.append("<root>										\n");
			inParam.append("	<CmdID>").append(cmdId).append("</CmdID>						\n");
			inParam.append("	<CmdType>CX_01</CmdType>					\n");
			inParam.append("	<ClientType>5</ClientType>						\n");
			inParam.append("	<Param>									\n");
			inParam.append("		<DevSn>").append(device_id).append("</DevSn>		\n");
			inParam.append("        <OUI>").append(oui).append("</OUI>      \n");
			inParam.append("		<CityId>").append(city_id).append("</CityId>	\n");
			inParam.append("		<WanPassageWay>").append(wt).append("</WanPassageWay>		\n");
			inParam.append("		<DownURL>").append(column1).append("</DownURL>		\n");
			inParam.append("		<Priority>").append(column2).append("</Priority>		\n");
			inParam.append("		<UserName>").append(userName).append("</UserName>		\n");
			inParam.append("		<Password>").append(password).append("</Password>		\n");
			if(LipossGlobals.inArea(Global.HBLT)){
				inParam.append("		<ConnType>").append(connType).append("</ConnType>		\n");
				inParam.append("		<PppoeUserName>").append(pppoeUserName).append("</PppoeUserName>		\n");
			}
			inParam.append("	</Param>								\n");
			inParam.append("</root>										\n");
			logger.warn("http:"+inParam.toString());
			final String url = LipossGlobals.getLipossProperty("IDSServiceUri");
			//默认按照路由模式，安徽桥接走另外的方法
			String methodName = "IP_Routed".equals(connType)?"downLoadByHTTP":"downLoadByHTTPForAH";
			if(LipossGlobals.inArea(Global.HBLT)){
				methodName = "testSpeed4HBLT";
			}
			String callBack =  WSClientUtil.callItmsService(url, inParam.toString(), methodName);
			
			logger.warn("回参："+callBack);
			//解析回参
			SAXReader reader = new SAXReader();
			Document document = null;
			try {
				document = reader.read(new StringReader(callBack));
				Element root = document.getRootElement();
				String  rstCode = root.elementTextTrim("RstCode");
				String RstMsg = root.elementTextTrim("RstMsg");
				String CmdID = root.elementTextTrim("CmdID");
				map.put("errMessage", RstMsg);
				if("0".equals(rstCode)){
					map.put("code", "0");
					if(LipossGlobals.inArea(Global.HBLT)){
						String DevSn =  root.elementTextTrim("DevSn");
						String diagnosticsState =  root.elementTextTrim("diagnosticsState");
						String pppoeName =  root.elementTextTrim("pppoeName");
						String pppoeIP =  root.elementTextTrim("pppoeIP");
						String Aspeed =  root.elementTextTrim("Aspeed");
						String Bspeed =  root.elementTextTrim("Bspeed");
						String maxspeed =  root.elementTextTrim("maxspeed");
						String starttime =  root.elementTextTrim("starttime");
						String endtime =  root.elementTextTrim("endtime");
						map.put("DevSn", StringUtil.IsEmpty(DevSn)?"":DevSn);
						map.put("diagnosticsState", StringUtil.IsEmpty(diagnosticsState)?"":diagnosticsState);
						map.put("pppoeName", StringUtil.IsEmpty(pppoeName)?"":pppoeName);
						map.put("pppoeIP", StringUtil.IsEmpty(pppoeIP)?"":pppoeIP);
						map.put("Aspeed", StringUtil.IsEmpty(Aspeed)?"":Aspeed);
						map.put("Bspeed", StringUtil.IsEmpty(Bspeed)?"":Bspeed);
						map.put("maxspeed", StringUtil.IsEmpty(maxspeed)?"":maxspeed);
						map.put("starttime", StringUtil.IsEmpty(starttime)?"":starttime);
						map.put("endtime", StringUtil.IsEmpty(endtime)?"":endtime);
					}
					else{
						String DevSn =  root.elementTextTrim("DevSn");
						String RequestsReceivedTime =  root.elementTextTrim("RequestsReceivedTime");
						String TransportStartTime =  root.elementTextTrim("TransportStartTime");
						String TransportEndTime  =  root.elementTextTrim("TransportEndTime");
						String ReceiveByteContainHead =  root.elementTextTrim("ReceiveByteContainHead");
						String ReceiveByte =  root.elementTextTrim("ReceiveByte");
						String TCPRequestTime =  root.elementTextTrim("TCPRequestTime");
						String TCPResponseTime =  root.elementTextTrim("TCPResponseTime");
						String AvgSampledTotalValues = "";
						String MaxSampledTotalValues = "";
						String diagnosticsState = "";
						String ip = "";
						String AvgSampledValues = "";
						String MaxSampledValues = "";
						String loid = "";
						String netAccount = "";
						if(LipossGlobals.inArea(Global.AHDX))
						{
							AvgSampledTotalValues = root.elementTextTrim("AvgSampledTotalValues");
							MaxSampledTotalValues = root.elementTextTrim("MaxSampledTotalValues");
							ip = root.elementTextTrim("ip");
						}
						if(LipossGlobals.inArea(Global.JXDX))
						{
							AvgSampledValues = root.elementTextTrim("AvgSampledValues");
							AvgSampledTotalValues = root.elementTextTrim("AvgSampledTotalValues");
							diagnosticsState = root.elementTextTrim("DiagnosticsState");
						}
						if(LipossGlobals.inArea(Global.JSDX))
						{
							AvgSampledValues = root.elementTextTrim("AvgSampledValues");
							MaxSampledValues = root.elementTextTrim("MaxSampledValues");
							// 根据设备获取绑定的用户loid和宽带账号
							Map<String,String> usermap= dao.getUserInfo(device_id);
							loid = usermap.get("loid");
							netAccount =  usermap.get("netaccount");
							// 上面获取的属地不对，写死了00  ，这边重新获取下
							city_id = usermap.get("city_id");
						}
						//入库
						int num;
						if(LipossGlobals.inArea(Global.JXDX) && !StringUtil.IsEmpty(idsShare_queryType) && "httpSpeed".equals(idsShare_queryType)){
							logger.warn("进入测速开始 ......");
							double ret = 0.0d;
							String retMsg = "否";
							String status = "0";
							DecimalFormat df = new DecimalFormat("0.0");
							double total = StringUtil.getDoubleValue(AvgSampledTotalValues) * 8 / 1024;
							ret = total / StringUtil.getDoubleValue(downlink);
  					        AvgSampledValues = StringUtil.getStringValue(df.format(StringUtil.getDoubleValue(AvgSampledValues) * 8 / 1024));
							AvgSampledTotalValues = StringUtil.getStringValue(df.format(total));
							logger.warn("测速结果ret：{}",ret);
							if(ret >= 0.9 && ret <= 1.2){
								retMsg = "是";
								status = "1";
							}
							map.put("httpSpeedRet", retMsg);
							num = dao.addHTTPResult(devId, oui, DevSn, status, rstCode, column1, column2, TransportStartTime, TransportEndTime, ReceiveByte, TCPRequestTime, 
									TCPResponseTime, userName, loids, AvgSampledTotalValues, AvgSampledValues, CmdID, RstMsg, downlink, ip,netUser,acc_loginname);
						}else{
						    num = dao.addHTTPDiagResult(oui, DevSn, rstCode, CmdID, column1, column2,
									RequestsReceivedTime,TransportStartTime,TransportEndTime,ReceiveByteContainHead,ReceiveByte,TCPRequestTime,TCPResponseTime, city_id,netAccount,loid,MaxSampledValues,AvgSampledValues);
						}
						//修改 处 os_jiangk
						double Pert=0.00;
						String down="";
						String downPert="";
						//修改 处 os_jiangk
						if(LipossGlobals.inArea(Global.NXDX))
						{
							downPert=time(TransportStartTime, TransportEndTime, ReceiveByte);
							double downPertTmp =  StringUtil.getDoubleValue(downPert)/1024d;
							downPert = StringUtil.getStringValue(downPertTmp);
							if(!StringUtil.IsEmpty(downPert)){
								String[] downPertArr = downPert.split("\\.");
								if(null!=downPertArr && downPertArr.length>=2){
									if(downPertArr[1].length()>2){
										downPert = downPertArr[0] + "." + downPertArr[1].substring(0, 2);
									}
								}
							}
						}
						else
						{
							downPert=getDownPert(TransportStartTime, TransportEndTime, ReceiveByte);
						}
						
						map.put("DevSn", DevSn);
						if( !StringUtil.IsEmpty(RequestsReceivedTime) && !StringUtil.IsEmpty(RequestsReceivedTime) && !StringUtil.IsEmpty(TransportEndTime)){
							map.put("RequestsReceivedTime", (RequestsReceivedTime.replace("T", " ")).substring(0,19));
							map.put("TransportStartTime", (TransportStartTime.replace("T", " ")).substring(0,19));
							map.put("TransportEndTime", (TransportEndTime.replace("T", " ")).substring(0,19));
						}else{
							map.put("RequestsReceivedTime", "");
							map.put("TransportStartTime","");
							map.put("TransportEndTime","");
						}
						map.put("ReceiveByteContainHead", ReceiveByteContainHead);
						map.put("ReceiveByte", ReceiveByte);
						if( !StringUtil.IsEmpty(RequestsReceivedTime) && !StringUtil.IsEmpty(RequestsReceivedTime) && !StringUtil.IsEmpty(TransportEndTime)){
							map.put("TCPRequestTime", (TCPRequestTime.replace("T", " ")).substring(0,19));
							map.put("TCPResponseTime", (TCPResponseTime.replace("T", " ")).substring(0,19));
						}else{
							map.put("TCPRequestTime","");
							map.put("TCPResponseTime","");
						}
						map.put("downPert", downPert);
						if("1".equals(wan_interface)){
							map.put("wanType", "宽带上网");
						} else if("3".equals(wan_interface)) {
							map.put("wanType", "TR069");
						}
						if(LipossGlobals.inArea(Global.AHDX))
						{
							if(!StringUtil.IsEmpty(AvgSampledTotalValues) && !StringUtil.IsEmpty(MaxSampledTotalValues)){
								map.put("AvgSampledTotalValues", AvgSampledTotalValues);
								map.put("MaxSampledTotalValues", MaxSampledTotalValues);
							} else {
								map.put("AvgSampledTotalValues", "");
								map.put("MaxSampledTotalValues", "");
							}
							map.put("city", CityDAO.getCityName(city_id));
							map.put("speed", speed);
							map.put("IP", ip);
						}
						if(LipossGlobals.inArea(Global.JXDX))
						{
							if(!StringUtil.IsEmpty(AvgSampledTotalValues)){
								map.put("AvgSampledTotalValues", AvgSampledTotalValues);
							} else {
								map.put("AvgSampledTotalValues", "");
							}
							if(!StringUtil.IsEmpty(AvgSampledValues)){
								map.put("AvgSampledValues", AvgSampledValues);
							} else {
								map.put("AvgSampledValues", "");
							}
							map.put("DiagnosticsState", diagnosticsState);
						}
						if(LipossGlobals.inArea(Global.JSDX))
						{
							if(!StringUtil.IsEmpty(MaxSampledValues)){
								map.put("MaxSampledValues", getValue(MaxSampledValues));
							} else {
								map.put("MaxSampledValues", "");
							}
							if(!StringUtil.IsEmpty(AvgSampledValues)){
								map.put("AvgSampledValues", getValue(AvgSampledValues));
							} else {
								map.put("AvgSampledValues", "");
							}
						}
					}
				}else{
//					int num = dao.addHTTPDiagResult(oui, "4430060DE44D4017C", rstCode, CmdID, column1, column2,
//							"23","24","25","26","27","38","50");
					map.put("code", "1");
				}
				if(LipossGlobals.inArea(Global.HBLT)){
					String rate = dao.getRate(pppoeUserName,device_id);
					if(!StringUtil.IsEmpty(rate)){
						map.put("rate", rate);
					}
				}
			} catch (DocumentException e) {
				map.put("code", "1");
				map.put("errMessage", "解析PPPoE通道返回值有误!");
				logger.warn("解析PPPoE通道返回值有误!");
			}
		return map;
	}

	public Map<String, String> queryHTTPServiceJLLT(String device_id,String wanType, String column1, String column2,String pppoeUserName, String userName,String password,String code) {
		Map<String, String> map = new HashMap<String, String>();
		StringBuffer call = new StringBuffer();
		// 通道值，传输过来的格式为：通道值+“$”+vlanid
		String wt = "";
		String vlanid = " ";
		if (!StringUtil.IsEmpty(wanType)) {
			String[] arr = wanType.split("￥");
			wt = arr[0];
			vlanid = arr[1];
		}

		DateTimeUtil dt = new DateTimeUtil();
		String cmdId = StringUtil.getStringValue(dt.getLongTime());
		String opId = StringUtil.getStringValue(new DateTimeUtil().getLongTime()) + StringUtil.getStringValue((int)(Math.random()*1000));
		StringBuffer inParam = new StringBuffer();
		inParam.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>	\n");
		inParam.append("<root>										\n");
		inParam.append("	<CmdID>").append(cmdId).append("</CmdID>						\n");
		inParam.append("	<CmdType>CX_01</CmdType>					\n");
		inParam.append("	<ClientType>5</ClientType>						\n");
		inParam.append("	<Param>									\n");
		inParam.append("        <op_id>").append(opId).append("</op_id>      \n");
		inParam.append("        <backgroundsize>").append(code).append("</backgroundsize>      \n");
		inParam.append("		<source>").append("web").append("</source>	\n");
		inParam.append("		<type>").append("6").append("</type>	\n");
		inParam.append("		<index>").append(device_id).append("</index>	\n");
		inParam.append("		<SpeedTest_DiagnosticsState>").append("Requested").append("</SpeedTest_DiagnosticsState>	\n");
		inParam.append("		<WanPassageWay>").append(wt).append("</WanPassageWay>		\n");
		inParam.append("		<SpeedTest_downloadURL>").append("").append("</SpeedTest_downloadURL>		\n");
		inParam.append("		<SpeedTest_testMode>").append("serverMode").append("</SpeedTest_testMode>		\n");
		inParam.append("		<SpeedTest_reportURL>").append(column2).append("</SpeedTest_reportURL>		\n");
		inParam.append("		<SpeedTest_testURL>").append(column1).append("</SpeedTest_testURL>		\n");
		inParam.append("		<UserName>").append(userName).append("</UserName>		\n");
		inParam.append("		<Password>").append(password).append("</Password>		\n");
		
		inParam.append("		<PppoeUserName>").append(pppoeUserName).append("</PppoeUserName>		\n");
		
		inParam.append("	</Param>								\n");
		inParam.append("</root>										\n");
		logger.warn("http:" + inParam.toString());
		final String url = LipossGlobals.getLipossProperty("IDSServiceUri");
		String methodName = "testSpeed4JLLT";
		String callBack = WSClientUtil.callItmsService(url, inParam.toString(), methodName);

		logger.warn("回参：" + callBack);
		// 解析回参
		SAXReader reader = new SAXReader();
		Document document = null;
		try {
			document = reader.read(new StringReader(callBack));
			Element root = document.getRootElement();
			String rstCode = root.elementTextTrim("RstCode");
			String RstMsg = root.elementTextTrim("RstMsg");
			map.put("errMessage", RstMsg);
			if ("1".equals(rstCode)) {
				map.put("code", "1");
				String diagnosticsState = root.elementTextTrim("diagnosticsState");
				String pppoeName = root.elementTextTrim("pppoeName");
				//String status = root.elementTextTrim("status");
				String pppoeIP = root.elementTextTrim("pppoeIP");
				String Aspeed = root.elementTextTrim("Aspeed");
				String Bspeed = root.elementTextTrim("Bspeed");
				String Cspeed = root.elementTextTrim("Cspeed");
				String maxspeed = root.elementTextTrim("maxspeed");
				String starttime = root.elementTextTrim("starttime");
				String endtime = root.elementTextTrim("endtime");
				map.put("diagnosticsState", StringUtil.IsEmpty(diagnosticsState) ? "" : diagnosticsState);
				//map.put("status", StringUtil.IsEmpty(status) ? "" : status);
				map.put("pppoeName", StringUtil.IsEmpty(pppoeName) ? "" : pppoeName);
				map.put("pppoeIP", StringUtil.IsEmpty(pppoeIP) ? "" : pppoeIP);
				map.put("Aspeed", StringUtil.IsEmpty(Aspeed) ? "" : Aspeed);
				map.put("Bspeed", StringUtil.IsEmpty(Bspeed) ? "" : Bspeed);
				map.put("Cspeed", StringUtil.IsEmpty(Cspeed) ? "" : Cspeed);
				map.put("maxspeed", StringUtil.IsEmpty(maxspeed) ? "" : maxspeed);
				map.put("starttime", StringUtil.IsEmpty(starttime) ? "" : starttime);
				map.put("endtime", StringUtil.IsEmpty(endtime) ? "" : endtime);
			} else {
				map.put("code", "1");
			}
		} catch (DocumentException e) {
			map.put("code", "1");
			map.put("errMessage", "解析PPPoE通道返回值有误!");
			logger.warn("解析PPPoE通道返回值有误!");
		}
		return map;
	}
	
	
	/**
	 * 山西调的是吉林的接口，因为山西自己接口是异步调用
	 * @param device_id
	 * @param wanType
	 * @param column1
	 * @param column2
	 * @param pppoeUserName
	 * @param userName
	 * @param password
	 * @param code
	 * @return
	 */
	public Map<String, String> queryHTTPServiceSXLT(String device_id,String wanType, String column1, String column2,String pppoeUserName, String userName,String password,String code) {
		Map<String, String> map = new HashMap<String, String>();
		StringBuffer call = new StringBuffer();
		// 通道值，传输过来的格式为：通道值+“$”+vlanid
		String wt = "";
		String vlanid = " ";
		if (!StringUtil.IsEmpty(wanType)) {
			String[] arr = wanType.split("￥");
			wt = arr[0];
			vlanid = arr[1];
		}

		DateTimeUtil dt = new DateTimeUtil();
		String cmdId = StringUtil.getStringValue(dt.getLongTime());
		String opId = StringUtil.getStringValue(new DateTimeUtil().getLongTime()) + StringUtil.getStringValue((int)(Math.random()*1000));
		StringBuffer inParam = new StringBuffer();
		inParam.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>	\n");
		inParam.append("<root>										\n");
		inParam.append("	<CmdID>").append(cmdId).append("</CmdID>						\n");
		inParam.append("	<CmdType>CX_01</CmdType>					\n");
		inParam.append("	<ClientType>5</ClientType>						\n");
		inParam.append("	<Param>									\n");
		inParam.append("        <op_id>").append(opId).append("</op_id>      \n");
		inParam.append("        <backgroundsize>").append(code).append("</backgroundsize>      \n");
		inParam.append("		<source>").append("web").append("</source>	\n");
		inParam.append("		<type>").append("6").append("</type>	\n");
		inParam.append("		<index>").append(device_id).append("</index>	\n");
		inParam.append("		<SpeedTest_DiagnosticsState>").append("Requested").append("</SpeedTest_DiagnosticsState>	\n");
		inParam.append("		<WanPassageWay>").append(wt).append("</WanPassageWay>		\n");
		inParam.append("		<SpeedTest_downloadURL>").append("").append("</SpeedTest_downloadURL>		\n");
		inParam.append("		<SpeedTest_testMode>").append("serverMode").append("</SpeedTest_testMode>		\n");
		inParam.append("		<SpeedTest_reportURL>").append(column2).append("</SpeedTest_reportURL>		\n");
		inParam.append("		<SpeedTest_testURL>").append(column1).append("</SpeedTest_testURL>		\n");
		inParam.append("		<UserName>").append(userName).append("</UserName>		\n");
		inParam.append("		<Password>").append(password).append("</Password>		\n");
		
		inParam.append("		<PppoeUserName>").append(pppoeUserName).append("</PppoeUserName>		\n");
		
		inParam.append("	</Param>								\n");
		inParam.append("</root>										\n");
		logger.warn("http:" + inParam.toString());
		final String url = LipossGlobals.getLipossProperty("IDSServiceUri");
		String methodName = "testSpeed4JLLT";
		String callBack = WSClientUtil.callItmsService(url, inParam.toString(), methodName);

		logger.warn("回参：" + callBack);
		// 解析回参
		SAXReader reader = new SAXReader();
		Document document = null;
		try {
			document = reader.read(new StringReader(callBack));
			Element root = document.getRootElement();
			String rstCode = root.elementTextTrim("RstCode");
			String RstMsg = root.elementTextTrim("RstMsg");
			map.put("errMessage", RstMsg);
			if ("1".equals(rstCode)) {
				map.put("code", "1");
				String diagnosticsState = root.elementTextTrim("diagnosticsState");
				String pppoeName = root.elementTextTrim("pppoeName");
				//String status = root.elementTextTrim("status");
				String pppoeIP = root.elementTextTrim("pppoeIP");
				String Aspeed = root.elementTextTrim("Aspeed");
				String Bspeed = root.elementTextTrim("Bspeed");
				String Cspeed = root.elementTextTrim("Cspeed");
				String maxspeed = root.elementTextTrim("maxspeed");
				String starttime = root.elementTextTrim("starttime");
				String endtime = root.elementTextTrim("endtime");
				map.put("diagnosticsState", StringUtil.IsEmpty(diagnosticsState) ? "" : diagnosticsState);
				//map.put("status", StringUtil.IsEmpty(status) ? "" : status);
				map.put("pppoeName", StringUtil.IsEmpty(pppoeName) ? "" : pppoeName);
				map.put("pppoeIP", StringUtil.IsEmpty(pppoeIP) ? "" : pppoeIP);
				map.put("Aspeed", StringUtil.IsEmpty(Aspeed) ? "" : Aspeed);
				map.put("Bspeed", StringUtil.IsEmpty(Bspeed) ? "" : Bspeed);
				map.put("Cspeed", StringUtil.IsEmpty(Cspeed) ? "" : Cspeed);
				map.put("maxspeed", StringUtil.IsEmpty(maxspeed) ? "" : maxspeed);
				map.put("starttime", StringUtil.IsEmpty(starttime) ? "" : starttime);
				map.put("endtime", StringUtil.IsEmpty(endtime) ? "" : endtime);
			} else {
				map.put("code", "1");
			}
		} catch (DocumentException e) {
			map.put("code", "1");
			map.put("errMessage", "解析PPPoE通道返回值有误!");
			logger.warn("解析PPPoE通道返回值有误!");
		}
		return map;
	}
	
	public Map<String, String> queryHTTPServiceHBLT(String city_id, String oui, String device_id, String wanType,
			String wan_interface, String column1, String column2, String userName, String password, String connType,
			String speed, String pppoeUserName,String type) {
		Map<String, String> map = new HashMap<String, String>();
		StringBuffer call = new StringBuffer();
		// 通道值，传输过来的格式为：通道值+“$”+vlanid
		String wt = "";
		String vlanid = " ";
		if (!StringUtil.IsEmpty(wanType)) {
			String[] arr = wanType.split("￥");
			wt = arr[0];
			vlanid = arr[1];
		}

		DateTimeUtil dt = new DateTimeUtil();
		String cmdId = StringUtil.getStringValue(dt.getLongTime());
		StringBuffer inParam = new StringBuffer();
		inParam.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>	\n");
		inParam.append("<root>										\n");
		inParam.append("	<CmdID>").append(cmdId).append("</CmdID>						\n");
		inParam.append("	<CmdType>CX_01</CmdType>					\n");
		inParam.append("	<ClientType>5</ClientType>						\n");
		inParam.append("	<Param>									\n");
		inParam.append("		<DevSn>").append(device_id).append("</DevSn>		\n");
		inParam.append("        <OUI>").append(oui).append("</OUI>      \n");
		inParam.append("		<CityId>").append(city_id).append("</CityId>	\n");
		inParam.append("		<WanPassageWay>").append(wt).append("</WanPassageWay>		\n");
		inParam.append("		<DownURL>").append(column1).append("</DownURL>		\n");
		inParam.append("		<Priority>").append(column2).append("</Priority>		\n");
		inParam.append("		<UserName>").append(userName).append("</UserName>		\n");
		inParam.append("		<Password>").append(password).append("</Password>		\n");
		inParam.append("		<ConnType>").append(connType).append("</ConnType>		\n");
		inParam.append("		<PppoeUserName>").append(pppoeUserName).append("</PppoeUserName>		\n");
		inParam.append("		<type>").append(type).append("</type>		\n");
		inParam.append("	</Param>								\n");
		inParam.append("</root>										\n");
		logger.warn("http:" + inParam.toString());
		final String url = LipossGlobals.getLipossProperty("IDSServiceUri");
		String methodName = "UpAndDownSpeedHBLT";
		String callBack = WSClientUtil.callItmsService(url, inParam.toString(), methodName);

		logger.warn("回参：" + callBack);
		// 解析回参
		SAXReader reader = new SAXReader();
		Document document = null;
		try {
			document = reader.read(new StringReader(callBack));
			Element root = document.getRootElement();
			String rstCode = root.elementTextTrim("RstCode");
			String RstMsg = root.elementTextTrim("RstMsg");
			map.put("errMessage", RstMsg);
			if ("0".equals(rstCode)) {
				map.put("code", "0");
				String DevSn = root.elementTextTrim("DevSn");
				String diagnosticsState = root.elementTextTrim("diagnosticsState");
				String pppoeName = root.elementTextTrim("pppoeName");
				String pppoeIP = root.elementTextTrim("pppoeIP");
				String Aspeed = root.elementTextTrim("Aspeed");
				String Bspeed = root.elementTextTrim("Bspeed");
				String maxspeed = root.elementTextTrim("maxspeed");
				String starttime = root.elementTextTrim("starttime");
				String endtime = root.elementTextTrim("endtime");
				map.put("DevSn", StringUtil.IsEmpty(DevSn) ? "" : DevSn);
				map.put("diagnosticsState", StringUtil.IsEmpty(diagnosticsState) ? "" : diagnosticsState);
				map.put("pppoeName", StringUtil.IsEmpty(pppoeName) ? "" : pppoeName);
				map.put("pppoeIP", StringUtil.IsEmpty(pppoeIP) ? "" : pppoeIP);
				map.put("Aspeed", StringUtil.IsEmpty(Aspeed) ? "" : Aspeed);
				map.put("Bspeed", StringUtil.IsEmpty(Bspeed) ? "" : Bspeed);
				map.put("maxspeed", StringUtil.IsEmpty(maxspeed) ? "" : maxspeed);
				map.put("starttime", StringUtil.IsEmpty(starttime) ? "" : starttime);
				map.put("endtime", StringUtil.IsEmpty(endtime) ? "" : endtime);
			} else {
				map.put("code", "1");
			}
			if (LipossGlobals.inArea(Global.HBLT)) {
				String rate = dao.getRate(pppoeUserName, device_id);
				if (!StringUtil.IsEmpty(rate)) {
					long rate1 = Long.parseLong(rate)/1024;
					map.put("rate", rate1+"");
				}
			}
		} catch (DocumentException e) {
			map.put("code", "1");
			map.put("errMessage", "解析PPPoE通道返回值有误!");
			logger.warn("解析PPPoE通道返回值有误!");
		}
		return map;
	}
	
	
	
	/**
	 * 宁夏在用
	 * @Description TODO
	 * @author guxl3
	 * @date 2019年6月10日
	 */
	public Map<String, String> queryUploadSpeedService(String city_id, String oui, String device_id, String wanType,
			String wan_interface, String column1, String column2, String userName, String password, String connType,
			String speed, String pppoeUserName) {
		Map<String,String> map = new HashMap<String, String>();
		
		//通道值，传输过来的格式为：通道值+“$”+vlanid
		String wt = "";
		if(!StringUtil.IsEmpty(wanType)){
			String[] arr = wanType.split("￥");
			wt = arr[0];
		}
		
		DateTimeUtil dt = new DateTimeUtil();
		String cmdId = StringUtil.getStringValue(dt.getLongTime());
		StringBuffer inParam = new StringBuffer();
		inParam.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>	\n");
		inParam.append("<root>										\n");
		inParam.append("	<CmdID>").append(cmdId).append("</CmdID>						\n");
		inParam.append("	<CmdType>CX_01</CmdType>					\n");
		inParam.append("	<ClientType>5</ClientType>						\n");
		inParam.append("	<Param>									\n");
		inParam.append("		<DevSn>").append(device_id).append("</DevSn>		\n");
		inParam.append("        <OUI>").append(oui).append("</OUI>      \n");
		inParam.append("		<CityId>").append(city_id).append("</CityId>	\n");
		inParam.append("		<WanPassageWay>").append(wt).append("</WanPassageWay>		\n");
		inParam.append("		<UpLoadURL>").append(column1).append("</UpLoadURL>		\n");
		inParam.append("		<TestFileLength>").append(column2).append("</TestFileLength>  \n");
		inParam.append("	</Param>								\n");
		inParam.append("</root> \n");
		
		logger.warn("http:"+inParam.toString());
		final String url = LipossGlobals.getLipossProperty("ItmsServiceUri");
		
		String methodName ="upLoadByHTTP";
		String callBack =  WSClientUtil.callItmsService(url, inParam.toString(), methodName);
		
		logger.warn("回参："+callBack);
		//解析回参
		SAXReader reader = new SAXReader();
		Document document = null;
		try {
			document = reader.read(new StringReader(callBack));
			Element root = document.getRootElement();
			String rstCode = root.elementTextTrim("RstCode");
			String RstMsg = root.elementTextTrim("RstMsg");
			map.put("errMessage", RstMsg);
			if ("0".equals(rstCode)) {
				map.put("code", "0");
				String DevSn=root.elementTextTrim("DevSn");
				String USpeed=root.elementTextTrim("USpeed");
				String BOMTime=root.elementTextTrim("BOMTime");
				String EOMTime=root.elementTextTrim("EOMTime");
				String TCPOpenRequestTime=root.elementTextTrim("TCPOpenRequestTime");
				String TCPOpenResponseTime=root.elementTextTrim("TCPOpenResponseTime");
				String TotalBytesSent=root.elementTextTrim("TotalBytesSent");
				
				map.put("DevSn", StringUtil.IsEmpty(DevSn)?"":DevSn);
				map.put("USpeed", StringUtil.IsEmpty(USpeed)?"":USpeed);
				if( !StringUtil.IsEmpty(BOMTime) && !StringUtil.IsEmpty(BOMTime)){
					map.put("BOMTime", (BOMTime.replace("T", " ")).substring(0,19));
					map.put("EOMTime", (EOMTime.replace("T", " ")).substring(0,19));
				}else{
					map.put("BOMTime","");
					map.put("EOMTime","");
				}
				if( !StringUtil.IsEmpty(TCPOpenRequestTime) && !StringUtil.IsEmpty(TCPOpenResponseTime)){
					map.put("TCPOpenRequestTime", (TCPOpenRequestTime.replace("T", " ")).substring(0,19));
					map.put("TCPOpenResponseTime", (TCPOpenResponseTime.replace("T", " ")).substring(0,19));
				}else{
					map.put("TCPOpenRequestTime","");
					map.put("TCPOpenResponseTime","");
				}
				map.put("TotalBytesSent", StringUtil.IsEmpty(TotalBytesSent)?"":TotalBytesSent);
				
				
				if ("1".equals(wan_interface)) {
					map.put("wanType", "宽带上网");
				} else if ("3".equals(wan_interface)) {
					map.put("wanType", "TR069");
				}
			} else {
				map.put("code", "1");
			}
		} catch (DocumentException e) {
			map.put("code", "1");
			map.put("errMessage", "解析PPPoE通道返回值有误!");
			logger.warn("解析PPPoE通道返回值有误!");
		}
	return map;
}
	
	public HTTPDeviceQueryDAO getDao()
	{
		return dao;
	}

	
	public void setDao(HTTPDeviceQueryDAO dao)
	{
		this.dao = dao;
	}
	
	/**
	 * 下载速率
	 * @param TransportStartTime 开始时间
	 * @param TransportEndTime 结束时间
	 * @param ReceiveByte 字节数
	 * @return
	 */
	private  String getDownPert(String transportStartTime,
			String transportEndTime, String receiveByte) 
	{
		logger.warn("transportStartTime====="+transportStartTime);
		logger.warn("transportEndTime====="+transportEndTime);
		logger.warn("receiveByte====="+receiveByte);
		float ff = 0;
		String strtime = transportStartTime;
		String endtime = transportEndTime;
		if( !StringUtil.IsEmpty(strtime) && !StringUtil.IsEmpty(endtime))
		{
			BigDecimal strTime = new BigDecimal(strtime.split(":")[2].split("[.]")[0]).add(new BigDecimal(strtime.split(":")[2].split("[.]")[1]).divide(new BigDecimal("1000000"), 6, BigDecimal.ROUND_HALF_UP));
			BigDecimal endTime = new BigDecimal(endtime.split(":")[2].split("[.]")[0]).add(new BigDecimal(endtime.split(":")[2].split("[.]")[1]).divide(new BigDecimal("1000000"), 6, BigDecimal.ROUND_HALF_UP));
			BigDecimal receiveBytes = new BigDecimal(receiveByte).divide(new BigDecimal("1024") ,6,BigDecimal.ROUND_HALF_UP);//k
			logger.warn("transportStartTime====="+strTime);
			logger.warn("transportEndTime====="+endTime);
			logger.warn("receiveByte====="+receiveBytes);
			
			BigDecimal mintue = (new BigDecimal(endtime.split(":")[1]).subtract(new BigDecimal(strtime.split(":")[1]))).multiply(new BigDecimal("60"));
			BigDecimal period = endTime.subtract(strTime).add(mintue);
			logger.warn("mintue====="+mintue);
			logger.warn("period====="+period);
			//k/s
			ff = receiveBytes.divide(period,6, BigDecimal.ROUND_HALF_UP).floatValue();
			DecimalFormat df = new DecimalFormat("#0.00");
			return StringUtil.getStringValue(df.format(ff));	
		}
		return StringUtil.getStringValue(ff);
	}
	public String time(String transportStartTime,
			String transportEndTime, String receiveByte)
	{
		String strTime="";
		String endTime=""; 
		double ff=0.0000;
		if( !StringUtil.IsEmpty(transportStartTime) && !StringUtil.IsEmpty(transportStartTime))
		{
		strTime=(transportStartTime.replace("T", " ")).substring(0,19);
		endTime=(transportEndTime.replace("T", " ")).substring(0,19);
		DateTimeUtil dt = null;
		if (strTime == null || "".equals(strTime)){
			strTime = null;
		}else{
			dt = new DateTimeUtil(strTime);
			strTime = String.valueOf(dt.getLongTime());
		}
		if (endTime == null || "".equals(endTime)){
			endTime = null;
		}else{
			dt = new DateTimeUtil(endTime);
			endTime = String.valueOf(dt.getLongTime());
		}
		int time=((Integer.valueOf(endTime))-(Integer.valueOf(strTime)));
		if(LipossGlobals.inArea(Global.NXDX)){
			ff=(Double.parseDouble(receiveByte)/time)* 1000 * 8 / 1024 / 1024;
		}else{
			ff=(Double.parseDouble(receiveByte)/time)*8*8;
		}
		//String test=String.valueOf(ff);
		DecimalFormat df = new DecimalFormat("#0.00");
		return StringUtil.getStringValue(df.format(ff));
		}
		return StringUtil.getStringValue(ff);
	}
	/**
	 * 修改os_jiangk
	 */
	public static void main(String[] args)
	{
		String AvgSampledTotalValues="15237.50";
		String AvgSampledValues="14725.50";
		String downlink="100.00";
		double ret = 0.0d;
		String retMsg = "否";
		String status = "0";
		DecimalFormat df = new DecimalFormat("0.0");
		double total = StringUtil.getDoubleValue(AvgSampledTotalValues) * 8 / 1024;
	        AvgSampledValues = StringUtil.getStringValue(df.format(StringUtil.getDoubleValue(AvgSampledValues) * 8 / 1024));
		AvgSampledTotalValues = StringUtil.getStringValue(df.format(total));
		logger.warn("AvgSampledTotalValues:{}",AvgSampledTotalValues);
		logger.warn("AvgSampledValues：{}",AvgSampledValues);
		logger.warn("ret：{}", StringUtil.getDoubleValue(downlink));
		logger.warn("ret：{}", total/StringUtil.getDoubleValue(downlink));
	}
	
	public static int getTestRate(double downlink){
		int ret = 100;
		if(downlink > 100 && downlink <= 200){
			ret = 200;
		}else
			if(downlink > 200 && downlink <= 300){
				ret = 300;
			}else
				if(downlink > 300 && downlink <= 500){
					ret = 500;
				}else
					if(downlink > 500){
						ret = 1000;
					}
		logger.warn("ret : {}",ret);
		return ret;
	}
/*	private void setTime(){
		logger.debug("setTime()" + starttime);
		// 定义DateTimeUtil
		if (starttime == null || "".equals(starttime)){
			starttime1 = null;
		}else{
			dt = new DateTimeUtil(starttime);
			starttime1 = String.valueOf(dt.getLongTime());
		}
		if (endtime == null || "".equals(endtime)){
			endtime1 = null;
		}else{
			dt = new DateTimeUtil(endtime);
			endtime1 = String.valueOf(dt.getLongTime());
		}
	}*/
	
	/**
	 * 安徽电信下载拨测专属测试用户查询
	 */
	public List<Map<String,String>> getTestUserList(){
		List<HashMap<String,String>> list=dao.getTestUserList();
		List<Map<String,String>> testUserList=new ArrayList<Map<String,String>>();
		if(list!=null){
			for(HashMap<String,String> map:list){
				Map<String,String> userMap=new HashMap<String,String>();
				String info=StringUtil.getStringValue(map, "testname")+"#"+
				            StringUtil.getStringValue(map, "username")+"#"+
						    StringUtil.getStringValue(map, "password")+"#"+
						    StringUtil.getStringValue(map, "testrate");
				userMap.put("info", info);
				userMap.put("testname", StringUtil.getStringValue(map, "testname"));
				testUserList.add(userMap);
			}
		}
		return testUserList;
	}

	/**
	 * kb与m换算
	 * 
	 * @param sampledValues
	 *            数组
	 * @return 平均值
	 */
	private String getValue(String sampledValues)
	{
		// 保留小数点后两位
		DecimalFormat df = new DecimalFormat("######0.00");
		double result = Double.parseDouble(sampledValues) / 128;
		return StringUtil.getStringValue(df.format(result));
	}

	public Map<String, String> queryHTTPServiceAHLT(String device_id,
			String wanType, String column1, String column2,
			String pppoeUserName, String userName, String password, String code) {
		Map<String, String> map = new HashMap<String, String>();
		StringBuffer call = new StringBuffer();
		// 通道值，传输过来的格式为：通道值+“$”+vlanid
		String wt = "";
		String vlanid = " ";
		if (!StringUtil.IsEmpty(wanType)) {
			String[] arr = wanType.split("￥");
			wt = arr[0];
			vlanid = arr[1];
		}

		DateTimeUtil dt = new DateTimeUtil();
		String cmdId = StringUtil.getStringValue(dt.getLongTime());
		String opId = StringUtil.getStringValue(new DateTimeUtil().getLongTime()) + StringUtil.getStringValue((int)(Math.random()*1000));
		StringBuffer inParam = new StringBuffer();
		inParam.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>	\n");
		inParam.append("<root>										\n");
		inParam.append("	<CmdID>").append(cmdId).append("</CmdID>						\n");
		inParam.append("	<CmdType>CX_01</CmdType>					\n");
		inParam.append("	<ClientType>5</ClientType>						\n");
		inParam.append("	<Param>									\n");
		inParam.append("        <op_id>").append(opId).append("</op_id>      \n");
		inParam.append("        <backgroundsize>").append(code).append("</backgroundsize>      \n");
		inParam.append("		<source>").append("web").append("</source>	\n");
		inParam.append("		<type>").append("6").append("</type>	\n");
		inParam.append("		<index>").append(device_id).append("</index>	\n");
		inParam.append("		<SpeedTest_DiagnosticsState>").append("Requested").append("</SpeedTest_DiagnosticsState>	\n");
		inParam.append("		<WanPassageWay>").append(wt).append("</WanPassageWay>		\n");
		inParam.append("		<SpeedTest_downloadURL>").append("").append("</SpeedTest_downloadURL>		\n");
		inParam.append("		<SpeedTest_testMode>").append("serverMode").append("</SpeedTest_testMode>		\n");
		inParam.append("		<SpeedTest_reportURL>").append(column2).append("</SpeedTest_reportURL>		\n");
		inParam.append("		<SpeedTest_testURL>").append(column1).append("</SpeedTest_testURL>		\n");
		inParam.append("		<UserName>").append(StringUtil.getStringValue(userName)).append("</UserName>		\n");
		inParam.append("		<Password>").append(StringUtil.getStringValue(password)).append("</Password>		\n");
		
		inParam.append("		<PppoeUserName>").append(StringUtil.getStringValue(pppoeUserName)).append("</PppoeUserName>		\n");
		
		inParam.append("	</Param>								\n");
		inParam.append("</root>										\n");
		logger.warn("http:" + inParam.toString());
		final String url = LipossGlobals.getLipossProperty("IDSServiceUri");
		String methodName = "testSpeed4AHLT";
		String callBack = WSClientUtil.callItmsService(url, inParam.toString(), methodName);

		logger.warn("回参：" + callBack);
		// 解析回参
		SAXReader reader = new SAXReader();
		Document document = null;
		try {
			document = reader.read(new StringReader(callBack));
			Element root = document.getRootElement();
			String rstCode = root.elementTextTrim("RstCode");
			String RstMsg = root.elementTextTrim("RstMsg");
			map.put("errMessage", RstMsg);
			if ("1".equals(rstCode)) {
				map.put("code", "1");
				String diagnosticsState = root.elementTextTrim("diagnosticsState");
				String pppoeName = root.elementTextTrim("pppoeName");
				//String status = root.elementTextTrim("status");
				String pppoeIP = root.elementTextTrim("pppoeIP");
				String Aspeed = root.elementTextTrim("Aspeed");
				String Bspeed = root.elementTextTrim("Bspeed");
				String Cspeed = root.elementTextTrim("Cspeed");
				String maxspeed = root.elementTextTrim("maxspeed");
				String starttime = root.elementTextTrim("starttime");
				String endtime = root.elementTextTrim("endtime");
				String testMode = root.elementTextTrim("testMode");
				map.put("diagnosticsState", StringUtil.IsEmpty(diagnosticsState) ? "" : diagnosticsState);
				//map.put("status", StringUtil.IsEmpty(status) ? "" : status);
				map.put("pppoeName", StringUtil.IsEmpty(pppoeName) ? "" : pppoeName);
				map.put("pppoeIP", StringUtil.IsEmpty(pppoeIP) ? "" : pppoeIP);
				map.put("Aspeed", StringUtil.IsEmpty(Aspeed) ? "" : Aspeed);
				map.put("Bspeed", StringUtil.IsEmpty(Bspeed) ? "" : Bspeed);
				map.put("Cspeed", StringUtil.IsEmpty(Cspeed) ? "" : Cspeed);
				map.put("maxspeed", StringUtil.IsEmpty(maxspeed) ? "" : maxspeed);
				map.put("starttime", StringUtil.IsEmpty(starttime) ? "" : starttime);
				map.put("testMode", StringUtil.IsEmpty(testMode) ? "" : testMode);
				map.put("endtime", StringUtil.IsEmpty(endtime) ? "" : endtime);
			} else {
				map.put("code", "1");
			}
		} catch (DocumentException e) {
			map.put("code", "1");
			map.put("errMessage", "解析PPPoE通道返回值有误!");
			logger.warn("解析PPPoE通道返回值有误!");
		}
		return map;
	}
	
	public Map<String, String> queryHTTPServiceNXLT(String device_id,
			String wanType, String column1, String column2,
			String pppoeUserName, String userName, String password, String code) {
		Map<String, String> map = new HashMap<String, String>();
		StringBuffer call = new StringBuffer();
		// 通道值，传输过来的格式为：通道值+“$”+vlanid
		String wt = "";
		String vlanid = " ";
		if (!StringUtil.IsEmpty(wanType)) {
			String[] arr = wanType.split("￥");
			wt = arr[0];
			vlanid = arr[1];
		}

		DateTimeUtil dt = new DateTimeUtil();
		String cmdId = StringUtil.getStringValue(dt.getLongTime());
		String opId = StringUtil.getStringValue(new DateTimeUtil().getLongTime()) + StringUtil.getStringValue((int)(Math.random()*1000));
		StringBuffer inParam = new StringBuffer();
		inParam.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>	\n");
		inParam.append("<root>										\n");
		inParam.append("	<CmdID>").append(cmdId).append("</CmdID>						\n");
		inParam.append("	<CmdType>CX_01</CmdType>					\n");
		inParam.append("	<ClientType>5</ClientType>						\n");
		inParam.append("	<Param>									\n");
		inParam.append("        <op_id>").append(opId).append("</op_id>      \n");
		inParam.append("        <Backgroundsize>").append(code).append("</Backgroundsize>      \n");
		inParam.append("		<source>").append("web").append("</source>	\n");
		inParam.append("		<UserInfoType>").append("6").append("</UserInfoType>	\n");
		inParam.append("		<UserInfo>").append(device_id).append("</UserInfo>	\n");
		inParam.append("		<TestMode>").append("serverMode").append("</TestMode>	\n");
		inParam.append("		<DiagnosticsState>").append("Requested").append("</DiagnosticsState>	\n");
		inParam.append("		<WanPassageWay>").append(wt).append("</WanPassageWay>		\n");
		inParam.append("		<DownloadURL>").append("").append("</DownloadURL>		\n");
 		inParam.append("		<ReportURL>").append(column2).append("</ReportURL>		\n");
		inParam.append("		<TestURL>").append(column1).append("</TestURL>		\n");
		inParam.append("		<Eupppoename>").append(StringUtil.getStringValue(userName)).append("</Eupppoename>		\n");
		inParam.append("		<Eupassword>").append(StringUtil.getStringValue(password)).append("</Eupassword>		\n");
		
		inParam.append("		<PppoeUserName>").append(StringUtil.getStringValue(pppoeUserName)).append("</PppoeUserName>		\n");
		
		inParam.append("	</Param>								\n");
		inParam.append("</root>										\n");
		logger.warn("http:" + inParam.toString());
		final String url = LipossGlobals.getLipossProperty("IDSServiceUri");
		String methodName = "testSpeedNXLT";
		String callBack = WSClientUtil.callItmsService(url, inParam.toString(), methodName);

		logger.warn("回参：" + callBack);
		// 解析回参
		SAXReader reader = new SAXReader();
		Document document = null;
		try {
			document = reader.read(new StringReader(callBack));
			Element root = document.getRootElement();
			String rstCode = root.elementTextTrim("RstCode");
			String RstMsg = root.elementTextTrim("RstMsg");
			map.put("errMessage", RstMsg);
			if ("1".equals(rstCode)) {
				map.put("code", "1");
				String diagnosticsState = root.elementTextTrim("diagnosticsState");
				String pppoeName = root.elementTextTrim("pppoeName");
				//String status = root.elementTextTrim("status");
				String pppoeIP = root.elementTextTrim("pppoeIP");
				String Aspeed = root.elementTextTrim("Aspeed");
				String Bspeed = root.elementTextTrim("Bspeed");
				String Cspeed = root.elementTextTrim("Cspeed");
				String maxspeed = root.elementTextTrim("maxspeed");
				String starttime = root.elementTextTrim("starttime");
				String endtime = root.elementTextTrim("endtime");
				String testMode = root.elementTextTrim("testMode");
				map.put("diagnosticsState", StringUtil.IsEmpty(diagnosticsState) ? "" : diagnosticsState);
				//map.put("status", StringUtil.IsEmpty(status) ? "" : status);
				map.put("pppoeName", StringUtil.IsEmpty(pppoeName) ? "" : pppoeName);
				map.put("pppoeIP", StringUtil.IsEmpty(pppoeIP) ? "" : pppoeIP);
				map.put("Aspeed", StringUtil.IsEmpty(Aspeed) ? "" : Aspeed);
				map.put("Bspeed", StringUtil.IsEmpty(Bspeed) ? "" : Bspeed);
				map.put("Cspeed", StringUtil.IsEmpty(Cspeed) ? "" : Cspeed);
				map.put("maxspeed", StringUtil.IsEmpty(maxspeed) ? "" : maxspeed);
				map.put("starttime", StringUtil.IsEmpty(starttime) ? "" : starttime);
				map.put("testMode", StringUtil.IsEmpty(testMode) ? "" : testMode);
				map.put("endtime", StringUtil.IsEmpty(endtime) ? "" : endtime);
			} else {
				map.put("code", "1");
			}
		} catch (DocumentException e) {
			map.put("code", "1");
			map.put("errMessage", "解析PPPoE通道返回值有误!");
			logger.warn("解析PPPoE通道返回值有误!");
		}
		return map;
	}
	
	public Map<String, String> queryHTTPServiceSDDX(String idsShare_queryType, String netUsername, String column1) {
		Map<String, String> infoMap = dao.getInfoForSDDX(idsShare_queryType, netUsername);
		
		DateTimeUtil dt = new DateTimeUtil();
		String cmdId = StringUtil.getStringValue(dt.getLongTime());
		StringBuffer inParam = new StringBuffer();
		inParam.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>	\n");
		inParam.append("<root>										\n");
		inParam.append("	<CmdID>").append(cmdId).append("</CmdID>						\n");
		inParam.append("	<CmdType>CX_01</CmdType>					\n");
		inParam.append("	<ClientType>5</ClientType>						\n");
		inParam.append("	<Param>									\n");
		inParam.append("        <UserInfoType>").append(2).append("</UserInfoType>      \n");
		inParam.append("        <UserInfo>").append(StringUtil.getStringValue(infoMap, "loid")).append("</UserInfo>      \n");
		inParam.append("		<UserName>").append(StringUtil.getStringValue(infoMap, "username")).append("</UserName>	\n");
		inParam.append("		<PassWord>").append(StringUtil.getStringValue(infoMap, "passwd")).append("</PassWord>	\n");
		inParam.append("		<Speed>").append("").append("</Speed>	\n");
		inParam.append("		<DownURL>").append(column1).append("</DownURL>	\n");
		inParam.append("		<Priority>").append("1").append("</Priority>		\n");
		inParam.append("	</Param>								\n");
		inParam.append("</root>										\n");
		logger.warn("http:" + inParam.toString());
		final String url = LipossGlobals.getLipossProperty("IDSServiceUri");
		String methodName = "downLoadByHTTPSpead";
		String callBack = WSClientUtil.callItmsService(url, inParam.toString(), methodName);

		logger.warn("回参：" + callBack);
		// 解析回参
		SAXReader reader = new SAXReader();
		Document document = null;
		try {
			document = reader.read(new StringReader(callBack));
			Element root = document.getRootElement();
			String rstCode = root.elementTextTrim("RstCode");
			String RstMsg = root.elementTextTrim("RstMsg");
			infoMap.put("errMessage", RstMsg);
			if ("0".equals(rstCode)) {
				infoMap.put("code", "0");
				String speed = root.elementTextTrim("DSpeed");

				infoMap.put("speed", StringUtil.IsEmpty(speed) ? "" : speed);
			} else {
				infoMap.put("code", "1");
			}
		} catch (DocumentException e) {
			infoMap.put("code", "1");
			infoMap.put("errMessage", "解析PPPoE通道返回值有误!");
			logger.warn("解析PPPoE通道返回值有误!");
		}
		logger.warn("山东电信测速返回结果[{}]", infoMap);
		return infoMap;
	}
}
