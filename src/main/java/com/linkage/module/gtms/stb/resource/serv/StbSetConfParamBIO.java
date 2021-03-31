package com.linkage.module.gtms.stb.resource.serv;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.DateTimeUtil;
import com.linkage.commons.util.StringUtil;
import com.linkage.module.gtms.stb.resource.dao.StbSetConfParamDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;

@SuppressWarnings("rawtypes")
public class StbSetConfParamBIO 
{
	private static Logger logger = LoggerFactory.getLogger(StbSetConfParamBIO.class);
	@SuppressWarnings("unused")
	private int queryCount;
	private StbSetConfParamDAO dao;
	

	/**
	 * 获取设备零配置下发参数
	 */
	public String getConfParam(String deviceId) 
	{
		logger.debug("getConfParam");
		Map devMap=dao.getDeviceConfParamInfo(deviceId);
		
		StringBuffer bf = new StringBuffer();
		if(devMap!=null && !devMap.isEmpty())
		{
			String vendorname=StringUtil.getStringValue(devMap,"vendor_add","");
			bf.append(StringUtil.IsEmpty(vendorname) ? 
					StringUtil.getStringValue(devMap,"vendor_name",""):vendorname);
			bf.append("#"+StringUtil.getStringValue(devMap,"device_model",""));
			bf.append("#"+StringUtil.getStringValue(devMap,"cpe_mac",""));
			bf.append("#"+StringUtil.getStringValue(devMap,"serv_account",""));
			bf.append("#"+StringUtil.getStringValue(devMap,"auth_server",""));
			bf.append("#"+StringUtil.getStringValue(devMap,"auth_server_bak",""));
			bf.append("#"+StringUtil.getStringValue(devMap,"auth_server_conn_peroid",""));
			bf.append("#"+StringUtil.getStringValue(devMap,"zeroconf_server",""));
			bf.append("#"+StringUtil.getStringValue(devMap,"zeroconfig_server_bak",""));
			bf.append("#"+StringUtil.getStringValue(devMap,"cmd_server",""));
			bf.append("#"+StringUtil.getStringValue(devMap,"cmd_server_bak",""));
			bf.append("#"+StringUtil.getStringValue(devMap,"cmd_server_conn_peroid",""));
			bf.append("#"+StringUtil.getStringValue(devMap,"ntp_server_main",""));
			bf.append("#"+StringUtil.getStringValue(devMap,"ntp_server_bak",""));
			bf.append("#"+StringUtil.getStringValue(devMap,"auto_sleep_mode",""));
			bf.append("#"+StringUtil.getStringValue(devMap,"auto_sleep_time",""));
			bf.append("#"+StringUtil.getStringValue(devMap,"ip_protocol_version_lan",""));
			bf.append("#"+StringUtil.getStringValue(devMap,"ip_protocol_version_wifi",""));
			bf.append("#"+getDate(StringUtil.getLongValue(devMap,"update_time")));
			bf.append("#"+getDate(StringUtil.getLongValue(devMap,"set_stb_time")));
			
			bf.append("#"+StringUtil.getStringValue(devMap,"oui","")
							+"-"+StringUtil.getStringValue(devMap,"device_serialnumber",""));
			bf.append("#"+CityDAO.getCityName(StringUtil.getStringValue(devMap,"city_id","")));
		}
		
		return bf.toString();
	}
	
	/**
	 * 修改设备零配置下发参数
	 */
	public String updateConfParam(String deviceId,String newParams) 
	{
		int result=dao.saveConfParam(deviceId,newParams);
		return result==1 ? "保存成功！":"保存失败！";
	}
	
	/**
	 * 删除设备零配置参数
	 */
	public String deleteConParamInfo(String deviceId) 
	{
		int result=dao.deleteConParamInfo(deviceId);
		return result==1 ? "删除成功":"删除失败";
	}
	
	/**
	 * 获取零配置设备参数列表
	 */
	public List<Map<String, String>> getConfParamList(int curPage_splitPage,
			int num_splitPage, String deviceSn, String deviceMac,
			String vendorId, String deviceModelId, String cityId,
			String citynext, long updateStartTime, long updateEndTime, 
			long setLastStartTime,
			long setLastEndTime, String auto_sleep_mode, String auto_sleep_time,
			String ip_protocol_version_lan, String ip_protocol_version_wifi,
			String servAccount) 
	{
		
		return dao.getConfParamList(curPage_splitPage,
				 num_splitPage, deviceSn,  deviceMac,
				 vendorId, deviceModelId,  cityId,
				 citynext,  updateStartTime,  updateEndTime, 
				 setLastStartTime,
				 setLastEndTime,  auto_sleep_mode,  auto_sleep_time,
				 ip_protocol_version_lan,  ip_protocol_version_wifi,
				 servAccount);
	}

