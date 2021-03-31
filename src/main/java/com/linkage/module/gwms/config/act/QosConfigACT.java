package com.linkage.module.gwms.config.act;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.LipossGlobals;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.config.bio.QosConfigBIO;
import com.linkage.module.gwms.obj.gw.LanEthObj;
import com.linkage.module.gwms.obj.gw.QoSOBJ;
import com.linkage.module.gwms.util.StringUtil;

/**
 * ACT: QoS config.
 * 
 * @author Jason(3412),alex(yanhj@)
 * @date 2009-7-20
 */
@SuppressWarnings("unchecked")
public class QosConfigACT implements ServletRequestAware {

	private static Logger logger = LoggerFactory
			.getLogger(QosConfigACT.class);

	/** request */
	private HttpServletRequest request;
	
	
	private String gw_type = null;

	/** session */
	private HttpSession session;

	private String qosType;
	// 设备ID
	private String deviceId;
	// Qos队列
	private String queue;
	// SSID名称|结点
	private String ssidParam;
	// Iptv绑定端口
	private String iptvInter;
	// Qos模板ID
	private String tempId;
	// List
	private List qosList;
	/** 结果 */
	private int resultId = -9;
	// ajax
	private String ajax;
	// QosBIO
	private QosConfigBIO qosBio;
	/** qos模式 */
	private String modeIptv = "0";
	/** qos模式 */
	private String modeVoip = "0";
	
	/** jx_dx优先级顺序*/
	private String qosMode = "";
	private String modeInternet = "";
	private String modeTr069 = "";

	/**
	 * action methods
	 * 
	 * @return
	 */
	public String execute() {
		logger.debug("execute()");

		if ("ssid".equals(qosType)) {
			return "qosSsid";
		} else if ("iptv".equals(qosType)) {
			return "qosIptv";
		} else if ("voip".equals(qosType)) {
			return "qosVoip";
		} else if ("serv".equals(qosType)) {
			this.getServQos();

			return "qosServ";
		}

		return "success";
	}

	/**
	 * Qos列表
	 */
	public String qosConfigList() {
		logger.debug("qosConfigList()");
		// 从数据库中获取该设备已经配置Qos的列表
		qosList = qosBio.getQosConfigList(deviceId, tempId);

		return "qosList";
	}

	/**
	 * IPTV的Qos下发
	 */
	public String iptvQosConfig() {
		logger.debug("iptvQosConfig()");

		session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		long accOId = curUser.getUser().getId();

		// Lan2口
		LanEthObj lanEthObj = new LanEthObj();
		lanEthObj.setLanid(1);
		lanEthObj.setLanEthid(StringUtil.getIntegerValue(iptvInter));
		// IPTV的Qos下发
		String[] iptvLanPort = new String[1];
		String[] aliaIptv = new String[1];
		// iptv lan2口
		iptvLanPort[0] = lanEthObj.getLanInterface();
		aliaIptv[0] = "Lan" + iptvInter;
		QoSOBJ qosObj = qosBio.genIptvQosObj(iptvLanPort, queue, aliaIptv);
		long sId = qosBio.saveQosInfo(deviceId, accOId, qosObj, 2);
		if (qosBio.qosConfig(sId,gw_type) == true) {
			this.resultId = 0;
			ajax = "通知后台成功！";
		} else {
			this.resultId = -9;
			ajax = "通知后台失败！";
		}

		return "ajax";
	}

	/**
	 * VOIP的Qos下发
	 */
	public String voipQosConfig() {
		logger.debug("voipQosConfig()");

		session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		long accOId = curUser.getUser().getId();
		// IPTV的Qos下发
		QoSOBJ qosObj = qosBio.genVoipQosObj();
		long sId = qosBio.saveQosInfo(deviceId, accOId, qosObj, 3);
		if (qosBio.qosConfig(sId,gw_type) == true) {
			this.resultId = 0;
			ajax = "通知后台成功！";
		} else {
			this.resultId = -9;
			ajax = "通知后台失败！";
		}

		return "ajax";
	}

