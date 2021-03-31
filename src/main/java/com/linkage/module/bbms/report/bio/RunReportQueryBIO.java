package com.linkage.module.bbms.report.bio;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.bbms.report.dao.RunReportQueryDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.util.StringUtil;

@SuppressWarnings("unchecked")
public class RunReportQueryBIO {
	
	private static Logger logger = LoggerFactory.getLogger(RunReportQueryBIO.class);
	
	//网关信息（直接查询数据库所得的部分）
	private Map gatewayInfo =new HashMap();
	private Map filterContentMap =new HashMap();
	private String accessControl ;
	private String netSecStr ;
	private String netSerVir;
	private Map fireWallMap;
	private Map auVPNInfo;
	private List<Map> gatewayService ;
	private List<Map> gatewayWarnInfo ;
	List<Map> gatewayFluxInfo ;
	 //LAN 口
	private  List<Map<String,Object>> lanLt ;
	//WLAN 口
	private  List<Map<String,Object>> wlanLt;
	//WAN 口
	private  List<Map<String,Object>> wanLt ;
	
	private RunReportQueryDAO runReportQueryDao ;
	
	public Map getBaseInfo(String userName,
			String deviceSerialnumber, String deviceIP ,String deviceId) {
		logger.debug("获取网关信息：RunReportQueryBIO.getBaseInfo()");
		Map returnMap = new HashMap();
		//获取网关基本信息
		getGatewayBaseInfo(userName,deviceSerialnumber, deviceIP);
		//获取网关业务信息
		gatewayService = runReportQueryDao.queryGatewayService(deviceId);
		//网关流量信息
		gatewayFluxInfo = runReportQueryDao.queryGatewayFluxInfo(deviceId);
		//告警信息(可能不止一条)
		gatewayWarnInfo = runReportQueryDao.queryGatewayWarnInfo(deviceId);		
		returnMap = gatewayInfo;
		return returnMap;
	}
	/**
	 * 
	 * @param userName 用户帐号
	 * @param deviceSerialnumber 设备序列号
	 * @param deviceIP  设备IP 
	 */
	private void  getGatewayBaseInfo(String userName,
			String deviceSerialnumber, String deviceIP){
		logger.debug("查询网关基本信息");
		List<Map> gatewayBaseInfo = null;
		gatewayBaseInfo = runReportQueryDao.queryGatewayBaseInfo(userName,
				deviceSerialnumber, deviceIP);
		Map cityMap = CityDAO.getCityIdCityNameMap();
		if(null != gatewayBaseInfo && gatewayBaseInfo.size() > 0){
			Map tempMap = new HashMap();
			tempMap = gatewayBaseInfo.get(0);
			String cityId = StringUtil.getStringValue(tempMap.get("city_id"));
			
			gatewayInfo.put("device_serialnumber", StringUtil.getStringValue(tempMap.get("device_serialnumber")));
			gatewayInfo.put("oui", StringUtil.getStringValue(tempMap.get("oui")));
			gatewayInfo.put("city_name", cityMap.get(cityId));
			gatewayInfo.put("cpe_mac", StringUtil.getStringValue(tempMap.get("cpe_mac")));
			gatewayInfo.put("loopback_ip", StringUtil.getStringValue(tempMap.get("loopback_ip")));
			gatewayInfo.put("vendor_name", StringUtil.getStringValue(tempMap.get("vendor_name")));
			gatewayInfo.put("device_model", StringUtil.getStringValue(tempMap.get("device_model")));
			gatewayInfo.put("device_type", StringUtil.getStringValue(tempMap.get("device_type")));
			gatewayInfo.put("softwareversion", StringUtil.getStringValue(tempMap.get("softwareversion")));
			tempMap = null;
		}
	}
	/**
	 * 查询需要采集的网关信息
	 * @param deviceId 设备号
	 */
	public void getGatewayGatherInfo(String deviceId) {
		logger.info("查询需要采集的网关信息");
		if(!StringUtil.IsEmpty(deviceId)){
			//访问控制（开启/关闭）
			 accessControl = runReportQueryDao.getByProperty(deviceId);
			//内容控制
			Map contentMap = runReportQueryDao.getFilterContent(deviceId);
			if(null!=contentMap){
				filterContentMap.put("http_filter_enabled", contentMap.get("http_filter_enabled"));
				filterContentMap.put("file_filter_enable", contentMap.get("file_filter_enable"));
				filterContentMap.put("log_enable", contentMap.get("log_enable"));
			}
			Map mailMap = runReportQueryDao.getFilterMail(deviceId);
			if(null!=mailMap){
				filterContentMap.put("smtp_filter_enabled", contentMap.get("smtp_filter_enabled"));
				filterContentMap.put("pop3_filter_enabled", contentMap.get("pop3_filter_enabled"));
			}
			//上网控制(防入侵状态)
			netSecStr = runReportQueryDao.getNetSecurity(deviceId);
			//上网控制(防病毒状态)
			netSerVir = runReportQueryDao.getSerVir(deviceId);
			//vpn
			auVPNInfo =runReportQueryDao.getVPNInfo(deviceId); 
			//防火墙
			fireWallMap = runReportQueryDao.getFireWallInfo(deviceId);
			
			//-----------网关端口信息------------
			//LAN 口
		    lanLt = runReportQueryDao.getLanInfo(deviceId);
			//WLAN 口
		    wlanLt = runReportQueryDao.getWlanInfo(deviceId);
			//WAN口
		    wanLt = runReportQueryDao.getWanLt(deviceId);
		}
	}
	/**
	 * 
	 * @return  runReportQueryDao
	 */
		public RunReportQueryDAO getRunReportQueryDao() {
			return runReportQueryDao;
		}
	/**
	 * 
	 * @param runReportQueryDao
	 */
	public void setRunReportQueryDao(RunReportQueryDAO runReportQueryDao) {
			this.runReportQueryDao = runReportQueryDao;
		}
	/**
	 * 
	 * @return lanLT LAN口的信息列表
	 */
	public List<Map<String, Object>> getLanLt() {
		return lanLt;
	}
	/**
	 * 
	 * @param lanLt LAN口的信息列表
	 */
	public void setLanLt(List<Map<String, Object>> lanLt) {
		this.lanLt = lanLt;
	}
	/**
	 * 
	 * @return wlanLt  WLAN口的信息列表
	 */
	public List<Map<String, Object>> getWlanLt() {
		return wlanLt;
	}
	/**
	 * 
	 * @param wlanLt   WLAN口的信息列表
	 */
	public void setWlanLt(List<Map<String, Object>> wlanLt) {
		this.wlanLt = wlanLt;
	}
	/**
	 * 
	 * @return wanLt  WAN口的信息列表
	 */
	public List<Map<String, Object>> getWanLt() {
		return wanLt;
	}
	/**
	 * 
	 * @param wanLt WAN口的信息列表
	 */
	public void setWanLt(List<Map<String, Object>> wanLt) {
		this.wanLt = wanLt;
	}
	/**
	 * 
	 * @return  netSecStr 防入侵状态
	 */
	public String getNetSecStr() {
		return netSecStr;
	}
	/**
	 * 
	 * @param netSecStr  防入侵状态
	 */
	public void setNetSecStr(String netSecStr) {
		this.netSecStr = netSecStr;
	}
	
