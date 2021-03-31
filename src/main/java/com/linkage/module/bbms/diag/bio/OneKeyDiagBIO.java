/**
 *
 */
package com.linkage.module.bbms.diag.bio;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ACS.DevRpc;
import ACS.Rpc;

import com.ailk.tr069.devrpc.obj.rpc.DevRpcCmdOBJ;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
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
import com.linkage.litms.paramConfig.ConfigDevice;
import com.linkage.litms.resource.FileSevice;
import com.linkage.module.bbms.diag.dao.OneKeyDiagDAO;
import com.linkage.module.gwms.ConstantClass;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.util.corba.SuperGatherCorba;

/**
 * @author chenjie
 * @date  2011-8-5
 */
public class OneKeyDiagBIO {

	/** log */
	private static Logger logger = LoggerFactory.getLogger(DeviceDiagBIO.class);

	private OneKeyDiagDAO dao;

	/**
	 * 返回DSL测试诊断结果 liuli@lianchuang.com
	 *
	 * @param device_id
	 * @param gw_type
	 * @return
	 */
	public String DSLList(String device_id, String dslWan, int gw_type)
	{
//		String device_id = request.getParameter("device_id");
//		String dslWan = request.getParameter("dslWan");
		String[] arrDevice_id = device_id.split(",");
		DevRpc[] devRPCArr = new DevRpc[arrDevice_id.length];
		logger.debug("LZJ>>>>> arrDevice_id.length :" + arrDevice_id.length);
		for (int i = 0; i < arrDevice_id.length; i++)
		{
			SetParameterValues setParameterValues = new SetParameterValues();
			ParameterValueStruct[] ParameterValueStruct = new ParameterValueStruct[1];
			ParameterValueStruct[0] = new ParameterValueStruct();
			ParameterValueStruct[0]
					.setName(dslWan + "WANDSLDiagnostics.LoopDiagnosticsState");
			AnyObject anyObject = new AnyObject();
			anyObject.para_value = "Requested";
			anyObject.para_type_id = "1";
			ParameterValueStruct[0].setValue(anyObject);
			setParameterValues.setParameterList(ParameterValueStruct);
			setParameterValues.setParameterKey("DSL");
			GetParameterValues getParameterValues = new GetParameterValues();
			String[] parameterNamesArr = new String[10];
			parameterNamesArr[0] = dslWan + "WANDSLDiagnostics.ACTPSDds";
			parameterNamesArr[1] = dslWan + "WANDSLDiagnostics.ACTPSDus";
			parameterNamesArr[2] = dslWan + "WANDSLDiagnostics.ACTATPds";
			parameterNamesArr[3] = dslWan + "WANDSLDiagnostics.ACTATPus";
			parameterNamesArr[4] = dslWan + "WANDSLDiagnostics.HLINSCds";
			parameterNamesArr[5] = dslWan + "WANDSLDiagnostics.HLINpsds";
			parameterNamesArr[6] = dslWan + "WANDSLDiagnostics.QLNpsds";
			parameterNamesArr[7] = dslWan + "WANDSLDiagnostics.SNRpsds";
			parameterNamesArr[8] = dslWan + "WANDSLDiagnostics.BITSpsds";
			parameterNamesArr[9] = dslWan + "WANDSLDiagnostics.GAINSpsds";
			getParameterValues.setParameterNames(parameterNamesArr);
			devRPCArr[i] = new DevRpc();
			devRPCArr[i].devId = device_id;
			Rpc[] rpcArr = new Rpc[2];
			rpcArr[0] = new Rpc();
			rpcArr[0].rpcId = "1";
			rpcArr[0].rpcName = "SetParameterValues";
			rpcArr[0].rpcValue = setParameterValues.toRPC();
			rpcArr[1] = new Rpc();
			rpcArr[1].rpcId = "2";
			rpcArr[1].rpcName = "GetParameterValues";
			rpcArr[1].rpcValue = getParameterValues.toRPC();
			devRPCArr[i].rpcArr = rpcArr;
		}
		// corba
		List<DevRpcCmdOBJ> devRpcCmdOBJList = null;
		DevRPCManager devRPCManager = new DevRPCManager(gw_type+"");
		devRpcCmdOBJList = devRPCManager.execRPC(devRPCArr, Global.DiagCmd_Type);
		String serviceHtml = "<table border='0' align='center' width='100%' cellpadding='0' cellspacing='1' bgcolor='#999999'>";
		serviceHtml += "<tr class='blue_foot'>";
		serviceHtml += "<td bgcolor='#FFFFFF' width='2%' nowrap>";
		serviceHtml += "设备名称";
		serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
		serviceHtml += "ACTPSDds(下行频谱宽度)";
		serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
		serviceHtml += "ACTPSDus(上行频谱宽度)";
		serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
		serviceHtml += "ACTATPds(DSL下行速率)";
		serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
		serviceHtml += "ACTATPus(DSL上行速率)";
		serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
		serviceHtml += "HLINSCds";
		serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
		serviceHtml += "HLINpsds";
		serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
		serviceHtml += "QLNpsds";
		serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
		serviceHtml += "SNRpsds";
		serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
		serviceHtml += "BITSpsds";
		serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
		serviceHtml += "GAINSpsds";
		serviceHtml += "</td></tr>";
		logger.debug("devRpcCmdOBJList.size--------------2222222222"
				+ devRpcCmdOBJList.size());

		StringBuffer sb = new StringBuffer();
		for (int j = 0; devRpcCmdOBJList != null && j < devRpcCmdOBJList.size(); j++)
		{
			Map DSLMap = null;
			ParameterValueStruct[] DSLStruct = null;
			DevRpcCmdOBJ devRpcCmdOBJ = devRpcCmdOBJList.get(j);
			if (devRpcCmdOBJ != null)
			{
				String strSQL1 = "select oui, device_serialnumber from tab_gw_device where device_id='"
						+ devRpcCmdOBJ.getDevId() + "'";
				PrepareSQL psql = new PrepareSQL(strSQL1);
				psql.getSQL();
				Cursor cursor1 = DataSetBean.getCursor(strSQL1);
				Map fields1 = cursor1.getNext();
				String out = (String) fields1.get("oui");
				String SerialNumber = (String) fields1.get("device_serialnumber");
				String device_name = out + "-" + SerialNumber;
				String errMessage = "";
				int stat = devRpcCmdOBJ.getStat();
				if (stat != 1)
				{
					errMessage = Global.G_Fault_Map.get(stat).getFaultDesc();
				}
				else
				{
					errMessage = "系统内部错误";
					if (devRpcCmdOBJ.getRpcList() == null
							|| devRpcCmdOBJ.getRpcList().size() == 0)
					{
						logger.warn("[{}]List<ACSRpcCmdOBJ>返回为空！", devRpcCmdOBJ
								.getDevId());
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
									logger.warn("[{}]设备返回：{}", devRpcCmdOBJ.getDevId(),
											resp);
//									Fault fault = null;
									if (resp == null || "".equals(resp))
									{
										logger.debug("[{}]DevRpcCmdOBJ.value == null",
												devRpcCmdOBJ.getDevId());
									}
									else
									{
										SoapOBJ soapOBJ = XML.getSoabOBJ(XML
												.CreateXML(resp));
										if (soapOBJ != null)
										{
											Element element = soapOBJ.getRpcElement();
											if (element != null)
											{
												GetParameterValuesResponse getParameterValuesResponse = XmlToRpc
														.GetParameterValuesResponse(element);
												if (getParameterValuesResponse != null)
												{
													DSLStruct = getParameterValuesResponse
															.getParameterList();
													DSLMap = new HashMap();
													for (int l = 0; l < DSLStruct.length; l++)
													{
														DSLMap
																.put(
																		DSLStruct[l]
																				.getName(),
																		DSLStruct[l]
																				.getValue().para_value
																				.replaceAll(
																						",",
																						",<br>"));
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
				if (null == DSLStruct || DSLMap == null)
				{
					serviceHtml += "<tr bgcolor='#FFFFFF'>";
					serviceHtml += "<td bgcolor='#FFFFFF' width='2%' nowrap>";
					serviceHtml += device_name;
					serviceHtml += "</td><td colspan='10'><span><font color=red>" + errMessage
							+ "</font></span></td></tr>";
				}
				else
				{
					serviceHtml += "<tr class='blue_foot'>";
					serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
					serviceHtml += device_name;
					serviceHtml += "<td bgcolor='#FFFFFF' width='2%' nowrap>";
					serviceHtml += DSLMap
							.get("InternetGatewayDevice.WANDevice.1.WANDSLDiagnostics.ACTPSDds");
					serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
					serviceHtml += DSLMap
							.get("InternetGatewayDevice.WANDevice.1.WANDSLDiagnostics.ACTPSDus");
					serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
					serviceHtml += DSLMap
							.get("InternetGatewayDevice.WANDevice.1.WANDSLDiagnostics.ACTATPds");
					serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
					serviceHtml += DSLMap
							.get("InternetGatewayDevice.WANDevice.1.WANDSLDiagnostics.ACTATPus");
					serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
					serviceHtml += DSLMap
							.get("InternetGatewayDevice.WANDevice.1.WANDSLDiagnostics.HLINSCds");
					serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
					serviceHtml += DSLMap
							.get("InternetGatewayDevice.WANDevice.1.WANDSLDiagnostics.HLINpsds");
					serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
					serviceHtml += DSLMap
							.get("InternetGatewayDevice.WANDevice.1.WANDSLDiagnostics.QLNpsds");
					serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
					serviceHtml += DSLMap
							.get("InternetGatewayDevice.WANDevice.1.WANDSLDiagnostics.SNRpsds");
					serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
					serviceHtml += DSLMap
							.get("InternetGatewayDevice.WANDevice.1.WANDSLDiagnostics.BITSpsds");
					serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
					serviceHtml += DSLMap
							.get("InternetGatewayDevice.WANDevice.1.WANDSLDiagnostics.GAINSpsds");
					serviceHtml += "</td></tr>";
				}

				// begin add by chenjie(67371) 2011-8-9 增加参考值和专家建议

				/**  参考值 **/
				sb.append("<tr class='blue_foot'>");
				sb.append("<td bgcolor='#FFFFFF' nowrap>参考值</td>");

				Map ACTPSDds_map = getSuggested(8);
				Map ACTPSDus_map = getSuggested(9);
				Map ACTATPds_map = getSuggested(10);
				Map ACTATPus_map = getSuggested(11);

				double ACTPSDds = Double.valueOf(StringUtil.getStringValue(ACTPSDds_map.get("ex_bias")));
				double ACTPSDds_orValue = Double.valueOf(StringUtil.getStringValue(ACTPSDds_map.get("ex_suggest")));
				sb.append("<td bgcolor='#FFFFFF' nowrap>");
				sb.append(ACTPSDds - ACTPSDds_orValue + " - " + Double.valueOf(ACTPSDds + ACTPSDds_orValue)).append("</td>");

				double ACTPSDus = Double.valueOf(StringUtil.getStringValue(ACTPSDus_map.get("ex_bias")));
				double ACTPSDus_orValue = Double.valueOf(StringUtil.getStringValue(ACTPSDus_map.get("ex_suggest")));
				sb.append("<td bgcolor='#FFFFFF' nowrap>");
				sb.append(ACTPSDus - ACTPSDus_orValue + " - " + Double.valueOf(ACTPSDds + ACTPSDds_orValue)).append("</td>");

				double ACTATPds = Double.valueOf(StringUtil.getStringValue(ACTATPds_map.get("ex_bias")));
				double ACTATPds_orValue = Double.valueOf(StringUtil.getStringValue(ACTATPds_map.get("ex_suggest")));
				sb.append("<td bgcolor='#FFFFFF' nowrap>");
				sb.append(ACTATPds - ACTATPds_orValue + " - " + Double.valueOf(ACTATPds + ACTATPds_orValue)).append("</td>");

				double ACTATPus = Double.valueOf(StringUtil.getStringValue(ACTATPus_map.get("ex_bias")));
				double ACTATPus_orValue = Double.valueOf(StringUtil.getStringValue(ACTATPus_map.get("ex_suggest")));
				sb.append("<td bgcolor='#FFFFFF' nowrap>");
				sb.append(ACTATPus - ACTATPus_orValue + " - " + Double.valueOf(ACTATPus + ACTATPus_orValue)).append("</td>");

				// 余下字段暂时为null
				sb.append("<td bgcolor='#FFFFFF' nowrap></td>");
				sb.append("<td bgcolor='#FFFFFF' nowrap></td>");
				sb.append("<td bgcolor='#FFFFFF' nowrap></td>");
				sb.append("<td bgcolor='#FFFFFF' nowrap></td>");
				sb.append("<td bgcolor='#FFFFFF' nowrap></td>");
				sb.append("<td bgcolor='#FFFFFF' nowrap></td>");
				sb.append("</tr>");

				if(DSLMap != null)
				{
				// 专家建议
				sb.append("<tr class='blue_foot'>");
				sb.append("<td bgcolor='#FFFFFF' nowrap>专家建议</td>");

				double ACTPSDds_fact = Double.valueOf(StringUtil.getStringValue(DSLMap.get("InternetGatewayDevice.WANDevice.1.WANDSLDiagnostics.ACTPSDds")));
				String ACTPSDds_regular = StringUtil.getStringValue(ACTPSDds_map.get("ex_regular"));
				sb.append(judgeIntValue(ACTPSDds, ACTPSDds_fact, ACTPSDds_regular, ACTPSDds_orValue));

				double ACTPSDus_fact = Double.valueOf(StringUtil.getStringValue(DSLMap.get("InternetGatewayDevice.WANDevice.1.WANDSLDiagnostics.ACTPSDus")));
				String ACTPSDus_regular = StringUtil.getStringValue(ACTPSDus_map.get("ex_regular"));
				sb.append(judgeIntValue(ACTPSDus, ACTPSDus_fact, ACTPSDus_regular, ACTPSDus_orValue));

				double ACTATPds_fact = Double.valueOf(StringUtil.getStringValue(DSLMap.get("InternetGatewayDevice.WANDevice.1.WANDSLDiagnostics.ACTATPds")));
				String ACTATPds_regular = StringUtil.getStringValue(ACTATPds_map.get("ex_regular"));
				sb.append(judgeIntValue(ACTATPds, ACTATPds_fact, ACTATPds_regular, ACTATPds_orValue));

				double ACTATPus_fact = Double.valueOf(StringUtil.getStringValue(DSLMap.get("InternetGatewayDevice.WANDevice.1.WANDSLDiagnostics.ACTATPus")));
				String ACTATPus_regular = StringUtil.getStringValue(ACTATPus_map.get("ex_regular"));
				sb.append(judgeIntValue(ACTATPus, ACTATPus_fact, ACTATPus_regular, ACTATPus_orValue));

				// 余下字段暂时为null
				sb.append("<td bgcolor='#FFFFFF' nowrap></td>");
				sb.append("<td bgcolor='#FFFFFF' nowrap></td>");
				sb.append("<td bgcolor='#FFFFFF' nowrap></td>");
				sb.append("<td bgcolor='#FFFFFF' nowrap></td>");
				sb.append("<td bgcolor='#FFFFFF' nowrap></td>");
				sb.append("<td bgcolor='#FFFFFF' nowrap></td>");
				sb.append("</tr>");
				}

				else
				{
					sb.append("<tr>");
					sb.append("<td bgcolor='#FFFFFF' nowrap>专家建议</td>");
					sb.append("<td bgcolor='#FFFFFF' nowrap colspan=10><font color=red>");
					sb.append("异常").append("</font></td></tr>");
				}
				// end add
			}
		}
		serviceHtml += sb.toString() + "</table>";
		return serviceHtml;
	}

	/**
	 * 获取DSL wan连接
	 * @param device_id
	 * @return
	 */
	public String getDslWAN(String device_id)
	{
		FileSevice serv = new FileSevice();
		return serv.getDslWAN(device_id);
	}

	public String getIntetface(String device_id)
	{

		logger.debug("getIntetface({})",device_id);
		//得到设备类型
		String gw_type = LipossGlobals.getGw_Type(device_id);
		SuperGatherCorba sgCorba = new SuperGatherCorba(gw_type+"");
//		String _Pvc = LipossGlobals.getLipossProperty("InstArea.PVC");
		String name = "";
		String value = "";
		String result = "";
		//只获取WAN口
		String wanConnDevice = "InternetGatewayDevice.WANDevice.1.WANConnectionDevice.";
		String _interList = "<select name=Interface id=Interface class='bk'>";
		boolean flag = false;
		// 获取Wan
		// 1、调用采集,采集InternetGatewayDevice.WANDevice下节点
		int irt = sgCorba.getCpeParams(device_id, ConstantClass.GATHER_WAN, 1);
		logger.warn("调用采集结果=====" + irt);
		if (irt != 1)
		{
			logger.warn("调用采集失败");
		}
		else
		{
			// 2、从数据库获取wan_conn_id/wan_conn_sess_id
			List<Map> wanConnIds = getWanConnIds(device_id);
			if (wanConnIds == null || wanConnIds.isEmpty())
			{
				logger.warn("没有获取到Wan接口");
			}
			else
			{
				flag = true;
				for (Map map : wanConnIds)
				{
					String wan_conn_id = StringUtil
							.getStringValue(map.get("wan_conn_id"));
					String wan_conn_sess_id = StringUtil.getStringValue(map
							.get("wan_conn_sess_id"));
					String pvc = StringUtil.getStringValue(map.get("pvc"));
					String vlanid = StringUtil.getStringValue(map.get("vlan_id"));
					String sessType = StringUtil.getStringValue(map.get("sess_type"));
					String serv_list = StringUtil.getStringValue(map.get("serv_list"));
					String tmp = "";
					if (vlanid == null || vlanid.equals("") || vlanid.equals("NULL"))
					{
						tmp = "pvc:" + pvc;
					}
					else
					{
						tmp = "vlanid:" + vlanid;
					}
					if (sessType.equals("1"))
					{
						name = "Wan(" + "WANPPPConnection/" + tmp + "):" + wan_conn_id
								+ "-" + wan_conn_sess_id;
						value = wanConnDevice + wan_conn_id + ".WANPPPConnection."
								+ wan_conn_sess_id + ".";
					}
					else if (sessType.equals("2"))
					{
						name = "Wan(" + "WANIPConnection/" + tmp + "):" + wan_conn_id
								+ "-" + wan_conn_sess_id;
						value = wanConnDevice + wan_conn_id + ".WANIPConnection."
								+ wan_conn_sess_id + ".";
					}
					else
					{
						logger.warn("sessType值不对：" + sessType);
						continue;
					}
					logger.debug("wanppp ping_value :" + value);
					_interList += "<option value='" + value + "'>" + name + "</option>";
					result = value;
				}
			}
		}
		// 3、封装listBox
		_interList += "</select>";
		if (!flag)
		{
			_interList = "";
		}
		logger.warn("interface:" + result);
		return result;

	}

	private List getWanConnIds(String device_id)
	{
		StringBuffer sql = new StringBuffer();
		List<Map> list = new ArrayList<Map>();
		sql.append("select b.sess_type,b.serv_list,a.vlan_id, a.vpi_id, a.vci_id,b.wan_conn_id,b.wan_conn_sess_id " +
                "from gw_wan_conn_bbms a, gw_wan_conn_session_bbms b " +
                "where a.device_id=b.device_id and a.wan_id=b.wan_id and a.wan_conn_id=b.wan_conn_id " +
                "and upper(b.serv_list) like '%INTERNET%' and a.device_id='");
		sql.append(device_id).append("'");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(sql.toString());

		for (int i = 0; i < cursor.getRecordSize(); i++)
		{
            Map record = cursor.getRecord(i);
            String vpi_id = StringUtil.getStringValue(record, "vpi_id");
            String vci_id = StringUtil.getStringValue(record, "vci_id");
            record.put("pvc", vpi_id + "/" + vci_id);
            list.add(record);
		}
		return list;
	}

	/*************************************************************************************************************************/
	/**
	 * tr069 和 snmp 设备PING 操作
	 *
	 * @author lizj （5202）
	 * @param request
	 * @return
	 */
	public String allPingResult(String deviceId, String Interface, String dataBlockSize, String host, String numberOfRepetitions, String timeout)
	{
		logger.debug("allPingResult({},{},{},{},{},{})", new Object[]{deviceId, Interface, dataBlockSize, host, numberOfRepetitions, timeout });
		// 返回结果
		String strResult = "";
		// 如果是tr069设备，调用ACS 接口
		strResult = PingList(deviceId, Interface, dataBlockSize, host, numberOfRepetitions, timeout);
		return strResult;
	}

	public String PingList(String device_id, String Interface, String dataBlockSize, String host, String numberOfRepetitions, String timeout)
	{
		logger.debug("PingList({},{},{},{},{},{})", new Object[]{device_id, Interface, dataBlockSize, host, numberOfRepetitions, timeout });
		//得到设备类型
		String gw_type = LipossGlobals.getGw_Type(device_id);
//		String gather_id = "";
		// add by panyin for bug XJDX-ITMS-BUG-20101117-XXF-001 lan口ping测试接口值不对 begin
		if (Interface
				.startsWith("InternetGatewayDevice.LANDevice.1.LANEthernetInterfaceConfig"))
		{
			Interface = "InternetGatewayDevice.LANDevice.1.LANHostConfigManagement.IPInterface.1";
			logger
					.debug("设置ping操作的LAN端口为：InternetGatewayDevice.LANDevice.1.LANHostConfigManagement.IPInterface.1");
		}
		// add by panyin for bug XJDX-ITMS-BUG-20101117-XXF-001 lan口ping测试接口值不对 end
		// String DSCP = request.getParameter("DSCP");
		// String strAction = request.getParameter("action");
		DevRpc[] devRPCArr = new DevRpc[1];

		Map<String, String> osMap = new HashMap<String, String>();
		String serviceHtml = "";
		String strSQL1 = "select oui, device_serialnumber from tab_gw_device where device_id='" + device_id
				+ "'";
		PrepareSQL psql = new PrepareSQL(strSQL1);
		psql.getSQL();
		Cursor cursor1 = DataSetBean.getCursor(strSQL1);
		Map fields1 = cursor1.getNext();
		// begin add by w5221 修改按照用户查询没有采集点信息的参数
		// gather_id = (String) fields1.get("gather_id");
		// end add by w5221 修改按照用户查询没有采集点信息的参数
		String out = (String) fields1.get("oui");
		String SerialNumber = (String) fields1.get("device_serialnumber");
		// String ip = (String) fields1.get("loopback_ip");
		// String Port = (String) fields1.get("cr_port");
		// String path = (String) fields1.get("cr_path");
		// String username = (String) fields1.get("acs_username");
		// String passwd = (String) fields1.get("acs_passwd");
		// add by lizhaojun ----2007-06-21
		osMap.put(device_id, out + "-" + SerialNumber);
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
		anyObject.para_value = Interface;// + ".";严海建要求去掉最后一个点
		anyObject.para_type_id = "1";
		ParameterValueStruct[1].setValue(anyObject);
		ParameterValueStruct[2] = new ParameterValueStruct();
		ParameterValueStruct[2].setName("InternetGatewayDevice.IPPingDiagnostics.Host");
		anyObject = new AnyObject();
		anyObject.para_value = host;
		anyObject.para_type_id = "1";
		ParameterValueStruct[2].setValue(anyObject);
		ParameterValueStruct[3] = new ParameterValueStruct();
		ParameterValueStruct[3]
				.setName("InternetGatewayDevice.IPPingDiagnostics.NumberOfRepetitions");
		anyObject = new AnyObject();
		anyObject.para_value = numberOfRepetitions;
		anyObject.para_type_id = "3";
		ParameterValueStruct[3].setValue(anyObject);
		ParameterValueStruct[4] = new ParameterValueStruct();
		ParameterValueStruct[4]
				.setName("InternetGatewayDevice.IPPingDiagnostics.Timeout");
		anyObject = new AnyObject();
		anyObject.para_value = timeout;
		anyObject.para_type_id = "3";
		ParameterValueStruct[4].setValue(anyObject);
		ParameterValueStruct[5] = new ParameterValueStruct();
		ParameterValueStruct[5]
				.setName("InternetGatewayDevice.IPPingDiagnostics.DataBlockSize");
		anyObject = new AnyObject();
		anyObject.para_value = dataBlockSize;
		anyObject.para_type_id = "3";
		ParameterValueStruct[5].setValue(anyObject);
		ParameterValueStruct[6] = new ParameterValueStruct();
		ParameterValueStruct[6].setName("InternetGatewayDevice.IPPingDiagnostics.DSCP");
		anyObject = new AnyObject();
		anyObject.para_value = "0";
		// unsignedInt
		anyObject.para_type_id = "3";
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
		// HttpSession session = request.getSession();
		// UserRes curUser = (UserRes) session.getAttribute("curUser");
		// User user = curUser.getUser();
		List<DevRpcCmdOBJ> devRPCRep = null;
		DevRPCManager devRPCManager = new DevRPCManager(gw_type+"");
		devRPCRep = devRPCManager.execRPC(devRPCArr, Global.DiagCmd_Type);
		serviceHtml += "<table border='0' align='center' width='100%' cellpadding='0' cellspacing='1' bgcolor='#999999'>";
		serviceHtml += "<tr class='blue_foot'>";
		serviceHtml += "<td bgcolor='#FFFFFF' width='2%' nowrap>";
		serviceHtml += "设备名称";
		serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
		serviceHtml += "成功数";
		serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
		serviceHtml += "失败数";
		serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
		serviceHtml += "平均响应时间";
		serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
		serviceHtml += "最小响应时间";
		serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
		serviceHtml += "最大响应时间";
		serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
		serviceHtml += "丢包率";
		serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
		serviceHtml += "IP地址";
		serviceHtml += "</td></tr>";
		String errMessage = "";
		Map PingMap = null;
		if (devRPCRep == null || devRPCRep.size() == 0)
		{
			logger.warn("[{}]List<DevRpcCmdOBJ>返回为空！", device_id);
			errMessage = "设备未知错误";
		}
		else if (devRPCRep.get(0) == null)
		{
			logger.warn("[{}]DevRpcCmdOBJ返回为空！", device_id);
			errMessage = "设备未知错误";
		}
		else
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
			logger.debug("GSJ------------------------:::" + devRPCRep.size());
			logger.debug("GSJ------------------------:::" + devRPCRep.get(0).getStat());
			String device_name = osMap.get(device_id);
			if (PingMap == null)
			{
				serviceHtml += "<tr bgcolor='#FFFFFF'>";
				serviceHtml += "<td bgcolor='#FFFFFF' width='2%' nowrap>";
				serviceHtml += device_name;
				serviceHtml += "</td><td colspan='7'><span><font color=red>" + errMessage
						+ "</font></span></td></tr>";
			}
			else
			{
				logger.debug("F-----------:" + PingMap);
				serviceHtml += "<tr class='blue_foot'>";
				serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
				serviceHtml += device_name;
				serviceHtml += "<td bgcolor='#FFFFFF' width='2%' nowrap>";
				serviceHtml += PingMap
						.get("InternetGatewayDevice.IPPingDiagnostics.SuccessCount");
				serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
				serviceHtml += PingMap
						.get("InternetGatewayDevice.IPPingDiagnostics.FailureCount");
				serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
				serviceHtml += PingMap
						.get("InternetGatewayDevice.IPPingDiagnostics.AverageResponseTime");
				serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
				serviceHtml += PingMap
						.get("InternetGatewayDevice.IPPingDiagnostics.MinimumResponseTime");
				serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
				serviceHtml += PingMap
						.get("InternetGatewayDevice.IPPingDiagnostics.MaximumResponseTime");
				serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
				serviceHtml += (StringUtil.getIntegerValue(PingMap
				.get("InternetGatewayDevice.IPPingDiagnostics.FailureCount"))/StringUtil.getIntegerValue(numberOfRepetitions))*100+""+"%";
				serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
				serviceHtml += host;
				serviceHtml += "</td></tr>";
			}
		}

		// begin add by chenjie(67371) 2011-8-9 增加参考值和专家建议

		StringBuffer sb = new StringBuffer();
		/**  参考值 **/
		sb.append("<tr class='blue_foot'>");
		sb.append("<td bgcolor='#FFFFFF' nowrap>参考值</td>");
		// 成功数
		sb.append("<td bgcolor='#FFFFFF' nowrap>");
		sb.append(numberOfRepetitions).append("</td>");
		// 失败数
		sb.append("<td bgcolor='#FFFFFF' nowrap>");
		sb.append(0).append("</td>");

		Map pingSuggestMap =  getSuggested(4);
		// 参考值
		String biasValue = (String)pingSuggestMap.get("ex_bias");

		// 平均响应时间
		sb.append("<td bgcolor='#FFFFFF' nowrap>");
		sb.append(biasValue).append("</td>");

		// 最小响应时间
		sb.append("<td bgcolor='#FFFFFF' nowrap>");
		sb.append(biasValue).append("</td>");

		// 最大响应时间
		sb.append("<td bgcolor='#FFFFFF' nowrap>");
		sb.append(biasValue).append("</td>");

		// 丢包率
		sb.append("<td bgcolor='#FFFFFF' nowrap>");
		sb.append(0).append("</td>");

		// ip地址
		sb.append("<td bgcolor='#FFFFFF' nowrap>");
		sb.append("").append("</td></tr>");

		if(PingMap != null){

			/**  专家建议 **/

			String suggestRegular = (String)pingSuggestMap.get("ex_regular");

			sb.append("<tr class='blue_foot'>");
			sb.append("<td bgcolor='#FFFFFF' nowrap>专家建议</td>");

			// 成功数
			int success_count = Integer.valueOf(String.valueOf(PingMap.get("InternetGatewayDevice.IPPingDiagnostics.SuccessCount")));
//			int numberOfRepetitions = Integer.parseInt(NumberOfRepetitions);
			sb.append(judgeIntValue(Integer.parseInt(numberOfRepetitions), success_count, "=", 0));

			// 失败数
			int fail_count = Integer.valueOf(String.valueOf(PingMap.get("InternetGatewayDevice.IPPingDiagnostics.FailureCount")));
			sb.append(judgeIntValue(0, fail_count, "=", 0));

			// 响应时间
			int average_time = Integer.valueOf(String.valueOf(PingMap.get("InternetGatewayDevice.IPPingDiagnostics.AverageResponseTime")));
			sb.append(judgeIntValue(Integer.parseInt(biasValue), average_time, suggestRegular, 0));

			int min_time = Integer.valueOf(String.valueOf(PingMap.get("InternetGatewayDevice.IPPingDiagnostics.MinimumResponseTime")));
			sb.append(judgeIntValue(Integer.parseInt(biasValue), min_time, suggestRegular, 0));

			int max_time = Integer.valueOf(String.valueOf(PingMap.get("InternetGatewayDevice.IPPingDiagnostics.MaximumResponseTime")));
			sb.append(judgeIntValue(Integer.parseInt(biasValue), max_time, suggestRegular, 0));

			// 丢包率
			int failure_count = (StringUtil.getIntegerValue(PingMap
					.get("InternetGatewayDevice.IPPingDiagnostics.FailureCount"))/StringUtil.getIntegerValue(numberOfRepetitions))*100;
			sb.append(judgeIntValue(0, failure_count, "=", 0));

			// host
			sb.append("<td bgcolor='#FFFFFF' nowrap>");
			sb.append("").append("</td>");

			//
			sb.append("</tr>");

			// end add

			serviceHtml += sb.toString() + "</table>";
		}
		else
		{
			sb.append("<tr>");
			sb.append("<td bgcolor='#FFFFFF' nowrap>专家建议</td>");
			sb.append("<td bgcolor='#FFFFFF' nowrap colspan=7><font color=red>");
			sb.append("异常").append("</font></td></tr>");
			serviceHtml += sb.toString() + "</table>";
		}

		return serviceHtml;
	}


	/**
	 * tr069 和 snmp 设备HttpGet 操作
	 *
	 * @author zhangcong （67706）
	 * @param request
	 * @return
	 */
	public String allHttpGetResult(String deviceId, String Interface, String httpVersion, String url, String numberOfRepetitions, String timeout)
	{
		String devicetype = "tr069";
		logger.debug("devicetype :" + devicetype);
		String strResult = HttpGetList(deviceId, Interface, httpVersion, url, numberOfRepetitions, timeout);
		return strResult;
	}

	public String HttpGetList(String deviceId, String Interface, String httpVersion, String url, String numberOfRepetitions, String timeout)
	{
		String device_id = deviceId;
		//得到设备类型
		String gw_type = LipossGlobals.getGw_Type(device_id);
//		String gather_id = "";

		String URL = url;
		String NumberOfRepetitions = numberOfRepetitions;
		String Timeout = timeout;
		DevRpc[] devRPCArr = new DevRpc[1];

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
		ParameterValueStruct[] ParameterValueStruct = new ParameterValueStruct[6];
		//Requested
		ParameterValueStruct[0] = new ParameterValueStruct();
		ParameterValueStruct[0]
				.setName("InternetGatewayDevice.X_CT-COM_HttpGetDiagnostics.DiagnosticsState");
		anyObject.para_value = "Requested";
		anyObject.para_type_id = "1";
		ParameterValueStruct[0].setValue(anyObject);
		//接口，只支持WAN
		ParameterValueStruct[1] = new ParameterValueStruct();
		ParameterValueStruct[1]
				.setName("InternetGatewayDevice.X_CT-COM_HttpGetDiagnostics.Interface");
		anyObject = new AnyObject();
		anyObject.para_value = Interface;
		anyObject.para_type_id = "1";
		ParameterValueStruct[1].setValue(anyObject);
		//测试URL
		ParameterValueStruct[2] = new ParameterValueStruct();
		ParameterValueStruct[2].setName("InternetGatewayDevice.X_CT-COM_HttpGetDiagnostics.URL");
		anyObject = new AnyObject();
		anyObject.para_value = URL;
		anyObject.para_type_id = "1";
		ParameterValueStruct[2].setValue(anyObject);
		//重试次数
		ParameterValueStruct[3] = new ParameterValueStruct();
		ParameterValueStruct[3]
				.setName("InternetGatewayDevice.X_CT-COM_HttpGetDiagnostics.NumberOfRepetitions");
		anyObject = new AnyObject();
		anyObject.para_value = NumberOfRepetitions;
		anyObject.para_type_id = "3";
		ParameterValueStruct[3].setValue(anyObject);
		//超时时间
		ParameterValueStruct[4] = new ParameterValueStruct();
		ParameterValueStruct[4]
				.setName("InternetGatewayDevice.X_CT-COM_HttpGetDiagnostics.Timeout");
		anyObject = new AnyObject();
		anyObject.para_value = Timeout;
		anyObject.para_type_id = "3";
		ParameterValueStruct[4].setValue(anyObject);
		//HTTP版本
		ParameterValueStruct[5] = new ParameterValueStruct();
		ParameterValueStruct[5]
				.setName("InternetGatewayDevice.X_CT-COM_HttpGetDiagnostics.HttpVersion");
		anyObject = new AnyObject();
		anyObject.para_value = httpVersion;
		anyObject.para_type_id = "3";
		ParameterValueStruct[5].setValue(anyObject);
		setParameterValues.setParameterList(ParameterValueStruct);
		setParameterValues.setParameterKey("Ping");

		//获取参数
		GetParameterValues getParameterValues = new GetParameterValues();
		String[] parameterNamesArr = new String[5];
		parameterNamesArr[0] = "InternetGatewayDevice.X_CT-COM_HttpGetDiagnostics.SuccessCount";
		parameterNamesArr[1] = "InternetGatewayDevice.X_CT-COM_HttpGetDiagnostics.FailureCount";
		parameterNamesArr[2] = "InternetGatewayDevice.X_CT-COM_HttpGetDiagnostics.AverageResponseTime";
		parameterNamesArr[3] = "InternetGatewayDevice.X_CT-COM_HttpGetDiagnostics.MinimumResponseTime";
		parameterNamesArr[4] = "InternetGatewayDevice.X_CT-COM_HttpGetDiagnostics.MaximumResponseTime";
		getParameterValues.setParameterNames(parameterNamesArr);
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
		serviceHtml += "<table border='0' align='center' width='100%' cellpadding='0' cellspacing='1' bgcolor='#999999'>";
		serviceHtml += "<tr class='blue_foot'>";
		serviceHtml += "<td bgcolor='#FFFFFF' width='2%' nowrap>";
		serviceHtml += "设备名称";
		serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
		serviceHtml += "成功数";
		serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
		serviceHtml += "失败数";
		serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
		serviceHtml += "平均响应时间";
		serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
		serviceHtml += "最小响应时间";
		serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
		serviceHtml += "最大响应时间";
		serviceHtml += "</td></tr>";
		String errMessage = "";
		Map PingMap = null;
		if (devRPCRep == null || devRPCRep.size() == 0)
		{
			logger.warn("[{}]List<DevRpcCmdOBJ>返回为空！", device_id);
			errMessage = "设备未知错误";
		}
		else if (devRPCRep.get(0) == null)
		{
			logger.warn("[{}]DevRpcCmdOBJ返回为空！", device_id);
			errMessage = "设备未知错误";
		}
		else
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
			logger.debug("GSJ------------------------:::" + devRPCRep.size());
			logger.debug("GSJ------------------------:::" + devRPCRep.get(0).getStat());
			String device_name = osMap.get(device_id);
			if (PingMap == null)
			{
				serviceHtml += "<tr bgcolor='#FFFFFF'>";
				serviceHtml += "<td bgcolor='#FFFFFF' width='2%' nowrap>";
				serviceHtml += device_name;
				serviceHtml += "</td><td colspan='5'><span><font color=red>" + errMessage
						+ "</font></span></td></tr>";
			}
			else
			{
				logger.debug("F-----------:" + PingMap);
				serviceHtml += "<tr class='blue_foot'>";
				serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
				serviceHtml += device_name;
				serviceHtml += "<td bgcolor='#FFFFFF' width='2%' nowrap>";
				serviceHtml += PingMap
						.get("InternetGatewayDevice.X_CT-COM_HttpGetDiagnostics.SuccessCount");
				serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
				serviceHtml += PingMap
						.get("InternetGatewayDevice.X_CT-COM_HttpGetDiagnostics.FailureCount");
				serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
				serviceHtml += PingMap
						.get("InternetGatewayDevice.X_CT-COM_HttpGetDiagnostics.AverageResponseTime");
				serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
				serviceHtml += PingMap
						.get("InternetGatewayDevice.X_CT-COM_HttpGetDiagnostics.MinimumResponseTime");
				serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
				serviceHtml += PingMap
						.get("InternetGatewayDevice.X_CT-COM_HttpGetDiagnostics.MaximumResponseTime");
				serviceHtml += "</td></tr>";
			}
			Map<String,String> expertMap = getSuggested(7);
			int ex_bias = StringUtil.getIntegerValue(expertMap.get("ex_bias"));
			String ex_regular = expertMap.get("ex_regular");

			serviceHtml += "<tr class='blue_foot'>";
			serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
			serviceHtml += "参考值";
			serviceHtml += "<td bgcolor='#FFFFFF' width='2%' nowrap>";
			serviceHtml += NumberOfRepetitions;
			serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
			serviceHtml += 0;
			serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
			serviceHtml += ex_bias;
			serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
			serviceHtml += ex_bias;
			serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
			serviceHtml += ex_bias;
			serviceHtml += "</td></tr>";

			if (PingMap == null)
			{
				serviceHtml += "<tr bgcolor='#FFFFFF'>";
				serviceHtml += "<td bgcolor='#FFFFFF' width='2%' nowrap>";
				serviceHtml += "专家建议";
				serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap colspan='5'>";
				serviceHtml += "<font color='red'>";
				serviceHtml += "异常";
				serviceHtml += "</font>";
				serviceHtml += "</td></tr>";
			}
			else
			{
				logger.debug("F-----------:" + PingMap);
				serviceHtml += "<tr class='blue_foot'>";
				serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
				serviceHtml += "专家建议";
				serviceHtml += "</td>";
				serviceHtml += judgeIntValue(StringUtil.getIntegerValue(NumberOfRepetitions), StringUtil.getIntegerValue(PingMap
						.get("InternetGatewayDevice.X_CT-COM_HttpGetDiagnostics.SuccessCount")), "=", 0);
				serviceHtml += judgeIntValue(0, StringUtil.getIntegerValue(PingMap
						.get("InternetGatewayDevice.X_CT-COM_HttpGetDiagnostics.FailureCount")), "=", 0);
				serviceHtml += judgeIntValue(ex_bias, StringUtil.getIntegerValue(PingMap
						.get("InternetGatewayDevice.X_CT-COM_HttpGetDiagnostics.AverageResponseTime")), ex_regular, 0);
				serviceHtml += judgeIntValue(ex_bias, StringUtil.getIntegerValue(PingMap
						.get("InternetGatewayDevice.X_CT-COM_HttpGetDiagnostics.MinimumResponseTime")), ex_regular, 0);
				serviceHtml += judgeIntValue(ex_bias, StringUtil.getIntegerValue(PingMap
						.get("InternetGatewayDevice.X_CT-COM_HttpGetDiagnostics.MaximumResponseTime")), ex_regular, 0);
				serviceHtml += "</tr>";
			}
		}
		serviceHtml += "</table>";
		return serviceHtml;
	}


	/**
	 * @param request
	 * @return
	 */
	public static String traceRoute(String deviceId, String Interface, String host, String maxHopCount, String timeout)
	{
		//查询类型(1:批量获取;2:分次获取)
		String queryTypeStr = null;
		int queryType = Integer.parseInt(queryTypeStr == null? "1":queryTypeStr.trim());
		//设备ID
		String device_id = deviceId;
		//Set条件
		//String Interface = request.getParameter("Interface");
		String Host = host;
		String MaxHopCount = maxHopCount;
		String Timeout = timeout;

		Map<String, String> osMap = new HashMap<String, String>();
		String serviceHtml = "";
		String strSQL1 = "select oui, device_serialnumber, gw_type from tab_gw_device where device_id='" + device_id
				+ "'";
		PrepareSQL psql = new PrepareSQL(strSQL1);
		psql.getSQL();
		Cursor cursor1 = DataSetBean.getCursor(strSQL1);
		Map fields1 = cursor1.getNext();
		String out = (String) fields1.get("oui");
		String SerialNumber = (String) fields1.get("device_serialnumber");
		int gw_type = Integer.parseInt((String)fields1.get("gw_type"));
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

		DevRPCManager devRPCManager = new DevRPCManager(gw_type+"");
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
		FileSevice fileSevice = new FileSevice();
		Map<String,String> expertMap = fileSevice.getSuggested(5);
		int ex_bias = StringUtil.getIntegerValue(expertMap.get("ex_bias"));
//		String ex_succ_desc = expertMap.get("ex_succ_desc");
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
			devRPCManager = new DevRPCManager(gw_type+"");
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
				devRPCManager = new DevRPCManager(gw_type+"");
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
		logger.info("==============size:" + size);
		logger.info("==============queryType:" + queryType);

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

	private static Rpc[] getACSPara(String DiagnosticsState, String Host, String device_id)
	{
		AnyObject anyObject = new AnyObject();
		// 设值参数
		SetParameterValues setParameterValues = new SetParameterValues();
		ParameterValueStruct[] ParameterValueStruct = new ParameterValueStruct[2];
		ParameterValueStruct[0] = new ParameterValueStruct();
		ParameterValueStruct[0].setName(ConfigDevice.getParaArr("411", device_id));
		anyObject.para_value = DiagnosticsState;
		anyObject.para_type_id = "1";
		ParameterValueStruct[0].setValue(anyObject);
		ParameterValueStruct[1] = new ParameterValueStruct();
		ParameterValueStruct[1].setName(ConfigDevice.getParaArr("412", device_id));
		anyObject = new AnyObject();
		anyObject.para_value = Host;
		anyObject.para_type_id = "1";
		ParameterValueStruct[1].setValue(anyObject);
		setParameterValues.setParameterList(ParameterValueStruct);
		setParameterValues.setParameterKey("Ping");
		// 取值参数
		GetParameterValues getParameterValues = new GetParameterValues();
		String[] parameterNamesArr = new String[2];
		parameterNamesArr[0] = ConfigDevice.getParaArr("413", device_id);
		parameterNamesArr[1] = ConfigDevice.getParaArr("414", device_id);
		getParameterValues.setParameterNames(parameterNamesArr);
		Rpc[] rpcArr = new Rpc[2];
		rpcArr[0] = new Rpc();
		rpcArr[0].rpcId = "1";
		rpcArr[0].rpcName = "SetParameterValues";
		rpcArr[0].rpcValue = setParameterValues.toRPC();
		rpcArr[1] = new Rpc();
		rpcArr[1].rpcId = "2";
		rpcArr[1].rpcName = "GetParameterValues";
		rpcArr[1].rpcValue = getParameterValues.toRPC();
		return rpcArr;
	}


	public String allDNSQueryResult(String deviceId, String  Interface, String dnsServerIP, String domainName, String numberOfRepetitions, String timeout)
	{
		// 返回结果
		String strResult = "";
		// 设备类型，tr069 还是 snmp
		strResult = DNSQueryList(deviceId, Interface, dnsServerIP, domainName, numberOfRepetitions, timeout);
		return strResult;
	}

	public String DNSQueryList(String deviceId, String  Interface, String dnsServerIP, String domainName, String numberOfRepetitions, String timeout)
	{
		String device_id = deviceId;
		//得到设备类型
		String gw_type = LipossGlobals.getGw_Type(device_id);
//		String gather_id = "";

		String DNSServerIP = dnsServerIP;
		String NumberOfRepetitions = numberOfRepetitions;
		String Timeout = timeout;
		String DomainName = domainName;
		DevRpc[] devRPCArr = new DevRpc[1];

		Map<String, String> osMap = new HashMap<String, String>();
		String serviceHtml = "";
		String strSQL1 = "select oui, device_serialnumber from tab_gw_device where device_id='" + device_id
				+ "'";
		PrepareSQL psql = new PrepareSQL(strSQL1);
		psql.getSQL();
		Cursor cursor1 = DataSetBean.getCursor(strSQL1);
		Map fields1 = cursor1.getNext();
		// begin add by w5221 修改按照用户查询没有采集点信息的参数
		// gather_id = (String) fields1.get("gather_id");
		// end add by w5221 修改按照用户查询没有采集点信息的参数
		String out = (String) fields1.get("oui");
		String SerialNumber = (String) fields1.get("device_serialnumber");
		// String ip = (String) fields1.get("loopback_ip");
		// String Port = (String) fields1.get("cr_port");
		// String path = (String) fields1.get("cr_path");
		// String username = (String) fields1.get("acs_username");
		// String passwd = (String) fields1.get("acs_passwd");
		// add by lizhaojun ----2007-06-21
		osMap.put(device_id, out + "-" + SerialNumber);
		AnyObject anyObject = new AnyObject();
		SetParameterValues setParameterValues = new SetParameterValues();
		ParameterValueStruct[] ParameterValueStruct = new ParameterValueStruct[6];
		//Requested
		ParameterValueStruct[0] = new ParameterValueStruct();
		ParameterValueStruct[0]
				.setName("InternetGatewayDevice.X_CT-COM_DNSQueryDiagnostics.DiagnosticsState");
		anyObject.para_value = "Requested";
		anyObject.para_type_id = "1";
		ParameterValueStruct[0].setValue(anyObject);
		//接口，只支持WAN
		ParameterValueStruct[1] = new ParameterValueStruct();
		ParameterValueStruct[1]
				.setName("InternetGatewayDevice.X_CT-COM_DNSQueryDiagnostics.Interface");
		anyObject = new AnyObject();
		anyObject.para_value = Interface;
		anyObject.para_type_id = "1";
		ParameterValueStruct[1].setValue(anyObject);
		//测试URL
		ParameterValueStruct[2] = new ParameterValueStruct();
		ParameterValueStruct[2].setName("InternetGatewayDevice.X_CT-COM_DNSQueryDiagnostics.DNSServerIP");
		anyObject = new AnyObject();
		anyObject.para_value = DNSServerIP;
		anyObject.para_type_id = "1";
		ParameterValueStruct[2].setValue(anyObject);
		//重试次数
		ParameterValueStruct[3] = new ParameterValueStruct();
		ParameterValueStruct[3]
				.setName("InternetGatewayDevice.X_CT-COM_DNSQueryDiagnostics.NumberOfRepetitions");
		anyObject = new AnyObject();
		anyObject.para_value = NumberOfRepetitions;
		anyObject.para_type_id = "3";
		ParameterValueStruct[3].setValue(anyObject);
		//超时时间
		ParameterValueStruct[4] = new ParameterValueStruct();
		ParameterValueStruct[4]
				.setName("InternetGatewayDevice.X_CT-COM_DNSQueryDiagnostics.Timeout");
		anyObject = new AnyObject();
		anyObject.para_value = Timeout;
		anyObject.para_type_id = "3";
		ParameterValueStruct[4].setValue(anyObject);
		//HTTP版本
		ParameterValueStruct[5] = new ParameterValueStruct();
		ParameterValueStruct[5]
				.setName("InternetGatewayDevice.X_CT-COM_DNSQueryDiagnostics.DomainName");
		anyObject = new AnyObject();
		anyObject.para_value = DomainName;
		anyObject.para_type_id = "1";
		ParameterValueStruct[5].setValue(anyObject);
		setParameterValues.setParameterList(ParameterValueStruct);
		setParameterValues.setParameterKey("Ping");

		//获取参数
		GetParameterValues getParameterValues = new GetParameterValues();
		String[] parameterNamesArr = new String[5];
		parameterNamesArr[0] = "InternetGatewayDevice.X_CT-COM_DNSQueryDiagnostics.SuccessCount";
		parameterNamesArr[1] = "InternetGatewayDevice.X_CT-COM_DNSQueryDiagnostics.FailureCount";
		parameterNamesArr[2] = "InternetGatewayDevice.X_CT-COM_DNSQueryDiagnostics.AverageResponseTime";
		parameterNamesArr[3] = "InternetGatewayDevice.X_CT-COM_DNSQueryDiagnostics.MinimumResponseTime";
		parameterNamesArr[4] = "InternetGatewayDevice.X_CT-COM_DNSQueryDiagnostics.MaximumResponseTime";
		getParameterValues.setParameterNames(parameterNamesArr);
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
		// HttpSession session = request.getSession();
		// UserRes curUser = (UserRes) session.getAttribute("curUser");
		// User user = curUser.getUser();
		List<DevRpcCmdOBJ> devRPCRep = null;
		DevRPCManager devRPCManager = new DevRPCManager(gw_type+"");
		devRPCRep = devRPCManager.execRPC(devRPCArr, Global.DiagCmd_Type);
		serviceHtml += "<table border='0' align='center' width='100%' cellpadding='0' cellspacing='1' bgcolor='#999999'>";
		serviceHtml += "<tr class='blue_foot'>";
		serviceHtml += "<td bgcolor='#FFFFFF' width='2%' nowrap>";
		serviceHtml += "设备名称";
		serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
		serviceHtml += "成功数";
		serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
		serviceHtml += "失败数";
		serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
		serviceHtml += "平均响应时间";
		serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
		serviceHtml += "最小响应时间";
		serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
		serviceHtml += "最大响应时间";
		serviceHtml += "</td></tr>";
		String errMessage = "";
		Map PingMap = null;
		if (devRPCRep == null || devRPCRep.size() == 0)
		{
			logger.warn("[{}]List<DevRpcCmdOBJ>返回为空！", device_id);
			errMessage = "设备未知错误";
		}
		else if (devRPCRep.get(0) == null)
		{
			logger.warn("[{}]DevRpcCmdOBJ返回为空！", device_id);
			errMessage = "设备未知错误";
		}
		else
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
			logger.debug("GSJ------------------------:::" + devRPCRep.size());
			logger.debug("GSJ------------------------:::" + devRPCRep.get(0).getStat());
			String device_name = osMap.get(device_id);
			if (PingMap == null)
			{
				serviceHtml += "<tr bgcolor='#FFFFFF'>";
				serviceHtml += "<td bgcolor='#FFFFFF' width='2%' nowrap>";
				serviceHtml += device_name;
				serviceHtml += "</td><td colspan='5'><span><font color=red>" + errMessage
						+ "</font></span></td></tr>";
			}
			else
			{
				logger.debug("F-----------:" + PingMap);
				serviceHtml += "<tr class='blue_foot'>";
				serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
				serviceHtml += device_name;
				serviceHtml += "<td bgcolor='#FFFFFF' width='2%' nowrap>";
				serviceHtml += PingMap
						.get("InternetGatewayDevice.X_CT-COM_DNSQueryDiagnostics.SuccessCount");
				serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
				serviceHtml += PingMap
						.get("InternetGatewayDevice.X_CT-COM_DNSQueryDiagnostics.FailureCount");
				serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
				serviceHtml += PingMap
						.get("InternetGatewayDevice.X_CT-COM_DNSQueryDiagnostics.AverageResponseTime");
				serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
				serviceHtml += PingMap
						.get("InternetGatewayDevice.X_CT-COM_DNSQueryDiagnostics.MinimumResponseTime");
				serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
				serviceHtml += PingMap
						.get("InternetGatewayDevice.X_CT-COM_DNSQueryDiagnostics.MaximumResponseTime");
				serviceHtml += "</td></tr>";
			}
		}

		// begin add by chenjie(67371) 2011-8-9 增加参考值和专家建议

		StringBuffer sb = new StringBuffer();
		/**  参考值 **/
		sb.append("<tr class='blue_foot'>");
		sb.append("<td bgcolor='#FFFFFF' nowrap>参考值</td>");
		// 成功数
		sb.append("<td bgcolor='#FFFFFF' nowrap>");
		sb.append(NumberOfRepetitions).append("</td>");
		// 失败数
		sb.append("<td bgcolor='#FFFFFF' nowrap>");
		sb.append(0).append("</td>");

		Map pingSuggestMap =  getSuggested(6);
		// 参考值
		String biasValue = (String)pingSuggestMap.get("ex_bias");

		// 平均响应时间
		sb.append("<td bgcolor='#FFFFFF' nowrap>");
		sb.append(biasValue).append("</td>");

		// 最小响应时间
		sb.append("<td bgcolor='#FFFFFF' nowrap>");
		sb.append(biasValue).append("</td>");

		// 最大响应时间
		sb.append("<td bgcolor='#FFFFFF' nowrap>");
		sb.append(biasValue).append("</td>");
		sb.append("</tr>");

		if(PingMap != null){

			/**  专家建议 **/
			String suggestRegular = (String)pingSuggestMap.get("ex_regular");

			sb.append("<tr class='blue_foot'>");
			sb.append("<td bgcolor='#FFFFFF' nowrap>专家建议</td>");

			// 成功数
			int success_count = getIntByString(String.valueOf(PingMap.get("InternetGatewayDevice.X_CT-COM_DNSQueryDiagnostics.SuccessCount")));
//			int numberOfRepetitions = Integer.parseInt(NumberOfRepetitions);
			sb.append(judgeIntValue(Integer.parseInt(numberOfRepetitions), success_count, "=", 0));

			// 失败数
			int fail_count = getIntByString(String.valueOf(PingMap.get("InternetGatewayDevice.X_CT-COM_DNSQueryDiagnostics.FailureCount")));
			sb.append(judgeIntValue(0, fail_count, "=", 0));

			// 响应时间
			int average_time = getIntByString(String.valueOf(PingMap.get("InternetGatewayDevice.X_CT-COM_DNSQueryDiagnostics.AverageResponseTime")));
			sb.append(judgeIntValue(Integer.parseInt(biasValue), average_time, suggestRegular, 0));

			int min_time = getIntByString(String.valueOf(PingMap.get("IInternetGatewayDevice.X_CT-COM_DNSQueryDiagnostics.MinimumResponseTime")));
			sb.append(judgeIntValue(Integer.parseInt(biasValue), min_time, suggestRegular, 0));

			int max_time = getIntByString(String.valueOf(PingMap.get("IInternetGatewayDevice.X_CT-COM_DNSQueryDiagnostics.MaximumResponseTime")));
			sb.append(judgeIntValue(Integer.parseInt(biasValue), max_time, suggestRegular, 0));

			//
			sb.append("</tr>");

			// end add

			serviceHtml += sb.toString() + "</table>";
		}
		else
		{
			sb.append("<tr>");
			sb.append("<td bgcolor='#FFFFFF' nowrap>专家建议</td>");
			sb.append("<td bgcolor='#FFFFFF' nowrap colspan=5><font color=red>");
			sb.append("异常").append("</font></td></tr>");
			serviceHtml += sb.toString() + "</table>";
		}
		return serviceHtml;
	}

	private int getIntByString(String value)
	{
		if(null == value || "null".equals(value) || "".equals(value))
		{
			return -99;
		}else
		{
			return Integer.parseInt(value);
		}
	}


	public OneKeyDiagDAO getDao() {
		return dao;
	}

	public void setDao(OneKeyDiagDAO dao) {
		this.dao = dao;
	}

	public Map getSuggested(int id)
	{
		PrepareSQL psql = new PrepareSQL("select ex_bias, ex_suggest, ex_regular from gw_egw_expert where id=?");
		psql.setInt(1, id);
		Cursor cursor1 = DataSetBean.getCursor(psql.getSQL());
		Map fields1 = cursor1.getNext();
		return fields1;
	}

	/**
	 * 判断值
	 * @param biasValue  参考值
	 * @param factValue  实际值
	 * @param regular    判断符号
	 */
	public String judgeIntValue(double biasValue, double factValue, String regular, double orValue)
	{
		StringBuffer sb = new StringBuffer();
		// 小于
		if("<".equals(regular))
		{
			if(factValue < biasValue)
			{
				sb.append("<td bgcolor='#FFFFFF' nowrap><font color=green>");
				sb.append("正常").append("</font></td>");
			}
			else
			{
				sb.append("<td bgcolor='#FFFFFF' nowrap><font color=red>");
				sb.append("异常").append("</font></td>");
			}
		}
		// 大于
		if(">".equals(regular))
		{
			if(factValue > biasValue)
			{
				sb.append("<td bgcolor='#FFFFFF' nowrap><font color=green>");
				sb.append("正常").append("</font></td>");
			}
			else
			{
				sb.append("<td bgcolor='#FFFFFF' nowrap><font color=red>");
				sb.append("异常").append("</font></td>");
			}
		}
		// 等于
		if("=".equals(regular))
		{
			if(factValue == biasValue)
			{
				sb.append("<td bgcolor='#FFFFFF' nowrap><font color=green>");
				sb.append("正常").append("</font></td>");
			}
			else
			{
				sb.append("<td bgcolor='#FFFFFF' nowrap><font color=red>");
				sb.append("异常").append("</font></td>");
			}
		}

		// 波动值
		if("<>".equals(regular))
		{
			if(biasValue - orValue < factValue && factValue < biasValue + orValue)
			{
				sb.append("<td bgcolor='#FFFFFF' nowrap><font color=green>");
				sb.append("正常").append("</font></td>");
			}
			else
			{
				sb.append("<td bgcolor='#FFFFFF' nowrap><font color=red>");
				sb.append("异常").append("</font></td>");
			}
		}
		return sb.toString();
	}
}
