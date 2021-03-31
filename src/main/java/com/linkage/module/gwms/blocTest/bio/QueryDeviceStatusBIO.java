package com.linkage.module.gwms.blocTest.bio;

import java.util.List;
import java.util.Map;

import com.linkage.module.gwms.blocTest.dao.DeviceStatusDAO;


public class QueryDeviceStatusBIO {


	private DeviceStatusDAO deviceStatusDAO;


	/**
	 * 分页查询设备记录
	 * @param infoType  用于判断是家庭网关：1 还是企业网关：2
	 * @param curPage_splitPage   当前页数
	 * @param num_splitPage  每页记录数
	 * @param curUser  
	 * @param vendor 设备厂商
	 * @param device_model  设备型号
	 * @param specversion  设备版本
	 * @param device_serialnumber  设备序列号
	 * @param city_id  设备属地
	 * @param loopback_ip  设备IP
	 * @return
	 */
	public List<Map> getDeviceList(String gw_type, String vendor, String device_model, String specversion, 
			                       String device_serialnumber, String city_id, String loopback_ip) {
		
		return deviceStatusDAO.getDeviceList(gw_type, vendor, device_model, specversion, device_serialnumber, city_id, loopback_ip);
	}

	public DeviceStatusDAO getDeviceStatusDAO() {
		return deviceStatusDAO;
	}

	public void setDeviceStatusDAO(DeviceStatusDAO deviceStatusDAO) {
		this.deviceStatusDAO = deviceStatusDAO;
	}


}
