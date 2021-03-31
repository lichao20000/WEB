
package com.linkage.module.itms.config.act;


import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.util.Base64;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.Global;
import com.linkage.module.itms.config.bio.ChangeConnectionTypeBIO;

@SuppressWarnings("rawtypes")
public class ChangeConnectionTypeACT extends splitPageAction implements SessionAware
{

	private static final long serialVersionUID = 1L;
	// 日志记录
	private static Logger logger = LoggerFactory.getLogger(ChangeConnectionTypeACT.class);
	// session
	private Map session;
	private String deviceId;
	private String connType;
//	private String doType;
	private String vlan;
	private List<Map> pvcList;
	private String pvc="";
	private ChangeConnectionTypeBIO bio;
	private String ajax;
	private String accessType;
	private String user_id;
	private String deviceserialnumber;
	private String oui ;
	private String bindPort;
	private String type;
	private String path;
	/**
	 * 路由账号
	 */
	private String routeAccount = null;

	/**
	 * 路由密码
	 */
	private String routePasswd = null;
	
	private String gw_type;
	
	
	public String getGw_type() {
		return gw_type;
	}


	public void setGw_type(String gw_type) {
		this.gw_type = gw_type;
	}


	public String changeConnectionType(){
		UserRes curUser = (UserRes) session.get("curUser");
		logger.warn("acc_loginname[{}],connType[{}],routeAccount[{}],routePasswd[{}],deviceserialnumber[{}],oui[{}]变更上网方式",
				new Object[]{curUser.getUser().getAccount(),connType,routeAccount,routePasswd,deviceserialnumber,oui});
		
		if(Global.AHDX.equals(Global.instAreaShortName)){
			ajax = bio.changeConnectionTypeForAh(deviceId,connType,routeAccount,routePasswd,path,gw_type);
		}else{
			ajax = bio.changeConnectionType(curUser.getUser().getId(),
					deviceId,connType,routeAccount,routePasswd,pvc,vlan,accessType,user_id,deviceserialnumber,oui,bindPort, gw_type);
		}
		return "ajax";
	}
	
	/**
	 * 检查设备是否绑定及绑定用户是否有上网业务
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String checkBindAndService(){
		UserRes curUser = (UserRes) session.get("curUser");
		Map map = null;
		if (LipossGlobals.inArea(Global.NXDX)) {
			map = bio.checkBindAndService(deviceId, curUser.getUser().getCityId(), gw_type, vlan);
		}
		else {
			map = bio.checkBindAndService(deviceId, curUser.getUser().getCityId(), gw_type);
		}
		
		ajax = map.get("strResl").toString();
		
		String flag = map.get("flag").toString();
		
	//	if("1".equals(flag)){ // 1 表示设备绑定的用户存在桥接上网业务
			if(map.get("access_style_id")!=null){
				String accessType = map.get("access_style_id").toString();
				if(accessType.equals("1"))
				{
					pvcList = (List<Map>)map.get("pvcList");
					Set<String> pvcSet = new HashSet<String>();
					for(Map map2 : pvcList){
						pvcSet.add(StringUtil.getStringValue(map2.get("pvc")));
//						if("".equals(pvc)){
//							pvc = StringUtil.getStringValue(map2.get("pvc"));
//						}else{
//							pvc = pvc + "#"+StringUtil.getStringValue(map2.get("pvc"));
//						}
//						logger.warn(pvc);
					}
					if(false==StringUtil.IsEmpty(StringUtil.getStringValue(map.get("vpiid")))){
						pvcSet.add(StringUtil.getStringValue(map.get("vpiid"))+"/"+StringUtil.getStringValue(map.get("vciid")));
//						if("".equals(pvc)){
//							pvc = StringUtil.getStringValue(map.get("vpiid"))+"/"+StringUtil.getStringValue(map.get("vciid"));
//						}else{
//							pvc = pvc + "#"+StringUtil.getStringValue(map.get("vpiid"))+"/"+StringUtil.getStringValue(map.get("vciid"));
//						}
					}
					for (String string : pvcSet) {
						if("".equals(pvc)){
							pvc = string;
						}else{
							pvc = pvc + "#"+string;
						}
					}
					ajax+=";"+pvc;
				}else if(accessType.equals("2"))
				{
					vlan = map.get("vlanid").toString();
					ajax+=";"+vlan;
				}
				else if(accessType.equals("3") || accessType.equals("4")) 
				{
					vlan = map.get("vlanid").toString();
					ajax+=";"+vlan;
				}else{
					ajax = "0;用户接入方式不知道";
				}
			}
			String username = StringUtil.getStringValue(map, "username", "");
			String passwd = StringUtil.getStringValue(map, "passwd", "");
			String user_id = StringUtil.getStringValue(map, "user_id", "");
			ajax += ";" + username;
			ajax += ";" + passwd;
			ajax += ";" + user_id;
			ajax += ";" + flag;
			
			ajax += ";" + map.get("vlanIds");
	//	}
		return "ajax";
	}
	
	
	private List servList = null;
	public String getServInfo() {
		servList = bio.getServInfo(deviceId);
		return "servList";
	}
	
	public List getServList() {
		return servList;
	}

	public void setServList(List servList) {
		this.servList = servList;
	}


	/**
	 * 预读设备获取设备的上网方式
	 * @return
	 */
	public String getDevNetType(){
		ajax = bio.getDevNetType(deviceId,gw_type);
		logger.warn(ajax);
		return "ajax";
	}
	
