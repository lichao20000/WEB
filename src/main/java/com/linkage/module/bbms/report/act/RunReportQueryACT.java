package com.linkage.module.bbms.report.act;

import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.litms.LipossGlobals;
import com.linkage.litms.system.UserRes;
import com.linkage.module.bbms.report.bio.RunReportQueryBIO;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.util.corba.SuperGatherCorba;
@SuppressWarnings("unchecked")
public class RunReportQueryACT extends splitPageAction implements SessionAware{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// 日志记录
	private static Logger logger = LoggerFactory.getLogger(RunReportQueryACT.class);
	// session
	private Map session;
	/** 用户账号 */
	private String userName;
	/** 设备序列号 */
	private String deviceserialnumber;
	/** 设备IP地址**/
	private String deviceIP;
	/**导出的数据集**/
	private Map gatewayInfo ;
	/** 业务信息**/
	private List<Map> servInfo;
	/** 告警信息**/
	private List<Map> gatewayWarnLt ; 
	/**流量信息**/
	private List<Map> gatewayFluxInfo;
	/**设备ID**/
	private String deviceId;
	
	
	//---------------接口查询结果-------------------------
	/**内容过滤**/
	private Map filterContentMap ;
	/**访问控制**/
	private String accessControl ;
	/**入侵检测**/
	private String netSecStr ;
	/**防病毒**/
	private String netSerVir;
	/**防火墙**/
	private Map fireWallMap;
	/**VPN**/
	private Map vpnInfo;
	/**LAN 口**/
	private  List<Map<String,Object>> lanLt ;
	/**WLAN 口**/
	private  List<Map<String,Object>> wlanLt;
	/**WAN 口**/
	private  List<Map<String,Object>> wanLt ;
	
	private RunReportQueryBIO runReportBIO ;
	/**采集失败原因**/
	private String gatewaySecMsg ;
	
	/**rowspan合并行需要**/
	private int lanLen  ;
	private int wlanLen ;
	private int wanLen;
	/**
	 * 查询网关基本信息（限于可以直接从数据库中查询出）
	 * @return
	 */
	public String baseInfo(){
		logger.debug("RunReportQueryACT.baseInfo()");
		logger.warn("ACTdeviceId:" + deviceId);
		logger.warn("ACTdeviceserialnumber:" + deviceserialnumber);
		
		UserRes curUser = (UserRes) session.get("curUser");
		String cityId = curUser.getCityId();
		String cityName  = CityDAO.getCityIdCityNameMap().get("cityId");
		gatewayInfo = runReportBIO.getBaseInfo(userName,deviceserialnumber,deviceIP,deviceId);
		gatewayInfo.put("cityName", cityName);
		 //业务信息
		servInfo = runReportBIO.getGatewayService();
		//告警信息
		gatewayWarnLt = runReportBIO.getGatewayWarnInfo();
		//流量信息
		gatewayFluxInfo = runReportBIO.getGatewayFluxInfo();
		return "baseInfo";
	}
	/**
	 * 采集设备信息
	 * @return String
	 */			 
	public String queryGatherInfo(){
		logger.debug("网关安全配置信息RunReportQueryACT.queryGatherInfo()");
		int result = -1;
		//getCpeParems(param1,param2) param 为0时直接调用采集，为1时直接查询数据库查询不到再采集
	    result = new SuperGatherCorba(LipossGlobals.getGw_Type(deviceId)).getCpeParams(deviceId, 1);
		if(result !=1){
			logger.debug("调用采集失败");
			/**调用采集失败**/
			if(null!=Global.G_Fault_Map.get(result)){
				this.gatewaySecMsg = Global.G_Fault_Map.get(result).getFaultReason();
			}else{
				this.gatewaySecMsg = Global.G_Fault_Map.get(10000).getFaultReason();
			}
		}else{
			//采集数据
			runReportBIO.getGatewayGatherInfo(deviceId);
			
			accessControl = runReportBIO.getAccessControl();
			filterContentMap = runReportBIO.getFilterContentMap();
			netSecStr = runReportBIO.getNetSecStr();
			netSerVir = runReportBIO.getNetSerVir();
			vpnInfo = runReportBIO.getAuVPNInfo();
			fireWallMap = runReportBIO.getFireWallMap();
			
			lanLt = runReportBIO.getLanLt();
			wlanLt = runReportBIO.getWlanLt();
			wanLt = runReportBIO.getWanLt();
			
			lanLen = lanLt.size()+1;
			wlanLen = wlanLt.size()+1;
			wanLen = wanLt.size()+1;
		}
		return "netConfigInfo";
	}
	/**
	 * 
	 * @return session
	 */
	public Map getSesssion(){
		return session;
	}

