package com.linkage.module.gtms.stb.resource.serv;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.DateTimeUtil;
import com.linkage.module.gtms.stb.resource.dao.StbReportQueryDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;

/**
 * 
 * @author hp (Ailk No.)
 * @version 1.0
 * @since 2017-12-6
 * @category com.linkage.module.gtms.stb.resource.serv
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 *
 */
public class StbReportQueryBIO
{
	private static Logger logger = LoggerFactory.getLogger(StbReportQueryBIO.class);
	private StbReportQueryDAO dao;
	private int maxPage_splitPage;
	/**
	 * 查询属地
	 * @return
	 */
	public String getCity(String cityId){
		logger.debug("GwDeviceQueryBIO=>getCity(cityId:{})",cityId);
		List list = CityDAO.getNextCityListByCityPid(cityId);
		StringBuffer bf = new StringBuffer();
		for(int i=0;i<list.size();i++){
			Map map = (Map) list.get(i);
			if(i>0){
				bf.append("#");
			}
			bf.append(map.get("city_id"));
			bf.append("$");
			bf.append(map.get("city_name"));
		}
		return bf.toString();
	}
	/**
	 * 查询设备厂商
	 * @return
	 */
	public String getVendor(){
		logger.debug("GwDeviceQueryBIO=>getVendor()");
		List list = dao.getVendor();
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
		List list = dao.getDeviceModel(vendorId);
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
	/**
	 * 查询设备版本
	 * @param deviceModelId
	 * @return
	 */
	public String getDevicetype(String deviceModelId){
		logger.debug("GwDeviceQueryBIO=>getDevicetype(deviceModelId:{})",deviceModelId);
		List list = dao.getVersionList(deviceModelId);
		StringBuffer bf = new StringBuffer();
		for(int i=0;i<list.size();i++){
			Map map = (Map) list.get(i);
			if(i>0){
				bf.append("#");
			}
			bf.append(map.get("devicetype_id"));
			bf.append("$");
			bf.append(map.get("hardwareversion")+"("+map.get("softwareversion")+")");
		}
		return bf.toString();
	}
	@SuppressWarnings("unchecked")
	public List<Map> queryList(int curPage_splitPage, int num_splitPage, String vendorId,String deviceModelId,String devicetypeId,String gwShare_cityId,String cityId,String startIp,String endIp,String gwShare_onlineStatus)
	{
		this.maxPage_splitPage=dao.queryTotalListCount(curPage_splitPage, num_splitPage, vendorId, deviceModelId, devicetypeId, gwShare_cityId, cityId, startIp, endIp, gwShare_onlineStatus);
		List list=dao.queryList(curPage_splitPage, num_splitPage, vendorId, deviceModelId, devicetypeId, gwShare_cityId, cityId, startIp, endIp, gwShare_onlineStatus);
		 List<Map> listShow = new ArrayList();
		
		for (int i = 0; i < list.size(); i++){
			Map map = (Map)list.get(i);
			map.put("vendor_name", map.get("vendor_name"));
			map.put("device_model", map.get("device_model"));
			map.put("softwareversion",map.get("softwareversion"));
			String city_id = String.valueOf( map.get("city_id"));
			map.put("city_name", CityDAO.getCityIdCityNameMap().get(city_id));
			map.put("device_serialnumber", map.get("device_serialnumber"));
			map.put("serv_account", map.get("serv_account"));
			map.put("cpe_mac", map.get("cpe_mac"));
			map.put("loopback_ip", map.get("loopback_ip"));
			map.put("complete_time", map.get("complete_time"));
			listShow.add(map);
		}
		
		return listShow;
	}
	/**
	 * 导出
	 * @param vendorId
	 * @param deviceModelId
	 * @param devicetypeId
	 * @param gwShare_cityId
	 * @param cityId
	 * @param startIp
	 * @param endIp
	 * @param gwShare_onlineStatus
	 * @return
	 */
	public List<Map> getExcel( String vendorId,String deviceModelId,String devicetypeId,String gwShare_cityId,String cityId,String startIp,String endIp,String gwShare_onlineStatus)
	{
		return dao.getExcel(vendorId, deviceModelId, devicetypeId, gwShare_cityId, cityId, startIp, endIp, gwShare_onlineStatus);
	}
	public StbReportQueryDAO getDao()
	{
		return dao;
	}

	
	
	public int getMaxPage_splitPage()
	{
		return maxPage_splitPage;
	}
	
	public void setMaxPage_splitPage(int maxPage_splitPage)
	{
		this.maxPage_splitPage = maxPage_splitPage;
	}
	public void setDao(StbReportQueryDAO dao)
	{
		this.dao = dao;
	}
	
}
