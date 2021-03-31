
package com.linkage.module.ids.bio;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ACS.DevRpc;
import ACS.Rpc;

import com.ailk.tr069.devrpc.obj.rpc.DevRpcCmdOBJ;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.acs.soap.io.XML;
import com.linkage.litms.acs.soap.io.XmlToRpc;
import com.linkage.litms.acs.soap.object.AnyObject;
import com.linkage.litms.acs.soap.object.ParameterValueStruct;
import com.linkage.litms.acs.soap.object.SoapOBJ;
import com.linkage.litms.acs.soap.service.GetParameterValues;
import com.linkage.litms.acs.soap.service.GetParameterValuesResponse;
import com.linkage.litms.acs.soap.service.SetParameterValues;
import com.linkage.litms.common.corba.DevRPCManager;
import com.linkage.module.gwms.Global;
import com.linkage.module.ids.dao.HttpQualityCheckDAO;

/**
 * @author yinlei3 (Ailk No.73167)
 * @version 1.0
 * @since 2016年4月27日
 * @category com.linkage.module.ids.bio
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class HttpQualityCheckBIO
{

	/** 日志记录 */
	private static Logger logger = LoggerFactory.getLogger(HttpQualityCheckBIO.class);
	/** dao */
	private HttpQualityCheckDAO dao;

	/**
	 * http下载拨测
	 * 
	 * @param downLoadUrl
	 *            测试用url
	 * @param userName
	 *            测试账户
	 * @param password
	 *            测试密码
	 * @param deviceSerialnumber
	 *            设备序列号
	 * @param oui
	 *            设备oui
	 * @return 拨测结果
	 */
	public Map<String, String> httpQualityCheck(String downLoadUrl, String userName,
			String password, String deviceSerialnumber, String oui)
	{
		Map<String, String> map = new HashMap<String, String>();
		// 获取设备Id
		// 根据设备序列号，厂商OUI检索设备
		Map<String, String> deviceInfoMap = dao.queryDevInfo(deviceSerialnumber, oui);
		// 设备不存在
		if (null == deviceInfoMap || deviceInfoMap.isEmpty())
		{
			map.put("message", "设备不存在");
			return map;
		}
		String device_id = StringUtil.getStringValue(deviceInfoMap, "device_id");
		// 查询设备是否在线
		// 测试连接 成功：1 失败： 0
		int int_flag = testConnection(device_id);
		String flag = "";
		if (int_flag != 1)
		{
			if (int_flag == 0)
			{
				flag = "发生未知连接错误！";
			}
			else if (int_flag == -1)
			{
				flag = "设备连接不上！";
			}
			else if (int_flag == -2)
			{
				flag = "设备参数为空！";
			}
			else if (int_flag == -3)
			{
				flag = "设备正被操作！";
			}
			else if (int_flag == -4)
			{
				flag = "未知错误原因！";
			}
			else
			{
				flag = "发生未知连接错误！";
			}
			map.put("message", flag);
			return map;
		}
		// 若连接成功, 给设备下发 http质量检测所需的节点参数
		map = downLoadByHTTP("1", device_id, downLoadUrl, userName, password,
				deviceSerialnumber);
		return map;
	}

	/**
	 * 测试设备是否在线
	 * 
	 * @param device_id
	 *            设备序列号
	 * @return 连接结果
	 */
	private int testConnection(String device_id)
	{
		// 得到设备类型
		String gw_type = LipossGlobals.getGw_Type(device_id);
		DevRpc[] devRPCArr = new DevRpc[1];
		devRPCArr[0] = new DevRpc();
		devRPCArr[0].devId = device_id;
		Rpc[] rpcArr = new Rpc[1];
		rpcArr[0] = new Rpc();
		rpcArr[0].rpcId = "1";
		rpcArr[0].rpcName = "";
		rpcArr[0].rpcValue = "";
		devRPCArr[0].rpcArr = rpcArr;
		// corba
		List<DevRpcCmdOBJ> devRPCRep = null;
		DevRPCManager devRPCManager = new DevRPCManager(gw_type);
		devRPCRep = devRPCManager.execRPC(devRPCArr, Global.RpcTest_Type);
		int flag = 0;
		if (devRPCRep == null || devRPCRep.size() == 0)
		{
			logger.warn("[{}]List<DevRpcCmdOBJ>返回为空！", device_id);
		}
		else if (devRPCRep.get(0) == null)
		{
			logger.warn("[{}]DevRpcCmdOBJ返回为空！", device_id);
		}
		else
		{
			flag = devRPCRep.get(0).getStat();
		}
		return flag;
	}

	/**
	 * http下载质量检测
	 * 
	 * @param gw_type
	 *            设备类型
	 * @param deviceId
	 *            设备Id
	 * @param url
	 *            下载url
	 * @param username
	 *            测试账号
	 * @param password
	 *            密码
	 * @return 检测结果
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Map<String, String> downLoadByHTTP(String gw_type, String deviceId,
			String url, String username, String password, String deviceSerialnumber)
	{
		DevRpc[] devRPCArr = new DevRpc[1];
		AnyObject anyObject = new AnyObject();
		SetParameterValues setParameterValues = new SetParameterValues();
		ParameterValueStruct[] ParameterValueStruct = new ParameterValueStruct[4];
		ParameterValueStruct[0] = new ParameterValueStruct();
		ParameterValueStruct[0]
				.setName("InternetGatewayDevice.DownloadDiagnostics.DiagnosticsState");
		anyObject.para_value = "Requested";
		anyObject.para_type_id = "1";
		ParameterValueStruct[0].setValue(anyObject);
		ParameterValueStruct[1] = new ParameterValueStruct();
		ParameterValueStruct[1]
				.setName("InternetGatewayDevice.DownloadDiagnostics.DownloadURL");
		anyObject = new AnyObject();
		anyObject.para_value = url;
		anyObject.para_type_id = "1";
		ParameterValueStruct[1].setValue(anyObject);
		ParameterValueStruct[2] = new ParameterValueStruct();
		ParameterValueStruct[2]
				.setName("InternetGatewayDevice.X_CT-COM_PPPOE_EMULATOR.Username");
		anyObject = new AnyObject();
		anyObject.para_value = username;
		anyObject.para_type_id = "1";
		ParameterValueStruct[2].setValue(anyObject);
		ParameterValueStruct[3] = new ParameterValueStruct();
		ParameterValueStruct[3]
				.setName("InternetGatewayDevice.X_CT-COM_PPPOE_EMULATOR.Password");
		anyObject = new AnyObject();
		anyObject.para_value = password;
		anyObject.para_type_id = "1";
		ParameterValueStruct[3].setValue(anyObject);
		setParameterValues.setParameterList(ParameterValueStruct);
		setParameterValues.setParameterKey("downLoad");
		GetParameterValues getParameterValues = new GetParameterValues();
		String[] parameterNamesArr = new String[4];
		parameterNamesArr[0] = "InternetGatewayDevice.DownloadDiagnostics.SampledValues";
		parameterNamesArr[1] = "InternetGatewayDevice.DownloadDiagnostics.SampledTotalValues";
		parameterNamesArr[2] = "InternetGatewayDevice.DownloadDiagnostics.BOMTime";
		parameterNamesArr[3] = "InternetGatewayDevice.DownloadDiagnostics.EOMTime";
		getParameterValues.setParameterNames(parameterNamesArr);
		devRPCArr[0] = new DevRpc();
		devRPCArr[0].devId = deviceId;
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
		DevRPCManager devRPCManager = new DevRPCManager(gw_type);
		devRPCRep = devRPCManager.execRPC(devRPCArr, Global.DiagCmd_Type);
		Map<String, String> map = new HashMap<String, String>();
		String errMessage = "";
		Map downByHTTPMap = null;
		if (devRPCRep == null || devRPCRep.size() == 0)
		{
			logger.warn("[{}]List<DevRpcCmdOBJ>返回为空！", deviceId);
			errMessage = "设备未知错误";
			map.put("message", errMessage);
			return map;
		}
		else if (devRPCRep.get(0) == null)
		{
			logger.warn("[{}]DevRpcCmdOBJ返回为空！", deviceId);
			errMessage = "设备未知错误";
			map.put("message", errMessage);
			return map;
		}
		else
		{
			int stat = devRPCRep.get(0).getStat();
			if (stat != 1)
			{
				logger.warn("[{}]devRPCRep的状态不为1！", deviceId);
				errMessage = Global.G_Fault_Map.get(stat).getFaultDesc();
				if (StringUtil.IsEmpty(errMessage))
				{
					errMessage = "设备未知错误";
				}
				map.put("message", errMessage);
				return map;
			}
			else
			{
				if (devRPCRep.get(0).getRpcList() == null
						|| devRPCRep.get(0).getRpcList().size() == 0)
				{
					logger.warn("[{}]List<ACSRpcCmdOBJ>返回为空！", deviceId);
					errMessage = "系统内部错误";
					map.put("message", errMessage);
					return map;
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
								logger.warn("[{}]设备返回：{}", deviceId, resp);
								if (resp == null || "".equals(resp))
								{
									logger.warn("[{}]DevRpcCmdOBJ.value == null",
											deviceId);
									errMessage = "系统内部错误，无返回值";
									map.put("message", errMessage);
									return map;
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
												downByHTTPMap = new HashMap<String, String>();
												for (int j = 0; j < parameterValueStructArr.length; j++)
												{
													downByHTTPMap
															.put(parameterValueStructArr[j]
																	.getName(),
																	parameterValueStructArr[j]
																			.getValue().para_value);
												}
											}
											else
											{
												errMessage = "系统内部错误，无返回值";
												map.put("message", errMessage);
												return map;
											}
										}
										else
										{
											errMessage = "系统内部错误，无返回值";
											map.put("message", errMessage);
											return map;
										}
									}
									else
									{
										errMessage = "系统内部错误，无返回值";
										map.put("message", errMessage);
										return map;
									}
								}
							}
						}
					}
					else
					{
						errMessage = "系统内部错误，无返回值";
						map.put("message", errMessage);
						return map;
					}
				}
				if (downByHTTPMap == null || downByHTTPMap.isEmpty())
				{
					errMessage = "系统内部错误，无返回值";
					map.put("message", errMessage);
					return map;
				}
				else
				{
					// 测速结果值
					String SampledValues = ""
							+ downByHTTPMap
									.get("InternetGatewayDevice.DownloadDiagnostics.SampledValues");
					// 总体速率采样
					String SampledTotalValues = ""
							+ downByHTTPMap
									.get("InternetGatewayDevice.DownloadDiagnostics.SampledTotalValues");
					// 开始时间
					String transportStartTime = ""
							+ downByHTTPMap
									.get("InternetGatewayDevice.DownloadDiagnostics.BOMTime");
					// 结束时间
					String transportEndTime = ""
							+ downByHTTPMap
									.get("InternetGatewayDevice.DownloadDiagnostics.EOMTime");
					if (!StringUtil.IsEmpty(transportStartTime)
							&& !StringUtil.IsEmpty(transportEndTime))
					{
						map.put("TransportStartTime",
								(transportStartTime.replace("T", " ")).substring(0, 19));
						map.put("TransportEndTime",
								(transportEndTime.replace("T", " ")).substring(0, 19));
					}
					else
					{
						map.put("TransportStartTime", "");
						map.put("TransportEndTime", "");
					}
					String[] sampledValues = SampledValues.split("\\|");
					String[] sampledTotalValues = SampledTotalValues.split("\\|");
					map.put("SampledValues", getSampledValue(sampledValues));
					map.put("SampledTotalValues", getSampledValue(sampledTotalValues));
					map.put("deviceSerialnumber", deviceSerialnumber);
					map.put("message", "none");
					return map;
				}
			}
		}
	}

	/**
	 * 获取平均值(保留两位小数)
	 * 
	 * @param sampledValues
	 *            数组
	 * @return 平均值
	 */
	private String getSampledValue(String[] sampledValues)
	{
		// 保留小数点后两位
		DecimalFormat df = new DecimalFormat("######0.00");
		double sum = 0.0d;
		double result;
		boolean a = false;
		for (int i = 0; i < sampledValues.length; i++)
		{
			if (sampledValues.length == 15)
			{
				a = true;
				if (i == 0 || i == 1 || i == 2 || i == 13 || i == 14)
				{
					continue;
				}
			}
			sum += Double.parseDouble(sampledValues[i]);
		}
		if (a)
		{
			result = sum / 10;
		}
		else
		{
			result = sum / sampledValues.length;
		}
		return StringUtil.getStringValue(df.format(result));
	}

	public void setDao(HttpQualityCheckDAO dao)
	{
		this.dao = dao;
	}
}
