
package com.linkage.litms.paramConfig;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ACS.DevRpc;
import ACS.Rpc;

import com.ailk.tr069.devrpc.obj.rpc.DevRpcCmdOBJ;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.acs.soap.io.XML;
import com.linkage.litms.acs.soap.io.XmlToRpc;
import com.linkage.litms.acs.soap.object.AnyObject;
import com.linkage.litms.acs.soap.object.Fault;
import com.linkage.litms.acs.soap.object.ParameterValueStruct;
import com.linkage.litms.acs.soap.object.SoapOBJ;
import com.linkage.litms.acs.soap.service.AddObject;
import com.linkage.litms.acs.soap.service.AddObjectResponse;
import com.linkage.litms.acs.soap.service.GetParameterValues;
import com.linkage.litms.acs.soap.service.GetParameterValuesResponse;
import com.linkage.litms.acs.soap.service.RPCObject;
import com.linkage.litms.acs.soap.service.SetParameterValues;
import com.linkage.litms.common.corba.DevRPCManager;
import com.linkage.module.gwms.Global;

/**
 * interface for wan/wlan configuration.
 * 
 * @author gongsj (gongsj@lianchaung.com)
 * @version 2.0, 2009
 * @since 2.0
 */
public class ParamInfoCORBA
{

	private static Logger logger = LoggerFactory.getLogger(ParamInfoCORBA.class);

	/**
	 * 获取参数实例值的Map，可同时获取多个
	 * 
	 * @param paraV
	 * @param ior
	 * @param device_id
	 * @param gather_id
	 * @return Map paramMap
	 */
	public Map<String, String> getParaValue_multi(String[] paraV, String device_id)
	{
		Map<String, String> paramMap = new HashMap<String, String>();
		GetParameterValues getParameterValues = new GetParameterValues();
		getParameterValues.setParameterNames(paraV);
		DevRpc[] devRPCArr = this.getDevRPCArr(device_id, getParameterValues);
		// 为NULL，则直接返回
		if (devRPCArr == null)
		{
			logger.error("devRPCArr == null");
			return null;
		}
		try
		{
			//得到设备类型
			String gw_type = LipossGlobals.getGw_Type(device_id);
			List<DevRpcCmdOBJ> devRPCRep = this.getDevRPCResponse(devRPCArr,Global.RpcCmd_Type,gw_type);
			Element element = dealDevRPCResponse("GetParameterValuesResponse", devRPCRep,
					device_id);
			if (element == null)
			{
				return paramMap;
			}
			GetParameterValuesResponse getParameterValuesResponse = XmlToRpc
					.GetParameterValuesResponse(element);
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
						logger.debug("name:{}, value={}", name, value);
					}
				}
			}
			else
			{
				return null;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
		return paramMap;
	}

	/**
	 * 批量设置设备上的参数值
	 * 
	 * @param params_name
	 * @param params_value
	 * @return true 设置成功 false 设置失败
	 */
	public boolean setParamValue_multi(String[] params_name, String[] params_value,
			String[] para_type_id, String device_id)
	{
		// 最外层结构:SetParameterValues
		SetParameterValues setParameterValues = new SetParameterValues();
		setParameterValues.setParameterKey("ss");
		for (String para_type : para_type_id)
		{
			logger.debug("para_type_id: {}", para_type);
		}
		ParameterValueStruct[] parameterValueStructArr = new ParameterValueStruct[params_name.length];
		for (int i = 0; i < params_name.length; i++)
		{
			AnyObject anyObject = new AnyObject();
			anyObject.para_value = params_value[i];
			anyObject.para_type_id = para_type_id[i];
			parameterValueStructArr[i] = new ParameterValueStruct();
			parameterValueStructArr[i].setName(params_name[i]);
			parameterValueStructArr[i].setValue(anyObject);
		}
		setParameterValues.setParameterList(parameterValueStructArr);
		DevRpc[] devRPCArr = getDevRPCArr(device_id, setParameterValues);
		//得到设备类型
		String gw_type = LipossGlobals.getGw_Type(device_id);
		logger.debug("devRPCArr: {}", devRPCArr);
		List<DevRpcCmdOBJ> devRPCRep = getDevRPCResponse(devRPCArr,Global.RpcCmd_Type,gw_type);
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
	 * 增加参数实例，成功返回实例结点值(对应的i) ；失败 返回0
	 * 
	 * @param paraName
	 * @param ior
	 * @param device_id
	 * @param gather_id
	 * @return instanceNum
	 */
	public int addPara(String paraName, String device_id)
	{
		logger.debug("paraName={}", paraName);
		int instanceNum = 0;
		AddObject addObject = new AddObject();
		addObject.setObjectName(paraName);
		addObject.setParameterKey("");
		DevRpc[] devRPCArr = this.getDevRPCArr(device_id, addObject);
		//得到设备类型
		String gw_type = LipossGlobals.getGw_Type(device_id);
		List<DevRpcCmdOBJ> devRPCRep = this.getDevRPCResponse(devRPCArr,Global.RpcCmd_Type,gw_type);
		// 一个设备返回的命令
		if (devRPCRep == null || devRPCRep.size() == 0 || devRPCRep.get(0) == null)
		{
			return instanceNum;
		}
		// 一个设备返回的命令
		if (devRPCRep.get(0).getStat() == 1)
		{
			Element element = dealDevRPCResponse("AddObjectResponse", devRPCRep,
					device_id);
			if (element == null)
			{
				return instanceNum;
			}
			AddObjectResponse addObjectResponse = XmlToRpc.AddObjectResponse(element);
			if (null != addObjectResponse)
			{
				instanceNum = addObjectResponse.getInstanceNumber();
			}
		}
		return instanceNum;
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
		DevRPCManager devRPCManager = new DevRPCManager(gw_type);
		devRPCRep = devRPCManager.execRPC(devRPCArr,rpcType);
		return devRPCRep;
	}

	/**
	 * 根据device_id得到长度为1的DevRPC对象数组
	 * 
	 * @author wangsenbo
	 * @date Apr 19, 2011
	 * @param
	 * @return DevRpc[]
	 */
	public DevRpc[] getDevRPCArr(String device_id, RPCObject rpcObject)
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
}
