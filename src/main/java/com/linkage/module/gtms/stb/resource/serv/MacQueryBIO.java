package com.linkage.module.gtms.stb.resource.serv;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.StringUtil;
import com.linkage.module.gtms.stb.resource.dao.MacQueryDAO;

/**
 * 
 * @author Administrator (Ailk No.)
 * @version 1.0
 * @since 2013-7-19
 * @category com.linkage.module.lims.stb.resource.bio
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 *
 */
public class MacQueryBIO
{
	private static Logger logger = LoggerFactory.getLogger(MacQueryBIO.class);
	private MacQueryDAO dao;
	
	public List<Map> getMacInfo(String cpe_mac,String vendor_name,String device_model, String supply_mode,int curPage_splitPage, int num_splitPage){
		logger.debug("MacQueryBIO-->getMacInfo");
		return dao.getMacInfo(cpe_mac, vendor_name, device_model, supply_mode, curPage_splitPage, num_splitPage);
	}
	
	public int getCount(String cpe_mac,String vendor_name,String device_model, String supply_mode,int curPage_splitPage, int num_splitPage ){
		logger.debug("MacQueryBIO--getCount");
		return dao.getCount(cpe_mac, vendor_name, device_model, supply_mode, curPage_splitPage, num_splitPage);
	}
	
	public String addMac(String orderId,String packageNo,String vendorName,String supplyMode,String deviceModel,String mac,String deviceSn,String cityId, String staffId){
		logger.debug("MacQueryBIO--addMac");
		if (dao.isMacExist(null, mac))
		{
			logger.info("Mac[{}] is already exists table[tab_devmac_init]", mac);
			return "MAC已经存在";
		}
		
		String deviceId = dao.getDeviceId();
		int num = 10000;
		if(!StringUtil.IsEmpty(deviceId)){
			num = Integer.parseInt(deviceId)+1;
		}
		return dao.insertMac(num, orderId, packageNo, vendorName, supplyMode, deviceModel, mac, deviceSn, cityId, staffId);
	}
	
	public String editMac(String orderId,String packageNo,String vendorName,String supplyMode,String deviceModel,String mac,String deviceSn,String deviceId,String cityId, String staffId){
		logger.debug("MacQueryBIO--editMac");
		if (dao.isMacExist(deviceId, mac))
		{
			logger.info("Mac[{}] is already exists table[tab_devmac_init]", mac);
			return "MAC已经存在";
		}
		int  result = dao.updateMac(orderId, packageNo, vendorName, supplyMode, deviceModel, mac, deviceSn, deviceId, cityId, staffId);
		if(1==result){
			return "修改成功";
		}else{
			return "修改失败";
		}
	}

	/**
	 * 查询设备厂商
	 * @return
	 */
	public String getVendorList(){
		List list=dao.getVendorList();
		StringBuffer bf = new StringBuffer();
		for(int i=0;i<list.size();i++){
			Map map = (Map) list.get(i);
			if(i>0){
				bf.append("#");
			}
			bf.append(map.get("vendor_id"));
			bf.append("$");
			bf.append(map.get("vendor_name"));
		}
		return bf.toString();
	}
	/**
	 * 查询设备型号
	 * @param vendorId
	 * @return
	 */
	public String getDeviceModelList(String vendorId){
		logger.debug("GwDeviceQueryBIO=>getDeviceModel(vendorId:{})",vendorId);
		List list = dao.getDeviceModelList(vendorId);
		StringBuffer bf = new StringBuffer();
		for(int i=0;i<list.size();i++){
			Map map = (Map) list.get(i);
			if(i>0){
				bf.append("#");
			}
			bf.append(map.get("model_id"));
			bf.append("$");
			bf.append(map.get("model_name"));
		}
		return bf.toString();
	}
	
	public String validateMAC(String vendorId,String mac){
		Map map=(Map) dao.validateMAC(vendorId, mac);
		String macPrefix=StringUtil.getStringValue(map.get("mac"));
		//把字符串拆分到数组
		String[] macPrefixs=macPrefix.split(",");
		//截取mac前缀
		String macSub=mac.substring(0, 6);
		boolean flag=false;
		for (int i = 0; i < macPrefixs.length; i++) {
			if(macPrefixs[i].equals(macSub)){
				flag=true;
			}
		}
		if(!flag){
			return "MAC不匹配";
		}
		return "true";
	}

	public MacQueryDAO getDao()
	{
		return dao;
	}

	
	public void setDao(MacQueryDAO dao)
	{
		this.dao = dao;
	}
	
	
}
