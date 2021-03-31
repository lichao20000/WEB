package com.linkage.litms.resource;

import java.util.List;
import java.util.Map;

import com.linkage.commons.db.DBUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ACS.DevRpc;
import ACS.Rpc;

import com.ailk.tr069.devrpc.obj.rpc.DevRpcCmdOBJ;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.corba.DevRPCManager;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.module.gwms.Global;

/**
 * 启动线程进行设备的测试连接
 * @author chenzm
 *
 */

public class SimpleThread extends Thread {
	/** log */
	private static Logger logger = LoggerFactory.getLogger(SimpleThread.class);
	boolean runningFlag = false;
	String username = "";
	String city_id = "";
	
	public SimpleThread(){
		super();
	}
	
	public void run(){
		while (true){
			try {
				if (!runningFlag){
					sleep(60000);
				}
				else{
					logger.warn("启动线程！");
					
					//测试连接
					checkDevice();
					
					runningFlag = false;
				}
			}
			catch (Exception e){
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 返回当前线程状态
	 */
	public boolean isRunning()
	{
		return runningFlag;
	}
	
	/**
	 * 设置线程状态
	 */
	public synchronized void setRunning(boolean flag){
		runningFlag = flag;
		if(flag){
			this.notify();
		}
	}
	
	/**
	 * 设置用户帐号
	 */
	public void setUsername(String str){
		username = str;
	}
	
	/**
	 * 设置属地
	 */
	public void setCityID(String str){
		city_id = str;
	}
	
	/**
	 * 根据用户帐号进行设备的测试连接
	 */
	private void checkDevice(){

		logger.warn("开始测试连接======>username: " + username);
		DevRpc[] devRPCArr = new DevRpc[1];
		
		//根据用户帐号查询设备信息
		String strSQL1 = "select a.* from tab_gw_device a,tab_hgwcustomer b" 
						+ " where a.device_id = b.device_id" 
						+ " and a.device_status = 1 and b.user_state = '1' and b.username = '" 
						+ username + "'";
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			strSQL1 = "select a.device_id from tab_gw_device a,tab_hgwcustomer b"
					+ " where a.device_id = b.device_id"
					+ " and a.device_status = 1 and b.user_state = '1' and b.username = '"
					+ username + "'";
		}
		PrepareSQL psql = new PrepareSQL(strSQL1);
		psql.getSQL();
		Cursor cursor1 = DataSetBean.getCursor(strSQL1);
		Map fields1 = cursor1.getNext();
		
		//设备不存在则直接返回
		if (fields1 == null){
			logger.warn("当前用户设备不存在 username：" + username);
			return;
		}
		
		String device_id = (String) fields1.get("device_id");
		
		//得到设备类型
		String gw_type = LipossGlobals.getGw_Type(device_id);

		Rpc[] rpcArr = new Rpc[1];
		rpcArr[0] = new Rpc();
		rpcArr[0].rpcId = "1";
		rpcArr[0].rpcName = "";
		rpcArr[0].rpcValue = "";
		//接口参数
		devRPCArr[0] = new DevRpc();
		devRPCArr[0].devId = device_id;

		devRPCArr[0].rpcArr = rpcArr;
		
		List<DevRpcCmdOBJ> devRPCRep = null;
		DevRPCManager devRPCManager = new DevRPCManager(gw_type+"");
		
		//调用接口
		devRPCRep = devRPCManager.execRPC(devRPCArr, Global.RpcTest_Type);


		//获取返回信息
		boolean flag = false;
		if (devRPCRep != null) {
			// String device_name = devRPCRep[0].os;
			if (devRPCRep.get(0) != null) {
				int setRes = devRPCRep.get(0).getStat();
				logger.warn("setRes===============>" + setRes);

				if (setRes != 1) {
					flag = false;
				} else {
					flag = true;
				}
			} else {
				flag = false;
			}
		} else {
			flag = false;
		}
		logger.warn("测试连接结束======>username:" + username + "  flag: " + flag + " city_id:" + city_id);
		
		//测试连接后进行数据库操作
		CheckUserThread.informDB(username, city_id, flag);
	}
	
}
