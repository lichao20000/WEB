/**
 * 家庭网关中前台web和后台ACS通讯操作工具类 Modify By HEMC
 * 
 * @2007 All Right Reserve
 * @Linkage
 */

package com.linkage.litms.common.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.linkage.litms.acs.soap.object.Fault;
import com.linkage.litms.acs.soap.object.ParameterValueStruct;
import com.linkage.litms.acs.soap.object.SoapOBJ;
import com.linkage.litms.acs.soap.service.GetParameterValues;
import com.linkage.litms.acs.soap.service.GetParameterValuesResponse;
import com.linkage.litms.acs.soap.service.RPCObject;
import com.linkage.litms.acs.soap.service.SetParameterValues;
import com.linkage.litms.common.corba.DevRPCManager;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.module.gwms.Global;

/**
 * @author Administrator
 */
public class RPCUtilHandler
{

	private static Logger logger = LoggerFactory.getLogger(RPCUtilHandler.class);
	/**
	 * 存放device_id对应的设备属性{device_serialnumber oui loopback_ip port path acs_username
	 * acs_passwd} 使用静态变量，主要是为了不再频繁查询设备资源表
	 */
	private static Map deviceAttributeMap = new HashMap();
	private static DevRPCManager devRPCManager = null;

	/**
	 * 获取数组参数的属性值，以MAP的形式返回。
	 * 
	 * @param param
	 *            属性数组(注意：此param值为节点属性，如：InternetGatewayDevice.ManagementServer.PeriodicInformEnable,而不是：InternetGatewayDevice.ManagementServer.)
	 * @param ior
	 * @param device_id
	 *            设备id
	 * @param gather_id
	 *            采集点
	 * @return MAP key：属性字符串 value：该属性对应的值
	 */
	public static Map getParamValueMap(String[] param, String device_id)
	{
		Map result = new HashMap();
		GetParameterValues getParameterValues = new GetParameterValues();
		getParameterValues.setParameterNames(param);
		DevRpc[] devRPCArr = getDevRPCArr(device_id, getParameterValues);
		List<DevRpcCmdOBJ> devRPCRep = getDevRPCResponse(devRPCArr,Global.RpcCmd_Type, LipossGlobals.getGw_Type(device_id));;
		// 一个设备返回的命令
		// 把SOAP形式的文件转换成标准的XML,便于通信
		Element element = dealDevRPCResponse("GetParameterValuesResponse", devRPCRep,
				device_id);
		if (element == null)
		{
			return result;
		}
		GetParameterValuesResponse getParameterValuesResponse = XmlToRpc
				.GetParameterValuesResponse(element);
		if (null != getParameterValuesResponse)
		{
			ParameterValueStruct[] pisArr = getParameterValuesResponse.getParameterList();
			if (pisArr != null)
			{
				String name = null;
				String value = null;
				for (int i = 0; i < pisArr.length; i++)
				{
					name = pisArr[i].getName();// .substring(param.length());
					value = pisArr[i].getValue().para_value;
					result.put(name, value);
				}
			}
		}
		return result;
	}

	/**
	 * 根据得到DevRPC[]对象和ior开始调用corba接口通知后台
	 * 
	 * @param ior
	 *            ior字符串user.getIor()
	 * @param devRPCArr
	 *            DevRpc[]数组对象
	 * @return 返回执行后的RemoteDB.DevRPCRep[]
	 */
	public static List<DevRpcCmdOBJ> getDevRPCResponse(DevRpc[] devRPCArr,int rpcType, String gw_type)
	{
		if (devRPCArr == null)
		{
			return null;
		}
		List<DevRpcCmdOBJ> devRPCRep = null;
		DevRPCManager devRPCManager = new DevRPCManager(gw_type);
		devRPCRep = devRPCManager.execRPC(devRPCArr,rpcType);
		return devRPCRep;
	}

