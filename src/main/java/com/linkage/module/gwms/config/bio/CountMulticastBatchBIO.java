package com.linkage.module.gwms.config.bio;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.config.dao.CountMulticastBatchDAO;

/**
 * 
 * @author wangyan10(Ailk NO.76091)
 * @version 1.0
 * @since 2018-12-26
 */
public class CountMulticastBatchBIO {
		
	//	 日志记录
	private static Logger logger = LoggerFactory.getLogger(CountMulticastBatchBIO.class);
	
	// dao
	private CountMulticastBatchDAO dao;
	private int maxPage_splitPage;

	/**
	 * 查询列表
	 * @param
	 * @param current_user
	 * @param device_serialnumber
	 * @param cityId
	 * @param vendorId
	 * @param deviceModelId
	 * @param deviceTypeId
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @param cpe_allocatedstatus
	 * @return
	 */
	public List<Map> queryMulticastBatchList(UserRes current_user, String device_serialnumber, String cityId, String vendorId, String deviceModelId, String deviceTypeId, int curPage_splitPage, int num_splitPage, String cpe_allocatedstatus) {
		
		logger.debug("queryMulticastBatchList({},{},{},{},{},{})",new Object[]{current_user, device_serialnumber, cityId, vendorId, deviceModelId, deviceTypeId});
		
		List<Map> list = dao.queryMulticastBatchList(current_user, device_serialnumber, cityId, vendorId, deviceModelId, deviceTypeId,curPage_splitPage, num_splitPage,cpe_allocatedstatus);
		int total = dao.countMulticastBatchList(current_user, device_serialnumber, cityId, vendorId, deviceModelId, deviceTypeId,cpe_allocatedstatus);
		maxPage_splitPage = (total + num_splitPage - 1) / num_splitPage;
		return list;
	}

	public CountMulticastBatchDAO getDao() {
		return dao;
	}

	public void setDao(CountMulticastBatchDAO dao) {
		this.dao = dao;
	}

	public int getMaxPage_splitPage() {
		return maxPage_splitPage;
	}

	public void setMaxPage_splitPage(int maxPage_splitPage) {
		this.maxPage_splitPage = maxPage_splitPage;
	}

	public int countMulticastBatchList(UserRes current_user,
			String device_serialnumber, String cityId, String vendorId,
			String deviceModelId, String deviceTypeId, String cpe_allocatedstatus) {
		return dao.countMulticastBatchList(current_user, device_serialnumber, cityId, vendorId, deviceModelId, deviceTypeId,cpe_allocatedstatus);
	}

	public List<Map> getMulticastBatchExcel(UserRes current_user, String device_serialnumber, String cityId, String vendorId, String deviceModelId, String deviceTypeId, String cpe_allocatedstatus) {
		
		logger.debug("getMulticastBatchExcel({},{},{},{},{},{})",new Object[]{current_user, device_serialnumber, cityId, vendorId, deviceModelId, deviceTypeId});
		
		List<Map> list = dao.getMulticastBatchExcel(current_user, device_serialnumber, cityId, vendorId, deviceModelId, deviceTypeId,cpe_allocatedstatus);
		return list;
	}
	
	public List<Map> queryMulticastDownwardsList( UserRes curUser ,String cityId,String vendorId,String deviceModelId){
		List<Map> listMaps = dao.queryMulticastDownwardsList(curUser,cityId, vendorId, deviceModelId);
		return listMaps;
	}
	
	public List<Map> queryMulticastDownwardsListExcel( UserRes curUser ,String cityId,String vendorId,String deviceModelId){
		List<Map> listMaps = dao.queryMulticastDownwardsListExcel(curUser,cityId, vendorId, deviceModelId);
		return listMaps;
	}
	
	public List<Map> multicastDownwardsDetail(UserRes curUser,String type, String cityId,String vendorId,String deviceModelId,int curPage_splitPage, int num_splitPage){
		List<Map> listMaps = dao.multicastDownwardsDetail(curUser, type, cityId, vendorId, deviceModelId, curPage_splitPage, num_splitPage);
		int total = dao.multicastDownwardsDetailCount(curUser, type, vendorId, deviceModelId, cityId);
		maxPage_splitPage = (total + num_splitPage - 1) / num_splitPage;
		return listMaps;
	}
	 
	public List<Map> multicastDownwardsDetailExcel(UserRes curUser,String type,  String cityId,String vendorId,String deviceModelId){
		List<Map> listMaps = dao.multicastDownwardsDetailExcel(curUser,type,cityId,vendorId,deviceModelId);
		return listMaps;
	}
} 