	/**
	 * 业务QoS
	 */
	public String servQosConfig() {
		logger.debug("servQosConfig()");

		session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		long accOId = curUser.getUser().getId();

		QoSOBJ qosObj = qosBio.genQosServ();
		String mode = "";
		if(Global.HBDX.equals(Global.instAreaShortName)){
			mode = "TR069";
			if ("1".equals(this.modeVoip) == true) {
				mode += ",VOIP";
			}
			if ("1".equals(this.modeIptv) == true) {
				mode += ",IPTV";
			}
			mode += ",INTERNET"; 
		}else if(Global.JXDX.equals(Global.instAreaShortName)){
			mode = this.qosMode;
		}else{
			mode = "INTERNET,TR069";
			if ("1".equals(this.modeVoip) == true) {
				mode += ",VOIP";
			}
			if ("1".equals(this.modeIptv) == true) {
				mode += ",IPTV";
			}
		}
		logger.warn("mode{}:", mode);
		qosObj.setMode(mode);

		long sId = qosBio.saveQosInfo(deviceId, accOId, qosObj, 3);

		logger.warn("sId{}:", sId);
		boolean rtn = qosBio.qosConfig(sId,gw_type);
		
		logger.warn("rtn{}:", rtn);
		if (rtn == true) {
			this.resultId = 0;
			ajax = "通知后台成功！";
		} else {
			this.resultId = -9;
			ajax = "通知后台失败！";
		}
		
		logger.warn("ajax{}:", ajax);
		return "ajax";
	}

	/**
	 * 业务QoS
	 */
	public String getServQos() {
		logger.debug("getServQos()");

		// 江西电信modeIptv、modeVoip默认为空
		if (Global.JXDX.equals(Global.instAreaShortName)){
			this.modeIptv = "";
			this.modeVoip = "";
		}
		QoSOBJ qosObj = qosBio.getData(deviceId);
		if (qosObj != null) {
			String tmp = qosObj.getMode();

			if (tmp == null) {
				logger.warn("[{}]:getServQos tmp == null", this.deviceId);
			} else {
				tmp = tmp.toUpperCase();
				
				// jx_电信
				if(Global.JXDX.equals(Global.instAreaShortName)){					
					qosModePriority(tmp);
				}
				else{
					if (tmp.indexOf("VOIP") >= 0) {
						this.modeVoip = "1";
					}

					if (tmp.indexOf("IPTV") >= 0) {
						this.modeIptv = "1";
					}
				}				
			}

		} else {
			logger.warn("[{}]:get data Error", this.deviceId);
		}

		ajax = qosBio.getDesc();

		return "ajax";
	}
	
	/**
	 * jx_dx优先级
	 * @param mode
	 */
	private void qosModePriority(String mode){
		String[] qosModeArr = mode.split(",");
		int modeLength = qosModeArr.length;		
		for(int modeIndex=0; modeIndex<modeLength;modeIndex++){
			if("".equals(this.qosMode)){
				this.qosMode += qosModeArr[modeIndex];
			}
			else{
				this.qosMode += ">" + qosModeArr[modeIndex];
			}
			if("INTERNET".equals(qosModeArr[modeIndex])){
				this.modeInternet = String.valueOf(modeIndex + 1);
			}
			if("TR069".equals(qosModeArr[modeIndex])){
				this.modeTr069 = String.valueOf(modeIndex + 1);
			}
			if("IPTV".equals(qosModeArr[modeIndex])){
				this.modeIptv = String.valueOf(modeIndex + 1);
			}
			if("VOIP".equals(qosModeArr[modeIndex])){
				this.modeVoip = String.valueOf(modeIndex + 1);
			}
		}
	} 

	/**
	 * SSID的Qos下发
	 */
	public String ssidQosConfig() {
		logger.debug("ssidQosConfig()");

		session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		long accOId = curUser.getUser().getId();

		// SSID的Qos下发
		logger.debug("ssidParam:{}", ssidParam);
		if (false == StringUtil.IsEmpty(ssidParam)) {
			String[] param = ssidParam.split("\\$");
			int len = param.length;
			String[] aliaSsid = new String[len];
			String[] interSsid = new String[len];
			for (int i = 0; i < len; i++) {
				aliaSsid[i] = param[i].split("\\|")[0];
				interSsid[i] = param[i].split("\\|")[1];
			}
			QoSOBJ qosObj = qosBio.genSsidQosObj(interSsid, queue, aliaSsid);
			long sId = qosBio.saveQosInfo(deviceId, accOId, qosObj, 1);
			if (qosBio.qosConfig(sId, gw_type) == true) {
				this.resultId = 0;
				ajax = "通知后台成功！";
			} else {
				this.resultId = -9;
				ajax = "通知后台失败！";
			}
		} else {
			this.resultId = -9;
			ajax = "通知后台失败！";
		}

		return "ajax";
	}