	/**
	 * 设置属性数组参数值 获取数组参数的属性值，以MAP的形式返回。
	 * 
	 * @param param
	 *            属性数组(注意：此param值为节点属性，如：InternetGatewayDevice.ManagementServer.PeriodicInformEnable,而不是：InternetGatewayDevice.ManagementServer.) {
	 *            {InternetGatewayDevice.ManagementServer.PeriodicInformEnable,true},
	 *            {InternetGatewayDevice.ManagementServer.PeriodicInformEnable,false} }
	 * @param ior
	 * @param device_id
	 *            设备id
	 * @param gather_id
	 *            采集点
	 * @return 设置参数值调用后台是否成功！true：成功 false：失败
	 */
	public static boolean setParamValueMap(String[][] paramValue, String device_id)
	{
		if (paramValue == null)
			return false;
		ParameterValueStruct[] parameterValueStructArr = new ParameterValueStruct[paramValue.length];
		AnyObject anyObject = null;
		for (int i = 0; i < paramValue.length; i++)
		{
			ParameterValueStruct parameterValueStruct = new ParameterValueStruct();
			parameterValueStruct.setName(paramValue[i][0]);
			anyObject = new AnyObject();
			anyObject.para_value = paramValue[i][1];
			anyObject.para_type_id = "1";
			parameterValueStruct.setValue(anyObject);
			parameterValueStructArr[i] = parameterValueStruct;
		}
		SetParameterValues setParameterValues = new SetParameterValues();
		setParameterValues.setParameterKey("ss");
		setParameterValues.setParameterList(parameterValueStructArr);
		DevRpc[] devRPCArr = getDevRPCArr(device_id, setParameterValues);
		List<DevRpcCmdOBJ> devRPCRep = getDevRPCResponse(devRPCArr,Global.RpcCmd_Type, LipossGlobals.getGw_Type(device_id));
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
	 * 根据device_id得到长度为1的DevRPC对象数组
	 * 
	 * @author wangsenbo
	 * @date Apr 19, 2011
	 * @param
	 * @return DevRpc[]
	 */
	public static DevRpc[] getDevRPCArr(String device_id, RPCObject rpcObject)
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

	public static String[] getDeviceResInfo(String device_id)
	{
		String[] deviceAttribute = null;// (String[])deviceAttributeMap.get(device_id);
		if (deviceAttribute == null)
		{
			String strSQL = "select *  from tab_gw_device where device_id='" + device_id
					+ "'";
			// teledb
			if (DBUtil.GetDB() == Global.DB_MYSQL) {
				strSQL = "select oui, device_serialnumber, loopback_ip, cr_port, cr_path, acs_username, " +
						" acs_passwd from tab_gw_device where device_id='" + device_id
						+ "'";
			}
			PrepareSQL psql = new PrepareSQL(strSQL);
			psql.getSQL();
			Cursor cursor = DataSetBean.getCursor(strSQL);
			Map fields = cursor.getNext();
			// 如果查询无设备，则直接返回NULL
			if (fields == null)
			{
				return null;
			}
			deviceAttribute = new String[7];
			deviceAttribute[0] = (String) fields.get("oui");// String oui = (String)
															// fields.get("oui");
			deviceAttribute[1] = (String) fields.get("device_serialnumber");// String
																			// SerialNumber
																			// = (String)
																			// fields.get("device_serialnumber");
			deviceAttribute[2] = (String) fields.get("loopback_ip");// String ip =
																	// (String)
																	// fields.get("loopback_ip");
			deviceAttribute[3] = (String) fields.get("cr_port");// String Port = (String)
																// fields.get("port");
			deviceAttribute[4] = (String) fields.get("cr_path");// String path = (String)
																// fields.get("path");
			deviceAttribute[5] = (String) fields.get("acs_username");// String username
																		// = (String)
																		// fields.get("acs_username");
			deviceAttribute[6] = (String) fields.get("acs_passwd");// String passwd =
																	// (String)
																	// fields.get("acs_passwd");
			deviceAttributeMap.put(device_id, deviceAttribute);
			cursor = null;
		}
		return deviceAttribute;
	}

	/**
	 * 单台设备单条命令返回的RPC结果处理
	 * 
	 * @author wangsenbo
	 * @date Mar 22, 2011
	 * @param
	 * @return RPCObject
	 */
	private static Element dealDevRPCResponse(String stringRpcName,
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
}
