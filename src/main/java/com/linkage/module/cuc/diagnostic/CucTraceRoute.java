
package com.linkage.module.cuc.diagnostic;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.dom4j.Element;
import org.jfree.util.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ACS.DevRpc;
import ACS.Rpc;

import com.ailk.tr069.devrpc.obj.rpc.DevRpcCmdOBJ;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.acs.soap.io.XML;
import com.linkage.litms.acs.soap.io.XmlToRpc;
import com.linkage.litms.acs.soap.object.AnyObject;
import com.linkage.litms.acs.soap.object.ParameterInfoStruct;
import com.linkage.litms.acs.soap.object.ParameterValueStruct;
import com.linkage.litms.acs.soap.object.SoapOBJ;
import com.linkage.litms.acs.soap.service.GetParameterNames;
import com.linkage.litms.acs.soap.service.GetParameterNamesResponse;
import com.linkage.litms.acs.soap.service.GetParameterValues;
import com.linkage.litms.acs.soap.service.GetParameterValuesResponse;
import com.linkage.litms.acs.soap.service.SetParameterValues;
import com.linkage.litms.common.corba.DevRPCManager;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.module.gwms.Global;

public class CucTraceRoute
{
	/** log */
	private static Logger logger = LoggerFactory.getLogger(CucTraceRoute.class);

//	private static final Map<String,ICMPType> ICMPType = new HashMap<String, ICMPType>();
//
//	static
//	{
//
//	}

//	public static String getTraceRoteResult(HttpServletRequest request)
//	{
//		//查询类型(1:批量获取;2:分次获取)
//		String queryTypeStr = request.getParameter("queryType");
//		int queryType = Integer.parseInt(queryTypeStr == null? "1":queryTypeStr.trim());
//		//设备ID
//		String device_id = request.getParameter("device_id");
//		//Set条件
//		String Interface = request.getParameter("Interface");
//		String Host = request.getParameter("Host");
//		String MaxHopCount = request.getParameter("MaxHopCount");
//		String Timeout = request.getParameter("Timeout");
//
//
//		Map<String, String> osMap = new HashMap<String, String>();
//		String serviceHtml = "";
//		String strSQL1 = "select *  from tab_gw_device where device_id='" + device_id
//				+ "'";
//		PrepareSQL psql = new PrepareSQL(strSQL1);
//		psql.getSQL();
//		Cursor cursor1 = DataSetBean.getCursor(strSQL1);
//		Map fields1 = cursor1.getNext();
//		String out = (String) fields1.get("oui");
//		String SerialNumber = (String) fields1.get("device_serialnumber");
//		int gw_type = Integer.parseInt((String)fields1.get("gw_type"));
//		osMap.put(device_id, out + "-" + SerialNumber);
//		AnyObject anyObject = new AnyObject();
//		SetParameterValues setParameterValues = new SetParameterValues();
//		ParameterValueStruct[] ParameterValueStruct = new ParameterValueStruct[5];
//		//Requested
//		ParameterValueStruct[0] = new ParameterValueStruct();
//		ParameterValueStruct[0]
//				.setName("InternetGatewayDevice.TraceRouteDiagnostics.DiagnosticsState");
//		anyObject.para_value = "Requested";
//		anyObject.para_type_id = "1";
//		ParameterValueStruct[0].setValue(anyObject);
//		//接口，支持WAN和LAN
//		ParameterValueStruct[1] = new ParameterValueStruct();
//		ParameterValueStruct[1]
//				.setName("InternetGatewayDevice.TraceRouteDiagnostics.Interface");
//		anyObject = new AnyObject();
//		anyObject.para_value = Interface;
//		anyObject.para_type_id = "1";
//		ParameterValueStruct[1].setValue(anyObject);
//		//测试Host
//		ParameterValueStruct[2] = new ParameterValueStruct();
//		ParameterValueStruct[2].setName("InternetGatewayDevice.TraceRouteDiagnostics.Host");
//		anyObject = new AnyObject();
//		anyObject.para_value = Host;
//		anyObject.para_type_id = "1";
//		ParameterValueStruct[2].setValue(anyObject);
//		//最大跳转次数
//		ParameterValueStruct[3] = new ParameterValueStruct();
//		ParameterValueStruct[3]
//				.setName("InternetGatewayDevice.TraceRouteDiagnostics.MaxHopCount");
//		anyObject = new AnyObject();
//		anyObject.para_value = MaxHopCount;
//		anyObject.para_type_id = "3";
//		ParameterValueStruct[3].setValue(anyObject);
//		//超时时间
//		ParameterValueStruct[4] = new ParameterValueStruct();
//		ParameterValueStruct[4]
//				.setName("InternetGatewayDevice.TraceRouteDiagnostics.Timeout");
//		anyObject = new AnyObject();
//		anyObject.para_value = Timeout;
//		anyObject.para_type_id = "3";
//		ParameterValueStruct[4].setValue(anyObject);
//
//		setParameterValues.setParameterList(ParameterValueStruct);
//		setParameterValues.setParameterKey("TraceRoute");
//
//		//获取参数
//		GetParameterValues getParameterValues = new GetParameterValues();
//		String[] parameterNamesArr = new String[2];
//		parameterNamesArr[0] = "InternetGatewayDevice.TraceRouteDiagnostics.DiagnosticsState";
//		parameterNamesArr[1] = "InternetGatewayDevice.TraceRouteDiagnostics.RouteHopsNumberOfEntries";
//		getParameterValues.setParameterNames(parameterNamesArr);
//
//		DevRpc[] devRPCArr = new DevRpc[1];
//		devRPCArr[0] = new DevRpc();
//		devRPCArr[0].devId = device_id;
//		Rpc[] rpcArr = new Rpc[2];
//		rpcArr[0] = new Rpc();
//		rpcArr[0].rpcId = "1";
//		rpcArr[0].rpcName = "SetParameterValues";
//		rpcArr[0].rpcValue = setParameterValues.toRPC();
//
//		rpcArr[1] = new Rpc();
//		rpcArr[1].rpcId = "2";
//		rpcArr[1].rpcName = "GetParameterValues";
//		rpcArr[1].rpcValue = getParameterValues.toRPC();
//		devRPCArr[0].rpcArr = rpcArr;
//
//		DevRPCManager devRPCManager = new DevRPCManager(gw_type);
//		List<DevRpcCmdOBJ> devRPCRep = devRPCManager.execRPC(devRPCArr, Global.DiagCmd_Type);
//
//		//获取设备名
//		String device_name = osMap.get(device_id);
//
//		serviceHtml += "<table border='0' align='center' width='100%' cellpadding='0' cellspacing='1' bgcolor='#999999'>";
//		serviceHtml += "<tr class='blue_foot'>";
//		serviceHtml += "<td bgcolor='#FFFFFF' width='2%' nowrap>";
//		serviceHtml += "设备名称";
//		serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
//		serviceHtml += "路由跳转数";
//		serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
//		serviceHtml += "路由编号";
//		serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
//		serviceHtml += "跳转主机";
//		serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
//		serviceHtml += "跳转主机地址";
//		serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
//		serviceHtml += "跳转错误码";
//		serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
//		serviceHtml += "跳转往返时间";
//		serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
//		serviceHtml += "跳转时间参考值";
//		serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
//		serviceHtml += "专家建议";
//		serviceHtml += "</td></tr>";
//		//开始获取参数
//		String errMessage = "";
//		Map<String,String> resultMap = null;
//		FileSevice fileSevice = new FileSevice();
//		Map<String,String> expertMap = fileSevice.getSuggested(5);
//		int ex_bias = StringUtil.getIntegerValue(expertMap.get("ex_bias"));
//		String ex_succ_desc = expertMap.get("ex_succ_desc");
//		String ex_fault_desc = expertMap.get("ex_fault_desc");
//		String ex_regular = expertMap.get("ex_regular");
//		if (devRPCRep != null && devRPCRep.size() != 0 && devRPCRep.get(0) != null)
//		{
//			int stat = devRPCRep.get(0).getStat();
//			if (stat != 1)
//			{
//				errMessage = Global.G_Fault_Map.get(stat).getFaultDesc();
//			}
//			else
//			{
//				errMessage = "系统内部错误";
//				if (devRPCRep.get(0).getRpcList() == null
//						|| devRPCRep.get(0).getRpcList().size() == 0)
//				{
//					logger.warn("[{}]List<ACSRpcCmdOBJ>返回为空！", device_id);
//				}
//				else
//				{
//					resultMap = getResultMap(device_id,devRPCRep);
//				}
//			}
//
//
//			//结果集不为空,则继续查询下面的参数
//			if (null != resultMap)
//			{
//				// 获取跳转数
//				Map<String, Object> res = getRouteHops(devRPCManager, device_id);
//				errMessage = String.valueOf(res.get("errMessage"));
//				int[] routeHops = (int[])res.get("routeHops");
//				//没有错误，则继续查询
//				if(null == errMessage)
//				{
//					// 组装结果界面
//					res = getRouteHopResult(routeHops, queryType,
//							devRPCManager, device_id);
//					errMessage = String.valueOf(res.get("errMessage"));
//					if (null == errMessage)
//					{
//						serviceHtml += "<tr class='blue_foot'>";
//						serviceHtml += "<td bgcolor='#FFFFFF' width='2%' nowrap rowspan='" + routeHops.length + "'>";
//						serviceHtml += device_name;
//						serviceHtml += "<td bgcolor='#FFFFFF' width='2%' nowrap rowspan='" + routeHops.length + "'>";
//						serviceHtml += resultMap
//								.get("InternetGatewayDevice.TraceRouteDiagnostics.RouteHopsNumberOfEntries");
//						serviceHtml += "</td>";
//						resultMap = (Map<String, String>) res.get("resultMap");
//						int isFirst = 0;
//						// 添加结果行
//						for (int id : routeHops)
//						{
//							// 第一行要占用多行
//							if (0 != isFirst)
//							{
//								// 其他行则只占用一行
//								serviceHtml += "<tr>";
//							}
//							int hopRTTimes = StringUtil.getIntegerValue(resultMap.get("InternetGatewayDevice.TraceRouteDiagnostics.RouteHops." + id + ".HopRTTimes"));
//							serviceHtml += "<td bgcolor='#FFFFFF' width='2%' nowrap>";
//							serviceHtml += id;
//							serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
//							serviceHtml += resultMap.get("InternetGatewayDevice.TraceRouteDiagnostics.RouteHops." + id + ".HopHost");
//							serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
//							serviceHtml += resultMap.get("InternetGatewayDevice.TraceRouteDiagnostics.RouteHops." + id + ".HopHostAddress");
//							serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
//							serviceHtml += resultMap.get("InternetGatewayDevice.TraceRouteDiagnostics.RouteHops." + id + ".HopErrorCode");
//							serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
//							serviceHtml += hopRTTimes;
//							serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
//							serviceHtml += ex_bias;
//							serviceHtml += "</td>";
//							serviceHtml += fileSevice.judgeIntValue(ex_bias, hopRTTimes, ex_regular, 0);
//							serviceHtml += "</tr>";
//
//							isFirst = 1;
//						}
//					} else
//					{
//						serviceHtml += "<tr class='blue_foot'>";
//						serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
//						serviceHtml += device_name;
//						serviceHtml += "<td bgcolor='#FFFFFF' width='2%' nowrap>";
//						serviceHtml += resultMap.get("InternetGatewayDevice.TraceRouteDiagnostics.RouteHopsNumberOfEntries");
//						serviceHtml += "</td>";
//						// 获取节点失败
//						serviceHtml += "<td bgcolor='#FFFFFF' width='2%' nowrap colspan='5'>";
//						serviceHtml += "<font color=red>" + errMessage + "</font>";
//						serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
//						serviceHtml += ex_bias;
//						serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
//						serviceHtml += "<font color='red'>";
//						serviceHtml += ex_fault_desc;
//						serviceHtml += "</font>";
//						serviceHtml += "</td><tr>";
//					}
//					serviceHtml += "</tr>";
//				}else
//				{
//					serviceHtml += "<tr class='blue_foot'>";
//					serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
//					serviceHtml += device_name;
//					serviceHtml += "<td bgcolor='#FFFFFF' width='2%' nowrap>";
//					serviceHtml += resultMap.get("InternetGatewayDevice.TraceRouteDiagnostics.RouteHopsNumberOfEntries");
//					serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' colspan='5'>";
//					serviceHtml += "<font color=red>" + errMessage + "</font>";
//					serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
//					serviceHtml += ex_bias;
//					serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
//					serviceHtml += "<font color='red'>";
//					serviceHtml += ex_fault_desc;
//					serviceHtml += "</font>";
//					serviceHtml += "</td></tr>";
//				}
//			}else
//			{
//				//查询结果不为空，但是有异常
//				serviceHtml += "<tr bgcolor='#FFFFFF'>";
//				serviceHtml += "<td bgcolor='#FFFFFF' width='2%' nowrap>";
//				serviceHtml += device_name;
//				serviceHtml += "</td><td colspan='6'>" + "<font color=red>" + errMessage + "</font>";
//				serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
//				serviceHtml += ex_bias;
//				serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
//				serviceHtml += "<font color='red'>";
//				serviceHtml += ex_fault_desc;
//				serviceHtml += "</font>";
//				serviceHtml += "</td></tr>";
//			}
//		}else
//		{
//			logger.warn("[{}]List<DevRpcCmdOBJ>返回为空！", device_id);
//			serviceHtml += "<tr bgcolor='#FFFFFF'>";
//			serviceHtml += "<td bgcolor='#FFFFFF' width='2%' nowrap>";
//			serviceHtml += device_name;
//			serviceHtml += "</td><td colspan='6'>" + "<font color=red>" + "设备未知错误" + "</font>";
//			serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
//			serviceHtml += ex_bias;
//			serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
//			serviceHtml += "<font color='red'>";
//			serviceHtml += ex_fault_desc;
//			serviceHtml += "</font>";
//			serviceHtml += "</td></tr>";
//		}
//		serviceHtml += "</table>";
//		return serviceHtml;
//	}

