package com.linkage.litms.common.corba.interfacecontrol;

import java.util.HashMap;

/**
 * 
 * @author benny
 * @SNMP 设备Ping corba接口
 *
 */

public class SnmpGwCheckInterface extends BaseInterface {

	static protected SnmpGwCheckInterface Instance = null;

	private SnmpGwCheckInterface() {
		SetProcessName("SnmpGwCheck");
		InitProcessList();
	}

	static public SnmpGwCheckInterface GetInstance() {
		if (Instance == null) {
			Instance = new SnmpGwCheckInterface();
		}
		return Instance;
	}

	/**
	 * SNMP设备的Ping
	 * @author lizj （5202）
	 * @param setData_arr
	 * @param length
	 * @param device_id
	 * @return
	 */
	public SnmpGwCheck.PingData[] SnmpPing(SnmpGwCheck.PingDev[] devData_Arr,String device_id) {
		
		HashMap deviceInfo = getDeviceInfo(device_id);
		if (deviceInfo == null)
			return new SnmpGwCheck.PingData[0];

		GatherProcess process = SearchProcessByObject(deviceInfo);
		if (process == null)
			return new SnmpGwCheck.PingData[0];
		if (process.Manager == null) {
			process.InitManager();
		}

		SnmpGwCheck.PingData[] pingResultData;
		
		try {
			
			pingResultData = ((SnmpGwCheck.I_ConfigModified) process.Manager).SnmpPing(devData_Arr);
		} catch (Exception e) {
			e.printStackTrace();
			pingResultData = new SnmpGwCheck.PingData[0];
			process.Manager = null;
		}
		return pingResultData;
	}
	
}
