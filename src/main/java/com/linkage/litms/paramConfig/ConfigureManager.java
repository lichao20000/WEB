
package com.linkage.litms.paramConfig;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import com.linkage.commons.db.DBUtil;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ACS.DevRpc;
import ACS.Rpc;
import Performance.Data;
import Performance.OIDSTR;
import Performance.SNMPV3PARAM;
import Performance.createDataV3;
import Performance.createOidNodeList;
import Performance.setDataReturn;
import Performance.setDataV3;

import com.ailk.tr069.devrpc.obj.rpc.DevRpcCmdOBJ;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.acs.soap.io.XML;
import com.linkage.litms.acs.soap.io.XmlToRpc;
import com.linkage.litms.acs.soap.object.AnyObject;
import com.linkage.litms.acs.soap.object.Fault;
import com.linkage.litms.acs.soap.object.ParameterInfoStruct;
import com.linkage.litms.acs.soap.object.ParameterValueStruct;
import com.linkage.litms.acs.soap.object.SoapOBJ;
import com.linkage.litms.acs.soap.service.AddObject;
import com.linkage.litms.acs.soap.service.DeleteObject;
import com.linkage.litms.acs.soap.service.GetParameterNames;
import com.linkage.litms.acs.soap.service.GetParameterNamesResponse;
import com.linkage.litms.acs.soap.service.GetParameterValues;
import com.linkage.litms.acs.soap.service.GetParameterValuesResponse;
import com.linkage.litms.acs.soap.service.RPCObject;
import com.linkage.litms.acs.soap.service.SetParameterValues;
import com.linkage.litms.acs.soap.service.SetParameterValuesResponse;
import com.linkage.litms.common.corba.DevRPCManager;
import com.linkage.litms.common.corba.interfacecontrol.SnmpGatherInterfaceV3;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.litms.common.util.VendorUtil;
import com.linkage.module.gwms.Global;

/**
 * @author Bruce(工号) tel：12345678
 * @version 1.0
 * @since May 28, 2008
 * @category com.linkage.litms.paramConfig 版权：南京联创科技 网管科技部
 */
public class ConfigureManager
{

	/** log */
	private static Logger logger = LoggerFactory.getLogger(ConfigureManager.class);
	private String device_tab_name;
	private String device_id;
	private String device_name;
//	private String device_serialnumber;
	private String loopback_ip;
//	private String port;
//	private String path;
//	private String gather_id;
//	private String acs_username;
//	private String acs_passwd;
	private String oui;
//	private String vendor_name;
//	private String ior;
	private String type;
	private SNMPV3PARAM v3param = null;
	private HashMap devInfoMap = null;

	public ConfigureManager()
	{
		int protocol = LipossGlobals.getGwProtocol();
		if (protocol == 2)
		{
			device_tab_name = "tab_deviceresource";
		}
		else
		{
			device_tab_name = "tab_gw_device";
		}
	}

