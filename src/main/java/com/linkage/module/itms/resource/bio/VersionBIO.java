package com.linkage.module.itms.resource.bio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.StringUtil;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.itms.resource.dao.VersionDAO;

public class VersionBIO {
	private static Logger logger = LoggerFactory.getLogger(VersionBIO.class);
	
	private VersionDAO dao;
	
	/**
	 * 获取光衰阀值列表数据
	 * 
	 * @return
	 */
	public List<Map<String, Object>> getDroopList() {
		return dao.getDroopList();
	}

	/**
	 * 获取厂商名称
	 * 
	 * @return String
	 */
	public List<Map<String, Object>> getVendors() {
		return dao.getVendors();
	}

	/**
	 * 保存光功率设定阀值
	 * 
	 * @param operType
	 * @param vendor_name
	 * @param tx_power
	 * @param rx_power
	 * @return String
	 */
	public String operDroop(String operType, String vendor_id,
			String vendor_name, String tx_power, String rx_power) {
		// 更新光功率数据
		if ("1".equals(operType)) {
			return dao.updateDroop(vendor_id, tx_power, rx_power);
		} else {
			// 新增光功率数据
			return dao.saveDroop(vendor_id, vendor_name, tx_power, rx_power);
		}

	}

	/**
	 * 删除光功率设定值
	 * 
	 * @param vendor_id
	 * @return
	 */
	public String delDroop(String vendor_id) {
		return dao.delDroop(vendor_id);
	}

	/**
	 * 光功率定制
	 * 
	 * @param cityId
	 * @param colType
	 * @return
	 */
	public String orderPower(String cityId, String colType) {
		return dao.orderPower(cityId, colType);
	}
	
	/**
	 * 获取软件版本
	 * 
	 * @param vendor_id
	 * @return String
	 */
	public String getSoftVersion(String vendor_id) {
		return dao.getSoftVersion(vendor_id);
	}
	
	/**
	 * 
	 * @param startTime
	 * @param endTime
	 * @param username
	 * @param device_serialnumber
	 * @param cityId
	 * @param vendor_id
	 * @param soft_version
	 * @param powerVal
	 * @param powerScope
	 * @return list
	 */
	public List<Map> getPowerExcel(String starttime,
			String endtime, String username, String device_serialnumber,
			String cityId, String vendor_id, String soft_version,
			String powerVal, String powerScope) {
		return dao.getPowerExcel(starttime, endtime, username,
				device_serialnumber, cityId, vendor_id, soft_version, powerVal,
				powerScope);
	}

	@SuppressWarnings("rawtypes")
	public List<Map> queryVersionList(String vendor, String devicemodel,
			String versionSpecification, String specName, String deviceType,
			String accessWay, String voiceAgreement, String zeroconf,
			String mbbndwidth, String ipvsix, String temval,
			int curPage_splitPage, int num_splitPage) {
		logger.debug("VersionDAO=>queryVersionList");
		return dao.queryVersionList(vendor, devicemodel, versionSpecification,
				specName, deviceType, accessWay, voiceAgreement, zeroconf,
				mbbndwidth, ipvsix, temval, curPage_splitPage, num_splitPage);
	}

	public int countQueryVersionList(String vendor, String devicemodel,
			String versionSpecification, String specName, String deviceType,
			String accessWay, String voiceAgreement, String zeroconf,
			String mbbndwidth, String ipvsix, String temval,
			int curPage_splitPage, int num_splitPage) {
		logger.debug("VersionDAO=>countQueryVersionList");
		return dao.countQueryVersionList(vendor, devicemodel,
				versionSpecification, specName, deviceType, accessWay,
				voiceAgreement, zeroconf, mbbndwidth, ipvsix, temval,
				curPage_splitPage, num_splitPage);
	}
	
