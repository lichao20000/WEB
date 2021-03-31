package com.linkage.module.gwms.config.act;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.config.bio.VoipBIO;
import com.linkage.module.gwms.obj.gw.MwbandOBJ;
import com.linkage.module.gwms.obj.gw.VoiceServiceProfileLineObj;
import com.linkage.module.gwms.obj.gw.VoiceServiceProfileObj;
import com.linkage.module.gwms.obj.gw.WanConnObj;
import com.linkage.module.gwms.obj.gw.WanConnSessObj;
import com.linkage.module.gwms.obj.gw.WanObj;
import com.linkage.module.gwms.obj.tr069.ParameValueOBJ;
import com.linkage.module.gwms.util.corba.ACSCorba;
import com.opensymphony.xwork2.ActionSupport;

/**
 * 应用服务模块：配置上网参数ACT（网络应用）
 * @author gongsj
 * @date 2009-7-14
 */
public class VoipACT extends ActionSupport implements ServletRequestAware {
	/** sid */
	private static final long serialVersionUID = -7948213365992547983L;

	/** log */
	private static Logger logger = LoggerFactory.getLogger(IptvACT.class);

	/** device_id */
	private String deviceId;
	
	private String gw_type = null;
	
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
	
	
	private String voipId = null;
	private String profId = null;
	private String lineId = null;
	
	private String proxServ = null;
	private String proxPort = null;
	private String proxServ2 = null;
	private String proxPort2 = null;
	private String enable = null;
	private String regiServ = null;
	private String regiPort = null;
	private String standRegiServ = null;
	private String standRegiPort = null;
	private String outBoundProxy = null;
	private String outBoundPort = null;
	private String standOutBoundProxy = null;
	private String standOutBoundPort = null;
	private String authUserName = null;
	private String authPassword = null;
	
	/** wanConfig的列表 */
	private List<Map<String, String>> wanConfigList;
	
	/** VOIP的列表 */
	private List<Map<String, String>> voipInfoList;
	
	/**
	 * VOIP协议类型0-IMS SIP,1-软交换 SIP,2-H.248
	 */
	private String voipProtocalTypeStr;
    
    /**
     * ACS Corba
     */
    private ACSCorba acsCorba = new ACSCorba(gw_type);
	
	/** BIO */
	private VoipBIO bio;
	
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
	 * voipInfoMsg
	 */
	private String voipInfoMsg = null;
	
	/**
	 * 是否同时增加wan连接和voip信息
	 * 1表示同时增加，0反之
	 */
	private String isAddWan = null;
	
	/**
	 * 增加一条IPTV连接，生成策略、调用预读
	 * @date 2009-7-15
	 */
	public String add() {
		
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
		
		
		//取得VOIP所需要的初始配置参数
		Map<String, String> voipInitParamMap = bio.getVoipSIP("0");
		if (null == voipInitParamMap || voipInitParamMap.isEmpty()) {
			//如果取值失败
			this.voipInfoMsg = "当前无VOIP初始配置信息！";
			logger.debug("gw_voip_init_param表中未取到数据！");
			return SUCCESS;
		}
		
		this.proxServ = voipInitParamMap.get("prox_server");
		this.proxPort = String.valueOf(voipInitParamMap.get("prox_port"));
		this.proxServ2 = voipInitParamMap.get("prox_server2");
		this.proxPort2 = String.valueOf(voipInitParamMap.get("prox_port2"));
		this.regiServ = voipInitParamMap.get("regi_serv");
		this.regiPort = String.valueOf(voipInitParamMap.get("regi_port"));
		this.standRegiServ = voipInitParamMap.get("stand_regi_serv");
		this.standRegiPort = String.valueOf(voipInitParamMap.get("stand_regi_port"));
		this.outBoundProxy = voipInitParamMap.get("out_bound_proxy");
		this.outBoundPort = String.valueOf(voipInitParamMap.get("out_bound_port"));
		this.standOutBoundProxy = voipInitParamMap.get("stand_out_bound_proxy");
		this.standOutBoundPort = String.valueOf(voipInitParamMap.get("stand_out_bound_port"));
		
		VoiceServiceProfileObj voipProfObj = new VoiceServiceProfileObj();
		voipProfObj.setProxServ(proxServ);
		voipProfObj.setProxPort(proxPort);
		voipProfObj.setProxServ2(proxServ2);
		voipProfObj.setProxPort2(proxPort2);
		voipProfObj.setRegiServ(regiServ);
		voipProfObj.setRegiPort(regiPort);
		voipProfObj.setStandRegiServ(standRegiServ);
		voipProfObj.setStandRegiPort(standRegiPort);
		voipProfObj.setOutBoundProxy(outBoundProxy);
		voipProfObj.setOutBoundPort(outBoundPort);
		voipProfObj.setStandOutBoundProxy(standOutBoundProxy);
		voipProfObj.setStandOutBoundPort(standOutBoundPort);
		
		VoiceServiceProfileLineObj voipProfLineObj = new VoiceServiceProfileLineObj();
		voipProfLineObj.setEnable(enable);
		voipProfLineObj.setUsername(authUserName);
		voipProfLineObj.setPassword(authPassword);
		
		session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		long accOId = curUser.getUser().getId();
		
		ajax = bio.addWanRelatedConn(isAddWan,accOId, wanOBJ, wanConnOBJ, wanConnSessOBJ, voipProfObj, voipProfLineObj);
			
		return "ajax";
	}
	
