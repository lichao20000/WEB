package com.linkage.module.gtms.stb.diagnostic.action;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.LipossGlobals;
import com.linkage.module.gtms.stb.diagnostic.serv.PingInfoBIO;
import com.linkage.module.gtms.stb.obj.tr069.PingOBJ;
import com.linkage.module.gwms.Global;


/**
 * @author zxj E-mail：qixq@lianchuang.com
 * @version $Revision$
 * @since 2009-12-25
 * @category com.linkage.module.stb.resource.act
 * 
 */
public class PingInfoACT implements ServletRequestAware 
{
	private static Logger logger = LoggerFactory.getLogger(PingInfoACT.class);
	
	private static final long serialVersionUID = 1L;
	private String ajax;
	@SuppressWarnings("unused")
	private HttpServletRequest request = null;
	PingInfoBIO pingBio = null;
	private String deviceId = null;
	/**包大小*/
	private String dataBlockSize = null;
	/**包数目*/
	private String numberOfRepetitions = null;
	/**test Ip*/
	private String hostIp = null;
	/**timeout*/
	private String timeout = null;
	/**最大跳数*/
	private String maxHopCount= null;
	/**1:家庭网关，2：企业网管，4：机顶盒*/
	private String gw_type = null;
	/**归属ID*/
	private String cityId = null;
	/** IP地址类别   1:EPG地址  2:HMS地址   3:业务平台地址*/
	private String ipType = null;
	private String servName = null;
	private String servPasswd = null;
	private PingOBJ pingObj = null;
	
	private static String instArea=Global.instAreaShortName;
	private List<Map<String,String>> routeHopsData=null;
	List<Map<String,String>> pingRetList = null;

	public List<Map<String, String>> getIpMap() {
		return IpMap;
	}

	public void setIpMap(List<Map<String, String>> ipMap) {
		IpMap = ipMap;
	}

	// 山西联通新增 ping ip动态读取
	private List<Map<String, String>> IpMap;
	
	/**
	 * execute
	 */
	public String execute() throws Exception {
		logger.debug("execute()");
		return "success";
	}

	public String stbPingIndex(){
		logger.warn("stbPingIndex");
		this.IpMap = pingBio.queryIpMap();
		logger.warn("IpMap:{}",IpMap);
		return "stbPingIndex";
	}

	public String stbTraceRouteIndex(){
		this.IpMap = pingBio.queryIpMap();
		return "stbTraceRouteIndex";
	}

	public String pingTest()
	{
		logger.debug("pingTest()");
		pingObj = new PingOBJ();
		pingObj.setDeviceId(deviceId);
		pingObj.setPackSize(Integer.parseInt(dataBlockSize));
		pingObj.setPackNum(Integer.parseInt(numberOfRepetitions));
		pingObj.setDscp(48);
//		pingObj.setPingAddr(hostIp);
		pingObj.setTimeout(Integer.parseInt(timeout)*1000);
		this.pingRetList = pingBio.pingForList(pingObj, gw_type);
		return "pingTest";
	}
	
	//Ping测试_装维
	
	public String pingDiag()
	{
		logger.debug("pingTest()");
		pingObj = new PingOBJ();
		pingObj.setDeviceId(deviceId);
		pingObj.setPackSize(Integer.parseInt(dataBlockSize));
		pingObj.setPackNum(Integer.parseInt(numberOfRepetitions));
		pingObj.setDscp(48);
		pingObj.setPingAddr(hostIp);
		pingObj.setTimeout(Integer.parseInt(timeout)*1000);
		
		pingObj= pingBio.pingTest(pingObj, gw_type);
		logger.warn("[{}] pingDiag() over",deviceId);
		return "pingDiag";
	}
	
	
	public String traceRoute()
	{
		logger.debug("traceRoute()");
		pingObj = new PingOBJ();
		pingObj.setDeviceId(deviceId);
		pingObj.setMaxHopCount(Integer.parseInt(maxHopCount));
		pingObj.setPingAddr(hostIp);
		pingObj.setTimeout(Integer.parseInt(timeout)*1000);
		pingObj.setPackSize(128);
		pingObj.setDscp(0);
		this.pingObj = pingBio.traceRoute(pingObj, gw_type);
		return "traceRoute";
	}
	
	
	//TraceRoute_装维
	public String traceRouteDiag()
	{
		logger.warn("traceRoute({},{})",deviceId,hostIp);
		pingObj = new PingOBJ();
		pingObj.setDeviceId(deviceId);
		pingObj.setMaxHopCount(Integer.parseInt(maxHopCount));
		pingObj.setPingAddr(hostIp);
		pingObj.setTimeout(Integer.parseInt(timeout)*1000);
		pingObj.setPackSize(128);
		pingObj.setDscp(0);
		
		pingObj = pingBio.traceRouteTest(pingObj, gw_type);
		
		if(Global.SXLT.equals(instArea)){
			routeHopsData=pingBio.getRouteHopsData(deviceId,gw_type);
		}
		
		logger.warn("[{}] traceRouteDiag() over",deviceId);
		return "traceRouteDiag";
	}
	