	/**
	 * 
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @param startTime
	 * @param endTime
	 * @param username
	 * @param device_serialnumber
	 * @param cityId
	 * @param vendor_id
	 * @param soft_version
	 * @param powerVal
	 * @param powerScope
	 * @return list
	 */
	@SuppressWarnings("rawtypes")
	public List<Map> getpowerList(int curPage_splitPage, int num_splitPage,
			String starttime, String endtime, String username,
			String device_serialnumber, String cityId, String vendor_id,
			String soft_version, String powerVal, String powerScope) {
		List<Map> list=dao.getpowerList(curPage_splitPage, num_splitPage, starttime,
				endtime, username, device_serialnumber, cityId, vendor_id,
				soft_version, powerVal, powerScope);
		
		List<Map> lis=new ArrayList<Map>();
		int index=(curPage_splitPage-1)*num_splitPage+1;
		if(list!=null && list.size()>0){
			for(Map<String,String> map:list){
				Map<String,String> m=new HashMap<String,String>();
			
				m.put("index",index+"");
				m.put("parentName",map.get("parentName"));
				m.put("cityName",map.get("cityName"));
				m.put("username",map.get("username"));
				m.put("vendorName",map.get("vendorName"));
				m.put("deviceSn",map.get("deviceSn"));
				m.put("txPower",map.get("txPower"));
				m.put("rxPower",map.get("rxPower"));
				m.put("colDate",map.get("colDate"));
				m.put("device_model",map.get("device_model"));
				
				lis.add(m);
				index++;
				m=null;
			}
		}
		
			
		
		return lis;
	}
	
	/**
	 * 
	 * @param num_splitPage
	 * @param startTime
	 * @param endTime
	 * @param username
	 * @param device_serialnumber
	 * @param cityId
	 * @param vendor_id
	 * @param soft_version
	 * @param powerVal
	 * @param powerScope
	 * @return int
	 */
	public int getpowerCount(int num_splitPage, String starttime,
			String endtime, String username, String device_serialnumber,
			String cityId, String vendor_id, String soft_version,
			String powerVal, String powerScope) {
		return dao.getpowerCount(num_splitPage, starttime, endtime, username,
				device_serialnumber, cityId, vendor_id, soft_version, powerVal,
				powerScope);
	}
	

	@SuppressWarnings("rawtypes")
	public List<Map> queryDetail(String vendor, String devicemodel,
			String versionSpecification, String specName, String deviceType,
			String accessWay, String voiceAgreement, String zeroconf,
			String mbbndwidth, String ipvsix, String temval,
			String softwareversion, int curPage_splitPage, int num_splitPage) {
		logger.debug("VersionDAO=>queryVersionList");
		return dao.queryDetail(vendor, devicemodel, versionSpecification,
				specName, deviceType, accessWay, voiceAgreement, zeroconf,
				mbbndwidth, ipvsix, temval, softwareversion, curPage_splitPage,
				num_splitPage);
	}
	
	/**
	 * 获取全部厂商信息
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getVendorList() {
		return dao.getVendorList();
	}
	
	
	@SuppressWarnings("rawtypes")
	public List<Map> excelqueryDetail(String vendor, String devicemodel,
			String versionSpecification, String specName, String deviceType,
			String accessWay, String voiceAgreement, String zeroconf,
			String mbbndwidth, String ipvsix, String temval,
			String softwareversion) {
		logger.debug("VersionDAO=>queryVersionList");
		return dao.excelqueryDetail(vendor, devicemodel, versionSpecification,
				specName, deviceType, accessWay, voiceAgreement, zeroconf,
				mbbndwidth, ipvsix, temval, softwareversion);
	}

	public int countQueryDetail(String vendor, String devicemodel,
			String versionSpecification, String specName, String deviceType,
			String accessWay, String voiceAgreement, String zeroconf,
			String mbbndwidth, String ipvsix, String temval,
			String softwareversion, int curPage_splitPage, int num_splitPage) {
		logger.debug("VersionDAO=>countQueryVersionList");
		return dao.countQueryDetail(vendor, devicemodel,
				versionSpecification, specName, deviceType, accessWay,
				voiceAgreement, zeroconf, mbbndwidth, ipvsix, temval,softwareversion,
				curPage_splitPage, num_splitPage);
	}

	@SuppressWarnings("rawtypes")
	public List<Map> excelQueryVersionList(String vendor, String devicemodel,
			String versionSpecification, String specName, String deviceType,
			String accessWay, String voiceAgreement, String zeroconf,
			String mbbndwidth, String ipvsix, String temval) {
		logger.debug("VersionDAO=>excelQueryVersionList");
		return dao.excelQueryVersionList(vendor, devicemodel,
				versionSpecification, specName, deviceType, accessWay,
				voiceAgreement, zeroconf, mbbndwidth, ipvsix, temval);
	}

	public VersionDAO getDao() {
		return dao;
	}

	public void setDao(VersionDAO dao) {
		this.dao = dao;
	}
}
