package com.linkage.module.gtms.stb.cao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ACS.DevRpc;
import ACS.Rpc;

import com.ailk.tr069.devrpc.dao.corba.AcsCorbaDAO;
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
import com.linkage.module.gtms.stb.obj.tr069.PingOBJ;
import com.linkage.module.gwms.ConstantClass;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.util.corba.SuperGatherCorba;

/**
 * @author Jason(3412)
 * @date 2009-12-17
 */
public class TraceRouteCAO {

	private static Logger logger = LoggerFactory.getLogger(TraceRouteCAO.class);
	
	private String gw_type = null;
	
	public TraceRouteCAO(String gw_type){
		this.gw_type = gw_type;
	}

	private PingOBJ pingObj;

	/**
	 * 调用ACS诊断设备
	 * 
	 * @param
	 * @author Jason(3412)
	 * @date 2009-6-16
	 * @return void
	 */
	public void pingACS() {
		int flag = -9;
		logger.debug("pingACS()");
		
		if (null == pingObj || StringUtil.IsEmpty(pingObj.getDeviceId())) {
			logger.warn("deivceId or rpcArr is null");
		}
		DevRpc[] devRPCArr = createDevRPCArray(pingObj.getDeviceId());
		
		List<DevRpcCmdOBJ> list = new AcsCorbaDAO(Global.getPrefixName(gw_type)
				+ Global.SYSTEM_ACS).execRPC(LipossGlobals.getClientId(),
				Global.DiagCmd_Type, Global.Priority_Hig, devRPCArr);
		
		if (null == list || list.isEmpty())
        {
			flag = -9;
			pingObj.setSuccess(false);
			pingObj.setResult("0");
			pingObj.setFaultCode(flag);
			pingObj.setFaultStr(Global.G_Fault_Map.get(flag).getFaultReason());
			return;
        }
//		if(1!=list.get(0).getStat()){
//			flag = list.get(0).getStat();
//			pingObj.setSuccess(false);
//			pingObj.setResult("0");
//			pingObj.setFaultCode(flag);
//			pingObj.setFaultStr(IptvGlobals.G_Fault_Map.get(flag));
//			return;
//		}
		logger.debug("pingACS success：" + pingObj.isSuccess());
		
		String setRes = list.get(0).getRpcList().get(1).getValue();
		
        SoapOBJ soapOBJ = XML.getSoabOBJ(XML.CreateXML(setRes));
        if (soapOBJ == null)
        {
        	flag = -9;
			pingObj.setSuccess(false);
			pingObj.setFaultCode(flag);
			return;
        }
        
        //获取ping测试结构Map
        
		Map<String, String> pingMap = getDevParamMap(soapOBJ);
		if (null != pingMap && !pingMap.isEmpty()) {
			pingObj.setResponseTime(pingMap.get("Device.LAN.TraceRouteDiagnostics.ResponseTime"));
			pingObj.setNumberOfRouteHops(pingMap.get("Device.LAN.TraceRouteDiagnostics.NumberOfRouteHops"));
			
			final int numb = Integer.parseInt(pingObj.getNumberOfRouteHops());
			List<String> hopHostIList = new ArrayList<String>();
			for(int i=1;i<=numb;i++)
			{
				String temp = pingMap.get("Device.LAN.TraceRouteDiagnostics.RouteHops."+i+".HopHost");
				if(null != temp && !"".equals(temp))
				{
					hopHostIList.add("Host-"+ i + ": " + temp);
				}
			}
			pingObj.setResult("1");
			pingObj.setHopHostI(hopHostIList);
			pingObj.setSuccess(true);
		} else {
			pingObj.setResult("0");
			pingObj.setSuccess(false);
			pingObj.setFaultCode(flag);
			pingObj.setFaultStr(Global.G_Fault_Map.get(flag).getFaultReason());
		}
	}
	
	/**
	 * 调用采集模块采集设备
	 */
	public void pingSuperGather(PingOBJ pingObj)
	{
		logger.debug("pingSuperGather({})", new Object[]{pingObj.getDeviceId()});
		// 返回结果
		int result = new SuperGatherCorba(gw_type).getCpeParams(pingObj.getDeviceId(), ConstantClass.STB_GATHER_TRACEROUTE);
		// 成功
		if(result == 1)
		{
			pingObj.setSuccess(true);
			pingObj.setResult("1");
		}
		else
		{
			pingObj.setSuccess(false);
			pingObj.setResult("0");
			pingObj.setFaultCode(result);
			pingObj.setFaultStr(Global.G_Fault_Map.get(result).getFaultReason());
		}
		logger.debug("pingSuperGather success：" + pingObj.isSuccess());
	}

