/**
 * 
 */
package com.linkage.litms.common.corba;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ACS.DevRpc;

import com.ailk.tr069.devrpc.dao.corba.AcsCorbaDAO;
import com.ailk.tr069.devrpc.obj.rpc.DevRpcCmdOBJ;
import com.linkage.litms.LipossGlobals;
import com.linkage.module.gwms.Global;
/**
 * corba 实时接口.
 * @author liuli
 * 		   Alex.Yan (yanhj@lianchuang.com)
 * 
 *
 */
public class DevRPCManager {

	/** log */
	private static Logger logger = LoggerFactory.getLogger(DevRPCManager.class);
	
	private String gw_type;
	
	/**
	 * 
	 */
	public DevRPCManager() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	public DevRPCManager(String gw_type) {
		super();
		// TODO Auto-generated constructor stub
		this.gw_type = gw_type;
	}
	

	
	public List<DevRpcCmdOBJ> execRPC(DevRpc[] devRPCRepObj,int rpcType)
	{
		int sleepTime = LipossGlobals.getSleepTime();
		try
		{
			logger.warn("每次调后台前，休眠" + sleepTime / 1000 + "秒...");
			Thread.sleep(sleepTime);
		}
		catch (InterruptedException e1)
		{
			e1.printStackTrace();
		}
		logger.warn("休眠结束，开始调用后台...");
		List<DevRpcCmdOBJ> ACSRpcOBJs = null;
		AcsCorbaDAO ascCorbaDAO = new AcsCorbaDAO(Global.getPrefixName(gw_type)+Global.SYSTEM_ACS);
		if (null != devRPCRepObj && devRPCRepObj.length > 0)
		{
			ACSRpcOBJs = ascCorbaDAO.execRPC(LipossGlobals.getClientId(),
					rpcType, Global.Priority_Hig, devRPCRepObj);
		}
		return ACSRpcOBJs;
	}

}
