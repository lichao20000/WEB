package com.linkage.module.gwms.cao.gw;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ACS.DevRpc;
import ACS.Rpc;

import com.linkage.litms.acs.soap.service.GetParameterValues;
import com.linkage.litms.acs.soap.service.SetParameterValues;
import com.linkage.module.gwms.cao.SuperAcsRpcInvoke;
import com.linkage.module.gwms.cao.gw.interf.IParamTree;
import com.linkage.module.gwms.obj.gw.DeviceWireInfoObj;

/**
 * @author Jason(3412)
 * @date 2009-7-6
 */
public class WireInfoCAO extends SuperAcsRpcInvoke implements IParamTree {
	
	private static Logger logger = LoggerFactory.getLogger(WireInfoCAO.class);
	
	private DeviceWireInfoObj wireInfoObj;
	/* (non-Javadoc)
	 * @see com.linkage.module.gwms.cao.SuperAcsRpcInvoke#createDevRPCArray()
	 */
	@Override
	public DevRpc[] createDevRPCArray() {
		logger.debug("createDevRPCArray()");
		GetParameterValues getParameterValues = getResponseParam();

		DevRpc[] devRpcArr = new DevRpc[1];
		devRpcArr[0] = new DevRpc();
		devRpcArr[0].devId = deviceId;
		Rpc[] rpcArr = new Rpc[1];
		rpcArr[0] = new Rpc();
		rpcArr[0].rpcId = "1";
		rpcArr[0].rpcName = "getParameterValues";
		rpcArr[0].rpcValue = getParameterValues.toRPC();
		
		devRpcArr[0].rpcArr = rpcArr;
		logger.debug(rpcArr[0].rpcValue);
		return devRpcArr;
	}

	/* (non-Javadoc)
	 * @see com.linkage.module.gwms.cao.SuperAcsRpcInvoke#getResponseParam()
	 */
	@Override
	public GetParameterValues getResponseParam() {
		GetParameterValues getParameterValues = new GetParameterValues();
//		String[] parameterNamesArr = new String[8];
//		parameterNamesArr[0] = "InternetGatewayDevice.WANDevice.i.WANDSLInterfaceConfig.Status";
//		parameterNamesArr[1] = "InternetGatewayDevice.WANDevice.i.WANDSLInterfaceConfig.ModulationType";
//		parameterNamesArr[2] = "InternetGatewayDevice.WANDevice.i.WANDSLInterfaceConfig.UpstreamAttenuation";
//		parameterNamesArr[3] = "InternetGatewayDevice.WANDevice.i.WANDSLInterfaceConfig.DownstreamAttenuation";
//		parameterNamesArr[4] = "InternetGatewayDevice.WANDevice.i.WANDSLInterfaceConfig.UpstreamMaxRate";
//		parameterNamesArr[5] = "InternetGatewayDevice.WANDevice.i.WANDSLInterfaceConfig.DownstreamMaxRate";
//		parameterNamesArr[6] = "InternetGatewayDevice.WANDevice.i.WANDSLInterfaceConfig.DataPath";
//		parameterNamesArr[7] = "InternetGatewayDevice.WANDevice.i.WANDSLInterfaceConfig.InterleaveDepth";
//		getParameterValues.setParameterNames(parameterNamesArr);

		return getParameterValues;
	}

	/* (non-Javadoc)
	 * @see com.linkage.module.gwms.cao.SuperAcsRpcInvoke#getSetParam()
	 * 线路信息不需要设置设备结点参数
	 */
	@Override
	public SetParameterValues getSetParam() {
		return null;
	}

	/**
	 * 获取设备的线路信息
	 * 
	 * @param 
	 * @author Jason(3412)
	 * @date 2009-6-15
	 * @return void
	 */
	public void gatherDeviceWireInfo() {
		//获取线路状态信息Map
//		Map<String,String> wireInfoMap = this.getDevParamMap();
//		
//		wireInfoObj.setWireStatus(wireInfoMap.get("InternetGatewayDevice.WANDevice.i.WANDSLInterfaceConfig.Status"));
//		wireInfoObj.setModulationType(wireInfoMap.get("InternetGatewayDevice.WANDevice.i.WANDSLInterfaceConfig.ModulationType"));
//		wireInfoObj.setUpstreamMaxRate(Integer.valueOf(wireInfoMap.get("InternetGatewayDevice.WANDevice.i.WANDSLInterfaceConfig.UpstreamMaxRate")));
//		wireInfoObj.setDownstreamMaxRate(Integer.valueOf(wireInfoMap.get("InternetGatewayDevice.WANDevice.i.WANDSLInterfaceConfig.DownstreamMaxRate")));
//		wireInfoObj.setUpstreamAttenuation(Integer.valueOf(wireInfoMap.get("InternetGatewayDevice.WANDevice.i.WANDSLInterfaceConfig.UpstreamAttenuation")));
//		wireInfoObj.setDownstreamAttenuation(Integer.valueOf(wireInfoMap.get("InternetGatewayDevice.WANDevice.i.WANDSLInterfaceConfig.DownstreamAttenuation")));
//		wireInfoObj.setDataPath(wireInfoMap.get("InternetGatewayDevice.WANDevice.i.WANDSLInterfaceConfig.DataPath"));
//		wireInfoObj.setInterleaveDepth(wireInfoMap.get("InternetGatewayDevice.WANDevice.i.WANDSLInterfaceConfig.InterleaveDepth"));
	}

	public DeviceWireInfoObj getWireInfoObj() {
		return wireInfoObj;
	}

	public void setWireInfoObj(DeviceWireInfoObj wireInfoObj) {
		this.wireInfoObj = wireInfoObj;
	}
	
}
