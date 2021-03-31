package com.linkage.module.gwms.config.act;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.LipossGlobals;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.config.bio.IptvBIO;
import com.linkage.module.gwms.config.bio.WanBIO;
import com.linkage.module.gwms.config.obj.BusinessSheetObj;
import com.linkage.module.gwms.config.obj.DevAndUserObj;
import com.linkage.module.gwms.obj.gw.MwbandOBJ;
import com.linkage.module.gwms.obj.gw.WanConnObj;
import com.linkage.module.gwms.obj.gw.WanConnSessObj;
import com.linkage.module.gwms.obj.gw.WanObj;
import com.opensymphony.xwork2.ActionSupport;

/**
 * 应用服务模块：配置上网参数ACT（网络应用）
 * @author gongsj
 * @date 2009-7-14
 */
public class BusinessSheetACT extends ActionSupport implements ServletRequestAware {
	
	/** sid */
	private static final long serialVersionUID = -7948213365992547983L;

	/** log */
	private static Logger logger = LoggerFactory.getLogger(BusinessSheetACT.class);

	/** device_id */
	private String deviceId;
	
	/** 工单类型 上网:1 或IPTV:2*/
	private String sheetType;
	
	/** 上行方式（接入方式）LAN上行或ADSL上行*/
	private String accessType;
	
	private String sessionType;
	private String servList;
	
	private String connType;
	private String vpi;
	private String vci;
	
	private String ipType;
	private String vlanId;
	
	private String natEnable;
	
	/** 多终端上网数 */
	private String num;
	
	private String username;
	private String password;
	
	private String ip;
	private String gateway;
	private String mask;
	private String dns;
	
	private String bindPort;
	
	
	private String wanId;
	private String wanConnId;
	private String wanConnSessId;
	
	/** 只读 */
	private String connStatus;
	
	/** 只读 */ 
	private String connError;
	
	private String deviceSn;
	
	private String userAccount;
	
	/** wanConfig的列表 */
	private List<Map<String, String>> wanConfigList;
	
	/** BIO */
	private WanBIO wanBio;
	
