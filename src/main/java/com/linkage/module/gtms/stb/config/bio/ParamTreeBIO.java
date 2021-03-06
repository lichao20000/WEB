package com.linkage.module.gtms.stb.config.bio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ACS.DevRpc;
import ACS.Rpc;

import com.ailk.tr069.devrpc.obj.rpc.DevRpcCmdOBJ;
import com.linkage.litms.acs.soap.io.XML;
import com.linkage.litms.acs.soap.io.XmlToRpc;
import com.linkage.litms.acs.soap.object.AnyObject;
import com.linkage.litms.acs.soap.object.Fault;
import com.linkage.litms.acs.soap.object.ParameterAttributeStruct;
import com.linkage.litms.acs.soap.object.ParameterInfoStruct;
import com.linkage.litms.acs.soap.object.ParameterValueStruct;
import com.linkage.litms.acs.soap.object.SetParameterAttributesStruct;
import com.linkage.litms.acs.soap.object.SoapOBJ;
import com.linkage.litms.acs.soap.service.AddObject;
import com.linkage.litms.acs.soap.service.DeleteObject;
import com.linkage.litms.acs.soap.service.GetParameterAttributes;
import com.linkage.litms.acs.soap.service.GetParameterAttributesResponse;
import com.linkage.litms.acs.soap.service.GetParameterNames;
import com.linkage.litms.acs.soap.service.GetParameterNamesResponse;
import com.linkage.litms.acs.soap.service.GetParameterValues;
import com.linkage.litms.acs.soap.service.GetParameterValuesResponse;
import com.linkage.litms.acs.soap.service.RPCObject;
import com.linkage.litms.acs.soap.service.SetParameterAttributes;
import com.linkage.litms.acs.soap.service.SetParameterValues;
import com.linkage.litms.common.corba.DevRPCManager;
import com.linkage.litms.common.util.VendorUtil;
import com.linkage.module.gwms.Global;

/**
 * 参数配置管理模块（树状结构操作）
 * 
 * @author benny Modify Record: 2007-06-18 Alex.Yan (yanhj@lianchuang.com) RemoteDB ACS.
 */
public class ParamTreeBIO
{
	
	
	private String gw_type = "";

	public String getGwType() {
		return gw_type;
	}

	public void setGwType(String gwType) {
		this.gw_type = gwType;
	}
	
	

	/** log */
	private static Logger logger = LoggerFactory.getLogger(ParamTreeBIO.class);