	/**
	 * 查询端口
	 * 
	 * @return
	 */
	public String addLanInit(){
		
		logger.debug("addWanInit()");

		ajax = bio.getLanInter(deviceId,type);

		return "ajax";
	}
	
	
		
	/**
	 * @return the session
	 */
	public Map getSession()
	{
		return session;
	}
	
	/**
	 * @param session the session to set
	 */
	public void setSession(Map session)
	{
		this.session = session;
	}
	
	/**
	 * @return the deviceId
	 */
	public String getDeviceId()
	{
		return deviceId;
	}
	
	/**
	 * @param deviceId the deviceId to set
	 */
	public void setDeviceId(String deviceId)
	{
		this.deviceId = deviceId;
	}
	
	/**
	 * @return the connType
	 */
	public String getConnType()
	{
		return connType;
	}
	
	/**
	 * @param connType the connType to set
	 */
	public void setConnType(String connType)
	{
		this.connType = connType;
	}
	
	/**
	 * @return the doType
	 */
//	public String getDoType()
//	{
//		return doType;
//	}
	
	/**
	 * @param doType the doType to set
	 */
//	public void setDoType(String doType)
//	{
//		this.doType = doType;
//	}
	
	/**
	 * @return the routeAccount
	 */
	public String getRouteAccount()
	{
		return routeAccount;
	}
	
	/**
	 * @param routeAccount the routeAccount to set
	 */
	public void setRouteAccount(String routeAccount)
	{
		this.routeAccount = routeAccount;
	}
	
	/**
	 * @return the routePasswd
	 */
	public String getRoutePasswd()
	{
		return routePasswd;
	}
	
	/**
	 * @param routePasswd the routePasswd to set
	 */
	public void setRoutePasswd(String routePasswd)
	{
		this.routePasswd = routePasswd;
	}

	
	/**
	 * @return the bio
	 */
	public ChangeConnectionTypeBIO getBio()
	{
		return bio;
	}

	
	/**
	 * @param bio the bio to set
	 */
	public void setBio(ChangeConnectionTypeBIO bio)
	{
		this.bio = bio;
	}

	
	/**
	 * @return the ajax
	 */
	public String getAjax()
	{
		return ajax;
	}

	
	
	public String getType()
	{
		return type;
	}

	
	public void setType(String type)
	{
		this.type = type;
	}

	/**
	 * @param ajax the ajax to set
	 */
	public void setAjax(String ajax)
	{
		this.ajax = ajax;
	}

	public String getVlan()
	{
		return vlan;
	}
	
	public void setVlan(String vlan)
	{
		this.vlan = vlan;
	}
	
	public List<Map> getPvcList()
	{
		return pvcList;
	}
	
	public void setPvcList(List<Map> pvcList)
	{
		this.pvcList = pvcList;
	}
	
	public String getPvc()
	{
		return pvc;
	}
	
	public String getAccessType()
	{
		return accessType;
	}
	
	public void setAccessType(String accessType)
	{
		this.accessType = accessType;
	}
	public void setPvc(String pvc)
	{
		this.pvc = pvc;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getDeviceserialnumber() {
		return deviceserialnumber;
	}
	public void setDeviceserialnumber(String deviceserialnumber) {
		this.deviceserialnumber = deviceserialnumber;
	}
	public String getOui() {
		return oui;
	}
	public void setOui(String oui) {
		this.oui = oui;
	}
	
	public String getBindPort()
	{
		return bindPort;
	}
	
	public void setBindPort(String bindPort)
	{
		this.bindPort = bindPort;
	}


	public String getPath()
	{
		return path;
	}


	public void setPath(String path)
	{
		this.path = path;
	}

}