	/**
	 * 采集无线结点信息
	 */
	public String gatherWlan() {
		logger.debug("gatherWlan()");
		// ajax = qosBio.gatherWlan(deviceId);
		ajax = qosBio.getWlanFromDb(deviceId);

		return "ajax";
	}

	/** getter, setter* */

	public void setQosBio(QosConfigBIO qosBio) {
		this.qosBio = qosBio;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public String getQueue() {
		return queue;
	}

	public void setQueue(String queue) {
		this.queue = queue;
	}

	public String getSsidParam() {
		return ssidParam;
	}

	public void setSsidParam(String ssid) {
		this.ssidParam = ssid;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getTempId() {
		return tempId;
	}

	public void setTempId(String tempId) {
		this.tempId = tempId;
	}

	public String getQosMode() {
		return qosMode;
	}

	public void setQosMode(String qosMode) {
		this.qosMode = qosMode;
	}

	public String getModeInternet() {
		return modeInternet;
	}

	public void setModeInternet(String modeInternet) {
		this.modeInternet = modeInternet;
	}

	public String getModeTr069() {
		return modeTr069;
	}

	public void setModeTr069(String modeTr069) {
		this.modeTr069 = modeTr069;
	}

	public String getAjax() {
		logger.debug("getAjax()");

//		if (this.resultId == 0 || this.resultId == 1) {
//			ajax = "<font color='green'>" + ajax + "</font>";
//		} else {
//			ajax = "<font color='red'>" + ajax + "</font>";
//		}

		return ajax;
	}

	public String getQosType() {
		return qosType;
	}

	public void setQosType(String qosType) {
		this.qosType = qosType;
	}

	public void setIptvInter(String iptvInter) {
		this.iptvInter = iptvInter;
	}

	public List getQosList() {
		return qosList;
	}

	/*
	 * set request
	 * 
	 * @see org.apache.struts2.interceptor.ServletRequestAware#setServletRequest(javax.servlet.http.HttpServletRequest)
	 */
	@Override
	public void setServletRequest(HttpServletRequest arg0) {
		logger.debug("setServletRequest(HttpServletRequest)");

		this.request = arg0;
	}

	/**
	 * get:iptv
	 * 
	 * @return the modeIptv
	 */
	public String getModeIptv() {
		logger.debug("setServletRequest(HttpServletRequest)");

		return modeIptv;
	}

	/**
	 * get:voip
	 * 
	 * @return the modeVoip
	 */
	public String getModeVoip() {
		logger.debug("getModeVoip()");

		return modeVoip;
	}

	/**
	 * set:iptv
	 * 
	 * @param modeIptv
	 *            the modeIptv to set
	 */
	public void setModeIptv(String modeIptv) {
		logger.debug("setModeIptv({})", modeIptv);

		this.modeIptv = modeIptv;
	}

	/**
	 * set:voip
	 * 
	 * @param modeVoip
	 *            the modeVoip to set
	 */
	public void setModeVoip(String modeVoip) {
		logger.debug("setModeVoip({})", modeVoip);

		this.modeVoip = modeVoip;
	}

	/**
	 * get:resultId
	 * 
	 * @return the resultId
	 */
	public int getResultId() {
		logger.debug("getResultId()");

		return resultId;
	}

	/**
	 * set:resultId
	 * 
	 * @param resultId
	 *            the resultId to set
	 */
	public void setResultId(int resultId) {
		logger.debug("setResultId({})", resultId);

		this.resultId = resultId;
	}

	public String getGw_type() {
		return gw_type;
	}

	public void setGw_type(String gw_type) {
		this.gw_type = gw_type;
	}

}