	/**
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static String traceRoute(HttpServletRequest request)
	{
		//查询类型(1:批量获取;2:分次获取)
		String queryTypeStr = request.getParameter("queryType");
		int queryType = Integer.parseInt(queryTypeStr == null? "1":queryTypeStr.trim());
		//设备ID
		String device_id = request.getParameter("device_id");
		//Set条件
		String Interface = request.getParameter("Interface");
		String Host = request.getParameter("Host");
		String MaxHopCount = request.getParameter("MaxHopCount");
		String Timeout = request.getParameter("Timeout");
		String gw_type = request.getParameter("gw_type");

		Map<String, String> osMap = new HashMap<String, String>();
		String serviceHtml = "";
		String strSQL1 = "select oui, device_serialnumber from tab_gw_device where device_id='" + device_id
				+ "'";
		PrepareSQL psql = new PrepareSQL(strSQL1);
		psql.getSQL();
		Cursor cursor1 = DataSetBean.getCursor(strSQL1);
		Map fields1 = cursor1.getNext();
		String out = (String) fields1.get("oui");
		String SerialNumber = (String) fields1.get("device_serialnumber");

		osMap.put(device_id, out + "-" + SerialNumber);
		AnyObject anyObject = new AnyObject();
		SetParameterValues setParameterValues = new SetParameterValues();
		ParameterValueStruct[] ParameterValueStruct = new ParameterValueStruct[5];
		//Requested
		ParameterValueStruct[0] = new ParameterValueStruct();
		ParameterValueStruct[0]
				.setName("InternetGatewayDevice.TraceRouteDiagnostics.DiagnosticsState");
		anyObject.para_value = "Requested";
		anyObject.para_type_id = "1";
		ParameterValueStruct[0].setValue(anyObject);
		//接口，支持WAN和LAN
		ParameterValueStruct[1] = new ParameterValueStruct();
		ParameterValueStruct[1]
				.setName("InternetGatewayDevice.TraceRouteDiagnostics.Interface");
		anyObject = new AnyObject();
		anyObject.para_value = Interface;
		anyObject.para_type_id = "1";
		ParameterValueStruct[1].setValue(anyObject);
		//测试Host
		ParameterValueStruct[2] = new ParameterValueStruct();
		ParameterValueStruct[2].setName("InternetGatewayDevice.TraceRouteDiagnostics.Host");
		anyObject = new AnyObject();
		anyObject.para_value = Host;
		anyObject.para_type_id = "1";
		ParameterValueStruct[2].setValue(anyObject);
		//最大跳转次数
		ParameterValueStruct[3] = new ParameterValueStruct();
		ParameterValueStruct[3]
				.setName("InternetGatewayDevice.TraceRouteDiagnostics.MaxHopCount");
		anyObject = new AnyObject();
		anyObject.para_value = MaxHopCount;
		anyObject.para_type_id = "3";
		ParameterValueStruct[3].setValue(anyObject);
		//超时时间
		ParameterValueStruct[4] = new ParameterValueStruct();
		ParameterValueStruct[4]
				.setName("InternetGatewayDevice.TraceRouteDiagnostics.Timeout");
		anyObject = new AnyObject();
		anyObject.para_value = Timeout;
		anyObject.para_type_id = "3";
		ParameterValueStruct[4].setValue(anyObject);

		setParameterValues.setParameterList(ParameterValueStruct);
		setParameterValues.setParameterKey("TraceRoute");

		//获取参数
		GetParameterValues getParameterValues = new GetParameterValues();
		String[] parameterNamesArr = new String[2];
		parameterNamesArr[0] = "InternetGatewayDevice.TraceRouteDiagnostics.DiagnosticsState";
		parameterNamesArr[1] = "InternetGatewayDevice.TraceRouteDiagnostics.RouteHopsNumberOfEntries";
		logger.debug("parameterNamesArr{}", parameterNamesArr);
		getParameterValues.setParameterNames(parameterNamesArr);

		DevRpc[] devRPCArr = new DevRpc[1];
		devRPCArr[0] = new DevRpc();
		devRPCArr[0].devId = device_id;
		Rpc[] rpcArr = new Rpc[2];
		rpcArr[0] = new Rpc();
		rpcArr[0].rpcId = "1";
		rpcArr[0].rpcName = "SetParameterValues";
		rpcArr[0].rpcValue = setParameterValues.toRPC();

		rpcArr[1] = new Rpc();
		rpcArr[1].rpcId = "2";
		rpcArr[1].rpcName = "GetParameterValues";
		rpcArr[1].rpcValue = getParameterValues.toRPC();
		devRPCArr[0].rpcArr = rpcArr;

		DevRPCManager devRPCManager = new DevRPCManager(gw_type);
		List<DevRpcCmdOBJ> devRPCRep = devRPCManager.execRPC(devRPCArr, Global.DiagCmd_Type);

		//获取设备名
		String device_name = osMap.get(device_id);

		serviceHtml += "<table border='0' align='center' width='100%' cellpadding='0' cellspacing='1' bgcolor='#999999'>";
		serviceHtml += "<tr class='blue_foot'>";
		serviceHtml += "<td bgcolor='#FFFFFF' width='2%' nowrap>";
		serviceHtml += "设备名称";
		serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
		serviceHtml += "路由跳转数";
		serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
		serviceHtml += "路由编号";
		serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
		serviceHtml += "跳转主机";
		serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
		serviceHtml += "跳转主机地址";
		serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
		serviceHtml += "跳转错误码";
		serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
		serviceHtml += "跳转往返时间";
		serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
		serviceHtml += "跳转时间参考值";
		serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
		serviceHtml += "专家建议";
		serviceHtml += "</td></tr>";
		//开始获取参数
		String errMessage = "";
		Map<String,String> resultMap = null;
		CucFileSevice fileSevice = new CucFileSevice();
		Map<String,String> expertMap = fileSevice.getSuggested(5);
		int ex_bias = StringUtil.getIntegerValue(expertMap.get("ex_bias"));
		//String ex_succ_desc = expertMap.get("ex_succ_desc");
		String ex_fault_desc = expertMap.get("ex_fault_desc");
		String ex_regular = expertMap.get("ex_regular");
		if (devRPCRep != null && devRPCRep.size() != 0 && devRPCRep.get(0) != null)
		{
			int stat = devRPCRep.get(0).getStat();
			if (stat != 1)
			{
				errMessage = Global.G_Fault_Map.get(stat).getFaultDesc();
			}
			else
			{
				errMessage = "系统内部错误";
				if (devRPCRep.get(0).getRpcList() == null
						|| devRPCRep.get(0).getRpcList().size() == 0)
				{
					logger.warn("[{}]List<ACSRpcCmdOBJ>返回为空！", device_id);
				}
				else
				{
					resultMap = getResultMap(device_id,devRPCRep);
				}
			}

			//是否应该继续采集(默认不继续)
			boolean needContinue = false;

			//为空则代表前面没有采集到数据,所以现在才要继续看是否有结果
			Map<String, Object> res = null;
//			if(null == resultMap)
//			{
				//再次获取跳数
			devRPCManager = new DevRPCManager(gw_type);
				res = getRouteHops(devRPCManager, device_id);
//				int[] routeHops = (int[])res.get("routeHops");
				errMessage = (String)res.get("errMessage");
				//没有错误，则继续查询
				if(null == errMessage)
				{
					String.valueOf(res.get("routeHops"));
					needContinue = true;
				}
//			}

			//结果集不为空,则继续查询下面的参数
			if (needContinue)
			{
				// 获取跳转数
				//res = getRouteHops(devRPCManager, device_id);
				//errMessage = String.valueOf(res.get("errMessage"));
				int[] routeHops = (int[])res.get("routeHops");
				//没有错误，则继续查询
//				if(null == errMessage)
//				{
					// 组装结果界面
				devRPCManager = new DevRPCManager(gw_type);
					res = getRouteHopResult(routeHops, queryType,
							devRPCManager, device_id);
					errMessage = (String)res.get("errMessage");
					if (null == errMessage)
					{
						serviceHtml += "<tr class='blue_foot'>";
						serviceHtml += "<td bgcolor='#FFFFFF' width='2%' nowrap rowspan='" + routeHops.length + "'>";
						serviceHtml += device_name;
						serviceHtml += "<td bgcolor='#FFFFFF' width='2%' nowrap rowspan='" + routeHops.length + "'>";
						serviceHtml += routeHops.length;
						serviceHtml += "</td>";
						resultMap = (Map<String, String>) res.get("resultMap");
						int isFirst = 0;
						// 添加结果行
						for (int id : routeHops)
						{
							// 第一行要占用多行
							if (0 != isFirst)
							{
								// 其他行则只占用一行
								serviceHtml += "<tr>";
							}
							int hopRTTimes = StringUtil.getIntegerValue(resultMap.get("InternetGatewayDevice.TraceRouteDiagnostics.RouteHops." + id + ".HopRTTimes"));
							serviceHtml += "<td bgcolor='#FFFFFF' width='2%' nowrap>";
							serviceHtml += id;
							serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
							serviceHtml += resultMap.get("InternetGatewayDevice.TraceRouteDiagnostics.RouteHops." + id + ".HopHost");
							serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
							serviceHtml += resultMap.get("InternetGatewayDevice.TraceRouteDiagnostics.RouteHops." + id + ".HopHostAddress");
							serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
							serviceHtml += resultMap.get("InternetGatewayDevice.TraceRouteDiagnostics.RouteHops." + id + ".HopErrorCode");
							serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
							serviceHtml += hopRTTimes;
							serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
							serviceHtml += ex_bias;
							serviceHtml += "</td>";
							serviceHtml += fileSevice.judgeIntValue(ex_bias, hopRTTimes, ex_regular, 0);
							serviceHtml += "</tr>";

							isFirst = 1;
						}
					} else
					{
						serviceHtml += "<tr class='blue_foot'>";
						serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
						serviceHtml += device_name;
						serviceHtml += "<td bgcolor='#FFFFFF' width='2%' nowrap>";
						serviceHtml += routeHops.length;
						serviceHtml += "</td>";
						// 获取节点失败
						serviceHtml += "<td bgcolor='#FFFFFF' width='2%' nowrap colspan='5'>";
						serviceHtml += "<font color=red>" + errMessage + "</font>";
						serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
						serviceHtml += ex_bias;
						serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
						serviceHtml += "<font color='red'>";
						serviceHtml += ex_fault_desc;
						serviceHtml += "</font>";
						serviceHtml += "</td><tr>";
					}
					serviceHtml += "</tr>";
//				}else
//				{
//					serviceHtml += "<tr class='blue_foot'>";
//					serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
//					serviceHtml += device_name;
//					serviceHtml += "<td bgcolor='#FFFFFF' width='2%' nowrap>";
//					serviceHtml += resultMap.get("InternetGatewayDevice.TraceRouteDiagnostics.RouteHopsNumberOfEntries");
//					serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' colspan='5'>";
//					serviceHtml += "<font color=red>" + errMessage + "</font>";
//					serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
//					serviceHtml += ex_bias;
//					serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
//					serviceHtml += "<font color='red'>";
//					serviceHtml += ex_fault_desc;
//					serviceHtml += "</font>";
//					serviceHtml += "</td></tr>";
//				}
			}else
			{
				//查询结果不为空，但是有异常
				serviceHtml += "<tr bgcolor='#FFFFFF'>";
				serviceHtml += "<td bgcolor='#FFFFFF' width='2%' nowrap>";
				serviceHtml += device_name;
				serviceHtml += "</td><td colspan='6'>" + "<font color=red>" + errMessage + "</font>";
				serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
				serviceHtml += ex_bias;
				serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
				serviceHtml += "<font color='red'>";
				serviceHtml += ex_fault_desc;
				serviceHtml += "</font>";
				serviceHtml += "</td></tr>";
			}
		}else
		{
			logger.warn("[{}]List<DevRpcCmdOBJ>返回为空！", device_id);
			serviceHtml += "<tr bgcolor='#FFFFFF'>";
			serviceHtml += "<td bgcolor='#FFFFFF' width='2%' nowrap>";
			serviceHtml += device_name;
			serviceHtml += "</td><td colspan='6'>" + "<font color=red>" + "设备未知错误" + "</font>";
			serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
			serviceHtml += ex_bias;
			serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
			serviceHtml += "<font color='red'>";
			serviceHtml += ex_fault_desc;
			serviceHtml += "</font>";
			serviceHtml += "</td></tr>";
		}
		serviceHtml += "</table>";
		logger.debug("serviceHtml==>" + serviceHtml);
		return serviceHtml;
	}

	private static Map<String, String> getResultMap(String device_id,List<DevRpcCmdOBJ> devRPCRep)
	{
		Map<String, String> resMap = null;
		List<com.ailk.tr069.devrpc.obj.mq.Rpc> rpcList = devRPCRep.get(0).getRpcList();
		if (rpcList != null && !rpcList.isEmpty())
		{
			for (int k = 0; k < rpcList.size(); k++)
			{
				if ("GetParameterValuesResponse".equals(rpcList.get(k)
						.getRpcName()))
				{
					String resp = rpcList.get(k).getValue();
					logger.warn("[{}]设备返回：{}", device_id, resp);
					if (resp == null || "".equals(resp))
					{
						logger.debug("[{}]DevRpcCmdOBJ.value == null",
								device_id);
					} else
					{
						SoapOBJ soapOBJ = XML.getSoabOBJ(XML.CreateXML(resp));
						if (soapOBJ != null)
						{
							Element element = soapOBJ.getRpcElement();
							if (element != null)
							{
								GetParameterValuesResponse getParameterValuesResponse = XmlToRpc
										.GetParameterValuesResponse(element);
								if (getParameterValuesResponse != null)
								{
									ParameterValueStruct[] parameterValueStructArr = getParameterValuesResponse
											.getParameterList();
									resMap = new HashMap<String, String>();
									for (int j = 0; j < parameterValueStructArr.length; j++)
									{
										resMap.put(
												parameterValueStructArr[j]
														.getName(),
												parameterValueStructArr[j]
														.getValue().para_value);
									}
								}
							}
						}
					}
				}
			}
		}

		return resMap;
	}

	/**
	 * 获取路由跳转的结果集数目
	 * @param devRPCManager
	 * @param device_id
	 * @return
	 */
	private static Map<String,Object> getRouteHops(DevRPCManager devRPCManager,String device_id)
	{
		//动态获取节点值
		GetParameterNames getParameterNames = new GetParameterNames();
		String parameterNames = "InternetGatewayDevice.TraceRouteDiagnostics.RouteHops.";
		getParameterNames.setParameterPath(parameterNames);
		getParameterNames.setNextLevel(1);
		DevRpc[] devRPCArr = new DevRpc[1];
		devRPCArr[0] = new DevRpc();
		devRPCArr[0].devId = device_id;

		Rpc[] rpcArr = new Rpc[1];
		rpcArr[0] = new Rpc();
		rpcArr[0].rpcId = "1";
		rpcArr[0].rpcName = "GetParameterNames";
		rpcArr[0].rpcValue = getParameterNames.toRPC();
		devRPCArr[0].rpcArr = rpcArr;
		List<DevRpcCmdOBJ> devRPCRep = devRPCManager.execRPC(devRPCArr, Global.DiagCmd_Type);

		//结果个数获取完毕

		Map<String,Object> res = new HashMap<String,Object>();
		if (devRPCRep == null || devRPCRep.size() == 0 || devRPCRep.get(0) == null)
		{
			logger.warn("[{}]List<DevRpcCmdOBJ>返回为空！", device_id);
			res.put("errMessage","设备未知错误");
		}
		else
		{
			int stat = devRPCRep.get(0).getStat();
			if (stat != 1)
			{
				res.put("errMessage",Global.G_Fault_Map.get(stat).getFaultDesc());
			}
			else
			{
				res.put("errMessage","系统内部错误");
				if (devRPCRep.get(0).getRpcList() == null
						|| devRPCRep.get(0).getRpcList().size() == 0)
				{
					logger.warn("[{}]List<ACSRpcCmdOBJ>返回为空！", device_id);
				}
				else
				{
					List<com.ailk.tr069.devrpc.obj.mq.Rpc> rpcList = devRPCRep.get(0)
							.getRpcList();
					if (rpcList != null && !rpcList.isEmpty())
					{
						for (int k = 0; k < rpcList.size(); k++)
						{
							if ("GetParameterNamesResponse".equals(rpcList.get(k)
									.getRpcName()))
							{
								String resp = rpcList.get(k).getValue();
								logger.warn("[{}]设备返回：{}", device_id, resp);
								if (resp == null || "".equals(resp))
								{
									logger.debug("[{}]DevRpcCmdOBJ.value == null",
											device_id);
								}
								else
								{
									SoapOBJ soapOBJ = XML.getSoabOBJ(XML.CreateXML(resp));
									if (soapOBJ != null)
									{
										Element element = soapOBJ.getRpcElement();
										if (element != null)
										{
											GetParameterNamesResponse getParameterNamesResponse = XmlToRpc
													.GetParameterNamesResponse(element);
											if (getParameterNamesResponse != null)
											{
												ParameterInfoStruct[] parameterInfoStruct = getParameterNamesResponse.getParameterList();
												int[] routeHops = new int[parameterInfoStruct.length];

												for (int j = 0; j < parameterInfoStruct.length; j++)
												{
													//InternetGatewayDevice.TraceRouteDiagnostics.RouteHops.
													String name = parameterInfoStruct[j].getName();

													routeHops[j] = StringUtil.getIntegerValue(name.split("\\.")[3]);
												}
												//排序
												Arrays.sort(routeHops);
												res.put("routeHops", routeHops);
												res.remove("errMessage");
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}

		return res;
	}

//	/**
//	 * 获取路由跳数
//	 * @param queryType
//	 * @param devRPCManager
//	 * @param device_id
//	 * @return
//	 */
//	private static Map<String,String> getRouteHopsNumberOfEntries(int queryType,DevRPCManager devRPCManager,String device_id)
//	{
//		DevRpc[] devRPCArr = new DevRpc[1];
//		devRPCArr[0] = new DevRpc();
//		devRPCArr[0].devId = device_id;
//
//		Rpc[] rpcArr = new Rpc[1];
//
//		GetParameterValues getParameterValues = new GetParameterValues();
//		String[] parameterNamesArr = new String[1];
//		parameterNamesArr[0] = "InternetGatewayDevice.TraceRouteDiagnostics.RouteHopsNumberOfEntries";
//
//		getParameterValues.setParameterNames(parameterNamesArr);
//
//		rpcArr[0] = new Rpc();
//		rpcArr[0].rpcId = "1";
//		rpcArr[0].rpcName = "GetParameterValues";
//		rpcArr[0].rpcValue = getParameterValues.toRPC();
//		devRPCArr[0].rpcArr = rpcArr;
//		List<DevRpcCmdOBJ> devRPCRep = devRPCManager.execRPC(devRPCArr, Global.DiagCmd_Type);
//
//		Map<String,Object> res = new HashMap<String,Object>();
//
//		Map<String,String> resultMap = new HashMap<String, String>();
//
//		if (devRPCRep == null || devRPCRep.size() == 0)
//		{
//			logger.warn("[{}]List<DevRpcCmdOBJ>返回为空！", device_id);
//			res.put("errMessage", "设备未知错误");
//		}
//		else if (devRPCRep.get(0) == null)
//		{
//			logger.warn("[{}]DevRpcCmdOBJ返回为空！", device_id);
//			res.put("errMessage", "设备未知错误");
//		}
//		else
//		{
//			int stat = devRPCRep.get(0).getStat();
//			if (stat != 1)
//			{
//				res.put("errMessage", Global.G_Fault_Map.get(stat).getFaultDesc());
//			}
//			else
//			{
//				res.put("errMessage", "系统内部错误");
//				if (devRPCRep.get(0).getRpcList() == null
//						|| devRPCRep.get(0).getRpcList().size() == 0)
//				{
//					logger.warn("[{}]List<ACSRpcCmdOBJ>返回为空！", device_id);
//				}
//				else
//				{
//					List<com.ailk.tr069.devrpc.obj.mq.Rpc> rpcList = devRPCRep.get(0)
//							.getRpcList();
//					if (rpcList != null && !rpcList.isEmpty())
//					{
//						for (int k = 0; k < rpcList.size(); k++)
//						{
//							if ("GetParameterValuesResponse".equals(rpcList.get(k)
//									.getRpcName()))
//							{
//								String resp = rpcList.get(k).getValue();
//								logger.warn("[{}]设备返回：{}", device_id, resp);
//								if (resp == null || "".equals(resp))
//								{
//									logger.debug("[{}]DevRpcCmdOBJ.value == null",
//											device_id);
//								}
//								else
//								{
//									SoapOBJ soapOBJ = XML.getSoabOBJ(XML.CreateXML(resp));
//									if (soapOBJ != null)
//									{
//										Element element = soapOBJ.getRpcElement();
//										if (element != null)
//										{
//											GetParameterValuesResponse getParameterValuesResponse = XmlToRpc
//													.GetParameterValuesResponse(element);
//											if (getParameterValuesResponse != null)
//											{
//												ParameterValueStruct[] parameterValueStructArr = getParameterValuesResponse
//														.getParameterList();
//												for (int j = 0; j < parameterValueStructArr.length; j++)
//												{
//													String name = parameterValueStructArr[j].getName();
//													String value = parameterValueStructArr[j].getValue().para_value;
//													resultMap.put(name,value);
//												}
//											}
//										}
//									}
//								}
//							}
//						}
//					}
//				}
//			}
//		}
//		return resultMap;
//			//结果个数获取完毕
//	}

	/**
	 * 获取节点下的参数值
	 * @param routeHops
	 * @param id
	 * @param queryType
	 * @param devRPCManager
	 * @param device_id
	 * @return
	 */
	private static Map<String,Object> getRouteHopResult(int[] routeHops,int queryType,DevRPCManager devRPCManager,String device_id)
	{
		//FIXME 长度有可能为0
		int size = (1 == queryType) ? 1:routeHops.length;
//		logger.info("==============size:" + size);
//		logger.info("==============queryType:" + queryType);

		DevRpc[] devRPCArr = new DevRpc[1];
		devRPCArr[0] = new DevRpc();
		devRPCArr[0].devId = device_id;

		Rpc[] rpcArr = new Rpc[size];

		//1是全量查询，2是单次查询
		if(1 == queryType)
		{
			GetParameterValues getParameterValues = new GetParameterValues();
			String[] parameterNamesArr = new String[4*routeHops.length];

			for(int i = 0;i< routeHops.length ;i ++)
			{
				parameterNamesArr[0+ 4*i] = "InternetGatewayDevice.TraceRouteDiagnostics.RouteHops."+routeHops[i]+".HopHost";
				parameterNamesArr[1+ 4*i] = "InternetGatewayDevice.TraceRouteDiagnostics.RouteHops."+routeHops[i]+".HopHostAddress";
				parameterNamesArr[2+ 4*i] = "InternetGatewayDevice.TraceRouteDiagnostics.RouteHops."+routeHops[i]+".HopErrorCode";
				parameterNamesArr[3+ 4*i] = "InternetGatewayDevice.TraceRouteDiagnostics.RouteHops."+routeHops[i]+".HopRTTimes";
			}

			getParameterValues.setParameterNames(parameterNamesArr);

			rpcArr[0] = new Rpc();
			rpcArr[0].rpcId = "1";
			rpcArr[0].rpcName = "GetParameterValues";
			rpcArr[0].rpcValue = getParameterValues.toRPC();
		}else
		{
			for(int i = 0;i< routeHops.length ;i ++)
			{
				GetParameterValues getParameterValues = new GetParameterValues();
				String[] parameterNamesArr = new String[4];
				parameterNamesArr[0] = "InternetGatewayDevice.TraceRouteDiagnostics.RouteHops."+routeHops[i]+".HopHost";
				parameterNamesArr[1] = "InternetGatewayDevice.TraceRouteDiagnostics.RouteHops."+routeHops[i]+".HopHostAddress";
				parameterNamesArr[2] = "InternetGatewayDevice.TraceRouteDiagnostics.RouteHops."+routeHops[i]+".HopErrorCode";
				parameterNamesArr[3] = "InternetGatewayDevice.TraceRouteDiagnostics.RouteHops."+routeHops[i]+".HopRTTimes";

				getParameterValues.setParameterNames(parameterNamesArr);

				rpcArr[i] = new Rpc();
				rpcArr[i].rpcId = "" + (i + 1);
				rpcArr[i].rpcName = "GetParameterValues";
				rpcArr[i].rpcValue = getParameterValues.toRPC();
			}
		}

		devRPCArr[0].rpcArr = rpcArr;
		List<DevRpcCmdOBJ> devRPCRep = devRPCManager.execRPC(devRPCArr, Global.DiagCmd_Type);

		Map<String,Object> res = new HashMap<String,Object>();

		Map<String,String> resultMap = new HashMap<String, String>();

		if (devRPCRep == null || devRPCRep.size() == 0)
		{
			logger.warn("[{}]List<DevRpcCmdOBJ>返回为空！", device_id);
			res.put("errMessage", "设备未知错误");
		}
		else if (devRPCRep.get(0) == null)
		{
			logger.warn("[{}]DevRpcCmdOBJ返回为空！", device_id);
			res.put("errMessage", "设备未知错误");
		}
		else
		{
			int stat = devRPCRep.get(0).getStat();
			if (stat != 1)
			{
				res.put("errMessage", Global.G_Fault_Map.get(stat).getFaultDesc());
			}
			else
			{
				res.put("errMessage", "系统内部错误");
				if (devRPCRep.get(0).getRpcList() == null
						|| devRPCRep.get(0).getRpcList().size() == 0)
				{
					logger.warn("[{}]List<ACSRpcCmdOBJ>返回为空！", device_id);
				}
				else
				{
					List<com.ailk.tr069.devrpc.obj.mq.Rpc> rpcList = devRPCRep.get(0)
							.getRpcList();
					if (rpcList != null && !rpcList.isEmpty())
					{
						for (int k = 0; k < rpcList.size(); k++)
						{
							if ("GetParameterValuesResponse".equals(rpcList.get(k)
									.getRpcName()))
							{
								String resp = rpcList.get(k).getValue();
								logger.warn("[{}]设备返回：{}", device_id, resp);
								if (resp == null || "".equals(resp))
								{
									logger.debug("[{}]DevRpcCmdOBJ.value == null",
											device_id);
								}
								else
								{
									SoapOBJ soapOBJ = XML.getSoabOBJ(XML.CreateXML(resp));
									if (soapOBJ != null)
									{
										Element element = soapOBJ.getRpcElement();
										if (element != null)
										{
											GetParameterValuesResponse getParameterValuesResponse = XmlToRpc
													.GetParameterValuesResponse(element);
											if (getParameterValuesResponse != null)
											{
												ParameterValueStruct[] parameterValueStructArr = getParameterValuesResponse
														.getParameterList();
												for (int j = 0; j < parameterValueStructArr.length; j++)
												{
													String name = parameterValueStructArr[j].getName();
													String value = parameterValueStructArr[j].getValue().para_value;
													resultMap.put(name,value);
													res.remove("errMessage");
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
			//结果个数获取完毕
		}
		res.put("resultMap", resultMap);
		return res;
	}
}
