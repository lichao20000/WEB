package com.linkage.module.gtms.stb.resource.serv;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.DateTimeUtil;
import com.linkage.commons.util.StringUtil;
import com.linkage.module.gtms.stb.resource.dao.StbSetParamDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;

@SuppressWarnings("rawtypes")
public class StbSetParamBIO 
{
	private static Logger logger = LoggerFactory.getLogger(StbSetParamBIO.class);
	/**网络类型*/
	private static Map<String,String> netTypeMap=new HashMap<String,String>();
	/**设备类型 */
	private static Map<String,String> devTypeMap=new HashMap<String,String>();
	static{
		netTypeMap.put("public_net","公 网");
		netTypeMap.put("private_net","专 网");
		netTypeMap.put("unknown_net","未 知");
		devTypeMap.put("0","行");
		devTypeMap.put("1","串");
		devTypeMap.put("2","未 知");
	}
	
	private StbSetParamDAO dao;
	
	public String getDeviceInfo(String device_id) 
	{
		String res=null;
		Map map=dao.getDeviceInfo(device_id);
		if(map!=null && !map.isEmpty())
		{
			res=StringUtil.getStringValue(map,"device_serialnumber","")
					+"#"+StringUtil.getStringValue(map,"device_model","")
					+"#"+StringUtil.getStringValue(map,"vendor_add","")
					+"#"+CityDAO.getCityName(StringUtil.getStringValue(map,"city_id"))
					+"#"+StringUtil.getStringValue(map,"cpe_mac","")
					+"#"+StringUtil.getStringValue(map,"serv_account","")
					+"#"+StringUtil.getStringValue(map,"hardwareversion","")
					+"#"+StringUtil.getStringValue(map,"softwareversion","")
					+"#"+StringUtil.getStringValue(map,"apk_version_name","")
					+"#"+StringUtil.getStringValue(map,"epg_version","")
					+"#"+new DateTimeUtil(StringUtil.getLongValue(map,"complete_time")*1000).getYYYY_MM_DD_HH_mm_ss()
					+"#"+new DateTimeUtil(StringUtil.getLongValue(map,"cpe_currentupdatetime")*1000).getYYYY_MM_DD_HH_mm_ss()
					+"#"+StringUtil.getStringValue(map,"network_type","")
					+"#"+StringUtil.getStringValue(map,"addressing_type","")
					+"#"+StringUtil.getStringValue(map,"loopback_ip","")
					+"#"+StringUtil.getStringValue(map,"public_ip","")
					+"#"+netTypeMap.get(StringUtil.getStringValue(map,"net_type","unknown_net"))
					+"#"+netTypeMap.get(StringUtil.getStringValue(map,"ip_type","unknown_net"))
					+"#"+devTypeMap.get(StringUtil.getStringValue(map,"category","2"));
		}
		return res;
	}
	
	/**
	 * 新增策略，重启或恢复出厂设置
	 */
	public String add(long accOid,String device_id, String service_id)
	{
		int i= dao.insertStrategy(accOid, device_id, StringUtil.getLongValue(service_id));
		logger.debug("add i="+i);
		if(i==0){
			return "策略定制失败！";
		}
		return "策略定制成功！";
	}

	/**
	 * 删除策略
	 */
	public String delete(String strategy_id) 
	{
		int i= dao.delete(strategy_id);
		logger.debug("delete i="+i);
		if(i==0){
			return "0,策略删除失败！";
		}
		return "1,策略删除成功！";
	}

	/**
	 * 获取所有策略
	 */
	public List<Map> getStrategyResult(String device_id,int curPage_splitPage,int num_splitPage) 
	{
		return dao.getStrategyResult(device_id, curPage_splitPage, num_splitPage);
	}

	/**
	 * 获取最大页数
	 */
	public int getCountStrategyResult(String device_id,int num_splitPage)
	{
		return dao.getCountStrategyResult(device_id, num_splitPage);
	}
	

	public StbSetParamDAO getDao() {
		return dao;
	}

	public void setDao(StbSetParamDAO dao) {
		this.dao = dao;
	}

}