	/**
	 * ping检测需要设备的结点参数
	 * 
	 * @param
	 * @author Jason(3412)
	 * @date 2009-6-16
	 * @return SetParameterValues
	 */
	public SetParameterValues getSetParam() {
		logger.debug("getSetParam()");
		SetParameterValues setParameterValues = new SetParameterValues();
		ParameterValueStruct[] ParameterValueStruct = new ParameterValueStruct[6];

		ParameterValueStruct[0] = new ParameterValueStruct();
		ParameterValueStruct[0]
				.setName("Device.LAN.TraceRouteDiagnostics.DiagnosticsState");
		AnyObject anyObject = new AnyObject();
		anyObject.para_value = "Requested";
		anyObject.para_type_id = "1";
		ParameterValueStruct[0].setValue(anyObject);

		ParameterValueStruct[1] = new ParameterValueStruct();
		ParameterValueStruct[1]
				.setName("Device.LAN.TraceRouteDiagnostics.Host");
		anyObject = new AnyObject();
		anyObject.para_value = pingObj.getPingAddr();
		anyObject.para_type_id = "1";
		ParameterValueStruct[1].setValue(anyObject);

		ParameterValueStruct[2] = new ParameterValueStruct();
		ParameterValueStruct[2]
				.setName("Device.LAN.TraceRouteDiagnostics.Timeout");
		anyObject = new AnyObject();
		anyObject.para_value = String.valueOf(pingObj.getTimeout());
		anyObject.para_type_id = "3";
		ParameterValueStruct[2].setValue(anyObject);

		ParameterValueStruct[3] = new ParameterValueStruct();
		ParameterValueStruct[3]
				.setName("Device.LAN.TraceRouteDiagnostics.DataBlockSize");
		anyObject = new AnyObject();
		anyObject.para_value = String.valueOf(pingObj.getPackSize());
		anyObject.para_type_id = "3";
		ParameterValueStruct[3].setValue(anyObject);

		ParameterValueStruct[4] = new ParameterValueStruct();
		ParameterValueStruct[4]
				.setName("Device.LAN.TraceRouteDiagnostics.DSCP");
		anyObject = new AnyObject();
		anyObject.para_value = String.valueOf(pingObj.getDscp());
		anyObject.para_type_id = "3";
		ParameterValueStruct[4].setValue(anyObject);

		ParameterValueStruct[5] = new ParameterValueStruct();
		ParameterValueStruct[5]
				.setName("Device.LAN.TraceRouteDiagnostics.MaxHopCount");
		anyObject = new AnyObject();
		anyObject.para_value = String.valueOf(pingObj.getMaxHopCount());
		anyObject.para_type_id = "3";
		ParameterValueStruct[5].setValue(anyObject);
		
		setParameterValues.setParameterList(ParameterValueStruct);
		setParameterValues.setParameterKey("Ping");

		return setParameterValues;
	}

	/**
	 * ping检测设备返回检测结果结点树
	 * 
	 * @param
	 * @author Jason(3412)
	 * @date 2009-6-16
	 * @return GetParameterValues
	 */
	public GetParameterValues getResponseParam() {
		logger.debug("getResponseParam()");
		GetParameterValues getParameterValues = new GetParameterValues();
		String[] parameterNamesArr = new String[2];
		parameterNamesArr[0] = "Device.LAN.TraceRouteDiagnostics.NumberOfRouteHops";
		parameterNamesArr[1] = "Device.LAN.TraceRouteDiagnostics.ResponseTime";
		getParameterValues.setParameterNames(parameterNamesArr);

		return getParameterValues;

	}

	/**
	 * 生成调用ACS的结构数组，用于ping检测
	 * 
	 * @param
	 * @author Jason(3412)
	 * @date 2009-6-16
	 * @return DevRPC[]
	 */
	public DevRpc[] createDevRPCArray(String deviceId) {
		logger.debug("createDevRPCArray()");
		SetParameterValues setParameterValues = getSetParam();

		GetParameterValues getParameterValues = getResponseParam();

		DevRpc[] devRPCArr = new DevRpc[1];
		devRPCArr[0] = new DevRpc();
		devRPCArr[0].devId = deviceId;
		
		Rpc rpc1 = new Rpc();
		rpc1.rpcId = "1";
		rpc1.rpcName = setParameterValues.getClass().getSimpleName();
		rpc1.rpcValue = setParameterValues.toRPC();
		
		Rpc rpc2 = new Rpc();
		rpc2.rpcId = "1";
		rpc2.rpcName = setParameterValues.getClass().getSimpleName();
		rpc2.rpcValue = getParameterValues.toRPC();
		
		devRPCArr[0].rpcArr = new Rpc[] { rpc1,rpc2 };
		
		return devRPCArr;
	}

	/**
	 * 获取设备的采集结点信息
	 * 
	 * @param
	 * @author Jason(3412)
	 * @date 2009-6-15
	 * @return void
	 */
	public Map<String, String> getDevParamMap(SoapOBJ soapOBJ) {
		Map<String, String> paramMap = null;

		GetParameterValuesResponse getParameterValuesResponse = new GetParameterValuesResponse();
		getParameterValuesResponse = XmlToRpc
				.GetParameterValuesResponse(soapOBJ.getRpcElement());
		if(null !=getParameterValuesResponse){
			int arrayLen = 0;
			if(null!=getParameterValuesResponse.getParameterList()){
				 arrayLen = getParameterValuesResponse.getParameterList().length;
			}
			ParameterValueStruct[] paramStruct = new ParameterValueStruct[arrayLen];
			paramStruct = getParameterValuesResponse.getParameterList();
	
			// 获取ping测试结构Map
			paramMap = new HashMap<String, String>();
			if (null != paramStruct && paramStruct.length > 0) {
				for (int i = 0; i < paramStruct.length; i++) {
					paramMap.put(paramStruct[i].getName(), paramStruct[i]
							.getValue().para_value);
				}
			}
		}
		logger.debug("getDevParamMap(): return " + paramMap);
		return paramMap;
	}
	
	
	public PingOBJ getPingObj() {
		return pingObj;
	}

	public void setPingObj(PingOBJ pingObj) {
		this.pingObj = pingObj;
	}
}
