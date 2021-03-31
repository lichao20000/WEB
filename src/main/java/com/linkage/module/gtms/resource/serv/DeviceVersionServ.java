package com.linkage.module.gtms.resource.serv;

import java.util.List;
import java.util.Map;


/**
 * 
 * @author Administrator(工号) Tel:
 * @version 1.0
 * @since Apr 19, 2012 10:17:52 AM
 * @category com.linkage.module.gtms.resource.serv
 * @copyright 南京联创科技 网管科技部
 *
 */
public interface DeviceVersionServ {
	
	public List<Map> queryDeviceList(String vendorId, String deviceModelId,
			String devicetypeId, int rela_dev_type, String bssDevPort,int curPage_splitPage,
			int num_splitPage);
	
	public List<Map> queryDeviceList(String vendorId, String deviceModelId,
			String devicetypeId, int rela_dev_type, String bssDevPort);
	
	public int queryDeviceCount(String vendorId, String deviceModelId,
			String devicetypeId, int rela_dev_type, String bssDevPort,int curPage_splitPage,
			int num_splitPage);
	
	public List<Map> querySpecName();
}
