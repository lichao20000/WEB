package com.linkage.module.gwms.util.corba;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ACS.DevRpc;
import ACS.Rpc;

import com.ailk.tr069.devrpc.obj.rpc.DevRpcCmdOBJ;
import com.linkage.litms.acs.soap.service.FactoryReset;

/**
 * @author Jason(3412)
 * @date 2009-7-1
 */
public class DevFactoryReset {

	private static Logger logger = LoggerFactory
			.getLogger(DevFactoryReset.class);
	
	/**
	 * 恢复出厂设置操作
	 * 
	 * @param
	 * @author Jason(3412)
	 * @date 2009-7-1
	 * @return
	 *            <li>0,1:成功</li>
	 *            <li>-7:系统参数错误</li>
	 *            <li>-6:设备正被操作</li>
	 *            <li>-1:设备连接失败</li>
	 *            <li>-9:系统内部错误</li>
	 */
	public static int reset(String deviceId, String gw_type) {
		int flag = -9;
		logger.debug("device factoryReset. deviceId:" + deviceId);
		ACSCorba acsCorba = new ACSCorba(gw_type);
		FactoryReset factoryReset = new FactoryReset();
		DevRpc[] devRPCArr = new DevRpc[1];
		devRPCArr[0] = new DevRpc();
		devRPCArr[0].devId = deviceId;
		Rpc[] rpcArr = new Rpc[1];
		rpcArr[0] = new Rpc();
		rpcArr[0].rpcId = "1";
		rpcArr[0].rpcName = "FactoryReset";
		rpcArr[0].rpcValue = factoryReset.toRPC();
		devRPCArr[0].rpcArr = rpcArr;

		List<DevRpcCmdOBJ> devRPCRep = null;
		devRPCRep = acsCorba.execRPC(deviceId, devRPCArr);
		if (null != devRPCRep) {
			if (null != devRPCRep.get(0)) {
				flag = devRPCRep.get(0).getStat();
			} else {
				logger.warn("ACS Reponse devRPCRep.get(0) is NULL...");
			}
		} else {
			logger.warn("ACS Reponse DevRPCRep is NULL...");
		}
		// oui-device_serialnumber
//		String device_name = devRPCRep[0].os;
//		String rpcResp = "";
//		flag = SuperAcsRpcInvoke.fault(rpcResp);
//		if (SuperAcsRpcInvoke.diagSeccuss(flag)) {
//			FactoryResetResponse factoryResetResponse = new FactoryResetResponse();
//			SoapOBJ soapOBJ = XML.getSoabOBJ(XML.CreateXML(rpcResp));
//	        if (soapOBJ == null) {
//	        	return flag;
//	        }
//			factoryResetResponse = XmlToRpc.FactoryResetResponse(soapOBJ.getRpcElement());
//			if (null == factoryResetResponse) {
//				logger.warn("Reset fail. 恢复出厂设置失败");
//				flag = -9;
//				return flag;
//			} else {
//				logger.warn("Reset success. 恢复出厂设置成功");
//				flag = 1;
//				return flag;
//			}
//		}
		return flag;
	}
}
