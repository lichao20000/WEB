package com.linkage.litms.webtopo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ACS.DevRpc;
import ACS.Rpc;

import com.ailk.tr069.devrpc.obj.rpc.DevRpcCmdOBJ;
import com.linkage.litms.acs.soap.io.XML;
import com.linkage.litms.acs.soap.io.XmlToRpc;
import com.linkage.litms.acs.soap.object.Fault;
import com.linkage.litms.acs.soap.object.ParameterInfoStruct;
import com.linkage.litms.acs.soap.object.ParameterValueStruct;
import com.linkage.litms.acs.soap.object.SoapOBJ;
import com.linkage.litms.acs.soap.service.GetParameterNames;
import com.linkage.litms.acs.soap.service.GetParameterNamesResponse;
import com.linkage.litms.acs.soap.service.GetParameterValues;
import com.linkage.litms.acs.soap.service.GetParameterValuesResponse;
import com.linkage.litms.acs.soap.service.RPCObject;
import com.linkage.litms.common.corba.DevRPCManager;
import com.linkage.module.gwms.Global;

/**
 * 
 * <P>
 * Linkage Communication Technology Co., Ltd
 * <P>
 * <P>
 * Copyright 2005-2007. All right reserved.
 * <P>
 * 
 * @version 1.0.0 2007-6-16
 * @author Linkage modified by Alex.Yan (yanhj@lianchuang.com) 2007-6-16
 */
public class ParameterAct
{
	/** log */
	private static Logger logger = LoggerFactory.getLogger(ParameterAct.class);
	