	/**
	 * 返回参数名和是否可写
	 * 
	 * @param request
	 * @return
	 */
	public HashMap getParaMap(String paraV, String device_id)
	{
		// 接收传递过来的参数
		HashMap paraMap = new HashMap();
		paraMap.clear();
		GetParameterNames getParameterNames = new GetParameterNames();
		getParameterNames.setParameterPath(paraV);
		getParameterNames.setNextLevel(1);
		DevRpc[] devRPCArr = this.getDevRPCArr(device_id, getParameterNames);
		List<DevRpcCmdOBJ> devRpcCmdOBJList = this.getDevRPCResponse(devRPCArr,
				Global.RpcCmd_Type);
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
	
	/**
	 * 返回参数名和是否可写
	 * @param request
	 * @return
	 */
	public HashMap getParaWithCode(String paraV, String device_id)
	{
		// 接收传递过来的参数
		HashMap paraMap = new HashMap();
		paraMap.put("code", 0);
		GetParameterNames getParameterNames = new GetParameterNames();
		getParameterNames.setParameterPath(paraV);
		getParameterNames.setNextLevel(1);
		DevRpc[] devRPCArr = this.getDevRPCArr(device_id, getParameterNames);
		List<DevRpcCmdOBJ> devRpcCmdOBJList = this.getDevRPCResponse(devRPCArr,
				Global.RpcCmd_Type);
		if(devRpcCmdOBJList != null && devRpcCmdOBJList.size() > 0) {
			paraMap.put("code", devRpcCmdOBJList.get(0).getStat());
		}
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
	
	
	
	/**
	 * 根据得到DevRPC[]对象调用corba接口通知后台
	 * 
	 * @author wangsenbo
	 * @date Apr 19, 2011
	 * @param
	 * @return List<DevRpcCmdOBJ>
	 */
	
	
	public List<DevRpcCmdOBJ> getDevRPCResponse(DevRpc[] devRPCArr, int rpcType)
	{
		logger.debug("getDevRPCResponse(devRPCArr)");
		if (devRPCArr == null)
		{
			logger.error("devRPCArr == null");
			return null;
		}
		List<DevRpcCmdOBJ> devRPCRep = null;
		DevRPCManager devRPCManager = new DevRPCManager(gw_type);
		devRPCRep = devRPCManager.execRPC(devRPCArr, rpcType);
		return devRPCRep;
	}
	
	
	
	public String getString(Map paraMap)
	{
		String strNode = "";
		if (paraMap != null)
		{
			int i = 0;
			while (paraMap.get(i + "") != null)
			{
				if (i == 0)
				{
					strNode = (String) paraMap.get(i + "");
				}
				else
				{
					strNode += "|" + (String) paraMap.get(i + "");
				}
				i++;
			}
		}
		return strNode;
	}
	
	
	/**
	 * 增加参数实例，成功返回 1 ；失败 0
	 * 
	 * @param paraV
	 * @param ior
	 * @param device_id
	 * @param gather_id
	 * @return int flag
	 */
	public int addPara(String paraV, String device_id)
	{
		int flag = 0;
		AddObject addObject = new AddObject();
		addObject.setObjectName(paraV);
		addObject.setParameterKey("");
		DevRpc[] devRPCArr = this.getDevRPCArr(device_id, addObject);
		List<DevRpcCmdOBJ> devRpcCmdOBJList = this.getDevRPCResponse(devRPCArr,
				Global.RpcCmd_Type);
		if (devRpcCmdOBJList == null || devRpcCmdOBJList.size() == 0)
		{
			logger.warn("[{}]List<DevRpcCmdOBJ>返回为空！", device_id);
		}
		else if (devRpcCmdOBJList.get(0) == null)
		{
			logger.warn("[{}]DevRpcCmdOBJ返回为空！", device_id);
		}
		else
		{
			if (devRpcCmdOBJList.get(0).getStat() == 1)
			{
				flag = 1;
			}
			else
			{
				flag = 0;
			}
		}
		return flag;
	}

	
	
	/**
	 * 设置参数实例值的Map
	 * 
	 * @param paraV
	 * @param ior
	 * @param device_id
	 * @param gather_id
	 * @return Map paramMap
	 */
	public int setParaValueFlag(String paraV, String device_id, String param_value )
	{
		logger.debug("setParaValueFlag({},{},{})", new Object[] { paraV, device_id,
				param_value });
		int flag = 0;
		SetParameterValues setParameterValues = new SetParameterValues();
		setParameterValues.setParameterKey("SetParameterValues");
		AnyObject anyObject = new AnyObject();
		anyObject.para_value = param_value;
		String para_type_id = this.getParaType(paraV, device_id);
		logger.debug("para_type_id :{}", para_type_id);
		anyObject.para_type_id = para_type_id;
		ParameterValueStruct parameterValueStruct = new ParameterValueStruct();
		parameterValueStruct.setName(paraV);
		parameterValueStruct.setValue(anyObject);
		ParameterValueStruct[] parameterValueStructArr = null;
		// 判断是思科的则增加一个参数
		// if ("11".equals(getDeviceModel(device_id))) {
		if (VendorUtil.IsCiscoByDeviceId(device_id))
		{
			parameterValueStructArr = new ParameterValueStruct[2];
			parameterValueStructArr[0] = parameterValueStruct;
			String tmp = paraV.substring(0, paraV.lastIndexOf(".")) + ".Apply";
			Map mapTmp = getParaValueMap(tmp, device_id);
			if (mapTmp != null)
			{
				logger.warn("==================================存在APPLY节点");
				anyObject = new AnyObject();
				anyObject.para_value = "1";
				anyObject.para_type_id = "1";
				parameterValueStruct = new ParameterValueStruct();
				parameterValueStruct.setName(tmp);
				parameterValueStruct.setValue(anyObject);
				parameterValueStructArr[1] = parameterValueStruct;
			}
			else
			{
				logger.warn("==================================不存在APPLY节点");
				anyObject = new AnyObject();
				anyObject.para_value = "1";
				anyObject.para_type_id = "1";
				parameterValueStruct = new ParameterValueStruct();
				tmp = paraV.substring(0, paraV.lastIndexOf(".")) + ".X_CT-COM_Apply";
				parameterValueStruct.setName(tmp);
				parameterValueStruct.setValue(anyObject);
				parameterValueStructArr[1] = parameterValueStruct;
			}
		}
		else
		{
			parameterValueStructArr = new ParameterValueStruct[1];
			parameterValueStructArr[0] = parameterValueStruct;
		}
		setParameterValues.setParameterList(parameterValueStructArr);
		// getParameterValues.
		DevRpc[] devRPCArr = this.getDevRPCArr(device_id, setParameterValues);
		List<DevRpcCmdOBJ> devRpcCmdOBJList = this.getDevRPCResponse(devRPCArr,
				Global.RpcCmd_Type);
		if (devRpcCmdOBJList == null || devRpcCmdOBJList.size() == 0)
		{
			logger.warn("[{}]List<DevRpcCmdOBJ>返回为空！", device_id);
		}
		else if (devRpcCmdOBJList.get(0) == null)
		{
			logger.warn("[{}]DevRpcCmdOBJ返回为空！", device_id);
		}
		else
		{
			if (devRpcCmdOBJList.get(0).getStat() == 1)
			{
				flag = 1;
			}
			else
			{
				flag = 0;
			}
		}
		return flag;
	}
	
	/**
	 * 设置参数实例值的Map
	 * @param para_name 参数名
	 * @param device_id 设备id
	 * @param param_value 参数值
	 * @return code
	 */
	public int setParaValue(String para_name, String device_id, String param_value )
	{
		logger.debug("setParaValue({},{},{})", new Object[] { para_name, device_id,
				param_value });
		int code = 0;
		SetParameterValues setParameterValues = new SetParameterValues();
		setParameterValues.setParameterKey("SetParameterValues");
		AnyObject anyObject = new AnyObject();
		anyObject.para_value = param_value;
		String para_type_id = this.getParaType(para_name, device_id);
		logger.debug("para_type_id :{}", para_type_id);
		anyObject.para_type_id = para_type_id;
		ParameterValueStruct parameterValueStruct = new ParameterValueStruct();
		parameterValueStruct.setName(para_name);
		parameterValueStruct.setValue(anyObject);
		ParameterValueStruct[] parameterValueStructArr = null;
		// 判断是思科的则增加一个参数
		// if ("11".equals(getDeviceModel(device_id))) {
		if (VendorUtil.IsCiscoByDeviceId(device_id))
		{
			parameterValueStructArr = new ParameterValueStruct[2];
			parameterValueStructArr[0] = parameterValueStruct;
			String tmp = para_name.substring(0, para_name.lastIndexOf(".")) + ".Apply";
			Map mapTmp = getParaValueMap(tmp, device_id);
			if (mapTmp != null)
			{
				logger.warn("存在APPLY节点");
				anyObject = new AnyObject();
				anyObject.para_value = "1";
				anyObject.para_type_id = "1";
				parameterValueStruct = new ParameterValueStruct();
				parameterValueStruct.setName(tmp);
				parameterValueStruct.setValue(anyObject);
				parameterValueStructArr[1] = parameterValueStruct;
			}
			else
			{
				logger.warn("不存在APPLY节点");
				anyObject = new AnyObject();
				anyObject.para_value = "1";
				anyObject.para_type_id = "1";
				parameterValueStruct = new ParameterValueStruct();
				tmp = para_name.substring(0, para_name.lastIndexOf(".")) + ".X_CT-COM_Apply";
				parameterValueStruct.setName(tmp);
				parameterValueStruct.setValue(anyObject);
				parameterValueStructArr[1] = parameterValueStruct;
			}
		}
		else
		{
			parameterValueStructArr = new ParameterValueStruct[1];
			parameterValueStructArr[0] = parameterValueStruct;
		}
		setParameterValues.setParameterList(parameterValueStructArr);
		// getParameterValues.
		DevRpc[] devRPCArr = this.getDevRPCArr(device_id, setParameterValues);
		List<DevRpcCmdOBJ> devRpcCmdOBJList = this.getDevRPCResponse(devRPCArr,
				Global.RpcCmd_Type);
		if (devRpcCmdOBJList == null || devRpcCmdOBJList.size() == 0)
		{
			logger.warn("[{}]List<DevRpcCmdOBJ>返回为空！", device_id);
		}
		else if (devRpcCmdOBJList.get(0) == null)
		{
			logger.warn("[{}]DevRpcCmdOBJ返回为空！", device_id);
		}
		else
		{
			code = devRpcCmdOBJList.get(0).getStat();
		}
		return code;
	}
	
	
	/**
	 * @param paraV
	 * @param ior
	 * @param device_id
	 * @param gather_id
	 * @return
	 */
	public String getParaType(String paraV, String device_id)
	{
		String paraType_id = "";
		GetParameterValues getParameterValues = new GetParameterValues();
		String[] paramValues = { paraV };
		getParameterValues.setParameterNames(paramValues);
		// getParameterValues.
		DevRpc[] devRPCArr = this.getDevRPCArr(device_id, getParameterValues);
		// 为NULL，则直接返回
		if (devRPCArr == null)
			return paraType_id;
		List<DevRpcCmdOBJ> devRpcCmdOBJList = this.getDevRPCResponse(devRPCArr,
				Global.RpcCmd_Type);
		Element element = dealDevRPCResponse("GetParameterValuesResponse",
				devRpcCmdOBJList, device_id);
		if (element == null)
		{
			return paraType_id;
		}
		GetParameterValuesResponse getParameterValuesResponse = XmlToRpc
				.GetParameterValuesResponse(element);
		if (getParameterValuesResponse != null)
		{
			ParameterValueStruct[] parameterValueStructArr = getParameterValuesResponse
					.getParameterList();
			if (parameterValueStructArr != null)
			{
				for (int j = 0; j < parameterValueStructArr.length; j++)
				{
					paraType_id = parameterValueStructArr[j].getValue().para_type_id;
				}
			}
		}
		return paraType_id;
	}
	
	/**
	 * 单台设备单条命令返回的RPC结果处理
	 * 
	 * @author wangsenbo
	 * @date Mar 22, 2011
	 * @param
	 * @return RPCObject
	 */
	public Element dealDevRPCResponse(String stringRpcName,
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
	 * 获取参数实例值的Map
	 * 
	 * @param paraV
	 * @param ior
	 * @param device_id
	 * @param gather_id
	 * @return Map paramMap
	 */
	public Map getParaValueMap(String paraV, String device_id )
	{
		Map paramMap = new HashMap();
		GetParameterValues getParameterValues = new GetParameterValues();
		String[] paramValues = { paraV };
		getParameterValues.setParameterNames(paramValues);
		// getParameterValues.
		DevRpc[] devRPCArr = this.getDevRPCArr(device_id, getParameterValues);
		// 为NULL，则直接返回
		if (devRPCArr == null)
			return paramMap;
		List<DevRpcCmdOBJ> devRpcCmdOBJList = this.getDevRPCResponse(devRPCArr,
				Global.RpcCmd_Type );
		Element element = dealDevRPCResponse("GetParameterValuesResponse",
				devRpcCmdOBJList, device_id);
		if (element == null)
		{
			return paramMap;
		}
		GetParameterValuesResponse getParameterValuesResponse = XmlToRpc
				.GetParameterValuesResponse(element);
		if (getParameterValuesResponse != null)
		{
			ParameterValueStruct[] parameterValueStructArr = getParameterValuesResponse
					.getParameterList();
			if(null == parameterValueStructArr || parameterValueStructArr.length == 0){
				logger.warn("parameterValueStructArr is null,deviceId:{}",device_id);
				return paramMap;
			}
			for (int j = 0; j < parameterValueStructArr.length; j++)
			{
				paramMap.put(parameterValueStructArr[j].getName(),
						parameterValueStructArr[j].getValue().para_value);
			}
		}
		return paramMap;
	}

	/**
	 * 获取参数实例值的Map
	 * @param para_name 参数名
	 * @param device_id 设备id
	 * @return map
	 */
	public Map getParaValueWithCode(String para_name, String device_id )
	{
		Map paramMap = new HashMap();
		paramMap.put("code", 0);
		
		GetParameterValues getParameterValues = new GetParameterValues();
		String[] paramValues = { para_name };
		getParameterValues.setParameterNames(paramValues);
		// getParameterValues.
		DevRpc[] devRPCArr = this.getDevRPCArr(device_id, getParameterValues);
		// 为NULL，则直接返回
		if (devRPCArr == null)
			return paramMap;
		List<DevRpcCmdOBJ> devRpcCmdOBJList = this.getDevRPCResponse(devRPCArr,
				Global.RpcCmd_Type );
		if(devRpcCmdOBJList != null && devRpcCmdOBJList.size() > 0) {
			paramMap.put("code", devRpcCmdOBJList.get(0).getStat());
		}
		Element element = dealDevRPCResponse("GetParameterValuesResponse",
				devRpcCmdOBJList, device_id);
		if (element == null)
		{
			return paramMap;
		}
		GetParameterValuesResponse getParameterValuesResponse = XmlToRpc
				.GetParameterValuesResponse(element);
		if (getParameterValuesResponse != null)
		{
			ParameterValueStruct[] parameterValueStructArr = getParameterValuesResponse
					.getParameterList();
			if(parameterValueStructArr != null) 
			{
				for (int j = 0; j < parameterValueStructArr.length; j++)
				{
					paramMap.put(parameterValueStructArr[j].getName(),
							parameterValueStructArr[j].getValue().para_value);
				}
			}
		}
		return paramMap;
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
	public int delPara(String paraV, String device_id)
	{
		int flag = 0;
		DeleteObject delObject = new DeleteObject();
		delObject.setObjectName(paraV);
		delObject.setParameterKey("");
		DevRpc[] devRPCArr = this.getDevRPCArr(device_id, delObject);
		List<DevRpcCmdOBJ> devRpcCmdOBJList = this.getDevRPCResponse(devRPCArr,
				Global.RpcCmd_Type);
		if (devRpcCmdOBJList == null || devRpcCmdOBJList.size() == 0)
		{
			logger.warn("[{}]List<DevRpcCmdOBJ>返回为空！", device_id);
		}
		else if (devRpcCmdOBJList.get(0) == null)
		{
			logger.warn("[{}]DevRpcCmdOBJ返回为空！", device_id);
		}
		else
		{
			if (devRpcCmdOBJList.get(0).getStat() == 1)
			{
				flag = 1;
			}
			else
			{
				flag = 0;
			}
		}
		return flag;
	}
	
	
	
	
	
	
	/**
	 * 配置用户可写属性
	 * 
	 * @param paraV
	 * @param ior
	 * @param device_id
	 * @param gather_id
	 * @param param_value
	 * @return
	 */
	public int configUserWritable(String paraV, String device_id,
			HttpServletRequest request )
	{
		// 返回结果
		int flag = 0;
		// 是否通知
		String _notificationValue = request.getParameter("_notificationValue");
		logger.debug("_notificationValue============>" + _notificationValue);
		boolean _ifNotification = false;
		if (_notificationValue.equals("1"))
		{
			_ifNotification = true;
		}
		// 通知方式
		String _notification = request.getParameter("_notification");
		// 属性通知
		String _accessListChange = request.getParameter("_accessValue");
		boolean _ifAccessListChange = false;
		if (_accessListChange.equals("1"))
		{
			_ifAccessListChange = true;
		}
		// 属性值列表
		String _accessList = request.getParameter("_accessList");
		String[] _arrAccessList = null;
		if (_accessList != null && !_accessList.equals(""))
		{
			_arrAccessList = _accessList.split(",");
		}
		else
		{
			_arrAccessList = new String[0];
		}
		SetParameterAttributes setParameterAttributes = new SetParameterAttributes();
		SetParameterAttributesStruct setParameterAttributesStruct = new SetParameterAttributesStruct();
		setParameterAttributesStruct.setName(paraV);
		setParameterAttributesStruct.setNotificationChange(_ifNotification);
		setParameterAttributesStruct.setNotification(Integer.parseInt(_notification));
		setParameterAttributesStruct.setAccessListChange(_ifAccessListChange);
		setParameterAttributesStruct.setAccessList(_arrAccessList);
		SetParameterAttributesStruct[] setParameterAttributesStructArr = { setParameterAttributesStruct };
		setParameterAttributes.setParameterList(setParameterAttributesStructArr);
		// getParameterValues.
		DevRpc[] devRPCArr = this.getDevRPCArr(device_id, setParameterAttributes);
		List<DevRpcCmdOBJ> devRPCRep = this.getDevRPCResponse(devRPCArr,
				Global.RpcCmd_Type);
		// 一个设备返回的命令
		if (devRPCRep == null || devRPCRep.size() == 0 || devRPCRep.get(0) == null)
		{
			return flag;
		}
		if (devRPCRep.get(0).getStat() == 1)
		{
			flag = 1;
		}
		else
		{
			flag = 0;
		}
		return flag;
	}
	
	
	/**
	 * 生成获取到的参数值的HTML
	 * 
	 * @param paraMap
	 * @return String strHtml
	 */
	public String getParaVlue(Map paraMap)
	{
		String strValue = "";
		if (paraMap == null)
		{
			strValue = "";
		}
		else
		{
			Set set = paraMap.keySet();
			Iterator iterator = set.iterator();
			String name = null;
			String value = null;
			int i = 0;
			while (iterator.hasNext())
			{
				name = (String) iterator.next();
				value = (String) paraMap.get(name);
				if (i == 0)
				{
					strValue += value;
				}
				else
				{
					strValue += "  " + value;
				}
				i++;
			}
			iterator = null;
			set = null;
		}
		logger.debug("LZJ>>>strValue : " + strValue);
		return strValue;
	}
	
	/**
	 * 参数属性上报获取的参数MAP
	 * 
	 * @param paraV
	 * @param ior
	 * @param device_id
	 * @param gather_id
	 * @return
	 */
	public Map upParaValue(String paraV, String ior, String device_id, String gather_id )
	{
		Map paramMap = new HashMap();
		GetParameterAttributes getParameterAttributes = new GetParameterAttributes();
		String[] paramValues = { paraV };
		getParameterAttributes.setParameterNames(paramValues);
		DevRpc[] devRPCArr = this.getDevRPCArr(device_id, getParameterAttributes);
		// 为NULL，则直接返回
		if (devRPCArr == null)
			return paramMap;
		try
		{
			List<DevRpcCmdOBJ> devRPCRep = this.getDevRPCResponse(devRPCArr,
					Global.RpcCmd_Type);
			// 一个设备返回的命令
			Element element = dealDevRPCResponse("GetParameterAttributesResponse",
					devRPCRep, device_id);
			if (element == null)
			{
				return paramMap;
			}
			GetParameterAttributesResponse getParameterAttributesResponse = new GetParameterAttributesResponse();
			// 把SOAP形式的文件转换成标准的XML,便于通信
			getParameterAttributesResponse = XmlToRpc
					.GetParameterAttributesResponse(element);
			if (null != getParameterAttributesResponse)
			{
				ParameterAttributeStruct[] pisArr = getParameterAttributesResponse
						.getParameterList();
				if (pisArr != null)
				{
					String name = null;
					int notification;
					for (int i = 0; i < pisArr.length; i++)
					{
						name = pisArr[i].getName();
						notification = pisArr[i].getNotification();
						String[] accessList = pisArr[i].getAccessList();
						// if(accessList == null){
						// accessList = new String[0];
						// }
						logger.debug("name :" + name);
						logger.debug("notification :" + notification);
						logger.debug("accessList :" + accessList);
						paramMap.put(name + "," + notification, accessList);
					}
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return paramMap;
	}
	
	
	
	public String getUpParaHTML(Map upMap)
	{
		String strHtml = "";
		logger.debug("upMap===============>" + upMap.size());
		if (upMap == null)
		{
			strHtml = "<TR bgcolor='#FFFFFF'><TD class=column align=left colspan=3>属性上报失败,请重新上报。</TD></TR>";
		}
		else
		{
			Set set = upMap.keySet();
			Iterator iterator = set.iterator();
			String _para = null;
			String[] _access = null;
			while (iterator.hasNext())
			{
				String l_access = "";
				_para = (String) iterator.next();
				_access = (String[]) upMap.get(_para);
				if (_access != null)
				{
					for (int i = 0; i < _access.length; i++)
					{
						if (i == (_access.length - 1))
						{
							l_access += _access[i];
						}
						else
						{
							l_access += _access[i] + "|";
						}
					}
				}
				strHtml += "<TR bgcolor='#FFFFFF'>";
				strHtml += "<TD class=column>" + _para.split(",")[0] + "</TD>";
				strHtml += "<TD class=column>" + _para.split(",")[1].toString() + "</TD>";
				strHtml += "<TD class=column> <textarea name='access' class='jive-description' rows='2' cols='18'>"
						+ l_access + "</textarea></TD>";
				strHtml += "</TR>";
			}
			iterator = null;
			set = null;
		}
		logger.debug("strHtml======>" + strHtml);
		return strHtml;
	}
	
}
