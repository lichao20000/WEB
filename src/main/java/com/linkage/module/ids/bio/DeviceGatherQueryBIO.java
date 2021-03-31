package com.linkage.module.ids.bio;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.ids.dao.DeviceGatherQueryDAO;

public class DeviceGatherQueryBIO {

	private static Logger logger = LoggerFactory.getLogger(DeviceGatherQueryBIO.class);
	private DeviceGatherQueryDAO dao; 
	/**
	public List getDeviceInfo(String serialnumber, String loid,
			String starttime1, String endtime1, String username,
			String voipusername,String routeInfo,String voiceRegistInfo) {
		logger.debug("getDeviceInfo({},{},{},{},{},{})",
				new Object[]{serialnumber,loid,starttime1,endtime1,username,voipusername});
		return dao.getDeviceInfo(serialnumber, loid, starttime1, endtime1, username, voipusername,routeInfo,voiceRegistInfo);
	}
	 **/
	public List getDeviceInfo(int curPage_splitPage,int num_splitPage,String starttime1, String endtime1, 
			String routeInfo,String voiceRegistInfo) {
		logger.debug("getDeviceInfo({},{},{},{},{},{})",
				new Object[]{starttime1,endtime1});
		return dao.getDeviceInfo(curPage_splitPage,num_splitPage,starttime1, endtime1,routeInfo,voiceRegistInfo);
	}
	
	
	public int getDeviceGatherCount(int num_splitPage, String starttime1,
			String endtime1,String routeInfo,String voiceRegistInfo) {
		// TODO Auto-generated method stub
		return dao.getDeviceGatherCount(num_splitPage,starttime1,endtime1,routeInfo,voiceRegistInfo);
	}
	
	public void setDao(DeviceGatherQueryDAO dao) {
		this.dao = dao;
	}

	
}
