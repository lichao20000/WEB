package com.linkage.module.itms.resource.bio;


import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.itms.resource.dao.GwVendorModelVersionDAO;

public class GwDeviceQueryBIO {
	//日志记录
	private static Logger logger = LoggerFactory.getLogger(GwDeviceQueryBIO.class);
	private GwVendorModelVersionDAO vmvDao = null;
	
	/**
	 * 查询设备厂商
	 * @return
	 */
	public String getVendor(){
		logger.debug("GwDeviceQueryBIO=>getVendor()");
		List list = vmvDao.getVendor();
		StringBuffer bf = new StringBuffer();
		for(int i=0;i<list.size();i++){
			Map map = (Map) list.get(i);
			if(i>0){
				bf.append("#");
			}
			bf.append(map.get("vendor_id"));
			bf.append("$");
			bf.append(map.get("vendor_add"));
			bf.append("(");
			bf.append(map.get("vendor_name"));
			bf.append(")");
		}
		return bf.toString();
	}
	
	/**
	 * 查询设备型号
	 * @param vendorId
	 * @return
	 */
	public String getDeviceModel(String vendorId){
		logger.debug("GwDeviceQueryBIO=>getDeviceModel(vendorId:{})",vendorId);
		List list = vmvDao.getDeviceModel(vendorId);
		StringBuffer bf = new StringBuffer();
		for(int i=0;i<list.size();i++){
			Map map = (Map) list.get(i);
			if(i>0){
				bf.append("#");
			}
			bf.append(map.get("device_model_id"));
			bf.append("$");
			bf.append(map.get("device_model"));
		}
		return bf.toString();
	}

	public GwVendorModelVersionDAO getVmvDao() {
		return vmvDao;
	}

	public void setVmvDao(GwVendorModelVersionDAO vmvDao) {
		this.vmvDao = vmvDao;
	}
}
