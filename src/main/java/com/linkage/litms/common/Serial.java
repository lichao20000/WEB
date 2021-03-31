package com.linkage.litms.common;

import java.util.Map;

import com.linkage.litms.common.database.DataSetBean;

public class Serial {

	public Serial() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * 获取设备资源表中最小可用设备ID
	 * 
	 * @param number
	 * @return int
	 */
	public static int getDeviceSerial(int number) {
		int serial = -1;
		Map map = DataSetBean.getRecord("{call maxDeviceIdProc (" + number
				+ ")}");
		serial = Integer.parseInt(String.valueOf(map.get("serial")));

		// clear
		map = null;

		return serial;
	}

	/**
	 * 获取网络视图中网段最小可用ID
	 * 
	 * @param number
	 * @return int
	 */
	public static int getSegMentSerial(int number) {
		int serial = -1;
		Map map = DataSetBean.getRecord("{call maxSegMentIdProc (" + number
				+ ")}");
		serial = Integer.parseInt(String.valueOf(map.get("serial")));

		// clear
		map = null;

		return serial;
	}

	/**
	 * 获取VPN任务中最小可用ID
	 * 
	 * @param number
	 * @return int
	 */
	public static int getVpnTaskSerial(int number) {
		int serial = -1;
		Map map = DataSetBean.getRecord("{call maxVpnTaskIdProc (" + number
				+ ")}");
		serial = Integer.parseInt(String.valueOf(map.get("serial")));

		// clear
		map = null;

		return serial;
	}
	
	/**
	 * 获取VPN分任务的最小可用ID
	 * @param number
	 * @return
	 */
	public static int getSectVpnTaskSerial(int number){
		int serial = -1;
		Map map = DataSetBean.getRecord("{call maxVpnPingTaskIdProc (" + number
				+ ")}");
		serial = Integer.parseInt(String.valueOf(map.get("serial")));
		
        //clear
		map = null;

		return serial;
		
	}

	/**
	 * 获取Adsl用户表中最小可用user_id
	 * 
	 * @param gather_id
	 * @param number
	 * @return int
	 */
	public static int getAdslUserSerial(int gather_id, int number) {
		int serial = -1;
		Map map = DataSetBean.getRecord("{call maxAdslUserIdProc (" + gather_id
				+ "," + number + ")}");
		serial = Integer.parseInt(String.valueOf(map.get("serial")));

		// clear
		map = null;

		return serial;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
