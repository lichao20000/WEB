package com.linkage.module.gtms.stb.cao;

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
import com.linkage.module.gwms.Global;

/**
 * @author Jason(3412)
 * @date 2009-12-17
 */
public class PingCAO {

	private static Logger logger = LoggerFactory.getLogger(PingCAO.class);
	
	private String gw_type = null;
	
	public PingCAO(String gw_type){
		this.gw_type = gw_type;
	}

	private PingOBJ pingObj;

	/**
	 * ping检测调用方法
	 * 
	 * @param
	 * @author Jason(3412)
	 * @date 2009-6-16
	 * @return void
	 */
	public void ping() {
		int flag = -9;
		logger.debug("ping()");
		
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
			pingObj.setFaultCode(flag);
			return;
        }
		if(1!=list.get(0).getStat()){
			flag = list.get(0).getStat();
			pingObj.setSuccess(false);
			pingObj.setFaultCode(flag);
			return;
		}
		String setRes = list.get(0).getRpcList().get(1).getValue();
		
        SoapOBJ soapOBJ = XML.getSoabOBJ(XML.CreateXML(setRes));
        if (soapOBJ == null)
        {
        	flag = -9;
			pingObj.setSuccess(false);
			pingObj.setFaultCode(flag);
			return;
        }
        
        Map<String, String> pingMap = getDevParamMap(soapOBJ);
		if (null != pingMap && false == pingMap.isEmpty()) {
			pingObj.setSuccNum(StringUtil.getIntegerValue(pingMap
									.get("Device.LAN.IPPingDiagnostics.SuccessCount")));
			pingObj.setFailNum(StringUtil.getIntegerValue(pingMap
									.get("Device.LAN.IPPingDiagnostics.FailureCount")));
			pingObj.setDelayAvg(StringUtil.getIntegerValue(pingMap
									.get("Device.LAN.IPPingDiagnostics.AverageResponseTime")));
			pingObj.setDelayMin(StringUtil.getIntegerValue(pingMap
									.get("Device.LAN.IPPingDiagnostics.MinimumResponseTime")));
			pingObj.setDelayMax(StringUtil.getIntegerValue(pingMap
									.get("Device.LAN.IPPingDiagnostics.MaximumResponseTime")));
			pingObj.setSuccess(true);
		} else {
			pingObj.setSuccess(false);
			pingObj.setFaultCode(flag);
		}

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
				.setName("Device.LAN.IPPingDiagnostics.DiagnosticsState");
		AnyObject anyObject = new AnyObject();
		anyObject.para_value = "Requested";
		anyObject.para_type_id = "1";
		ParameterValueStruct[0].setValue(anyObject);

		ParameterValueStruct[1] = new ParameterValueStruct();
		ParameterValueStruct[1]
				.setName("Device.LAN.IPPingDiagnostics.Host");
		anyObject = new AnyObject();
		anyObject.para_value = pingObj.getPingAddr();
		anyObject.para_type_id = "1";
		ParameterValueStruct[1].setValue(anyObject);

		ParameterValueStruct[2] = new ParameterValueStruct();
		ParameterValueStruct[2]
				.setName("Device.LAN.IPPingDiagnostics.NumberOfRepetitions");
		anyObject = new AnyObject();
		anyObject.para_value = String.valueOf(pingObj.getPackNum());
		anyObject.para_type_id = "3";
		ParameterValueStruct[2].setValue(anyObject);

		ParameterValueStruct[3] = new ParameterValueStruct();
		ParameterValueStruct[3]
				.setName("Device.LAN.IPPingDiagnostics.Timeout");
		anyObject = new AnyObject();
		anyObject.para_value = String.valueOf(pingObj.getTimeout());
		anyObject.para_type_id = "3";
		ParameterValueStruct[3].setValue(anyObject);

		ParameterValueStruct[4] = new ParameterValueStruct();
		ParameterValueStruct[4]
				.setName("Device.LAN.IPPingDiagnostics.DataBlockSize");
		anyObject = new AnyObject();
		anyObject.para_value = String.valueOf(pingObj.getPackSize());
		anyObject.para_type_id = "3";
		ParameterValueStruct[4].setValue(anyObject);

		ParameterValueStruct[5] = new ParameterValueStruct();
		ParameterValueStruct[5]
				.setName("Device.LAN.IPPingDiagnostics.DSCP");
		anyObject = new AnyObject();
		anyObject.para_value = String.valueOf(pingObj.getDscp());
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
		String[] parameterNamesArr = new String[5];
		parameterNamesArr[0] = "Device.LAN.IPPingDiagnostics.SuccessCount";
		parameterNamesArr[1] = "Device.LAN.IPPingDiagnostics.FailureCount";
		parameterNamesArr[2] = "Device.LAN.IPPingDiagnostics.AverageResponseTime";
		parameterNamesArr[3] = "Device.LAN.IPPingDiagnostics.MaximumResponseTime";
		parameterNamesArr[4] = "Device.LAN.IPPingDiagnostics.MinimumResponseTime";
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
		rpc1.rpcValue = setParameterValues.toRPC();;
		
		Rpc rpc2 = new Rpc();
		rpc2.rpcId = "2";
		rpc2.rpcName = getParameterValues.getClass().getSimpleName();
		rpc2.rpcValue = getParameterValues.toRPC();;
		
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
		int arrayLen = getParameterValuesResponse.getParameterList().length;
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
