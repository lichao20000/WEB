package com.linkage.module.gwms.cao.gw;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ACS.DevRpc;
import ACS.Rpc;

import com.linkage.litms.acs.soap.object.AnyObject;
import com.linkage.litms.acs.soap.object.ParameterValueStruct;
import com.linkage.litms.acs.soap.service.GetParameterValues;
import com.linkage.litms.acs.soap.service.SetParameterValues;
import com.linkage.module.gwms.cao.SuperAcsRpcInvoke;
import com.linkage.module.gwms.cao.gw.interf.IParamTree;
import com.linkage.module.gwms.obj.gw.PingObject;

/**
 * @author Jason(3412)
 * @date 2009-7-6
 */
public class PingCAO extends SuperAcsRpcInvoke implements IParamTree {

	private static Logger logger = LoggerFactory.getLogger(PingCAO.class);

	private PingObject pingObj;

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
		ParameterValueStruct[] ParameterValueStruct = new ParameterValueStruct[7];

		ParameterValueStruct[0] = new ParameterValueStruct();
		ParameterValueStruct[0]
				.setName("InternetGatewayDevice.IPPingDiagnostics.DiagnosticsState");
		AnyObject anyObject = new AnyObject();
		anyObject.para_value = "Requested";
		anyObject.para_type_id = "1";
		ParameterValueStruct[0].setValue(anyObject);

		ParameterValueStruct[1] = new ParameterValueStruct();
		ParameterValueStruct[1]
				.setName("InternetGatewayDevice.IPPingDiagnostics.Interface");
		anyObject = new AnyObject();
		anyObject.para_value = pingObj.getDevInterface();
		anyObject.para_type_id = "1";
		ParameterValueStruct[1].setValue(anyObject);

		ParameterValueStruct[2] = new ParameterValueStruct();
		ParameterValueStruct[2]
				.setName("InternetGatewayDevice.IPPingDiagnostics.Host");
		anyObject = new AnyObject();
		anyObject.para_value = pingObj.getPingAddress();
		anyObject.para_type_id = "1";
		ParameterValueStruct[2].setValue(anyObject);

		ParameterValueStruct[3] = new ParameterValueStruct();
		ParameterValueStruct[3]
				.setName("InternetGatewayDevice.IPPingDiagnostics.NumberOfRepetitions");
		anyObject = new AnyObject();
		anyObject.para_value = String.valueOf(pingObj.getNumOfRepetitions());
		anyObject.para_type_id = "3";
		ParameterValueStruct[3].setValue(anyObject);

		ParameterValueStruct[4] = new ParameterValueStruct();
		ParameterValueStruct[4]
				.setName("InternetGatewayDevice.IPPingDiagnostics.Timeout");
		anyObject = new AnyObject();
		anyObject.para_value = String.valueOf(pingObj.getTimeOut());
		anyObject.para_type_id = "3";
		ParameterValueStruct[4].setValue(anyObject);

		ParameterValueStruct[5] = new ParameterValueStruct();
		ParameterValueStruct[5]
				.setName("InternetGatewayDevice.IPPingDiagnostics.DataBlockSize");
		anyObject = new AnyObject();
		anyObject.para_value = String.valueOf(pingObj.getPackageSize());
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
		parameterNamesArr[0] = "InternetGatewayDevice.IPPingDiagnostics.SuccessCount";
		parameterNamesArr[1] = "InternetGatewayDevice.IPPingDiagnostics.FailureCount";
		parameterNamesArr[2] = "InternetGatewayDevice.IPPingDiagnostics.AverageResponseTime";
		parameterNamesArr[3] = "InternetGatewayDevice.IPPingDiagnostics.MinimumResponseTime";
		parameterNamesArr[4] = "InternetGatewayDevice.IPPingDiagnostics.MaximumResponseTime";
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
	public DevRpc[] createDevRPCArray() {
		logger.debug("createDevRPCArray()");
		SetParameterValues setParameterValues = getSetParam();

		GetParameterValues getParameterValues = getResponseParam();

		DevRpc[] devRpcArr = new DevRpc[1];
		devRpcArr[0] = new DevRpc();
		devRpcArr[0].devId = deviceId;
		Rpc[] rpcArr = new Rpc[2];
		rpcArr[0] = new Rpc();
		rpcArr[0].rpcId = "1";
		rpcArr[0].rpcName = "SetParameterValues";
		rpcArr[0].rpcValue = setParameterValues.toRPC();
		
		rpcArr[1] = new Rpc();
		rpcArr[1].rpcId = "2";
		rpcArr[1].rpcName = "GetParameterValues";
		rpcArr[1].rpcValue = getParameterValues.toRPC();
		
		devRpcArr[0].rpcArr = rpcArr;
		logger.debug(rpcArr[1].rpcValue);
		return devRpcArr;
	}


	/**
	 * ping检测调用方法
	 * 
	 * @param
	 * @author Jason(3412)
	 * @date 2009-6-16
	 * @return void
	 */
	public void ping(String gw_type) {
		int diagResult = -9;
		logger.debug("ping()");
		String rpcRes = getRespStr(gw_type);
		// 判断返回是否错误
		diagResult = getFlag();
		if (diagSeccuss(diagResult)) {
			// 获取ping测试结构Map
			Map<String, String> pingMap = getDevParamMap(rpcRes);
			if (null != pingMap) {
				pingObj
						.setSuccessCount(Integer
								.valueOf(pingMap
										.get("InternetGatewayDevice.IPPingDiagnostics.SuccessCount")));
				pingObj
						.setFailureCount(Integer
								.valueOf(pingMap
										.get("InternetGatewayDevice.IPPingDiagnostics.FailureCount")));
				pingObj
						.setAverageResponseTime(Integer
								.valueOf(pingMap
										.get("InternetGatewayDevice.IPPingDiagnostics.AverageResponseTime")));
				pingObj
						.setMinimumResponseTime(Integer
								.valueOf(pingMap
										.get("InternetGatewayDevice.IPPingDiagnostics.MinimumResponseTime")));
				pingObj
						.setMaximumResponseTime(Integer
								.valueOf(pingMap
										.get("InternetGatewayDevice.IPPingDiagnostics.MaximumResponseTime")));
				pingObj.setSuccess(true);
			} else {
				pingObj.setSuccess(false);
				pingObj.setFaultCode(-9);
			}
		}else{
			pingObj.setSuccess(false);
			pingObj.setFaultCode(diagResult);
		}

	}

	public PingObject getPingObj() {
		return pingObj;
	}

	public void setPingObj(PingObject pingObj) {
		this.pingObj = pingObj;
	}

}
