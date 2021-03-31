package com.linkage.module.gtms.stb.resource.serv;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.gtms.stb.resource.dao.PreMacDeviceDAO;
import com.linkage.module.gwms.util.StringUtil;

/**
 * 机顶盒MAC前缀BIO
 * @author jiafh
 *
 */
public class PreMacDeviceBIO 
{
	private static Logger logger = LoggerFactory.getLogger(PreMacDeviceBIO.class);
	
	private PreMacDeviceDAO dao;
	
	
	/**
	 * 查询厂商列表
	 */
	public List<Map<String,String>> querVentorList(){
		logger.debug("PreMacDeviceBIO ==> querVentorList()");
		return dao.queryVendor(null);
	}
	
	public String getVendors()
	{
		logger.debug("PreMacDeviceBIO ==> getVendors()");
		
		String vendors = "";
		List<Map<String,String>> vendorList = dao.queryVendor(null);
		if(null != vendorList && !vendorList.isEmpty() && null != vendorList.get(0)){
			for(Map<String,String> vendorsMap : vendorList){
				if(StringUtil.IsEmpty(vendors)){
					vendors += StringUtil.getStringValue(vendorsMap.get("vendor_id")) + "$" + vendorsMap.get("vendor_add");
				}else{
					vendors += "#" + StringUtil.getStringValue(vendorsMap.get("vendor_id")) + "$" + vendorsMap.get("vendor_add");
				}
			}
		}
		return vendors;
	}
	
	/**
	 * 根据厂商ID查询设备型号
	 */
	public String getDeviceModelS(String vendorId)
	{
		logger.debug("PreMacDeviceBIO ==> getDeviceModelS({})",vendorId);
		
		String deviceModelS = "";
		List<Map<String,String>> deviceModelList = dao.getDeviceModelList(vendorId);
		if(null != deviceModelList && !deviceModelList.isEmpty() && null != deviceModelList.get(0)){
			for(Map<String,String> deviceModelMap : deviceModelList){
				if(StringUtil.IsEmpty(deviceModelS)){
					deviceModelS += deviceModelMap.get("device_model_id") + "$" + deviceModelMap.get("device_model");
				}else{
					deviceModelS += "#" + deviceModelMap.get("device_model_id") + "$" + deviceModelMap.get("device_model");
				}
			}
		}
		return deviceModelS;
	}
	
	/**
	 * 根据厂商ID、型号ID查询设备软件版本
	 */
	public String getSoftwareversionS(String vendorId,String deviceModelId)
	{		
		logger.debug("PreMacDeviceBIO ==> getSoftwareversionS({},{})",vendorId,deviceModelId);
		
		String softwareversionS = "";
		List<Map<String,String>> softwareversionList = dao.getSoftwareversionS(vendorId, deviceModelId);
		if(null != softwareversionList && !softwareversionList.isEmpty() && null != softwareversionList.get(0)){
			for(Map<String,String> softwareversionMap : softwareversionList){
				if(StringUtil.IsEmpty(softwareversionS)){
					softwareversionS += softwareversionMap.get("softwareversion");
				}else{
					softwareversionS += "#" + softwareversionMap.get("softwareversion");
				}
				
			}
		}
		return softwareversionS;
		
	}
	
	/**
	 * 根据厂商ID、设备型号ID、软件版本查询设备硬件版本
	 */
	public String getHardwareversionS(String vendorId,String deviceModelId,String softwareversion)
	{		
		logger.debug("PreMacDeviceBIO ==> getHardwareversionS({},{},{})",vendorId,deviceModelId,softwareversion);
		
		String hardwareversionS = "";
		List<Map<String,String>> hardwareversionList = dao.getHardwareversionS(vendorId, deviceModelId, softwareversion);
		if(null != hardwareversionList && !hardwareversionList.isEmpty() && null != hardwareversionList.get(0)){
			for(Map<String,String> hardwareversionMap : hardwareversionList){
				if(StringUtil.IsEmpty(hardwareversionS)){
					hardwareversionS += hardwareversionMap.get("hardwareversion");
				}else{
					hardwareversionS += "#" + hardwareversionMap.get("hardwareversion");
				}
				
			}
		}
		return hardwareversionS;
	}
	
	/**
	 * 查询MAC前缀和设备绑定关系，分页
	 */
	@SuppressWarnings("rawtypes")
	public List<Map> queryMacDevice(int curPage_splitPage,int num_splitPage,String preMac,String vendorId,
			String deviceModelId,String hardwareversion,String softwareversion){
		
		return dao.queryMacDevicetypeListById(curPage_splitPage, num_splitPage,
				preMac, vendorId, deviceModelId, hardwareversion, softwareversion);
	}
	