	/**
	 * 删除IPTV连接
	 * @author gongsj
	 * @date 2009-7-16
	 * @return
	 */
	public String del() {
		WanConnObj wanConnOBJ = new WanConnObj();
		wanConnOBJ.setDevice_id(deviceId);
		wanConnOBJ.setWan_id(wanId);
		wanConnOBJ.setWan_conn_id(wanConnId);
		
		if (bio.delWanConn(wanConnOBJ, gw_type)) {
			ajax = "true";
		} else {
			ajax = "false";
		}
		
		return "ajax";
	}
	
	/**
	 * 删除IPTV连接
	 * @author gongsj
	 * @date 2009-7-16
	 * @return
	 */
	public String delVoIP() {
		VoiceServiceProfileObj voiceObj = new VoiceServiceProfileObj();
		voiceObj.setDeviceId(deviceId);
		voiceObj.setProfId(profId);
		
		if (bio.delVoipInfo(voiceObj)) {
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
		
//		if (bio.configVoip(wanOBJ, wanConnOBJ, wanConnSessOBJ)) {
//			ajax = "true";
//		} else {
//			ajax = "false";
//		}
		
		return ajax;
	}
	
	
	
	/**
	 * get data.
	 * 
	 * @return
	 */
	public String execute() {
		logger.debug("execute()");
		acsCorba = new ACSCorba(gw_type);
		// SG
		//采集WAN链接
		int rsint = bio.getSuperCorba(deviceId, 2);

		if (rsint!= 1) {
			logger.warn("getData sg fail");
			this.corbaMsg = Global.G_Fault_Map.get(rsint).getFaultReason();
			
			if (null == this.corbaMsg) { 
				this.corbaMsg = Global.G_Fault_Map.get(100000).getFaultReason(); 
			}
			return SUCCESS;
		}
		
		//获取采集完的数据
		this.wanConfigList = bio.getData(deviceId, gw_type);
		
		if (null == this.wanConfigList) {
			//如果取值失败
			this.corbaMsg = "当前设备上无VOIP相关连接（PVC或VLANID）！";
			logger.debug("当前设备上无VOIP相关连接（PVC或VLANID）");
		}
		
		//获取上行方式
		String accessType = bio.getAccessType();
		if (accessType == null) {
			logger.warn("从设备上取得的上行方式为空，默认设为DSL");
			accessType = "DSL";
		}
		this.accessType = accessType;
		
		//采集VOIP链路信息
		//清除缓存
        String voipProtocalType = null;
        voipInfoList = null;
        
        //先获取VOIP协议类型
        //InternetGatewayDevice.Services.VoiceService.1.VoiceProfile.1.X_CT-COM_ServerType
        acsCorba = new ACSCorba(gw_type); // add by chenjie 2012-8-18 118行acsCorba的定义中gw_type没有值，这个地方重新赋值
        ArrayList<ParameValueOBJ> result = acsCorba
                .getValue(
                        deviceId,
                        "InternetGatewayDevice.Services.VoiceService.1.VoiceProfile.1.X_CT-COM_ServerType");
        if (null == result)
        {
            // 获取设备绑定用户的VOIP的开通协议类型(暂时不处理H.248的情况)
            String voipProtocal = bio
                    .getBssVoipSheetProtocalByDeviceId(deviceId);
            if (null != voipProtocal && !"".equals(voipProtocal.trim()))
            {
                voipProtocalType = voipProtocal;
            }
        }
        else
        {
            voipProtocalType = result.get(0).getValue();
        }

        // 无法获取，则显示无法获取VOIP信息
        if (null == voipProtocalType)
        {
            this.voipInfoMsg = "设备没有绑定用户或者绑定的用户没有开通VOIP业务";
        }
        else
        {
            // 转换显示信息
            switch (Integer.parseInt(voipProtocalType))
            {
                case 0:
                {
                    voipProtocalTypeStr = "IMS SIP";
                    break;
                }
                case 1:
                {
                    voipProtocalTypeStr = "软交换 SIP";
                    break;
                }
                case 2:
                {
                    voipProtocalTypeStr = "H.248";
                    break;
                }
            }

            //采集VOIP链接
            /*rsint = bio.getSuperCorba(deviceId, 34);
            if (rsint!= 1) {
                logger.warn("getData sg fail");
                this.corbaMsg = Global.G_Fault_Map.get(rsint).getFaultReason();
                
                if (null == this.corbaMsg) { 
                    this.corbaMsg = Global.G_Fault_Map.get(100000).getFaultReason(); 
                }
                return SUCCESS;
            }*/
            
            // 协议类型 H248  OR  IMS SIP
            if(2 == Integer.parseInt(voipProtocalType)){
            	this.voipInfoList =  bio.getVoipInfoH248(deviceId);
			}else{
				this.voipInfoList =  bio.getVoipInfo(deviceId);
			}
            
//            this.voipInfoList = bio.getVoipInfo(deviceId);
            if (null == this.voipInfoList || this.voipInfoList.size() < 1)
            {
                // 如果取值失败
                this.voipInfoMsg = "设备没有绑定用户或者绑定的用户没有开通VOIP业务!";
                logger.debug("设备没有绑定用户或者绑定的用户没有开通VOIP业务!");
            }
        }
		return SUCCESS;
	}


	/**
	 * 查询端口
	 * 
	 * @return
	 */
	public String addLanInit(){
		
		logger.debug("addLanInit()");
		boolean lanbool = false;
		boolean wlanbool = false;
		
		// SG
		int rsint = bio.getSuperCorba(deviceId, 11);
		if (rsint!= 1) {
			lanbool = true;
		}
		rsint = bio.getSuperCorba(deviceId, 12);
		if (rsint!= 1) {
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
	public void setBio(VoipBIO bio) {
		logger.debug("setBio(IptvBIO)");

		this.bio = bio;
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


	public String getVoipId() {
		return voipId;
	}

	public void setVoipId(String voipId) {
		this.voipId = voipId;
	}

	public String getProfId() {
		return profId;
	}

	public void setProfId(String profId) {
		this.profId = profId;
	}

	public String getLineId() {
		return lineId;
	}

	public void setLineId(String lineId) {
		this.lineId = lineId;
	}

	public String getProxServ() {
		return proxServ;
	}

	public void setProxServ(String proxServ) {
		this.proxServ = proxServ;
	}

	public String getProxPort() {
		return proxPort;
	}

	public void setProxPort(String proxPort) {
		this.proxPort = proxPort;
	}

	public String getProxServ2() {
		return proxServ2;
	}

	public void setProxServ2(String proxServ2) {
		this.proxServ2 = proxServ2;
	}

	public String getProxPort2() {
		return proxPort2;
	}

	public void setProxPort2(String proxPort2) {
		this.proxPort2 = proxPort2;
	}

	/**
	 * @return the enable
	 */
	public String getEnable() {
		return enable;
	}

	/**
	 * @param enable the enable to set
	 */
	public void setEnable(String enable) {
		this.enable = enable;
	}

	/**
	 * @return the authPassword
	 */
	public String getAuthPassword() {
		return authPassword;
	}

	/**
	 * @param authPassword the authPassword to set
	 */
	public void setAuthPassword(String authPassword) {
		this.authPassword = authPassword;
	}

	/**
	 * @return the authUserName
	 */
	public String getAuthUserName() {
		return authUserName;
	}

	/**
	 * @param authUserName the authUserName to set
	 */
	public void setAuthUserName(String authUserName) {
		this.authUserName = authUserName;
	}

	/**
	 * @return the outBoundPort
	 */
	public String getOutBoundPort() {
		return outBoundPort;
	}

	/**
	 * @param outBoundPort the outBoundPort to set
	 */
	public void setOutBoundPort(String outBoundPort) {
		this.outBoundPort = outBoundPort;
	}

	/**
	 * @return the outBoundProxy
	 */
	public String getOutBoundProxy() {
		return outBoundProxy;
	}

	/**
	 * @param outBoundProxy the outBoundProxy to set
	 */
	public void setOutBoundProxy(String outBoundProxy) {
		this.outBoundProxy = outBoundProxy;
	}

	/**
	 * @return the regiPort
	 */
	public String getRegiPort() {
		return regiPort;
	}

	/**
	 * @param regiPort the regiPort to set
	 */
	public void setRegiPort(String regiPort) {
		this.regiPort = regiPort;
	}

	/**
	 * @return the regiServ
	 */
	public String getRegiServ() {
		return regiServ;
	}

	/**
	 * @param regiServ the regiServ to set
	 */
	public void setRegiServ(String regiServ) {
		this.regiServ = regiServ;
	}

	/**
	 * @return the standOutBoundPort
	 */
	public String getStandOutBoundPort() {
		return standOutBoundPort;
	}

	/**
	 * @param standOutBoundPort the standOutBoundPort to set
	 */
	public void setStandOutBoundPort(String standOutBoundPort) {
		this.standOutBoundPort = standOutBoundPort;
	}

	/**
	 * @return the standOutBoundProxy
	 */
	public String getStandOutBoundProxy() {
		return standOutBoundProxy;
	}

	/**
	 * @param standOutBoundProxy the standOutBoundProxy to set
	 */
	public void setStandOutBoundProxy(String standOutBoundProxy) {
		this.standOutBoundProxy = standOutBoundProxy;
	}

	/**
	 * @return the standRegiPort
	 */
	public String getStandRegiPort() {
		return standRegiPort;
	}

	/**
	 * @param standRegiPort the standRegiPort to set
	 */
	public void setStandRegiPort(String standRegiPort) {
		this.standRegiPort = standRegiPort;
	}

	/**
	 * @return the standRegiServ
	 */
	public String getStandRegiServ() {
		return standRegiServ;
	}

	/**
	 * @param standRegiServ the standRegiServ to set
	 */
	public void setStandRegiServ(String standRegiServ) {
		this.standRegiServ = standRegiServ;
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
	 * @return the voipInfoList
	 */
	public List<Map<String, String>> getVoipInfoList() {
		return voipInfoList;
	}

	/**
	 * @param voipInfoList the voipInfoList to set
	 */
	public void setVoipInfoList(List<Map<String, String>> voipInfoList) {
		this.voipInfoList = voipInfoList;
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
	 * @return the isAddWan
	 */
	public String getIsAddWan() {
		return isAddWan;
	}

	/**
	 * @param isAddWan the isAddWan to set
	 */
	public void setIsAddWan(String isAddWan) {
		this.isAddWan = isAddWan;
	}

	/**
	 * @return the voipInfoMsg
	 */
	public String getVoipInfoMsg() {
		return voipInfoMsg;
	}

	/**
	 * @param voipInfoMsg the voipInfoMsg to set
	 */
	public void setVoipInfoMsg(String voipInfoMsg) {
		this.voipInfoMsg = voipInfoMsg;
	}

    /**
     * 获取voipProtocalTypeStr
     * @return String voipProtocalTypeStr
     */
    public String getVoipProtocalTypeStr()
    {
        return voipProtocalTypeStr;
    }

    /**
     * 设置voipProtocalTypeStr
     * @param String voipProtocalTypeStr
     */
    public void setVoipProtocalTypeStr(String voipProtocalTypeStr)
    {
        this.voipProtocalTypeStr = voipProtocalTypeStr;
    }

	public String getGw_type() {
		return gw_type;
	}

	public void setGw_type(String gw_type) {
		this.gw_type = gw_type;
	}
	
}