	/** BIO */
	private IptvBIO iptvBio;
	
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
	 * 增加一条上网连接，生成策略、调用预读
	 * @date 2009-7-15
	 */
	public String add() {
		
		BusinessSheetObj businessSheetObj = new BusinessSheetObj();
		businessSheetObj.setSheetType(sheetType);
		
		WanObj wanOBJ = new WanObj();
		wanOBJ.setAccessType(accessType);
		
		WanConnObj wanConnOBJ = new WanConnObj();
		wanConnOBJ.setDevice_id(deviceId);
		wanConnOBJ.setVpi_id(vpi);
		wanConnOBJ.setVci_id(vci);
		wanConnOBJ.setVlan_id(vlanId);
		
		WanConnSessObj wanConnSessOBJ = new WanConnSessObj();
		
		wanConnSessOBJ.setSessType(sessionType);
		wanConnSessOBJ.setServList(servList);
		
		wanConnSessOBJ.setConnType(connType);
		
		wanConnSessOBJ.setIpType(ipType);
		wanConnSessOBJ.setNatEnable(natEnable);
		
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
		
		if ("1".equals(sheetType)) {
			logger.debug("sheetType为1,上网工单");
			if (wanBio.addWanRelatedConn(accOId, wanOBJ, wanConnOBJ, wanConnSessOBJ)) {
				ajax = "true";
			} else {
				ajax = "false";
			}
		} else {
			logger.debug("sheetType为2,IPTV工单");
			ajax = iptvBio.addWanRelatedConn(accOId, wanOBJ, wanConnOBJ, wanConnSessOBJ).split(";")[0];
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
		
		if (wanBio.delWanConn(wanConnOBJ, LipossGlobals.getGw_Type(wanConnOBJ.getDevice_id()))) {
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
		wanOBJ.setAccessType(accessType);
		
		WanConnObj wanConnOBJ = new WanConnObj();
		wanConnOBJ.setDevice_id(deviceId);
		wanConnOBJ.setVpi_id(vpi);
		wanConnOBJ.setVci_id(vci);
		wanConnOBJ.setVlan_id(vlanId);
		
		WanConnSessObj wanConnSessOBJ = new WanConnSessObj();
		wanConnSessOBJ.setWanId(wanId);
		wanConnSessOBJ.setWanConnId(wanConnId);
		wanConnSessOBJ.setWanConnSessId(wanConnSessId);
		
		wanConnSessOBJ.setConnType(connType);
		wanConnSessOBJ.setBindPort(bindPort);
		wanConnSessOBJ.setServList(servList);
		wanConnSessOBJ.setStatus(connStatus);
		wanConnSessOBJ.setIp(ip);
		wanConnSessOBJ.setDns(dns);
		wanConnSessOBJ.setUsername(username);
		wanConnSessOBJ.setNatEnable(natEnable);
		wanConnSessOBJ.setConnError(connError);
		
		MwbandOBJ mwbandOBJ = new MwbandOBJ();
		mwbandOBJ.setTotalNumber(Integer.parseInt(num));
		
		if (wanBio.configWanConn(wanOBJ, wanConnOBJ, wanConnSessOBJ)) {
			ajax = "true";
		} else {
			ajax = "false";
		}
		
		return ajax;
	}
	
	/**
	 * 
	 * @author gongsj
	 * @date 2009-7-29
	 * @return
	 */
	public String search() {
		logger.debug("search()");
		
		logger.debug("序列号：{}，用户账号：{}", deviceSn, userAccount);
		
		if ((null == deviceSn && null == userAccount) || ("".equals(deviceSn.trim()) && "".equals(userAccount.trim()))) {
			logger.warn("查询时序列号和用户账号都为NULL，出错返回");
			ajax = "1";
			
		} else {
			
			DevAndUserObj devAndUserObj = wanBio.searchSheetInfo(deviceSn, userAccount);
			
			if (null == devAndUserObj) {
				ajax = "2";
				
			} else if (devAndUserObj.getDevNum() > 1) {
				ajax = "3";
			} else {
				
				this.deviceId = devAndUserObj.getDeviceId();
				
				logger.debug("deviceId---:{}", deviceId);
				
				ajax = deviceId;
			}
			
		}
		
		return "ajax";
	}
	
	/**
	 * get data.
	 * 
	 * @return
	 */
	public String execute() {
		
		logger.debug("execute()");
		
		logger.debug("deviceId={}", deviceId);
		
		DevAndUserObj devAndUserObj = null;
		
		if (null != deviceId && !"".equals(deviceId)) {
			devAndUserObj = wanBio.searchSheetInfo(deviceId);
			
			if (null != devAndUserObj) {
				this.deviceSn = devAndUserObj.getDeviceSn();
				this.userAccount = devAndUserObj.getUserAccount();
			}
		}
		
//		List<Map<String, String>> wanConfigList = wanBio.getData(deviceId);
//		
//		String accessType = wanBio.getAccessType();
//		
//		if (accessType == null) {
//			logger.warn("从设备上取得的上行方式为空，默认设为DSL");
//			accessType = "DSL";
//		}
//		this.accessType = accessType;
//		this.wanConfigList = wanConfigList;
		
		return SUCCESS;
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
		int rsint = wanBio.getSuperCorba(deviceId, 11);
		if ( rsint!= 1) {
			lanbool = true;
		}
		rsint = wanBio.getSuperCorba(deviceId, 12);
		if ( rsint!= 1) {
			wlanbool = true;
		}
		if(lanbool && wlanbool){
			ajax =  "端口获取失败！";
		}else{
			ajax = wanBio.getLanInter(deviceId,"0");
		}
		return "ajax";
	}
	
	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getVpi() {
		return vpi;
	}

	public void setVpi(String vpi) {
		this.vpi = vpi;
	}

	public String getVci() {
		return vci;
	}

	public void setVci(String vci) {
		this.vci = vci;
	}

	public String getVlanId() {
		return vlanId;
	}

	public void setVlanId(String vlanId) {
		this.vlanId = vlanId;
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

	public String getConnStatus() {
		return connStatus;
	}

	public void setConnStatus(String connStatus) {
		this.connStatus = connStatus;
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

	public String getNatEnable() {
		return natEnable;
	}

	public void setNatEnable(String natEnable) {
		this.natEnable = natEnable;
	}

	public String getConnError() {
		return connError;
	}

	public void setConnError(String connError) {
		this.connError = connError;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public String getConnType() {
		return connType;
	}

	public void setConnType(String connType) {
		this.connType = connType;
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


	public String getSessionType() {
		return sessionType;
	}


	public void setSessionType(String sessionType) {
		this.sessionType = sessionType;
	}


	public String getIpType() {
		return ipType;
	}


	public void setIpType(String ipType) {
		this.ipType = ipType;
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


	public String getAccessType() {
		return accessType;
	}


	public void setAccessType(String accessType) {
		this.accessType = accessType;
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

	public String getDeviceSn() {
		return deviceSn;
	}

	public void setDeviceSn(String deviceSn) {
		this.deviceSn = deviceSn;
	}

	public String getUserAccount() {
		return userAccount;
	}

	public void setUserAccount(String userAccount) {
		this.userAccount = userAccount;
	}

	public void setWanBio(WanBIO wanBio) {
		this.wanBio = wanBio;
	}

	public void setIptvBio(IptvBIO iptvBio) {
		this.iptvBio = iptvBio;
	}

	public String getSheetType() {
		return sheetType;
	}

	public void setSheetType(String sheetType) {
		this.sheetType = sheetType;
	}
	
	
}

