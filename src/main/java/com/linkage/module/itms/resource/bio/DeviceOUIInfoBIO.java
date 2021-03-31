package com.linkage.module.itms.resource.bio;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.itms.resource.dao.DeviceOUIInfoDAO;

public class DeviceOUIInfoBIO {
	
	private static Logger logger = LoggerFactory.getLogger(DeviceOUIInfoBIO.class);
	
	private DeviceOUIInfoDAO dao;
	
	public List<Map> getDeviceOUIinfo(String oui, String vendor_name, int curPage_splitPage,int num_splitPage){
		logger.debug("DeviceOUIInfoBIO-getDeviceOUIinfo");
		return dao.getDeviceOUIinfo(oui, vendor_name, curPage_splitPage, num_splitPage);
	}
	
	public List<Map> getDeviceOUIinfo(String device_type_qry, String oui, String vendor_name,String vendor_name_stb, int curPage_splitPage,int num_splitPage){
		logger.debug("DeviceOUIInfoBIO-getDeviceOUIinfo");
		return dao.getDeviceOUIinfo(device_type_qry, oui, vendor_name, vendor_name_stb, curPage_splitPage, num_splitPage);
	}
	
	public int countDeviceOUIinfo(String oui, String vendor_name,int curPage_splitPage,int num_splitPage){
		logger.debug("DeviceOUIInfoBIO->countDeviceOUIinfo");
		return dao.countDeviceOUIinfo(oui, vendor_name, curPage_splitPage, num_splitPage);
	}
	
	public int countDeviceOUIinfo(String device_type_qry, String oui, String vendor_name,String vendor_name_stb,int curPage_splitPage,int num_splitPage){
		logger.debug("DeviceOUIInfoBIO->countDeviceOUIinfo");
		return dao.countDeviceOUIinfo(device_type_qry, oui, vendor_name, vendor_name_stb,curPage_splitPage, num_splitPage);
	}
	
	public List<Map<String,String>> getVendorMap(){
		logger.debug("DeviceOUIInfoBIO->getVendorMap");
		return dao.getVendorMap();
	}
	
	public List<Map<String,String>> getVendorMapStb(){
		logger.debug("DeviceOUIInfoBIO->getVendorMap");
		return dao.getVendorMapStb();
	}
	
	public List<Map<String,String>> getOUIMap(){
		logger.debug("DeviceOUIInfoBIO->getOUIMap");
		return dao.getOUIMap();
	}
	
	public int deleteOUI(String id, String deviceType){
		logger.debug("DeviceOUIInfoBIO->deleteOUI");
		return dao.deleteOUI(id,deviceType);
	}

	public int deleteSXLTOUI(String id){
		logger.debug("DeviceOUIInfoBIO->deleteSXLTOUI");
		return dao.deleteSXLTOUI(id);
	}

	public int addOUI(String ouiId, String ouiDesc, String vendorName, String remark ,String add_date, String device_model){
		logger.debug("DeviceOUIInfoBIO->addOUI");
		String id = dao.getId();
		int num = 1;
		boolean isNum = id.matches("[0-9]+");
		if(isNum){
			num = Integer.parseInt(id)+1;
		}
		return dao.addOUI(num, ouiId, ouiDesc, vendorName, remark, add_date, device_model);
	}
	
	public int editOUI(String id, String ouiId, String ouiDesc, String vendorName, String remark ,String add_date, String device_model, String deviceType){
		logger.debug("DeviceOUIInfoBIO->editOUI");
		//vendor是vendor_add(vendor_name)
		if(vendorName.contains("(")){
			vendorName = vendorName.substring(vendorName.indexOf("(")).replace("(", "").replace(")", "");
		}
		return dao.editOUI(id, ouiId, ouiDesc, vendorName, remark, add_date, device_model, deviceType);
	}

	public DeviceOUIInfoDAO getDao() {
		return dao;
	}

	public void setDao(DeviceOUIInfoDAO dao) {
		this.dao = dao;
	}

	public int addSXLTOUI(String ouiId, String ouiDesc, String vendorName, String remark, String add_date, String deviceModel, String deviceType, String deviceModelId, String vendorId) {
		int result = -1;
		//vendor是vendor_add(vendor_name)
		if(vendorName.contains("(")){
			vendorName = vendorName.substring(vendorName.indexOf("(")).replace("(", "").replace(")", "");
		}
		
		// 校验是否已存在
		int isLink = dao.ouiIsExist(vendorName, deviceModel, ouiId, deviceType);
		// 如果已经激活
		if(isLink == 1) {
			result = 2;
		}else if(isLink == -1) {
			String id = dao.getId(deviceType);
			int num = 1;
			boolean isNum = id.matches("[0-9]+");
			if(isNum){
				num = Integer.parseInt(id)+1;
			}
			result = dao.addSXLTOUI(num ,ouiId,ouiDesc,vendorName,remark,add_date,deviceModel,deviceType,deviceModelId,vendorId);
		}
		return result;
	}
}