	/**
	 * 	
	 * @return netSerVir 防病毒状态
	 */
	public String getNetSerVir() {
		return netSerVir;
	}
	/**
	 * 
	 * @param netSerVir 防病毒状态
	 */
	public void setNetSerVir(String netSerVir) {
		this.netSerVir = netSerVir;
	}
	/**
	 * 
	 * @return filterContentMap 过滤内容MAP
	 */
	public Map getFilterContentMap() {
		return filterContentMap;
	}
	/**
	 * 
	 * @param filterContentMap 过滤内容MAP
	 */
	public void setFilterContentMap(Map filterContentMap) {
		this.filterContentMap = filterContentMap;
	}
	/**
	 * 
	 * @return  accessControl 访问控制状态
	 */
	public String getAccessControl() {
		return accessControl;
	}
	/**
	 * 
	 * @param accessControl  访问控制状态
	 */
	public void setAccessControl(String accessControl) {
		this.accessControl = accessControl;
	}
    /**
     * 
     * @return  gatewayService 网关业务信息列表
     */
	public List<Map> getGatewayService() {
		return gatewayService;
	}
	/**
	 *  
	 * @param gatewayService 网关业务信息列表
	 */
	public void setGatewayService(List<Map> gatewayService) {
		this.gatewayService = gatewayService;
	}
	/**
	 * 
	 * @return  gatewayWarnInfo 网关告警信息列表
	 */
	public List<Map> getGatewayWarnInfo() {
		return gatewayWarnInfo;
	}
	/**
	 * 
	 * @param gatewayWarnInfo 网关告警信息列表
	 */
	public void setGatewayWarnInfo(List<Map> gatewayWarnInfo) {
		this.gatewayWarnInfo = gatewayWarnInfo;
	}
	public List<Map> getGatewayFluxInfo() {
		return gatewayFluxInfo;
	}
	public void setGatewayFluxInfo(List<Map> gatewayFluxInfo) {
		this.gatewayFluxInfo = gatewayFluxInfo;
	}
	public Map getFireWallMap() {
		return fireWallMap;
	}
	public void setFireWallMap(Map fireWallMap) {
		this.fireWallMap = fireWallMap;
	}
	public Map getAuVPNInfo() {
		return auVPNInfo;
	}
	public void setAuVPNInfo(Map auVPNInfo) {
		this.auVPNInfo = auVPNInfo;
	}
	
}
