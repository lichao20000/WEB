
package com.linkage.litms.resource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.linkage.commons.db.DBUtil;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ACS.DevRpc;
import ACS.Rpc;

import com.ailk.tr069.devrpc.obj.rpc.DevRpcCmdOBJ;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.acs.soap.io.XML;
import com.linkage.litms.acs.soap.io.XmlToRpc;
import com.linkage.litms.acs.soap.object.AnyObject;
import com.linkage.litms.acs.soap.object.ParameterValueStruct;
import com.linkage.litms.acs.soap.object.SoapOBJ;
import com.linkage.litms.acs.soap.service.FactoryReset;
import com.linkage.litms.acs.soap.service.GetParameterValues;
import com.linkage.litms.acs.soap.service.GetParameterValuesResponse;
import com.linkage.litms.acs.soap.service.Reboot;
import com.linkage.litms.acs.soap.service.SetParameterValues;
import com.linkage.litms.common.corba.DevRPCManager;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.module.gwms.Global;

/**
 * @author liuli
 * @version 1.00, 4/18/2007
 * @since jtwg_1.1.0 Modify Record: 2007-06-16 Alex.Yan (yanhj@lianchuang.com) RemoteDB
 *        ACS.
 */
public class Filedevice
{

	/** log */
	private static Logger logger = LoggerFactory.getLogger(Filedevice.class);


