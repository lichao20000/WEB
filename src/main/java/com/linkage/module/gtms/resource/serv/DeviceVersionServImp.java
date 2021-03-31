package com.linkage.module.gtms.resource.serv;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.gtms.resource.dao.DeviceVersionDAO;

/**
 * 
 * @author Administrator(工号) Tel:
 * @version 1.0
 * @since Apr 19, 2012 11:03:57 AM
 * @category com.linkage.module.gtms.resource.serv
 * @copyright 南京联创科技 网管科技部
 *
 */
public class DeviceVersionServImp implements DeviceVersionServ{
	
	private static Logger logger = LoggerFactory.getLogger(DeviceVersionServImp.class);
	private DeviceVersionDAO dao;
	
	public List<Map> queryDeviceList(String vendorId, String deviceModelId,
			String devicetypeId, int rela_dev_type, String bssDevPort,int curPage_splitPage,
			int num_splitPage) {
		
		logger.debug("DeviceVersionServImp==>queryDeviceList({},{},{},{},{},{})",
				new Object[] { vendorId, deviceModelId, devicetypeId,bssDevPort, curPage_splitPage, num_splitPage });
		
		return dao.queryDeviceList(vendorId, deviceModelId, devicetypeId, rela_dev_type, bssDevPort, curPage_splitPage, num_splitPage);
		
	}
	
	public List<Map> queryDeviceList(String vendorId, String deviceModelId,
			String devicetypeId, int rela_dev_type, String bssDevPort) {
		
		logger.debug("DeviceVersionServImp==>queryDeviceList({},{},{},{},{},{})",
				new Object[] { vendorId, deviceModelId, devicetypeId,bssDevPort });
		
		return dao.queryDeviceList(vendorId, deviceModelId, devicetypeId, rela_dev_type, bssDevPort);
		
	}
	
	
	public int queryDeviceCount(String vendorId, String deviceModelId,
			String devicetypeId, int rela_dev_type,String bssDevPort, int curPage_splitPage,
			int num_splitPage) {
		
		logger.debug("DeviceVersionServImp==>queryDeviceCount({},{},{},{},{},{})",
				new Object[] { vendorId, deviceModelId, devicetypeId, bssDevPort, curPage_splitPage,
						num_splitPage });
		
		return dao.queryDeviceCount(vendorId, deviceModelId, devicetypeId, rela_dev_type, bssDevPort,
				curPage_splitPage, num_splitPage);
	}
	
	public List<Map> querySpecName(){
		return dao.querySpecName();
	}



	
	public DeviceVersionDAO getDao() {
		return dao;
	}

	public void setDao(DeviceVersionDAO dao) {
		this.dao = dao;
	}
	
}
