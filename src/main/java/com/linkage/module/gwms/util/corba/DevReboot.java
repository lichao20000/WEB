
package com.linkage.module.gwms.util.corba;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ACS.DevRpc;
import ACS.Rpc;

import com.ailk.tr069.devrpc.dao.corba.AcsCorbaDAO;
import com.ailk.tr069.devrpc.obj.rpc.DevRpcCmdOBJ;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.acs.soap.service.FactoryReset;
import com.linkage.litms.acs.soap.service.Reboot;
import com.linkage.litms.common.corba.DevRPCManager;
import com.linkage.module.gwms.Global;

/**
 * @author Jason(3412)
 * @date 2009-7-1
 */
public class DevReboot
{

	private static Logger logger = LoggerFactory.getLogger(DevReboot.class);

	/**
	 * 重启操作
	 * 
	 * @param
	 * @author Jason(3412)
	 * @date 2009-7-1
	 * @return
	 */
	public static int reboot(String deviceId, String gw_type)
	{
		logger.debug("device reboot. deviceId:" + deviceId);
		DevRpc[] devRPCArr = new DevRpc[1];
		Reboot reboot = new Reboot();
		reboot.setCommandKey("Reboot");
		devRPCArr[0] = new DevRpc();
		devRPCArr[0].devId = deviceId;
		Rpc[] rpcArr = new Rpc[1];
		rpcArr[0] = new Rpc();
		rpcArr[0].rpcId = "1";
		rpcArr[0].rpcName = "Reboot";
		rpcArr[0].rpcValue = reboot.toRPC();
		devRPCArr[0].rpcArr = rpcArr;
		DevRPCManager devRPCManager = new DevRPCManager(gw_type);
		List<DevRpcCmdOBJ> devRPCRep = devRPCManager.execRPC(devRPCArr,
				Global.RpcCmd_Type);
		int flag = -9;
		if (devRPCRep == null || devRPCRep.size() == 0)
		{
			logger.warn("[{}]List<DevRpcCmdOBJ>返回为空！", deviceId);
		}
		else if (devRPCRep.get(0) == null)
		{
			logger.warn("[{}]DevRpcCmdOBJ返回为空！", deviceId);
		}
		else
		{
			flag = devRPCRep.get(0).getStat();
		}
		return flag;
	}
	
	
	
	/**
	 * 恢复出厂设置（机顶盒）
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
		logger.info("device reset. deviceId:" + deviceId);
		FactoryReset factoryReset = new FactoryReset();
		
		DevRpc[] devRPCArr = new DevRpc[1];
		devRPCArr[0] = new DevRpc();
		devRPCArr[0].devId = deviceId;
		Rpc rpc = new Rpc();
		rpc.rpcId = "1";
		rpc.rpcName = "FactoryReset";
		rpc.rpcValue = factoryReset.toRPC();
		devRPCArr[0].rpcArr = new Rpc[] { rpc };
		
		List<DevRpcCmdOBJ> list = new AcsCorbaDAO(Global.getPrefixName(gw_type)
				+ Global.SYSTEM_ACS).execRPC(LipossGlobals.getClientId(),
				Global.RpcCmd_Type, Global.Priority_Hig, devRPCArr);
		
		logger.info("device reset. deviceId:" + deviceId+"|aaaaaa");
		if (null == list || list.isEmpty())
		{
			logger.info("device reset. deviceId:" + deviceId+"|bbbbbb");
			flag = -9;
		    return flag;
		}
		logger.info("device reset. deviceId:" + deviceId+"|{}",flag);
		flag = list.get(0).getStat();
		logger.info("device reset. deviceId:" + deviceId+"|{}",flag);
		return flag;
		
	}
	
}
