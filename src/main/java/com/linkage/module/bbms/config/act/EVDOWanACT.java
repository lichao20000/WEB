package com.linkage.module.bbms.config.act;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.system.UserRes;
import com.linkage.module.bbms.config.bio.EVDOWanBIO;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.obj.gw.WanConnObj;
import com.linkage.module.gwms.obj.gw.WanConnSessObj;
import com.linkage.module.gwms.obj.gw.WanObj;
import com.opensymphony.xwork2.ActionSupport;

/**
 * EVDO模块：配置上网参数ACT
 * @author 漆学启
 * @date 2009-10-13
 */
public class EVDOWanACT extends ActionSupport implements ServletRequestAware {
	
	/** sid */
	private static final long serialVersionUID = -7948213365992547983L;
	
	/** log */
	private static Logger logger = LoggerFactory.getLogger(EVDOWanACT.class);
	
	/** device_id */
	private String deviceId = null;
	
	private String servList = null;
	private String workMode = null;
	private String connMedia = null;
	private String connTrigger = null;
	private String dialNum = null;
	private String backupItfs = null;
	private String loadPercent = null;
	private String wanId = null;
	private String wanConnId = null;
	private String wanConnSessId = null;
	
	private String username = null;
	private String password = null;
	private String ip = null;
	private String gateway = null;
	private String mask = null;
	private String dns = null;
	private String bindPort = null;	
	
	/** wanConfig的列表 */
	private List<Map<String, String>> wanConfigList;
	
	/** BIO */
	private EVDOWanBIO bio;
	
	/** ajax */
	private String ajax = "";
	
	/** request */
	private HttpServletRequest request;

	/** session */
	private HttpSession session;
	
	/**
	 * 作为调用corba失败返回的信息
	 */
	private String corbaMsg = null;
	
	/**
	 * get data.
	 * 
	 * @return
	 */
	public String execute() {
		
		logger.debug("execute()");

		// SG
		/**
		 * 开发测试时，关闭设备采集，以便调试
		 */
		//int rsint = 1;
		int rsint = bio.getSuperCorba(deviceId, 2);
		if ( rsint != 1) {
			logger.warn("getData sg fail");
			this.corbaMsg = Global.G_Fault_Map.get(rsint).getFaultReason();
			
			if (null == this.corbaMsg) { 
				this.corbaMsg = Global.G_Fault_Map.get(100000).getFaultReason(); 
			}
			return SUCCESS;
		}
		
		List<Map<String, String>> wanConfigList = null;
		
		wanConfigList = bio.getDataByBBMS(deviceId);
		
		if(null == wanConfigList || wanConfigList.size()<1){
			this.corbaMsg = "该设备没有开通此业务!";
		}else{
			this.wanConfigList = wanConfigList;
		}
		
		return SUCCESS;
		
	}
	
	/**
	 * 增加一条上网连接，生成策略、调用预读
	 * @date 2009-7-15
	 */
	public String add() {
		
		WanObj wanOBJ = new WanObj();
		
		WanConnObj wanConnOBJ = new WanConnObj();
		wanConnOBJ.setDevice_id(deviceId);
		
		WanConnSessObj wanConnSessOBJ = new WanConnSessObj();
		
		wanConnSessOBJ.setUsername(username);
		wanConnSessOBJ.setPassword(password);
		
		wanConnSessOBJ.setIp(ip);
		wanConnSessOBJ.setGateway(gateway);
		wanConnSessOBJ.setMask(mask);
		wanConnSessOBJ.setDns(dns);
		
		wanConnSessOBJ.setBindPort(bindPort);
		
		session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		long accOId = curUser.getUser().getId();
		
		if (bio.addWanRelatedConn(accOId, wanOBJ, wanConnOBJ, wanConnSessOBJ)) {
			ajax = "true";
		} else {
			ajax = "false";
		}
		
		return "ajax";
	}
	
	/**
	 * 删除上网连接
	 * @author gongsj
	 * @date 2009-7-16
	 * @return
	 */
	public String del() {
		WanConnObj wanConnOBJ = new WanConnObj();
		wanConnOBJ.setDevice_id(deviceId);
		wanConnOBJ.setWan_id(wanId);
		wanConnOBJ.setWan_conn_id(wanConnId);
		
		if (bio.delWanConn(wanConnOBJ)) {
			ajax = "true";
		} else {
			ajax = "false";
		}
		
		return "ajax";
	}
	
	/**
	 * 配置
	 */
	public String config() {
		
		logger.debug("config()");
		
		WanObj wanOBJ = new WanObj();
		
		WanConnObj wanConnOBJ = new WanConnObj();
		
		WanConnSessObj wanConnSessOBJ = new WanConnSessObj();
		wanConnSessOBJ.setDeviceId(deviceId);
		wanConnSessOBJ.setWanId(wanId);
		wanConnSessOBJ.setWanConnId(wanConnId);
		wanConnSessOBJ.setWanConnSessId(wanConnSessId);
		
		wanConnSessOBJ.setBindPort(bindPort);
		wanConnSessOBJ.setServList(servList);
		wanConnSessOBJ.setIp(ip);
		wanConnSessOBJ.setDns(dns);
		wanConnSessOBJ.setUsername(username);
		
		if (bio.configWanConn(wanOBJ, wanConnOBJ, wanConnSessOBJ)) {
			ajax = "true";
		} else {
			ajax = "false";
		}
		
		return ajax;
		
	}