	/**
	 * 查询MAC前缀和设备绑定关系数量
	 */
	public int queryMacDeviceCount(String preMac,String vendorId,String deviceModelId,String hardwareversion,String softwareversion)
	{
		List<Map<String,String>> macDeviceList = dao.queryMacDevicetypeListById(preMac,
				vendorId, deviceModelId, hardwareversion, softwareversion);
		if(null != macDeviceList && !macDeviceList.isEmpty() && null != macDeviceList.get(0)){
			return macDeviceList.size();
		}
		return 0;
	}
	
	/**
	 * 计算最大页数
	 */
	public int queryMacDeviceCout(int curPage_splitPage,int num_splitPage,int total)
	{
		int maxPage = 1;
		if (total % num_splitPage == 0){
			maxPage = total / num_splitPage;
		}else{
			maxPage = total / num_splitPage + 1;
		}
		return maxPage;
	}
	
	/**
	 * 校验MAC前缀和设备绑定关系是否存在，1：不存在，2：存在
	 */
	public String validateMacDeviceType(String preMac,String preMacId)
	{
		logger.debug("PreMacDeviceBIO ==> validateMacDeviceType({},{},{},{},{})",preMac,preMacId);
		// 查询版本信息
		List<Map<String,String>> macDeviceTypeList = dao.queryMacDevicetypeListByPreMac(preMac,preMacId);
		if(null != macDeviceTypeList && !macDeviceTypeList.isEmpty() && null != macDeviceTypeList.get(0)){
			return "2";
		}	
		return "1";
	}
	
	/**
	 * 新增前缀MAC和设备信息绑定
	 * @param preMac2 
	 * @param user_id 
	 */
	public void addMacDeviceType(long user_id,String user_ip,String vendor_id,String deviceModelId,
			String softwareversion,String hardwareversion,String preMac)
	{
		logger.debug("PreMacDeviceBIO ==> addMacDeviceType({},{},{},{},{})",vendor_id,deviceModelId,softwareversion,hardwareversion,preMac);
		
		int devicetypeId = 0;
		// 查询版本是否存在
		List<Map<String,String>> deviceTypeList = dao.queryDevicetypeInfo(vendor_id, deviceModelId, hardwareversion, softwareversion);
		if(null != deviceTypeList && !deviceTypeList.isEmpty() && null != deviceTypeList.get(0)){
			devicetypeId = StringUtil.getIntegerValue(deviceTypeList.get(0).get("devicetype_id"));	
		}
		
		String rs=dao.addMacDevicetype(devicetypeId, preMac);
		dao.addLogInsert(user_id,user_ip,vendor_id,devicetypeId,preMac,rs);
	}
	
	/**
	 * 删除MAC前缀和设备信息的关系
	 */
	public void deleteMacDeviceType(long user_id,String user_ip,String preMacId){
		logger.debug("PreMacDeviceBIO ==> deleteMacDeviceType({})",preMacId);
		Map<String,String> map=dao.getMacDevicetype(preMacId);
		dao.deleteMacDevicetype(preMacId);
		dao.addLogDelete(user_id,user_ip,preMacId,map);
	}
	
	/**
	 * 更新MAC前缀和设备信息的关系
	 */
	public void editMacDeviceType(long user_id,String user_ip,String vendorId,String deviceModelId,
			String softwareversion,String hardwareversion,String preMac,String preMacId)
	{
		logger.debug("PreMacDeviceBIO ==> editMacDeviceType({},{},{},{},{},{})",vendorId,deviceModelId,softwareversion,hardwareversion,preMac,preMacId);

		int devicetypeId = 0;
		// 查询版本是否存在
		List<Map<String,String>> deviceTypeList = dao.queryDevicetypeInfo(vendorId, deviceModelId, hardwareversion, softwareversion);
		if(null != deviceTypeList && !deviceTypeList.isEmpty() && null != deviceTypeList.get(0)){
			devicetypeId = StringUtil.getIntegerValue(deviceTypeList.get(0).get("devicetype_id"));	
		}
		
		Map<String,String> map=dao.getMacDevicetype(preMacId);
		dao.updateMacDevicetype(preMacId, preMac, devicetypeId);
		dao.addLogUpdate(user_id,user_ip,preMacId,preMac,devicetypeId,map);
	}
	

	public PreMacDeviceDAO getDao() {
		return dao;
	}

	public void setDao(PreMacDeviceDAO dao) {
		this.dao = dao;
	}
}