	/**
	 * 获取V3所需要的认证信息
	 * 
	 * @param device_id
	 * @return
	 */
	public SNMPV3PARAM getAuthInfoForV3(String device_id)
	{
		String sql = "select * from sgw_security";
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			sql = "select auth_passwd, auth_protocol, privacy_passwd, privacy_protocol, security_level, security_model, " +
					"security_username, snmp_r_passwd, snmp_w_passwd from sgw_security";
		}
		sql += " where device_id='" + device_id + "'";
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		HashMap map = DataSetBean.getRecord(sql);
		SNMPV3PARAM param = new SNMPV3PARAM();
		param.authPasswd = (String) map.get("auth_passwd");
		param.authProtocol = (String) map.get("auth_protocol");
		param.privPasswd = (String) map.get("privacy_passwd");
		param.privProtocol = (String) map.get("privacy_protocol");
		param.securityLevel = Integer.parseInt((String) map.get("security_level"));
		param.securityModel = Integer.parseInt((String) map.get("security_model"));
		param.securityName = (String) map.get("security_username");
		param.snmp_r_word = (String) map.get("snmp_r_passwd");
		param.snmp_w_word = (String) map.get("snmp_w_passwd");
		return param;
	}

	/**
	 * 获取设备信息
	 * 
	 * @param device_id
	 * @return
	 */
	private HashMap getDevInfo(String device_id)
	{
		String sqlDevInfo = "select device_name, gather_id, loopback_ip, cr_port"
				+ ",cr_path, acs_username, acs_passwd, device_id, device_serialnumber"
				+ ",oui,vendor_name from " + device_tab_name
				+ " a, tab_vendor b where device_id='" + device_id
				// + "' and a.oui=b.vendor_id";
				+ "' and a.vendor_id=b.vendor_id";
		PrepareSQL psql = new PrepareSQL(sqlDevInfo);
		psql.getSQL();
		Map devInfoMap = DataSetBean.getRecord(sqlDevInfo);
		oui = (String) devInfoMap.get("oui");
//		vendor_name = (String) devInfoMap.get("vendor_name");
//		device_serialnumber = (String) devInfoMap.get("device_serialnumber");
		loopback_ip = (String) devInfoMap.get("loopback_ip");
//		port = (String) devInfoMap.get("cr_port");
//		path = (String) devInfoMap.get("cr_path");
//		acs_username = (String) devInfoMap.get("acs_username");
//		acs_passwd = (String) devInfoMap.get("acs_passwd");
		device_name = (String) devInfoMap.get("device_name");
		return (HashMap) devInfoMap;
	}

	/**
	 * 获取NTP状态
	 * 
	 * @param request
	 * @return size=0：禁用 size !-0：返回服务器列表
	 */
	public Map<String, String> getNTPStatus(HttpServletRequest request)
	{
		Map<String, String> resultMap = new HashMap<String, String>();
		return getNTPStatus(request.getParameter("device_id"), request
				.getParameter("type"));
	}

	/**
	 * 获取NTP状态(为批量配置准备的方法)
	 * 
	 * @param theDevice_id
	 *            设备ID
	 * @param theType
	 *            配置方式
	 * @return size=0：禁用 size !-0：返回服务器列表
	 */
	public Map<String, String> getNTPStatus(String theDevice_id, String theType)
	{
		Map<String, String> resultMap = new HashMap<String, String>();
		device_id = theDevice_id; // device_id
		//得到设备类型
		String gw_type = LipossGlobals.getGw_Type(device_id);
		// String oid_type = ""; // Oid类型
		type = theType; // 配置方式
		// 取得设备表中的值
		devInfoMap = getDevInfo(device_id);
		// 如果找不到设备信息，就返回
		if (devInfoMap.isEmpty())
		{
			return resultMap;
		}
		if ("1".equals(type))
		{
			String ns1 = "InternetGatewayDevice.Time.NTPServer1";
			String ns2 = "InternetGatewayDevice.Time.NTPServer2";
			// String[] paramNames = new String[] {
			// "InternetGatewayDevice.Time.NTPServer1",
			// "InternetGatewayDevice.Time.NTPServer2",
			// "InternetGatewayDevice.Time.X_ATP_SNTPEnable" };
			String[] paramNames = new String[] { ns1, ns2 };
			Map<String, String> map = getParaValueMap(paramNames,gw_type);
			if (map.size() == 0)
			{
				return resultMap;
			}
			else
			{
				String nsv1 = map.get(ns1);
				String nsv2 = map.get(ns2);
				if (nsv1 != null && !"".equals(nsv1))
				{
					resultMap.put("server_1", nsv1);
					if (nsv2 != null && !"".equals(nsv2))
					{
						resultMap.put("server_2", nsv2);
					}
				}
				else
				{
					if (nsv2 != null && !"".equals(nsv2))
					{
						resultMap.put("server_1", nsv2);
					}
				}
			}
			return resultMap;
		}
		if ("2".equals(type))
		{
			v3param = getAuthInfoForV3(device_id);
			String oid_1 = "1.3.6.1.4.1.25506.8.22.2.1.1.1.36";
			Data[] dataList = SnmpGatherInterfaceV3.getInstance().getDataListIMDV3(oid_1,
					devInfoMap);
			if (dataList == null)
				return resultMap;
			for (int i = 0; i < dataList.length; i++)
			{
				Data data = dataList[i];
				String ip = data.index;
				ip = ip.substring(0, ip.length() - 2);
				resultMap.put("server_" + (i + 1), ip);
			}
			logger.debug("NTP server info =" + resultMap);
		}
		return resultMap;
	}

	private boolean tr069Set(String[] param, String gw_type)
	{
		logger.debug("--------tr069 set---------");
		SetParameterValues setParameterValues = new SetParameterValues();
		ParameterValueStruct[] parameterValueStructArr = new ParameterValueStruct[1];
		if (param == null)
			return false;
		AnyObject anyObject = new AnyObject();
		anyObject.para_value = param[2];
		anyObject.para_type_id = param[1];
		parameterValueStructArr[0] = new ParameterValueStruct();
		parameterValueStructArr[0].setName(param[0]);
		parameterValueStructArr[0].setValue(anyObject);
		setParameterValues.setParameterList(parameterValueStructArr);
		DevRpc[] devRPCArr = getDevRPCArr(setParameterValues);
		SetParameterValuesResponse res = null;
		List<DevRpcCmdOBJ> devRPCRep = getDevRPCResponse(devRPCArr,Global.RpcCmd_Type, gw_type);
		if (devRPCRep == null || devRPCRep.size() == 0 || devRPCRep.get(0) == null)
		{
			return false;
		}
		// 一个设备返回的命令
		if (devRPCRep.get(0).getStat() == 1)
		{
			return true;
		}
		return false;
	}

	/**
	 * NTP Server地址配置 设置NTP服务状态、NTP服务器地址(为批量配置准备的方法)
	 * 
	 * @param theDevice_id
	 *            设备ID
	 * @param status
	 *            服务状态
	 * @param theType
	 *            配置方式
	 * @param main_ntp_server
	 *            主NTP服务器地址或域名
	 * @param second_ntp_server
	 *            备NTP服务器地址或域名
	 * @return
	 */
	public Map<String, String> NTPConfigure(String theDevice_id, String status,
			String theType, String main_ntp_server, String second_ntp_server)
	{
		Map<String, String> resultMap = new HashMap<String, String>();
		device_id = theDevice_id; // device_id
		//得到设备类型
		String gw_type = LipossGlobals.getGw_Type(device_id);
		String oid_type = ""; // Oid类型//TODO :NTP类型还未定义
		// String status = request.getParameter("status"); // 服务状态
		type = theType; // 配置方式
		// String main_ntp_server = request.getParameter("main_ntp_server"); //
		// 主NTP服务器地址或域名
		// String second_ntp_server = request.getParameter("second_ntp_server");
		// // 备NTP服务器地址或域名
		// 取得设备表中的值
		devInfoMap = getDevInfo(device_id);
		// 如果找不到设备信息，就返回
		if (devInfoMap.isEmpty())
		{
			return resultMap;
		}
		if ("1".equals(type))
		{// tr069
			// 最外层结构:SetParameterValues
			SetParameterValues setParameterValues = new SetParameterValues();
			// setParameterValues.setParameterKey("zf");
			String[] p1 = new String[] { "InternetGatewayDevice.Time.NTPServer1", "1",
					main_ntp_server };
			String[] p2 = new String[] { "InternetGatewayDevice.Time.NTPServer2", "1",
					second_ntp_server };
			// String[] p3 = new String[] {
			// "InternetGatewayDevice.Time.X_ATP_SNTPEnable", "4",
			// ("1".equals(status) ? "1" : "0") };
			String tmp = "InternetGatewayDevice.Time.X_CT-COM_Apply";
			// -------------------------
			// 判断是否思科
			// boolean ciscoFlag = false;
			// if ("11".equals(getDeviceModel(device_id))) {
			// ciscoFlag = true;
			// }
			boolean ciscoFlag = VendorUtil.IsCiscoByDeviceId(device_id);
			String tempStr = "";
			String[][] params = null;
			ParameterValueStruct[] parameterValueStructArr = null;
			DevRpc[] devRPCArr = null;
			if (main_ntp_server != null && !"".equals(main_ntp_server.trim()))
			{
				params = new String[][] { p1 };
				if (ciscoFlag)
				{
					parameterValueStructArr = new ParameterValueStruct[params.length + 1];
				}
				else
				{
					parameterValueStructArr = new ParameterValueStruct[params.length];
				}
				for (int i = 0; i < params.length; i++)
				{
					String[] p = params[i];
					AnyObject anyObject = new AnyObject();
					anyObject.para_value = p[2];
					anyObject.para_type_id = p[1];
					parameterValueStructArr[i] = new ParameterValueStruct();
					parameterValueStructArr[i].setName(p[0]);
					parameterValueStructArr[i].setValue(anyObject);
				}
				if (ciscoFlag)
				{
					AnyObject anyObject = new AnyObject();
					anyObject.para_value = "1";
					anyObject.para_type_id = "1";
					parameterValueStructArr[params.length] = new ParameterValueStruct();
					parameterValueStructArr[params.length].setName(tmp);
					parameterValueStructArr[params.length].setValue(anyObject);
				}
				setParameterValues.setParameterList(parameterValueStructArr);
				devRPCArr = getDevRPCArr(setParameterValues);
				List<DevRpcCmdOBJ> devRPCRep = getDevRPCResponse(devRPCArr,Global.RpcCmd_Type, gw_type);
				// 一个设备返回的命令
				if (devRPCRep == null || devRPCRep.size() == 0
						|| devRPCRep.get(0) == null)
				{
					return resultMap;
				}
				// 一个设备返回的命令
				if (devRPCRep.get(0).getStat() == 1)
				{
					tempStr += "主NTP服务器配置成功";
					resultMap.put(device_name, tempStr);
				}
				else
				{
					tempStr += "主NTP服务器配置失败";
					resultMap.put(device_name, tempStr);
				}
				// 释放资源
				params = null;
				parameterValueStructArr = null;
				setParameterValues = null;
			}
			// ------------------------
			params = new String[][] { p2 };
			if (ciscoFlag)
			{
				parameterValueStructArr = new ParameterValueStruct[params.length + 1];
			}
			else
			{
				parameterValueStructArr = new ParameterValueStruct[params.length];
			}
			for (int i = 0; i < params.length; i++)
			{
				String[] p = params[i];
				AnyObject anyObject = new AnyObject();
				anyObject.para_value = p[2];
				anyObject.para_type_id = p[1];
				parameterValueStructArr[i] = new ParameterValueStruct();
				parameterValueStructArr[i].setName(p[0]);
				parameterValueStructArr[i].setValue(anyObject);
			}
			if (ciscoFlag)
			{
				AnyObject anyObject = new AnyObject();
				anyObject.para_value = "1";
				anyObject.para_type_id = "1";
				parameterValueStructArr[params.length] = new ParameterValueStruct();
				parameterValueStructArr[params.length].setName(tmp);
				parameterValueStructArr[params.length].setValue(anyObject);
			}
			setParameterValues = new SetParameterValues();
			setParameterValues.setParameterList(parameterValueStructArr);
			devRPCArr = getDevRPCArr(setParameterValues);
			List<DevRpcCmdOBJ> devRPCRep = getDevRPCResponse(devRPCArr,Global.RpcCmd_Type, gw_type);
			if (devRPCRep == null)
			{
				return resultMap;
			}
			// 一个设备返回的命令
			if (devRPCRep == null || devRPCRep.size() == 0 || devRPCRep.get(0) == null)
			{
				return resultMap;
			}
			// 一个设备返回的命令
			if (devRPCRep.get(0).getStat() == 1)
			{
				tempStr += " 备NTP服务器配置成功";
				resultMap.put(device_name, tempStr);
			}
			else
			{
				tempStr += " 备NTP服务器配置失败";
				resultMap.put(device_name, tempStr);
			}
			// 释放资源
			params = null;
			parameterValueStructArr = null;
			setParameterValues = null;
		}
		if ("2".equals(type))
		{// snmp
			v3param = getAuthInfoForV3(device_id);
			String oid_1 = "1.3.6.1.4.1.25506.8.22.2.1.1.1.36";
			Data[] dataList = SnmpGatherInterfaceV3.getInstance().getDataListIMDV3(oid_1,
					devInfoMap);
			if (dataList != null)
			{
				// 原来已有实例，删除先
				for (int i = 0; i < dataList.length; i++)
				{
					Data data = dataList[i];
					String ip = data.index;
					ip = oid_1 + "." + ip;
					logger.debug("NTP sub oid = " + ip);
					setDataV3 d1 = new setDataV3();
					d1.loopback_ip = loopback_ip;
					d1.port = "161";
					d1.oid = ip;
					d1.value = "6";
					d1.readComm = "";
					d1.writeComm = "";
					d1.v3param = v3param;
					setDataV3[] setDataV3_arr = new setDataV3[1];
					setDataV3_arr[0] = d1;
					// 调用接口中方法
					setDataReturn[] setDataReturnArr = SnmpGatherInterfaceV3
							.getInstance().setWayV3(setDataV3_arr, setDataV3_arr.length,
									devInfoMap);
					boolean ret = true;
					if (null == setDataReturnArr || 0 == setDataReturnArr.length)
					{
						logger.warn("删除原有NTPServer实例失败!");
						return resultMap;
					}
					else
					{
						for (int j = 0; j < setDataReturnArr.length; j++)
						{
							String rst = setDataReturnArr[0].result;
							ret = ret && Boolean.parseBoolean(rst.toLowerCase());
						}
					}
					if (!ret)
					{
						logger.warn("删除原有NTPServer实例失败!");
						return resultMap;
					}
					else
					{
						logger.warn("删除NTPServer实例" + ip + "成功!");
					}
				}
			}
			if ("1".equals(status))
			{// 状态是启用，则创建新server
				createDataV3 cd = null;
				if (null != main_ntp_server && !"".equals(main_ntp_server))
				{
					cd = new createDataV3();
					cd.loopback_ip = loopback_ip;
					cd.oid = oid_1 + "." + main_ntp_server.trim() + ".3";
					cd.port = "161";
					cd.readComm = "";
					cd.type = 2;
					cd.v3param = v3param;
					cd.value = "4";
					cd.writeComm = "";
					logger.debug("cd.oid=" + cd.oid);
				}
				createDataV3 cd2 = null;
				if (null != second_ntp_server && !"".equals(second_ntp_server))
				{
					cd2 = new createDataV3();
					cd2.loopback_ip = loopback_ip;
					cd2.oid = oid_1 + "." + second_ntp_server.trim() + ".3";
					cd2.port = "161";
					cd2.readComm = "";
					cd2.type = 2;
					cd2.v3param = v3param;
					cd2.value = "4";
					cd2.writeComm = "";
					logger.debug("cd2.oid=" + cd2.oid);
				}
				createDataV3[] setDataV3_arr = null;
				if (cd != null && cd2 != null)
				{
					setDataV3_arr = new createDataV3[2];
					setDataV3_arr[0] = cd;
					setDataV3_arr[1] = cd2;
				}
				else if (cd != null)
				{
					setDataV3_arr = new createDataV3[1];
					setDataV3_arr[0] = cd;
				}
				else if (cd2 != null)
				{
					setDataV3_arr = new createDataV3[1];
					setDataV3_arr[0] = cd2;
				}
				else
				{
					return resultMap;
				}
				logger.debug("setDataV3_arr.length=" + setDataV3_arr.length);
				setDataReturn[] dataList2 = SnmpGatherInterfaceV3.getInstance()
						.createOidNodeV3(setDataV3_arr, setDataV3_arr.length, devInfoMap);
				if (null == dataList2 || 0 == dataList2.length)
				{
					logger.warn("创建新NTPServer实例失败!");
					return resultMap;
				}
				boolean ret2 = true;
				for (int i = 0; i < dataList2.length; i++)
				{
					setDataReturn data = dataList2[i];
					String rst = dataList2[0].result;
					ret2 = ret2 && Boolean.parseBoolean(rst.toLowerCase());
					logger.debug("[" + i + "]loopback_ip=" + data.loopback_ip);
					logger.debug("[" + i + "]result=" + data.result);
				}
				if (ret2)
				{
					resultMap.put(device_name, "启用成功");
				}
				else
				{
					resultMap.put(device_name, "启用失败");
				}
			}
			else
			{// 禁用
				resultMap.put(device_name, "禁用成功");
			}
		}
		return resultMap;
	}

	/**
	 * @param device_id
	 * @return
	 */
	private String getDeviceModel(String device_id)
	{
		String sql = "select device_model_id from tab_gw_device where device_id='"
				+ device_id + "'";
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(sql);
		Map fields = cursor.getNext();
		if (fields != null)
		{
			return fields.get("device_model_id").toString();
		}
		else
		{
			return "";
		}
	}

	/**
	 * NTP Server地址配置 设置NTP服务状态、NTP服务器地址
	 * 
	 * @param request
	 * @return
	 */
	public Map<String, String> NTPConfigure(HttpServletRequest request)
	{
		return NTPConfigure(request.getParameter("device_id"), request
				.getParameter("status"), request.getParameter("type"), request
				.getParameter("main_ntp_server"), request
				.getParameter("second_ntp_server"));
	}

	/**
	 * 设置WLAN SSID信息
	 * 
	 * @param request
	 * @return
	 */
	public synchronized Map<String, String> setWlanSSID(HttpServletRequest request)
	{
		Map<String, String> resultMap = new TreeMap<String, String>(
				new NumberComparator());
		device_id = request.getParameter("device_id"); // device_id
		type = request.getParameter("type"); // 配置方式(1:tr069 2:snmpv3)
		String param = request.getParameter("param"); // 配置参数
		logger.debug("-----param=" + param);
		// 取得设备表中的值
		devInfoMap = getDevInfo(device_id);
		// 如果找不到设备信息，就返回
		if (devInfoMap.isEmpty())
		{
			return resultMap;
		}
		if ("2".equals(type))
		{// snmp
			v3param = getAuthInfoForV3(device_id);
			if (VendorUtil.IsH3CByDeviceId(device_id))
			{// 华三
				if (null != param)
				{
					String[] info_arr = param.split("\\|");
					if (null != info_arr)
					{
						for (String info : info_arr)
						{
							String[] arr = info.split("_");
							if (null != arr)
							{
								String idx = arr[0];
								String isOpen = arr[1];
								String isHide = arr[2];
								String isDel = arr[3];
								String name = arr[4];
								String ret = setH3CWlanSSID(idx, isOpen, isHide, isDel,
										name);
								resultMap.put(idx, ret);
							}
						}
					}
				}
			}
		}
		return resultMap;
	}

	/**
	 * 设置Wlan SSID状态
	 * 
	 * @param idx
	 * @param isOpen
	 * @param isHide
	 * @param isDel
	 * @return
	 */
	private String setH3CWlanSSID(String idx, String isOpen, String isHide, String isDel,
			String name)
	{
		String hh3cDot11SSIDName = "1.3.6.1.4.1.25506.2.75.4.2.2.1.2";
		String hh3cDot11SrvPolicyRowStatus = "1.3.6.1.4.1.25506.2.75.4.2.2.1.8";
		String hh3cDot11SSIDHidden = "1.3.6.1.4.1.25506.2.75.4.2.2.1.3";
		String rets = "";
		if ("1".equals(isDel))
		{
			setDataReturn retDel = snmpSet(hh3cDot11SrvPolicyRowStatus + "." + idx, "6");
			if (null == retDel || !"true".equals(retDel.result.toLowerCase()))
			{
				return rets + "删除失败.";
			}
			else
			{
				return rets + "删除成功.";
			}
		}
		else
		{
			// 获取原SSID开启或关闭状态
			Data dataOpen = snmpGet(hh3cDot11SrvPolicyRowStatus + "." + idx);
			String nowOpen = dataOpen.dataStr;// 是否做开启或关闭操作
			boolean flag = isOpen.equals(nowOpen);
			int count = 0;
			// 先获取原SSID名称，如果名称发生了变化，则重新设置名称
			Data dataName = snmpGet(hh3cDot11SSIDName + "." + idx);
			if (null != dataName && !name.equals(dataName.dataStr))
			{
				// 改变名称之前要先关闭SSID
				boolean isClosed = true;
				if (!"2".equals(nowOpen))
				{
					count++;
					// 原OPEN状态为开启，则先关闭
					setDataReturn retOpen = snmpSet(hh3cDot11SrvPolicyRowStatus + "."
							+ idx, "2");
					if (null != retOpen && "true".equals(retOpen.result.toLowerCase()))
					{
						nowOpen = "2";
						isClosed = true;
						if (!flag && count == 1)
						{
							rets += "更改使用状态成功；";
						}
					}
					else
					{
						isClosed = false;
						if (!flag && count == 1)
						{
							rets += "更改使用状态失败；";
						}
					}
				}
				if (isClosed)
				{
					setDataReturn retName = snmpSet(hh3cDot11SSIDName + "." + idx, name);
					if (null != retName && "true".equals(retName.result.toLowerCase()))
					{
						rets += "更改名称成功；";
					}
					else
					{
						rets += "更改名称失败；";
					}
				}
				else
				{
					rets += "更改名称失败；";
				}
			}
			// 先获取原SSID广播状态，如有变化，则改之
			Data dataHide = snmpGet(hh3cDot11SSIDHidden + "." + idx);
			if (null != dataHide && !isHide.equals(dataHide.dataStr))
			{
				// 改变广播状态之前要先关闭SSID
				boolean isClosed = true;
				if (!"2".equals(nowOpen))
				{
					count++;
					// 原OPEN状态为开启，则先关闭
					setDataReturn retOpen = snmpSet(hh3cDot11SrvPolicyRowStatus + "."
							+ idx, "2");
					if (null != retOpen && "true".equals(retOpen.result.toLowerCase()))
					{
						nowOpen = "2";
						isClosed = true;
						if (!flag && count == 1)
						{
							rets += "更改使用状态成功；";
						}
					}
					else
					{
						isClosed = false;
						if (!flag && count == 1)
						{
							rets += "更改使用状态失败；";
						}
					}
				}
				if (isClosed)
				{
					setDataReturn retHide = snmpSet(hh3cDot11SSIDHidden + "." + idx,
							isHide);
					if (null != retHide && "true".equals(retHide.result.toLowerCase()))
					{
						rets += "更改广播状态成功；";
					}
					else
					{
						rets += "更改广播状态失败；";
					}
				}
				else
				{
					rets += "更改广播状态失败；";
				}
			}
			// 判断OPEN目前状态，如有变化，则改之
			if (!isOpen.equals(nowOpen))
			{
				setDataReturn retOpen = snmpSet(hh3cDot11SrvPolicyRowStatus + "." + idx,
						isOpen);
				if (null != retOpen && "true".equals(retOpen.result.toLowerCase()))
				{
					if (!flag)
					{
						rets += "更改使用状态成功；";
					}
				}
				else
				{
					if (!flag)
					{
						rets += "更改使用状态失败；";
					}
				}
			}
		}
		return rets;
	}

	/**
	 * 新建一个SSID
	 * 
	 * @param request
	 * @return
	 */
	public synchronized Map<String, String> addWlanSSID(HttpServletRequest request)
	{
		Map<String, String> resultMap = new TreeMap<String, String>(
				new NumberComparator());
		device_id = request.getParameter("device_id"); // device_id
		type = request.getParameter("type"); // 配置方式(1:tr069 2:snmpv3)
//		String open = request.getParameter("open"); // 配置参数
//		String hide = request.getParameter("hide"); // 配置参数
		String name = request.getParameter("name"); // 配置参数
		String new_idx = request.getParameter("new_idx"); // 新的索引id
		// 取得设备表中的值
		devInfoMap = getDevInfo(device_id);
		// 如果找不到设备信息，就返回
		if (devInfoMap.isEmpty())
		{
			return resultMap;
		}
		if ("2".equals(type))
		{// snmp
			v3param = getAuthInfoForV3(device_id);
			if (VendorUtil.IsH3CByDeviceId(device_id))
			{// 华三
				String hh3cDot11SSIDName = "1.3.6.1.4.1.25506.2.75.4.2.2.1.2";
				String hh3cDot11SrvPolicyRowStatus = "1.3.6.1.4.1.25506.2.75.4.2.2.1.8";
				String hh3cDot11SSIDHidden = "1.3.6.1.4.1.25506.2.75.4.2.2.1.3";
				String hh3cDot11AuthenMode = "1.3.6.1.4.1.25506.2.75.4.2.2.1.4";
				String hh3cDot11SSIDEncryptionMode = "1.3.6.1.4.1.25506.2.75.4.2.2.1.5";
				Data[] dataList = snmpWalk(hh3cDot11SSIDName);
				String idx = findIndexInWalkWithValue(dataList, name);
				// 如果存在同名SSID，则删除先
				if (idx != null)
				{
					setDataReturn ret = snmpSet(hh3cDot11SrvPolicyRowStatus + "." + idx,
							"6");
					if (!"true".equals(ret.result.toLowerCase()))
					{
						resultMap.put(new_idx, "删除同名SSID失败！");
						return resultMap;
					}
				}
				OIDSTR w1 = getOIDStr(hh3cDot11SSIDName + "." + new_idx, 4, name);
				OIDSTR w2 = getOIDStr(hh3cDot11SSIDHidden + "." + new_idx, 2, "2");
				OIDSTR w3 = getOIDStr(hh3cDot11AuthenMode + "." + new_idx, 2, "2");
				OIDSTR w4 = getOIDStr(hh3cDot11SSIDEncryptionMode + "." + new_idx, 2, "1");
				OIDSTR w5 = getOIDStr(hh3cDot11SrvPolicyRowStatus + "." + new_idx, 2, "4");
				OIDSTR[] oidArr = new OIDSTR[] { w1, w2, w3, w4, w5 };
				setDataReturn ret = snmpCreateListSingle(oidArr);
				if (null == ret || !"true".equals(ret.result))
				{
					resultMap.put(new_idx, "创建失败！");
				}
				else
				{
					resultMap.put(new_idx, "创建成功。");
				}
			}
		}
		return resultMap;
	}

	/**
	 * @param oidArr
	 * @return
	 */
	private setDataReturn snmpCreateListSingle(OIDSTR[] oidArr)
	{
		Performance.createOidNodeList createNodeList = getDataListV3Create(oidArr);
		Performance.createOidNodeList[] createNodeListArr = new Performance.createOidNodeList[1];
		createNodeListArr[0] = createNodeList;
		setDataReturn[] ret = SnmpGatherInterfaceV3.getInstance().createOidNodeListV3(
				createNodeListArr, createNodeListArr.length, devInfoMap);
		if (null == ret)
		{
			return new setDataReturn();
		}
		return ret[0];
	}

	/**
	 * @param oidArr
	 * @return
	 */
	private setDataReturn[] snmpCreateList(OIDSTR[] oidArr)
	{
		Performance.createOidNodeList createNodeList = getDataListV3Create(oidArr);
		Performance.createOidNodeList[] createNodeListArr = new Performance.createOidNodeList[1];
		createNodeListArr[0] = createNodeList;
		return SnmpGatherInterfaceV3.getInstance().createOidNodeListV3(createNodeListArr,
				createNodeListArr.length, devInfoMap);
	}

	/**
	 * 创建结点 type 2 int,3 Bits,4 string,6 Oid,64 Ipaddress
	 * 
	 * @param device_id
	 * @param loopback_ip
	 * @param oid
	 * @param value
	 * @return dataV3
	 */
	public createOidNodeList getDataListV3Create(OIDSTR[] oidArr)
	{
		createOidNodeList dataV3Create = new createOidNodeList();
		dataV3Create.loopback_ip = loopback_ip;
		dataV3Create.port = "161";
		dataV3Create.list = oidArr;
		dataV3Create.readComm = "";
		dataV3Create.writeComm = "";
		dataV3Create.v3param = v3param;
		return dataV3Create;
	}

	/**
	 * 取得一个结点对象
	 * 
	 * @param oid
	 * @param type
	 * @param value
	 * @return
	 */
	public OIDSTR getOIDStr(String oid, int type, String value)
	{
		OIDSTR oidStr = new OIDSTR();
		oidStr.oid = oid;
		oidStr.type = type;
		oidStr.value = value;
		return oidStr;
	}

	/**
	 * 获取WLAN SSID信息
	 * 
	 * @param request
	 * @return
	 */
	public Map<String, List<String>> getWlanSSID(HttpServletRequest request)
	{
		Map<String, List<String>> resultMap = new TreeMap<String, List<String>>(
				new NumberComparator());
		device_id = request.getParameter("device_id"); // device_id
		type = request.getParameter("type"); // 配置方式(1:tr069 2:snmpv3)
		// 取得设备表中的值
		devInfoMap = getDevInfo(device_id);
		// 如果找不到设备信息，就返回
		if (devInfoMap.isEmpty())
		{
			return resultMap;
		}
		if ("2".equals(type))
		{// snmp
			v3param = getAuthInfoForV3(device_id);
			if (VendorUtil.IsH3CByDeviceId(device_id))
			{// 华三
				String hh3cDot11SSIDName = "1.3.6.1.4.1.25506.2.75.4.2.2.1.2";
				String hh3cDot11SrvPolicyRowStatus = "1.3.6.1.4.1.25506.2.75.4.2.2.1.8";
				String hh3cDot11SSIDHidden = "1.3.6.1.4.1.25506.2.75.4.2.2.1.3";
				Data[] dataList = snmpWalk(hh3cDot11SSIDName);
				if (dataList != null)
				{
					logger.debug("-------dataList.length=" + dataList.length + "-------");
					for (int i = 0; i < dataList.length; i++)
					{
						Data data = dataList[i];
						if (data == null)
						{
							logger.warn("------------data is null--------------");
							continue;
						}
						List<String> list = new ArrayList<String>();
						String idx = data.index;
						String ssid_name = data.dataStr;
						String hh3cDot11SrvPolicyRowStatus_ = hh3cDot11SrvPolicyRowStatus
								+ "." + idx;
						Data d1 = snmpGet(hh3cDot11SrvPolicyRowStatus_);
						String hh3cDot11SSIDHidden_ = hh3cDot11SSIDHidden + "." + idx;
						Data d2 = snmpGet(hh3cDot11SSIDHidden_);
						list.add(idx);
						list.add(ssid_name);
						list.add(d1 == null ? "" : d1.dataStr);
						list.add(d2 == null ? "" : d2.dataStr);
						resultMap.put(idx, list);
					}
				}
			}// end 华三
		}
		return resultMap;
	}

	/**
	 * 删除参数实例，成功返回 1 ；失败 0
	 * 
	 * @param paraV
	 * @param ior
	 * @param device_id
	 * @param gather_id
	 * @return int flag
	 */
	public int delPara(String paraV, String gw_type)
	{
		int flag = 0;
		DeleteObject delObject = new DeleteObject();
		delObject.setObjectName(paraV);
		delObject.setParameterKey("");
		DevRpc[] devRPCArr = getDevRPCArr(delObject);
		List<DevRpcCmdOBJ> devRPCRep = getDevRPCResponse(devRPCArr,Global.RpcCmd_Type, gw_type);
		// 一个设备返回的命令
		if (devRPCRep == null || devRPCRep.size() == 0 || devRPCRep.get(0) == null)
		{
			return flag;
		}
		// 一个设备返回的命令
		if (devRPCRep.get(0).getStat() == 1)
		{
			flag = 1;
		}
		return flag;
	}

	/**
	 * 增加参数实例，成功返回 1 ；失败 0
	 * 
	 * @param paraV
	 * @return int flag
	 */
	public int addPara(String paraV, String gw_type)
	{
		int flag = 0;
		AddObject addObject = new AddObject();
		addObject.setObjectName(paraV);
		addObject.setParameterKey("");
		DevRpc[] devRPCArr = getDevRPCArr(addObject);
		List<DevRpcCmdOBJ> devRPCRep = getDevRPCResponse(devRPCArr,Global.RpcCmd_Type, gw_type);
		// 一个设备返回的命令
		if (devRPCRep == null || devRPCRep.size() == 0 || devRPCRep.get(0) == null)
		{
			return flag;
		}
		// 一个设备返回的命令
		if (devRPCRep.get(0).getStat() == 1)
		{
			flag = 1;
		}
		return flag;
	}

	/**
	 * 新建一个SSID
	 * 
	 * @param request
	 * @return
	 */
	public Map<String, String> addWlanSSIDTr069(HttpServletRequest request, String gw_type)
	{
		Map<String, String> resultMap = new HashMap<String, String>();
		device_id = request.getParameter("device_id"); // device_id
		// type = request.getParameter("type"); // 配置方式(1:tr069 2:snmpv3)
		String open = request.getParameter("open"); // 配置参数
		String hide = request.getParameter("hide"); // 配置参数
		String name = request.getParameter("name"); // 配置参数
//		String channel = request.getParameter("open"); // 配置参数
//		String model = request.getParameter("hide"); // 配置参数
//		String power = request.getParameter("power"); // 配置参数
		String new_ssid_idx = request.getParameter("new_ssid_idx"); // 新的索引id
		String new_wlan_idx = request.getParameter("new_wlan_idx"); // wlan索引id
		// 取得设备表中的值
		devInfoMap = getDevInfo(device_id);
		// 如果找不到设备信息，就返回
		if (devInfoMap.isEmpty())
		{
			return resultMap;
		}
		String ns1 = "InternetGatewayDevice.LANDevice.";
		String ns2 = ".WLANConfiguration.";
		String ns3 = ns1 + new_wlan_idx + ns2;// + new_ssid_idx + ".";
		String ns4 = ns1 + new_wlan_idx + ns2 + new_ssid_idx + ".";
		List<String[]> setSsidIdx = new ArrayList<String[]>();
		int ret = 0;
		ret = addPara(ns3, gw_type);
		if (1 == ret)
		{
			logger.debug("----- add ns3 success");
			resultMap.put(device_name, "新建SSID实例节点成功！");
			setSsidIdx.add(new String[] { ns4 + "SSID", "1", name });
			setSsidIdx.add(new String[] { ns4 + "Enable", "4", open });
			setSsidIdx.add(new String[] { ns4 + "X_CT-COM_SSIDHide", "4", hide });
			// setSsidIdx.add(new String[] { ns4 + "Channel", "3", channel });
			// setSsidIdx.add(new String[] { ns4 + "Standard", "1", model });
			// setSsidIdx.add(new String[] { ns4 + "X_CT-COM_Powerlevel", "3",
			// power });
			// ret = addPara(ns3 + "SSID");
			// if (1 == ret) {
			// setSsidIdx.add(new String[] { ns3 + "SSID", "1", name });
			// }
			// ret = addPara(ns3 + "Enable");
			// if (1 == ret) {
			// setSsidIdx.add(new String[] { ns3 + "Enable", "4", open });
			// }
			// ret = addPara(ns3 + "X_CT-COM_SSIDHide");
			// if (1 == ret) {
			// setSsidIdx.add(new String[] { ns3 + "X_CT-COM_SSIDHide", "4",
			// hide });
			// }
			// ret = addPara(ns3 + "Channel");
			// if (1 == ret) {
			// setSsidIdx.add(new String[] { ns3 + "Channel", "3", channel });
			// }
			// ret = addPara(ns3 + "Standard");
			// if (1 == ret) {
			// setSsidIdx.add(new String[] { ns3 + "Standard", "1", model });
			// }
			// ret = addPara(ns3 + "X_CT-COM_Powerlevel");
			// if (1 == ret) {
			// setSsidIdx.add(new String[] { ns3 + "X_CT-COM_Powerlevel", "3",
			// power });
			// }
			int setSize = setSsidIdx.size();
			if (setSize > 0)
			{
				String[][] params = new String[setSize][];
				for (int j = 0; j < setSize; j++)
				{
					params[j] = setSsidIdx.get(j);
				}
				ParameterValueStruct[] parameterValueStructArr = new ParameterValueStruct[params.length];
				for (int i = 0; i < params.length; i++)
				{
					String[] p = params[i];
					logger.debug("---------------------setSsidIdx name=" + p[0]);
					logger.debug("---------------------setSsidIdx value=" + p[2]);
					AnyObject anyObject = new AnyObject();
					anyObject.para_value = p[2];
					anyObject.para_type_id = p[1];
					parameterValueStructArr[i] = new ParameterValueStruct();
					parameterValueStructArr[i].setName(p[0]);
					parameterValueStructArr[i].setValue(anyObject);
				}
				SetParameterValues setParameterValues = new SetParameterValues();
				setParameterValues.setParameterList(parameterValueStructArr);
				DevRpc[] devRPCArr = getDevRPCArr(setParameterValues);
				List<DevRpcCmdOBJ> devRPCRep = getDevRPCResponse(devRPCArr,Global.RpcCmd_Type, gw_type);
				// 一个设备返回的命令
				if (devRPCRep == null || devRPCRep.size() == 0
						|| devRPCRep.get(0) == null)
				{
					return resultMap;
				}
				if (devRPCRep.get(0).getStat() == 1)
				{
					resultMap.put(device_name, "设置SSID值成功");
				}
				else
				{
					resultMap.put(device_name, "设置SSID值失败");
				}
			}
			else
			{
				resultMap.put(device_name, "新建SSID属性实例失败！");
			}
		}
		else
		{
			logger.debug("----- add ns3 fail");
			resultMap.put(device_name, "新建SSID实例失败！");
		}
		return resultMap;
	}

	/**
	 * 设置WLAN信息(tr069)
	 * 
	 * @param request
	 * @return
	 */
	public Map<String, String> setWlanStatusTr069(HttpServletRequest request)
	{
		logger
				.debug("Map<String, String> setWlanStatusTr069(HttpServletRequest request)");
		Map<String, String> resultMap = new HashMap<String, String>();
		device_id = request.getParameter("device_id"); // device_id
		//得到设备类型
		String gw_type = LipossGlobals.getGw_Type(device_id);
		String param = request.getParameter("param"); // 配置参数
		logger.debug("----- param=" + param);
		// 取得设备表中的值
		devInfoMap = getDevInfo(device_id);
		// 如果找不到设备信息，就返回
		if (devInfoMap.isEmpty())
		{
			return resultMap;
		}
		if (null == param || "".equals("param"))
		{
			resultMap.put(device_name, "参数为空");
			return resultMap;
		}
		String ns1 = "InternetGatewayDevice.LANDevice.";
		String ns2 = ".WLANConfiguration.";
		String tmp = "X_CT-COM_Apply";
		List<String> delWlanIdx = new ArrayList<String>();
		List<String> delSsidIdx = new ArrayList<String>();
		List<String[]> setSsidIdx = new ArrayList<String[]>();
		// boolean ciscoFlag = false;
		// if ("CISCO".equals(getDeviceModel(device_id))) {
		// ciscoFlag = true;
		// }
		boolean ciscoFlag = VendorUtil.IsCiscoByDeviceId(device_id);
		String[] wlans = param.split("\\|");
		for (int i = 0; i < wlans.length; i++)
		{
			logger.debug("------------ wlans.length = " + wlans.length);
			String ssidParam = wlans[i];
			if (ssidParam != null)
			{
				String[] ssids = ssidParam.split("\\$");
				if (ssids != null)
				{
					String idxh = ssids[0];
					// 如果wlan删除标志为1,则将之置于wlan删除队列
					String isWlanDel = ssids[8];
					if ("1".equals(isWlanDel))
					{
						String wlan_idx = idxh.substring(0, 1);
						if (!delWlanIdx.contains(wlan_idx))
						{
							delWlanIdx.add(wlan_idx);
						}
						continue;
					}
					// 如果ssid删除标志为1,则将之置于ssid删除队列
					String isSsidDel = ssids[7];
					if ("1".equals(isSsidDel))
					{
						delSsidIdx.add(idxh);
						continue;
					}
					// 如果只是删除
					if ("-1".equals(ssids[1]))
					{
						continue;
					}
					// 设置值,将之置于设置队列
					String i1 = idxh.substring(0, 1);
					String i2 = idxh.substring(2);
					String idx = ns1 + i1 + ns2 + i2 + ".";
					String[] p1 = new String[] { idx + "SSID", "1", ssids[1] };
					String[] p2 = new String[] { idx + "Enable", "4", ssids[2] };
					String[] p3 = new String[] { idx + "X_CT-COM_SSIDHide", "4", ssids[3] };
					// String[] p4 = new String[] { idx + "Channel", "1", ssids[4] };
					String[] p5 = new String[] { idx + "Standard", "1", ssids[5] };
					String[] p6 = new String[] { idx + "X_CT-COM_Powerlevel", "3",
							ssids[6] };
					setSsidIdx.add(p1);
					setSsidIdx.add(p2);
					// setSsidIdx.add(p3);
					// setSsidIdx.add(p4);
					// setSsidIdx.add(p5);
					// setSsidIdx.add(p6);
					if (ciscoFlag)
					{
						String[] p7 = new String[] { idx + "X_CT-COM_Apply", "1", "1" };
						setSsidIdx.add(p7);
					}
				}
			}
		}
		if (delWlanIdx.size() > 0)
		{
			for (String index : delWlanIdx)
			{
				String delIdx = ns1 + index + ".";
				logger.debug("---------------------delWlanIdx idx=" + delIdx);
				int ret = delPara(delIdx,gw_type);
				if (ret == 1)
				{
					resultMap.put(device_name, "删除WLAN实例" + delIdx + "成功.");
				}
				else
				{
					resultMap.put(device_name, "删除WLAN实例" + delIdx + "失败！");
				}
			}
		}
		if (delSsidIdx.size() > 0)
		{
			for (String index : delSsidIdx)
			{
				String delIdx = ns1 + index.substring(0, 1) + ns2 + index.substring(2)
						+ ".";
				logger.debug("---------------------delSsidIdx idx=" + delIdx);
				int ret = delPara(delIdx, gw_type);
				if (ret == 1)
				{
					resultMap.put(device_name, "删除SSID实例" + delIdx + "成功.");
				}
				else
				{
					resultMap.put(device_name, "删除SSID实例" + delIdx + "失败！");
				}
			}
		}
		int setSize = setSsidIdx.size();
		if (setSize > 0)
		{
			String[][] params = new String[setSize][];
			for (int j = 0; j < setSize; j++)
			{
				params[j] = setSsidIdx.get(j);
			}
			ParameterValueStruct[] parameterValueStructArr = new ParameterValueStruct[params.length];
			for (int i = 0; i < params.length; i++)
			{
				String[] p = params[i];
				logger.debug("---------------------setSsidIdx name=" + p[0]);
				logger.debug("---------------------setSsidIdx value=" + p[2]);
				AnyObject anyObject = new AnyObject();
				anyObject.para_value = p[2];
				anyObject.para_type_id = p[1];
				parameterValueStructArr[i] = new ParameterValueStruct();
				parameterValueStructArr[i].setName(p[0]);
				parameterValueStructArr[i].setValue(anyObject);
			}
			SetParameterValues setParameterValues = new SetParameterValues();
			setParameterValues.setParameterList(parameterValueStructArr);
			DevRpc[] devRPCArr = getDevRPCArr(setParameterValues);
			List<DevRpcCmdOBJ> devRPCRep = getDevRPCResponse(devRPCArr,Global.RpcCmd_Type,gw_type);
			// 一个设备返回的命令
			if (devRPCRep == null || devRPCRep.size() == 0 || devRPCRep.get(0) == null)
			{
				return resultMap;
			}
			if (devRPCRep.get(0).getStat() == 1)
			{
				resultMap.put(device_name, "设置SSID值成功");
			}
			else
			{
				resultMap.put(device_name, "设置SSID值失败");
			}
		}
		return resultMap;
	}

	/**
	 * 获取WLAN信息(tr069)
	 * 
	 * @param request
	 * @return
	 */
	public Map<String, Map<String, Map<String, String>>> getWLANStatusTr069(
			HttpServletRequest request)
	{
		// 存放wlan实例与ssid实例的对应关系
		Map<String, Map<String, Map<String, String>>> resultMap = new TreeMap<String, Map<String, Map<String, String>>>(
				new NumberComparator());
		device_id = request.getParameter("device_id"); // device_id
		//得到设备类型
		String gw_type = LipossGlobals.getGw_Type(device_id);
		// type = request.getParameter("type"); // 配置方式(1:tr069 2:snmpv3)
		// 取得设备表中的值
		devInfoMap = getDevInfo(device_id);
		// 如果找不到设备信息，就返回
		if (devInfoMap.isEmpty())
		{
			return resultMap;
		}
		// Map<String, String> resultMap = new HashMap<String, String>();
		String ns1 = "InternetGatewayDevice.LANDevice.";
		// 获取wlan节点
		Map<String, String> paraTree = getParaTreeMap(ns1, gw_type);
		if (null == paraTree || 0 == paraTree.size())
		{
			logger.warn("// 未获取到节点(1)");
			return resultMap;
		}
		logger.debug("//---------- param tree=" + paraTree);
		// 存放实际的wlan实例
		List<String> wlanTree = new ArrayList<String>();
		Set<String> set = paraTree.keySet();
		Iterator<String> iterator = set.iterator();
		// 去除空节点、与父节点同名的节点
		while (iterator.hasNext())
		{
			String name = iterator.next();
			String value = paraTree.get(name);
			if (null == value)
			{
				continue;
			}
			// 分离出节点名
			value = value.substring(0, value.indexOf(","));
			if (null != value && !"".equals(value) && !ns1.equals(value))
			{
				// 从节点名称中分离出索引号
				wlanTree.add(value.substring(ns1.length(), ns1.length() + 1));
			}
		}
		if (0 == wlanTree.size())
		{
			logger.warn("//--------- 未获取到节点(2)");
			return resultMap;
		}
		logger.debug("//-------- wlan 数量：" + wlanTree.size());
		// 获取每个wlan节点下的SSID节点
		for (String name : wlanTree)
		{
			// 存放ssid与属性值对应关系
			Map<String, Map<String, String>> ssids = new TreeMap<String, Map<String, String>>(
					new NumberComparator());
			String ns2 = ns1 + name + ".WLANConfiguration.";
			logger.debug("//-------- ns2 ：" + ns2);
			Map<String, String> paraTree2 = getParaTreeMap(ns2, gw_type);
			logger.debug("// ------------ para tree map = " + paraTree2);
			if (null == paraTree2 || 0 == paraTree2.size())
			{
				resultMap.put(name, null);
				logger.warn("//------------ 未获取到节点(3)");
				continue;
			}
			// 存放实际的ssid实例
			List<String> ssidTree = new ArrayList<String>();
			Set<String> set2 = paraTree2.keySet();
			Iterator<String> it2 = set2.iterator();
			// 去除空节点、与父节点同名的节点
			while (it2.hasNext())
			{
				String name2 = it2.next();
				String value = paraTree2.get(name2);
				if (null == value)
				{
					continue;
				}
				// 分离出节点名
				value = value.substring(0, value.indexOf(","));
				if (null != value && !"".equals(value) && !ns2.equals(value))
				{
					// 从节点名称中分离出索引号
					ssidTree.add(value.substring(ns2.length(), ns2.length() + 1));
				}
			}
			if (0 == ssidTree.size())
			{
				resultMap.put(name, null);
				continue;
			}
			logger.debug("//------------ ssid 数量：" + ssidTree.size());
			for (String name3 : ssidTree)
			{
				String ns3 = ns2 + name3 + ".";
				logger.debug("//------------- ns3:" + ns3);
				// SSID名称
				String SSID = ns3 + "SSID";
				// SSID是否隐藏, 取值范围(true,false)，缺省值：false
				String SSIDHide = ns3 + "X_CT-COM_SSIDHide";
				// 启用或关闭对应 SSID
				String Enable = ns3 + "Enable";
				// WLAN 模块所在信道
				String Channel = ns3 + "Channel";
				// 802．11工作模式：“a” “b” “g” “b,g” (802.11b/g 混和模式)
				String Standard = ns3 + "Standard";
				// 无线模块功率的级别，本参数在多实例中应该保持一致，取值范围：{1,2,3,4,5},1为最大功率
				String Powerlevel = ns3 + "X_CT-COM_Powerlevel";
				logger.debug("//------ SSID=" + SSID);
				logger.debug("//------ SSIDHide=" + SSIDHide);
				logger.debug("//------ Enable=" + Enable);
				logger.debug("//------ Channel=" + Channel);
				logger.debug("//------ Standard=" + Standard);
				String[] paramNames = new String[] { SSID, SSIDHide, Enable, Channel,
						Standard, Powerlevel };
				// 存放ssid各属性值
				Map<String, String> values = null;
				Map<String, String> paraValues = getParaValueMap(paramNames, gw_type);
				if (null != paraValues && 0 != paraValues.size())
				{
					logger.debug("----------name3=" + name3 + "------------");
					values = new HashMap<String, String>();
					Set<String> set4 = paraValues.keySet();
					Iterator<String> it4 = set4.iterator();
					// 从节点中分离出属性名称
					while (it4.hasNext())
					{
						String name4 = it4.next();
						String value4 = paraValues.get(name4);
						name4 = name4.substring(name4.lastIndexOf(".") + 1);
						values.put(name4, value4);
					}
				}
				// values.put(SSID, "linkage");
				// ssid <-> values
				ssids.put(name3, values);
			}
			// wlan <-> ssids
			resultMap.put(name, ssids);
		}
		logger.debug("//---------- resultMap arr = " + resultMap);
		return resultMap;
	}

	/**
	 * 获取WLAN状态（开启 or 关闭)
	 * 
	 * @param request
	 * @return
	 */
	public Map<String, String> getWLANStatus(HttpServletRequest request)
	{
		Map<String, String> resultMap = new HashMap<String, String>();
		device_id = request.getParameter("device_id"); // device_id
		type = request.getParameter("type"); // 配置方式(1:tr069 2:snmpv3)
		// 取得设备表中的值
		devInfoMap = getDevInfo(device_id);
		// 如果找不到设备信息，就返回
		if (devInfoMap.isEmpty())
		{
			return resultMap;
		}
		if ("1".equals(type))
		{
			// return getWLANStatusTr069(request);
		}
		if ("2".equals(type))
		{// snmp
			v3param = getAuthInfoForV3(device_id);
			if (VendorUtil.IsH3CByDeviceId(device_id))
			{
				String wlan_status = getH3CWlanStatus();// 无线开启或关闭
				resultMap.put("wlan_status", wlan_status);
				String wlan_model = getH3CWlanModel();// 无线网络模式
				resultMap.put("wlan_model", wlan_model);
				String wlan_channel = getH3CWlanChannel();// 无线信道
				resultMap.put("wlan_channel", wlan_channel);
				String wlan_power = getH3CWlanPower();// 发射功率
				resultMap.put("wlan_power", wlan_power);
				resultMap.put("oui", oui);
			}
			else if (VendorUtil.IsCiscoByDeviceId(device_id))
			{
				String wlan_status = getCiscoWlanStatus();// 无线开启或关闭
				resultMap.put("wlan_status", wlan_status);// 1:enable
				// 0:disable
				String wlan_model = getCiscoWlanModel();// 无线网络模式
				resultMap.put("wlan_model", wlan_model);
				// String wlan_channel = getCiscoWlanChannel();// 无线信道
				// resultMap.put("wlan_channel", wlan_channel);
				//
				// String wlan_power = getCiscoWlanPower();// 发射功率
				// resultMap.put("wlan_power", wlan_power);
				resultMap.put("oui", oui);
			}
		}
		return resultMap;
	}

	/**
	 * 获取华三设备的WLAN无线发射功率
	 * 
	 * @return
	 */
	private String getH3CWlanPower()
	{
		String ifName = "1.3.6.1.2.1.31.1.1.1.1";
		String hh3cDot11RadioCfgMaxTxPwrLvl = "1.3.6.1.4.1.25506.2.75.4.4.1.1.11";
		String value = "WLAN-Radio2/0";
		Data[] dataList = snmpWalk(ifName);
		if (dataList == null)
		{
			return null;
		}
		String idx = null;
		idx = findIndexInWalkWithValue(dataList, value);
		if (idx == null)
		{
			return null;
		}
		hh3cDot11RadioCfgMaxTxPwrLvl += "." + idx;
		Data data = snmpGet(hh3cDot11RadioCfgMaxTxPwrLvl);
		if (data == null)
		{
			return null;
		}
		return data.dataStr;
	}

	/**
	 * 获取Cisco设备的WLAN无线发射功率
	 * 
	 * @return
	 */
	private String getCiscoWlanPower()
	{
		String ifName = "1.3.6.1.2.1.31.1.1.1.1";
		String hh3cDot11RadioCfgMaxTxPwrLvl = "1.3.6.1.4.1.25506.2.75.4.4.1.1.11";
		String value = "WLAN-Radio2/0";
		Data[] dataList = snmpWalk(ifName);
		if (dataList == null)
		{
			return null;
		}
		String idx = null;
		idx = findIndexInWalkWithValue(dataList, value);
		if (idx == null)
		{
			return null;
		}
		hh3cDot11RadioCfgMaxTxPwrLvl += "." + idx;
		Data data = snmpGet(hh3cDot11RadioCfgMaxTxPwrLvl);
		if (data == null)
		{
			return null;
		}
		return data.dataStr;
	}

	/**
	 * 获取华三设备的WLAN无线信道
	 * 
	 * @return
	 */
	private String getH3CWlanChannel()
	{
		String ifName = "1.3.6.1.2.1.31.1.1.1.1";
		String hh3cDot11RadioCfgChannel = "1.3.6.1.4.1.25506.2.75.4.4.1.1.10";
		String value = "WLAN-Radio2/0";
		Data[] dataList = snmpWalk(ifName);
		if (dataList == null)
		{
			return null;
		}
		String idx = null;
		idx = findIndexInWalkWithValue(dataList, value);
		if (idx == null)
		{
			return null;
		}
		hh3cDot11RadioCfgChannel += "." + idx;
		Data data = snmpGet(hh3cDot11RadioCfgChannel);
		if (data == null)
		{
			return null;
		}
		return data.dataStr;
	}

	/**
	 * 获取Cisco设备的WLAN无线信道
	 * 
	 * @return
	 */
	private String getCiscoWlanChannel()
	{
		String ifName = "1.3.6.1.2.1.31.1.1.1.1";
		String hh3cDot11RadioCfgChannel = "1.3.6.1.4.1.25506.2.75.4.4.1.1.10";
		String value = "WLAN-Radio2/0";
		Data[] dataList = snmpWalk(ifName);
		if (dataList == null)
		{
			return null;
		}
		String idx = null;
		idx = findIndexInWalkWithValue(dataList, value);
		if (idx == null)
		{
			return null;
		}
		hh3cDot11RadioCfgChannel += "." + idx;
		Data data = snmpGet(hh3cDot11RadioCfgChannel);
		if (data == null)
		{
			return null;
		}
		return data.dataStr;
	}

	/**
	 * 获取华三设备的WLAN无线网络模式
	 * 
	 * @return
	 */
	private String getH3CWlanModel()
	{
		String ifName = "1.3.6.1.2.1.31.1.1.1.1";
		String hh3cDot11RadioCfgType = "1.3.6.1.4.1.25506.2.75.4.4.1.1.9";
		String value = "WLAN-Radio2/0";
		Data[] dataList = snmpWalk(ifName);
		if (dataList == null)
		{
			return null;
		}
		String idx = null;
		idx = findIndexInWalkWithValue(dataList, value);
		if (idx == null)
		{
			return null;
		}
		hh3cDot11RadioCfgType += "." + idx;
		Data data = snmpGet(hh3cDot11RadioCfgType);
		if (data == null)
		{
			return null;
		}
		return data.dataStr;
	}

	/**
	 * 获取Cisco设备的WLAN无线网络模式
	 * 
	 * @return
	 */
	private String getCiscoWlanModel()
	{
		String ifName = "1.3.6.1.4.1.9.24.2.0";
		Data data = snmpGet(ifName);
		if (data == null)
		{
			return null;
		}
		return data.dataStr;
	}

	/**
	 * 获取华三设备的WLAN无线开启或关闭
	 * 
	 * @return
	 */
	private String getH3CWlanStatus()
	{
		String ifDescr = "1.3.6.1.2.1.2.2.1.2";// ifDescr
		String ifAdminStatusd = "1.3.6.1.2.1.2.2.1.7";// ifAdminStatusd
		String value = "WLAN-Radio2/0";
		Data[] dataList = snmpWalk(ifDescr);
		if (dataList == null)
		{
			return null;
		}
		String idx = null;
		idx = findIndexInWalkWithValue(dataList, value);
		if (idx == null)
		{
			return null;
		}
		ifAdminStatusd += "." + idx;
		Data data = snmpGet(ifAdminStatusd);
		if (data == null)
		{
			return null;
		}
		return data.dataStr;
	}

	/**
	 * 获取Cisco设备的WLAN无线开启或关闭
	 * 
	 * @return
	 */
	private String getCiscoWlanStatus()
	{
		String ifDescr = "1.3.6.1.4.1.9.24.1.0";// ifDescr
		Data data = snmpGet(ifDescr);
		if (data == null)
		{
			return null;
		}
		return data.dataStr;
	}

	/**
	 * snmpGet
	 * 
	 * @param oid
	 * @return
	 */
	private Data snmpGet(String oid)
	{
		return SnmpGatherInterfaceV3.getInstance().getDataIMDV3Single(oid, devInfoMap);
	}

	/**
	 * snmpSet
	 * 
	 * @param oid
	 * @param value
	 * @return
	 */
	private setDataReturn snmpSet(String oid, String value)
	{
		setDataV3 data = new setDataV3();
		data.loopback_ip = loopback_ip;
		data.port = "161";
		data.oid = oid;
		data.value = value;
		data.readComm = "";
		data.writeComm = "";
		data.v3param = v3param;
		setDataV3[] setDataV3_arr = new setDataV3[1];
		setDataV3_arr[0] = data;
		// 调用接口中方法
		setDataReturn[] setDataReturnArr = SnmpGatherInterfaceV3.getInstance().setWayV3(
				setDataV3_arr, setDataV3_arr.length, devInfoMap);
		if (null == setDataReturnArr || 0 == setDataReturnArr.length)
		{
			return null;
		}
		else
		{
			return setDataReturnArr[0];
		}
	}

	/**
	 * snmpWalk
	 * 
	 * @param oid
	 * @return
	 */
	private Data[] snmpWalk(String oid)
	{
		return SnmpGatherInterfaceV3.getInstance().getDataListIMDV3(oid, devInfoMap);
	}

	/**
	 * 根据value找到对应的index
	 * 
	 * @param dataList
	 * @param value
	 * @return
	 */
	private String findIndexInWalkWithValue(Data[] dataList, String value)
	{
		if (dataList == null)
			return null;
		for (int i = 0; i < dataList.length; i++)
		{
			Data data = dataList[i];
			if (value.equals(data.dataStr))
			{
				logger.debug("[" + i + "]index=" + data.index);
				logger.debug("[" + i + "]dataDou=" + data.dataDou);
				logger.debug("[" + i + "]dataStr=" + data.dataStr);
				return data.index;
			}
		}
		return null;
	}

	/**
	 * 根据value找到对应的index
	 * 
	 * @param dataList
	 * @param value
	 * @return
	 */
	private String findIndexInWalkWithValue(Data[] dataList, int value)
	{
		if (dataList == null)
			return null;
		for (int i = 0; i < dataList.length; i++)
		{
			Data data = dataList[i];
			if (value == data.dataDou)
			{
				logger.debug("[" + i + "]index=" + data.index);
				logger.debug("[" + i + "]dataDou=" + data.dataDou);
				logger.debug("[" + i + "]dataStr=" + data.dataStr);
				return data.index;
			}
		}
		return null;
	}

	/**
	 * WLAN配置 开启和关闭无线
	 * 
	 * @param request
	 * @return
	 */
	public Map<String, String> WLANConfigure(HttpServletRequest request)
	{
		Map<String, String> resultMap = new HashMap<String, String>();
		device_id = request.getParameter("device_id"); // device_id
		String oid_type = ""; // Oid类型//TODO :NTP类型还未定义
		String status = request.getParameter("status"); // 服务状态
		String model = request.getParameter("model"); // 网络模式
		String channel = request.getParameter("channel"); // 无线信道
		String power = request.getParameter("power"); // 无线功率
		type = request.getParameter("type"); // 配置方式
		// 取得设备表中的值
		devInfoMap = getDevInfo(device_id);
		// 如果找不到设备信息，就返回
		if (devInfoMap.isEmpty())
		{
			return resultMap;
		}
		if ("2".equals(type))
		{// snmp
			v3param = getAuthInfoForV3(device_id);
			int i = 0;
			if (VendorUtil.IsH3CByDeviceId(device_id))
			{
				resultMap.put(device_name + (i++), setH3CWlanStatus(status));
				resultMap.put(device_name + (i++), setH3CWlanModel(model));
				resultMap.put(device_name + (i++), setH3CWlanChannel(channel));
				resultMap.put(device_name + (i++), setH3CWlanPower(power));
				resultMap.put("oui", oui);
			}
			else if (VendorUtil.IsCiscoByDeviceId(device_id))
			{
				resultMap.put(device_name + (i++), setCiscoWlanStatus(status));
				resultMap.put(device_name + (i++), setCiscoWlanModel(model));
				resultMap.put("oui", oui);
			}
			else if (VendorUtil.IsAlcatelByDeviceId(device_id))
			{
				resultMap.put("oui", oui);
			}
			else if ("10000".equals(oui))
			{// 赛诺
				resultMap.put("oui", oui);
			}// end 赛诺
		}
		return resultMap;
	}

	/**
	 * 设置华三设备的WLAN无线网络模式
	 * 
	 * @return
	 */
	private String setH3CWlanModel(String model)
	{
		String ifName = "1.3.6.1.2.1.31.1.1.1.1";
		String hh3cDot11RadioCfgType = "1.3.6.1.4.1.25506.2.75.4.4.1.1.9";
		String value = "WLAN-Radio2/0";
		Data[] dataList = snmpWalk(ifName);
		if (dataList == null)
		{
			return "网络模式配置失败";
		}
		String idx = null;
		idx = findIndexInWalkWithValue(dataList, value);
		if (idx == null)
		{
			return "网络模式配置失败";
		}
		hh3cDot11RadioCfgType += "." + idx;
		setDataReturn dataRet = snmpSet(hh3cDot11RadioCfgType, model);
		if (null != dataRet)
		{
			return "网络模式配置成功";
		}
		else
		{
			return "网络模式配置失败";
		}
	}

	/**
	 * 设置Cisco设备的WLAN无线网络模式
	 * 
	 * @return
	 */
	private String setCiscoWlanModel(String model)
	{
		String ifName = "1.3.6.1.4.1.9.24.2.0";
		setDataReturn dataRet = snmpSet(ifName, model);
		if (null != dataRet)
		{
			return "网络模式配置成功";
		}
		else
		{
			return "网络模式配置失败";
		}
	}

	/**
	 * 设置华三设备的WLAN无线无线信道
	 * 
	 * @return
	 */
	private String setH3CWlanChannel(String channel)
	{
		String ifName = "1.3.6.1.2.1.31.1.1.1.1";
		String hh3cDot11RadioCfgChannel = "1.3.6.1.4.1.25506.2.75.4.4.1.1.10";
		String value = "WLAN-Radio2/0";
		Data[] dataList = snmpWalk(ifName);
		if (dataList == null)
		{
			return "无线信道配置失败";
		}
		String idx = null;
		idx = findIndexInWalkWithValue(dataList, value);
		if (idx == null)
		{
			return "无线信道配置失败";
		}
		hh3cDot11RadioCfgChannel += "." + idx;
		setDataReturn dataRet = snmpSet(hh3cDot11RadioCfgChannel, channel);
		if (null != dataRet)
		{
			return "无线信道配置成功";
		}
		else
		{
			return "无线信道配置失败";
		}
	}

	/**
	 * 设置华三设备的WLAN无线功率
	 * 
	 * @return
	 */
	private String setH3CWlanPower(String power)
	{
		String ifName = "1.3.6.1.2.1.31.1.1.1.1";
		String hh3cDot11RadioCfgMaxTxPwrLvl = "1.3.6.1.4.1.25506.2.75.4.4.1.1.11";
		String value = "WLAN-Radio2/0";
		Data[] dataList = snmpWalk(ifName);
		if (dataList == null)
		{
			return "无线功率配置失败";
		}
		String idx = null;
		idx = findIndexInWalkWithValue(dataList, value);
		if (idx == null)
		{
			return "无线功率配置失败";
		}
		hh3cDot11RadioCfgMaxTxPwrLvl += "." + idx;
		setDataReturn dataRet = snmpSet(hh3cDot11RadioCfgMaxTxPwrLvl, power);
		if (null != dataRet)
		{
			return "无线功率配置成功";
		}
		else
		{
			return "无线功率配置失败";
		}
	}

	/**
	 * 设置华三设备的WLAN无线开启或关闭
	 * 
	 * @return
	 */
	private String setH3CWlanStatus(String status)
	{
		String ifDescr = "1.3.6.1.2.1.2.2.1.2";// ifDescr
		String ifAdminStatusd = "1.3.6.1.2.1.2.2.1.7";// ifAdminStatusd
		String value = "WLAN-Radio2/0";
		Data[] dataList = snmpWalk(ifDescr);
		if (dataList == null)
		{
			return "无线状态配置失败";
		}
		String idx = null;
		idx = findIndexInWalkWithValue(dataList, value);
		if (idx == null)
		{
			return "无线状态配置失败";
		}
		ifAdminStatusd += "." + idx;
		setDataReturn dataRet = snmpSet(ifAdminStatusd, status);
		if (null != dataRet)
		{
			return "无线状态配置成功";
		}
		else
		{
			return "无线状态配置失败";
		}
	}

	/**
	 * 设置Cisco设备的WLAN无线开启或关闭
	 * 
	 * @return
	 */
	private String setCiscoWlanStatus(String status)
	{
		String ifDescr = "1.3.6.1.4.1.9.24.1.0";// ifDescr
		setDataReturn dataRet = snmpSet(ifDescr, "2".equals(status) ? "0" : status);
		if (null != dataRet)
		{
			return "无线状态配置成功";
		}
		else
		{
			return "无线状态配置失败";
		}
	}

	/**
	 * 获取节点下子节点名称和读写属性
	 * 
	 * @param path
	 * @return
	 */
	private Map<String, String> getParaTreeMap(String path, String gw_type)
	{
		Map<String, String> paramMap = new HashMap<String, String>();
		GetParameterNames getParameterNames = new GetParameterNames();
		getParameterNames.setParameterPath(path);
		getParameterNames.setNextLevel(1);
		DevRpc[] devRPCArr = getDevRPCArr(getParameterNames);
		List<DevRpcCmdOBJ> devRpcCmdOBJList = this.getDevRPCResponse(devRPCArr,Global.RpcCmd_Type, gw_type);
		if (null == devRpcCmdOBJList || 0 == devRpcCmdOBJList.size())
		{
			return paramMap;
		}
		// 一个设备返回的命令
		Element element = dealDevRPCResponse("GetParameterNamesResponse",
				devRpcCmdOBJList, device_id);
		if (element == null)
		{
			return paramMap;
		}
		GetParameterNamesResponse getParameterNamesResponse = new GetParameterNamesResponse();
		// 把SOAP形式的文件转换成标准的XML,便于通信
		getParameterNamesResponse = XmlToRpc.GetParameterNamesResponse(element);
		// 通过这个XML对象,获取参数列表
		if (null != getParameterNamesResponse)
		{
			ParameterInfoStruct[] pisArr = getParameterNamesResponse.getParameterList();
			if (null != pisArr)
			{
				String name = null;
				for (int i = 0; i < pisArr.length; i++)
				{
					name = pisArr[i].getName();
					String writable = pisArr[i].getWritable();
					paramMap.put(i + "", name + "," + writable);
				}
			}
		}
		return paramMap;
	}

	/**
	 * 单台设备单条命令返回的RPC结果处理
	 * 
	 * @author wangsenbo
	 * @date Mar 22, 2011
	 * @param
	 * @return RPCObject
	 */
	private Element dealDevRPCResponse(String stringRpcName,
			List<DevRpcCmdOBJ> devRpcCmdOBJList, String deviceId)
	{
		if (devRpcCmdOBJList == null || devRpcCmdOBJList.size() == 0)
		{
			logger.warn("[{}]List<DevRpcCmdOBJ>返回为空！", deviceId);
			return null;
		}
		DevRpcCmdOBJ devRpcCmdOBJ = devRpcCmdOBJList.get(0);
		if (devRpcCmdOBJ == null)
		{
			logger.warn("[{}]DevRpcCmdOBJ返回为空！", deviceId);
			return null;
		}
		List<com.ailk.tr069.devrpc.obj.mq.Rpc> rpcCmdObjList = devRpcCmdOBJ.getRpcList();
		if (rpcCmdObjList == null || rpcCmdObjList.size() == 0)
		{
			logger.warn("[{}]List<ACSRpcCmdOBJ>返回为空！", deviceId);
			return null;
		}
		com.ailk.tr069.devrpc.obj.mq.Rpc acsRpcCmdObj = rpcCmdObjList.get(0);
		if (acsRpcCmdObj == null)
		{
			logger.warn("[{}]ACSRpcCmdOBJ返回为空！", deviceId);
			return null;
		}
		if (stringRpcName == null)
		{
			logger.warn("[{}]stringRpcName为空！", deviceId);
			return null;
		}
		if (stringRpcName.equals(acsRpcCmdObj.getRpcName()))
		{
			String resp = acsRpcCmdObj.getValue();
			logger.warn("[{}]设备返回：{}", deviceId, resp);
			Fault fault = null;
			if (resp == null || "".equals(resp))
			{
				logger.debug("[{}]DevRpcCmdOBJ.value == null", deviceId);
			}
			else
			{
				SoapOBJ soapOBJ = XML.getSoabOBJ(XML.CreateXML(resp));
				if (soapOBJ != null)
				{
					fault = XmlToRpc.Fault(soapOBJ.getRpcElement());
					if (fault != null)
					{
						logger.warn("setValue({})={}", deviceId, fault.getDetail()
								.getFaultString());
					}
					else
					{
						return soapOBJ.getRpcElement();
					}
				}
			}
		}
		return null;
	}

	/**
	 * 获取参数实例值的Map(tr069)
	 * 
	 * @param para_name
	 * @return Map paramMap
	 */
	private Map<String, String> getParaValueMap(String[] para_name, String gw_type)
	{
		Map<String, String> paramMap = new HashMap<String, String>();
		GetParameterValues getParameterValues = new GetParameterValues();
		getParameterValues.setParameterNames(para_name);
		// getParameterValues.
		DevRpc[] devRPCArr = getDevRPCArr(getParameterValues);
		// 为NULL，则直接返回
		if (devRPCArr == null)
			return paramMap;
		try
		{
			List<DevRpcCmdOBJ> devRPCRep = getDevRPCResponse(devRPCArr,Global.RpcCmd_Type, gw_type);
			// 一个设备返回的命令
			if (devRPCRep == null)
			{
				return paramMap;
			}
			// 一个设备返回的命令
			Element element = dealDevRPCResponse("GetParameterValuesResponse", devRPCRep,
					device_id);
			if (element == null)
			{
				return paramMap;
			}
			GetParameterValuesResponse getParameterValuesResponse = new GetParameterValuesResponse();
			getParameterValuesResponse = XmlToRpc.GetParameterValuesResponse(element);
			if (null != getParameterValuesResponse)
			{
				ParameterValueStruct[] pisArr = getParameterValuesResponse
						.getParameterList();
				if (pisArr != null)
				{
					String name = null;
					String value = null;
					for (int i = 0; i < pisArr.length; i++)
					{
						name = pisArr[i].getName();
						value = pisArr[i].getValue().para_value;
						paramMap.put(name, value);
						logger.debug("getParaValueMap----------------Name:" + name
								+ ",Value:" + value);
					}
				}
			}
			else
			{
				return paramMap;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return paramMap;
	}

	/**
	 * 根据device_id得到长度为1的DevRPC对象数组
	 * 
	 * @author wangsenbo
	 * @date Apr 19, 2011
	 * @param
	 * @return DevRpc[]
	 */
	public DevRpc[] getDevRPCArr(RPCObject rpcObject)
	{
		DevRpc[] devRPCArr = new DevRpc[1];
		String stringRpcValue = "";
		String stringRpcName = "";
		if (rpcObject == null)
		{
			stringRpcValue = "";
			stringRpcName = "";
		}
		else
		{
			stringRpcValue = rpcObject.toRPC();
			stringRpcName = rpcObject.getClass().getSimpleName();
		}
		devRPCArr[0] = new DevRpc();
		devRPCArr[0].devId = device_id;
		devRPCArr[0].rpcArr = new Rpc[1];
		devRPCArr[0].rpcArr[0] = new Rpc();
		devRPCArr[0].rpcArr[0].rpcId = "1";
		devRPCArr[0].rpcArr[0].rpcName = stringRpcName;
		devRPCArr[0].rpcArr[0].rpcValue = stringRpcValue;
		return devRPCArr;
	}

	/**
	 * 根据得到DevRPC[]对象调用corba接口通知后台
	 * 
	 * @author wangsenbo
	 * @date Apr 19, 2011
	 * @param
	 * @return List<DevRpcCmdOBJ>
	 */
	public List<DevRpcCmdOBJ> getDevRPCResponse(DevRpc[] devRPCArr,int rpcType, String gw_type)
	{
		logger.debug("getDevRPCResponse(devRPCArr)");
		if (devRPCArr == null)
		{
			logger.error("devRPCArr == null");
			return null;
		}
		List<DevRpcCmdOBJ> devRPCRep = null;
		DevRPCManager devRPCManager = new DevRPCManager(gw_type+"");
		devRPCRep = devRPCManager.execRPC(devRPCArr,rpcType);
		return devRPCRep;
	}

	/**
	 * 根据index排序（非自然排序）
	 * 
	 * @author Bruce
	 */
	private class NumberComparator implements Comparator
	{

		public int compare(Object arg0, Object arg1)
		{
			Integer a = new Integer(arg0.toString());
			Integer b = new Integer(arg1.toString());
			return a.compareTo(b);
			// String a = (String)arg0;
			// a = a.substring(a.lastIndexOf(".") + 1);
			// String b = (String)arg1;
			// b = b.substring(b.lastIndexOf(".") + 1);
			// return new Integer(a).compareTo(new Integer(b));
		}
	}
}