	private String gw_type;
	
//	/**
//	 * 包含下一级
//	 */
//	public static int NEXT_LEVEL = 1;
//	/**
//	 * 不包含下一级
//	 */
//	public static int NOT_NEXT_LEVEL = 0;
//	/**
//	 * 存放设备型号所对应的常见参数命令
//	 */
//	// public static HashMap deviceModelParameterMap = new HashMap();
//	/**
//	 * sql语句
//	 */
////	private String deviceModelParameterByDeviceIDSql = "select c.* from tab_gw_device a,tab_devicetype_info b,tab_device_params_oid c where a.oui=b.oui and a.devicetype_id = b.devicetype_id"
////			+ " and c.device_model=b.manufacturer and c.device_version=b.device_model and a.device_id=?";
//	private String deviceModelParameterByDeviceIDSql = "select c.* from tab_gw_device a, tab_vendor b, tab_device_params_oid c,gw_device_model d "
//					+ " where a.device_model_id=d.device_model_id and a.vendor_id = b.vendor_id"
//					+ " and c.device_model=b.vendor_name and c.device_version=d.device_model and a.device_id=?";
//	/**
//	 * DLS参数命令父节点
//	 */
//	private static String DLSParam = "InternetGatewayDevice.WANDevice.1.WANDSLInterfaceConfig.";
//	/**
//	 * PPOE、IP、ATM命令父节点
//	 */
//	private static String PPOEAndIPAndATMParam = "InternetGatewayDevice.WANDevice.1.WANConnectionDevice.";
//	/**
//	 * LAN命令父节点
//	 */
//	private static String LANParam = "InternetGatewayDevice.LANDevice.1.LANEthernetInterfaceConfig.";
//	/**
//	 * WLAN命令父节点
//	 */
//	private static String WLANParam = "InternetGatewayDevice.LANDevice.1.WLANConfiguration.";
//	/**
//	 * 
//	 * @return
//	 */
//	private Map getDeviceModelParamMap(HttpServletRequest request)
//	{
//		HashMap deviceParamMap = new HashMap();
//		String device_id = request.getParameter("device_id");
//		if (null == device_id || "".equals(device_id))
//			{
//				return deviceParamMap;
//			}
//		PrepareSQL pSQL = new PrepareSQL();
//		pSQL.setSQL(deviceModelParameterByDeviceIDSql);
//		pSQL.setString(1, device_id);
//		logger.debug("deviceModelParameterByDeviceIDSql:" + pSQL.getSQL());
//		Cursor cursor = DataSetBean.getCursor(pSQL.getSQL());
//		Map fields = cursor.getNext();
//		String param_array = "";
//		String param_oid = "";
//		String param_desc = "";
//		HashMap paramMap = null;
//		// 数据库中搜索到对应的参数数据
//		if (null != fields)
//			{
//				while (null != fields)
//					{
//						param_array = (String) fields.get("param_array");
//						param_oid = (String) fields.get("param_oid");
//						param_desc = (String) fields.get("param_desc");
//						// map中已包含param_array组的参数命令
//						if (deviceParamMap.containsKey(param_array))
//							{
//								paramMap = (HashMap) deviceParamMap.get(param_array);
//								paramMap.put(param_oid, param_desc);
//							}
//						else
//							{
//								paramMap = new HashMap();
//								paramMap.put(param_oid, param_desc);
//							}
//						deviceParamMap.put(param_array, paramMap);
//						fields = cursor.getNext();
//					}
//			}
//		else
//			{
//				// 初始化DLS参数
//				paramMap = new HashMap();
//				paramMap.put("Status", "状态");
//				paramMap.put("UpstreamNoiseMargin", "上行速度");
//				paramMap.put("DownstreamMaxRate", "下行速度");
//				deviceParamMap.put("DLS", paramMap);
//				// 初始化PPPOE参数
//				paramMap = new HashMap();
//				paramMap.put("Username", "用户名");
//				paramMap.put("ConnectionStatus", "连接状态");
//				deviceParamMap.put("PPPOE", paramMap);
//				// 初始化IP
//				paramMap = new HashMap();
//				paramMap.put("ExternalIPAddress", "外部IP地址");
//				paramMap.put("DNSServers", "DNS服务器");
//				paramMap.put("MACAddress", "MAC地址");
//				deviceParamMap.put("IP", paramMap);
//				// 初始化ATM
//				paramMap = new HashMap();
//				paramMap.put("ATMEncapsulation", "ATMEncapsulation");
//				paramMap.put("ATMMaximumBurstSize", "ATMMaximumBurstSize");
//				paramMap.put("ATMPeakCellRate", "ATMPeakCellRate");
//				paramMap.put("ATMQoS", "ATMQoS");
//				paramMap.put("ATMSustainableCellRate", "ATMSustainableCellRate");
//				deviceParamMap.put("ATM", paramMap);
//				// 初始化LAN
//				paramMap = new HashMap();
//				paramMap.put("PacketsReceived", "接收包数");
//				paramMap.put("PacketsSent", "发送包数");
//				paramMap.put("BytesReceived", "接收字节数");
//				paramMap.put("BytesSent", "发送字节数");
//				paramMap.put("Status", "状态");
//				paramMap.put("Enable", "可用");
//				deviceParamMap.put("LAN", paramMap);
//				// 初始化WLAN
//				paramMap = new HashMap();
//				paramMap.put("Enable", "可用");
//				paramMap.put("Status", "状态");
//				paramMap.put("TotalBytesSent", "总共发送字节数");
//				paramMap.put("TotalBytesReceived", "总共接收字节数");
//				paramMap.put("TotalPacketsSent", "总共发送包数");
//				paramMap.put("TotalPacketsReceived", "总共接收包数");
//				deviceParamMap.put("WLAN", paramMap);
//			}
//		logger.debug("deviceModelParameterMap_size:" + deviceParamMap.size());
//		return deviceParamMap;
//	}
//	/**
//	 * param必须含有数字中
//	 * 
//	 * @param param
//	 * @return
//	 */
//	private String getRoot(String param)
//	{
//		String root = "";
//		if (param == null || "".equals(param))
//			{
//				logger.debug("param is null!");
//				return root;
//			}
//		String str = "";
//		int i = 1;
//		while (true || i > 10)
//			{
//				i++;
//				// param以.结尾的情况,去掉.
//				if (param.length() == param.lastIndexOf("."))
//					{
//						param = param.substring(0, param.length() - 1);
//					}
//				str = param.substring(param.lastIndexOf(".") + 1);
//				// 非数字就go on 循环，是数字则跳出
//				try
//					{
//						Integer.parseInt(str);
//						root = param;
//						break;
//					}
//				catch (NumberFormatException e)
//					{
//						param = param.substring(0, param.lastIndexOf("."));
//					}
//				catch (Exception e1)
//					{
//						break;
//					}
//			}
//		return root;
//	}
//	/**
//	 * 
//	 * @param type
//	 *            1:DLS,2:PPPOE、IP、ATM,3：LAN,4:WLAN
//	 * @param request
//	 * @return 没有查询到对应的设备信息、对应的type下没有配置参数命令、 没有采集到数据的情况，都会返回0长度的Map
//	 */
//	public Map getDeviceParam(int type, HttpServletRequest request)
//	{
//		logger.debug("begin getDeviceParam type:" + type + "      "
//				+ new DateTimeUtil().getLongDate());
//		HashMap deviceParam = new HashMap();
//		// 获取这个设备所支持的常见参数命令
//		Map paramArrayMap = getDeviceModelParamMap(request);
//		// 存放参数命令
//		ArrayList paramList = new ArrayList();
//		// 存放ACS返回的各命令数据
//		Map dataMap = new HashMap();
//		// 准备参数命令
//		paramList = getParam(type, request, paramArrayMap);
//		logger.debug("total paramList_size:" + paramList.size());
//		// 等待3秒，防止由于调用corba接口太频繁而出现的错误
//		try
//			{
//				java.lang.Thread.sleep(3000);
//			}
//		catch (Exception e)
//			{
//				e.printStackTrace();
//			}
//		dataMap = getParasValue(request, paramList);
//		logger.debug("dataSize:" + dataMap.size());
//		// 返回数据长度为0，则返回0长度的数据
//		if (0 == dataMap.size())
//			{
//				return deviceParam;
//			}
//		Iterator it = dataMap.keySet().iterator();
//		String key = "";
//		String value = "";
//		String key1 = "";
//		String key2 = "";
//		if (1 == type)
//			{
//				// 某组常见参数命令
//				Map paramMap = (HashMap) paramArrayMap.get("DLS");
//				Map nodeMap = new HashMap();
//				// 存放paramMap对应的常见参数命令值
//				HashMap paramsMap = new HashMap();
//				// 遍历数据取得需要的数据
//				while (it.hasNext())
//					{
//						key = (String) it.next();
//						value = (String) dataMap.get(key);
//						key1 = key.substring(0, key.lastIndexOf("."));
//						// 参数以.结尾，也就是不是最终的叶子节点
//						if (key.length() == key.lastIndexOf("."))
//							{
//								continue;
//							}
//						key2 = key.substring(key.lastIndexOf(".") + 1, key.length());
//						key1 = getRoot(key1);
//						// logger.debug("key:"+key+"key2:"+key2);
//						// 是我们要展示的参数
//						if (paramMap.containsKey(key2))
//							{
//								if (paramsMap.containsKey(key1))
//									{
//										nodeMap = (HashMap) paramsMap.get(key1);
//									}
//								else
//									{
//										nodeMap = new HashMap();
//									}
//								nodeMap.put(key2, value);
//								paramsMap.put(key1, nodeMap);
//							}
//					}
//				logger.debug("size:" + ((HashMap) paramsMap.get(key1)).size());
//				ArrayList list = new ArrayList();
//				list.add(paramMap);
//				list.add(paramsMap);
//				deviceParam.put("DLS", list);
//			}
//		else
//			if (2 == type)
//				{
//					// PPPOE
//					Map pppoeParamMap = null;
//					if (null == paramArrayMap.get("PPPOE"))
//						{
//							pppoeParamMap = new HashMap();
//						}
//					else
//						{
//							pppoeParamMap = (HashMap) paramArrayMap.get("PPPOE");
//						}
//					// IP
//					Map ipParamMap = null;
//					if (null == paramArrayMap.get("IP"))
//						{
//							ipParamMap = new HashMap();
//						}
//					else
//						{
//							ipParamMap = (HashMap) paramArrayMap.get("IP");
//						}
//					// ATM
//					Map atmParamMap = null;
//					if (null == paramArrayMap.get("ATM"))
//						{
//							atmParamMap = new HashMap();
//						}
//					else
//						{
//							atmParamMap = (HashMap) paramArrayMap.get("ATM");
//						}
//					HashMap paramsMap = null;
//					HashMap nodeMap = null;
//					logger.debug("begin select value!");
//					while (it.hasNext())
//						{
//							key = (String) it.next();
//							value = (String) dataMap.get(key);
//							key1 = key.substring(0, key.lastIndexOf("."));
//							// 参数以.结尾，也就是不是最终的叶子节点
//							if (key.length() == key.lastIndexOf("."))
//								{
//									continue;
//								}
//							key2 = key.substring(key.lastIndexOf(".") + 1, key.length());
//							key1 = getRoot(key1);
//							// PPOE连接
//							// 只展示表中配置了的参数
//							if (pppoeParamMap.containsKey(key2))
//								{
//									logger.debug("pppoeParamMap_key1:" + key1
//											+ "      key2:" + key2);
//									if (deviceParam.containsKey("PPPOE"))
//										{
//											paramsMap = (HashMap) deviceParam
//													.get("PPPOE");
//											// 包含这个参数实例的信息
//											if (paramsMap.containsKey(key1))
//												{
//													nodeMap = (HashMap) paramsMap
//															.get(key1);
//												}
//											else
//												{
//													nodeMap = new HashMap();
//												}
//											nodeMap.put(key2, value);
//											paramsMap.put(key1, nodeMap);
//										}
//									else
//										{
//											paramsMap = new HashMap();
//											nodeMap = new HashMap();
//											nodeMap.put(key2, value);
//											paramsMap.put(key1, nodeMap);
//										}
//									deviceParam.put("PPPOE", paramsMap);
//								}
//							// IP连接
//							// 只展示表中配置了的参数
//							if (ipParamMap.containsKey(key2))
//								{
//									logger.debug("ipParamMap_key1:" + key1
//											+ "      key2:" + key2);
//									if (deviceParam.containsKey("IP"))
//										{
//											paramsMap = (HashMap) deviceParam.get("IP");
//											// 包含这个参数实例的信息
//											if (paramsMap.containsKey(key1))
//												{
//													nodeMap = (HashMap) paramsMap
//															.get(key1);
//												}
//											else
//												{
//													nodeMap = new HashMap();
//												}
//											nodeMap.put(key2, value);
//											paramsMap.put(key1, nodeMap);
//										}
//									else
//										{
//											paramsMap = new HashMap();
//											nodeMap = new HashMap();
//											nodeMap.put(key2, value);
//											paramsMap.put(key1, nodeMap);
//										}
//									deviceParam.put("IP", paramsMap);
//								}
//							// ATM连接
//							// 只展示表中配置了的参数
//							if (atmParamMap.containsKey(key2))
//								{
//									logger.debug("atmParamMap_key1:" + key1
//											+ "      key2:" + key2);
//									if (deviceParam.containsKey("ATM"))
//										{
//											paramsMap = (HashMap) deviceParam.get("ATM");
//											// 包含这个参数实例的信息
//											if (paramsMap.containsKey(key1))
//												{
//													nodeMap = (HashMap) paramsMap
//															.get(key1);
//												}
//											else
//												{
//													nodeMap = new HashMap();
//												}
//											nodeMap.put(key2, value);
//											paramsMap.put(key1, nodeMap);
//										}
//									else
//										{
//											paramsMap = new HashMap();
//											nodeMap = new HashMap();
//											nodeMap.put(key2, value);
//											paramsMap.put(key1, nodeMap);
//										}
//									deviceParam.put("ATM", paramsMap);
//								}
//						}// end while
//					// PPPOE
//					ArrayList list = new ArrayList();
//					if (deviceParam.containsKey("PPPOE"))
//						{
//							paramsMap = (HashMap) deviceParam.get("PPPOE");
//						}
//					else
//						{
//							paramsMap = new HashMap();
//						}
//					// Iterator deviceIt = paramsMap.keySet().iterator();
//					// HashMap valueMap = null;
//					// Iterator valueIt = null;
//					// while(deviceIt.hasNext())
//					// {
//					// logger.debug("nodename:"+deviceIt.next());
//					// valueMap =
//					// (HashMap)paramsMap.get((String)deviceIt.next());
//					// valueIt = valueMap.keySet().iterator();
//					// while(valueIt.hasNext())
//					// {
//					// key = (String)valueIt.next();
//					// logger.debug("PPPOEkey:"+key+"
//					// value:"+valueMap.get(key));
//					// }
//					// }
//					list.add(pppoeParamMap);
//					list.add(paramsMap);
//					deviceParam.put("PPPOE", list);
//					// IP
//					list = new ArrayList();
//					if (deviceParam.containsKey("IP"))
//						{
//							paramsMap = (HashMap) deviceParam.get("IP");
//						}
//					else
//						{
//							paramsMap = new HashMap();
//						}
//					// deviceIt = paramsMap.keySet().iterator();
//					// while(deviceIt.hasNext())
//					// {
//					// logger.debug("nodename:"+deviceIt.next());
//					// valueMap =
//					// (HashMap)paramsMap.get((String)deviceIt.next());
//					// valueIt = valueMap.keySet().iterator();
//					// while(valueIt.hasNext())
//					// {
//					// key = (String)valueIt.next();
//					// logger.debug("IPkey:"+key+"
//					// value:"+valueMap.get(key));
//					// }
//					// }
//					list.add(ipParamMap);
//					list.add(paramsMap);
//					deviceParam.put("IP", list);
//					// ATM
//					list = new ArrayList();
//					if (deviceParam.containsKey("ATM"))
//						{
//							paramsMap = (HashMap) deviceParam.get("ATM");
//						}
//					else
//						{
//							paramsMap = new HashMap();
//						}
//					// deviceIt = paramsMap.keySet().iterator();
//					// while(deviceIt.hasNext())
//					// {
//					// logger.debug("nodename:"+deviceIt.next());
//					// valueMap =
//					// (HashMap)paramsMap.get((String)deviceIt.next());
//					// valueIt = valueMap.keySet().iterator();
//					// while(valueIt.hasNext())
//					// {
//					// key = (String)valueIt.next();
//					// logger.debug("ATMkey:"+key+"
//					// value:"+valueMap.get(key));
//					// }
//					// }
//					list.add(atmParamMap);
//					list.add(paramsMap);
//					deviceParam.put("ATM", list);
//				}
//			else
//				if (3 == type)
//					{
//						Map lanParamMap = (HashMap) paramArrayMap.get("LAN");
//						HashMap paramsMap = null;
//						HashMap nodeMap = null;
//						while (it.hasNext())
//							{
//								key = (String) it.next();
//								value = (String) dataMap.get(key);
//								key1 = key.substring(0, key.lastIndexOf("."));
//								// 参数以.结尾，也就是不是最终的叶子节点
//								if (key.length() == key.lastIndexOf("."))
//									{
//										continue;
//									}
//								key2 = key.substring(key.lastIndexOf(".") + 1, key
//										.length());
//								key1 = getRoot(key1);
//								if ("".equals(key1))
//									{
//										continue;
//									}
//								key1 = "LAN" + key1.substring(key1.lastIndexOf(".") + 1);
//								// 取表中配置的参数值
//								if (lanParamMap.containsKey(key2))
//									{
//										if (deviceParam.containsKey("LAN"))
//											{
//												paramsMap = (HashMap) deviceParam
//														.get("LAN");
//												// 已有这个节点的信息
//												if (paramsMap.containsKey(key1))
//													{
//														nodeMap = (HashMap) paramsMap
//																.get(key1);
//													}
//												else
//													{
//														nodeMap = new HashMap();
//													}
//												nodeMap.put(key2, value);
//												paramsMap.put(key1, nodeMap);
//											}
//										else
//											{
//												paramsMap = new HashMap();
//												nodeMap = new HashMap();
//												nodeMap.put(key2, value);
//												paramsMap.put(key1, nodeMap);
//											}
//										deviceParam.put("LAN", paramsMap);
//									}
//							}// end while
//						ArrayList list = new ArrayList();
//						if (null == deviceParam.get("LAN"))
//							{
//								paramsMap = new HashMap();
//							}
//						else
//							{
//								paramsMap = (HashMap) deviceParam.get("LAN");
//							}
//						list.add(lanParamMap);
//						list.add(paramsMap);
//						deviceParam.put("LAN", list);
//					}
//				else
//					if (4 == type)
//						{
//							Map wlanParamMap = (HashMap) paramArrayMap.get("WLAN");
//							HashMap paramsMap = null;
//							HashMap nodeMap = null;
//							while (it.hasNext())
//								{
//									key = (String) it.next();
//									value = (String) dataMap.get(key);
//									key1 = key.substring(0, key.lastIndexOf("."));
//									// 参数以.结尾，也就是不是最终的叶子节点
//									if (key.length() == key.lastIndexOf("."))
//										{
//											continue;
//										}
//									key2 = key.substring(key.lastIndexOf(".") + 1, key
//											.length());
//									key1 = getRoot(key1);
//									// 取表中配置的参数值
//									if (wlanParamMap.containsKey(key2))
//										{
//											if (deviceParam.containsKey("WLAN"))
//												{
//													paramsMap = (HashMap) deviceParam
//															.get("WLAN");
//													// 已有这个节点的信息
//													if (paramsMap.containsKey(key1))
//														{
//															nodeMap = (HashMap) paramsMap
//																	.get(key1);
//														}
//													else
//														{
//															nodeMap = new HashMap();
//														}
//													nodeMap.put(key2, value);
//													paramsMap.put(key1, nodeMap);
//												}
//											else
//												{
//													paramsMap = new HashMap();
//													nodeMap = new HashMap();
//													nodeMap.put(key2, value);
//													paramsMap.put(key1, nodeMap);
//												}
//											deviceParam.put("WLAN", paramsMap);
//										}
//								}// end while
//							ArrayList list = new ArrayList();
//							if (null == deviceParam.get("WLAN"))
//								{
//									paramsMap = new HashMap();
//								}
//							else
//								{
//									paramsMap = (HashMap) deviceParam.get("WLAN");
//								}
//							list.add(wlanParamMap);
//							list.add(paramsMap);
//							deviceParam.put("WLAN", list);
//						}
//		// clear
//		dataMap = null;
//		paramArrayMap = null;
//		paramList = null;
//		logger.debug("end getDeviceParam type:" + type + "      "
//				+ new DateTimeUtil().getLongDate());
//		return deviceParam;
//	}
//	public ArrayList getParam(int type, HttpServletRequest request, Map paramArrayMap)
//	{
//		ArrayList paramPreLst = new ArrayList();
//		if (1 == type)
//			{
//				HashMap paras = new HashMap();
//				paras.put(DLSParam, "1");
//				ArrayList tempList = getMultiPara(paras, request);
//				HashMap dlsParam = new HashMap();
//				if (paramArrayMap.get("DLS") != null)
//					{
//						dlsParam = (HashMap) paramArrayMap.get("DLS");
//					}
//				String key = "";
//				String paramName = "";
//				for (int i = 0; i < tempList.size(); i++)
//					{
//						paramName = (String) tempList.get(i);
//						// 参数不是以.结尾的
//						if (paramName.lastIndexOf(".") != paramName.length() - 1)
//							{
//								key = paramName.substring(paramName.lastIndexOf(".") + 1);
//							}
//						if (dlsParam.containsKey(key))
//							{
//								paramPreLst.add(paramName);
//							}
//					}
//				// clear
//				paras = null;
//			}
//		if (2 == type)
//			{
//				logger.debug("enter into type:2");
//				HashMap paras = new HashMap();
//				paras.put(PPOEAndIPAndATMParam, "1");
//				String paramName = "";
//				ArrayList tempList = getMultiPara(paras, request);
//				logger.debug(" first tempList_size:" + tempList.size());
//				paras.clear();
//				// 获取InternetGatewayDevice.WANDevice.1.WANConnectionDevice.i.节点
//				for (int i = 0; i < tempList.size(); i++)
//					{
//						paramName = (String) tempList.get(i);
//						// paras.put(paramName+"WANPPPConnection.","0");
//						paras.put(paramName + "WANDSLLinkConfig.", "0");
//					}
//				tempList.clear();
//				/**
//				 * 获取InternetGatewayDevice.WANDevice.1.WANConnectionDevice.i.WANPPPConnection.
//				 * 获取InternetGatewayDevice.WANDevice.1.WANConnectionDevice.i.WANDSLLinkConfig.节点
//				 */
//				tempList = getMultiPara(paras, request);
//				logger.debug("second tempList_size:" + tempList.size());
//				HashMap atmParam = new HashMap();
//				if (null != paramArrayMap.get("ATM"))
//					{
//						atmParam = (HashMap) paramArrayMap.get("ATM");
//					}
//				HashMap PPPOEParam = new HashMap();
//				if (null != paramArrayMap.get("PPPOE"))
//					{
//						PPPOEParam = (HashMap) paramArrayMap.get("PPPOE");
//					}
//				HashMap IPParam = new HashMap();
//				if (null != paramArrayMap.get("IP"))
//					{
//						IPParam = (HashMap) paramArrayMap.get("IP");
//					}
//				String key = "";
//				for (int i = 0; i < tempList.size(); i++)
//					{
//						paramName = (String) tempList.get(i);
//						// atm
//						if (paramName.indexOf(".WANDSLLinkConfig.") != -1)
//							{
//								// 参数不是以.结尾的
//								if (paramName.lastIndexOf(".") != paramName.length() - 1)
//									{
//										key = paramName.substring(paramName
//												.lastIndexOf(".") + 1);
//										if (atmParam.containsKey(key))
//											{
//												paramPreLst.add(paramName);
//											}
//									}
//							}
//						// PPOE、IP
//						if (paramName.indexOf(".WANPPPConnection.") != -1)
//							{
//								// 参数不是以.结尾的
//								if (paramName.lastIndexOf(".") != paramName.length() - 1)
//									{
//										key = paramName.substring(paramName
//												.lastIndexOf(".") + 1);
//										if (PPPOEParam.containsKey(key))
//											{
//												paramPreLst.add(paramName);
//											}
//										if (IPParam.containsKey(key))
//											{
//												paramPreLst.add(paramName);
//											}
//									}
//							}
//					}
//				// clear
//				paras = null;
//				tempList = null;
//			}
//		if (3 == type)
//			{
//				HashMap paras = new HashMap();
//				paras.put(LANParam, "0");
//				ArrayList tempList = getMultiPara(paras, request);
//				HashMap lanParam = new HashMap();
//				if (paramArrayMap.get("LAN") != null)
//					{
//						lanParam = (HashMap) paramArrayMap.get("LAN");
//					}
//				String key = "";
//				String paramName = "";
//				for (int i = 0; i < tempList.size(); i++)
//					{
//						paramName = (String) tempList.get(i);
//						// 参数不是以.结尾的
//						if (paramName.lastIndexOf(".") != paramName.length() - 1)
//							{
//								key = paramName.substring(paramName.lastIndexOf(".") + 1);
//							}
//						if (lanParam.containsKey(key))
//							{
//								paramPreLst.add(paramName);
//							}
//					}
//				// clear
//				paras = null;
//			}
//		if (4 == type)
//			{
//				HashMap paras = new HashMap();
//				paras.put(WLANParam, "0");
//				ArrayList tempList = getMultiPara(paras, request);
//				HashMap wlanParam = new HashMap();
//				if (paramArrayMap.get("WLAN") != null)
//					{
//						wlanParam = (HashMap) paramArrayMap.get("WLAN");
//					}
//				String key = "";
//				String paramName = "";
//				for (int i = 0; i < tempList.size(); i++)
//					{
//						paramName = (String) tempList.get(i);
//						// 参数不是以.结尾的
//						if (paramName.lastIndexOf(".") != paramName.length() - 1)
//							{
//								key = paramName.substring(paramName.lastIndexOf(".") + 1);
//							}
//						if (wlanParam.containsKey(key))
//							{
//								paramPreLst.add(paramName);
//							}
//					}
//				// clear
//				paras = null;
//			}
//		logger.debug("paramPreLst_size:" + paramPreLst.size());
//		for (int i = 0; i < paramPreLst.size(); i++)
//			{
//				logger.debug("getParam:" + paramPreLst.get(i));
//			}
//		return paramPreLst;
//	}
//	/**
//	 * 采集点
//	 */
//	private String gather_id = null;
//	public String getGather_id()
//	{
//		return gather_id;
//	}
//	private void setGather_id(String gather_id)
//	{
//		this.gather_id = gather_id;
//	}
//	/**
//	 * 从session中user得到ior
//	 * 
//	 * @param request
//	 * @return
//	 */
//	public String getIor(HttpServletRequest request)
//	{
//		HttpSession session = request.getSession();
//		UserRes curUser = (UserRes) session.getAttribute("curUser");
//		User user = curUser.getUser();
//		String object_name = "ACS_" + gather_id;
//		String object_Poaname = "ACS_Poa_" + gather_id;
//		String strIor = user.getIor(object_name, object_Poaname);
//		logger.debug("getIor_object_name:" + object_name + "     object_Poaname:"
//				+ object_Poaname + "   ior:" + strIor);
//		return strIor;
//	}
//	public ArrayList getMultiPara(HashMap paras, HttpServletRequest request)
//	{
//		logger.debug("getMultiPara_size:" + paras.size());
//		ArrayList paraList = new ArrayList();
//		String device_id = request.getParameter("device_id");
//		ArrayList getParameterList = new ArrayList();
//		Iterator it = paras.keySet().iterator();
//		String paramName = "";
//		String doeshavechild = "";
//		while (it.hasNext())
//			{
//				paramName = (String) it.next();
//				doeshavechild = (String) paras.get(paramName);
//				// 设置参数
//				GetParameterNames getParameterNames = new GetParameterNames();
//				getParameterNames.setParameterPath(paramName);
//				getParameterNames.setNextLevel(Integer.parseInt(doeshavechild));
//				getParameterList.add(getParameterNames);
//			}
//		// logger.debug("getMultiPara_size:"+getParameterList.size());
//		for (int i = 0; i < getParameterList.size(); i++)
//			{
//				logger.debug("getMultiPara_param:"
//						+ ((GetParameterNames) getParameterList.get(i))
//								.getParameterPath());
//			}
//		DevRPC[] devRPCArr = getMultiParamDevPRCArr(device_id, getParameterList);
//		paraList.clear();
//		if (devRPCArr == null)
//			{
//				logger.debug("getMultiParamDevPRCArr fail!");
//				return paraList;
//			}
//		DevRPCRep[] devRPCRep = this.getDevRPCResponse(getIor(request), devRPCArr);
//		if (devRPCRep == null || null == devRPCRep[0].rpcArr
//				|| 0 == devRPCRep[0].rpcArr.length)
//			{
//				logger.debug("getDevRPCResponse fail!");
//				return paraList;
//			}
//		for (int i = 0; i < devRPCRep[0].rpcArr.length; i++)
//			{
//				String setRes = devRPCRep[0].rpcArr[i];
//				GetParameterNamesResponse getParameterNamesResponse = new GetParameterNamesResponse();
//				// 把SOAP形式的文件转换成标准的XML,便于通信
//				SoapOBJ soapOBJ = XML.getSoabOBJ(XML.CreateXML(setRes));
//		        if (soapOBJ == null) {
//		        	continue;
//		        }
//				getParameterNamesResponse = XmlToRpc.GetParameterNamesResponse(soapOBJ.getRpcElement());
//				// 通过这个XML对象,获取参数列表
//				if (null != getParameterNamesResponse)
//					{
//						ParameterInfoStruct[] pisArr = getParameterNamesResponse
//								.getParameterList();
//						if (null != pisArr)
//							{
//								String name = null;
//								for (int j = 0; j < pisArr.length; j++)
//									{
//										name = pisArr[j].getName();
//										paraList.add(name);
//									}
//							}
//					}
//				else
//					{
//						logger.debug("devRPCRep[0].rpcArr[" + i + "] is  null!");
//					}
//			}
//		// clear
//		getParameterList = null;
//		devRPCRep = null;
//		return paraList;
//	}
//	public HashMap getParas(String paraV, HttpServletRequest request, String doeshavechild)
//	{
//		// 接收传递过来的参数
//		String device_id = request.getParameter("device_id");
//		/*
//		 * String paraV = request.getParameter("paraV"); String doeshavechild =
//		 * request.getParameter("doeshavechild"); if (doeshavechild == null) {
//		 * doeshavechild = "1"; }
//		 */
//		// 定义一个返回的MAP
//		HashMap paraMap = new HashMap();
//		paraMap.clear();
//		GetParameterNames getParameterNames = new GetParameterNames();
//		getParameterNames.setParameterPath(paraV);
//		getParameterNames.setNextLevel(Integer.parseInt(doeshavechild));
//		DevRPC[] devRPCArr = this.getDevRPCArr(device_id, getParameterNames);
//		DevRPCRep[] devRPCRep = this.getDevRPCResponse(getIor(request), devRPCArr);
//		// 一个设备返回的命令
//		String setRes = devRPCRep[0].rpcArr[0];
//		GetParameterNamesResponse getParameterNamesResponse = new GetParameterNamesResponse();
//		// 把SOAP形式的文件转换成标准的XML,便于通信
//		SoapOBJ soapOBJ = XML.getSoabOBJ(XML.CreateXML(setRes));
//        if (soapOBJ == null) {
//        	return paraMap;
//        }
//		getParameterNamesResponse = XmlToRpc.GetParameterNamesResponse(soapOBJ.getRpcElement());
//		// 通过这个XML对象,获取参数列表
//		if (null != getParameterNamesResponse)
//			{
//				ParameterInfoStruct[] pisArr = getParameterNamesResponse
//						.getParameterList();
//				if (null != pisArr)
//					{
//						String name = null;
//						for (int i = 0; i < pisArr.length; i++)
//							{
//								name = pisArr[i].getName();
//								String writable = pisArr[i].getWritable();
//								paraMap.put(i + "", name + "," + writable);
//							}
//					}
//			}
//		return paraMap;
//	}
	/**
	 * 返回参数名和是否可写
	 * 
	 * @param request
	 * @return
	 */
	public HashMap getParaMap(HttpServletRequest request)
	{
		// 接收传递过来的参数
		String device_id = request.getParameter("device_id");
		String paraV = request.getParameter("paraV");
		String doeshavechild = request.getParameter("doeshavechild");
		if (doeshavechild == null)
			{
				doeshavechild = "1";
			}
		// 定义一个返回的MAP
		HashMap paraMap = new HashMap();
		paraMap.clear();
		GetParameterNames getParameterNames = new GetParameterNames();
		getParameterNames.setParameterPath(paraV);
		getParameterNames.setNextLevel(Integer.parseInt(doeshavechild));
		DevRpc[] devRPCArr = this.getDevRPCArr(device_id, getParameterNames);
		this.setGw_type(request.getParameter("gwType"));
		List<DevRpcCmdOBJ> devRpcCmdOBJList = this.getDevRPCResponse(devRPCArr,Global.RpcCmd_Type);
		SoapOBJ soapOBJ = null;
		List<String> pisList = null;
		ParameterInfoStruct[] pisArr = null;
		if (devRpcCmdOBJList == null || devRpcCmdOBJList.size() == 0)
		{
			logger.warn("[{}]List<DevRpcCmdOBJ>返回为空！", device_id);
			return paraMap;
		}
		DevRpcCmdOBJ devRpcCmdOBJ = devRpcCmdOBJList.get(0);
		if (devRpcCmdOBJ == null)
		{
			logger.warn("[{}]DevRpcCmdOBJ返回为空！", device_id);
			return paraMap;
		}
		// 多条命令
		ArrayList<com.ailk.tr069.devrpc.obj.mq.Rpc> rpcCmdObjList = devRpcCmdOBJ
				.getRpcList();
		if (rpcCmdObjList == null || rpcCmdObjList.size() == 0)
		{
			logger.warn("[{}]List<ACSRpcCmdOBJ>返回为空！", device_id);
			return paraMap;
		}
		GetParameterNamesResponse[] getParameterNamesResponseArr = new GetParameterNamesResponse[rpcCmdObjList
				.size()];
		for (int j = 0; j < rpcCmdObjList.size(); j++)
		{
			com.ailk.tr069.devrpc.obj.mq.Rpc acsRpcCmdObj = rpcCmdObjList.get(j);
			if ("GetParameterNamesResponse".equals(acsRpcCmdObj.getRpcName()))
			{
				String resp = acsRpcCmdObj.getValue();
				Fault fault = null;
				if (resp == null || "".equals(resp))
				{
					logger.debug("DevRpcCmdOBJ.value == null");
				}
				else
				{
					soapOBJ = XML.getSoabOBJ(XML.CreateXML(resp));
					if (soapOBJ != null)
					{
						fault = XmlToRpc.Fault(soapOBJ.getRpcElement());
						if (fault != null)
						{
							logger.warn("setValue({})={}", device_id, fault.getDetail()
									.getFaultString());
						}
						else
						{
							getParameterNamesResponseArr[j] = XmlToRpc
									.GetParameterNamesResponse(soapOBJ.getRpcElement());
						}
					}
				}
			}
		}
		for (int k = 0; k < getParameterNamesResponseArr.length; k++)
		{
			pisList = new ArrayList<String>();
			if (null == getParameterNamesResponseArr[k])
			{
				logger.warn("getParameterNamesResponseArr[k]为空");
			}
			else
			{
				pisArr = getParameterNamesResponseArr[k].getParameterList();
				if (null != pisArr)
				{
					String name = null;
					for (int i = 0; i < pisArr.length; i++)
					{
						name = pisArr[i].getName();
						String writable = pisArr[i].getWritable();
						paraMap.put(i + "", name + "," + writable);
					}
				}
			}
		}
		// 一个设备返回的命令
		return paraMap;
	}
//	/**
//	 * 
//	 * @param paramPre
//	 * @param paramEnd
//	 * @param type
//	 *            1:PPPOE,2:IP,3:LAN端口状态数据
//	 * @param ior
//	 * @param device_id
//	 * @param gather_id
//	 * @return
//	 */
//	public ArrayList getParaNames(int type, String ior, String device_id, String gather_id)
//	{
//		logger.debug("begin getParaNames  " + new DateTimeUtil().getLongDate()
//				+ "  type:" + type);
//		ArrayList parasList = new ArrayList(10);
//		String paramPre = "";
//		if (1 == type || 2 == type)
//			{
//				paramPre = "InternetGatewayDevice.WANDevice.1.WANConnectionDevice.";
//			}
//		else
//			if (3 == type)
//				{
//					paramPre = "InternetGatewayDevice.LANDevice.1.LANEthernetInterfaceConfig.";
//				}
//		ParamTreeObject treeObject = new ParamTreeObject();
//		Map childMap = treeObject.getParaMap(paramPre, ior, device_id, gather_id);
//		logger.debug("getParaNames_childMap_size:" + childMap.size());
//		Map grandMap = null;
//		if (null == childMap || childMap.size() == 0)
//			{
//				return parasList;
//			}
//		String value = "";
//		String paramName = "";
//		// 遍历它的子节点
//		for (int i = 0; i < childMap.size(); i++)
//			{
//				value = (String) childMap.get(i + "");
//				paramName = value.split(",")[0];
//				// 遍历InternetGatewayDevice.WANDevice.{i}.WAN-ConnectionDevice.{i}.下的节点
//				if (1 == type)
//					{
//						paramName += "WANPPPConnection.";
//						grandMap = treeObject.getParaMap(paramName, ior, device_id,
//								gather_id);
//						System.out
//								.println(i + "   paramName:" + paramName
//										+ "        getParaNames_grandMap_size:"
//										+ grandMap.size());
//						// 没有孙节点，则继续遍历其它子节点
//						if (null == grandMap || 0 == grandMap.size())
//							{
//								continue;
//							}
//						for (int j = 0; j < grandMap.size(); j++)
//							{
//								value = (String) grandMap.get(j + "");
//								logger.debug("grandMap_value:" + value);
//								paramName = value.split(",")[0];
//								parasList.add(paramName + "ConnectionStatus");
//								parasList.add(paramName + "Username");
//								// parasList.add(paramName+"Password");
//							}
//					}
//				else
//					if (2 == type)
//						{
//							paramName += "WANIPConnection.";
//							grandMap = treeObject.getParaMap(paramName, ior, device_id,
//									gather_id);
//							// 没有孙节点，则继续遍历其它子节点
//							if (null == grandMap || 0 == grandMap.size())
//								{
//									continue;
//								}
//							for (int j = 0; j < grandMap.size(); j++)
//								{
//									value = (String) grandMap.get(j + "");
//									paramName = value.split(",")[0];
//									parasList.add(paramName + "ExternalIPAddress");
//									parasList.add(paramName + "SubnetMask");
//									parasList.add(paramName + "DefaultGateway");
//									parasList.add(paramName + "DNSServers");
//								}
//						}
//					else
//						if (3 == type)
//							{
//								parasList.add(paramName + "Stats.PacketsReceived");
//								parasList.add(paramName + "Stats.PacketsSent");
//								parasList.add(paramName + "Stats.BytesReceived");
//								parasList.add(paramName + "Stats.BytesSent");
//								parasList.add(paramName + "MACAddress");
//								parasList.add(paramName + "Status");
//								parasList.add(paramName + "Enable");
//							}
//			}
//		// 清除
//		grandMap = null;
//		childMap = null;
//		logger.debug("end getParaNames  " + new DateTimeUtil().getLongDate()
//				+ "  type:" + type + "   size:" + parasList.size());
//		return parasList;
//	}
//	/**
//	 * 获取PPPOE、IP的数据
//	 * 
//	 * @param type
//	 *            1:PPPOE,2:IP,3:Lan端口状态数据
//	 * @param request
//	 * @return
//	 */
//	public Map getMuTiParamValue1(int type, HttpServletRequest request)
//	{
//		logger.debug("begin getMuTiParamValue  " + new DateTimeUtil().getLongDate()
//				+ " type:" + type);
//		Map mapValue = new HashMap();
//		String device_id = request.getParameter("device_id");
//		if (null == device_id)
//			{
//				return mapValue;
//			}
//		// 获取采集点
//		String gather_id = getGather_id();
//		if (null == gather_id)
//			{
//				gather_id = getGatherIDByDeviceID(device_id);
//				if (!"".equals(gather_id))
//					{
//						setGather_id(gather_id);
//					}
//				else
//					{
//						return mapValue;
//					}
//			}
//		// 获取ior
//		String ior = getIor(request);
//		logger.debug("device_id:" + device_id + "   gather_id:" + gather_id
//				+ "   ior:" + ior);
//		// 获取PPOE下面的所有参数名字
//		ArrayList paramNames = getParaNames(type, ior, device_id, gather_id);
//		logger.debug("paramNames_size:" + paramNames.size());
//		// 获取这些参数对应的值
//		Map paramValues = getParasValue(request, paramNames);
//		logger.debug("paramValues_size:" + paramValues.size());
//		Iterator it = paramValues.keySet().iterator();
//		String key = "";
//		String value = "";
//		String key1 = "";
//		String key2 = "";
//		HashMap nodeValue = null;
//		// 遍历返回的数据,进行分类
//		while (it.hasNext())
//			{
//				key = (String) it.next();
//				value = (String) paramValues.get(key);
//				if (1 == type || 2 == type)
//					{
//						key1 = key.substring(0, key.lastIndexOf("."));
//						key2 = key.substring(key.lastIndexOf(".") + 1, key.length());
//					}
//				else
//					if (3 == type)
//						{
//							if (-1 != key.indexOf(".Stats."))
//								{
//									key1 = key.substring(0, key.lastIndexOf(".Stats."));
//									key2 = key.substring(key.lastIndexOf(".Stats.") + 1,
//											key.length());
//								}
//							else
//								{
//									key1 = key.substring(0, key.lastIndexOf("."));
//									key2 = key.substring(key.lastIndexOf(".") + 1, key
//											.length());
//								}
//						}
//				// 看容器中是否已包含了这个节点的信息
//				if (mapValue.containsKey(key1))
//					{
//						nodeValue = (HashMap) mapValue.get(key1);
//					}
//				else
//					{
//						nodeValue = new HashMap(2);
//					}
//				nodeValue.put(key2, value);
//				mapValue.put(key1, nodeValue);
//			}
//		logger.debug("end getMuTiParamValue  " + new DateTimeUtil().getLongDate()
//				+ " type:" + type + "    size:" + mapValue.size());
//		return mapValue;
//	}
//	/**
//	 * 
//	 * @param type
//	 * @param request
//	 * @return
//	 */
//	public Map getPPOEAndIPParamValue(HttpServletRequest request)
//	{
//		logger.debug("begin getPPOEAndIPParamValue  "
//				+ new DateTimeUtil().getLongDate());
//		HashMap PPOEMap = new HashMap(10);
//		HashMap IPMap = new HashMap(10);
//		HashMap ATMMap = new HashMap(10);
//		Map mapValue = new HashMap();
//		mapValue.put("PPOE", PPOEMap);
//		mapValue.put("IP", IPMap);
//		mapValue.put("ATM", ATMMap);
//		ArrayList paramNames = new ArrayList();
//		String paramName = "InternetGatewayDevice.WANDevice.1.WANConnectionDevice.";
//		paramNames.add(paramName);
//		// 获取这些参数对应的值
//		Map paramValues = new HashMap();
//		paramValues = getParasValue(request, paramNames);
//		logger.debug("paramValues_size:" + paramValues.size());
//		Iterator it = paramValues.keySet().iterator();
//		String key = "";
//		String value = "";
//		String key1 = "";
//		String key2 = "";
//		HashMap nodeValue = null;
//		// 遍历返回的数据,进行分类
//		while (it.hasNext())
//			{
//				key = (String) it.next();
//				value = (String) paramValues.get(key);
//				// PPOE连接
//				if (-1 != key.indexOf(".WANPPPConnection."))
//					{
//						key1 = key.substring(0, key.lastIndexOf("."));
//						key2 = key.substring(key.lastIndexOf(".") + 1, key.length());
//						if ("Username".equals(key2) || "ConnectionStatus".equals(key2)
//								|| "ExternalIPAddress".equals(key2)
//								|| "DNSServers".equals(key2)
//								|| ".MACAddress".equals(key2))
//							{
//								// 是否存在PPOE的数据
//								if (mapValue.containsKey("PPOE"))
//									{
//										PPOEMap = (HashMap) mapValue.get("PPOE");
//										// 是否已包含这个节点的数据
//										if (PPOEMap.containsKey(key1))
//											{
//												nodeValue = (HashMap) PPOEMap.get(key1);
//											}
//										else
//											{
//												nodeValue = new HashMap(10);
//											}
//										nodeValue.put(key2, value);
//										PPOEMap.put(key1, nodeValue);
//									}
//								else
//									{
//										PPOEMap = new HashMap(10);
//										nodeValue = new HashMap(10);
//										nodeValue.put(key2, value);
//										PPOEMap.put(key1, nodeValue);
//									}
//								mapValue.put("PPOE", PPOEMap);
//							}
//					}
//				else
//					if (-1 != key.indexOf(".WANIPConnection."))
//						{
//							key1 = key.substring(0, key.lastIndexOf("."));
//							key2 = key.substring(key.lastIndexOf(".") + 1, key.length());
//							if ("ExternalIPAddress".equals(key2)
//									|| "SubnetMask".equals(key2)
//									|| "DefaultGateway".equals(key2)
//									|| "DNSServers".equals(key2))
//								{
//									// 是否存在IP数据
//									if (mapValue.containsKey("IP"))
//										{
//											IPMap = (HashMap) mapValue.get("IP");
//											// 是否存在这个节点的数据
//											if (IPMap.containsKey(key1))
//												{
//													nodeValue = (HashMap) IPMap.get(key1);
//												}
//											else
//												{
//													nodeValue = new HashMap(10);
//												}
//											nodeValue.put(key2, value);
//											IPMap.put(key1, nodeValue);
//										}
//									else
//										{
//											IPMap = new HashMap(10);
//											nodeValue = new HashMap(10);
//											nodeValue.put(key2, value);
//											IPMap.put(key1, nodeValue);
//										}
//									mapValue.put("IP", IPMap);
//								}
//						}
//					else
//						if (-1 != key.indexOf(".WANDSLLinkConfig."))
//							{
//								key1 = key.substring(0, key.lastIndexOf("."));
//								key2 = key.substring(key.lastIndexOf(".") + 1, key
//										.length());
//								if ("ATMEncapsulation".equals(key2)
//										|| "ATMTransmittedBlocks".equals(key2)
//										|| "ATMReceivedBlocks".equals(key2)
//										|| "LinkStatus".equals(key2)
//										|| "LinkType".equals(key2))
//									{
//										if (mapValue.containsKey("ATM"))
//											{
//												ATMMap = (HashMap) mapValue.get("ATM");
//												// 是否存在这个节点的数据
//												if (ATMMap.containsKey(key1))
//													{
//														nodeValue = (HashMap) ATMMap
//																.get(key1);
//													}
//												else
//													{
//														nodeValue = new HashMap(10);
//													}
//												nodeValue.put(key2, value);
//												ATMMap.put(key1, nodeValue);
//											}
//										else
//											{
//												ATMMap = new HashMap(10);
//												nodeValue = new HashMap(10);
//												nodeValue.put(key2, value);
//												ATMMap.put(key1, nodeValue);
//											}
//									}
//							}
//			}
//		logger.debug("end getPPOEAndIPParamValue  "
//				+ new DateTimeUtil().getLongDate() + "    PPOE_size:"
//				+ ((HashMap) mapValue.get("PPOE")).size() + "   IP_size:"
//				+ ((HashMap) mapValue.get("IP")).size());
//		return mapValue;
//	}
//	/**
//	 * 
//	 * @param request
//	 * @return
//	 */
//	public Map getLanParamValue(HttpServletRequest request)
//	{
//		logger.debug("begin getLanParamValue  " + new DateTimeUtil().getLongDate());
//		Map mapValue = new HashMap();
//		ArrayList paramNames = new ArrayList();
//		String paramName = "InternetGatewayDevice.LANDevice.1.LANEthernetInterfaceConfig.";
//		paramNames.add(paramName);
//		// 获取这些参数对应的值
//		Map paramValues = new HashMap();
//		paramValues = getParasValue(request, paramNames);
//		logger.debug("getLanParamValue_paramValues_size:" + paramValues.size());
//		Iterator it = paramValues.keySet().iterator();
//		String key = "";
//		String value = "";
//		String key1 = "";
//		String key2 = "";
//		HashMap nodeValue = null;
//		boolean exist = false;
//		while (it.hasNext())
//			{
//				exist = false;
//				key = (String) it.next();
//				value = (String) paramValues.get(key);
//				if (-1 != key.indexOf(".Stats."))
//					{
//						key1 = key.substring(0, key.lastIndexOf(".Stats."));
//						key2 = key
//								.substring(key.lastIndexOf(".Stats.") + 1, key.length());
//						exist = true;
//					}
//				else
//					if (-1 != key.indexOf(".MACAddress") || -1 != key.indexOf("Status")
//							|| -1 != key.indexOf("Enable"))
//						{
//							key1 = key.substring(0, key.lastIndexOf("."));
//							key2 = key.substring(key.lastIndexOf(".") + 1, key.length());
//							exist = true;
//						}
//				if (exist
//						&& ("Stats.PacketsReceived".equals(key2)
//								|| "Stats.PacketsSent".equals(key2)
//								|| "Stats.BytesReceived".equals(key2)
//								|| "Stats.BytesSent".equals(key2)
//								|| "MACAddress".equals(key2) || "Status".equals(key2) || "Enable"
//								.equals(key2)))
//					{
//						// 已存在这个节点的数据
//						if (mapValue.containsKey(key1))
//							{
//								nodeValue = (HashMap) mapValue.get(key1);
//							}
//						else
//							{
//								nodeValue = new HashMap(10);
//							}
//						nodeValue.put(key2, value);
//						mapValue.put(key1, nodeValue);
//					}
//			}
//		logger.debug("end getLanParamValue  " + new DateTimeUtil().getLongDate()
//				+ "   size:" + mapValue.size());
//		return mapValue;
//	}
//	/**
//	 * 
//	 * @param request
//	 * @return
//	 */
//	public Map getWLANParamValue(HttpServletRequest request)
//	{
//		logger.debug("begin getWLANParamValue  " + new DateTimeUtil().getLongDate());
//		Map mapValue = new HashMap();
//		ArrayList paramNames = new ArrayList();
//		String paramName = "InternetGatewayDevice.LANDevice.1.WLANConfiguration.";
//		paramNames.add(paramName);
//		// 获取这些参数对应的值
//		Map paramValues = new HashMap();
//		paramValues = getParasValue(request, paramNames);
//		logger.debug("getWLANParamValue_paramValues_size:" + paramValues.size());
//		Iterator it = paramValues.keySet().iterator();
//		String key = "";
//		String value = "";
//		String key1 = "";
//		String key2 = "";
//		HashMap nodeValue = null;
//		while (it.hasNext())
//			{
//				key = (String) it.next();
//				value = (String) paramValues.get(key);
//				if (key.length() > paramName.length())
//					{
//						key1 = key.substring(0, key.lastIndexOf("."));
//						key2 = key.substring(key.lastIndexOf(".") + 1, key.length());
//						if ("Enable".equals(key2) || "Status".equals(key2)
//								|| "TotalBytesSent".equals(key2)
//								|| "TotalBytesReceived".equals(key2)
//								|| "TotalPacketsSent".equals(key2)
//								|| "TotalPacketsReceived".equals(key2))
//							{
//								// 已存在这个节点的数据
//								if (mapValue.containsKey(key1))
//									{
//										nodeValue = (HashMap) mapValue.get(key1);
//									}
//								else
//									{
//										nodeValue = new HashMap(10);
//									}
//								nodeValue.put(key2, value);
//								mapValue.put(key1, nodeValue);
//							}
//					}
//			}
//		logger.debug("end getWLANParamValue  " + new DateTimeUtil().getLongDate()
//				+ "   size:" + mapValue.size());
//		return mapValue;
//	}
//	/**
//	 * 获取设备的采集点
//	 * 
//	 * @param device_id
//	 * @return
//	 */
//	private String getGatherIDByDeviceID(String device_id)
//	{
//		String gather_id = "";
//		String sql = "select gather_id from tab_gw_device where device_id=?";
//		PrepareSQL pSQL = new PrepareSQL();
//		pSQL.setSQL(sql);
//		pSQL.setString(1, device_id);
//		logger.debug("getGatherIDByDeviceID_SQL:" + pSQL.getSQL());
//		Map record = DataSetBean.getRecord(pSQL.getSQL());
//		if (null != record)
//			{
//				gather_id = (String) record.get("gather_id");
//			}
//		return gather_id;
//	}
//	/**
//	 * 查询多个参数对应的值
//	 * 
//	 * @param request
//	 * @param params
//	 * @return
//	 */
//	public Map getParasValue(HttpServletRequest request, ArrayList params)
//	{
//		logger.debug("begin getParasValue  " + new DateTimeUtil().getLongDate()
//				+ " params_size:" + params.size());
//		Map mapValue = new HashMap();
//		try
//			{
//				String device_id = request.getParameter("device_id");
//				if (null == device_id || null == params || 0 == params.size())
//					{
//						return mapValue;
//					}
//				String[] paramArray = new String[params.size()];
//				params.toArray(paramArray);
//				for (int i = 0; i < paramArray.length; i++)
//					{
//						logger.debug("paramArray[" + i + "]:" + paramArray[i]);
//					}
//				GetParameterValues getParameterValues = new GetParameterValues();
//				getParameterValues.setParameterNames(paramArray);
//				logger.debug("getParameterValues:" + getParameterValues);
//				DevRPC[] devRPCArr = getDevRPCArr(device_id, getParameterValues);
//				// logger.debug("end getDevRPCArr!");
//				// 为NULL，则直接返回
//				if (devRPCArr == null)
//					return mapValue;
//				// corba
//				DevRPCRep[] devRPCRep = this
//						.getDevRPCResponse(getIor(request), devRPCArr);
//				if (devRPCRep == null || 0 == devRPCRep.length
//						|| 0 == devRPCRep[0].rpcArr.length)
//					return mapValue;
//				// 一个设备返回的命令
//				String setRes = devRPCRep[0].rpcArr[0];
//				// logger.debug("setRes"+setRes);
//				GetParameterValuesResponse getParameterValuesResponse = new GetParameterValuesResponse();
//				// 把SOAP形式的文件转换成标准的XML,便于通信
//				SoapOBJ soapOBJ = XML.getSoabOBJ(XML.CreateXML(setRes));
//		        if (soapOBJ == null) {
//		        	return mapValue;
//		        }
//				getParameterValuesResponse = XmlToRpc.GetParameterValuesResponse(soapOBJ.getRpcElement());
//				// logger.debug("begin getParameterValuesResponse");
//				// 通过这个XML对象,获取参数列表
//				if (null != getParameterValuesResponse)
//					{
//						// logger.debug("begin
//						// getParameterValuesResponse.getParameterList");
//						ParameterValueStruct[] pisArr = getParameterValuesResponse
//								.getParameterList();
//						if (null != pisArr)
//							{
//								// logger.debug("getParasValue_value_size:"+pisArr.length);
//								String name = null;
//								String value = null;
//								for (int i = 0; i < pisArr.length; i++)
//									{
//										name = pisArr[i].getName();
//										value = pisArr[i].getValue().para_value;
//										mapValue.put(name, value);
//										logger.debug("wp:" + name + ":" + value);
//									}
//							}
//						pisArr = null;
//					}
//				// clear
//				getParameterValuesResponse = null;
//				devRPCRep = null;
//				logger.debug("end getParasValue" + new DateTimeUtil().getLongDate()
//						+ "      size:" + mapValue.size());
//			}
//		catch (Exception e)
//			{
//				e.printStackTrace();
//			}
//		return mapValue;
//	}
	/**
	 * 根据用户输入的参数具体查询值
	 * 
	 * @param request
	 * @return
	 */
	public Map getMoreParaValue(HttpServletRequest request)
	{
		Map mapValue = new HashMap();
		// 接收传递过来的参数
		String device_id = request.getParameter("device_id");
		String[] parameter = request.getParameterValues("paraV");
		this.setGw_type(request.getParameter("gwType"));
		if (parameter == null || device_id == null)
			return mapValue;
		GetParameterValues getParameterValues = new GetParameterValues();
		getParameterValues.setParameterNames(parameter);
		// getParameterValues.
		DevRpc[] devRPCArr = this.getDevRPCArr(device_id, getParameterValues);
		// 为NULL，则直接返回
		if (devRPCArr == null)
			return mapValue;
		List<DevRpcCmdOBJ> devRpcCmdOBJList = this.getDevRPCResponse(devRPCArr,Global.RpcCmd_Type);
		Element element = dealDevRPCResponse("GetParameterValuesResponse",
				devRpcCmdOBJList, device_id);
		if (element == null)
		{
			return mapValue;
		}
		GetParameterValuesResponse getParameterValuesResponse = XmlToRpc
				.GetParameterValuesResponse(element);
		if (getParameterValuesResponse != null)
		{
			ParameterValueStruct[] parameterValueStructArr = getParameterValuesResponse
					.getParameterList();
			for (int j = 0; j < parameterValueStructArr.length; j++)
			{
				mapValue.put(parameterValueStructArr[j].getName(),
						parameterValueStructArr[j].getValue().para_value);
			}
		}
		return mapValue;
	}
//	public DevRPC[] getMultiParamDevPRCArr(String device_id, ArrayList list)
//	{
//		logger.debug("begin getMultiParamDevPRCArr!");
//		DevRPC[] devRPCArr = new DevRPC[1];
//		String strSQL = "select *  from tab_gw_device where device_id='" + device_id
//				+ "'";
//		PrepareSQL psql = new PrepareSQL(strSQL);
//		psql.getSQL();
//		Cursor cursor = DataSetBean.getCursor(strSQL);
//		Map fields = cursor.getNext();
//		// 如果查询无设备，则直接返回NULL
//		if (fields == null)
//			{
//				logger.debug("deviceid is not exist!");
//				return null;
//			}
//		String oui = (String) fields.get("oui");
//		String SerialNumber = (String) fields.get("device_serialnumber");
//		String ip = (String) fields.get("loopback_ip");
//		String Port = (String) fields.get("cr_port");
//		String path = (String) fields.get("cr_path");
//		String username = (String) fields.get("acs_username");
//		String passwd = (String) fields.get("acs_passwd");
//		String gather_id = (String) fields.get("gather_id");
//		setGather_id(gather_id);
//		if (null == list || 0 == list.size())
//			{
//				logger.debug("paramList is null!");
//				return null;
//			}
//		devRPCArr[0] = new DevRPC();
//		devRPCArr[0].DeviceId = device_id;
//		devRPCArr[0].OUI = oui;
//		devRPCArr[0].SerialNumber = SerialNumber;
//		devRPCArr[0].ip = ip;
//		devRPCArr[0].port = Integer.parseInt(Port);
//		devRPCArr[0].path = path;
//		devRPCArr[0].username = username;
//		devRPCArr[0].passwd = passwd;
//		String[] stringArr = new String[list.size()];
//		for (int i = 0; i < list.size(); i++)
//			{
//				stringArr[i] = ((RPCObject) list.get(i)).toRPC();
//			}
//		devRPCArr[0].rpcArr = stringArr;
//		logger.debug("end getMultiParamDevPRCArr!");
//		return devRPCArr;
//	}
	/**
	 * 单台设备单条命令返回的RPC结果处理
	 * 
	 * @author wangsenbo
	 * @date Mar 22, 2011
	 * @param
	 * @return RPCObject
	 */
	private Element dealDevRPCResponse(String stringRpcName, List<DevRpcCmdOBJ> devRpcCmdOBJList,String deviceId) {
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
		if(stringRpcName==null){
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
				logger.debug("[{}]DevRpcCmdOBJ.value == null",deviceId);
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
	 * 根据device_id得到长度为1的DevRPC对象数组
	 *
	 * @author wangsenbo
	 * @date Apr 19, 2011
	 * @param 
	 * @return DevRpc[]
	 */
	public DevRpc[] getDevRPCArr(String device_id, RPCObject rpcObject) {
		DevRpc[] devRPCArr = new DevRpc[1];
		String stringRpcValue = "";
		String stringRpcName = "";
		if (rpcObject == null) {
			stringRpcValue = "";
			stringRpcName = "";
		} else {
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
	public List<DevRpcCmdOBJ> getDevRPCResponse(DevRpc[] devRPCArr,int rpcType) {
		logger.debug("getDevRPCResponse(devRPCArr)");

		if (devRPCArr == null) {
			logger.error("devRPCArr == null");
			return null;
		}

		List<DevRpcCmdOBJ> devRPCRep = null;
		DevRPCManager devRPCManager = new DevRPCManager(this.gw_type);
		devRPCRep = devRPCManager.execRPC(devRPCArr,rpcType);
		return devRPCRep;
	}
//	/**
//	 * 参数变化上报配置保存调用接口
//	 * 
//	 * @param request
//	 * @return 通知后台成功与否
//	 */
//	public boolean setParamChangeAct(HttpServletRequest request)
//	{
//		// 接收传递过来的参数
//		String device_id = request.getParameter("device_id");
//		// 参数
//		String param = request.getParameter("parameter");
//		// 是否通知
//		boolean isNotify = Boolean.valueOf(request.getParameter("isNotify"))
//				.booleanValue();
//		// 通知方式
//		String notifyWay = request.getParameter("NotifyWay");
//		if (notifyWay == null || notifyWay == "")
//			notifyWay = "0";
//		int intNotifyWay = Integer.parseInt(notifyWay);
//		// 属性名称
//		String[] attrName = request.getParameterValues("attrName");
//		// 属性是否通知 true / false
//		boolean attrChange = Boolean.valueOf(request.getParameter("attrChange"))
//				.booleanValue();
//		SetParameterAttributesStruct setParameterAttributesStruct = new SetParameterAttributesStruct();
//		setParameterAttributesStruct.setName(param);
//		setParameterAttributesStruct.setNotification(intNotifyWay);
//		setParameterAttributesStruct.setNotificationChange(isNotify);
//		setParameterAttributesStruct.setAccessListChange(attrChange);
//		setParameterAttributesStruct.setAccessList(attrName);
//		SetParameterAttributes setParameterAttributes = new SetParameterAttributes();
//		setParameterAttributes.setParameterList(new SetParameterAttributesStruct[]
//			{ setParameterAttributesStruct });
//		DevRPC[] devRPCArr = getDevRPCArr(device_id, setParameterAttributes);
//		if (devRPCArr == null)
//			return false;
//		DevRPCRep[] devRPCRep = getDevRPCResponse(this.getIor(request), devRPCArr);
//		if (devRPCRep == null)
//			return false;
//		// 调用后台后，响应处理逻辑
//		String setRes = devRPCRep[0].rpcArr[0];
//		SetParameterAttributesResponse setParameterAttributesResponse = new SetParameterAttributesResponse();
//		// 把SOAP形式的文件转换成标准的XML,便于通信
//		SoapOBJ soapOBJ = XML.getSoabOBJ(XML.CreateXML(setRes));
//        if (soapOBJ == null) {
//        	return false;
//        }
//		setParameterAttributesResponse = XmlToRpc.SetParameterAttributesResponse(soapOBJ.getRpcElement());
//		// setParameterAttributesResponse不为空时，才调用成功！
//		return setParameterAttributesResponse != null;
//	}
//	/**
//	 * 实时获取设备上多终端上网参数
//	 * 
//	 * @param request
//	 * @return 返回MAP-->key:os value:version值
//	 */
//	public Map getWBAND(HttpServletRequest request)
//	{
//		String rootPara = "InternetGatewayDevice.Services.X_CT-COM_MWBAND.";
//		String pathWBANDConn = null;
//		ParamTreeObject paramTreeObject = new ParamTreeObject();
//		Map paraValueMap_WBAND;
//		String device_id = request.getParameter("device_id");
//		// 开始调用corba接口
//		HttpSession session = request.getSession();
//		UserRes curUser = (UserRes) session.getAttribute("curUser");
//		User user = curUser.getUser();
//		// 根据设备 由数据库中查询获得采集点
//		DeviceAct act = new DeviceAct();
//		HashMap deviceInfo = act.getDeviceInfo(device_id);
//		String gather_id = (String) deviceInfo.get("gather_id");
//		// 取得IOR
//		String ior = user.getIor("ACS_" + gather_id, "ACS_Poa_" + gather_id);
//		// 取得WANConnectionDevice.下所有的子节点
//		Collection collection_1 = WANConnDeviceAct.getParameterNameList(device_id,
//				gather_id, rootPara, ior);
//		Iterator iterator_1 = collection_1.iterator();
//		if (collection_1.size() == 0)
//			{
//			}
//		while (iterator_1.hasNext())
//			{
//				pathWBANDConn = (String) iterator_1.next();
//				paraValueMap_WBAND = paramTreeObject.getParaValueMap(pathWBANDConn, ior,
//						device_id, gather_id);
//				return paraValueMap_WBAND;
//			}
//		return getTestMap();
//		// return null;
//	}
//	private Map getTestMap()
//	{
//		Map map = new HashMap();
//		
//		//是否限制同时接入公网的终端数量，取值范围(0，1，2)， 0：代表此业务不启用，1： 代表使用模式一，2：代表使 用模式二，缺省值：1
//		map.put("Mode", "");
//		
//		//模式一：同时接入公网的终端的最大数量，缺省值：4
//		map.put("TotalTerminalNumber", "");
//		
//		//模式二：是否限制同时接入公网的 STB终端数量，取值范围(true,false)， 缺省值： false
//		map.put("STBRestrictEnable", "");
//		
//		//同时接入公网的STB终端的最大数量 
//		map.put("STBNumber", "");
//		
//		//模式二：是否限制同时接入公网的 Camera 终端数量， 取值范围(true,false)，缺省值：false 
//		map.put("CameraRestrictEnable", "");
//		
//		//同时接入公网的 Camera 终端的最大数量
//		map.put("CameraNumber", "");
//		
//		//模式二：是否限制同时接入公网的 Computer终端数量，取值范围(true,false)，缺省值：false 
//		map.put("ComputerRestrictEnable", "");
//		
//		//同时接入公网的Computer终端的最大数量，缺省值：1
//		map.put("ComputerNumber", "");
//		
//		//模式二：是否限制同时接入公网的 Phone 终端数量，取值范围(true,false)，缺省值：false
//		map.put("PhoneRestrictEnable", "");
//		
//		//同时接入公网的 Phone终端的最大数量
//		map.put("PhoneNumber", "");
//		return map;
//	}
//	/**
//	 * 实时获取设备上已有的无线SSID
//	 * 
//	 * @param request
//	 * @return 返回MAP-->key:os value:version值
//	 */
//	public Map getSSID(HttpServletRequest request)
//	{
//		String rootPara = "InternetGatewayDevice.LANDevice.1.WLANConfiguration.";
//		String pathWLANConn = null;
//		ParamTreeObject paramTreeObject = new ParamTreeObject();
//		String paraValue_SSID = "";
//		Map paraValueMap_SSID;
//		String paraValue_isHidden = "";
//		Map paraValueMap_isHidden;
//		Map<String, String> WLANValue_Map = new HashMap<String, String>();
//		String device_id = request.getParameter("device_id");
//		// 开始调用corba接口
//		HttpSession session = request.getSession();
//		UserRes curUser = (UserRes) session.getAttribute("curUser");
//		User user = curUser.getUser();
//		// 根据设备 由数据库中查询获得采集点
//		DeviceAct act = new DeviceAct();
//		HashMap deviceInfo = act.getDeviceInfo(device_id);
//		String gather_id = (String) deviceInfo.get("gather_id");
//		String devicetype_id = (String) deviceInfo.get("devicetype_id");
//		// 取得IOR
//		String ior = user.getIor("ACS_" + gather_id, "ACS_Poa_" + gather_id);
//		// 取得WANConnectionDevice.下所有的子节点
//		Collection collection_1 = WANConnDeviceAct.getParameterNameList(device_id,
//				gather_id, rootPara, ior);
//		Iterator iterator_1 = collection_1.iterator();
//		if (collection_1.size() == 0)
//			{
//			}
//		while (iterator_1.hasNext())
//			{
//				// InternetGatewayDevice.LANDevice.1.WLANConfiguration.1.
//				pathWLANConn = (String) iterator_1.next();
//				// logger.debug("GSJ----------pathWLANConn:" +
//				// pathWLANConn);
//				paraValueMap_SSID = paramTreeObject.getParaValueMap(
//						pathWLANConn + "SSID", ior, device_id, gather_id);
//				paraValue_SSID = paramTreeObject.getParaVlue(paraValueMap_SSID);
//				// logger.debug("GSJ----------paraValue_SSID :" +
//				// paraValue_SSID);
//				paraValueMap_isHidden = paramTreeObject.getParaValueMap(pathWLANConn
//						+ "X_CT-COM_SSIDHide", ior, device_id, gather_id);
//				paraValue_isHidden = paramTreeObject.getParaVlue(paraValueMap_isHidden);
//				// logger.debug("GSJ----------paraValue_isHidden :" +
//				// paraValue_isHidden);
//				WLANValue_Map.put(paraValue_SSID, paraValue_isHidden);
//			}
//		return WLANValue_Map;
//	}
	
	
	
	
	public String getGw_type() {
		return gw_type;
	}
	public void setGw_type(String gw_type) {
		this.gw_type = gw_type;
	}
}