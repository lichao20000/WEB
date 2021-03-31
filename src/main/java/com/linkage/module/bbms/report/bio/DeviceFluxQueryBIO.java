
package com.linkage.module.bbms.report.bio;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.bbms.report.dao.DeviceFluxQueryDAO;

/**
 * 端口流量按设备查询
 * 
 * @author onelinesky
 * @since 2011-1-15
 */
public class DeviceFluxQueryBIO {

	private static Logger logger = LoggerFactory.getLogger(DeviceFluxQueryBIO.class);
	private DeviceFluxQueryDAO deviceFluxQueryDao;

	/**
	 * @author onelinesky
	 * @date 2011-1-15
	 * @param
	 * @return List<Map>
	 */
	public List<Map<String,String>> deviceFluxQuery(String queryType,String cityId, String startDate, String endDate,
			String deviceSn, String loopbackIp, String username) {
		logger.debug("deviceFluxQuery()");
		
		String tableName = "";
		DateTimeUtil dt = new DateTimeUtil(startDate);
		tableName = "flux_day_stat_" + dt.getYear() + "_" + dt.getMonth();
		long _starttime = dt.getLongTime();
		long _endtime = new DateTimeUtil(endDate).getLongTime();
		
		int ret = deviceFluxQueryDao.isHaveTable(tableName);
		
		if(0==ret){
			return new ArrayList<Map<String,String>>();
		}
		List<String> _deviceList = null;
		if("device".equals(queryType)){
			_deviceList = deviceFluxQueryDao.getInfoByDevice(cityId, deviceSn, loopbackIp);
		}else{
			_deviceList = deviceFluxQueryDao.getInfoByUser(cityId, username);
		}
		if(0==_deviceList.size()){
			return new ArrayList<Map<String,String>>();
		}
		return deviceFluxQueryDao.getFlux(tableName, _deviceList, _starttime, _endtime);
	}

	/**
	 * @return the deviceFluxQueryDao
	 */
	public DeviceFluxQueryDAO getDeviceFluxQueryDao() {
		return deviceFluxQueryDao;
	}

	/**
	 * @param deviceFluxQueryDao the deviceFluxQueryDao to set
	 */
	public void setDeviceFluxQueryDao(DeviceFluxQueryDAO deviceFluxQueryDao) {
		this.deviceFluxQueryDao = deviceFluxQueryDao;
	}

	
}