	/**
	 * 查询端口
	 * 
	 * @return
	 */
	public String addLanInit(){
		
		logger.debug("addWanInit()");
		boolean lanbool = false;
		boolean wlanbool = false;
		
		// SG
		int rsint = bio.getSuperCorba(deviceId, 11);
		if ( rsint!= 1) {
			lanbool = true;
		}
		rsint = bio.getSuperCorba(deviceId, 12);
		if ( rsint!= 1) {
			wlanbool = true;
		}
		if(lanbool && wlanbool){
			ajax =  "端口获取失败！";
		}else{
			ajax = bio.getLanInter(deviceId);
		}
		return "ajax";
	}
	
	/**
	 * set: BIO
	 * 
	 * @param bio
	 *            the bio to set
	 */
	public void setBio(EVDOWanBIO bio) {
		logger.debug("setBio(EVDOWanBIO)");

		this.bio = bio;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getBindPort() {
		return bindPort;
	}

	public void setBindPort(String bindPort) {
		this.bindPort = bindPort;
	}

	public String getServList() {
		return servList;
	}

	public void setServList(String servList) {
		this.servList = servList;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getDns() {
		return dns;
	}

	public void setDns(String dns) {
		this.dns = dns;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getWanId() {
		return wanId;
	}


	public void setWanId(String wanId) {
		this.wanId = wanId;
	}


	public String getWanConnId() {
		return wanConnId;
	}


	public void setWanConnId(String wanConnId) {
		this.wanConnId = wanConnId;
	}


	public String getWanConnSessId() {
		return wanConnSessId;
	}


	public void setWanConnSessId(String wanConnSessId) {
		this.wanConnSessId = wanConnSessId;
	}

	
	public List<Map<String, String>> getWanConfigList() {
		return wanConfigList;
	}

	public void setWanConfigList(List<Map<String, String>> wanConfigList) {
		this.wanConfigList = wanConfigList;
	}

	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public String getGateway() {
		return gateway;
	}


	public void setGateway(String gateway) {
		this.gateway = gateway;
	}


	public String getMask() {
		return mask;
	}


	public void setMask(String mask) {
		this.mask = mask;
	}

	/**
	 * get:ajax
	 * 
	 * @return the ajax
	 */
	public String getAjax() {
		logger.debug("getAjax()");

		return ajax;
	}

	/**
	 * set:ajax
	 * 
	 * @param ajax
	 *            the ajax to set
	 */
	public void setAjax(String ajax) {
		logger.debug("setAjax({})", ajax);

		this.ajax = ajax;
	}


	@Override
	public void setServletRequest(HttpServletRequest arg0) {
		this.request = arg0;
	}

	/**
	 * @return the corbaMsg
	 */
	public String getCorbaMsg() {
		return corbaMsg;
	}

	/**
	 * @param corbaMsg the corbaMsg to set
	 */
	public void setCorbaMsg(String corbaMsg) {
		this.corbaMsg = corbaMsg;
	}

	/**
	 * @return the backupItfs
	 */
	public String getBackupItfs() {
		return backupItfs;
	}

	/**
	 * @param backupItfs the backupItfs to set
	 */
	public void setBackupItfs(String backupItfs) {
		this.backupItfs = backupItfs;
	}

	/**
	 * @return the connMedia
	 */
	public String getConnMedia() {
		return connMedia;
	}

	/**
	 * @param connMedia the connMedia to set
	 */
	public void setConnMedia(String connMedia) {
		this.connMedia = connMedia;
	}

	/**
	 * @return the connTrigger
	 */
	public String getConnTrigger() {
		return connTrigger;
	}

	/**
	 * @param connTrigger the connTrigger to set
	 */
	public void setConnTrigger(String connTrigger) {
		this.connTrigger = connTrigger;
	}

	/**
	 * @return the dialNum
	 */
	public String getDialNum() {
		return dialNum;
	}

	/**
	 * @param dialNum the dialNum to set
	 */
	public void setDialNum(String dialNum) {
		this.dialNum = dialNum;
	}

	/**
	 * @return the loadPercent
	 */
	public String getLoadPercent() {
		return loadPercent;
	}

	/**
	 * @param loadPercent the loadPercent to set
	 */
	public void setLoadPercent(String loadPercent) {
		this.loadPercent = loadPercent;
	}

	/**
	 * @return the workMode
	 */
	public String getWorkMode() {
		return workMode;
	}

	/**
	 * @param workMode the workMode to set
	 */
	public void setWorkMode(String workMode) {
		this.workMode = workMode;
	}
	
}