	/**
	 * 返回Ping测试诊断结果
	 * 
	 * @param request
	 * @return
	 */
	public String PingList(HttpServletRequest request)
	{
		String device_id = request.getParameter("device_id");
		
		//得到设备类型
		String gw_type = LipossGlobals.getGw_Type(device_id);

		String Interface = request.getParameter("Interface");
		String Host = request.getParameter("Host");
		String NumberOfRepetitions = request.getParameter("NumberOfRepetitions");
		String Timeout = request.getParameter("Timeout");
		String DataBlockSize = request.getParameter("DataBlockSize");
//		String strSQL1 = "select *  from tab_gw_device where device_id='" + device_id
//				+ "'";
//		PrepareSQL psql = new PrepareSQL(strSQL1);
//		psql.getSQL();
//		Cursor cursor1 = DataSetBean.getCursor(strSQL1);
//		Map fields1 = cursor1.getNext();
//		String out = (String) fields1.get("oui");
//		String SerialNumber = (String) fields1.get("device_serialnumber");
//		String ip = (String) fields1.get("loopback_ip");
//		String Port = (String) fields1.get("cr_port");
//		String path = (String) fields1.get("cr_path");
//		String username = (String) fields1.get("acs_username");
//		String passwd = (String) fields1.get("acs_passwd");
		// add by lizhaojun ----2007-06-21
		AnyObject anyObject = new AnyObject();
		SetParameterValues setParameterValues = new SetParameterValues();
		ParameterValueStruct[] ParameterValueStruct = new ParameterValueStruct[7];
		ParameterValueStruct[0] = new ParameterValueStruct();
		ParameterValueStruct[0]
				.setName("InternetGatewayDevice.IPPingDiagnostics.DiagnosticsState");
		anyObject.para_value = "Requested";
		anyObject.para_type_id = "1";
		ParameterValueStruct[0].setValue(anyObject);
		ParameterValueStruct[1] = new ParameterValueStruct();
		ParameterValueStruct[1]
				.setName("InternetGatewayDevice.IPPingDiagnostics.Interface");
		anyObject = new AnyObject();
		anyObject.para_value = Interface;
		anyObject.para_type_id = "1";
		ParameterValueStruct[1].setValue(anyObject);
		ParameterValueStruct[2] = new ParameterValueStruct();
		ParameterValueStruct[2].setName("InternetGatewayDevice.IPPingDiagnostics.Host");
		anyObject = new AnyObject();
		anyObject.para_value = Host;
		anyObject.para_type_id = "1";
		ParameterValueStruct[2].setValue(anyObject);
		ParameterValueStruct[3] = new ParameterValueStruct();
		ParameterValueStruct[3]
				.setName("InternetGatewayDevice.IPPingDiagnostics.NumberOfRepetitions");
		anyObject = new AnyObject();
		anyObject.para_value = NumberOfRepetitions;
		anyObject.para_type_id = "3";
		ParameterValueStruct[3].setValue(anyObject);
		ParameterValueStruct[4] = new ParameterValueStruct();
		ParameterValueStruct[4]
				.setName("InternetGatewayDevice.IPPingDiagnostics.Timeout");
		anyObject = new AnyObject();
		anyObject.para_value = Timeout;
		anyObject.para_type_id = "3";
		ParameterValueStruct[4].setValue(anyObject);
		ParameterValueStruct[5] = new ParameterValueStruct();
		ParameterValueStruct[5]
				.setName("InternetGatewayDevice.IPPingDiagnostics.DataBlockSize");
		anyObject = new AnyObject();
		anyObject.para_value = DataBlockSize;
		anyObject.para_type_id = "3";
		ParameterValueStruct[5].setValue(anyObject);
		ParameterValueStruct[6] = new ParameterValueStruct();
		ParameterValueStruct[6].setName("InternetGatewayDevice.IPPingDiagnostics.DSCP");
		anyObject = new AnyObject();
		anyObject.para_value = "0";
		anyObject.para_type_id = "1";
		ParameterValueStruct[6].setValue(anyObject);
		setParameterValues.setParameterList(ParameterValueStruct);
		setParameterValues.setParameterKey("Ping");
		GetParameterValues getParameterValues = new GetParameterValues();
		String[] parameterNamesArr = new String[5];
		parameterNamesArr[0] = "InternetGatewayDevice.IPPingDiagnostics.SuccessCount";
		parameterNamesArr[1] = "InternetGatewayDevice.IPPingDiagnostics.FailureCount";
		parameterNamesArr[2] = "InternetGatewayDevice.IPPingDiagnostics.AverageResponseTime";
		parameterNamesArr[3] = "InternetGatewayDevice.IPPingDiagnostics.MinimumResponseTime";
		parameterNamesArr[4] = "InternetGatewayDevice.IPPingDiagnostics.MaximumResponseTime";
		getParameterValues.setParameterNames(parameterNamesArr);
		String[] stringArr = new String[3];
		stringArr[0] = setParameterValues.toRPC();
		stringArr[1] = getParameterValues.toRPC();
		logger.debug("stringArr[1]==" + stringArr[1]);
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
		// corba
//		HttpSession session = request.getSession();
//		UserRes curUser = (UserRes) session.getAttribute("curUser");
//		User user = curUser.getUser();
//		String gather_id = request.getParameter("gather_id");
//		String object_name = "ACS_" + gather_id;
//		String object_Poaname = "ACS_Poa_" + gather_id;
//		String strIor = user.getIor(object_name, object_Poaname);
		List<DevRpcCmdOBJ> devRPCRep = null;
		DevRPCManager devRPCManager = new DevRPCManager(gw_type+"");
		devRPCRep = devRPCManager.execRPC(devRPCArr, Global.DiagCmd_Type);
		ParameterValueStruct[] pingStruct = new ParameterValueStruct[5];
		Map PingMap = new HashMap();
		if (devRPCRep == null || devRPCRep.size() == 0)
		{
			logger.warn("[{}]List<DevRpcCmdOBJ>返回为空！", device_id);
			return null;
		}
		else if (devRPCRep.get(0) == null)
		{
			logger.warn("[{}]DevRpcCmdOBJ返回为空！", device_id);
			return null;
		}
		else
		{
			int stat = devRPCRep.get(0).getStat();
			if (stat != 1)
			{
				return null;
			}
			else
			{
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
//								Fault fault = null;
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
//										fault = XmlToRpc.Fault(soapOBJ.getRpcElement());
										Element element = soapOBJ.getRpcElement();
										if (element != null)
										{
											GetParameterValuesResponse getParameterValuesResponse = XmlToRpc
													.GetParameterValuesResponse(element);
											if (getParameterValuesResponse != null)
											{
												ParameterValueStruct[] parameterValueStructArr = getParameterValuesResponse
														.getParameterList();
												PingMap = new HashMap<String, String>();
												for (int j = 0; j < parameterValueStructArr.length; j++)
												{
													PingMap
															.put(
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
				}
			}
		}
		String serviceHtml = "<table border='0' align='center' width='100%' cellpadding='0' cellspacing='1' bgcolor='#999999'>";
		if (null == pingStruct || PingMap == null)
		{
			serviceHtml += "<tr bgcolor='#FFFFFF'>";
			serviceHtml += "<td colspan='2'><span>无Ping测试返回信息</span></td></tr>";
		}
		else
		{
			serviceHtml += "<tr class='blue_foot'>";
			serviceHtml += "<td bgcolor='#FFFFFF' width='2%' nowrap>";
			serviceHtml += "成功数:";
			serviceHtml += "</td><td bgcolor='#FFFFFF' width='98%'>";
			serviceHtml += "&nbsp;"
					+ PingMap.get("InternetGatewayDevice.IPPingDiagnostics.SuccessCount");
			serviceHtml += "</td></tr>";
			serviceHtml += "<tr class='blue_foot'>";
			serviceHtml += "<td bgcolor='#FFFFFF' width='2%' nowrap>";
			serviceHtml += "失败数:";
			serviceHtml += "</td><td bgcolor='#FFFFFF' width='98%'>";
			serviceHtml += "&nbsp;"
					+ PingMap.get("InternetGatewayDevice.IPPingDiagnostics.FailureCount");
			serviceHtml += "</td></tr>";
			serviceHtml += "<tr class='blue_foot'>";
			serviceHtml += "<td bgcolor='#FFFFFF' width='2%' nowrap>";
			serviceHtml += "平均响应时间:";
			serviceHtml += "</td><td bgcolor='#FFFFFF' width='98%'>";
			serviceHtml += "&nbsp;"
					+ PingMap
							.get("InternetGatewayDevice.IPPingDiagnostics.AverageResponseTime");
			serviceHtml += "</td></tr>";
			serviceHtml += "<tr class='blue_foot'>";
			serviceHtml += "<td bgcolor='#FFFFFF' width='2%' nowrap>";
			serviceHtml += "最小响应时间:";
			serviceHtml += "</td><td bgcolor='#FFFFFF' width='98%'>";
			serviceHtml += "&nbsp;"
					+ PingMap
							.get("InternetGatewayDevice.IPPingDiagnostics.MinimumResponseTime");
			serviceHtml += "</td></tr>";
			serviceHtml += "<tr class='blue_foot'>";
			serviceHtml += "<td bgcolor='#FFFFFF' width='2%' nowrap>";
			serviceHtml += "最大响应时间:";
			serviceHtml += "</td><td bgcolor='#FFFFFF' width='98%'>";
			serviceHtml += "&nbsp;"
					+ PingMap
							.get("InternetGatewayDevice.IPPingDiagnostics.MaximumResponseTime");
			serviceHtml += "</td></tr>";
		}
		serviceHtml += "</table>";
		return serviceHtml;
	}

	/**
	 * 返回ATMF5LOOP测试诊断结果
	 * 
	 * @param request
	 * @return
	 */
	public String ATMF5LOOPList(HttpServletRequest request)
	{
		String device_id = request.getParameter("device_id");
		
		//得到设备类型
		String gw_type = LipossGlobals.getGw_Type(device_id);

		String NumberOfRepetions = request.getParameter("NumberOfRepetions");
		String Timeout = request.getParameter("Timeout");
		AnyObject anyObject = new AnyObject();
		SetParameterValues setParameterValues = new SetParameterValues();
		ParameterValueStruct[] ParameterValueStruct = new ParameterValueStruct[3];
		ParameterValueStruct[0] = new ParameterValueStruct();
		ParameterValueStruct[0]
				.setName("InternetGatewayDevice.WANDevice.1.WANConnectionDevice.1.WANATMF5LoopbackDiagnostics.DiagnosticsState");
		anyObject.para_value = "Requested";
		anyObject.para_type_id = "1";
		ParameterValueStruct[0].setValue(anyObject);
		ParameterValueStruct[1] = new ParameterValueStruct();
		ParameterValueStruct[1]
				.setName("InternetGatewayDevice.WANDevice.1.WANConnectionDevice.1.WANATMF5LoopbackDiagnostics.NumberOfRepetitions");
		anyObject = new AnyObject();
		anyObject.para_value = NumberOfRepetions;
		anyObject.para_type_id = "3";
		ParameterValueStruct[1].setValue(anyObject);
		ParameterValueStruct[2] = new ParameterValueStruct();
		ParameterValueStruct[2]
				.setName("InternetGatewayDevice.WANDevice.1.WANConnectionDevice.1.WANATMF5LoopbackDiagnostics.Timeout");
		anyObject = new AnyObject();
		anyObject.para_value = Timeout;
		anyObject.para_type_id = "3";
		ParameterValueStruct[2].setValue(anyObject);
		setParameterValues.setParameterList(ParameterValueStruct);
		setParameterValues.setParameterKey("ATMF5LOOP");
		GetParameterValues getParameterValues = new GetParameterValues();
		String[] parameterNamesArr = new String[5];
		parameterNamesArr[0] = "InternetGatewayDevice.WANDevice.1.WANConnectionDevice.1.WANATMF5LoopbackDiagnostics.SuccessCount";
		parameterNamesArr[1] = "InternetGatewayDevice.WANDevice.1.WANConnectionDevice.1.WANATMF5LoopbackDiagnostics.FailureCount";
		parameterNamesArr[2] = "InternetGatewayDevice.WANDevice.1.WANConnectionDevice.1.WANATMF5LoopbackDiagnostics.AverageResponseTime";
		parameterNamesArr[3] = "InternetGatewayDevice.WANDevice.1.WANConnectionDevice.1.WANATMF5LoopbackDiagnostics.MinimumResponseTime";
		parameterNamesArr[4] = "InternetGatewayDevice.WANDevice.1.WANConnectionDevice.1.WANATMF5LoopbackDiagnostics.MaximumResponseTime";
		getParameterValues.setParameterNames(parameterNamesArr);
		DevRpc[] devRPCArr = new DevRpc[1];
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
		// corba
		List<DevRpcCmdOBJ> devRPCRep = null;
		DevRPCManager devRPCManager = new DevRPCManager(gw_type);
		devRPCRep = devRPCManager.execRPC(devRPCArr, Global.DiagCmd_Type);
		// set
		// String setRes = devRPCRep[0].rpcArr[0];
		// get
		Map PingMap = null;
		ParameterValueStruct[] pingStruct = null;
		DevRpcCmdOBJ devRpcCmdOBJ = devRPCRep.get(0);
		if (devRpcCmdOBJ != null)
		{
//			String strSQL1 = "select *  from tab_gw_device where device_id='"
//					+ devRpcCmdOBJ.getDevId() + "'";
//			PrepareSQL psql = new PrepareSQL(strSQL1);
//			psql.getSQL();
//			Cursor cursor1 = DataSetBean.getCursor(strSQL1);
//			Map fields1 = cursor1.getNext();
//			String out = (String) fields1.get("oui");
//			String SerialNumber = (String) fields1.get("device_serialnumber");
//			String device_name = out + "-" + SerialNumber;
//			String errMessage = "";
			int stat = devRpcCmdOBJ.getStat();
			if (stat != 1)
			{
//				errMessage = Global.G_Fault_Map.get(stat).getFaultDesc();
			}
			else
			{
//				errMessage = "系统内部错误";
				if (devRpcCmdOBJ.getRpcList() == null
						|| devRpcCmdOBJ.getRpcList().size() == 0)
				{
					logger.warn("[{}]List<ACSRpcCmdOBJ>返回为空！", devRpcCmdOBJ.getDevId());
				}
				else
				{
					List<com.ailk.tr069.devrpc.obj.mq.Rpc> rpcList = devRpcCmdOBJ
							.getRpcList();
					if (rpcList != null && !rpcList.isEmpty())
					{
						for (int k = 0; k < rpcList.size(); k++)
						{
							if ("GetParameterValuesResponse".equals(rpcList.get(k)
									.getRpcName()))
							{
								String resp = rpcList.get(k).getValue();
								logger.warn("[{}]设备返回：{}", devRpcCmdOBJ.getDevId(), resp);
//								Fault fault = null;
								if (resp == null || "".equals(resp))
								{
									logger.debug("[{}]DevRpcCmdOBJ.value == null",
											devRpcCmdOBJ.getDevId());
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
												pingStruct = new ParameterValueStruct[5];
												pingStruct = getParameterValuesResponse
														.getParameterList();
												for (int l = 0; l < pingStruct.length; l++)
												{
													PingMap
															.put(
																	pingStruct[l]
																			.getName(),
																	pingStruct[l]
																			.getValue().para_value);
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
		}
		String serviceHtml = "<table border='0' align='center' width='100%' cellpadding='0' cellspacing='1' bgcolor='#999999'>";
		if (PingMap == null)
		{
			serviceHtml += "<tr bgcolor='#FFFFFF'>";
			serviceHtml += "<td colspan='2'><span>无ATMF5LOOP测试返回信息</span></td></tr>";
		}
		else
		{
			serviceHtml += "<tr class='blue_foot'>";
			serviceHtml += "<td bgcolor='#FFFFFF' width='2%' nowrap>";
			serviceHtml += "成功数:";
			serviceHtml += "</td><td bgcolor='#FFFFFF' width='98%'>";
			serviceHtml += "&nbsp;"
					+ PingMap
							.get("InternetGatewayDevice.WANDevice.1.WANConnectionDevice.1.WANATMF5LoopbackDiagnostics.SuccessCount");
			;
			serviceHtml += "</td></tr>";
			serviceHtml += "<tr class='blue_foot'>";
			serviceHtml += "<td bgcolor='#FFFFFF' width='2%' nowrap>";
			serviceHtml += "失败数:";
			serviceHtml += "</td><td bgcolor='#FFFFFF' width='98%'>";
			serviceHtml += "&nbsp;"
					+ PingMap
							.get("InternetGatewayDevice.WANDevice.1.WANConnectionDevice.1.WANATMF5LoopbackDiagnostics.FailureCount");
			;
			serviceHtml += "</td></tr>";
			serviceHtml += "<tr class='blue_foot'>";
			serviceHtml += "<td bgcolor='#FFFFFF' width='2%' nowrap>";
			serviceHtml += "平均响应时间:";
			serviceHtml += "</td><td bgcolor='#FFFFFF' width='98%'>";
			serviceHtml += "&nbsp;"
					+ PingMap
							.get("InternetGatewayDevice.WANDevice.1.WANConnectionDevice.1.WANATMF5LoopbackDiagnostics.AverageResponseTime");
			;
			serviceHtml += "</td></tr>";
			serviceHtml += "<tr class='blue_foot'>";
			serviceHtml += "<td bgcolor='#FFFFFF' width='2%' nowrap>";
			serviceHtml += "最小响应时间:";
			serviceHtml += "</td><td bgcolor='#FFFFFF' width='98%'>";
			serviceHtml += "&nbsp;"
					+ PingMap
							.get("InternetGatewayDevice.WANDevice.1.WANConnectionDevice.1.WANATMF5LoopbackDiagnostics.MinimumResponseTime");
			;
			serviceHtml += "</td></tr>";
			serviceHtml += "<tr class='blue_foot'>";
			serviceHtml += "<td bgcolor='#FFFFFF' width='2%' nowrap>";
			serviceHtml += "最大响应时间:";
			serviceHtml += "</td><td bgcolor='#FFFFFF' width='98%'>";
			serviceHtml += "&nbsp;"
					+ PingMap
							.get("InternetGatewayDevice.WANDevice.1.WANConnectionDevice.1.WANATMF5LoopbackDiagnostics.MaximumResponseTime");
			;
			serviceHtml += "</td></tr>";
		}
		serviceHtml += "</table>";
		return serviceHtml;
	}

	/**
	 * 返回重启测试诊断结果
	 * 
	 * @param request
	 * @return
	 */
	public String ChongqiList(HttpServletRequest request)
	{
		String device_id = request.getParameter("device_id");
		
		//得到设备类型
		String gw_type = LipossGlobals.getGw_Type(device_id);

		String strSQL1 = "select *  from tab_gw_device where device_id='" + device_id
				+ "'";
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			strSQL1 = "select oui, device_serialnumber from tab_gw_device where device_id='" + device_id
					+ "'";
		}
		PrepareSQL psql = new PrepareSQL(strSQL1);
		psql.getSQL();
		Cursor cursor1 = DataSetBean.getCursor(strSQL1);
		Map fields1 = cursor1.getNext();
		String out = (String) fields1.get("oui");
		String SerialNumber = (String) fields1.get("device_serialnumber");
//		String ip = (String) fields1.get("loopback_ip");
//		String Port = (String) fields1.get("cr_port");
//		String path = (String) fields1.get("cr_path");
//		String username = (String) fields1.get("acs_username");
//		String passwd = (String) fields1.get("acs_passwd");
		DevRpc[] devRPCArr = new DevRpc[1];
		Reboot reboot = new Reboot();
		reboot.setCommandKey("Reboot");
		devRPCArr[0] = new DevRpc();
		devRPCArr[0].devId = device_id;
		Rpc[] rpcArr = new Rpc[1];
		rpcArr[0] = new Rpc();
		rpcArr[0].rpcId = "1";
		rpcArr[0].rpcName = "Reboot";
		rpcArr[0].rpcValue = reboot.toRPC();
		devRPCArr[0].rpcArr = rpcArr;
		// corba
		DevRPCManager devRPCManager = new DevRPCManager(gw_type);
		List<DevRpcCmdOBJ> devRPCRep = devRPCManager.execRPC(devRPCArr,Global.RpcCmd_Type);
		// oui-device_serialnumber
		if (devRPCRep == null || devRPCRep.size() == 0 || devRPCRep.get(0) == null)
		{
			return null;
		}
		String serviceHtml = "<table border='0' align='center' width='100%' cellpadding='0' cellspacing='1' bgcolor='#999999'>";
		if (devRPCRep.get(0).getStat() != 1)
		{
			serviceHtml += "<tr class='blue_foot'>";
			serviceHtml += "<td bgcolor='#FFFFFF' width='2%' nowrap>";
			serviceHtml += out + "-" + SerialNumber;
			serviceHtml += ":</td><td bgcolor='#FFFFFF' width='98%'>";
			serviceHtml += "&nbsp;重启失败";
			serviceHtml += "</td></tr>";
		}
		else
		{
			serviceHtml += "<tr class='blue_foot'>";
			serviceHtml += "<td bgcolor='#FFFFFF' width='2%' nowrap>";
			serviceHtml += out + "-" + SerialNumber;
			serviceHtml += ":</td><td bgcolor='#FFFFFF' width='98%'>";
			serviceHtml += "&nbsp;重启成功";
			serviceHtml += "</td></tr>";
		}
		serviceHtml += "</table>";
		return serviceHtml;
	}

	/**
	 * 返回恢复出厂设置测试诊断结果
	 * 
	 * @param request
	 * @return
	 */
	public String HuifuList(HttpServletRequest request)
	{
		String device_id = request.getParameter("device_id");
		
		//得到设备类型
		String gw_type = LipossGlobals.getGw_Type(device_id);

		String strSQL1 = "select *  from tab_gw_device where device_id='" + device_id
				+ "'";
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			strSQL1 = "select oui, device_serialnumber from tab_gw_device where device_id='" + device_id
					+ "'";
		}
		PrepareSQL psql = new PrepareSQL(strSQL1);
		psql.getSQL();
		Cursor cursor1 = DataSetBean.getCursor(strSQL1);
		Map fields1 = cursor1.getNext();
		String out = (String) fields1.get("oui");
		String SerialNumber = (String) fields1.get("device_serialnumber");
		FactoryReset factoryReset = new FactoryReset();
		String[] stringArr = new String[1];
		stringArr[0] = factoryReset.toRPC();
		DevRpc[] devRPCArr = new DevRpc[1];
		devRPCArr[0] = new DevRpc();
		devRPCArr[0].devId = device_id;
		Rpc[] rpcArr = new Rpc[1];
		rpcArr[0] = new Rpc();
		rpcArr[0].rpcId = "1";
		rpcArr[0].rpcName = "FactoryReset";
		rpcArr[0].rpcValue = factoryReset.toRPC();
		devRPCArr[0].rpcArr = rpcArr;
		// corba
		DevRPCManager devRPCManager = new DevRPCManager(gw_type);
		List<DevRpcCmdOBJ> devRPCRep = devRPCManager.execRPC(devRPCArr,Global.RpcCmd_Type);
		// oui-device_serialnumber
		if (devRPCRep == null || devRPCRep.size() == 0 || devRPCRep.get(0) == null)
		{
			return null;
		}
		// oui-device_serialnumber
		String serviceHtml = "<table border='0' align='center' width='100%' cellpadding='0' cellspacing='1' bgcolor='#999999'>";
		if (devRPCRep.get(0).getStat() != 1)
		{
			serviceHtml += "<tr class='blue_foot'>";
			serviceHtml += "<td bgcolor='#FFFFFF' width='2%' nowrap>";
			serviceHtml += out + "-" + SerialNumber;
			serviceHtml += ":</td><td bgcolor='#FFFFFF' width='98%'>";
			serviceHtml += "&nbsp;恢复出厂设置失败";
			serviceHtml += "</td></tr>";
		}
		else
		{
			serviceHtml += "<tr class='blue_foot'>";
			serviceHtml += "<td bgcolor='#FFFFFF' width='2%' nowrap>";
			serviceHtml += out + "-" + SerialNumber;
			serviceHtml += ":</td><td bgcolor='#FFFFFF' width='98%'>";
			serviceHtml += "&nbsp;恢复出厂设置成功";
			serviceHtml += "</td></tr>";
		}
		serviceHtml += "</table>";
		return serviceHtml;
	}

	/**
	 * 返回DSL测试诊断结果
	 * 
	 * @param request
	 * @return
	 */
	public String DSLList(HttpServletRequest request)
	{
		logger.debug("DSL 测试诊断！");
		String device_id = request.getParameter("device_id");
		
		//得到设备类型
		String gw_type = LipossGlobals.getGw_Type(device_id);

//		String strSQL1 = "select *  from tab_gw_device where device_id='" + device_id
//				+ "'";
//		PrepareSQL psql = new PrepareSQL(strSQL1);
//		psql.getSQL();
//		Map fields1 = DataSetBean.getRecord(strSQL1);
//		String out = (String) fields1.get("oui");
//		String SerialNumber = (String) fields1.get("device_serialnumber");
//		String ip = (String) fields1.get("loopback_ip");
//		String Port = (String) fields1.get("cr_port");
//		String path = (String) fields1.get("cr_path");
//		String username = (String) fields1.get("acs_username");
//		String passwd = (String) fields1.get("acs_passwd");
		SetParameterValues setParameterValues = new SetParameterValues();
		ParameterValueStruct[] ParameterValueStruct = new ParameterValueStruct[1];
		ParameterValueStruct[0] = new ParameterValueStruct();
		ParameterValueStruct[0]
				.setName("InternetGatewayDevice.WANDevice.1.WANDSLDiagnostics.LoopDiagnosticsState");
		AnyObject anyObject = new AnyObject();
		anyObject.para_value = "Requested";
		anyObject.para_type_id = "1";
		ParameterValueStruct[0].setValue(anyObject);
		setParameterValues.setParameterList(ParameterValueStruct);
		setParameterValues.setParameterKey("DSL");
		GetParameterValues getParameterValues = new GetParameterValues();
		String[] parameterNamesArr = new String[10];
		parameterNamesArr[0] = "InternetGatewayDevice.WANDevice.1.WANDSLDiagnostics.ACTPSDds";
		parameterNamesArr[1] = "InternetGatewayDevice.WANDevice.1.WANDSLDiagnostics.ACTPSDus";
		parameterNamesArr[2] = "InternetGatewayDevice.WANDevice.1.WANDSLDiagnostics.ACTATPds";
		parameterNamesArr[3] = "InternetGatewayDevice.WANDevice.1.WANDSLDiagnostics.ACTATPus";
		parameterNamesArr[4] = "InternetGatewayDevice.WANDevice.1.WANDSLDiagnostics.HLINSCds";
		parameterNamesArr[5] = "InternetGatewayDevice.WANDevice.1.WANDSLDiagnostics.HLINpsds";
		parameterNamesArr[6] = "InternetGatewayDevice.WANDevice.1.WANDSLDiagnostics.QLNpsds";
		parameterNamesArr[7] = "InternetGatewayDevice.WANDevice.1.WANDSLDiagnostics.SNRpsds";
		parameterNamesArr[8] = "InternetGatewayDevice.WANDevice.1.WANDSLDiagnostics.BITSpsds";
		parameterNamesArr[9] = "InternetGatewayDevice.WANDevice.1.WANDSLDiagnostics.GAINSpsds";
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
		List<DevRpcCmdOBJ> devRPCRep = null;
		DevRPCManager devRPCManager = new DevRPCManager(gw_type+"");
		devRPCRep = devRPCManager.execRPC(devRPCArr, Global.DiagCmd_Type);
		if (devRPCRep == null || devRPCRep.size() == 0 || devRPCRep.get(0) == null)
		{
			return null;
		}
		String setRes = devRPCRep.get(0).getRpcList().get(1).getValue();
		logger.debug("DSL setRes！" + setRes);
		GetParameterValuesResponse getParameterValuesResponse = new GetParameterValuesResponse();
		SoapOBJ soapOBJ = XML.getSoabOBJ(XML.CreateXML(setRes));
		if (soapOBJ == null)
		{
			return null;
		}
		getParameterValuesResponse = XmlToRpc.GetParameterValuesResponse(soapOBJ
				.getRpcElement());
		ParameterValueStruct[] DSLStruct = new ParameterValueStruct[10];
		DSLStruct = getParameterValuesResponse.getParameterList();
		Map DSLMap = new HashMap();
		for (int i = 0; i < DSLStruct.length; i++)
		{
			DSLMap.put(DSLStruct[i].getName(), DSLStruct[i].getValue().para_value);
		}
		String serviceHtml = "<table border='0' align='center' width='100%' cellpadding='0' cellspacing='1' bgcolor='#999999'>";
		if (DSLMap == null)
		{
			serviceHtml += "<tr bgcolor='#FFFFFF'>";
			serviceHtml += "<td colspan='2'><span>无DSL测试返回信息</span></td></tr>";
		}
		else
		{
			serviceHtml += "<tr class='blue_foot'>";
			serviceHtml += "<td bgcolor='#FFFFFF' width='2%' nowrap>";
			serviceHtml += "ACTPSDds:";
			serviceHtml += "</td><td bgcolor='#FFFFFF' width='98%'>";
			serviceHtml += "&nbsp;"
					+ DSLMap
							.get("InternetGatewayDevice.WANDevice.1.WANDSLDiagnostics.ACTPSDds");
			serviceHtml += "</td></tr>";
			serviceHtml += "<tr class='blue_foot'>";
			serviceHtml += "<td bgcolor='#FFFFFF' width='2%' nowrap>";
			serviceHtml += "ACTPSDus:";
			serviceHtml += "</td><td bgcolor='#FFFFFF' width='98%'>";
			serviceHtml += "&nbsp;"
					+ DSLMap
							.get("InternetGatewayDevice.WANDevice.1.WANDSLDiagnostics.ACTPSDus");
			serviceHtml += "</td></tr>";
			serviceHtml += "<tr class='blue_foot'>";
			serviceHtml += "<td bgcolor='#FFFFFF' width='2%' nowrap>";
			serviceHtml += "ACTATPds:";
			serviceHtml += "</td><td bgcolor='#FFFFFF' width='98%'>";
			serviceHtml += "&nbsp;"
					+ DSLMap
							.get("InternetGatewayDevice.WANDevice.1.WANDSLDiagnostics.ACTATPds");
			serviceHtml += "</td></tr>";
			serviceHtml += "<tr class='blue_foot'>";
			serviceHtml += "<td bgcolor='#FFFFFF' width='2%' nowrap>";
			serviceHtml += "ACTATPus:";
			serviceHtml += "</td><td bgcolor='#FFFFFF' width='98%'>";
			serviceHtml += "&nbsp;"
					+ DSLMap
							.get("InternetGatewayDevice.WANDevice.1.WANDSLDiagnostics.ACTATPus");
			serviceHtml += "</td></tr>";
			serviceHtml += "<tr class='blue_foot'>";
			serviceHtml += "<td bgcolor='#FFFFFF' width='2%' nowrap>";
			serviceHtml += "HLINSCds:";
			serviceHtml += "</td><td bgcolor='#FFFFFF' width='98%'>";
			serviceHtml += "&nbsp;"
					+ DSLMap
							.get("InternetGatewayDevice.WANDevice.1.WANDSLDiagnostics.HLINSCds");
			serviceHtml += "</td></tr>";
			serviceHtml += "<tr class='blue_foot'>";
			serviceHtml += "<td bgcolor='#FFFFFF' width='2%' nowrap>";
			serviceHtml += "HLINpsds:";
			serviceHtml += "</td><td bgcolor='#FFFFFF' width='98%'>";
			serviceHtml += "&nbsp;"
					+ DSLMap
							.get("InternetGatewayDevice.WANDevice.1.WANDSLDiagnostics.HLINpsds");
			serviceHtml += "</td></tr>";
			serviceHtml += "<tr class='blue_foot'>";
			serviceHtml += "<td bgcolor='#FFFFFF' width='2%' nowrap>";
			serviceHtml += "QLNpsds:";
			serviceHtml += "</td><td bgcolor='#FFFFFF' width='98%'>";
			serviceHtml += "&nbsp;"
					+ DSLMap
							.get("InternetGatewayDevice.WANDevice.1.WANDSLDiagnostics.QLNpsds");
			serviceHtml += "</td></tr>";
			serviceHtml += "<tr class='blue_foot'>";
			serviceHtml += "<td bgcolor='#FFFFFF' width='2%' nowrap>";
			serviceHtml += "SNRpsds:";
			serviceHtml += "</td><td bgcolor='#FFFFFF' width='98%'>";
			serviceHtml += "&nbsp;"
					+ DSLMap
							.get("InternetGatewayDevice.WANDevice.1.WANDSLDiagnostics.SNRpsds");
			serviceHtml += "</td></tr>";
			serviceHtml += "<tr class='blue_foot'>";
			serviceHtml += "<td bgcolor='#FFFFFF' width='2%' nowrap>";
			serviceHtml += "BITSpsds:";
			serviceHtml += "</td><td bgcolor='#FFFFFF' width='98%'>";
			serviceHtml += "&nbsp;"
					+ DSLMap
							.get("InternetGatewayDevice.WANDevice.1.WANDSLDiagnostics.BITSpsds");
			serviceHtml += "</td></tr>";
			serviceHtml += "<tr class='blue_foot'>";
			serviceHtml += "<td bgcolor='#FFFFFF' width='2%' nowrap>";
			serviceHtml += "GAINSpsds:";
			serviceHtml += "</td><td bgcolor='#FFFFFF' width='98%'>";
			serviceHtml += "&nbsp;"
					+ DSLMap
							.get("InternetGatewayDevice.WANDevice.1.WANDSLDiagnostics.GAINSpsds");
			serviceHtml += "</td></tr>";
		}
		serviceHtml += "</table>";
		logger.debug("DSL serviceHtml！" + serviceHtml);
		return serviceHtml;
	}
}