	/**
	 * @param session
	 *      the session to set
	 */
	public void setSession(Map session){
		this.session  = session;
	}
	/**
	 * 
	 * @return userName
	 */
	public String getUserName(){
		return userName;
	}
	/**
	 * 
	 * @param userName
	 */
	public void setUserName(String userName){
		this.userName = userName;
	}

/**
 * 
 * @return runReportBIO
 */
	public RunReportQueryBIO getRunReportBIO() {
		return runReportBIO;
	}
/**
 * 
 * @param runReportBIO
 */
	public void setRunReportBIO(RunReportQueryBIO runReportBIO) {
		this.runReportBIO = runReportBIO;
	}
	/**
	 * 
	 * @return device_IP
	 */
	public String getDeviceIP() {
		return deviceIP;
	}
	/**
	 * 
	 * @param deviceIP
	 */
	public void setDeviceIP(String deviceIP) {
		this.deviceIP = deviceIP;
	}
	/**
	 * 
	 * @return  gatewayInfo
	 */
	public Map getGatewayInfo() {
		return gatewayInfo;
	}
	/**
	 * 
	 * @param gatewayInfo
	 */
	public void setGatewayInfo(Map gatewayInfo) {
		this.gatewayInfo = gatewayInfo;
	}
	
	public String getDeviceserialnumber() {
		return deviceserialnumber;
	}
	public void setDeviceserialnumber(String deviceserialnumber) {
		this.deviceserialnumber = deviceserialnumber;
	}
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	/**
	 * 
	 * @return 采集失败的原因
	 */
	public String getGatewayMsg() {
		return gatewaySecMsg;
	}
	/**
	 * 
	 * @param gatewayMsg
	 */
	public void setGatewayMsg(String gatewaySecMsg) {
		this.gatewaySecMsg = gatewaySecMsg;
	}
	public Map getFilterContentMap() {
		return filterContentMap;
	}
	public void setFilterContentMap(Map filterContentMap) {
		this.filterContentMap = filterContentMap;
	}
	public String getAccessControl() {
		return accessControl;
	}
	public void setAccessControl(String accessControl) {
		this.accessControl = accessControl;
	}
	public String getNetSecStr() {
		return netSecStr;
	}
	public void setNetSecStr(String netSecStr) {
		this.netSecStr = netSecStr;
	}
	public String getNetSerVir() {
		return netSerVir;
	}
	public void setNetSerVir(String netSerVir) {
		this.netSerVir = netSerVir;
	}
	public List<Map<String, Object>> getLanLt() {
		return lanLt;
	}
	public void setLanLt(List<Map<String, Object>> lanLt) {
		this.lanLt = lanLt;
	}
	public List<Map<String, Object>> getWlanLt() {
		return wlanLt;
	}
	public void setWlanLt(List<Map<String, Object>> wlanLt) {
		this.wlanLt = wlanLt;
	}
	public List<Map<String, Object>> getWanLt() {
		return wanLt;
	}
	public void setWanLt(List<Map<String, Object>> wanLt) {
		this.wanLt = wanLt;
	}
	public String getGatewaySecMsg() {
		return gatewaySecMsg;
	}
	public void setGatewaySecMsg(String gatewaySecMsg) {
		this.gatewaySecMsg = gatewaySecMsg;
	}
	public Map getSession() {
		return session;
	}
	public List<Map> getServInfo() {
		return servInfo;
	}
	public void setServInfo(List<Map> servInfo) {
		this.servInfo = servInfo;
	}
	public List<Map> getGatewayWarnLt() {
		return gatewayWarnLt;
	}
	public void setGatewayWarnLt(List<Map> gatewayWarnLt) {
		this.gatewayWarnLt = gatewayWarnLt;
	}
	public List<Map> getGatewayFluxInfo() {
		return gatewayFluxInfo;
	}
	public void setGatewayFluxInfo(List<Map> gatewayFluxInfo) {
		this.gatewayFluxInfo = gatewayFluxInfo;
	}
	public int getLanLen() {
		return lanLen;
	}
	public void setLanLen(int lanLen) {
		this.lanLen = lanLen;
	}
	public int getWlanLen() {
		return wlanLen;
	}
	public void setWlanLen(int wlanLen) {
		this.wlanLen = wlanLen;
	}
	public int getWanLen() {
		return wanLen;
	}
	public void setWanLen(int wanLen) {
		this.wanLen = wanLen;
	}
	public Map getFireWallMap() {
		return fireWallMap;
	}
	public void setFireWallMap(Map fireWallMap) {
		this.fireWallMap = fireWallMap;
	}
	public Map getVpnInfo() {
		return vpnInfo;
	}
	public void setVpnInfo(Map vpnInfo) {
		this.vpnInfo = vpnInfo;
	}
	
}