	//业务账号密码参数下发
	public String paramConfig()
	{
		logger.debug("paramConfig()");
		String res= pingBio.paramConfig(servName,servPasswd, gw_type,deviceId);
		ajax = res;
		return "ajax";
	}
		
	/**
	 * 根据city_id查询IP类别，IP名称
	 */
	public String getIpTypeByCityId(){
		ajax = pingBio.getIpTypeByCityId(cityId);
		return "ajax";
	}
	
	public String getIpByIpType(){
		ajax = pingBio.getIpByIpType(cityId, ipType);
		return "ajax";
	}
	
	public void setServletRequest(HttpServletRequest arg0) {
		this.request = arg0;
	}

	public String getDataBlockSize() {
		return dataBlockSize;
	}

	public void setDataBlockSize(String dataBlockSize) {
		this.dataBlockSize = dataBlockSize;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getHostIp() {
		return hostIp;
	}

	public void setHostIp(String hostIp) {
		this.hostIp = hostIp;
	}

	public String getNumberOfRepetitions() {
		return numberOfRepetitions;
	}

	public void setNumberOfRepetitions(String numberOfRepetitions) {
		this.numberOfRepetitions = numberOfRepetitions;
	}

	public String getTimeout() {
		return timeout;
	}

	public void setTimeout(String timeout) {
		this.timeout = timeout;
	}

	public PingInfoBIO getPingBio() {
		return pingBio;
	}

	public void setPingBio(PingInfoBIO pingBio) {
		this.pingBio = pingBio;
	}

	public PingOBJ getPingObj() {
		return pingObj;
	}

	public void setPingObj(PingOBJ pingObj) {
		this.pingObj = pingObj;
	}

	public String getMaxHopCount() {
		return maxHopCount;
	}

	public void setMaxHopCount(String maxHopCount) {
		this.maxHopCount = maxHopCount;
	}
	
	public List<Map<String, String>> getPingRetList(){
		return pingRetList;
	}
	
	public void setPingRetList(List<Map<String, String>> pingRetList){
		this.pingRetList = pingRetList;
	}
	
	public String getIpType() {
		return ipType;
	}

	public void setIpType(String ipType) {
		this.ipType = ipType;
	}

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}
	
	public String getGw_type() {
		return gw_type;
	}
	
	public void setGw_type(String gw_type) {
		this.gw_type = gw_type;
	}
	
	public String getAjax() {
		return ajax;
	}
	
	public void setAjax(String ajax) {
		this.ajax = ajax;
	}
	
	public String getServName(){
		return servName;
	}
	
	public void setServName(String servName){
		this.servName = servName;
	}
	
	public String getServPasswd(){
		return servPasswd;
	}

	public void setServPasswd(String servPasswd){
		this.servPasswd = servPasswd;
	}

	public static String getInstArea() {
		return instArea;
	}

	public static void setInstArea(String instArea) {
		PingInfoACT.instArea = instArea;
	}

	public List<Map<String, String>> getRouteHopsData() {
		return routeHopsData;
	}

	public void setRouteHopsData(List<Map<String, String>> routeHopsData) {
		this.routeHopsData = routeHopsData;
	}

	
}