	/**
	 * 分页
	 */
	public int countConfParamList(int num_splitPage, String deviceSn,
			String deviceMac, String vendorId, String deviceModelId,
			String cityId, String citynext, long updateStartTime, long updateEndTime,
			long setLastStartTime, long setLastEndTime, String auto_sleep_mode,
			String auto_sleep_time, String ip_protocol_version_lan,
			String ip_protocol_version_wifi, String servAccount) 
	{
		return dao.countConfParamList(
				 num_splitPage, deviceSn,  deviceMac,
				 vendorId, deviceModelId,  cityId,
				 citynext,  updateStartTime,  updateEndTime, 
				 setLastStartTime,
				 setLastEndTime,  auto_sleep_mode,  auto_sleep_time,
				 ip_protocol_version_lan,  ip_protocol_version_wifi,
				 servAccount);
	}
	
	/**
	 * 获取单台设备零配置参数详细
	 */
	@SuppressWarnings("unchecked")
	public Map<String, String> getConfParamMap(String deviceId) 
	{
		Map devMap=dao.getDeviceConfParamInfo(deviceId);
		if(devMap!=null && !devMap.isEmpty())
		{
			String vendorname=StringUtil.getStringValue(devMap,"vendor_add","");
			if(StringUtil.IsEmpty(vendorname)){
				vendorname=StringUtil.getStringValue(devMap,"vendor_name","");
			}
			
			devMap.put("sn",StringUtil.getStringValue(devMap,"oui")
							+"-"+StringUtil.getStringValue(devMap,"device_serialnumber"));
			devMap.put("city_name",CityDAO.getCityName(StringUtil.getStringValue(devMap,"city_id")));
			devMap.put("vendor_name",vendorname);
			devMap.put("device_model",StringUtil.getStringValue(devMap,"device_model"));
			devMap.put("mac",StringUtil.getStringValue(devMap,"cpe_mac"));
			devMap.put("serv_account",StringUtil.getStringValue(devMap,"serv_account"));
			
			String auth_server_conn_peroid=StringUtil.getStringValue(devMap,"auth_server_conn_peroid","");
			auth_server_conn_peroid=StringUtil.IsEmpty(auth_server_conn_peroid) ? 
													"":auth_server_conn_peroid+"秒";
			
			String cmd_server_conn_peroid=StringUtil.getStringValue(devMap,"cmd_server_conn_peroid","");
			cmd_server_conn_peroid=StringUtil.IsEmpty(cmd_server_conn_peroid) ? 
													"":cmd_server_conn_peroid+"秒";
			
			String sleep_mode=StringUtil.getStringValue(devMap,"auto_sleep_mode","");
			if("1".equals(sleep_mode)){
				sleep_mode="开";
			}else if("0".equals(sleep_mode)){
				sleep_mode="关";
			}
			
			String sleep_time=StringUtil.getStringValue(devMap,"auto_sleep_time","");
			if("3600".equals(sleep_time)){
				sleep_time="1小时";
			}else if("1800".equals(sleep_time)){
				sleep_time="30分钟";
			}else if("600".equals(sleep_time)){
				sleep_time="10分钟";
			}else if("300".equals(sleep_time)){
				sleep_time="5分钟";
			}else if("180".equals(sleep_time)){
				sleep_time="3分钟";
			}else if("60".equals(sleep_time)){
				sleep_time="1分钟";
			}else if("30".equals(sleep_time)){
				sleep_time="30秒";
			}
			
			String ip_lan=StringUtil.getStringValue(devMap,"ip_protocol_version_lan","");
			if("1".equals(ip_lan)){
				ip_lan="IPv4";
			}else if("2".equals(ip_lan)){
				ip_lan="IPv6";
			}else if("3".equals(ip_lan)){
				ip_lan="IPv4/v6";
			}
			
			String ip_wifi=StringUtil.getStringValue(devMap,"ip_protocol_version_wifi","");
			if("1".equals(ip_wifi)){
				ip_wifi="IPv4";
			}else if("2".equals(ip_wifi)){
				ip_wifi="IPv6";
			}else if("3".equals(ip_wifi)){
				ip_wifi="IPv4/v6";
			}
			
			devMap.put("auth_server_conn_peroid",auth_server_conn_peroid);
			devMap.put("cmd_server_conn_peroid",cmd_server_conn_peroid);
			devMap.put("auto_sleep_mode",sleep_mode);
			devMap.put("auto_sleep_time",sleep_time);
			devMap.put("ip_protocol_version_lan",ip_lan);
			devMap.put("ip_protocol_version_wifi",ip_wifi);
			devMap.put("update_time",getDate(StringUtil.getLongValue(devMap,"update_time")));
			devMap.put("set_stb_time",getDate(StringUtil.getLongValue(devMap,"set_stb_time")));
		}
		
		return devMap;
	}
	
	
	/**
	 * 获取总数
	 */
	public int getQueryCount()
	{
		return dao.getQueryCount();
	}
	
	
	
	/**
	 * 秒转日期
	 */
	private String getDate(long time)
	{
		return time==0 ? "":new DateTimeUtil(time*1000).getYYYY_MM_DD_HH_mm_ss();
	}
	
	public StbSetConfParamDAO getDao() {
		return dao;
	}

	public void setDao(StbSetConfParamDAO dao) {
		this.dao = dao;
	}

	public void setQueryCount(int queryCount) {
		this.queryCount = queryCount;
	}
}
